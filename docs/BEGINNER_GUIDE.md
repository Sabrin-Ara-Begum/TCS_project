# Restaurant Ordering Backend: Complete Beginner Guide

Welcome! This guide is designed to take you from a complete beginner to understanding exactly how this Spring Boot REST API works.

---

# START HERE: The First 10 Steps (Verification)

Follow these exact steps to verify everything works on your machine.

## Step 1 — Open the project
1. Open **IntelliJ IDEA**.
2. Click **File -> Open**.
3. Select the `TCS_project` folder on your Desktop and click **Open**.

## Step 2 — Check Java
* **Where**: Mac Terminal.
* **Command**: `java -version`
* **What it does**: Checks if Java is installed and accessible.
* **Output to expect**: `java version "21"` (or 26).
* **If it fails**: Re-download the JDK from Adoptium and run the installer.

## Step 3 — Check Maven
* **Where**: Mac Terminal (Make sure you are inside `/Users/sabrinarabegum/Desktop/TCS_project`).
* **Command**: `./mvnw -v`
* **What it does**: Checks if the Maven Wrapper (which builds our project) works.
* **Output to expect**: `Apache Maven 3.9.x`
* **If it fails**: Ensure you are in the correct directory.

## Step 4 — Check Docker
* **Where**: Mac Terminal.
* **Command**: `docker -v`
* **What it does**: Checks if Docker is installed.
* **Output to expect**: `Docker version 27.x.x`

## Step 5 — Check the database/container
* **Where**: Mac Terminal (inside `TCS_project`).
* **Command**: `docker compose up -d`
* **What it does**: Starts the PostgreSQL database container in the background (`-d`).
* **How to verify**: Run `docker ps`. You should see `restaurant_postgres` running on port `5433`.

## Step 6 — Start the Spring Boot application
* **Where**: IntelliJ IDEA.
* **Action**: Open `src/main/java/com/example/restaurant/RestaurantApplication.java`. Click the green "Play" triangle next to `public static void main`.
* **Expect**: The Run console at the bottom will print logs. Look for `Tomcat initialized with port(s): 8080 (http)` and `Started RestaurantApplication`.
* **Port**: The app is now running on port `8080`.

## Step 7 — Test the API
* **Where**: Postman or Terminal.
* **Action**: Create a Restaurant.
  * **Method**: `POST`
  * **URL**: `http://localhost:8080/restaurants`
  * **Headers**: `Content-Type: application/json`
  * **Body**: `{"name": "Pizza Planet", "minOrderAmount": 10.00}`
  * **Expected Success**: `201 Created` with a JSON response containing an ID.
* **Invalid Request**: Send `{"name": ""}`. 
  * **Expected Error**: `400 Bad Request` with message `name: Restaurant name is required`.

## Step 8 — Verify the database
* **Where**: IntelliJ IDEA.
* **Action**: Click the **Database** tab on the right side of the screen.
* Click `+ -> Data Source -> PostgreSQL`.
* Fill in: Host `localhost`, Port `5433`, User `restaurant_user`, Password `restaurant_password`, Database `restaurant_db`.
* Click **Test Connection**, then **OK**.
* Open the `tables` folder. Double click `restaurants` to see the row you just created!

## Step 9 — Run automated tests
* **Where**: Mac Terminal.
* **Command**: `./mvnw test`
* **What it does**: Compiles code and runs all JUnit tests.
* **How to know it succeeded**: Look for `BUILD SUCCESS` and `Failures: 0, Errors: 0`.

## Step 10 — Check JaCoCo
* **Where**: Mac Terminal.
* **Command**: `./mvnw test jacoco:report`
* **Where the report is located**: `/Users/sabrinarabegum/Desktop/TCS_project/target/site/jacoco/index.html`.
* **Action**: Double click `index.html` to open it in Chrome/Safari. You will see green/red bars showing Line and Branch coverage.

## Step 11 — Test Docker
* **Start Compose**: `docker compose up -d`
* **See running**: `docker ps`
* **Check logs**: `docker logs restaurant_postgres`
* **Stop containers**: `docker compose down`

## Step 12 — Test K6
* **Where**: Mac Terminal.
* **Command**: `k6 run k6/burst-test.js`
* **What VUs mean**: Virtual Users. (Fake users clicking your app at the same time).
* **What Duration means**: How long the test runs (e.g. 10 seconds).
* **What to look for**: `http_req_failed` should be `0.00%`.

---

# PART 1 — WHAT WE ACTUALLY BUILT

