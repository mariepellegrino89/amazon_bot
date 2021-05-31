package com.mpellegrino.amazon_bot.bean.paymentinfo;

import lombok.Data;

@Data
public class BpmPowerPaymentMethod {

    private BpmPowerPaymentMethodName bpmPowerPaymentMethodName;
    private PaymentInfo paymentInfo;

}
