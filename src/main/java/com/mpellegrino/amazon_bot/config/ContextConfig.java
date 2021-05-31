package com.mpellegrino.amazon_bot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.mpellegrino.amazon_bot.bean.common.AmazonBotConfig;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.IOException;

@Configuration
public class ContextConfig {

    public static Logger logger = LogManager.getLogger(ContextConfig.class);

    @Bean
    @Scope(scopeName = "singleton")
    public AmazonBotConfig getAmazonBotConfig() throws IOException {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        String json = FileUtils.readFileToString(new File("src/main/resources/gpu_bot_config.json"), "UTF-8");
        logger.info(json);
        ObjectMapper objectMapper = new ObjectMapper();
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().build();
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
        AmazonBotConfig amazonBotConfig = objectMapper.readValue(new File("src/main/resources/gpu_bot_config.json"), AmazonBotConfig.class);
        logger.info(amazonBotConfig.toString());
        return amazonBotConfig;
    }




}
