### Phone Booking Service

#### Development Env
```console
Language: Java 17
Web Framework: Quarkus
DB: H2
```
#### Launch
```bash
# Launch the service in dev env
$> ./mvnw clean quarkus:dev
```

#### Project Structure
```
src\main\java
 |--boot\           # Contains DataLoader which loads the initial data fetching data from Fono API
 |--fono\           # Mock implementation for Fono API.
 |--phone\          # Contains the API endpoint for managing Phone inventory.
src\main\resources
 |--db\             # Contains db initialization scripts
```
#### API Documentation
To view the list of available APIs and their specifications, run the server and go to `http://localhost:8080/q/dev/` in your browser. This documentation page is automatically generated using the **Smallrye OpenAPI** definitions from API interface.

#### API Endpoints

List of available routes:

**Application routes**:\
`GET /v1/phones` - Lists all phones\
`GET /v1/phone/{phoneIdentifier}` - Gets the phone's details\
`POST /v1/phone/book` - Books a phone\
`DELETE /v1/phone/book/{phoneIdentifier}` - Returns the phone\

#### Packaging and running the application

The application can be packaged using:
```bash
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```bash
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```bash
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```bash
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/phone-service-1.0.0-SNAPSHOT-runner`
