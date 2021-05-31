package com.mpellegrino.amazon_bot.config.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.mpellegrino.amazon_bot.bean.product.AmazonProduct;
import com.mpellegrino.amazon_bot.bean.product.BpmPowerProduct;
import com.mpellegrino.amazon_bot.bean.product.Product;
import com.mpellegrino.amazon_bot.utils.exceptions.CustomDeserializationException;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProductDeserializer extends JsonDeserializer<Product> {

    public static Logger logger = LogManager.getLogger(ProductDeserializer.class);

    private List<Class> productClassList = Arrays.asList(AmazonProduct.class, BpmPowerProduct.class);

    @SneakyThrows
    @Override
    public Product deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Product product = null;
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode tree = codec.readTree(jsonParser);
        boolean deserialized = false;
        for (Class clazz : productClassList) {
            if(!deserialized) {
                try {
                    product = codec.treeToValue(tree, AmazonProduct.class);
                    deserialized = true;
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
        if(product==null){
            throw new CustomDeserializationException("Product json can not be deserialized, check json integrity");
        }
        return product;
    }
}
