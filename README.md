# TV series

This project was done to practice the JDBC, it is a simple DAO layer for a TV series streaming company.


## General information

The ```SeriesDaoJdbc``` class provides the following methods to perform operations on the *series* table:

* ```add``` Adds a new record to *series* table which contains data of series from the parameter.

* ```update``` Updates series in database which has the same title as the series in the parameter to 
  have the same data as the series in parameter.

* ```delete``` Deletes series with title given in parameter.

* ```filterByGenre``` Returns list of series filtered by genre given in parameter.

* ```createTable``` Creates a table named *series*.

* ```dropTable``` Drops table named *series* if its exists on the database.


## Setup

This is not a stand-alone application, however its operation can be checked by running the implemented 
tests in the ```SeriesDaoTest``` class.

The test uses PostgreSQL and reads the following environment variables to configure the database 
connection:

* ```DB_NAME``` Name of the database.

* ```USER``` User to connect.

* ```PASSWORD``` Password to connect.


## Technologies

* Java

* JDBC

* PostgreSQL