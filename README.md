\# HealthFirst Pharmacy Inventory Management System



A desktop-based Pharmacy Inventory Management System developed using Java, Swing, MySQL, and JDBC.



The system is designed to help pharmacy staff manage medicines, suppliers, users, sales, and reports through a role-based interface.



\## Technologies



\- Java JDK 25

\- Java Swing / AWT

\- MySQL

\- JDBC

\- Maven

\- IntelliJ IDEA

\- Git \& GitHub



\## System Features



\### Login and Authentication

\- Username and password authentication

\- Admin and Cashier roles

\- Active and Inactive user accounts

\- Role-based access to system features



\### Admin Dashboard

\- Pharmacy management dashboard

\- Inventory statistics

\- Sales information

\- Navigation to management modules



\### Medicine Management

\- Add medicines

\- Edit medicine information

\- Delete medicines

\- Search medicines

\- Track stock quantities

\- Track prices and expiry dates

\- Configure reorder levels

\- Identify low-stock medicines



\### Supplier Management

\- Add suppliers

\- Edit supplier information

\- Delete suppliers

\- Search suppliers

\- Store supplier contact information



\### User Management

\- Add system users

\- Edit user information

\- Activate or deactivate accounts

\- Delete users where permitted

\- Search users by username, role, or status



\### Point of Sale

\- Search available medicines

\- Add medicines to a cart

\- Adjust quantities

\- Calculate sale totals

\- Complete sales

\- Update medicine stock

\- Generate a bill

\- Clear the cart



\### Reporting

\- Sales reports

\- Expiry reports

\- Display medicines approaching their expiry date



\## Database



The application uses MySQL with the following main tables:



\- `users`

\- `suppliers`

\- `medicines`

\- `sales`

\- `sale\_items`



The database setup script is located at:



`DBS/PIMS.sql`



\## Database Setup



1\. Open MySQL Workbench.

2\. Open `DBS/PIMS.sql`.

3\. Run the script on a MySQL server to create the `pims` database and its tables.

4\. The application connects to the database using JDBC.



The database password is supplied through the `PIMS\_DB\_PASSWORD` environment variable rather than being stored directly in the Java source code.



\## Demo Accounts



The database script contains demonstration accounts for testing:



| Username | Password | Role |

|----------|----------|------|

| admin | admin123 | Admin |

| cashier | cashier123 | Cashier |



These accounts are intended for demonstration and testing purposes.



\## Project Structure



```text

HealthFirst PIMS/

│

├── DBS/

│   └── PIMS.sql

│

├── src/

│   └── main/

│       └── java/

│           └── pims/

│               ├── Dashboard.java

│               ├── DatabaseConnection.java

│               ├── DatabaseTest.java

│               ├── ExpiryReport.java

│               ├── LoginForm.java

│               ├── Main.java

│               ├── MedicineManagement.java

│               ├── PointOfSale.java

│               ├── SalesReport.java

│               ├── SupplierManagement.java

│               └── UserManagement.java

│

├── .gitignore

├── pom.xml

└── README.md

