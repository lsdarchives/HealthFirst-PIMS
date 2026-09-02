# HealthFirst Pharmacy Inventory Management System (PIMS)

A Java Swing desktop application developed for **HealthFirst Pharmacy** as part of the **Programming 732** assignment.

The Pharmacy Inventory Management System (PIMS) is designed to manage pharmacy inventory, process customer sales, control user access based on roles, and generate useful business reports.

---

## 📌 Project Overview

HealthFirst Pharmacy requires a digital system to replace manual inventory and sales record-keeping.

The system allows administrators to manage medicines, suppliers and cashier accounts, while cashiers can process sales through a Point-of-Sale (POS) interface.

The application is built as a desktop application using:

* **Java**
* **Java Swing / AWT**
* **IntelliJ IDEA Swing UI Designer**
* **JDBC**
* **MySQL**
* **Git & GitHub**

The assignment requires Swing/AWT for the graphical interface and JDBC for database connectivity.

---

## 🎯 Objectives

The main objectives of PIMS are to:

* Manage pharmacy medicine stock
* Track medicine quantities and expiry dates
* Manage medicine suppliers
* Process customer sales
* Generate customer bills
* Provide role-based access
* Manage cashier accounts
* Generate business and inventory reports
* Store application data in a relational MySQL database

---

# 👥 User Roles

The system contains two main user roles.

## Administrator

Administrators have access to:

* Admin Dashboard
* Medicine Management
* Supplier Management
* User Management
* Reports

Administrators can perform full CRUD operations on medicines and suppliers and manage cashier accounts.

## Cashier

Cashiers have access to:

* Cashier Dashboard
* Point of Sale (POS)
* Stock checking
* Cart management
* Checkout
* Bill generation

Cashiers cannot add or edit medicines.

---

# 🔐 Authentication

The system uses a login-based authentication system.

Users enter:

```text
Username
Password
```

After successful authentication, the system checks the user's role.

```text
                 LOGIN
                   │
                   ▼
             Authenticate
                   │
          ┌────────┴────────┐
          │                 │
       ADMIN             CASHIER
          │                 │
          ▼                 ▼
   Admin Dashboard    Cashier Dashboard
```

Incorrect credentials result in an appropriate error message.

There is **no public registration screen**. Cashier accounts are created and managed by an administrator through the User Management module.

---

# 🖥️ System Modules

## 1. Authentication Module

Features:

* Login screen
* Username validation
* Password validation
* Database authentication
* Role identification
* Role-based dashboard redirection
* Login error messages

---

## 2. Administrator Module

### Medicine Management

Administrators can:

* Add medicines
* View medicines
* Update medicines
* Delete medicines
* Search medicines
* View stock quantities
* View expiry dates
* View suppliers
* Monitor reorder levels

### Supplier Management

Administrators can:

* Add suppliers
* View suppliers
* Update suppliers
* Delete suppliers

### User Management

Administrators can:

* Create cashier accounts
* View users
* Manage cashier accounts
* Delete cashier accounts

### Reports

The system provides:

* Sales Report
* Item-Wise Sales Report
* Low Stock Report
* Expiry Report

---

# 🧾 Cashier Point of Sale

The cashier module provides a Point-of-Sale interface for processing sales.

The cashier can:

1. Search/check available medicines
2. Select medicines
3. Enter quantities
4. Add items to the cart
5. View the cart
6. Calculate the total
7. Remove/clear cart items
8. Complete checkout
9. Generate a bill
10. Save or print the bill

The POS is implemented as a simulation as required by the assignment.

---

# 🧾 Billing

After checkout, the system generates a customer bill containing relevant transaction information, such as:

* Sale number
* Date and time
* Cashier
* Medicine
* Quantity
* Price
* Subtotal
* Total amount

The completed transaction is stored in the database.

---

# 📊 Reports

The system provides four reports.

## Sales Report

Displays information about completed sales and overall sales performance.

## Item-Wise Sales Report

Shows sales information grouped by individual medicines.

## Low Stock Report

Identifies medicines whose stock has reached or fallen below their defined reorder level.

## Expiry Report

Identifies medicines that are approaching their expiry date.

---

# 🗄️ Database

The application uses **MySQL** as its relational database.

The database contains the following tables:

```text
users
suppliers
medicines
sales
sale_items
```

### Database Relationships

