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
```

Open de app:

http://localhost:8080/

H2 Database
H2 Console

URL: http://localhost:8080/h2-console

Toegang: ADMIN-only

DB connectie (in de H2 Console)

JDBC URL: jdbc:h2:mem:testdb

User: sa

Password: (leeg)

Tip: als je een andere datasource URL instelt, moet de JDBC URL in de console overeenkomen.

Admin account (optioneel, voor demo / H2-console)

Om een admin automatisch te seeden in dev (zonder wachtwoord in code), gebruik application-local.properties
(dat bestand wordt best niet gecommit).

Maak bestand:

src/main/resources/application-local.properties

Inhoud:
```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

admin.email=admin@school.be
admin.password=Admin123!
```

Voeg toe aan .gitignore:

src/main/resources/application-local.properties

Activeer profile local:

In application.properties:
```
spring.profiles.active=local
```
Daarna:

login in de app met admin@school.be + jouw admin password

ga naar /h2-console


### Notes

H2 is in-memory: data verdwijnt bij restart (bewust voor POC).

Geen betalingen in deze POC.

### Referenties
Spring Boot Reference Documentation — https://docs.spring.io/spring-boot/docs/current/reference/html/

Spring Security Reference — https://docs.spring.io/spring-security/reference/

CSRF in Spring Security — https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html

Spring Data JPA Reference — https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

H2 Database Engine (Console & JDBC) — https://www.h2database.com/html/main.html

MDN Web Docs — Fetch API — https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API

GitHub Copilot — https://github.com/features/copilot