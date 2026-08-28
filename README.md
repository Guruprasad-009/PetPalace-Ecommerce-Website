# 🐾 PetPalace – Pet Supplies E-Commerce Website

PetPalace is a full-stack E-Commerce web application designed for purchasing pet supplies online. The application provides a user-friendly shopping experience where users can browse products, view product details, manage their accounts, and interact with the online store.

## 📌 Project Overview

The goal of PetPalace is to build a practical E-Commerce platform for pet products such as pet food, toys, grooming products, accessories, and other pet-care items.

The project demonstrates full-stack application development using Java, Spring Boot, MySQL, Thymeleaf, HTML, CSS, and JavaScript.

## ✨ Features

### 👤 User Features

* User Registration
* User Login
* User Authentication
* User Session Management
* User Dashboard
* Logout Functionality

### 🛍️ Product Features

* View Products
* Product Details
* Product Search
* Product Quantity Management
* Add Product
* Edit Product
* Delete Product
* Product Listing

### ❤️ Shopping Features

* Wishlist functionality
* Product browsing
* Product management
* User-friendly shopping interface

### 🎨 UI Features

* Responsive design
* Modern E-Commerce interface
* Navigation bar
* Product cards
* Clean and user-friendly layouts

## 🛠️ Technologies Used

### Frontend

* HTML5
* CSS3
* JavaScript
* Bootstrap
* Thymeleaf

### Backend

* Java
* Spring Boot
* Spring MVC
* Spring Data JPA
* Hibernate

### Database

* MySQL

### Build Tool

* Maven

### Development Tools

* Eclipse / IntelliJ IDEA
* MySQL Workbench
* Git
* GitHub

## 🏗️ Application Architecture

```text
User
 │
 ▼
Frontend / Thymeleaf
 │
 ▼
Spring MVC Controller
 │
 ▼
Service Layer
 │
 ▼
Repository Layer
 │
 ▼
MySQL Database
```

## 📂 Project Structure

```text
PetPalace-Ecommerce-Website/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── controllers/
│   │   │   ├── services/
│   │   │   ├── repositories/
│   │   │   └── entities/
│   │   │
│   │   └── resources/
│   │       ├── templates/
│   │       ├── static/
│   │       └── application.properties
│
├── pom.xml
└── README.md
```

## ⚙️ Prerequisites

Make sure the following software is installed:

* Java JDK 17+
* Maven
* MySQL
* MySQL Workbench
* Git
* IDE such as Eclipse or IntelliJ IDEA

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Guruprasad-009/PetPalace-Ecommerce-Website.git
```

### 2. Navigate to the Project

```bash
cd PetPalace-Ecommerce-Website
```

### 3. Configure MySQL

Create a MySQL database and update the database configuration inside:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/petpalace
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Open the application in your browser:

```text
http://localhost:8080
```

## 🎯 Key Learning Outcomes

Through this project, I gained practical experience in:

* Java application development
* Spring Boot
* Spring MVC
* Spring Data JPA
* Hibernate
* MySQL database integration
* CRUD operations
* User authentication
* Session management
* Thymeleaf
* MVC architecture
* REST/backend concepts
* Maven project management
* Git and GitHub

## 🔗 Repository

[PetPalace-Ecommerce-Website](https://github.com/Guruprasad-009/PetPalace-Ecommerce-Website)

## 👨‍💻 Author

**Guruprasad Halde**

Java Full Stack Developer | Java | Spring Boot | MySQL
