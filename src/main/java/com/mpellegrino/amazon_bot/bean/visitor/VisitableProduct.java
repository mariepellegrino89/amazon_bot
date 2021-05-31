package com.mpellegrino.amazon_bot.bean.visitor;

import com.mpellegrino.amazon_bot.bean.common.AmazonBotConfig;
import com.mpellegrino.amazon_bot.manager.impl.EmailServiceImpl;
import com.mpellegrino.amazon_bot.utils.AmazonProductUtils;

import java.util.concurrent.ExecutorService;

public interface VisitableProduct {

    //as for now only amazon implementation is working
    void accept(AmazonBotConfig amazonBotConfig, AutoBuyBotVisitor autoBuyBotVisitor, EmailServiceImpl emailService, AmazonProductUtils amazonProductUtils);

}
