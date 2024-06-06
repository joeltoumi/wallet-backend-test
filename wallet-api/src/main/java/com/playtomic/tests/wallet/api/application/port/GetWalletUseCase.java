package com.playtomic.tests.wallet.api.application.port;

import com.playtomic.tests.wallet.api.domain.model.Wallet;

public interface GetWalletUseCase {

    Wallet getWallet(String walletId);
}
