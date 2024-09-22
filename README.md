# E-Commerce Order Management System

## Overview

This project is an E-Commerce Order Management System built using Spring Boot. It provides RESTful APIs for managing orders, including creating, updating, retrieving, and deleting orders. It also allows searching and sorting of orders by customer name and status.

## Features

- **Create Orders**: Admin can create new orders.
- **Retrieve Orders**: Fetch all orders with options to filter by customer name and status.
- **Update Orders**: Admin can update the status of existing orders.
- **Delete Orders**: Admin can delete orders.
- **Search and Sort**: Orders can be searched by customer name or status, and sorted by date created.

## Technologies Used

- **Spring Boot**: 3.3.4
- **Spring Security**: For authentication and authorization.
- **Spring Web**: For building RESTful APIs.
- **Spring Validation**: For input validation.
- **Spring DevTools**: For enhanced development experience (optional).
- **Springdoc OpenAPI**: 2.2.0 for API documentation.
- **Lombok**: For reducing boilerplate code (optional).
- **JUnit & Mockito**: For unit testing and mocking.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- IDE (IntelliJ IDEA, Eclipse, etc.)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/order-management-system.git
   cd order-management-system

2. Build the project:
    mvn clean install

3. Run the application:
    mvn spring-boot:run

4. Access the application at http://localhost:8080.



API Documentation
    The API documentation is available at: http://localhost:8080/swagger-ui/index.html#/



Endpoints

1. **Create Order**
   - **Method**: POST
   - **Endpoint**: /api/v1/orders
   - **Request Body**: OrderRequest
   - **Access**: Admin only

2. **Get Orders**
   - **Method**: GET
   - **Endpoint**: /api/v1/orders
   - **Query Parameters**: customerName, sort, status
   - **Access**: Public

3. **Get Order by ID**
   - **Method**: GET
   - **Endpoint**: /api/v1/orders/{id}
   - **Access**: Public

4. **Update Order**
   - **Method**: PUT
   - **Endpoint**: /api/v1/orders/{id}
   - **Request Body**: OrderUpdateRequest
   - **Access**: Admin only

5. **Delete Order**
   - **Method**: DELETE
   - **Endpoint**: /api/v1/orders/{id}
   - **Access**: Admin only

6. **Get Order History for Customer**
   - **Method**: GET
   - **Endpoint**: /api/v1/orders/customer/{customerName}
   - **Access**: Public



Testing
--------
To run the tests, use the following command:

    mvn test

