Before running the project, ensure you have the following installed on your local machine:

Java Development Kit (JDK): Download and install JDK from here.
Apache Tomcat: Download and install Apache Tomcat from here.
MySQL Database: Install MySQL from here.

Steps to Run the Project Locally
Follow these steps to set up and run the project on your local machine:

1. Clone the Repository
2. Navigate to the Project Directory
3. Set Up the Database
Start your MySQL server.

Create a new database for the project:

sql
Copy
CREATE DATABASE book_management_system;
Import the database schema
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) UNIQUE NOT NULL
);

4. Configure Database Connection

5. Build the Project
If you are using Maven, navigate to the project root directory and run the following command to build the project:

bash
Copy
mvn clean install
This will generate a .war file in the target directory.

6. Deploy the Application
Copy the generated .war file from the target directory to the webapps directory of your Apache Tomcat installation.

Start the Tomcat server:

7. Access the Application
Once the server is running, open your browser and navigate to:

Copy
http://localhost:8080/BookManagementSystem
