# Bank Account Management API (JWT Auth) – Spring Boot

## Project Overview
The **Bank Account Management API** is a Spring Boot REST application that simulates core digital banking functionality. It allows users to register for an account, log in, and securely manage bank accounts using **JWT authentication** and **role-based authorization**.

This project simulates real-world banking operations with multiple account types, secure authentication, and transaction management. Perfect for learning Spring Security, JWT implementation, and RESTful API design.

## 📂 Project Structure

```
src/main/java/com/bankapp/
├── 🎮 controller/             # REST Endpoints for Auth and Banking
│   ├── AuthController.java
│   └── AccountController.java
├── ⚙️ service/                # Business logic and validation
│   ├── UserService.java
│   ├── AccountService.java
│   └── TransactionService.java
├── 📦 repository/             # Spring Data JPA repositories
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   └── TransactionRepository.java
├── 📑 model/                  # JPA Entities (User, Account, Transaction)
│   ├── User.java
│   ├── Account.java
│   ├── Transaction.java
│   └── AccountType.java
├── ✉️ dto/                    # Data Transfer Objects for API requests
│   ├── LoginDto.java
│   ├── RegisterDto.java
│   ├── AccountDto.java
│   ├── TransactionDto.java
│   └── JwtResponseDto.java
├── 🔐 security/               # JWT & Security implementation
│   ├── JwtGenerator.java
│   ├── JwtFilter.java
│   └── CustomUserDetailsService.java
└── 🛠️ config/                 # Global configurations
    └── SecurityConfig.java
```

##  Features

### **Secure Authentication**
* **JWT-based authentication** for stateless session management.
* **Password encryption** using **BCrypt** hashing.
* **Role-based access control (RBAC)** to distinguish between `USER` and `ADMIN` permissions.

### **Multiple Account Types**
* **Easy Account**: Min: $0 | Max: $10,000
* **Aspire Account**: Min: $1,000 | Max: $50,000
* **Premier Account**: Min: $10,000 | Max: $200,000
* **Private Clients Account**: Min: $50,000 | Max: $500,000
* **Private Wealth Account**: Min: $200,000 | Max: Unlimited

### **Banking Operations**
* **Account Registration**: Seamless onboarding with account type selection.
* **Balance Inquiry**: Real-time access to current funds.
* **Financial Transactions**: Support for **Deposits** and **Withdrawals**.
* **Transaction History**: Comprehensive logging of all account activity.
* **Limit Enforcement**: Automatic validation of account-specific minimum and maximum balance limits.

### **Database Design**
* **Entity Relationships**:
    * **One-to-One**: `User` ↔ `Account`
    * **One-to-Many**: `Account` ↔ `Transactions`
* **Automated Logic**: Smart account number generation using unique **type-specific prefixes**.

## Technologies Used
![Java](https://img.shields.io/badge/Java-17+-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-JSON%20Web%20Token-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-bc002d?style=for-the-badge&logo=lombok&logoColor=white)


## Prerequisites
Before running the application, ensure you have the following installed:
* **Java 17** or higher
* **Maven 3.6+**
* **MySQL Server**
* An IDE (IntelliJ IDEA recommended)

# Check your environment
java --version
mvn --version
mysql --version

# 🛠️ Installation Steps
## 1. Clone the Repository
Open your terminal and clone the project to your local machine:

```
git clone https://github.com/yourusername/mini-banking-api.git
cd mini-banking-api
```
## 2. Configure MySQL Database
Ensure your MySQL server is running, then create the database:
```
CREATE DATABASE minibank;
```
## 3. Update Application Properties
Navigate to the resources folder and update your database credentials:

```
# Edit src/main/resources/application.properties
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

## 4. Build the Project
Use Maven to download dependencies and build the executable JAR:
```
mvn clean install
```

## 5. Run the Application
Start the Spring Boot application:
```
mvn spring-boot:run
```
[!NOTE]
The application will be accessible at``` http://localhost:8080```


# API Documentation
Authentication Endpoints
1. Get Available Account Types
   ```GET /api/auth/account-types```

## json Response
```
 {
    "name": "EASY",
    "displayName": "Easy Account",
    "minimumBalance": 0.0,
    "maximumBalance": 10000.0
  },
  {
    "name": "PREMIER",
    "displayName": "Premier Account",
    "minimumBalance": 10000.0,
    "maximumBalance": 200000.0
  }
```

2. Register New User
```
POST /api/auth/register
Content-Type: application/json

{
    "username": "john_doe",
    "password": "password123",
    "accountType": "Premier Account"
}
```
## Response
```
{
    "message": "User registered successfully! Account Type: Premier Account, Account Number: PRM1709234567123"
}
```
3. Login
```
POST /api/auth/login
Content-Type: application/json

{
    "username": "john_doe",
    "password": "password123"
}
```
## Response
```
{
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "username": "john_doe"
}
```

# Protected Endpoints (Require JWT Token)
Include in Header:``` Authorization:``` ```Bearer <your-token>```
4. Get Account Details
GET /api/account
## Response
```
{
    "id": 1,
    "accountNumber": "PRM1709234567123",
    "balance": 15000.00,
    "username": "john_doe",
    "accountType": "PREMIER",
    "accountTypeDisplayName": "Premier Account",
    "minimumBalance": 10000.0,
    "maximumBalance": 200000.0
}
```
5. Deposit Money
   POST /api/account/deposit?amount=5000
   ## Response
   ```
   {
    "id": 1,
    "accountNumber": "PRM1709234567123",
    "balance": 20000.00,
    "username": "john_doe",
    "accountType": "PREMIER",
    "accountTypeDisplayName": "Premier Account",
    "minimumBalance": 10000.0,
    "maximumBalance": 200000.0
}
```
6. Withdraw Money
POST /api/account/withdraw?amount=3000
```
{
    "id": 1,
    "accountNumber": "PRM1709234567123",
    "balance": 17000.00,
    "username": "john_doe",
    "accountType": "PREMIER",
    "accountTypeDisplayName": "Premier Account",
    "minimumBalance": 10000.0,
    "maximumBalance": 200000.0
}
```
7. View Transaction History
GET /api/account/transactions
## Response
```
{
        "id": 1,
        "amount": 5000.00,
        "type": "DEPOSIT",
        "description": "Deposit to Premier Account",
        "transactionDate": "2024-02-29T10:30:00"
    },
    {
        "id": 2,
        "amount": 3000.00,
        "type": "WITHDRAWAL",
        "description": "Withdrawal from Premier Account",
        "transactionDate": "2024-02-29T10:35:00"
    }
```
