package com.mpellegrino.amazon_bot.manager.impl;

import com.mpellegrino.amazon_bot.bean.common.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.product.Product;
import com.mpellegrino.amazon_bot.manager.interf.AmazonBotRunner;
import com.mpellegrino.amazon_bot.manager.runnable.ProductRunnable;
import com.mpellegrino.amazon_bot.utils.AmazonProductUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
public class AmazonBotRunnerGpuImpl implements AmazonBotRunner {


    @Value("${spring.mail.username}")
    private String mailDev;

    public static Boolean errorHasBeenNotified = false;


    private final AmazonBotConfig amazonBotConfig;

    private final EmailServiceImpl emailService;

    private final AmazonProductUtils amazonProductUtils;



    public static Logger logger = LogManager.getLogger(AmazonBotRunnerGpuImpl.class);

    public AmazonBotRunnerGpuImpl(AmazonBotConfig amazonBotConfig, EmailServiceImpl emailService, AmazonProductUtils amazonProductUtils) {
        this.amazonBotConfig = amazonBotConfig;
        this.emailService = emailService;
        this.amazonProductUtils = amazonProductUtils;
    }


    @Override
    public void runBot() throws InterruptedException {
        int threadPoolSize = Math.min(amazonBotConfig.getProducts().size(), Runtime.getRuntime().availableProcessors());
        logger.info("Thread pool size {}", threadPoolSize);
        //creation of an executorservice with a pool fixed on max cpu capacity or product size if the product size is inferior to the max cpu capacity
        ExecutorService executor= Executors.newFixedThreadPool(threadPoolSize);
        //if product list is empty exit the while cycle
        while (!amazonBotConfig.getProducts().isEmpty()) {
            for (Product p : amazonBotConfig.getProducts()) {
                Thread.sleep(2000L);
                //if thread pool limit has been reached skip this
                int activeCount = ((ThreadPoolExecutor) executor).getActiveCount();
                List<Product> products = Collections.synchronizedList(amazonBotConfig.getProducts());
                if(activeCount<products.size()){
                    //check if items has been bought by other threads
                    logger.info("Product list bought value -> {}", products.stream().map(Product::getBought).collect(Collectors.toList()));
                    //if an item has been bought exit the for loop to ensure no unwanted items will be bought
                    if (products.stream().anyMatch(pr -> pr.getBought()!=null && pr.getBought())) {
                        logger.info("one item has been bought, thus breaking the for loop");
                        break;
                    }
                    //create and execute thread
                    ProductRunnable productRunnable = new ProductRunnable(amazonBotConfig, p, emailService, amazonProductUtils, errorHasBeenNotified, mailDev);
                    try {
                        executor.execute(productRunnable);
                    }catch (Exception e){
                        logger.error("Exception during execute ", e);
                    }
                }

            }
            if(((ThreadPoolExecutor) executor).getActiveCount()!=threadPoolSize && isBreakCondition(amazonBotConfig)){
                logger.info("Break condition for while loop matched, exiting application");
                executor.shutdown();
                break;
            }
        }
    }

    private boolean isBreakCondition(AmazonBotConfig amazonBotConfig) {
        logger.info("Analyzing break condition for while cycle");
        boolean justOneToBuyAndBought = amazonBotConfig.getBuyJustOne()!=null && amazonBotConfig.getBuyJustOne() && amazonBotConfig.getProducts().stream().anyMatch(pr-> pr.getBought()!=null && pr.getBought());
        boolean allToBuyAndAllBought = (amazonBotConfig.getBuyJustOne() == null || !amazonBotConfig.getBuyJustOne()) && amazonBotConfig.getProducts().stream().allMatch(pr-> pr.getBought()!=null && pr.getBought());
        logger.info("Condition justOneToBuyAndBought {}, condition allToBuyAndAllBought {}", justOneToBuyAndBought, allToBuyAndAllBought);
        amazonBotConfig.getProducts().removeIf(p-> p.getBought()!=null && p.getBought());
        logger.info("Bought items have been removed, items remaining in the list are {}", amazonBotConfig.getProducts().toString());
        return justOneToBuyAndBought || allToBuyAndAllBought;
    }


}
