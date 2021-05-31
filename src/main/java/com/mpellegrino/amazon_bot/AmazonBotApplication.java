package com.mpellegrino.amazon_bot;

import com.mpellegrino.amazon_bot.bean.product.Product;
import com.mpellegrino.amazon_bot.manager.impl.AmazonBotRunnerGpuImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SpringBootApplication
public class AmazonBotApplication implements CommandLineRunner {


    private final AmazonBotRunnerGpuImpl amazonBotRunnerGpu;

    public AmazonBotApplication(AmazonBotRunnerGpuImpl amazonBotRunnerGpu) {
        this.amazonBotRunnerGpu = amazonBotRunnerGpu;
    }


    public static void main(String[] args) {
        SpringApplication.run(AmazonBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws AWTException, InterruptedException {
        amazonBotRunnerGpu.runBot();
    }
}
