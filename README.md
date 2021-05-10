# Getting Started
To run this, you'd need at least java 11, maven, mongodb
run mongodb on localhost
run maven: mvn clean package to generate springboot jar
then java -jar colorgroup-0.0.1-SNAPSHOT.jar

### Assumption
For simplicity the input.json is loaded from the classpath and the java application populate the db on start if the can't find the db & collection 'ibm_cio'.
it needs to be moved to a path.

The java app as a controller to expose the data http://localhost:8081/api/v1/*
* GET groups : all groups and members
* GET members/{id}: returns individual group
* POST members/{memberId}: enable editing a member e.g., changing the group, name, or color
* DELETE members/{memberId}: enable removing a member from a group
* GET members/names/{name}: find group based on member's name
* GET colors: returns distinct colors in the collection

The java app has a login controller to generate JWT that can be used by the client apps as a bearer token to access the membership controller.
the token can be obtained by a POST request to http://localhost:8081/login with the username and password encoded with application/x-www-form-urlencoded

### Unit Test
Example of unit tests are included to test the rest endpoints. but because of the time limitation, they are not completed.
Unit tests are to be run with 'test' profile to disable the JWT token authentication. 

### Docker
* install docker and docker compose on your machine
* docker build -t ferryxo/ibm-demo-backend .
* docker-compose up
 


