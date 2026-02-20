# Bank Account Management API (JWT Auth) – Spring Boot

## Project Overview
The **Bank Account Management API** is a Spring Boot REST application that simulates core digital banking functionality. It allows users to register, log in, and securely manage bank accounts using **JWT authentication** and **role-based authorization**.

This project demonstrates how secure backend systems are architected in modern financial platforms, focusing on stateless security and data integrity.

---

## Features
* **User Management:** Registration and Login.
* **Security:** JWT authentication & authorization with **BCrypt** password encryption.
* **Account Operations:** Create accounts, view details, and manage balances.
* **Transactions:** Secure deposit and withdrawal logic.
* **Access Control:** Role-based restrictions on sensitive endpoints.
* **Persistence:** MySQL database integration via Spring Data JPA.

---

## Technologies Used
* **Java 17+**
* **Spring Boot 3.x**
* **Spring Security**
* **Spring Data JPA**
* **JWT (JSON Web Token)**
* **MySQL**
* **Maven**
* **Lombok**

---

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
