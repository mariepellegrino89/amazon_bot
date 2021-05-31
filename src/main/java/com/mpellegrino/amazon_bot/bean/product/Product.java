package com.mpellegrino.amazon_bot.bean.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mpellegrino.amazon_bot.bean.AmazonBotConfig;
import com.mpellegrino.amazon_bot.bean.visitor.VisitableProduct;
import lombok.Data;
import lombok.ToString;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

@Data
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

    public Product() {
    }
}
