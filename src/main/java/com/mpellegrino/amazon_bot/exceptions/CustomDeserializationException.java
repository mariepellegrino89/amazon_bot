package com.mpellegrino.amazon_bot.exceptions;

public class CustomDeserializationException extends Exception {

    public CustomDeserializationException(String message) {
        super(message);
    }

    public CustomDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomDeserializationException(Throwable cause) {
        super(cause);
    }

    public CustomDeserializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
