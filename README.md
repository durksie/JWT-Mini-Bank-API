# Bank Account Management API (JWT Auth) – Spring Boot

## Project Overview
The **Bank Account Management API** is a Spring Boot REST application that simulates core digital banking functionality. It allows users to register for an account, log in, and securely manage bank accounts using **JWT authentication** and **role-based authorization**.

This project simulates real-world banking operations with multiple account types, secure authentication, and transaction management. Perfect for learning Spring Security, JWT implementation, and RESTful API design.


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

```bash
# Check your environment
java --version
mvn --version
mysql --version



Dependencies (pom.xml)Include these in your dependencies section. Note: Use Spring Boot 3.x to avoid the "unresolved dependency" error.XML<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>        
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>

    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>


Database SetupLog in to your MySQL terminal:SQLmysql -u root -p
Create the project database:SQLCREATE DATABASE bankdb;
Application ConfigurationUpdate src/main/resources/application.properties with your credentials:Propertiesspring.datasource.url=jdbc:mysql://localhost:3306/bankdb
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Configuration (Minimum 64 characters for HS512)
jwt.secret=your_64_character_long_super_secret_key_make_it_very_long_and_secure
jwt.expiration=86400000
API Endpoints1. Authentication EndpointsEndpointMethodBodyDescription/api/auth/registerPOST{ "username": "user", "password": "pass" }Register a new user/api/auth/loginPOST{ "username": "user", "password": "pass" }Login & receive JWT2. Account Endpoints (Protected)Requires Header: Authorization: Bearer <token>EndpointMethodBodyDescription/api/accountsPOST{ "accountNumber":"123", "balance":1000 }Create a new account/api/accountsGET—View all accounts/api/accounts/{id}GET—View account by ID/api/accounts/deposit/{id}POST{ "amount":500 }Deposit money/api/accounts/withdraw/{id}POST{ "amount":200 }Withdraw moneyProject StructurePlaintextsrc/main/java/com/example/bankapi
│
├── controller    # REST Controllers
├── service       # Business Logic
├── repository    # Data Access Layer (JPA)
├── model         # Entities (User, Account)
├── security      # JWT & Security Config
└── dto           # Data Transfer Objects
Running the ApplicationUsing Maven:Bashmvn spring-boot:run
Expected Console Output:PlaintextTomcat started on port 8080
Started BankApplication in 3.5 seconds
Future Improvements[ ] Internal transfers between accounts.[ ] Full transaction history/audit logs.[ ] Refresh token implementation.[ ] Dockerization for easy deployment.[ ] Swagger/OpenAPI documentation.
