CREATE TABLE Phone (
    id INT PRIMARY KEY,
    model VARCHAR(255),
    ext_ref VARCHAR(255),
    technology VARCHAR(255),
    g2_bands VARCHAR(255),
    g3_bands VARCHAR(255),
    g4_bands VARCHAR(255)
);

CREATE TABLE Inventory (
    id INT PRIMARY KEY,
    phone_type_id INT,
    FOREIGN KEY (phone_type_id) REFERENCES Phone(id),
);

CREATE TABLE Booking (
    id INT PRIMARY KEY,
    inventory_id INT,
    user_id INT,
    booking_date DATE,
    FOREIGN KEY (inventory_id) REFERENCES Inventory(id)
);