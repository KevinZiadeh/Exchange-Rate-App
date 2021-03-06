from statistics import median, mode, stdev, mean

from flask import request, jsonify, abort, Blueprint, Response
import datetime

from api.functions import PredictFutureRate
from model.transaction import Transaction

app_rate = Blueprint('app_rate', __name__)

#added number of days as an input
@app_rate.route('/exchangeRate', methods=['GET'])
def exchangeRate():
 """ Returns the average exchange rates (USD to LBP and LBP to USD) of the last 10 days .
       ---
       responses:
         200:
           description:  It returns the exchange rate of both usd_to_lbp and lbp_to_usd of the last 10 days.
 """
 #added as argument the num of days
 START_DATE= datetime.datetime.now() - datetime.timedelta(days=10)
 END_DATE=datetime.datetime.now()
 usd_to_lbp=Transaction.query.filter(Transaction.added_date.between(START_DATE, END_DATE),Transaction.usd_to_lbp == True).all()
 lbp_to_usd = Transaction.query.filter(Transaction.added_date.between(START_DATE, END_DATE),Transaction.usd_to_lbp == False).all()
 avg_usd_lbp="Not available yet"
 avg_lbp_usd="Not available yet"
 sum1=0
 sum2=0
 len1=0
 len2=0
 for trans in usd_to_lbp:
  ratio=trans.lbp_amount/trans.usd_amount
  sum1=ratio + sum1
  len1 +=1

 if (len1 != 0):
  avg_usd_lbp=sum1/len1

 for trans in lbp_to_usd:
  ratio=trans.lbp_amount/trans.usd_amount
  sum2=ratio + sum2
  len2 +=1
 if (len2!=0):
  avg_lbp_usd=sum2/len2

 return jsonify(usd_to_lbp = avg_usd_lbp ,lbp_to_usd= avg_lbp_usd)

#NEW: Stat +prediction
@app_rate.route('/statistics', methods=['GET'])
def stats():
 """  Provide users with statistics and insights (predictions) of the exchange rate for the transactions registered for 10 days.
     ---
     responses:
       200:
          description: Returns json file of Maximum, Minimum, Median, Mode, and Standard deviation of the exchange rates (usd to lbp and lbp to usd).

     """

 START_DATE = datetime.datetime.now() - datetime.timedelta(days=10)
 END_DATE = datetime.datetime.now()
 usd_to_lbp = Transaction.query.filter(Transaction.added_date.between(START_DATE, END_DATE),
                                       Transaction.usd_to_lbp == True).all()
 lbp_to_usd = Transaction.query.filter(Transaction.added_date.between(START_DATE, END_DATE),
                                       Transaction.usd_to_lbp == False).all()

 #store rates of usd to lbp in a list
 rates_usd_to_lbp=[]
 for trans in usd_to_lbp:
  if trans.usd_amount!=0:
   rates_usd_to_lbp.append(trans.lbp_amount/trans.usd_amount)

 #stores rates of lbp to usd in a list
 rates_lbp_to_usd = []
 for trans in lbp_to_usd:
  if trans.usd_amount != 0:
   rates_lbp_to_usd.append(trans.lbp_amount / trans.usd_amount)


 #now some statistics:
 stat={}
 if (len(rates_lbp_to_usd)>0)  :
  stat['max_lbp_to_usd']=max(rates_lbp_to_usd)
  stat['min_lbp_to_usd'] = min(rates_lbp_to_usd)
  stat['median_lbp_to_usd'] = median(rates_lbp_to_usd)
  stat['mode_lbp_to_usd'] = mode(rates_lbp_to_usd)
  stat['std_lbp_to_usd'] = stdev(rates_lbp_to_usd)
  future_lbp_to_usd = PredictFutureRate(rates_lbp_to_usd)
  stat['predicted_lbp_to_usd'] = future_lbp_to_usd

 if (len(rates_usd_to_lbp)>0):
  stat['max_usd_to_lbp'] = max(rates_usd_to_lbp)
  stat['min_usd_to_lbp'] = min(rates_usd_to_lbp)
  stat['median_usd_to_lbp'] = median(rates_usd_to_lbp)
  stat['mode_usd_to_lbp'] = mode(rates_usd_to_lbp)
  stat['std_usd_to_lbp'] = stdev(rates_usd_to_lbp)
  future_usd_to_lbp=PredictFutureRate(rates_usd_to_lbp)
  stat['predicted_usd_to_lbp']=future_usd_to_lbp

 if (len(rates_usd_to_lbp)==0):
  stat['max_usd_to_lbp'] = "not available"
  stat['min_usd_to_lbp'] = "not available"
  stat['median_usd_to_lbp'] = "not available"
  stat['mode_usd_to_lbp'] = "not available"
  stat['std_usd_to_lbp'] = "not available"
  stat['predicted_usd_to_lbp'] = "not available"

 if (len(rates_lbp_to_usd) == 0):
  stat['max_lbp_to_usd'] = "not available"
  stat['min_lbp_to_usd'] = "not available"
  stat['median_lbp_to_usd'] = "not available"
  stat['mode_lbp_to_usd'] = "not available"
  stat['std_lbp_to_usd'] = "not available"
  stat['predicted_lbp_to_usd'] = "not available"


 return jsonify(stat)



