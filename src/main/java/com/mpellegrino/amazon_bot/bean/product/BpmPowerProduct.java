package com.mpellegrino.amazon_bot.bean.product;

import com.mpellegrino.amazon_bot.bean.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.shipping.BPMPowerShippingMode;
import com.mpellegrino.amazon_bot.bean.visitor.AutoBuyBotVisitor;
import com.mpellegrino.amazon_bot.bean.visitor.VisitableProduct;
import com.mpellegrino.amazon_bot.manager.impl.EmailServiceImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class BpmPowerProduct extends Product implements VisitableProduct {

    private BPMPowerShippingMode bpmPowerShippingMode;

    @Override
    public void accept(AmazonBotConfig amazonBotConfig, AutoBuyBotVisitor autoBuyBotVisitor, EmailServiceImpl emailService, ExecutorService executor) {
        autoBuyBotVisitor.visit(amazonBotConfig, this, emailService, executor);
    }

}
