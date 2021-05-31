package com.mpellegrino.amazon_bot.bean;

import com.mpellegrino.amazon_bot.bean.product.Product;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class AmazonBotConfig {

    private String type;
    private List<Product> products;
    private String mailFrom;
    private String mailTo;
    private String mailText;
    private Boolean buyJustOne;

}