```text
USERS
  │
  │ 1
  │
  │
  ▼
SALES
  │
  │ 1
  │
  ▼
SALE_ITEMS
  │
  │ *
  │
  ▼
MEDICINES
  │
  │ *
  │
  ▼
SUPPLIERS
```

### Main Tables

#### users

Stores login credentials and user roles.

```text
user_id
username
password
role
full_name
```

#### suppliers

Stores supplier information.

```text
supplier_id
name
contact_person
phone
email
address
```

#### medicines

Stores pharmacy inventory.

```text
medicine_id
name
company
medicine_type
price
quantity_in_stock
reorder_level
expiry_date
supplier_id
```

#### sales

Stores transaction information.

```text
sale_id
sale_date
total_amount
user_id
```

#### sale_items

Stores individual items belonging to each sale.

```text
sale_item_id
sale_id
medicine_id
quantity_sold
price_at_sale
```

---

# 🏗️ Project Architecture

The project follows a simple layered structure to keep the application organised and easy to maintain.

```text
GUI
 │
 ▼
DAO / Business Logic
 │
 ▼
JDBC
 │
 ▼
MySQL Database
```

Recommended project structure:

```text
src/
└── pims/
    ├── Main.java
    │
    ├── model/
    │   ├── User.java
    │   ├── Medicine.java
    │   ├── Supplier.java
    │   ├── Sale.java
    │   └── SaleItem.java
    │
    ├── database/
    │   └── DatabaseConnection.java
    │
    ├── dao/
    │   ├── UserDAO.java
    │   ├── MedicineDAO.java
    │   ├── SupplierDAO.java
    │   ├── SaleDAO.java
    │   └── ReportDAO.java
    │
    └── gui/
        ├── LoginFrame.java
        ├── AdminDashboard.java
        ├── CashierDashboard.java
        ├── MedicinePanel.java
        ├── SupplierPanel.java
        ├── UserPanel.java
        ├── POSPanel.java
        └── ReportsPanel.java
```

The exact structure may change during development as the application grows.

---

# 🎨 GUI Development

The graphical interface is developed using **Java Swing**.

IntelliJ IDEA's **Swing UI Designer** is used to create the GUI using drag-and-drop.

Components may include:

* JFrame
* JPanel
* JLabel
* JTextField
* JPasswordField
* JButton
* JTable
* JScrollPane
* JComboBox
* JTabbedPane
* Layout Managers

The visual design is created using the GUI designer while application logic is maintained in Java code.

---

# 🔨 Development Phases

The project is being developed incrementally. Each completed phase is committed to Git so that the development history can be tracked.

---

## Phase 1 — Project Setup

### Tasks

* Create IntelliJ IDEA project
* Create Java package structure
* Configure Swing UI Designer
* Create initial JFrame / UI forms
* Set up Git repository
* Create GitHub repository
* Add `.gitignore`
* Create initial README

### Git Commit

```bash
git add .
git commit -m "chore: initialize PIMS project"
git push
```

---

## Phase 2 — MySQL Database

### Tasks

* Create PIMS database
* Create `users` table
* Create `suppliers` table
* Create `medicines` table
* Create `sales` table
* Create `sale_items` table
* Add primary keys
* Add foreign keys
* Add sample data
* Create `database.sql`

### Git Commit

```bash
git add .
git commit -m "feat: create MySQL database schema and sample data"
git push
```

---

## Phase 3 — JDBC Database Connection

### Tasks

* Add MySQL JDBC driver
* Create `DatabaseConnection.java`
* Connect Java application to MySQL
* Test database connection
* Test basic SELECT operation
* Test INSERT operation
* Test UPDATE operation
* Test DELETE operation

### Git Commit

```bash
git add .
git commit -m "feat: add JDBC database connection"
git push
```

---

## Phase 4 — Login & Authentication

### Tasks

* Create login GUI using Swing UI Designer
* Add username field
* Add password field
* Add Login button
* Connect login to MySQL
* Validate credentials
* Retrieve user role
* Redirect Admin to Admin Dashboard
* Redirect Cashier to Cashier Dashboard
* Display login errors

### Git Commit

```bash
git add .
git commit -m "feat: implement user authentication and role-based login"
git push
```

---

## Phase 5 — Admin Dashboard

### Tasks

Create the administrator dashboard and navigation structure.

Tabs/sections:

```text
Admin Dashboard
│
├── Medicines
├── Suppliers
├── Users
└── Reports
```

