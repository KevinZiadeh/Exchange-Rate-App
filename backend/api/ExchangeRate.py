from flask import request, jsonify, abort, Blueprint, Response
import datetime
from ..model.transaction import Transaction

app_rate = Blueprint('app_rate', __name__)


@app_rate.route('/exchangeRate', methods=['GET'])
def exchangeRate():
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

