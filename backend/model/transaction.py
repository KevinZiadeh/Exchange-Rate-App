import datetime

from app import db, ma

class Transaction(db.Model):
 def __init__(self, usd_amount, lbp_amount, usd_to_lbp, user_name,receiver_name,sender_name):
  super(Transaction, self).__init__(usd_amount=usd_amount,
                                    lbp_amount=lbp_amount, usd_to_lbp=usd_to_lbp,
                                    user_name=user_name,
                                    added_date= datetime.datetime.now(),
                                    receiver_name=receiver_name,
                                    sender_name=sender_name)

 id = db.Column(db.Integer, primary_key=True)
 usd_amount = db.Column(db.Float,nullable=False)
 lbp_amount = db.Column(db.Float,nullable=False)
 usd_to_lbp = db.Column(db.Boolean, nullable=False)
 added_date = db.Column(db.DateTime)
 user_name = db.Column(db.String(30), db.ForeignKey('user.user_name'),nullable=True)
 receiver_name=db.Column(db.String(30), db.ForeignKey('user.user_name'),nullable=True)
 sender_name = db.Column(db.String(30), db.ForeignKey('user.user_name'), nullable=True)

class TransactionSchema(ma.Schema):
 class Meta:
  fields = ("id", "usd_amount", "lbp_amount", "usd_to_lbp","user_name","added_date","receiver_name","sender_name")
  model = Transaction
transaction_schema = TransactionSchema()
transactions_schema = TransactionSchema(many=True)
