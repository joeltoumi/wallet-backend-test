package com.playtomic.tests.wallet.api.domain.paymentclient;

public interface PaymentClient {

    RechargeResponse recharge(RechargeRequest rechargeRequest);

}
