# Documentatie – Equipment Rental (POC)

## 1. Overzicht van het project

**Equipment Rental** is een proof-of-concept webapplicatie voor een uitleenplatform binnen een kunstopleiding.  
Gebruikers kunnen zich registreren en inloggen, producten bekijken en filteren, items toevoegen aan een sessie-mandje, afrekenen (checkout) en hun actieve reservaties bekijken/annuleren.

Doel: aantonen dat login/registratie, security, sessiebeheer, checkout-flow en data-overzicht correct werken.

---

## 2. Architectuur & structuur

### 2.1. Laagindeling

- **Controller layer (`controller`)**
    - REST endpoints onder `/api/**` (auth, cart, reservations, products)
    - Pagina’s via Thymeleaf templates (`templates`)

- **Service layer (`service`)**
    - Business logic (registratie, checkout, annuleren, stock update)
    - Validatie van regels (bv. stock, datums)

- **Repository layer (`repository`)**
    - Spring Data JPA repositories voor database-access

- **Model layer (`model`)**
    - Entities zoals `Product`, `Category`, `Reservation`, `AppUser`

### 2.2. Waarom deze opdeling?

- Duidelijke scheiding van verantwoordelijkheden
- Services blijven testbaar en herbruikbaar
- Controllers blijven “dun” (alleen request/response)

---

## 3. Data & filtering

### 3.1. Catalogus

De catalogus toont producten met:
- naam
- beschrijving
- categorie
- beschikbare stock

### 3.2. Filters

Frontend filtering in de catalogus:
- **zoekveld**: filter op naam/beschrijving/categorie
- **categorie dropdown**: filter op categorie

Reden: POC-simpel, performant genoeg voor beperkte dataset.

---

## 4. Login & registratie (security)

### 4.1. Registratie

- Gebruiker registreert met **email + password**
- Email wordt genormaliseerd (`trim + lowercase`)
- Duplicate email wordt geblokkeerd
- Wachtwoord wordt gehashed met **BCrypt**

### 4.2. Login

- Login via `/api/auth/login`
- Session-based authentication:
    - bij succesvolle login wordt `SecurityContext` in de HTTP session opgeslagen
- Rate limiting via `LoginRateLimiter`:
    - beperkt brute-force pogingen per `(IP + email)`

### 4.3. Rollen

- `AppUser.role` bestaat (bv. `"USER"`, `"ADMIN"`)
- Voor POC is ADMIN gebruikt om H2-console af te schermen

---

## 5. CSRF & fetch integratie

Omdat de frontend via `fetch()` POST requests doet, is CSRF correct ingesteld met:
- CSRF token in cookie (`XSRF-TOKEN`)
- Frontend stuurt header `X-XSRF-TOKEN`

In `app.js`:
- `ensureCsrfCookie()` doet een call naar `/api/csrf` zodat de cookie gezet wordt
- `csrfFetch()` voegt automatisch de CSRF header toe bij requests
- `credentials: 'same-origin'` wordt gebruikt zodat cookies meegestuurd worden

---

## 6. Sessie & checkout

### 6.1. Winkelmandje (HttpSession)

Het mandje wordt server-side bijgehouden in de sessie:
- key: `"CART"`
- type: `List<CartItem>`

Voordelen:
- eenvoudig voor POC
- geen extra database-tabellen nodig

### 6.2. Checkout flow

Bij checkout:
1. Stock wordt eerst gecontroleerd (alles-of-niets)
2. Reservations worden aangemaakt
3. Stock wordt verminderd
4. Mandje wordt leeggemaakt

Deze logica zit in `ReservationService.checkout(...)` en is `@Transactional` om partial updates te voorkomen.

---

## 7. Reservaties & annuleren

### 7.1. Mijn leningen

Endpoint geeft enkel reservaties van de ingelogde gebruiker terug.  
Frontend toont:
- productnaam
- aantal
- van/tot datum
- status

### 7.2. Cancel

- Alleen de owner van de reservatie mag annuleren (ownership check)
- Status wordt `CANCELED`
- Stock wordt terug verhoogd
- Ook `@Transactional` voor consistentie

---

## 8. H2 Database & ADMIN-only console

De H2-console staat aan voor development/demo.
Toegang tot `/h2-console/**` is **ADMIN-only** in Spring Security.

### Waarom afschermen?
- H2-console geeft volledige DB toegang
- zonder afscherming is dit een groot security risico

### Credentials (H2 console database login)
Deze zijn de standaard H2 connectiegegevens (voor de database zelf):
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **User:** `sa`
- **Password:** *(leeg)*

---

## 9. Seed data (development)

Met `DataSeeder` worden categorieën en producten automatisch toegevoegd bij start:
- draait enkel buiten `prod` profile
- seed gebeurt alleen als er nog geen categorieën bestaan

Optioneel kan een admin-user in dev ge-seed worden via `application-local.properties` (niet committen).

---

## 10. Error handling

Met `ApiExceptionHandler` worden fouten netjes vertaald naar bruikbare HTTP responses (bv. 400/403/404), zodat frontend duidelijke meldingen kan tonen.

---


## 11. Design keuzes (verdediging)

**Waarom sessions i.p.v. JWT?**  
Voor een POC is session-based auth sneller en eenvoudiger, en past goed bij server-rendered Thymeleaf + same-origin fetch calls.

**Waarom CSRF aan?**  
Omdat session cookies gebruikt worden. Zonder CSRF kan een andere site POST requests uitvoeren als de gebruiker ingelogd is.

**Waarom `@Transactional` bij checkout/cancel?**  
Zorgt ervoor dat stock + reservaties consistent blijven, ook bij exceptions.

**Waarom rate limiting?**  
Beschermt tegen brute-force loginpogingen (basis security verbetering).

---

## 12. Mogelijke verbeteringen

- Server-side filtering/paging voor catalogus (bij grotere datasets)
- Admin dashboard (CRUD voor producten)
- Betere validatie: overlapping van reservatieperiodes (niet enkel stock)
- E-mail bevestiging / password reset
