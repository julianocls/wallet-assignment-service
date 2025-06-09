# Wallet Assignment Service - Documentation

## Estimated Development Time

**13 hours**

---

## Instructions for Installing, Testing, and Running the Service

### 1. Set Up the Database
```bash
docker compose -f docker-compose-database.yml up -d
docker compose -f docker-compose-database.yml down
```


### 2. Set Up the Kakfa
```bash
docker compose -f docker-compose-kafka.yml up -d
docker compose -f docker-compose-kafka.yml down
```


### 3. Create System Tables
````
./gradlew flywayMigrate -Denv=local
````


### 4. Configure Application Startup

VM Options: 
```
-Dspring.profiles.active=local
```


### 5. Create Topics
#### Access: http://localhost:8080/ui/clusters/local-cluster/all-topics/create-new-topic
```
docker exec kafka kafka-topics --create \
  --topic wallet.assignment.service.wallet-deposit \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1

docker exec kafka kafka-topics --create \
  --topic wallet.assignment.service.wallet-transfer \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1

docker exec kafka kafka-topics --create \
  --topic wallet.assignment.service.wallet-withdraw \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1
```

### DLQ Topics:
```
docker exec kafka kafka-topics --create \
  --topic wallet.assignment.service.wallet-deposit-dlq \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1

docker exec kafka kafka-topics --create \
  --topic wallet.assignment.service.wallet-transfer-dlq \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1

docker exec kafka kafka-topics --create \
  --topic wallet.assignment.service.wallet-withdraw-dlq \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1
```


---
### Access Spring Docs http://localhost:9999/swagger-ui/index.html
### or

## Curls for Testing the System

### Create Wallets
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

---

In this project, I am using the hexagonal architecture for better project organization.  
In this application, in addition to creating wallets for multiple users, it is also possible to create multiple wallets for the same user.  
The implementation meets the requirements for creating wallets, checking balances, transferring, depositing, and withdrawing funds.

The system also includes a history table, allowing tracking of all transactions. When transferring a balance from one account to another, it is possible to identify the source account of that transfer.  
The system also logs with tracing to enable tracking of potential errors in the application.

Due to time constraints, unit tests, which are crucial for validating all parts of the code, were not implemented.  
It would be ideal to have a user entity instead of directly storing the name in the Wallet table.  
Using Kafka would make the application more resilient, secure, and faster, as in case of any issues during an operation, we could use a DLQ (Dead Letter Queue) for retries.

Another important point would be to put Redis to make our queries faster, since not every query changes frequently, an example is the query of the Wallet table.
