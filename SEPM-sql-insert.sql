INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,createDate,imageUrl,editDate,isDeleted) 
VALUES ('Skoda Karoq', 2005, 'white', 4, 'W23448', 'BC', 'Motorized', 70, 8, '2018-03-24 09:26:50.12', 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL, false);
INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,createDate,imageUrl,editDate,isDeleted) 
VALUES ('Trek Madone', 2015, 'white', 0, '', '', 'Brawn', 0, 2, '2018-03-25 09:26:50.12', 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL, false);
INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,createDate,imageUrl,editDate,isDeleted) 
VALUES ('Ford EcoSport', 2009, 'blue', 4, 'W84568', 'BC', 'Motorized', 80, 10, '2018-03-26 09:26:50.12', 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL, false);
INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,createDate,imageUrl,editDate,isDeleted) 
VALUES ('Honda CBR250RR', 2017, 'black', 2, 'W56748', 'A', 'Motorized', 70, 8, '2018-03-27 09:26:50.12', 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL, false);
INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,createDate,imageUrl,editDate,isDeleted) 
VALUES ('BMW X6', 2016, 'white', 4, 'W12345', 'BC', 'Motorized', 90, 12, '2018-03-29 09:26:50.12', 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL, false);
INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,createDate,imageUrl,editDate,isDeleted) 
VALUES ('Volvo XC40', 2015, 'blue', 4, 'W221345', 'BC', 'Motorized', 75, 9, '2018-04-01 09:26:50.12', 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL, false);
INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,createDate,imageUrl,editDate,isDeleted) 
VALUES ('Bugatti', 2016, 'red', 4, 'W2456456', 'AB', 'Motorized', 150, 30, '2018-04-03 09:26:50.12', 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\bugatti.jpeg', NULL, false);
INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,createDate,imageUrl,editDate,isDeleted) 
VALUES ('Audi A3', 2015, 'gray', 4, 'W1235456', 'AB', 'Motorized', 110, 20, '2018-04-04 09:26:50.12', 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\bugatti.jpeg', NULL, false);
INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,createDate,imageUrl,editDate,isDeleted) 
VALUES ('Mercedes Benz', 2015, 'black', 4, 'W735645', 'AB', 'Motorized', 150, 9, '2018-04-05 09:26:50.12', 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\bugatti.jpeg', NULL, false);
INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,createDate,imageUrl,editDate,isDeleted) 
VALUES ('Segway', 2013, 'blue', 4, '', '', 'Brawn', 0, 9, '2018-04-01 09:26:50.12', 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL, false);

//TODO totalPrice
INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice,editDate,invoiceDate,invoiceNumber,status) 
Values('Ismail', 'AT611904300234573201', '2018-04-01 09:00:00.00', '2018-04-01 11:00:00.00', '2018-03-23 09:26:50.12', 20, NULL, '2018-04-01 09:26:50.12', 995, 'COMPLETED');
INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice,editDate,invoiceDate,invoiceNumber,status) 
Values('Yahya', 'AT611904300234573201', '2018-04-21 09:00:00.00', '2018-04-21 11:00:00.00', '2018-03-24 09:26:50.12', 36, NULL, NULL, NULL, 'OPEN');
INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice,editDate,invoiceDate,invoiceNumber,status) 
Values('Stefan', 'AT611904300234573201', '2018-04-09 09:00:00.00', '2018-04-09 11:00:00.00', '2018-03-26 09:26:50.12', 5, NULL, '2018-03-30 08:26:50.12', 996, 'CANCELED');
INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice,editDate,invoiceDate,invoiceNumber,status) 
Values('Christopher', 'AT611904300234573201', '2018-04-11 09:00:00.00', '2018-04-11 11:00:00.00', '2018-03-27 09:26:50.12', 78, NULL, NULL, NULL, 'OPEN');
INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice,editDate,invoiceDate,invoiceNumber,status) 
Values('Alexander', 'AT611904300234573201', '2018-04-15 09:00:00.00', '2018-04-16 15:00:00.00', '2018-03-29 09:26:50.12', 600, NULL, NULL, NULL, 'OPEN');
INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice,editDate,invoiceDate,invoiceNumber,status) 
Values('Ahmet', 'AT611904300234573201', '2018-04-13 09:00:00.00', '2018-04-14 15:00:00.00', '2018-04-01 09:26:50.12', 270, NULL, NULL, NULL, 'OPEN');
INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice,editDate,invoiceDate,invoiceNumber,status) 
Values('Yasin', 'AT611904300234573201', '2018-04-14 09:00:00.00', '2018-04-14 15:00:00.00', '2018-04-01 09:26:50.12', 102, NULL, NULL, NULL, 'OPEN');
INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice,editDate,invoiceDate,invoiceNumber,status) 
Values('Hans', 'AT611904300234573201', '2018-04-12 09:00:00.00', '2018-04-12 15:00:00.00', '2018-04-02 09:26:50.12', 54, NULL, NULL, NULL, 'OPEN');
INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice,editDate,invoiceDate,invoiceNumber,status) 
Values('Masaki', 'AT611904300234573201', '2018-04-19 09:00:00.00', '2018-04-20 15:00:00.00', '2018-04-03 09:26:50.12', 54, NULL, NULL, NULL, 'OPEN');
INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice,editDate,invoiceDate,invoiceNumber,status) 
Values('Anna', 'AT611904300234573201', '2018-04-13 09:00:00.00', '2018-04-13 15:00:00.00', '2018-04-05 09:26:50.12', 72, NULL, NULL, NULL, 'OPEN');


INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (1,1, '1234213df', '2018-04-01 15:00:00.00','Skoda Karoq', 2005, 'white', 4, 'W23448', 'BC', 'Motorized', 70, 8, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (2,1, '', NULL,'Trek Madone', 2015, 'white', 0, '', '', 'Brawn', 0, 2, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (3,2, '', '2018-04-01 15:00:00.00','Ford EcoSport', 2009, 'blue', 4, 'W84568', 'BC', 'Motorized', 80, 10, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (4,2, '', '2018-04-01 15:00:00.00', 'Honda CBR250RR', 2017, 'black', 2, 'W56748', 'A', 'Motorized', 70, 8, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (5,3,'', '2017-04-01 15:00:00.00', 'BMW X6', 2016, 'white', 4, 'W12345', 'BC', 'Motorized', 90, 12, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (6,4,'', '2018-01-01 15:00:00.00', 'Volvo XC40', 2015, 'blue', 4, 'W221345', 'BC', 'Motorized', 75, 9, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (7,4,'', '2016-04-06 15:00:00.00', 'Bugatti', 2016, 'red', 4, 'W2456456', 'AB', 'Motorized', 150, 30, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\bugatti.jpeg', NULL,);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (8,5,'', '2015-04-01 15:00:00.00', 'Audi A3', 2015, 'gray', 4, 'W1235456', 'AB', 'Motorized', 110, 20, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\bugatti.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (9,6,'', '2015-03-01 15:00:00.00', 'Mercedes Benz', 2015, 'black', 4, 'W735645', 'AB', 'Motorized', 150, 9, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\bugatti.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (10,7,'', '2012-04-01 15:00:00.00', 'Segway', 2013, 'blue', 4, '', '', 'Brawn', 0, 9, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (1,7,'', '2016-04-08 15:00:00.00','Skoda Karoq', 2005, 'white', 4, 'W23448', 'BC', 'Motorized', 70, 8, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (10,8,'', '2013-04-01 15:00:00.00', 'Segway', 2013, 'blue', 4, '', '', 'Brawn', 0, 9, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (9,9,'', '2016-04-01 15:00:00.00', 'Mercedes Benz', 2015, 'black', 4, 'W735645', 'AB', 'Motorized', 150, 9, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\bugatti.jpeg', NULL);
INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate,model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power,basePrice,imageUrl,editDate) 
Values (5,10,'', '2018-03-01 15:00:00.00', 'BMW X6', 2016, 'white', 4, 'W12345', 'BC', 'Motorized', 90, 12, 'C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\noImageCar.jpeg', NULL);



