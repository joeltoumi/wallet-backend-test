package com.playtomic.tests.wallet.api.application.rest;

import com.playtomic.tests.wallet.api.WalletsApi;
import com.playtomic.tests.wallet.api.application.port.GetWalletUseCase;
import com.playtomic.tests.wallet.api.application.port.TopUpWalletUseCase;
import com.playtomic.tests.wallet.api.application.rest.mapper.GetWalletResponseMapper;
import com.playtomic.tests.wallet.api.application.rest.mapper.TopUpWalletMapper;
import com.playtomic.tests.wallet.dto.TopUpRequestDTO;
import com.playtomic.tests.wallet.dto.WalletDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WalletController implements WalletsApi {
  private final GetWalletUseCase getWalletUseCase;
  private final GetWalletResponseMapper getWalletResponseMapper;
  private final TopUpWalletUseCase topUpWalletUseCase;
  private final TopUpWalletMapper topUpWalletMapper;
  private final Logger log = LoggerFactory.getLogger(WalletController.class);

  @RequestMapping("/")
  void log() {
    log.info("Logging from /");
  }

  @Override
  public ResponseEntity<WalletDetailsDTO> v1WalletsIdGet(String id) {
    return ResponseEntity.ok(getWalletResponseMapper.map(getWalletUseCase.getWallet(id)));
  }

  @Override
  public ResponseEntity<WalletDetailsDTO> v1WalletsIdTopupPost(
      String id, TopUpRequestDTO topUpRequestDTO) {
    return ResponseEntity.ok(
        getWalletResponseMapper.map(
            topUpWalletUseCase.topUpWallet(id, topUpWalletMapper.map(topUpRequestDTO))));
  }
}
