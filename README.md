# wallet-assignment-service

# Wallet Service Documentation

## Estimated Development Time

**13 hours**

---

## Instructions for Installing, Testing, and Running the Service

### 1. Set Up the Database
```bash
docker compose up -d
```

### 2. Create System Tables
````
./gradlew flywayMigrate -Denv=local
````


### 3. Configure Application Startup

VM Options: 
```
-Dspring.profiles.active=local
```

---

## Curls for Testing the System

### Criar Carteira
````
curl --location 'http://localhost:9999/wallet'
--header 'accept: /'
--header 'Content-Type: application/json'
--data '{
"name": "Conta Investimento",
"userName": "Juliano Santos"
}'
````
---

### List Wallets by User
````
curl --location 'http://localhost:9999/wallet?createAtStart=2024-01-01&createAtEnd=2026-01-01&userName=Juliano%20Santos'
--header 'accept: /'
````

---

### Deposit Funds
````
curl --location 'http://localhost:9999/wallet/deposit'
--header 'accept: /'
--header 'Content-Type: application/json'
--data '{
"amount": 100.20,
"walletCode": "7aa50041-6a5a-4bff-b1df-f74b88959583"
}'
````

---

### Withdraw Funds
````
curl --location 'http://localhost:9999/wallet/withdraw'
--header 'accept: /'
--header 'Content-Type: application/json'
--data '{
"amount": 31.87,
"walletCode": "7aa50041-6a5a-4bff-b1df-f74b88959583"
}'
````

---

### Transfer Funds
````
curl --location 'http://localhost:9999/wallet/transfer'
--header 'accept: /'
--header 'Content-Type: application/json'
--data '{
"amount": 20.50,
"originWalletCode": "7aa50041-6a5a-4bff-b1df-f74b88959583",
"destinationWalletCode": "67eaac5e-3c99-4c3d-b32b-0b09ad6ace1b"
}'
````

---

### Check Balance
````
curl --location 'http://localhost:9999/wallet/7aa50041-6a5a-4bff-b1df-f74b88959583'
--header 'accept: /'
````

---

### Check Balance History
```
curl --location 'http://localhost:9999/wallet/balanceHistory?walletCode=67eaac5e-3c99-4c3d-b32b-0b09ad6ace1b'
--header 'accept: /'
```

---

**Note:** Ensure that the application is running and the database is properly configured before executing the tests.