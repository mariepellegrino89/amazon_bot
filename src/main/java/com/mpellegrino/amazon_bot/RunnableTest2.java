package com.mpellegrino.amazon_bot;

import com.mpellegrino.amazon_bot.bean.product.Product;

import java.util.Random;

public class RunnableTest2 implements Runnable{

    private Product testInt;
    private String threadName = Thread.currentThread().getName();

    public RunnableTest2(Product testInt) {
        this.testInt = testInt;
    }

    public Product getTestInt() {
        return testInt;
    }

    public void setTestInt(Product testInt) {
        this.testInt = testInt;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void run() {
        testInt.setMaxPrice((double) new Random().nextInt(1000));
        System.out.println("Max price is > " + testInt.getMaxPrice());
        if(testInt.getMaxPrice() == 1.0){
            testInt.setBought(true);
        } else {
            testInt.setBought(false);
        }
    }
}
