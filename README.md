# Equipment Rental (POC) – Spring Boot

Proof of concept webapplicatie voor een uitleenplatform binnen een kunstopleiding.

## Features

- Registratie & login (Spring Security, session-based)
- Wachtwoorden gehasht met BCrypt
- Login rate limiting
- Productcatalogus met categorieën + zoek/filter
- Winkelmandje in `HttpSession`
- Checkout:
    - maakt `Reservation` records
    - vermindert product stock
- Pagina “Mijn leningen”:
    - toont actieve reservaties
    - annuleren zet status op `CANCELED` en herstelt stock
- CSRF-beveiliging met token in cookie (compatibel met `fetch`)
- H2-console alleen toegankelijk voor ADMIN (POC)

## Tech Stack

- Java + Spring Boot (Maven)
- Spring Security
- Spring Data JPA
- H2 (in-memory)
- Thymeleaf + Bootstrap
- Frontend calls via `fetch` naar `/api/**`

## Run locally

### Vereisten
- Java 17+ (of jouw ingestelde versie)
- Maven (of Maven wrapper)

### Starten
```bash
./mvnw spring-boot:run

Open de app:

http://localhost:8080/

H2 Database
H2 Console

URL: http://localhost:8080/h2-console

Toegang tot de pagina is ADMIN-only.

DB connectie (in de H2 Console)

Gebruik:

JDBC URL: jdbc:h2:mem:testdb

User: sa

Password: (leeg)

Tip: als je een andere datasource URL instelt, moet de JDBC URL in de console overeenkomen.

Admin account (optioneel, voor demo / H2-console)

Om een admin automatisch te seeden in dev (zonder wachtwoord in code), gebruik application-local.properties
(dat bestand wordt best niet gecommit).

Maak bestand:
src/main/resources/application-local.properties

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

admin.email=admin@school.be
admin.password=Admin123!


Voeg toe aan .gitignore:

src/main/resources/application-local.properties


Activeer profile local:

In application.properties staat:

spring.profiles.active=local

Daarna:

login in de app met admin@school.be + jouw admin password

ga naar /h2-console

Demo flow

Ga naar catalogus en voeg items toe aan mandje (met datum + aantal)

Ga naar mandje en doe checkout

Ga naar “Mijn leningen” en annuleer een reservatie

(Optioneel) Toon H2-console en laat RESERVATION / PRODUCT tabellen zien

Notes

H2 is in-memory: data verdwijnt bij restart (bewust voor POC).

Geen betalingen in deze POC.