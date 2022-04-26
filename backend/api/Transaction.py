from flask import request, jsonify, abort, Blueprint, Response
from app import db
from api.functions import extract_auth_token, decode_token
from model.transaction import Transaction, transaction_schema, transactions_schema
from model.user import User

app_transaction = Blueprint('app_transaction', __name__)

@app_transaction.route('/transaction', methods=['POST'])
def transaction():

 usd_amount= request.json["usd_amount"]
 lbp_amount = request.json["lbp_amount"]
 usd_to_lbp = request.json["usd_to_lbp"]
 user_id=None



 print(usd_amount)
 print(lbp_amount)
 print(usd_to_lbp)
 token = extract_auth_token(request)

 print(token)

 if(token):
   try :
      user_id = decode_token(token)
   except:
      abort(403)

 if (usd_amount is not None) and (usd_amount!= 0) and (lbp_amount is not None) and (lbp_amount!=0) and (usd_to_lbp is not None):
    newTransaction = Transaction(
       usd_amount = usd_amount,
       lbp_amount = lbp_amount,
       usd_to_lbp = usd_to_lbp,
       user_id = user_id,
       receiver_id = None
    )


    db.session.add(newTransaction)
    db.session.commit()

    return jsonify(transaction_schema.dump(newTransaction))

 return "Invalid Input"

@app_transaction.route('/transaction', methods=['GET'])
def getTransactions():
 token = extract_auth_token(request)
 user_id = None
 if(token):
   try :
      user_id = decode_token(token)
      transs= Transaction.query.filter_by(user_id=user_id)
      return jsonify(transactions_schema.dump(transs))
   except:
      abort(403)
 return "no token"


@app_transaction.route('/exchangeuser', methods=['POST'])
def exchangeuser():

 usd_amount= request.json["usd_amount"]
 lbp_amount = request.json["lbp_amount"]
 usd_to_lbp = request.json["usd_to_lbp"]
 receiver_username = request.json["receiver_username"]

 user_id = None

 token = extract_auth_token(request)

 print(token)

 if(token):
   try :
      user_id = decode_token(token)
   except:
      abort(403)
 else:
     abort(403)

 try:
    receiver_id = User.query.filter_by(user_name = receiver_username).all()[0].id
 except:
     return "invalid user to give"

 if (usd_amount is not None) and (usd_amount!= 0) and (lbp_amount is not None) and (lbp_amount!=0) and (usd_to_lbp is not None):
    newTransaction = Transaction(
       usd_amount = usd_amount,
       lbp_amount = lbp_amount,
       usd_to_lbp = usd_to_lbp,
       user_id = user_id,
       receiver_id = receiver_id
    )

    db.session.add(newTransaction)
    db.session.commit()

    return jsonify(transaction_schema.dump(newTransaction))

 return "Invalid Input"


@app_transaction.route('/exchangeuser', methods=['GET'])
def getexchangeuser():
 token = extract_auth_token(request)
 user_id = None
 if(token):
   try :
      user_id = decode_token(token)
      give= Transaction.query.filter_by(user_id=user_id)
      print(give)
      receive = Transaction.query.filter_by(receiver_id=user_id)
      d = {}
      d['give'] = transactions_schema.dump(give)
      d['receive'] = transactions_schema.dump(receive)
      return jsonify((d))
   except:
      abort(403)
 return "no token"
