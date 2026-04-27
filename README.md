Personal Budget Application

Description:
This is a personal budget management application built utilizing Spring Boot.
It is designed to mimic real budget planning and allocation.

Project Structure:
- PersonalBudgetApplication/ → Main Spring Boot application
- PersonalBudgetSQLSchema/ → Database export (.sql file)
- DemoVideo/ → Application demo video
- ProjectUIWireframes/ → UI design wireframes
- Budget_Tracker_UI/ → Additional UI work
- ProjectDeliverables/ → Supporting project files
- ProjectTestReport/ → Test Report


Features:
-User authentication and security
-Create, read, update, and delete transactions
-Category-based transaction organization(income/expense types)
-Budget creation and tracking with allocation
-Financial summary dashboard
-Analytics summary

Requirements
-Java 21
-Maven
-MySQL


Setup Instructions:

1. Clone or download the project.


2.Create the database in MySQL:
CREATE DATABASE PersonalBudgetApplicationV2_DB;


3. Import the provided SQL file:
   -Open MySQL Workbench
   -Go to Server -> Data Import
   -Select the included .sql file
   -Run the import

4. Update application.properties:
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password

5. Run the application:
   mvn spring-boot:run

6. Open in browser:
   http://localhost:8080

Notes:
-The application was developed and tested using MySQL.
-A demo video is included to demonstrate functionality.

Known Issues:
No known issues at this time.


Author:
Rachel Moment
