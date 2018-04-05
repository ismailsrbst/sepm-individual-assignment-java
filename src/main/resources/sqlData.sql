CREATE TABLE if not EXISTS Vehicle(
  vid INTEGER AUTO_INCREMENT PRIMARY KEY,
  model VARCHAR(255) NOT NULL,
  constructionYear INTEGER NOT NULL,
  description VARCHAR(255),
  seating INTEGER,
  plateNumber VARCHAR(255),
  driverLicense VARCHAR(255),
  powerUnit VARCHAR(255) NOT  NULL,
  power INTEGER NOT NULL,
  basePrice INTEGER NOT NULL,
  createDate TIMESTAMP NOT NULL,
  imageUrl VARCHAR(255),
  editDate TIMESTAMP,
  isDeleted BOOLEAN NOT NULL
);
CREATE TABLE if not EXISTS Booking(
  bid INTEGER AUTO_INCREMENT PRIMARY KEY,
  customerName VARCHAR(255) NOT NULL,
--  payTyp ENUM('IBAN', 'CREDITCARD') NOT NULL,
  payNumber VARCHAR(255) NOT NULL,
  beginnDate TIMESTAMP NOT NULL,
  endDate TIMESTAMP NOT NULL,
  createDate TIMESTAMP NOT NULL,
  totalPrice INTEGER NOT NULL,
  editDate TIMESTAMP,
  invoiceDate TIMESTAMP,
  invoiceNumber INTEGER,
  status ENUM ('OPEN', 'CANCELED', 'COMPLETED') NOT NULL
);
CREATE TABLE if not EXISTS BookingVehicle(
  vid INTEGER REFERENCES Vehicle(vid),
  bid INTEGER REFERENCES Booking(bid),
  licenseNumber VARCHAR(255),
  licenseCreateDate TIMESTAMP,
  model VARCHAR(255) NOT NULL,
  constructionYear INTEGER NOT NULL,
  description VARCHAR(255),
  seating INTEGER,
  plateNumber VARCHAR(255),
  driverLicense VARCHAR(255),
  powerUnit VARCHAR(255) NOT  NULL,
  power INTEGER NOT NULL,
  basePrice INTEGER NOT NULL,
  imageUrl VARCHAR(255),
  editDate TIMESTAMP,
  PRIMARY KEY (vid, bid)
);

--INSERT INTO Vehicle
 -- select all entities to be inserted
--  SELECT * FROM (
  -- the following select returns nothing and is just
  -- for convenience to make the following lines look pretty
 --    SELECT * FROM Vehicle WHERE FALSE
     -- followed by each row which should be inserted in the table
     -- IDs are hardcoded in the default insert to be able to insert relations afterwards
   --     UNION SELECT 1, "Car1", 2002, "PKW", 4, "W111", "AB", "Motorized", 70, 10, 2018-03-25, "C:\Users\Ismail\Desktop\SEPM\sepm-individual-assignment-java\src\main\resources\Images\bugatti.jpeg", NULL, FALSE
  --)
 -- run the insert only when the product table is empty
--WHERE NOT EXISTS(SELECT * FROM Vehicle);
