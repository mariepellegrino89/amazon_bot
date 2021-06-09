package com.mpellegrino.amazon_bot.bean.product;

import com.mpellegrino.amazon_bot.bean.common.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.visitor.AutoBuyBotVisitor;
import com.mpellegrino.amazon_bot.bean.visitor.VisitableProduct;
import com.mpellegrino.amazon_bot.manager.impl.EmailServiceImpl;
import com.mpellegrino.amazon_bot.utils.AmazonProductUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.concurrent.ExecutorService;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class AmazonProduct extends Product implements VisitableProduct {


    private String accountEmail;
    private String accountPassword;
    private boolean shippedByAmazonOnly;

    @Override
    public void accept(AmazonBotConfig amazonBotConfig, AutoBuyBotVisitor autoBuyBotVisitor, EmailServiceImpl emailService, AmazonProductUtils amazonProductUtils) {
        //visit the item
        autoBuyBotVisitor.visit(amazonBotConfig, this, emailService,  amazonProductUtils);
    }
}
