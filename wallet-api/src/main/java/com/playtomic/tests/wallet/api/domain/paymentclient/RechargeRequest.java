package com.playtomic.tests.wallet.api.domain.paymentclient;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RechargeRequest(String creditCard, BigDecimal amount) {}
