package com.es.phoneshop.utils;

import java.util.Map;

public class QuantityParseValidator implements ParseValidator {

    private QuantityParseValidator() {

    }

    public static QuantityParseValidator getInstance() {
        return QuantityParseValidator.SingletonManager.INSTANCE.getSingleton();
    }

    public boolean validate(String quantityStr, Map<Long, String> errors, Long productId) {
        if (quantityStr.matches("^-?\\d+([\\.\\,]\\d+)?$")) {
            return true;
        } else {
            errors.put(productId, "Not a number");
            return false;
        }
    }

    private enum SingletonManager {

        INSTANCE;
        private static final QuantityParseValidator singleton = new QuantityParseValidator();

        public QuantityParseValidator getSingleton() {
            return singleton;
        }
    }
}
