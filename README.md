# Currency Exchange Rate

> University Project (EECE 430L - Web, Mobile, and Application Development Labw @ AUB) where we develop a n exchange rate app on multiple platforms having a REST API backend.

## Overview
The application shows the exchange rate of the Lebanese Currency (LBP) against the US dollar (USD $), and allows the creation of users, adding transactions, calculating the equivalent amount of each currency in the other, as well as some statistical information about the data and user transactions. 
There are three frontend platforms (web based using React, desktop app using Java, and android app using Kotlin).

### Architecture
The complete application architecture can be found in the image below. To know more about each platform, you can check the README files of each.

<p align="center">
  <img src="res/architecture.png?raw=true"/>
</p>

### Functional Documentation
Below you can find a description of the features supported by our application. The implementation of each may differ to better suit each platform.

#### Exchange Rates
Check the USD and LBP exchange rates. These rates are calculated based on the transactions that occured in the previous 4 days.
#### Exchange Rate Calculator
Convert an  amount of a given currency to the other using the exchange rate found in Exchange Rates.
#### Recording a Transaction
Add a transaction, specifying the USD amount, LBP amount, and direction of exchange (USD to LBP, or vice-versa). These transactions can be added anonymously (without having a account), or can be done while logged it, which leads to the transaction being associated with the user.
#### User Accounts
Login or Signup, which allows you to add transactions associated with the user account and check all the previous transactions that the user did. 
#### Statistics


## Requirements
To be able to run the application on any platform, you need to 

 1. Have the backend running
 2. Have a database to connect to

To setup the backend, please read backend [documentation](backend/README.md).</br>
To setup the react frontend, please read frontend [documentation](frontend/README.md).</br>
To setup the java-based desktop app, please read desktop [documentation](desktop/README.md).</br>
To setup the android, please read android [documentation](android/README.md).
