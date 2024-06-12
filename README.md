# Wallet Service Application

## Project Description
Proof of concept of a wallet service:
- Get a wallet using its identifier.
- Top-up money in that wallet using a credit card number. It has to charge that amount internally using a third-party platform.

## Endpoints
### Get the wallet:

- **URL**: `/v1/wallets/{id}`
- **Method**: `GET`
- **Path parameter**:
  - `id` (string): Wallet identifier
- **Response**:
  - `id` (string): Wallet identifier
  - `balance` (big decimal): Wallet balance
  - `currency` (string): Currency of the balance

#### Request
```bash
curl http://localhost:8090/v1/wallets/wallet-id
```

#### Response

```json
{
	"id": "wallet-id",
	"balance": 50.5,
	"currency": "EUR"
}
```

### Top up the wallet:
- **URL**: `/v1/wallets/{id}/topup`
- **Method**: `POST`
- **Path parameter**:
 - `id` (string): Wallet identifier
- **Body**:
 - `creditCard` (string): Credit card number
 - `amount` (big decimal): Amount to recharge
- **Response**:
  - `id` (string): Wallet identifier
  - `balance` (big decimal): Wallet balance
  - `currency` (string): Currency of the balance

#### Request
```bash
curl --location 'http://localhost:8090/v1/wallets/wallet-id/topup' \
--header 'Content-Type: application/json' \
--data '{"creditCard": "41111111111111111", "amount": 50}'
```
#### Response
```json
{
	"id": "wallet-id",
	"balance": 100.5,
	"currency": "EUR"
}
```

## Steps to Run the Project

This POC follows the API first approach, so it is required to run a _maven clean install_ command to generate the necessary classes.
```bash
git clone https://github.com/joeltoumi/wallet-backend-test.git
cd wallet-backend-test
./mvn clean install
```

#### Run spring boot application from console
To start the aplication you need to run the command:
```bash
cd wallet-api
./mvnw spring-boot:run
```
