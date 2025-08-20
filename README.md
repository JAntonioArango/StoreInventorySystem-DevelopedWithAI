# Inventory Management System

A simple **Spring Boot Inventory Management System** that provides REST APIs for managing products.  
It demonstrates clean code, layered architecture, unit tests, and integration with **MySQL**.

---

## 🚀 Features
- Manage products with CRUD operations
- RESTful API with JSON
- Validation on input data
- Unit tests with **JUnit + Mockito + MockMvc**
- Code quality via **Checkstyle**
- Code coverage via **JaCoCo**

---

## 🛠 Tech Stack
- Java 17
- Spring Boot 3
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- JUnit 5 + Mockito
- JaCoCo (95% code coverage)

---

## ⚙️ Setup MySQL

1. Install MySQL (local or Docker).
2. Create a database:
3. Create user, password and edit them in application.properties

```sql
CREATE DATABASE inventory_db;
```

---
##  💫️ Run the App
1. mvn clean install
2. mvn clean verify


---

##  ♻ Short feedback:
- Was it easy to complete the task using AI? I started using an agent to create step-by-step prompts for developing the app

- How long did task take you to complete? 2 hours

- Was the code ready to run after generation? I had to ignore several of the suggestions, the LLM was going so fast and I wanted to go step by step. I also had to start from scratch again, it was designing it overly complex, and I want it simple and fast, I spent 20 mins with the first build that I discarded. Then I had to fix many bugs related to database, plugins and test cases.

- What did you have to change to make it usable? I had to change the domain model every time because I didn't set it from the beginning. Had to import ProductService class for the test, the LLM forgot to add it at the beginning. Also, the database settings

- Which challenges did you face during completion of the task? To make each part cohesive and not overly complex

- Which specific prompts you learned as a good practice to complete the task? Initial Context Prompt
  "You are an experienced Spring Boot developer. I need you to help me create a simple inventory management system step by step. Focus on clean, readable code with good practices. Keep explanations concise and practical"


