package com.playtomic.tests.wallet.api.infrastructure.stripe;

import static com.playtomic.tests.wallet.api.TestData.CREDIT_CARD_TEST;
import static com.playtomic.tests.wallet.api.TestData.TOO_SMALL_AMOUNT_TO_RECHARGE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.playtomic.tests.wallet.api.infrastructure.stripe.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.api.infrastructure.stripe.exception.StripeServiceException;
import java.math.BigDecimal;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(StripeClient.class)
@ActiveProfiles(profiles = "test")
class StripeClientTest {

  @Value("${stripe.simulator.charges-uri}")
  URI testUri;

  @Autowired StripeClient stripeClient;
  @Autowired private MockRestServiceServer mockServer;

  @Test
  void test_exception() {
    mockServer.expect(requestTo(testUri)).andRespond(withStatus(HttpStatusCode.valueOf(422)));
    var amount = new BigDecimal(TOO_SMALL_AMOUNT_TO_RECHARGE);
    assertThrows(
        StripeAmountTooSmallException.class, () -> stripeClient.charge(CREDIT_CARD_TEST, amount));
  }

  @Test
  void test_ok() throws StripeServiceException {
    mockServer
        .expect(requestTo(testUri))
        .andRespond(withSuccess("{\"id\": \"123ABC\"}", MediaType.APPLICATION_JSON));

    stripeClient.charge(CREDIT_CARD_TEST, new BigDecimal(15));
  }
}
