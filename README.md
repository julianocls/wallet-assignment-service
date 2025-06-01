# wallet-assignment-service

## Docker Config
Config Database:
docker compose up -d

---
## Create tables
./gradlew flywayMigrate -Denv=local

---
## Application Config
VM Options:
-Dspring.profiles.active=local
