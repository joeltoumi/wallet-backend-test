package com.playtomic.tests.wallet.api.infrastructure.stripe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.playtomic.tests.wallet.api.domain.paymentclient.PaymentClient;
import com.playtomic.tests.wallet.api.domain.paymentclient.PaymentException;
import com.playtomic.tests.wallet.api.domain.paymentclient.RechargeRequest;
import com.playtomic.tests.wallet.api.domain.paymentclient.RechargeResponse;
import com.playtomic.tests.wallet.api.infrastructure.stripe.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.api.infrastructure.stripe.exception.StripeRestTemplateResponseErrorHandler;
import com.playtomic.tests.wallet.api.infrastructure.stripe.exception.StripeServiceException;
import java.math.BigDecimal;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Handles the communication with Stripe.
 *
 * <p>A real implementation would call to String using their API/SDK. This dummy implementation
 * throws an error when trying to charge less than 10€.
 */
@Service
public class StripeClient implements PaymentClient {

  @NonNull private final URI chargesUri;

  @NonNull private final URI refundsUri;

  @NonNull private final RestTemplate restTemplate;

  public StripeClient(
      @Value("${stripe.simulator.charges-uri}") @NonNull URI chargesUri,
      @Value("${stripe.simulator.refunds-uri}") @NonNull URI refundsUri,
      @NonNull RestTemplateBuilder restTemplateBuilder) {
    this.chargesUri = chargesUri;
    this.refundsUri = refundsUri;
    this.restTemplate =
        restTemplateBuilder.errorHandler(new StripeRestTemplateResponseErrorHandler()).build();
  }

  @Override
  public RechargeResponse recharge(RechargeRequest rechargeRequest) {
    try {
      return charge(rechargeRequest.creditCard(), rechargeRequest.amount());
    } catch (StripeAmountTooSmallException e) {
        throw new PaymentException("Minimum amount to charge is 10.");
    } catch (StripeServiceException e){
        throw new PaymentException("An error occurred while calling Stripe service.");
    }
  }

  /**
   * Charges money in the credit card.
   *
   * <p>Ignore the fact that no CVC or expiration date are provided.
   *
   * @param creditCardNumber The number of the credit card
   * @param amount The amount that will be charged.
   * @throws StripeServiceException
   */
  public RechargeResponse charge(@NonNull String creditCardNumber, @NonNull BigDecimal amount)
      throws StripeServiceException {
    ChargeRequest body = new ChargeRequest(creditCardNumber, amount);
    return restTemplate.postForObject(chargesUri, body, RechargeResponse.class);
  }

  /** Refunds the specified payment. */
  public void refund(@NonNull String paymentId) throws StripeServiceException {
    // Object.class because we don't read the body here.
    restTemplate.postForEntity(chargesUri.toString(), null, Object.class, paymentId);
  }

  @AllArgsConstructor
  private static class ChargeRequest {

    @NonNull
    @JsonProperty("credit_card")
    String creditCardNumber;

    @NonNull
    @JsonProperty("amount")
    BigDecimal amount;
  }
}
