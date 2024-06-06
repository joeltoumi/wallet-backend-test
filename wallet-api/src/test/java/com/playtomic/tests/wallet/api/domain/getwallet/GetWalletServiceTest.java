package com.playtomic.tests.wallet.api.domain.getwallet;

import static com.playtomic.tests.wallet.api.TestData.WALLET_ID;
import static com.playtomic.tests.wallet.api.TestData.aSampleWallet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.playtomic.tests.wallet.api.domain.model.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetWalletServiceTest {

  @Mock private WalletRepository walletRepository;
  private GetWalletService service;

  @BeforeEach
  public void setUp() {
    service = new GetWalletService(walletRepository);
  }

  @Test
  void givenValidWalletId_whenGetWallet_thenReturnsWallet() {
    var expectedWallet = aSampleWallet();

    when(walletRepository.findById(WALLET_ID)).thenReturn(expectedWallet);

    var response = service.getWallet(WALLET_ID);

    assertEquals(expectedWallet.id(), response.id());
    assertEquals(expectedWallet.balance(), response.balance());
    assertEquals(expectedWallet.currency(), response.currency());
  }
}
