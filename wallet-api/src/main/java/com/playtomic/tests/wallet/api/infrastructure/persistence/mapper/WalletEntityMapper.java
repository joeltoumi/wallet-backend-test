package com.playtomic.tests.wallet.api.infrastructure.persistence.mapper;

import com.playtomic.tests.wallet.api.domain.model.Wallet;
import com.playtomic.tests.wallet.api.infrastructure.persistence.dto.WalletDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WalletEntityMapper {

  @Mapping(source = "walletId", target = "id")
  @Mapping(source = "balance", target = "balance")
  @Mapping(source = "currency", target = "currency")
  Wallet map(WalletDTO walletDTO);
}
