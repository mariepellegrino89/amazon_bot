package com.mpellegrino.amazon_bot.bean.paymentinfo;

import com.mpellegrino.amazon_bot.bean.paymentinfo.PaymentInfo;

public enum BpmPowerPaymentMethodName {

    VISA_MASTERCARD_CARD{
        @Override
        public boolean performPayment(PaymentInfo paymentInfo) {
            return false;
        }
    }, TRANSFER {
        @Override
        public boolean performPayment(PaymentInfo paymentInfo) {
            return false;
        }
    }, AMERICAN_EXPRESS {
        @Override
        public boolean performPayment(PaymentInfo paymentInfo) {
            return false;
        }
    }, POSTEPAY {
        @Override
        public boolean performPayment(PaymentInfo paymentInfo) {
            return false;
        }
    }, SISALPAY {
        @Override
        public boolean performPayment(PaymentInfo paymentInfo) {
            return false;
        }
    }, CASH_ON_DELIVERY {
        @Override
        public boolean performPayment(PaymentInfo paymentInfo) {
            return false;
        }
    }, PAYPAL {
        @Override
        public boolean performPayment(PaymentInfo paymentInfo) {
            return false;
        }
    }, AMAZON_PAY {
        @Override
        public boolean performPayment(PaymentInfo paymentInfo) {
            return false;
        }
    }, SATISPAY {
        @Override
        public boolean performPayment(PaymentInfo paymentInfo) {
            return false;
        }
    };


    public abstract boolean performPayment(PaymentInfo paymentInfo);

}
