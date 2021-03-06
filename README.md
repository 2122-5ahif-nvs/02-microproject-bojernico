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
![class-diagram](asciidocs/images/cld.png?raw=true)

## Use-Case Diagrams
![use-case-diagram](asciidocs/images/ucd.png?raw=true)

![use-case-diagram-db](asciidocs/images/ucd-db.png?raw=true)

## Quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

## How to run the project

with cool quarkus cli 
```
quarkus dev

or with maven

./mvnw clean compile quarkus:dev
```

## Swagger

You can find the Swagger UI here:
```
http://localhost:8080/q/swagger-ui
```