@app_rate.route('/graph', methods=['GET'])
def graph():
 """ Returns the moving average exchange rate (of the past 10 days) for each day during 10 days
    ---
    responses:
      200:
        description: A json containing the moving average rate of lbp to usd, usd to lbp each of the 10days
    """
 START_DATE = datetime.datetime.now() - datetime.timedelta(days=20)
 END_DATE = datetime.datetime.now()

 usd_to_lbp = Transaction.query.filter(Transaction.added_date.between(START_DATE, END_DATE),
                                       Transaction.usd_to_lbp == True).all()
 lbp_to_usd = Transaction.query.filter(Transaction.added_date.between(START_DATE, END_DATE),
                                       Transaction.usd_to_lbp == False).all()

 date=[]
 mean_rate_lbp_to_usd=[]
 mean_rate_usd_to_lbp = []
 for i in range(9,-1,-1):
  date.append((datetime.datetime.now() - datetime.timedelta(days=i)).strftime("%Y %b %d"))
  rates_lbp_to_usd = []
  rates_usd_to_lbp = []

  for trans in usd_to_lbp:
    if (trans.added_date.strftime("%Y %b %d") <= (datetime.datetime.now() - datetime.timedelta(days=i)).strftime("%Y %b %d")) and (trans.added_date - (datetime.datetime.now() - datetime.timedelta(days=i))).days >= -10:
     rates_usd_to_lbp.append(trans.lbp_amount / trans.usd_amount)

  for trans in lbp_to_usd:
    if (trans.added_date.strftime("%Y %b %d") <= (datetime.datetime.now() - datetime.timedelta(days=i)).strftime("%Y %b %d")) and (trans.added_date - (datetime.datetime.now() - datetime.timedelta(days=i))).days >= -10:
     rates_lbp_to_usd.append(trans.lbp_amount / trans.usd_amount)

  if len(rates_lbp_to_usd)>0:
   mean_rate_lbp_to_usd.append(mean(rates_lbp_to_usd))
  else:
   mean_rate_lbp_to_usd.append(-1)

  if len(rates_usd_to_lbp) > 0:
   print(rates_usd_to_lbp)
   mean_rate_usd_to_lbp.append(mean(rates_usd_to_lbp))
  else:
   mean_rate_usd_to_lbp.append(-1)

 return jsonify(date =date,mean_rate_lbp_to_usd=mean_rate_lbp_to_usd,mean_rate_usd_to_lbp=mean_rate_usd_to_lbp)