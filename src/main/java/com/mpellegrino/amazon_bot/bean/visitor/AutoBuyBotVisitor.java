package com.mpellegrino.amazon_bot.bean.visitor;

import com.mpellegrino.amazon_bot.bean.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.product.AmazonProduct;
import com.mpellegrino.amazon_bot.bean.product.BpmPowerProduct;
import com.mpellegrino.amazon_bot.manager.impl.EmailServiceImpl;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public interface AutoBuyBotVisitor {

    public void visit(AmazonBotConfig amazonBotConfig, AmazonProduct amazonProduct, EmailServiceImpl emailService, ExecutorService executor);
    public void visit(AmazonBotConfig amazonBotConfig, BpmPowerProduct bpmPowerProduct, EmailServiceImpl emailService, ExecutorService executor);

}
