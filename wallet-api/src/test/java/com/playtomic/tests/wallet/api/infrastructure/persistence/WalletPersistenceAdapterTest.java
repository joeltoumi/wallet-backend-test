package com.playtomic.tests.wallet.api.infrastructure.persistence;

import static com.playtomic.tests.wallet.api.TestData.*;
import static com.playtomic.tests.wallet.api.TestData.aWalletDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.playtomic.tests.wallet.api.infrastructure.persistence.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.api.infrastructure.persistence.mapper.WalletEntityMapper;
import java.util.Optional;

import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalletPersistenceAdapterTest {

  @Mock private WalletJPARepository walletJPARepository;

  private WalletPersistenceAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter =
        new WalletPersistenceAdapter(
            walletJPARepository, Mappers.getMapper(WalletEntityMapper.class));
  }

  @Test
  void givenValidWalletId_whenFindById_thenReturnsWallet() {
    var expectedWallet = aSampleWallet();

    when(walletJPARepository.findByWalletId(WALLET_ID)).thenReturn(aWalletDto());

    var response = adapter.findById(WALLET_ID);

    assertEquals(expectedWallet.id(), response.id());
    assertEquals(expectedWallet.balance(), response.balance());
    assertEquals(expectedWallet.currency(), response.currency());
  }

  @Test
  void givenNonExistingWalletId_whenFindById_thenThrowWalletNotFoundException() {

    when(walletJPARepository.findByWalletId(WALLET_ID)).thenReturn(Optional.empty());

    assertThrows(WalletNotFoundException.class, () -> adapter.findById(WALLET_ID));
  }

  @Test
  void givenValidWallet_whenUpdateBalance_thenSaveAndReturnUpdatedWallet() {

    var expectedWallet = aUpdatedWallet();
    var walletDto = aWalletDto();

    when(walletJPARepository.findByWalletId(WALLET_ID)).thenReturn(walletDto);
    walletDto.get().setBalance(RECHARGED_BALANCE);
    when(walletJPARepository.save(walletDto.get())).thenReturn(aUpdatedWalletDto());

    var response = adapter.updateBalance(WALLET_ID, RECHARGED_BALANCE);

    assertEquals(expectedWallet.id(), response.id());
  }


  @Test
  void givenConcurrentModificationOfAWallet_whenUpdateBalance_thenThrowOptimisticLockException() {
    var walletDto = aWalletDto();

    when(walletJPARepository.findByWalletId(WALLET_ID)).thenReturn(walletDto);
    walletDto.get().setBalance(RECHARGED_BALANCE);
    when(walletJPARepository.save(walletDto.get())).thenThrow(OptimisticLockException.class);

    assertThrows(OptimisticLockException.class, () -> adapter.updateBalance(WALLET_ID, RECHARGED_BALANCE));
  }


}
