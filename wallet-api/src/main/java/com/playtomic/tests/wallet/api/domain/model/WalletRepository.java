package com.playtomic.tests.wallet.api.domain.model;

import java.math.BigDecimal;

public interface WalletRepository {

  Wallet findById(String walletId);
  Wallet updateBalance(String walletId, BigDecimal balance);
}
