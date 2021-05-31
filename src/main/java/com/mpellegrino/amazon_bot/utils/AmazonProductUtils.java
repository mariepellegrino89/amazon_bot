package com.mpellegrino.amazon_bot.utils;

import com.mpellegrino.amazon_bot.bean.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.product.AmazonProduct;
import com.mpellegrino.amazon_bot.bean.product.Product;
import com.mpellegrino.amazon_bot.bean.visitor.AutoBuyBotConcreteVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class AmazonProductUtils {

    public static Logger logger = LogManager.getLogger(AmazonProductUtils.class);


    public static AmazonOrderAndBuyResponse orderAndBuy(AmazonBotConfig amazonBotConfig, AmazonProduct product, ExecutorService executor) throws InterruptedException {
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
        List<Product> products = Collections.synchronizedList(amazonBotConfig.getProducts());
        logger.info("Other thread Product list bought value -> {}", products.stream().map(Product::getBought).collect(Collectors.toList()));
        if (products.stream().anyMatch(pr -> pr.getBought()!=null && pr.getBought())) {
            logger.info("one item has been bought, thus breaking the for loop");
            executor.shutdownNow();
            return new AmazonOrderAndBuyResponse(false, true);
        }
        logger.info("Checks have been performed on multiple threads for item to have been bought, proceeding.");
//        product.getChromeDriver().findElements(By.name("proceedToRetailCheckout"))
//                .stream()
//                .filter(w -> w.isDisplayed() && w.isEnabled())
//                .findAny()
//                .ifPresent(webElement -> new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click());
//        product.getChromeDriver().findElements(By.id("proceed-to-checkout-action"))
//                .stream()
//                .filter(w -> w.isDisplayed() && w.isEnabled())
//                .findAny()
//                .ifPresent(webElement -> new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click());
//        product.getChromeDriver().findElements(By.name("placeYourOrder1"))
//                .stream()
//                .filter(w -> w.isDisplayed() && w.isEnabled())
//                .findAny()
//                .ifPresent(webElement -> new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click());
        return new AmazonOrderAndBuyResponse(true, false);
    }

    public static void login(AmazonProduct product) throws InterruptedException {
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
        inputPw.sendKeys(product.getAccountPasswordEnc());
        WebElement signInSubmit = product.getChromeDriver().findElement(By.id("signInSubmit"));
        new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(signInSubmit)).click();
    }

    public static boolean checkPriceAndSeller(AmazonProduct product) throws InterruptedException {
        List<WebElement> singleBuyDiv = product.getChromeDriver().findElements(By.id("newAccordionRow"));
        if (!singleBuyDiv.isEmpty()) {
            new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(singleBuyDiv.get(0))).click();
        }
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
            return false;
        }
        Double aDouble = Utils.convertPriceToDouble(priceToCheck);
        return seller.getText().contains("Venduto e spedito da Amazon.") && aDouble.compareTo(product.getMaxPrice()) < 0;
    }
}
