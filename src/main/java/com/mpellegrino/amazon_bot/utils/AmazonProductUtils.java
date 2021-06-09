package com.mpellegrino.amazon_bot.utils;

import com.mpellegrino.amazon_bot.bean.common.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.common.AmazonOrderAndBuyResponse;
import com.mpellegrino.amazon_bot.bean.product.AmazonProduct;
import com.mpellegrino.amazon_bot.bean.product.Product;
import com.mpellegrino.amazon_bot.manager.impl.EmailServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Component
public class AmazonProductUtils {

    private final EmailServiceImpl emailService;

    @Value("${spring.mail.username}")
    private String mailDev;

    public static Logger logger = LogManager.getLogger(AmazonProductUtils.class);

    public AmazonProductUtils(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }


    public AmazonOrderAndBuyResponse orderAndBuy(AmazonBotConfig amazonBotConfig, AmazonProduct product) {
        AmazonOrderAndBuyResponse amazonOrderAndBuyResponse = new AmazonOrderAndBuyResponse(false, false);
        try {
            logger.info("Order and buy process starting for item {}", product.getTitle());
            //thread sleep to allow browser to open pages and see buttons
            Thread.sleep(2000L);

            List<WebElement> singleBuyDiv = product.getChromeDriver().findElements(By.id("newAccordionRow"));
            if (!singleBuyDiv.isEmpty()) {
                new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(singleBuyDiv.get(0))).click();
            }
            WebElement addToCartButton = product.getChromeDriver().findElement(By.id("add-to-cart-button"));
            new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
            Thread.sleep(2000L);
            Optional<WebElement> proceed1 = product.getChromeDriver().findElements(By.id("hlb-ptc-btn-native")).stream().findAny();
            Optional<WebElement> cart1 = product.getChromeDriver().findElements(By.id("attach-view-cart-button-form")).stream().findAny();
            proceed1.ifPresent(webElement -> new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click());
            cart1.ifPresent(webElement -> new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click());
            Thread.sleep(2000L);
            //check if other threads have concluded other orders
            List<Product> products = Collections.synchronizedList(amazonBotConfig.getProducts());
            logger.info("Other thread Product list bought value -> {}", products.stream().map(Product::getBought).collect(Collectors.toList()));
            if (products.stream().anyMatch(pr -> pr.getBought() != null && pr.getBought())) {
                logger.info("one item has been bought, thus breaking the for loop");
                return new AmazonOrderAndBuyResponse(false, true);
            }
            logger.info("Checks have been performed on multiple threads for item to have been bought, proceeding.");

            //check different buttons
            Optional<WebElement> proceedToRetailCheckout = product.getChromeDriver().findElements(By.name("proceedToRetailCheckout"))
                    .stream()
                    .filter(w -> w.isDisplayed() && w.isEnabled())
                    .findAny();
            proceedToRetailCheckout.ifPresent(webElement -> {
                new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click();
                amazonOrderAndBuyResponse.setItemBought(true);
            });

            Optional<WebElement> proceedToCheckOutAction = product.getChromeDriver().findElements(By.id("proceed-to-checkout-action"))
                    .stream()
                    .filter(w -> w.isDisplayed() && w.isEnabled())
                    .findAny();
            proceedToCheckOutAction.ifPresent(webElement -> {
                new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click();
                amazonOrderAndBuyResponse.setItemBought(true);
            });

            Optional<WebElement> placeYourOrder1 = product.getChromeDriver().findElements(By.name("placeYourOrder1"))
                    .stream()
                    .filter(w -> w.isDisplayed() && w.isEnabled())
                    .findAny();
            placeYourOrder1.ifPresent(webElement -> {
                new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click();
                amazonOrderAndBuyResponse.setItemBought(true);
            });
            if (!amazonOrderAndBuyResponse.isItemBought()) {
                emailService.sendMail(mailDev, "ERROR AUTOBUY BOT SERVICE", "Error in bot item proceed to buy button, check item with title " + product.getTitle() + " and url " + product.getUrl());
                logger.error("No buttons for proceeding to buy item has been found, check webpage for item {}", product.getTitle());
            }
        } catch (InterruptedException e) {
            logger.error("Thread {} interrupted by outside ", Thread.currentThread().getName());
        }
        return amazonOrderAndBuyResponse;
    }

    public void login(AmazonProduct product) throws InterruptedException {
        logger.info("Login process starting for item {}", product.getTitle());
        //thread sleep to allow browser to open pages and see buttons
        Thread.sleep(2000L);
        List<WebElement> loginButtonStatisc = product.getChromeDriver().findElements(By.id("nav-link-accountList-nav-line-1"));
        Thread.sleep(2000L);
        Optional<WebElement> accediStatic = loginButtonStatisc.stream().filter(webElement -> webElement.getText().contains("Ciao, Accedi")).findFirst();

        accediStatic.ifPresent(webElement -> new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click());

        Thread.sleep(2000L);
        WebElement inputEmail = product.getChromeDriver().findElement(By.id("ap_email"));
        inputEmail.sendKeys(product.getAccountEmail());
        WebElement continueButton = product.getChromeDriver().findElement(By.id("continue"));
        new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(continueButton)).click();
        Thread.sleep(2000L);
        WebElement inputPw = product.getChromeDriver().findElement(By.id("ap_password"));
        inputPw.sendKeys(product.getAccountPassword());
        WebElement signInSubmit = product.getChromeDriver().findElement(By.id("signInSubmit"));
        new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(signInSubmit)).click();
        logger.info("Login process ended for item {}", product.getTitle());
    }

    public boolean checkPriceAndSeller(AmazonProduct product) throws InterruptedException {
        logger.info("Checking for price and seller for product {}", product.getTitle());
        List<WebElement> singleBuyDiv = product.getChromeDriver().findElements(By.id("newAccordionRow"));
        if (!singleBuyDiv.isEmpty()) {
            new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(singleBuyDiv.get(0))).click();
        }
        //thread sleep to allow browser to open pages and see buttons
        Thread.sleep(2000L);
        Optional<WebElement> priceblockOurprice = product.getChromeDriver().findElements(By.id("priceblock_ourprice")).stream().findAny();
        Optional<WebElement> priceblockDealprice = product.getChromeDriver().findElements(By.id("priceblock_dealprice")).stream().findAny();


        WebElement seller = product.getChromeDriver().findElement(By.id("merchant-info"));
        Thread.sleep(2000L);
        String priceToCheck = null;
        if (priceblockDealprice.isPresent()) {
            priceToCheck = priceblockDealprice.get().getText();
        } else if (priceblockOurprice.isPresent()) {
            priceToCheck = priceblockOurprice.get().getText();
        } else {
            //couldn't find price tag
            logger.info("Couldn't find known price tag, check webpage for item {}", product.getTitle());
            return false;
        }
        Double priceInDouble = Utils.convertPriceToDouble(priceToCheck);
        boolean soldByAmazon = seller.getText().contains("spedito da Amazon.");
        boolean maxPriceMajorThanPrice = priceInDouble.compareTo(product.getMaxPrice()) < 0;

        logger.info("For item {} the price is {}, max price is {} and seller is {}", product.getTitle(), product.getMaxPrice(), priceInDouble, seller.getText());

        return product.isShippedByAmazonOnly() ? soldByAmazon && maxPriceMajorThanPrice : maxPriceMajorThanPrice;
    }
}