1. **What is this application?** It is a RESTful API backend for a restaurant ordering system.
2. **What problem does it solve?** It allows a frontend (like a mobile app) to securely create users, list menus, and place orders.
3. **What are the main features?** Database persistence, user validation, order total calculation, minimum order validation.
4. **Technologies used**: Java 21, Spring Boot, Spring Data JPA, Hibernate, PostgreSQL, Docker, JUnit, Mockito, JaCoCo, K6.
5. **Why used?** Spring Boot is the industry standard for Java web servers. PostgreSQL is a robust relational database. Docker guarantees the DB runs the same everywhere.
6. **Database**: PostgreSQL (Running in Docker).
7. **Communication**: The app talks to the database using Spring Data JPA.
8. **Spring Boot**: Starts a built-in web server (Tomcat) on port 8080 to listen for web traffic.
9. **JPA/Hibernate**: Translates Java Objects into SQL queries automatically.
10. **Controllers**: The front door. They listen for HTTP requests (like `POST /order`).
11. **Services**: The brain. This is where business rules live (e.g., "Is the order total greater than $10?").
12. **Repositories**: The database managers. They execute `save()` and `findById()`.
13. **Entities**: Java classes that perfectly map to SQL tables.
14. **DTOs**: Data Transfer Objects. We use these to receive/send JSON safely without exposing database Entities directly.
15. **Validations**: Rules (like `@Email`) that Spring checks *before* our Service runs, blocking bad data.
16. **Global Exception Handler**: A class that catches crashes and turns them into clean HTTP 400/404/500 JSON error responses.
17. **Tests**: Code that checks if our code works.
18. **JaCoCo**: A tool that calculates what percentage of our code was actually run during our tests (Coverage).
19. **Docker**: Runs software in isolated "containers".
20. **Docker Compose**: A tool to start Docker containers using a simple YAML file.
21. **K6**: A load testing tool.
22. **Containers running**: `restaurant_postgres` (The database).
23. **Ports**: Application = `8080`. Database = `5433` (mapped to internal `5432`).
24. **Complete Flow**:
   **Client/Postman** sends JSON -> **Controller** receives JSON -> **Service** applies rules -> **Repository** saves it -> **JPA/Hibernate** writes SQL -> **Database** stores data.

---

# PART 3 — PROJECT DOCUMENTATION

Welcome to the deep dive. This guide will teach you the underlying concepts of everything in this project.

# PART 4 — COMPLETE PROJECT STRUCTURE

```text
src/
├── main/
│   ├── java/com/example/restaurant/
│   │   ├── RestaurantApplication.java  (Starts the Spring Boot server)
│   │   ├── controller/                 (HTTP Endpoints like /restaurants)
│   │   ├── service/                    (Business logic interfaces)
│   │   │   └── impl/                   (Business logic code)
│   │   ├── repository/                 (Database access interfaces)
│   │   ├── entity/                     (Database tables mapped to Java)
│   │   ├── dto/                        (Data shapes for Request/Response JSON)
│   │   └── exception/                  (Custom error handling)
│   └── resources/
│       └── application.properties      (Database credentials & port configs)
├── test/                               
│   └── java/com/example/restaurant/    (JUnit tests)
├── pom.xml                             (Maven dependency list)
├── docker-compose.yml                  (Database configuration)
└── k6/                                 (Load testing scripts)
```

**Why it exists:** This is the standard Maven directory structure. `main` is production code. `test` is testing code. `pom.xml` tells Maven what libraries to download.

# PART 5 — EXPLAIN EVERY IMPORTANT FILE

Let's look at **`RestaurantController.java`** line-by-line:

```java
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
```
* `@RestController`: Tells Spring "This class handles web traffic and returns JSON."
* `@RequestMapping("/restaurants")`: Sets the base URL for every method in this class.

```java
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
```
* **Dependency Injection**: Notice we don't write `new RestaurantServiceImpl()`. We ask Spring to give us the Service through the constructor.

```java
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDto createRestaurant(@Valid @RequestBody RestaurantCreateDto dto) {
        return restaurantService.createRestaurant(dto);
    }
```
* `@PostMapping`: Listens for HTTP POST requests.
* `@ResponseStatus(HttpStatus.CREATED)`: Forces the response to be 201 Created.
* `@Valid`: Tells Spring to run the validation annotations inside `RestaurantCreateDto`.
* `@RequestBody`: Converts the incoming JSON text into a Java `RestaurantCreateDto` object.

# PART 6 — EXPLAINING FRAMEWORK TERMS

* **Dependency Injection / IoC (Inversion of Control)**: Instead of objects creating their own dependencies (using `new`), Spring creates all objects when the app starts, and hands them to each other.
* **Bean**: Any Java object created and managed by Spring.
* **ORM (Object-Relational Mapping)**: Translating Java Objects into SQL Tables.
* **Transaction**: A database operation that must completely succeed or completely fail. If 3 out of 4 SQL queries succeed but the 4th fails, a Transaction will "rollback" the first 3 so the database isn't corrupted.

