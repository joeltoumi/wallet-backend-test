package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.domain.model.Wallet;
import com.playtomic.tests.wallet.api.domain.topup.TopUpRequest;
import com.playtomic.tests.wallet.api.infrastructure.persistence.dto.WalletDTO;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class TestData {

  public static final String WALLET_ID = "wallet-id";
  public static final double TOO_SMALL_AMOUNT_TO_RECHARGE = 2D;

  public static final BigDecimal AMOUNT_TO_RECHARGE = new BigDecimal(10);
  public static final BigDecimal CURRENT_BALANCE = new BigDecimal(50.5);
  public static final BigDecimal RECHARGED_BALANCE = CURRENT_BALANCE.add(AMOUNT_TO_RECHARGE);
  public static final String CREDIT_CARD_TEST = "4242 4242 4242 4242";
  public static final String CURRENCY = "EUR";


  public static Optional<WalletDTO> aWalletDto() {
    return Optional.of(WalletDTO.builder().walletId(WALLET_ID).balance(CURRENT_BALANCE).currency(CURRENCY).build());
  }

  public static Wallet aSampleWallet() {
    return Wallet.builder().id(WALLET_ID).balance(CURRENT_BALANCE).currency(CURRENCY).build();
  }

  public static Map<String, Object> aValidTopupRequestDto() {
    return Map.of(
            "amount", AMOUNT_TO_RECHARGE,
            "creditCard", CREDIT_CARD_TEST);
  }

  public static Map<String, Object> aNotValidTopupRequestDto() {
    return Map.of(
            "amount", TOO_SMALL_AMOUNT_TO_RECHARGE,
            "creditCard", CREDIT_CARD_TEST);
  }

  public static Map<String, Object> aTopupRequestDtoWithMissingParameters() {
    return Map.of(
            "creditCard", CREDIT_CARD_TEST);
  }

  public static Wallet aUpdatedWallet() {
    return Wallet.builder().id(WALLET_ID).balance(RECHARGED_BALANCE).currency(CURRENCY).build();
  }

  public static WalletDTO aUpdatedWalletDto() {
    return WalletDTO.builder().walletId(WALLET_ID).balance(RECHARGED_BALANCE).currency(CURRENCY).build();
  }

  public static TopUpRequest aTopUpRequest(){
    return TopUpRequest.builder().amount(AMOUNT_TO_RECHARGE).creditCard(CREDIT_CARD_TEST).build();
  }

}
