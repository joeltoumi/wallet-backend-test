package com.playtomic.tests.wallet.api.domain.topup;

import static com.playtomic.tests.wallet.api.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.playtomic.tests.wallet.api.domain.model.WalletRepository;
import com.playtomic.tests.wallet.api.domain.paymentclient.PaymentClient;
import com.playtomic.tests.wallet.api.domain.paymentclient.PaymentException;
import com.playtomic.tests.wallet.api.domain.paymentclient.RechargeRequest;
import com.playtomic.tests.wallet.api.domain.paymentclient.RechargeResponse;
import com.playtomic.tests.wallet.api.infrastructure.persistence.exception.WalletNotFoundException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TopUpWalletServiceTest {

  @Mock WalletRepository walletRepository;
  @Mock PaymentClient paymentClient;
  private TopUpWalletService service;

  @BeforeEach
  void setUp() {
    this.service = new TopUpWalletService(walletRepository, paymentClient);
  }

  @Test
  void givenValidTopupRequest_whenTopUpWallet_thenCallPaymentServiceAndUpdateWallet() {
    var walletId = WALLET_ID;
    var topUpRequest = aTopUpRequest();
    var existingWallet = aSampleWallet();
    var expectedWallet = aUpdatedWallet();

    when(walletRepository.findById(walletId)).thenReturn(existingWallet);
    when(walletRepository.updateBalance(
            WALLET_ID, existingWallet.balance().add(AMOUNT_TO_RECHARGE)))
        .thenReturn(expectedWallet);
    when(paymentClient.recharge(any(RechargeRequest.class)))
        .thenReturn(RechargeResponse.builder().id("123ABC").build());

    var response = service.topUpWallet(walletId, topUpRequest);

    assertEquals(expectedWallet.balance(), response.balance());
    verify(walletRepository).findById(anyString());
    verify(walletRepository).updateBalance(anyString(), any(BigDecimal.class));
    verify(paymentClient).recharge(any(RechargeRequest.class));
  }

  @Test
  void givenNonExistingWalletId_whenTopUpWallet_thenThrowWalletNotFoundException() {
    var walletId = WALLET_ID;
    var topUpRequest = aTopUpRequest();

    when(walletRepository.findById(walletId)).thenThrow(WalletNotFoundException.class);

    assertThrows(WalletNotFoundException.class, () -> service.topUpWallet(walletId, topUpRequest));

    verify(walletRepository).findById(anyString());
    verify(walletRepository, never()).updateBalance(anyString(), any(BigDecimal.class));
    verifyNoInteractions(paymentClient);
  }

  @Test
  void givenInvalidAmountToCharge_whenTopUpWallet_thenThrowPaymentExceptionAndWalletIsNotUpdated() {
    var walletId = WALLET_ID;
    var topUpRequest = aTopUpRequest();
    var existingWallet = aSampleWallet();
    var expectedWallet = aUpdatedWallet();

    when(walletRepository.findById(walletId)).thenReturn(existingWallet);
    when(walletRepository.updateBalance(
            WALLET_ID, existingWallet.balance().add(AMOUNT_TO_RECHARGE)))
        .thenReturn(expectedWallet);
    when(paymentClient.recharge(any(RechargeRequest.class))).thenThrow(PaymentException.class);

    assertThrows(PaymentException.class, () -> service.topUpWallet(walletId, topUpRequest));

    verify(walletRepository).findById(anyString());
    verify(walletRepository).updateBalance(anyString(), any(BigDecimal.class));
    verify(paymentClient).recharge(any(RechargeRequest.class));
  }

  @Test
  void
      givenConcurrencyError_whenTopUpWallet_thenThrowPlaytomicWalletExceptionAndPaymentIsNotMade() {
    var walletId = WALLET_ID;
    var topUpRequest = aTopUpRequest();
    var existingWallet = aSampleWallet();

    when(walletRepository.findById(walletId)).thenReturn(existingWallet);
    when(walletRepository.updateBalance(
            WALLET_ID, existingWallet.balance().add(AMOUNT_TO_RECHARGE)))
        .thenThrow(PlaytomicWalletException.class);

    assertThrows(PlaytomicWalletException.class, () -> service.topUpWallet(walletId, topUpRequest));

    verify(walletRepository).findById(anyString());
    verify(walletRepository).updateBalance(anyString(), any(BigDecimal.class));
    verifyNoInteractions(paymentClient);
    // i.e verify refunds kafka producer is called
  }
}
