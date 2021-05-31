package com.mpellegrino.amazon_bot.manager.runnable;

import com.mpellegrino.amazon_bot.bean.common.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.product.Product;
import com.mpellegrino.amazon_bot.bean.visitor.AutoBuyBotConcreteVisitor;
import com.mpellegrino.amazon_bot.bean.visitor.AutoBuyBotVisitor;
import com.mpellegrino.amazon_bot.manager.impl.AmazonBotRunnerGpuImpl;
import com.mpellegrino.amazon_bot.manager.impl.EmailServiceImpl;
import com.mpellegrino.amazon_bot.utils.AmazonProductUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

public class ProductRunnable implements Runnable {

    public static Logger logger = LogManager.getLogger(ProductRunnable.class);


    private final AmazonBotConfig amazonBotConfig;
    private final Product product;
    private final EmailServiceImpl emailService;
    private final AmazonProductUtils amazonProductUtils;
    private final boolean errorHasBeenNotified;
    private final String mailDev;

    public ProductRunnable(AmazonBotConfig amazonBotConfig, Product product, EmailServiceImpl emailService,  AmazonProductUtils amazonProductUtils, Boolean errorHasBeenNotified, String mailDev) {
        this.amazonBotConfig = amazonBotConfig;
        this.product = product;
        this.emailService = emailService;
        this.amazonProductUtils = amazonProductUtils;
        this.errorHasBeenNotified = errorHasBeenNotified;
        this.mailDev = mailDev;
    }


    @SneakyThrows
    @Override
    public void run() {
        try {
            logger.info("Run: " + Thread.currentThread().getName());
            //visitor pattern implementation
            AutoBuyBotVisitor autoBuyBotVisitor = new AutoBuyBotConcreteVisitor();
            product.accept(amazonBotConfig, autoBuyBotVisitor, emailService, amazonProductUtils);
        } catch (Exception e) {
            logger.error("Exception during run method in ProductRunnable, error has already been notified -> {}", errorHasBeenNotified);
            if(!errorHasBeenNotified){
                AmazonBotRunnerGpuImpl.errorHasBeenNotified=true;
                emailService.sendMail(mailDev, "ERROR AUTOBUY BOT SERVICE", "Error during purchase of the following product " + product.getTitle() + " with url " + product.getUrl() + "stack trace: \n " + ExceptionUtils.getStackTrace(e));
            }
        }
    }
}