### Git Commit

```bash
git add .
git commit -m "feat: add administrator dashboard"
git push
```

---

## Phase 6 — Medicine Management

### Tasks

Implement medicine CRUD:

* Create medicine
* Read/view medicines
* Update medicine
* Delete medicine
* Search medicine
* Display medicines in JTable
* Supplier selection
* Stock quantity
* Reorder level
* Expiry date

### Git Commit

```bash
git add .
git commit -m "feat: implement medicine management CRUD"
git push
```

---

## Phase 7 — Supplier Management

### Tasks

Implement supplier CRUD:

* Add supplier
* View suppliers
* Update supplier
* Delete supplier
* Display suppliers in JTable

### Git Commit

```bash
git add .
git commit -m "feat: implement supplier management CRUD"
git push
```

---

## Phase 8 — User Management

### Tasks

Implement administrator-controlled cashier management:

* Add cashier
* View users
* Update cashier details
* Delete cashier
* Assign Cashier role
* Validate usernames

There is no public registration screen.

### Git Commit

```bash
git add .
git commit -m "feat: implement cashier user management"
git push
```

---

## Phase 9 — Cashier POS

### Tasks

Create the cashier Point-of-Sale interface.

Features:

* Medicine search
* Stock checking
* Add item to cart
* Set quantity
* Remove item
* Clear cart
* Calculate subtotal
* Calculate total

### Git Commit

```bash
git add .
git commit -m "feat: implement cashier POS and shopping cart"
git push
```

---

## Phase 10 — Sales & Billing

### Tasks

* Create sales transaction
* Store sale header
* Store sale items
* Update medicine stock
* Calculate total
* Generate bill
* Display bill window
* Save/print bill
* Prevent selling more stock than available

### Git Commit

```bash
git add .
git commit -m "feat: implement sales processing and billing"
git push
```

---

## Phase 11 — Reports

### Tasks

Implement:

* Sales Report
* Item-Wise Sales Report
* Low Stock Report
* Expiry Report

Reports will retrieve information directly from the MySQL database.

### Git Commit

```bash
git add .
git commit -m "feat: implement inventory and sales reports"
git push
```

---

## Phase 12 — Testing & UI Improvements

### Tasks

* Test login
* Test incorrect credentials
* Test Admin access
* Test Cashier access
* Test medicine CRUD
* Test supplier CRUD
* Test user management
* Test POS
* Test stock deduction
* Test billing
* Test reports
* Fix bugs
* Improve GUI layout
* Improve labels and button placement
* Improve error messages
* Add validation
* Test database relationships

### Git Commit

```bash
git add .
git commit -m "test: complete system testing and UI improvements"
git push
```

---

## Phase 13 — Final Packaging

### Tasks

* Final application testing
* Prepare executable
* Verify MySQL database setup
* Verify `database.sql`
* Prepare source code
* Capture required screenshots
* Create final `README.txt`
* Create final ZIP
* Verify ZIP is below 50 MB
* Test final submission package

### Git Commit

```bash
git add .
git commit -m "release: prepare final PIMS submission"
git push
```

Optional release tag:

```bash
git tag v1.0.0
git push origin v1.0.0
```

---

# 📁 Final Repository Structure

The planned GitHub repository will contain something similar to:

```text
PIMS/
│
├── src/
│   └── pims/
│       ├── model/
│       ├── database/
│       ├── dao/
│       └── gui/
│
├── screenshots/
│   ├── login.png
│   ├── admin-dashboard.png
│   ├── medicines.png
│   ├── cashier-pos.png
│   ├── bill.png
│   ├── sales-report.png
│   ├── item-wise-report.png
│   ├── low-stock-report.png
│   └── expiry-report.png
│
├── database.sql
├── README.md
├── README.txt
├── .gitignore
└── pom.xml / build configuration
```

The final academic submission will additionally contain the required executable and other submission files.

---

# 🧪 Testing Strategy

Testing will be performed throughout development instead of waiting until the end.

Examples:

### Authentication

```text
Correct Admin credentials → Admin Dashboard
Correct Cashier credentials → Cashier Dashboard
Incorrect credentials → Error message
```

### Medicine Management

```text
Add → Medicine appears
Update → Information changes
Delete → Medicine is removed
Search → Correct medicine appears
```

### POS

