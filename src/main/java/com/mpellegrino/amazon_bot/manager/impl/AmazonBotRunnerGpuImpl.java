package com.mpellegrino.amazon_bot.manager.impl;

import com.mpellegrino.amazon_bot.bean.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.product.Product;
import com.mpellegrino.amazon_bot.bean.visitor.AutoBuyBotConcreteVisitor;
import com.mpellegrino.amazon_bot.bean.visitor.AutoBuyBotVisitor;
import com.mpellegrino.amazon_bot.manager.ProductRunnable;
import com.mpellegrino.amazon_bot.manager.interf.AmazonBotRunner;
import com.mpellegrino.amazon_bot.utils.AmazonOrderAndBuyResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AmazonBotRunnerGpuImpl implements AmazonBotRunner {



    @Autowired
    private AmazonBotConfig amazonBotConfig;

    @Autowired
    private EmailServiceImpl emailService;



    public static Logger logger = LogManager.getLogger(AmazonBotRunnerGpuImpl.class);


    @Override
    public void runBot() throws InterruptedException {
        int threadPoolSize = Math.min(amazonBotConfig.getProducts().size(), Runtime.getRuntime().availableProcessors());
        ExecutorService executor= Executors.newFixedThreadPool(threadPoolSize);
        while (!amazonBotConfig.getProducts().isEmpty()) {
            for (Product p : amazonBotConfig.getProducts()) {
                if(((ThreadPoolExecutor) executor).getActiveCount()!=threadPoolSize){
                    List<Product> products = Collections.synchronizedList(amazonBotConfig.getProducts());
                    logger.info("Product list bought value -> {}", products.stream().map(Product::getBought).collect(Collectors.toList()));
                    if (products.stream().anyMatch(pr -> pr.getBought()!=null && pr.getBought())) {
                        logger.info("one item has been bought, thus breaking the for loop");
                        executor.shutdownNow();
                        break;
                    }
                    Thread.sleep(1000L);
                    ProductRunnable productRunnable = new ProductRunnable(amazonBotConfig, p, emailService, executor);
                    executor.execute(productRunnable);
                }

            }
            if(isBreakCondition(amazonBotConfig)){
                logger.info("Break condition for while loop matched, exiting application");
                break;
            }
        }
    }

    private boolean isBreakCondition(AmazonBotConfig amazonBotConfig) {
        logger.info("Analyzing break condition for while cycle");
        boolean justOneToBuyAndBought = amazonBotConfig.getBuyJustOne()!=null && amazonBotConfig.getProducts().stream().anyMatch(pr-> pr.getBought()!=null && pr.getBought());
        boolean allToBuyAndAllBought = (amazonBotConfig.getBuyJustOne() == null || !amazonBotConfig.getBuyJustOne()) && amazonBotConfig.getProducts().stream().allMatch(pr-> pr.getBought()!=null && pr.getBought());
        logger.info("Condition justOneToBuyAndBought {}, condition allToBuyAndAllBought {}", justOneToBuyAndBought, allToBuyAndAllBought);
        amazonBotConfig.getProducts().removeIf(p-> p.getBought()!=null && p.getBought());
        logger.info("Removed items for been bought, items remaining in the list are {}", amazonBotConfig.getProducts().toString());
        return justOneToBuyAndBought || allToBuyAndAllBought;
    }


}
