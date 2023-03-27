CREATE DATABASE IF NOT EXISTS minihw4and5;
USE minihw4and5;
CREATE TABLE IF NOT EXISTS newdata_csv (
	Invoice int,
	StockCode int,
	Description varchar(50),
	Quantity int,
	InvoiceDate datetime,
	Price decimal(5,2),
	Customer_ID int,
	Country varchar(20)
	);
LOAD DATA LOCAL INFILE 'src/minihw4and5/newData.csv' INTO TABLE newdata_csv
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES (
	Invoice,
	StockCode,
	Description,
	Quantity,
	@InvoiceDate,
	Price,
	Customer_ID,
	Country
	)
SET InvoiceDate = STR_TO_DATE(@InvoiceDate, '%d/%m/%Y %H:%i');