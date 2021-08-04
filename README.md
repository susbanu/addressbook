# Address-Book lookup application

This is a sample address book lookup application which is exposing 3 rest services to save and search addresses.

#### Technologies
	Java 11
	Spring boot 2.5.3
	‎MongoDB 4.4
	Tomcat-embed 9.x
	Apache Maven 3.8.1

#### Environment variables
Following environment variables must be configured to run the application,

	MONGODB_HOST : The host name of MongoDB database
	MONGODB_PORT : The port of MonogDB database
	MONGODB_DATABASE : The database name of MongoDB database
	MONGODB_AUTH_DATABASE : Name of MongoDB authentication database
	MONGODB_USERNAME : The user name of MongoDB database
	MONGODB_PASSWORD : The password of MongoDB database
	LOGGER_LEVEL : The root log level of application
		
#### MongoDB query to create a database and table

1. 	 To create a database, you will first need to switch the context to a non-existing database using the use.

	use address_book
	
2. MongoDB only creates the database when you first store data in that database. So, add a document to your database.
	
	db.address.insert({firstName: "firstname1", lastname: "lastname1", city: "city1", street: "street1", state:"state1", zip: "zip1"})

3. Create text index on fields that contains string data

	db.address.createIndex({
     firstName: "text",
     lastName: "text",
     city: "text",
     street: "text",
     state: "text",
     zip: "text"
     },{name: "MyAddressIndex"})

#### Application Build and Run Test cases

To build and run test cases of application execute following command

	mvn clean package
	
#### Run Application

To run application, Navigate to the root of the project via command line and execute the command. You can active spring profile using "-Dspring.profiles.active=<profile>" JVM system parameter. 

	mvn spring-boot:run

### API Documentation
	
#### Swagger UI
	http://<host>:<port>/swagger-ui.html
#### OpenAPI UI
	http://<host>:<port>/api-docs	