# P6-Full-Stack-reseau-dev

## Local Installation Guide for Orion project

## 1. Clone the repository
    git clone https://github.com/JuOm49/Orion_MVP.git

#### Project Structure
After cloning the repository, you'll find two main folders

    Orion_MVP/
      ─ backend/   → Spring Boot API
      ─ frontend/  → Angular application

## 2. Backend Setup (Spring Boot
Before running the API, configure your environment variables in "/src/main/resources/application.properties". Use placeholders to avoid hardcoding sensitive data:
    
    spring.application.name=Orion
    server.port=9000

    spring.datasource.url=${DB_URL_ORION}
    spring.datasource.username=${DB_USERNAME_ORION}
    spring.datasource.password=${DB_PASSWORD_ORION}
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

    # create tables automatically with Hibernate
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true

    jwt.secret=${JWT_SECRET_ORION}

  You can define these variables in your system

#### Run the API

From the backend folder, run:

    ./mvnw spring-boot:run

Or with Maven installed:

    mvn spring-boot:run

The API will start on http://localhost:9000.

## 3. Frontend setup

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.3.

install dependencies

    npm install

#### environment configuration

  edit "environment.ts" to match with API URL:

    export const environment = {
      production: false,
      apiUrl: 'http://localhost:9000/api'
    };

#### Run Angular app

    ng serve
  Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

#### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.


## 4. Run the API
   
    mvn clean install
   
   To launch the application, open the project in your IDE (e.g., IntelliJ) and locate the "MddApiApplication" class.
   From there, click the green Run button next to the main() method or use the IDE's run menu to execute "MddApiApplication". This will start the Spring Boot application locally.




