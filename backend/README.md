# Backend
> Backend done using Flask and MySQL

## Requirements
- Python 3.7+
- MySQL Installed


## How to set it up
1. Clone the repo
2. Create a new environment where we will download all our required libraries
3. Use pip to install all the dependencies specified in requirements.txt file
```
git clone https://github.com/KevinZiadeh/Exchange-Rate-App.git
cd Exchange-Rate-App
cd backend
py -3 -m venv venv
.\venv\Scripts\activate
pip install -r requirements.txt
```
4. Create the `db_config.py` so it holds your credentials on MySQL:
    Write in it: `DB_CONFIG='mysql+pymysql://<mysql_username>:<mysql_password>@<mysql_host>:<mysql_port>/<mysql_db_name>'`
5. Initialize the database and create the needed databases using a python shell within the virtual environment using `python`:
    ```python
    from app import db
    db.create_all()
    exit()
    ```
6. Run the application using `python run.py`

## Database:
It contains 2 models:
1. User:
   * Attributes:
     * id: integer, primary key 
     * user_name: string(30), unique 
     * hashed_password: string(128)
     
2. Transaction:
      * Attributes:
        * id: integer, primary key 
        * usd_amount: float,non-nullable 
        * lbp_amount:float, non-nullable 
        * usd_to_lbp: boolean, non-nullable 
        * added_date: Datetime 
        * user_id: integer, foreign key(user.id), nullable 
        * receiver_name: string(30), foreign key(user.user_name), nullable
        * sender_name:  string(30), foreign key(user.user_name), nullable

## Architecture:
The APIs are all found in the folder API divided into three files: ExchangeRate.py, Transaction.py and User.py
They are linked to two models: user and transaction which are in the database

<p align="center">
  <img src="Architecture_backend.PNG"  width="300" height="400" alt="background architecture"/>
</p>

## Documentation:
We used Swagger for api's documentation:
Run the application, and enter "http://localhost:5000/apidocs/#/", you should see this doc:

<p align="center">
  <img src="doc.PNG"  width="800" height="300" alt="background architecture"/>
</p>
