package com.es.phoneshop.validators;

import java.util.Map;

public class QuantityParseValidator implements ParseValidator {

    private QuantityParseValidator() {

    }

    public static QuantityParseValidator getInstance() {
        return QuantityParseValidator.SingletonManager.INSTANCE.getSingleton();
    }

    public void validate(String quantityStr, Map<Long, String> errors, Long productId) {
        if (!quantityStr.matches("^-?\\d+([\\.\\,]\\d+)?$")) {
            errors.put(productId, "Not a number");
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
