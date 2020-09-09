# Yelp-DBMS
This project is to create a database and develop an application for Yelp concentrated on reviews data.
createdb.sql contains the schema for databases.
Data is present in JSON format which is to be formatted and loaded to tables in database. Populate program performs these tasks
The front end application is built which is used for querying the data from the database that answers questions on reviews. The application is developed and present in hw3.

Required:
  Oracle 11gR2
  Netbeans

Compile:
  javac populate.java
  javac hw3.java
  
Run the programs:
  java populate yelp_business.json yelp_review.json yelp_checkin.json yelp_user.json
  java hw3
  
Note: Populate should be run only for the first time.


