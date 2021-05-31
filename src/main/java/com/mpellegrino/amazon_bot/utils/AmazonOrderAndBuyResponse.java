package com.mpellegrino.amazon_bot.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmazonOrderAndBuyResponse {

    private boolean itemBought;
    private boolean anotherItemBoughtInAnotherThread;

}
