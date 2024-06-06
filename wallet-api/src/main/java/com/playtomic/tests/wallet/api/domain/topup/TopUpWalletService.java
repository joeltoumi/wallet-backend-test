package com.playtomic.tests.wallet.api.domain.topup;

import com.playtomic.tests.wallet.api.application.port.TopUpWalletUseCase;
import com.playtomic.tests.wallet.api.domain.model.Wallet;
import com.playtomic.tests.wallet.api.domain.model.WalletRepository;
import com.playtomic.tests.wallet.api.domain.paymentclient.PaymentClient;
import com.playtomic.tests.wallet.api.domain.paymentclient.RechargeRequest;
import com.playtomic.tests.wallet.api.domain.paymentclient.RechargeResponse;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TopUpWalletService implements TopUpWalletUseCase {

  private final WalletRepository walletRepository;
  private final PaymentClient paymentClient;

  @Override
  @Transactional
  public Wallet topUpWallet(String id, TopUpRequest topUpRequest) {

    return Optional.of(walletRepository.findById(id))
        .map(
            existingWallet -> {
              var updatedWallet =
                  updateWalletBalance(id, existingWallet.balance().add(topUpRequest.amount()));
              var recharge = processRechargePayment(topUpRequest);
              saveTransaction(recharge.id(), id, topUpRequest);
              return updatedWallet;
            })
        .orElseThrow();
  }

  private RechargeResponse processRechargePayment(TopUpRequest topUpRequest) {
    return paymentClient.recharge(
        RechargeRequest.builder()
            .amount(topUpRequest.amount())
            .creditCard(topUpRequest.creditCard())
            .build());
  }

  private Wallet updateWalletBalance(String id, BigDecimal newBalance)
      throws PlaytomicWalletException {

    return walletRepository.updateBalance(id, newBalance);
  }

  private void saveTransaction(String transactionId, String walletId, TopUpRequest topUpRequest) {
    // publish or store this transaction somewhere like a kafka queue or database.
    // I would also add to that object some timestamp.
    // This would be implemented in another service, not here,
    // I define the method call here to avoid further complexity by adding interfaces and services.
  }
}
