from statistics import median, mode, stdev

from flask import request, jsonify, abort, Blueprint, Response
import datetime

from .functions import PredictFutureRate
from ..model.transaction import Transaction

app_rate = Blueprint('app_rate', __name__)

#added number of days as an input
@app_rate.route('/exchangeRate/<int:num_day>/', methods=['GET'])
def exchangeRate(num_day):
 #added as argument the num of days
 START_DATE= datetime.datetime.now() - datetime.timedelta(days=num_day)
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
@app_rate.route('/statistics/<int:num_day>/', methods=['GET'])
def stats(num_day):
 START_DATE = datetime.datetime.now() - datetime.timedelta(days=num_day)
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
 if (len(rates_lbp_to_usd)>0) and(len(rates_usd_to_lbp)>0) :
  stat['max_lbp_to_usd']=max(rates_lbp_to_usd)
  stat['min_lbp_to_usd'] = min(rates_lbp_to_usd)
  stat['median_lbp_to_usd'] = median(rates_lbp_to_usd)
  stat['mode_lbp_to_usd'] = mode(rates_lbp_to_usd)
  stat['std_lbp_to_usd'] = stdev(rates_lbp_to_usd)

  stat['max_usd_to_lbp'] = max(rates_usd_to_lbp)
  stat['min_usd_to_lbp'] = min(rates_usd_to_lbp)
  stat['median_usd_to_lbp'] = median(rates_usd_to_lbp)
  stat['mode_usd_to_lbp'] = mode(rates_usd_to_lbp)
  stat['std_usd_to_lbp'] = stdev(rates_usd_to_lbp)

  #insights/predicting the next rate given all rates of the time lapse given
  future_usd_to_lbp=PredictFutureRate(rates_usd_to_lbp)
  future_lbp_to_usd = PredictFutureRate(rates_lbp_to_usd)
  stat['predicted_usd_to_lbp']=future_usd_to_lbp
  stat['predicted_lbp_to_usd']=future_lbp_to_usd

  return jsonify(stat)





