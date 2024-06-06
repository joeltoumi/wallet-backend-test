package com.playtomic.tests.wallet.api.application.rest;

import static com.playtomic.tests.wallet.api.TestData.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("develop")
class WalletControllerIT {
  @LocalServerPort private int port;

  @Test
  @Sql(
      scripts = {"/sql/cleanWallets.sql"},
      executionPhase = BEFORE_TEST_METHOD)
  void givenValidWalletId_whenV1WalletsIdGet_thenReturnWallet() {
    var expectedWallet = aSampleWallet();

    given()
        .port(port)
        .contentType("application/json")
        .pathParam("id", WALLET_ID)
        .when()
        .get("/v1/wallets/{id}")
        .then()
        .statusCode(200)
        .contentType("application/json")
        .body("id", equalTo(expectedWallet.id()))
        .body("balance", equalTo(expectedWallet.balance().floatValue()))
        .body("currency", equalTo(expectedWallet.currency()));
  }

  @Test
  @Sql(
      scripts = {"/sql/cleanWallets.sql"},
      executionPhase = BEFORE_TEST_METHOD)
  void givenNonExistingWalletId_whenV1WalletsIdGet_thenReturn404Error() {
    given()
        .port(port)
        .contentType("application/json")
        .pathParam("id", "invalid-wallet-id")
        .when()
        .get("/v1/wallets/{id}")
        .then()
        .statusCode(404)
        .onFailMessage("Wallet not found with ID invalid-wallet-id");
  }

  @Test
  @Sql(
      scripts = {"/sql/cleanWallets.sql"},
      executionPhase = BEFORE_TEST_METHOD)
  void givenValidTopupRequest_whenV1WalletsIdTopupPost_thenTopupWalletAndReturnUpdatedWallet() {
    var expectedWallet = aUpdatedWallet();

    given()
        .port(port)
        .contentType("application/json")
        .pathParam("id", WALLET_ID)
        .body(aValidTopupRequestDto())
        .when()
        .post("/v1/wallets/{id}/topup")
        .then()
        .statusCode(200)
        .contentType("application/json")
        .body("id", equalTo(expectedWallet.id()))
        .body("balance", equalTo(expectedWallet.balance().floatValue()))
        .body("currency", equalTo(expectedWallet.currency()));
  }

  @Test
  @Sql(
      scripts = {"/sql/cleanWallets.sql"},
      executionPhase = BEFORE_TEST_METHOD)
  void givenInvalidTopupAmountRequest_whenV1WalletsIdTopupPost_thenReturn422Error() {

    given()
        .port(port)
        .contentType("application/json")
        .pathParam("id", WALLET_ID)
        .body(aNotValidTopupRequestDto())
        .when()
        .post("/v1/wallets/{id}/topup")
        .then()
        .statusCode(422)
        .onFailMessage("Minimum amount to charge is 10.");
  }

  @Test
  void givenRequestWithMissingParameters_whenV1WalletsIdTopupPost_thenReturnBadRequest() {

    given()
        .port(port)
        .contentType("application/json")
        .pathParam("id", WALLET_ID)
        .body(aTopupRequestDtoWithMissingParameters())
        .when()
        .post("/v1/wallets/{id}/topup")
        .then()
        .statusCode(400);
  }
}
