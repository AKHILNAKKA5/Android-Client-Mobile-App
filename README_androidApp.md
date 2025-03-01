
# Employee Recognition Program 

This project is part of **CS550 - Database Systems** and implements **Project Component 4: Server-side Business Logic and Android Client Mobile App** for the **Employee Recognition Program**.

---

## 📂 Project Structure

### 1. Server-Side (Java Servlet + JSP Files)

Location: `/sleepyhollow/`

- `Login.java` (Servlet) - Handles user authentication.
- `Info.jsp` - Fetches employee name and sales data.
- `Transactions.jsp` - Lists all transactions for an employee.
- `TransactionDetails.jsp` - Shows transaction details (products, prices, quantities).
- `AwardIds.jsp` - Fetches award IDs for an employee.
- `GrantedDetails.jsp` - Fetches award date and center name for a given award ID.
- `Transfer.jsp` - Transfers balance between employees.

#### Technologies
- Apache Tomcat
- JDBC (Oracle)
- JSP
- Java Servlet

### 2. Android Client (Mobile App)

Location: `/CS550_ProjectComponent4_AndroidStudio/`

- **Activities:**
    - `MainActivity.java` - Login Screen
    - `MainActivity2.java` - Employee Info & Photo Display
    - `Transactions.java` - Transaction List
    - `TransactionDetails.java` - Transaction Details
    - `AwardDetails.java` - Award Info
    - `Transfer.java` - Funds Transfer

#### Technologies
- Android Studio
- Java
- AsyncTask (for networking)
- HttpURLConnection (for server communication)

---

## 📊 Database Design

### Tables
- `LOGIN` - Stores username, password, and employee SSN.
- `EMPLOYEES` - Stores employee personal info and sales data.
- `TRANSACTIONS` - Stores transaction details linked to employees.
- `PRODUCTS` - Lists products for each transaction.
- `AWARDS` - Stores award history for employees.

---

## ⚙️ Setup Instructions

### 1. Server Setup
- Deploy `/sleepyhollow` folder into **Apache Tomcat's webapps directory**.
- Configure **Oracle Database Connection** in each JSP and Servlet (JDBC URL, username, password).
- Start Tomcat.

### 2. Android App Setup
- Open `/CS550_ProjectComponent4_AndroidStudio/` in Android Studio.
- Use Emulator (or physical device).
- Update server IP in `MainActivity.java` (replace `10.0.2.2` with your local machine IP if running on physical device).
- Build and run.

---

## 📺 Demo Video

Refer to **vlink.txt** for the link to the 15-minute video demonstration.

---

## 📁 File Structure

```
P4_Nakka_G01411969/
├── sleepyhollow/
│   ├── AwardIds.jsp
│   ├── GrantedDetails.jsp
│   ├── Info.jsp
│   ├── Transactions.jsp
│   ├── TransactionDetails.jsp
│   ├── Transfer.jsp
│   ├── img/ (employee photos)
│   ├── WEB-INF/classes/
│       └── Login.class
├── CS550_ProjectComponent4_AndroidStudio/
│   ├── app/
│       ├── src/main/java/com/example/login/
│       │   ├── MainActivity.java
│       │   ├── MainActivity2.java
│       │   ├── Transactions.java
│       │   ├── TransactionDetails.java
│       │   ├── AwardDetails.java
│       │   ├── Transfer.java
│       ├── res/layout/
│       │   ├── activity_main.xml
│       │   ├── activity_main2.xml
│       │   ├── activity_transactions.xml
│       │   ├── activity_transaction_details.xml
│       │   ├── activity_award_details.xml
│       │   ├── activity_transfer.xml
├── vlink.txt (demo video link)
├── 


## 📌 Key Features

✅ 3-tier architecture (Database, Server, Client)  
✅ JSP & Servlets for server logic  
✅ Android app for employee access  
✅ Employee photo display  
✅ Award & transaction history retrieval  
✅ Funds transfer between employees

---

## 🚀 Technologies Used

- **Java (Servlet, Android)**
- **JSP & HTML**
- **Oracle Database**
- **Android Studio**
- **Apache Tomcat**
- **JDBC**

Note: Note: We've included the TestServlet folder as well for reference to access the login servlet code.

## 📧 Contact

For questions or clarifications, please contact:  
**Akhil Nakka** - akhilnakkaus5@gmail.com
