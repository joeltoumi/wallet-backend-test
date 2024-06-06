package com.playtomic.tests.wallet.api.domain.topup;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TopUpRequest(String creditCard, BigDecimal amount) {}
