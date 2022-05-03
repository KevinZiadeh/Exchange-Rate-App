from statistics import mean
from sklearn.linear_model import LinearRegression

import jwt
import datetime
import pandas as pd

SECRET_KEY = "b'|\xe7\xbfU3`\xc4\xec\xa7\xa9zf:}\xb5\xc7\xb9\x139^3@Dv'"

def create_token(user_id):
 payload = {
 'exp': datetime.datetime.utcnow() + datetime.timedelta(days=4),
 'iat': datetime.datetime.utcnow(),
 'sub': user_id
 }
 return jwt.encode(
 payload,
 SECRET_KEY,
 algorithm='HS256'
 )

def extract_auth_token(authenticated_request):
 auth_header = authenticated_request.headers.get('Authorization')
 if auth_header and auth_header != "Bearer":
   return auth_header.split(" ")[1]
 else:
   return None

def decode_token(token):
 payload = jwt.decode(token, SECRET_KEY, 'HS256')
 return payload['sub']

def PredictFutureRate(listRates):
 if len(listRates) < 1 :
  return None
 if len(listRates) < 6 :
  return mean(listRates)
 else:
  series=pd.Series(listRates)
  temps = pd.DataFrame(series.values)
  dataframe = pd.concat([temps.shift(3), temps.shift(2), temps.shift(1), temps], axis=1)
  dataframe.columns = ['t-2', 't-1', 't', 't+1']
  dataframe=dataframe.dropna()
  train=dataframe[0:(len(dataframe)-2)]
  test=dataframe[(len(dataframe)-2):(len(dataframe)-1)]
  Xtrain=train.iloc[:,0:3]
  ytrain=train.iloc[:,3:4]
  Xtest=test.iloc[:,1:4]

  lr= LinearRegression()
  lr.fit(Xtrain,ytrain)
  ytest=lr.predict(Xtest)
  return(ytest[0][0])



