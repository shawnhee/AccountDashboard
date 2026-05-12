# Account Dashboard

Java desktop application for retrieving and visualizing expense data from MySQL.

## Requirements

- JDK 17 or higher
- Maven
- XAMPP or any related MySQL server

## Dependencies

This project uses Maven to handle dependencies as below (Can be found in pom.xml):

- MySQL Connector/J (9.2.0): `com.mysql:mysql-connector-j` - Connects application with database
- JFreeChart (1.5.6): `org.jfree:jfreechart` - Visualizes spending in graphical charts

Install dependencies with:

```bash
mvn clean install
```

## Database Setup

1. Run MySQL locally on `localhost:3306`.
2. Import `accountdashboard.sql` into the database manager.
3. Ensure the credentials in `src/main/java/com/account/SQLConnection.java` match your database setup.

## Run

Start the database server, then run the main class from your IDE.

The application should connect successfully and display the expense dashboard.

## Notes

- Main source code is in `src/main/java`.
- Database schema and sample data are in `accountdashboard.sql`.
