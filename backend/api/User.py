from flask import request, jsonify, abort, Blueprint, Response

from .functions import create_token
from ..model.user import user_schema, User
from ..app import db, bcrypt

app_user = Blueprint('app_user', __name__)

#add a user
@app_user.route('/user', methods=['POST'])
def user():
 user_name = request.json["user_name"]
 password = request.json["password"]

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