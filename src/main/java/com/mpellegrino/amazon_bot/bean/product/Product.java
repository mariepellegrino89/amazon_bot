package com.mpellegrino.amazon_bot.bean.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mpellegrino.amazon_bot.bean.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.visitor.VisitableProduct;
import lombok.Data;
import lombok.ToString;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AmazonProduct.class, name = "amazonProduct"),
        @JsonSubTypes.Type(value = BpmPowerProduct.class, name = "bpmPowerProduct")
})
@ToString(callSuper = true)
public abstract class Product implements VisitableProduct {

    private String title;
    private String url;
    private Double maxPrice;
    private ChromeDriver chromeDriver;
    private Boolean bought;
    public SellSource sellSource;
    public String mailTo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public ChromeDriver getChromeDriver() {
        return chromeDriver;
    }

    public void setChromeDriver(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
    }

    public Boolean getBought() {
        return bought;
    }

    public void setBought(Boolean bought) {
        this.bought = bought;
    }

    public SellSource getSellSource() {
        return sellSource;
    }

    public void setSellSource(SellSource sellSource) {
        this.sellSource = sellSource;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }
}