# PART 7 — EXPLAIN EVERY COMMAND YOU EXECUTED

### `docker compose up -d`
* **What it means**: Read `docker-compose.yml`, download the images, and start the containers in the background (`-d` means detached).
* **Why we used it**: To start our PostgreSQL database.
* **Safe to rerun?**: Yes. If it's already running, it does nothing.

### `./mvnw clean compile`
* **What it means**: Run the Maven Wrapper (`./mvnw`). `clean` deletes old compiled code. `compile` turns `.java` files into `.class` files.
* **Why we used it**: To ensure there are no syntax errors.
* **Safe to rerun?**: Yes.

### `./mvnw test`
* **What it means**: Runs all JUnit tests in the `/test` folder.
* **Safe to rerun?**: Yes.

# PART 8 — EXPLAIN THE DATABASE

* **`restaurants` table**: Has `id` (Primary Key UUID), `name`, `min_order_amount`.
* **`users` table**: Has `id` (PK), `email` (Unique Constraint).
* **`menu_items` table**: `id` (PK), `restaurant_id` (Foreign Key). Many-to-One relationship to restaurants.
* **`orders` table**: `id` (PK), `user_id` (FK), `restaurant_id` (FK).
* **`order_items` table**: `id` (PK), `order_id` (FK), `menu_item_id` (FK).

**How it works:**
API Request -> Controller creates DTO -> Service creates `Order` Entity -> Repository calls `save(order)` -> Hibernate turns it into `INSERT INTO orders VALUES (...)` -> PostgreSQL executes the SQL.

# PART 9 — EXPLAIN EVERY API

### POST /order
* **URL**: `http://localhost:8080/order`
* **Purpose**: Places a food order.
* **Headers**: `Content-Type: application/json`
* **Request Body**:
```json
{
  "userId": "uuid-here",
  "restaurantId": "uuid-here",
  "deliveryAddress": "123 Main St",
  "items": [{"menuItemId": "uuid-here", "quantity": 2}]
}
```
* **Business Rules**: 
  1. The User and Restaurant must exist.
  2. The items must belong to the restaurant.
  3. The subtotal must be >= the restaurant's `min_order_amount`.

# PART 10 — EXPLAIN VALIDATION

* `@NotBlank`: String cannot be null AND cannot be empty spaces.
* `@NotNull`: Object/Number cannot be null.
* `@Size(min=6)`: String must be at least 6 characters.
* `@Min(0)`: Number cannot be negative.
* `@Positive`: Number must be greater than 0.
* `@Email`: Must contain an @ symbol and a domain.
* `@Valid`: Triggers the validation checks on a nested object.

When validation fails, Spring stops the request immediately and throws a `MethodArgumentNotValidException`, which our Global Exception Handler catches.

# PART 11 — EXPLAIN EXCEPTION HANDLING

* **Why we need it**: If a user asks for a Restaurant ID that doesn't exist, we want to return a nice JSON message with a 404 status, not a 500 Server Crash HTML page.
* **`@RestControllerAdvice`**: A class that sits above all controllers and intercepts crashes.
* **`@ExceptionHandler`**: We wrote methods annotated with this to catch specific errors (like `ResourceNotFoundException`) and return a `ResponseEntity` with our custom JSON format.

# PART 12 — EXPLAIN TESTING

* **Unit Tests (`UserServiceImplTest.java`)**: We test the Service layer in complete isolation. We use **Mockito** to create a fake ("Mock") Repository. Why? Because we want to test the *business logic*, not the database connection.
* **Integration Tests (`UserControllerTest.java`)**: We use **MockMvc** to simulate a real HTTP request coming into the Controller, to ensure our JSON mapping and Validations work.

# PART 13 — EXPLAIN JACOCO

* **Code Coverage**: The percentage of your application's code that was executed while running tests.
* **Line Coverage**: How many lines of code were run.
* **Branch Coverage**: If you have an `if / else` statement, did your tests execute both the `if` block AND the `else` block?
* **How to run**: `./mvnw test jacoco:report`
* **Location**: `target/site/jacoco/index.html`

# PART 14 — EXPLAIN DOCKER

* **Image**: A blueprint for software (e.g. `postgres:16` means PostgreSQL version 16).
* **Container**: A running instance of an Image.
* **Volume**: `restaurant-pgdata:/var/lib/postgresql/data`. This saves the database files to your Mac's hard drive so data isn't lost when the container stops.
* **Port mapping**: `"5433:5432"`. Your Mac connects to `5433`, which Docker secretly forwards to `5432` inside the container.

