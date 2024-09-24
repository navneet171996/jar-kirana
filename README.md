# jar-kirana

Whenever the application starts it creates a user with username "admin" and password "password"

The admin should login and get their JWT token using the login API

Login API:
    -POST /api/v1/auth/login
    -This API authenticates a user(including admin) and returns a JWT token
    -Request Body:
    
    {
        "username": string,
        "password": string
    }

    -Response Body

    {
        "username": string,
        "token": string
    }

Admin can add users with the Add User api. Users represent kirana store owners who can record transactions. Admin cannot add transactions.

Add User API:
    -POST /api/v1/admin/addUser
    -This API lets admin add users(kirana store owners)
    -Request Body:

    {
        "username" : string,
        "password" : string
    }

    -Response Body
        user_id : string

After an user is created, he/she can record transactions in their preferred currency. The record transaction API takes in the transaction and converts the amount to INR and stores the transaction.

Record Transaction API:
    -POST /api/v1/user/record
    -This API lets users add transactions. Admin cannot add transactions
    -Request Body:
    
    {
        "userId" : "66ec61be39c2f916b8cb803d",(string)
        "amount" : 70,(long)
        "currency" : "USD",(string)
        "transactionType" : "CREDIT" or "DEBIT (string)
    }

The user can see his/her transactions with the following APIs
1. Get daily report API:
    -GET /api/v1/user/daily-report/{userId}
    -This API returns the daily report consisting of totalCredit, totalDebit, balance and the list  of transactions

2. Get weekly report API:
    -GET /api/v1/user/weekly-report/{userId}
    -This API returns the daily report consisting of totalCredit, totalDebit, balance and the list of transactions

3. Get monthly report API:
    -GET /api/v1/user/monthly-report/{userId}
    -This API returns the daily report consisting of totalCredit, totalDebit, balance and the list of transactions

4. Get yearly report API:
    -GET /api/v1/user/yearly-report/{userId}
    -This API returns the daily report consisting of totalCredit, totalDebit, balance and the list of transactions