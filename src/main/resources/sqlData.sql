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
  payTyp ENUM('IBAN', 'CREDITCARD') NOT NULL,
  payNumber VARCHAR(255) NOT NULL,
  beginnDate TIMESTAMP NOT NULL,
  endDate TIMESTAMP NOT NULL,
  createDate TIMESTAMP NOT NULL,
  totalPrice INTEGER NOT NULL,
  editDate TIMESTAMP NOT NULL,
  status ENUM ('OPEN', 'CANCELED', 'COMPLETED') NOT NULL
);
CREATE TABLE if not EXISTS BookingVehicle(
  vid INTEGER REFERENCES Vehicle(vid),
  bid INTEGER REFERENCES Booking(bid),
  licenseNumber VARCHAR(255),
  licenseCreateDate TIMESTAMP,
  PRIMARY KEY (vid, bid)
);
