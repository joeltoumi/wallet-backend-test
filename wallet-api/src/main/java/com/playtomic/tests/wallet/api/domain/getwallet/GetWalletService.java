package com.playtomic.tests.wallet.api.domain.getwallet;

import com.playtomic.tests.wallet.api.application.port.GetWalletUseCase;
import com.playtomic.tests.wallet.api.domain.model.Wallet;
import com.playtomic.tests.wallet.api.domain.model.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetWalletService implements GetWalletUseCase {

  private final WalletRepository walletRepository;

  @Override
  public Wallet getWallet(String walletId) {
    return walletRepository.findById(walletId);
  }
}
