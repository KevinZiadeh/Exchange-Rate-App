from flask import request, jsonify, abort, Blueprint, Response
from ..app import db
from .functions import extract_auth_token, decode_token
from ..model.transaction import Transaction, transaction_schema, transactions_schema

app_transaction = Blueprint('app_transaction', __name__)


@app_transaction.route('/transaction', methods=['POST'])
def transaction():
 usd_amount= request.json["usd_amount"]
 lbp_amount = request.json["lbp_amount"]
 usd_to_lbp = request.json["usd_to_lbp"]
 user_id=None

 token = extract_auth_token(request)

 print(token)

 if(token):
   try :
      user_id = decode_token(token)
   except:
      abort(403)

 if usd_amount is not None and lbp_amount is not None and usd_to_lbp is not None:
    newTransaction = Transaction(
       usd_amount = usd_amount,
       lbp_amount = lbp_amount,
       usd_to_lbp = usd_to_lbp,
       user_id = user_id
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
