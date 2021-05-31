package com.mpellegrino.amazon_bot.bean.visitor;

import com.mpellegrino.amazon_bot.bean.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.product.AmazonProduct;
import com.mpellegrino.amazon_bot.bean.product.BpmPowerProduct;
import com.mpellegrino.amazon_bot.manager.impl.EmailServiceImpl;
import com.mpellegrino.amazon_bot.utils.AmazonOrderAndBuyResponse;
import com.mpellegrino.amazon_bot.utils.AmazonProductUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public class AutoBuyBotConcreteVisitor implements AutoBuyBotVisitor{

    public static Logger logger = LogManager.getLogger(AutoBuyBotConcreteVisitor.class);



    @Override
    public void visit(AmazonBotConfig amazonBotConfig, AmazonProduct product, EmailServiceImpl emailService, ExecutorService executor, AmazonProductUtils amazonProductUtils) {
        logger.info("Starting for item {}", product.getTitle());
        product.setChromeDriver(new ChromeDriver());
        product.getChromeDriver().get(product.getUrl());
        try {
            if (amazonProductUtils.checkPriceAndSeller(product)) {
                amazonProductUtils.login(product);
                AmazonOrderAndBuyResponse amazonOrderAndBuyResponse = amazonProductUtils.orderAndBuy(amazonBotConfig, product, executor);
                if(amazonOrderAndBuyResponse.isAnotherItemBoughtInAnotherThread()){
                    logger.info("An item has been bought in another thread, returning {}", product.getTitle());
                    product.getChromeDriver().close();
                    return;
                }
                product.setBought(true);
                logger.info("An item has been bought!!");
                emailService.sendMail(product.getMailTo(), "Buy confirmation", "The following item has been bought: " + product.getUrl());
                product.getChromeDriver().close();
            } else {
                product.getChromeDriver().navigate().refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Finish for item {}", product.getTitle());
    }

    @Override
    public void visit(AmazonBotConfig amazonBotConfig, BpmPowerProduct bpmPowerProduct, EmailServiceImpl emailService, ExecutorService executor) {

    }


}
