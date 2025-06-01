# Electronic Store
[Link do Swaggera](http://localhost:8080/swagger-ui/index.html#/)

## Opis Projektu

Electronic Store to aplikacja backendowa dla sklepu elektronicznego zrealizowana w Spring Boot. System umożliwia:
* Rejestrację i zarządzanie użytkownikami (USER i ADMIN)
* Zarządzanie katalogiem produktów
* Składanie zamówień
* Przetwarzanie płatności

## Diagram ERD bazy danych
![ERD diagram.](/images/erd_diagram.png")

## Wymagania systemowe
* Java 17
* Maven 3.8+
* PostgreSQL 14+
* Docker (opcjonalnie)

## Użyte technologie
* Backend: Java 17, Spring Boot 3.4.5
* Bezpieczeństwo: Spring Security
* Baza Danych: PostgreSQL, Hibernate, Flyway
* API Documentation: Swagger (Springdoc OpenAPI)
* Testowanie JUnit 5, Mockito, Testcontainers
* Narzędzia: Maven, Jacoco, Docker

### Bez dockera:
```
# Sklonuj repozytorium
git clone https://github.com/twoj-uzytkownik/ElectronicStore.git
cd ElectronicStore

# Zbuduj projekt
mvn clean package

# Uruchom aplikację
java -jar target/ElectronicStore-0.0.1-SNAPSHOT.jar
```

### Z Dockerem
```
docker-compose up -d
```

## Swagger
![Swagger.](/images/swagger.png")
### Główne endpointy
* POST /api/auth/register - Rejestracja użytkownika
* GET /api/products - Pobieranie listy produktów
* POST /api/orders - Tworzenie zamówienia
* POST /api/payments - Przetwarzanie płatności

### Testy
```
mvn clean test jacoco:report
```
![Jacoco report.](/images/jacoco.png")

## Autor
Adrian Bober
adr-bober@o2.pl