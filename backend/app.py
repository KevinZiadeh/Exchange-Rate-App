from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow
from flask_bcrypt import Bcrypt
from flask_cors import CORS
from flasgger import Swagger


## Creating app
app = Flask(__name__)
from db_config import DB_CONFIG
app.config['SQLALCHEMY_DATABASE_URI'] = DB_CONFIG
db = SQLAlchemy(app)
ma = Marshmallow(app)
bcrypt = Bcrypt(app)
CORS(app)
## app created

swagger = Swagger(app)

from api.User import app_user
app.register_blueprint(app_user)
from api.Transaction import app_transaction
app.register_blueprint(app_transaction)
from api.ExchangeRate import app_rate
app.register_blueprint(app_rate)