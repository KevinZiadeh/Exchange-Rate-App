from flask import request, jsonify, abort, Blueprint, Response
from app import db
from api.functions import extract_auth_token, decode_token
from model.transaction import Transaction, transaction_schema, transactions_schema
from model.user import User

app_transaction = Blueprint('app_transaction', __name__)

@app_transaction.route('/transaction', methods=['POST'])
def transaction():
 """ Add a new transaction, with or without a user being logged in
        ---
        parameters:
          - name: usd_amount
            in: body
            type : number
            example: 1
            required: true
          - name: lbp_amount
            in: body
            type : number
            example: 27000
            required: true
          - name : usd_to_lbp
            in : body
            type : boolean
            example : "true"
            required : true
            description : true for exchange from USD to LBP, false for exchange from LBP to USD.
          - name : token
            in : authorization
            type : Bearer Token
            required : false
            description: if the user is logged in, then token must be passed.

        responses:
          200:
            description: The transaction is added as a json
          400:
            description : The input is invalid
          403:
            description: token wrong
 """
 try:
     usd_amount= request.json["usd_amount"]
     lbp_amount = request.json["lbp_amount"]
     usd_to_lbp = request.json["usd_to_lbp"]
     user_id=None
 except:
     abort(400)

 token = extract_auth_token(request)

 print(token)

 if(token):
   try :
      user_id = decode_token(token)
   except:
      abort(403)

 user_name = User.query.filter_by(id=user_id).all()[0].user_name
 if (usd_amount is not None) and (usd_amount!= 0) and (lbp_amount is not None) and (lbp_amount!=0) and (usd_to_lbp is not None):

    newTransaction = Transaction(
       usd_amount = usd_amount,
       lbp_amount = lbp_amount,
       usd_to_lbp = usd_to_lbp,
       user_name = user_name,
       receiver_name = None,
       sender_name=None
    )

    db.session.add(newTransaction)
    db.session.commit()
    return jsonify(transaction_schema.dump(newTransaction))
 abort(400)

@app_transaction.route('/transaction', methods=['GET'])
def getTransactions():
 """ Returns all transactions of the user logged in
       ---
       parameters:
         - name : token
           in : authorization
           type : Bearer Token
           required : true
       responses:
         200:
           description:  We receive a json file of all transactions the user did.
         403:
           description:  token wrong.
 """

 token = extract_auth_token(request)
 user_id = None
 if(token):
   try :
      user_id = decode_token(token)
      user_name = User.query.filter_by(id=user_id).all()[0].user_name
      transs= Transaction.query.filter_by(user_name=user_name)
      return jsonify(transactions_schema.dump(transs))
   except:
      abort(403)
 abort(403)


@app_transaction.route('/exchangeuser', methods=['POST'])
def exchangeuser():
 """ Add a new transaction between the user logged in and another user
        ---
        parameters:
          - name: usd_amount
            in: body
            type : number
            example: 1
            required: true
          - name: lbp_amount
            in: body
            type : number
            example: 27000
            required: true
          - name : usd_to_lbp
            in : body
            type : boolean
            example : "true"
            required : true
            description : true for exchange from USD to LBP, false for exchange from LBP to USD.
          - name : token
            in : authorization
            type : Bearer Token
            required : true
          - name: receiver_name
            in: body
            type : string
            example: "sacha"
            required: true

        responses:
          200:
            description: The transaction added as a json.
          400:
            description : The input is invalid. Make sure you have passed all input correctly.
          403:
            description: token wrong
        """
 try:

     usd_amount= request.json["usd_amount"]
     lbp_amount = request.json["lbp_amount"]
     usd_to_lbp = request.json["usd_to_lbp"]
     receiver_name = request.json["receiver_name"]
 except:
     abort(400)

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
    receiver_id = User.query.filter_by(user_name = receiver_name).all()[0].id

    sender_name= User.query.filter_by(id = user_id).all()[0].user_name

 except:
     abort(400)
         #"invalid user to give"

 if receiver_id==user_id:
     abort(400) #"can't exchange with yourself"

 user_name = User.query.filter_by(id=user_id).all()[0].user_name

 if (usd_amount is not None) and (usd_amount!= 0) and (lbp_amount is not None) and (lbp_amount!=0) and (usd_to_lbp is not None) :
    newTransaction = Transaction(
       usd_amount = usd_amount,
       lbp_amount = lbp_amount,
       usd_to_lbp = usd_to_lbp,
       user_name = user_name,
       receiver_name = receiver_name,
       sender_name=sender_name
    )

    db.session.add(newTransaction)
    db.session.commit()

    return jsonify(transaction_schema.dump(newTransaction))

 abort(400)

@app_transaction.route('/exchangeuser', methods=['GET'])
def getexchangeuser():
 """ Returns all transactions done by the user logged in with other users
        ---
        parameters:
          - name : token
            in : authorization
            type : Bearer Token
            required : true

        responses:
          200:
            description: We receive a json file of all transactions the user did.
          403:
            description:  token wrong.
        """
 token = extract_auth_token(request)
 user_id = None
 if(token):
   try :
      user_id = decode_token(token)
      user_name = User.query.filter_by(id=user_id).all()[0].user_name

      give= Transaction.query.filter((Transaction.user_name==user_name) & (Transaction.receiver_name != None))
      #print(give)
      receive = Transaction.query.filter_by(receiver_name=user_name)
      d = {}
      d['give'] = transactions_schema.dump(give)
      d['receive'] = transactions_schema.dump(receive)
      return jsonify((d))
   except:
      abort(403)
 abort(403)
