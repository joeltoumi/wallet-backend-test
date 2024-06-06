package com.playtomic.tests.wallet.api.application.rest.mapper;

import com.playtomic.tests.wallet.api.domain.model.Wallet;
import com.playtomic.tests.wallet.dto.WalletDetailsDTO;
import org.mapstruct.Mapper;

@Mapper
public interface GetWalletResponseMapper {
    WalletDetailsDTO map(Wallet wallet);
}
