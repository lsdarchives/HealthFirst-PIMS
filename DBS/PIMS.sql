-- ============================================
-- PIMS DATABASE SETUP
-- Pharmacy Inventory Management System
-- ============================================

-- ============================================
-- 1. CREATE DATABASE
-- ============================================

CREATE DATABASE IF NOT EXISTS pims;

USE pims;


-- ============================================
-- 2. CREATE USERS TABLE
-- ============================================

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Cashier') NOT NULL,
    status ENUM('Active', 'Inactive') NOT NULL DEFAULT 'Active'
);


-- ============================================
-- 3. CREATE SUPPLIERS TABLE
-- ============================================

CREATE TABLE suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(255)
);


-- ============================================
-- 4. CREATE MEDICINES TABLE
-- ============================================

CREATE TABLE medicines (
    medicine_id INT AUTO_INCREMENT PRIMARY KEY,
    medicine_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    supplier_id INT,
    quantity INT NOT NULL DEFAULT 0,
    price DECIMAL(10,2) NOT NULL,
    expiry_date DATE,
    reorder_level INT DEFAULT 10,

    -- Link each medicine to a supplier
    FOREIGN KEY (supplier_id)
        REFERENCES suppliers(supplier_id)
);


-- ============================================
-- 5. CREATE SALES TABLE
-- ============================================

CREATE TABLE sales (
    sale_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    sale_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,

    -- Link each sale to the user who processed it
    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);


-- ============================================
-- 6. CREATE SALE ITEMS TABLE
-- ============================================

CREATE TABLE sale_items (
    sale_item_id INT AUTO_INCREMENT PRIMARY KEY,
    sale_id INT NOT NULL,
    medicine_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,

    -- Link each item to a sale
    FOREIGN KEY (sale_id)
        REFERENCES sales(sale_id),

    -- Link each item to a medicine
    FOREIGN KEY (medicine_id)
        REFERENCES medicines(medicine_id)
);


-- ============================================
-- 7. TEST DATA: USERS
-- ============================================

-- Demo Admin account
INSERT INTO users (username, password, role, status)
VALUES ('admin', 'admin123', 'Admin', 'Active');

-- Demo Cashier account
INSERT INTO users (username, password, role, status)
VALUES ('cashier', 'cashier123', 'Cashier', 'Active');


-- ============================================
-- 8. TEST DATA: SUPPLIERS
-- ============================================

INSERT INTO suppliers
    (supplier_name, contact_person, phone, email, address)
VALUES
    ('PharmaCare Distributors', 'John Smith', '0115551234',
     'pharmacare@example.com', 'Johannesburg');

INSERT INTO suppliers
    (supplier_name, contact_person, phone, email, address)
VALUES
    ('MediSupply SA', 'Sarah Williams', '0115555678',
     'medisupply@example.com', 'Pretoria');


-- ============================================
-- 9. TEST DATA: MEDICINES
-- ============================================

-- supplier_id = 1 refers to PharmaCare Distributors
INSERT INTO medicines
    (medicine_name, category, supplier_id, quantity, price, expiry_date, reorder_level)
VALUES
    ('Paracetamol 500mg', 'Painkiller', 1, 100, 25.50, '2028-06-30', 20);

-- supplier_id = 2 refers to MediSupply SA
INSERT INTO medicines
    (medicine_name, category, supplier_id, quantity, price, expiry_date, reorder_level)
VALUES
    ('Amoxicillin 500mg', 'Antibiotic', 2, 50, 45.00, '2027-12-31', 10);

-- supplier_id = 1 refers to PharmaCare Distributors
INSERT INTO medicines
    (medicine_name, category, supplier_id, quantity, price, expiry_date, reorder_level)
VALUES
    ('Ibuprofen 200mg', 'Painkiller', 1, 75, 30.00, '2028-03-15', 15);


-- ============================================
-- 10. TEST DATA: SALES
-- ============================================

-- user_id = 2 refers to the demo Cashier account
INSERT INTO sales (user_id, total_amount)
VALUES (2, 81.00);


-- ============================================
-- 11. TEST DATA: SALE ITEMS
-- ============================================

-- 2 Paracetamol at R25.50 each
INSERT INTO sale_items
    (sale_id, medicine_id, quantity, unit_price)
VALUES
    (1, 1, 2, 25.50);

-- 1 Ibuprofen at R30.00
INSERT INTO sale_items
    (sale_id, medicine_id, quantity, unit_price)
VALUES
    (1, 3, 1, 30.00);


-- ============================================
-- 12. DATABASE SETUP COMPLETE
-- ============================================

