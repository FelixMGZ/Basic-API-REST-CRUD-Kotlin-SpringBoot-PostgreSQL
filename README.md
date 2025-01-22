# Basic API REST CRUD with Kotlin, Spring Boot, and PostgreSQL

This document describes step-by-step how to create a CRUD API developed with Kotlin, Spring Boot, and PostgreSQL.

---
Introduction

This guide is designed to help developers build a CRUD API from scratch using Kotlin, Spring Boot, and PostgreSQL. CRUD stands for Create, Read, Update, and Delete, which represent the basic operations of persistent storage. By following this guide, you will:

Set up a Spring Boot project with Kotlin.

Create an API for managing a product database.

Connect the API to a PostgreSQL database.

Learn best practices for structuring a backend project.

Test the API endpoints using Postman.

This guide is ideal for developers looking to enhance their skills in Kotlin and Spring Boot or for those building a backend project with relational databases.

## 1. **Project Description**

This project consists of a CRUD API that manages products. Each product has the following fields:

- **ID**: Unique identifier for the product.
- **nameProduct**: Name of the product.
- **descriptionProduct**: Description of the product.
- **price**: Price of the product.

The goal is to implement the API using the following technologies:

- **Kotlin** as the programming language.
- **Spring Boot** for backend configuration.
- **PostgreSQL** as the database.

---

## 2. **Initial Configuration**

### Create the Project in Spring Initializr

1. Go to [Spring Initializr](https://start.spring.io/).
2. Configure the project with the following details:
   - **Project**: Gradle - Kotlin
   - **Spring Boot**: 3.4.1
   - **Dependencies**:
     - Spring Web
     - Spring Data JPA
     - PostgreSQL Driver
   - **Packaging**: Jar
   - **Java version**: 17
3. Download the project and open it in your preferred editor (e.g., Visual Studio Code or IntelliJ IDEA).

---

## 3. **Project Development**

### Create the Product Entity

In the folder `src/main/kotlin/com/example/product_api/model`, create a file `Product.kt` with the following content:

```kotlin
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nameProduct: String,
    val descriptionProduct: String,
    val price: Double
)
```

### Configure the Repository

Create an interface to interact with the database in `src/main/kotlin/com/example/product_api/repository/ProductRepository.kt`:

```kotlin
import org.springframework.data.jpa.repository.JpaRepository
import com.example.product_api.model.Product

interface ProductRepository : JpaRepository<Product, Long>
```

### Create the Service

In `src/main/kotlin/com/example/product_api/service/ProductService.kt`:

```kotlin
import org.springframework.stereotype.Service
import com.example.product_api.model.Product
import com.example.product_api.repository.ProductRepository

@Service
class ProductService(private val repository: ProductRepository) {
    fun getAllProducts(): List<Product> = repository.findAll()
    fun getProductById(id: Long): Product? = repository.findById(id).orElse(null)
    fun createProduct(product: Product): Product = repository.save(product)
    fun updateProduct(id: Long, product: Product): Product? {
        return if (repository.existsById(id)) {
            repository.save(product)
        } else {
            null
        }
    }
    fun deleteProduct(id: Long) = repository.deleteById(id)
}
```

### Configure the Controller

In `src/main/kotlin/com/example/product_api/controller/ProductController.kt`:

```kotlin
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.product_api.model.Product
import com.example.product_api.service.ProductService

@RestController
@RequestMapping("/products")
class ProductController(private val service: ProductService) {

    @GetMapping
    fun getAllProducts(): List<Product> = service.getAllProducts()

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> {
        val product = service.getProductById(id)
        return if (product != null) ResponseEntity.ok(product) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createProduct(@RequestBody product: Product): Product = service.createProduct(product)

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody product: Product): ResponseEntity<Product> {
        val updatedProduct = service.updateProduct(id, product)
        return if (updatedProduct != null) ResponseEntity.ok(updatedProduct) else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        service.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}
```

---

## 4. **Configure PostgreSQL Connection**

Edit the file `src/main/resources/application.properties` to add the connection credentials:

```properties
spring.application.name=product-api
spring.datasource.url=jdbc:postgresql://localhost:5432/productdb
spring.datasource.username=postgres
spring.datasource.password=********
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
server.port=8081
```

---

## 5. **Create the PostgreSQL Database and Table**

### Connect to PostgreSQL
1. Open your PostgreSQL client (e.g., pgAdmin or psql CLI).
2. Log in with your credentials.

### Create the Database
Run the following SQL command to create the database:

```sql
CREATE DATABASE your_database;
```

### Create the Table
Switch to the newly created database and run the following SQL command:

```sql
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name_product VARCHAR(255) NOT NULL,
    description_product TEXT NOT NULL,
    price NUMERIC(10, 2) NOT NULL
);
```

This matches the structure of the `Product` entity in the code.

---

## 6. **Run the code**

### in the terminal write

```bash
./gradlew bootRun
```

---
## 7. **Test the API with Postman**

1. Use Postman to perform the following operations:
   - **GET** `http://localhost:8080/products`
   - **POST** `http://localhost:8080/products` with a JSON body:
     ```json
     {
         "nameProduct": "Product 1",
         "descriptionProduct": "Product description",
         "price": 19.99
     }
     ```

---

Thanks for watching and happy coding.

---
