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
    private final ExecutorService executor;
    private final AmazonProductUtils amazonProductUtils;
    private boolean errorHasBeenNotified;
    private final String mailDev;

    public ProductRunnable(AmazonBotConfig amazonBotConfig, Product product, EmailServiceImpl emailService, ExecutorService executor, AmazonProductUtils amazonProductUtils, Boolean errorHasBeenNotified, String mailDev) {
        this.amazonBotConfig = amazonBotConfig;
        this.product = product;
        this.emailService = emailService;
        this.executor = executor;
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
            product.accept(amazonBotConfig, autoBuyBotVisitor, emailService, executor, amazonProductUtils);
        } catch (Exception e) {
            logger.error("Exception during run method in ProductRunnable, error has already been notified -> {}", errorHasBeenNotified);
            if(!errorHasBeenNotified){
                AmazonBotRunnerGpuImpl.errorHasBeenNotified=true;
                emailService.sendMail(mailDev, "ERROR AUTOBUY BOT SERVICE", "Error during purchase of the following product " + product.getTitle() + " with url " + product.getUrl() + "stack trace: \n " + ExceptionUtils.getStackTrace(e));
            }
        }
    }

//    private void orderAndBuy() throws InterruptedException {
//        Thread.sleep(2000L);
//
//        List<WebElement> singleBuyDiv = product.getChromeDriver().findElements(By.id("newAccordionRow"));
//        if (!singleBuyDiv.isEmpty()) {
//            new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(singleBuyDiv.get(0))).click();
//        }
//        WebElement addToCartButton = product.getChromeDriver().findElement(By.id("add-to-cart-button"));
//        new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
//        Thread.sleep(2000L);
//        Optional<WebElement> proceed1 = product.getChromeDriver().findElements(By.id("hlb-ptc-btn-native")).stream().findAny();
//        Optional<WebElement> cart1 = product.getChromeDriver().findElements(By.id("attach-view-cart-button-form")).stream().findAny();
//        proceed1.ifPresent(webElement -> new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click());
//        cart1.ifPresent(webElement -> new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click());
//        Thread.sleep(2000L);
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
//
//    }
//
//    private void login() throws InterruptedException {
//        Thread.sleep(2000L);
//        List<WebElement> loginButtonStatisc = product.getChromeDriver().findElements(By.id("nav-link-accountList-nav-line-1"));
//        Thread.sleep(2000L);
//        Optional<WebElement> accediStatic = loginButtonStatisc.stream().filter(webElement -> webElement.getText().contains("Ciao, Accedi")).findFirst();
//
//        accediStatic.ifPresent(webElement -> new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(webElement)).click());
//
//        Thread.sleep(2000L);
//        WebElement inputEmail = product.getChromeDriver().findElement(By.id("ap_email"));
//        inputEmail.sendKeys(accountMail);
//        WebElement continueButton = product.getChromeDriver().findElement(By.id("continue"));
//        new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(continueButton)).click();
//        Thread.sleep(2000L);
//        WebElement inputPw = product.getChromeDriver().findElement(By.id("ap_password"));
//        inputPw.sendKeys(accounPassword);
//        WebElement signInSubmit = product.getChromeDriver().findElement(By.id("signInSubmit"));
//        new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(signInSubmit)).click();
//    }
//
//    private boolean checkPriceAndSeller(Product p) throws InterruptedException {
//        List<WebElement> singleBuyDiv = product.getChromeDriver().findElements(By.id("newAccordionRow"));
//        if (!singleBuyDiv.isEmpty()) {
//            new WebDriverWait(product.getChromeDriver(), 10).until(ExpectedConditions.elementToBeClickable(singleBuyDiv.get(0))).click();
//        }
//        Thread.sleep(2000L);
//        Optional<WebElement> priceblockOurprice = product.getChromeDriver().findElements(By.id("priceblock_ourprice")).stream().findAny();
//        Optional<WebElement> priceblockDealprice = product.getChromeDriver().findElements(By.id("priceblock_dealprice")).stream().findAny();
//
//
//        WebElement seller = product.getChromeDriver().findElement(By.id("merchant-info"));
//        Thread.sleep(2000L);
//        String priceToCheck = null;
//        if (priceblockDealprice.isPresent()) {
//            priceToCheck = priceblockDealprice.get().getText();
//        } else if (priceblockOurprice.isPresent()) {
//            priceToCheck = priceblockOurprice.get().getText();
//        } else {
//            return false;
//        }
//        Double aDouble = Utils.convertPriceToDouble(priceToCheck);
//        return seller.getText().contains("Venduto e spedito da Amazon.") && aDouble.compareTo(p.getMaxPrice()) < 0;
//    }
}
