# bojer-kochrezepte project

## Description

This project is designed to manage different recipes for different types of meals. 
For these recipes, the CRUD methods are implemented and callable with JSON or and XML, whereas JSON gives the better results.

The import request imports 6 recipes into the database.

Furthermore, you can create a menu, which needs a starter, a main course and a dessert to deliver a successful result.

Worth mentioning is the calorie calculation of a single recipe.

You can also view the ingredients individually. For these, the CRUD methods have been implemented to simplify the management of recipes.

## Checkliste - Erledigte Aufgaben

- [X] Tabellen entsprechen der 3. NF
- [X] Queries geben Daten aus mehreren Tabellen zurück
- [X] QueryParams und PathParams wurden angewendet
- [X] Alle 5 Request-Arten würden implementiert
- [X] Es wurden http-request Files angelegt
- [X] README beinhaltet Dokumentation, Datenmodell, Use-Case-Diagrams und Swagger-Link

## Class Diagram
![class-diagram](backend/asciidoc/images/cld.png?raw=true)

## Use-Case Diagrams
![use-case-diagram](backend/asciidoc/images/ucd.png?raw=true)

![use-case-diagram-db](backend/asciidoc/images/ucd-db.png?raw=true)

## Quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

## How to run the project

```
./mvnw clean compile quarkus:dev
or on Windows:
mvnw clean compile quarkus:dev
```

## Swagger

You can find the Swagger UI here:
```
http://localhost:8080/swagger-ui
```


## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `bojer-kochrezepte-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/bojer-kochrezepte-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/bojer-kochrezepte-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.
