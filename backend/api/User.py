from flask import request, jsonify, abort, Blueprint, Response

from api.functions import create_token
from model.user import user_schema, User
from app import db, bcrypt

app_user = Blueprint('app_user', __name__)

#add a user
@app_user.route('/user', methods=['POST'])
def user():
   """ Creates a user and adds it in the database
         ---
         parameters:
           - name: user_name
             in: body
             type : string
             example: Sacha
             required: true
           - name: password
             in: body
             type : string
             example: pass1
             required: true
         responses:
           200:
             description: returns a json of the user_name and the user's id
           400:
             description : The input is invalid. Make sure you have passed user_name and password correctly.
   """
   try:
       user_name = request.json["user_name"]
       password = request.json["password"]
   except:
       abort(400)

   if user_name and password is not None:
    NewUser = User(
     user_name=user_name,
     password=password
    )

   db.session.add(NewUser)
   db.session.commit()
   return jsonify(user_schema.dump(NewUser))

#authenticate
@app_user.route('/authentication', methods=['POST'])
def auth():
 """ authenticate a user
        ---
        parameters:
          - name: user_name
            in: body
            type : string
            example: "sacha"
            required: true
          - name: password
            in: body
            type : string
            example: "pass1"
            required: true
        responses:
          200:
            description: returns a token that will be used for autjorization.it is a hashed code of user_id
          400:
            description : The input is invalid
          403:
            description : The username or password is wrong
 """
 user_name = request.json["user_name"]
 password = request.json["password"]

 if user_name is None or password is None:
  abort(400)

 user = User.query.filter_by(user_name=user_name).first()
 if not user:
  abort(403)
 if not bcrypt.check_password_hash(user.hashed_password, password):
  abort(403)

 token=create_token(user.id)

 return jsonify(token=token)