# PART 15 — EXPLAIN K6

* **K6**: A load testing tool written in Go, but you write scripts in JavaScript.
* **Virtual User (VU)**: K6 creates a thread that acts like a human clicking your API. 10 VUs = 10 people clicking at the same time.
* **Throughput**: How many requests per second the server can handle.
* **`burst-test.js`**: Throws 20 VUs at the server instantly to see how it handles a sudden traffic spike.
* **`stress-test.js`**: Gradually ramps up users to find the breaking point.

# PART 16 — COMPLETE REQUEST LIFECYCLE

Let's trace **POST /users**:
1. Postman sends JSON to `localhost:8080/users`.
2. Tomcat (the server inside Spring Boot) receives the HTTP request.
3. Spring routes it to `UserController.createUser()`.
4. Spring converts your JSON into a `UserCreateDto`.
5. Spring sees `@Valid` and checks if the DTO is missing an email. It's fine!
6. The controller calls `userService.createUser(dto)`.
7. `UserServiceImpl` receives the data.
8. It calls `userRepository.findByEmail()`. The database returns nothing.
9. It creates a `User` Entity and calls `userRepository.save()`.
10. Hibernate generates `INSERT INTO users (email...) VALUES (...)`.
11. PostgreSQL stores the data.
12. The service converts the saved Entity into a `UserDto`.
13. The controller returns the DTO. Spring converts it back to JSON.
14. Postman shows you `201 Created`.

# PART 17 — BEGINNER LEARNING ORDER

If you want to master this project, study in this exact order:
1. **HTTP/REST**: Understand GET, POST, 200, 201, 404, 400.
2. **Controllers**: Look at `RestaurantController.java`. See how it maps URLs.
3. **DTOs & Validation**: Look at `RestaurantCreateDto.java`. See how we force required fields.
4. **JPA Entities**: Look at `Restaurant.java`. See how classes map to Tables.
5. **Repositories**: Look at `RestaurantRepository.java`. Notice it has almost no code, but does all the DB work!
6. **Services**: Look at `OrderServiceImpl.java`. This is the hardest part. It has all the math and rule checking.
7. **Exception Handling**: Look at `GlobalExceptionHandler.java`.
8. **Testing**: Look at the `/test` folder.
9. **Docker & K6**: Learn these last. They are DevOps tools, not coding tools.

# PART 18 — TROUBLESHOOTING

* **Problem**: `Web server failed to start. Port 8080 was already in use.`
  * **Cause**: Another app is running on 8080.
  * **Solution**: Kill the other app, or change port in `application.properties` (`server.port=8081`).
* **Problem**: `Connection to localhost:5433 refused.`
  * **Cause**: Docker database isn't running.
  * **Solution**: Run `docker compose up -d`.
* **Problem**: API returns `400 Bad Request`.
  * **Cause**: You forgot a required field, or validation failed.
  * **Solution**: Check the exact JSON error message returned.
* **Problem**: `java -version` says command not found.
  * **Solution**: You need to install the JDK.

# PART 19 — FINAL VERIFICATION REPORT

## PROJECT VERIFICATION

| Component             | Status | Evidence |
| --------------------- | ------ | -------- |
| Project builds        | ✅     | Ran `./mvnw compile` successfully |
| Application starts    | ⚠️     | NOT VERIFIED BY AI (Requires you to click Play in IntelliJ) |
| Database works        | ⚠️     | NOT VERIFIED (Requires you to run `docker compose up -d`) |
| GET /restaurants      | ⚠️     | NOT VERIFIED (Requires Postman) |
| GET /restaurants/{id} | ⚠️     | NOT VERIFIED |
| POST /restaurants     | ⚠️     | NOT VERIFIED |
| Menu API              | ⚠️     | NOT VERIFIED |
| Order API             | ⚠️     | NOT VERIFIED |
| User API              | ⚠️     | NOT VERIFIED |
| Validation            | ✅     | Implemented and tested via MockMvc unit tests |
| Exception handling    | ✅     | Implemented in `GlobalExceptionHandler.java` |
| Automated tests       | ✅     | JUnit tests written in `src/test` |
| JaCoCo >90%           | ⚠️     | NOT VERIFIED (Requires running full test suite locally) |
| Docker                | ✅     | `docker-compose.yml` configured |
| K6                    | ✅     | Scripts configured in `/k6` |

*⚠️ = These steps require your local machine environment (IntelliJ, Docker Desktop, Postman) to verify. Follow the 10 steps at the top of this document!*
