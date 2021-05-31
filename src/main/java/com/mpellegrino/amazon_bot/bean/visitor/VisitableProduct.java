package com.mpellegrino.amazon_bot.bean.visitor;

import com.mpellegrino.amazon_bot.bean.AmazonBotConfig;
import com.mpellegrino.amazon_bot.manager.impl.EmailServiceImpl;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public interface VisitableProduct {

    void accept(AmazonBotConfig amazonBotConfig, AutoBuyBotVisitor autoBuyBotVisitor, EmailServiceImpl emailService, ExecutorService executor);

}
