# Yelp-DBMS
This project is to create a database and develop an application for Yelp concentrated on reviews data. </br>
createdb.sql contains the schema for databases. </br>
Data is present in JSON format which is to be formatted and loaded to tables in database. Populate program performs these tasks </br>
The front end application is built which is used for querying the data from the database that answers questions on reviews. The application is developed and present in hw3. </br>
</br>
Required: </br>
  Oracle 11gR2 </br>
  Netbeans </br>
</br>
Compile: </br>
  javac populate.java </br>
  javac hw3.java </br>
  </br>
Run the programs: </br>
  java populate yelp_business.json yelp_review.json yelp_checkin.json yelp_user.json </br>
  java hw3 </br>
  
Note: Populate should be run only for the first time. </br>


