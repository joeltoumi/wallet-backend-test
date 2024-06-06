package com.playtomic.tests.wallet.api.infrastructure.persistence;

import com.playtomic.tests.wallet.api.infrastructure.persistence.dto.WalletDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletJPARepository extends JpaRepository<WalletDTO, String> {

    Optional<WalletDTO> findByWalletId(String walletId);
}
