package com.playtomic.tests.wallet.api.domain.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record Wallet(String id, BigDecimal balance, String currency) {}
