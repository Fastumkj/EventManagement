
https://github.com/Fastumkj/EventManagement/assets/95695039/1b5c796b-e95e-452e-85f5-af9f1d5862df
# EventX - Online Event Management System (small scale)


https://github.com/Fastumkj/EventManagement/assets/95695039/decf98af-9f1e-4a95-a286-9be123245596

## Learning Objectives
- Perform backend development using Java EE technologies.
- Use JSF and PrimeFaces to create rich front-end presentations.

## System Description
The EventX system is an online event management platform similar to Eventbrite. It aims to facilitate event organizers in managing events. This assignment is worth 15% of the course grade and assesses the ability to perform both backend and frontend development using Java EE technologies.

### General Requirements
- Individual assignment. No collaboration allowed.
- Developed using Java EE and PrimeFaces in the NetBeans environment.
- Media files used should not infringe copyright.

### Function Requirements
The system should support the following functions:

1. **General**
   - Sign up
   - Login
   - Logout
   - View and edit user profile
2. **Event Listing Management (for event organizers)**
   - Add, delete, and list events
   - View event details
   - Mark/unmark users as present for an event
3. **Event Registration (for event attendees)**
   - Search, register, and unregister for events
   - View registered events

## How to Use
1. Clone the repository.
2. Open the project in NetBeans.
3. Deploy the project.
4. Follow the instructions provided in README.txt for database setup and deployment.

## How to Use (More detailed)
Database Details
=======================================
Database Type: MySQL
Database Name: [YourDatabaseName]

Initial Setup:
1. Install MySQL Server and NetBeans (preferably 12.5) if not already installed.
2. Open Services Tab in NetBeans and execute the following:
   - CREATE DATABASE [AnyDatabaseName];
   - USE [AnyDatabaseName];

Database Schema:
- Tables: EVENT, EVENTPERSON, PERSON
- Relationships are defined in the provided JPA entity classes within the project.

Connection Details:
- Username: [AnyDatabaseUsername]
- Password: [AnyDatabasePassword]
- JDBC URL: jdbc:mysql://localhost:8080[AnyDatabaseName]

Deployment Instructions (NetBeans 12.5 & GlassFish Server)
=======================================
1. Open NetBeans IDE 12.5.
2. Go to 'File' > 'Open Project' and navigate to the unzipped project folder.
3. Right-click the project in the 'Projects' tab and select 'Build' to compile the project.
4. Ensure GlassFish Server is installed and set up in NetBeans:
   - Go to 'Tools' > 'Servers' to add a server if not already added.
   - Select GlassFish Server and follow the setup wizard using default values.
5. Configure the GlassFish server:
   - Right-click the GlassFish Server in the 'Services' tab and select 'View Domain Admin Console'.
   - Navigate to 'Resources' > 'JDBC' > 'JDBC Connection Pools'.
   - Create a new connection pool with the JDBC URL, username, and password provided above.
   - Create a JDBC resource linked to the connection pool with a JNDI name matching the one in your project's `persistence.xml` file.
6. Right-click the project and select 'Run' to deploy the application to GlassFish Server. The link should be http://localhost:8080/EventX-war/ by default.

Note: Make sure the GlassFish server is running and the MySQL database in Netbeans is accessible during deployment.

Troubleshooting Tips:
- If the deployment fails, check the 'Output' tab in NetBeans for error messages.
- Ensure the JDBC connection pool and resource in GlassFish are configured correctly with valid credentials.
- Check the MySQL service is running if there are database connection issues.

If you encounter issues that are not covered by this guide, please consult the NetBeans and GlassFish Server documentation or the MySQL reference manual.

