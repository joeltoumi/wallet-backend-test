package com.playtomic.tests.wallet.api.application.port;

import com.playtomic.tests.wallet.api.domain.model.Wallet;
import com.playtomic.tests.wallet.api.domain.topup.TopUpRequest;

public interface TopUpWalletUseCase {

    Wallet topUpWallet(String id, TopUpRequest topUpRequest);

}
