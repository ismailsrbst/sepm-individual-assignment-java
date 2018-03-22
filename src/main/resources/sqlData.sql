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