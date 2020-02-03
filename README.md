### Service Number Validator

This project contains a simple basic business logic related to the validation of international phone numbers.

This project contains a Spring Boot Application.

### Rerequisities and Dependencies

- Java 8;
- Apache Commons Lang;
- Apache HTTP Client;
- SQLITE3;


### Executing Unit Tests
The unit tests of the service can be executed with the following Maven command

```mvn clean install -Punit-tests```

### Features


- [X] Register a new Customer
- [X] Get all Customers
- [X] Delete Customer 
- [X] Get a single Customer
- [X] Update Customer
- [X] Get pageable Customers
- [X] Find Customers by filters


> ### Examples

#### GET 
- http://localhost:9410/customers/pages/?page=0&size=10
- http://localhost:9410/customers/pages/?page=0&size=10&filterBy=country:273
- https://localhost:9410/customers/33
- https://localhost:9410/customers/33/validate


### The API methods that can be invoked

- GET /customers - find all customers;
- GET /customers/{id} - find a single customer by id;
- POST /customers/{id} - create a new Customer;
- PUT /customers{id} - update all data customer;
- PATCH /customers/{id} - update some data customer;
- DELETE /customers{id} - remove a customer;
- GET /customers/page - find all customers pageable with filters (Country, State);
- GET /customers/{id}/validate - check if phone number from customer is valid;