```text
Select medicine
      ↓
Enter quantity
      ↓
Add to cart
      ↓
Calculate total
      ↓
Checkout
      ↓
Save sale
      ↓
Deduct stock
      ↓
Generate bill
```

### Reports

Reports will be tested against known database data to ensure that the displayed results are accurate.

---

# 🔑 Default Login Credentials

The default test accounts will be documented here once the database is finalised.

Example:

```text
Administrator
Username: admin
Password: admin123

Cashier
Username: cashier
Password: cash123
```

**Note:** These credentials are for demonstration/testing purposes only.

---

# 🚀 Running the Project

## Requirements

* Windows
* Java JDK
* IntelliJ IDEA
* MySQL Server
* MySQL Workbench or another MySQL client
* MySQL JDBC Driver

## Database Setup

1. Start MySQL Server.
2. Open MySQL Workbench or another MySQL client.
3. Open `database.sql`.
4. Execute the script.
5. Confirm that the PIMS database and tables have been created.
6. Check the database connection settings in the Java application.

## Running the Application

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Configure the Java SDK.
4. Configure the MySQL JDBC dependency.
5. Ensure MySQL Server is running.
6. Verify the database connection settings.
7. Run `Main.java`.

---

# 📸 Screenshots

The project will contain screenshots demonstrating the completed functionality.

Required screenshots include:

* Login Screen
* Admin Dashboard with tabs
* Manage Medicines with data
* Cashier POS with items in cart
* Generated Bill
* Sales Report
* Item-Wise Report
* Low Stock Report
* Expiry Report

---

# 📚 Assignment Requirements

This project is developed according to the Programming 732 Pharmacy Inventory Management System requirements.

The required technology includes:

* Java Swing/AWT
* JDBC
* MySQL
* Role-based authentication
* Inventory management
* Sales processing
* Reporting

The database includes the required `users`, `suppliers`, `medicines`, `sales`, and `sale_items` tables.

---

# 📦 Academic Submission

The final academic submission will be packaged according to the assignment requirements.

Expected structure:

```text
YourName_StudentNumber_PRO732.zip
│
├── yourname_pims.exe
├── src/
├── database.sql
├── screenshots/
└── README.txt
```

The final ZIP must not exceed **50 MB**.

---

# 📈 Development Progress

| Phase | Feature                   | Status |
| ----- | ------------------------- | ------ |
| 1     | Project Setup             | ⬜      |
| 2     | MySQL Database            | ⬜      |
| 3     | JDBC Connection           | ⬜      |
| 4     | Login & Authentication    | ⬜      |
| 5     | Admin Dashboard           | ⬜      |
| 6     | Medicine Management       | ⬜      |
| 7     | Supplier Management       | ⬜      |
| 8     | User Management           | ⬜      |
| 9     | Cashier POS               | ⬜      |
| 10    | Sales & Billing           | ⬜      |
| 11    | Reports                   | ⬜      |
| 12    | Testing & UI Improvements | ⬜      |
| 13    | Final Packaging           | ⬜      |

---

# 📝 Git Commit Strategy

Each major development phase will be committed separately.

The commit messages follow a simple convention:

```text
feat:      new functionality
fix:       bug fix
test:      testing changes
docs:      documentation
chore:     setup/configuration
refactor:  code restructuring
release:   final release
```

Examples:

```text
chore: initialize PIMS project
feat: create MySQL database schema
feat: add JDBC database connection
feat: implement authentication
feat: add administrator dashboard
feat: implement medicine CRUD
feat: implement supplier management
feat: implement cashier user management
feat: implement cashier POS
feat: implement sales and billing
feat: implement reports
test: complete system testing and UI improvements
release: prepare final PIMS submission
```

This allows the GitHub repository to show the progression of the system from initial setup to the completed application.

---

# 👨‍💻 Development Approach

The system is being developed incrementally.

Each phase will be:

1. Planned
2. Implemented
3. Tested
4. Committed to Git
5. Pushed to GitHub
6. Reviewed before moving to the next phase

This approach makes it easier to identify bugs, maintain the project and track development progress.

---

# ⚠️ Security Note

This is an academic project intended for demonstration and assessment.

Production pharmacy systems would require stronger security measures, including secure password hashing, proper access control, audit logging, encrypted connections and additional validation.

---

# 📄 License

This project was developed as an academic assignment for Programming 732.

© 2026 Lesedi Moremedi. All rights reserved.

