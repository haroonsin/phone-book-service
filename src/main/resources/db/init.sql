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
    available BYTE,
    FOREIGN KEY (phone_type_id) REFERENCES Phone(id)
);

CREATE TABLE Booking (
    id INT PRIMARY KEY,
    inventory_id INT,
    user_id INT,
    booking_date DATE,
    FOREIGN KEY (inventory_id) REFERENCES Inventory(id)
);

INSERT INTO
    Phone (
        id,
        model,
        ext_ref,
        technology,
        g2_bands,
        g3_bands,
        g4_bands
    )
VALUES
    (
        1,
        'Samsung Galaxy',
        'SG001',
        '5G',
        'GSM',
        'UMTS',
        'LTE'
    );

INSERT INTO
    Inventory (id, phone_type_id, available)
VALUES
    (1, 1, 1);

commit;

-- CREATE SCHEMA pc;
-- -- CREATE TABLE pc.phone (
-- CREATE TABLE phone {
--     id INT PRIMARY KEY AUTO_INCREMENT,
--     model VARCHAR(255) NOT NULL,
--     ext_ref VARCHAR(100) NOT NULL,
--     technology VARCHAR(100) NOT NULL,
--     g2_bands VARCHAR(255) NOT NULL,
--     g3_bands VARCHAR(255) NOT NULL,
--     g4_bands VARCHAR(255) NOT NULL
-- );
-- CREATE TABLE booking (
--     id INT PRIMARY KEY AUTO_INCREMENT,
--     phone_id INT NOT NULL,
--     bookedBy VARCHAR(255) NOT NULL,
--     bookedAt DATETIME NOT NULL,
--     version INT,
--     FOREIGN KEY (phone_id) REFERENCES phone(id)
-- );