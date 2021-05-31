package com.mpellegrino.amazon_bot.utils;

public class Utils {

    public static Double convertPriceToDouble(String price){
        String numbers = price.replaceAll("A-Za-z", "");
        numbers = numbers.replace(".","").replace("€","").replace(",",".").trim();
        return Double.valueOf(numbers);
    }
}
