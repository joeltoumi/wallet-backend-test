package com.playtomic.tests.wallet.api.application.rest.mapper;

import com.playtomic.tests.wallet.api.domain.topup.TopUpRequest;
import com.playtomic.tests.wallet.dto.TopUpRequestDTO;
import java.math.BigDecimal;
import org.mapstruct.Mapper;

@Mapper
public interface TopUpWalletMapper {

  TopUpRequest map(TopUpRequestDTO dto);

  default BigDecimal map(Double value) {
    return BigDecimal.valueOf(value);
  }
}
