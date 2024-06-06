package com.playtomic.tests.wallet.api.infrastructure.persistence;

import com.playtomic.tests.wallet.api.domain.model.Wallet;
import com.playtomic.tests.wallet.api.domain.model.WalletRepository;
import com.playtomic.tests.wallet.api.domain.topup.PlaytomicWalletException;
import com.playtomic.tests.wallet.api.infrastructure.persistence.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.api.infrastructure.persistence.mapper.WalletEntityMapper;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletPersistenceAdapter implements WalletRepository {

  private final WalletJPARepository walletJPARepository;
  private final WalletEntityMapper walletEntityMapper;

  @Override
  public Wallet findById(String walletId) {
    return walletJPARepository
        .findByWalletId(walletId)
        .map(walletEntityMapper::map)
        .orElseThrow(() -> new WalletNotFoundException("Wallet not found with ID " + walletId));
  }

  @Override
  public Wallet updateBalance(String walletId, BigDecimal balance) throws WalletNotFoundException {
    try {
      return walletJPARepository
          .findByWalletId(walletId)
          .map(
              existingWallet -> {
                existingWallet.setBalance(balance);
                return walletJPARepository.save(existingWallet);
              })
          .map(walletEntityMapper::map)
          .orElseThrow();
    } catch (ObjectOptimisticLockingFailureException e) {
      throw new PlaytomicWalletException("An error occurred while processing your request.");
    }
  }
}
