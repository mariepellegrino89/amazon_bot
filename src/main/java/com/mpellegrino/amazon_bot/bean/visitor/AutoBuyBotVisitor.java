package com.mpellegrino.amazon_bot.bean.visitor;

import com.mpellegrino.amazon_bot.bean.common.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.product.AmazonProduct;
import com.mpellegrino.amazon_bot.bean.product.BpmPowerProduct;
import com.mpellegrino.amazon_bot.manager.impl.EmailServiceImpl;
import com.mpellegrino.amazon_bot.utils.AmazonProductUtils;

import java.util.concurrent.ExecutorService;

public interface AutoBuyBotVisitor {

    void visit(AmazonBotConfig amazonBotConfig, AmazonProduct amazonProduct, EmailServiceImpl emailService, AmazonProductUtils amazonProductUtils);
    void visit(AmazonBotConfig amazonBotConfig, BpmPowerProduct bpmPowerProduct, EmailServiceImpl emailService);

}
