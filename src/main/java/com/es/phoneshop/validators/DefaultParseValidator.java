package com.es.phoneshop.validators;

import java.util.Map;

public class DefaultParseValidator implements ParseValidator {

    private DefaultParseValidator() {

    }

    public static DefaultParseValidator getInstance() {
        return DefaultParseValidator.SingletonManager.INSTANCE.getSingleton();
    }

    public void validateQuantity(String quantityStr, Map<Long, String> errors, Long productId) {
        if (!quantityStr.matches("^-?\\d+([\\.\\,]\\d+)?$")) {
            errors.put(productId, "Not a number");
        }
    }

    @Override
    public void validateName(String stringToValidate, Map<String, String> errors, String parameter) {
        if (!stringToValidate.matches("^\\D+$")) {
            errors.put(parameter, "Remove numbers, please");
        }
    }

    @Override
    public void validateNumber(String stringToValidate, Map<String, String> errors, String parameter) {
        if (!stringToValidate.matches("^\\+375(\\s+)?\\(?(17|29|33|44)\\)?(\\s+)?[0-9]{7}$")) {
            errors.put(parameter, "Put number in format +375(17/29/22/44)xxxxxxx");
        }
    }

    @Override
    public void validateDate(String stringToValidate, Map<String, String> errors, String parameter) {
        if (!stringToValidate.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}$")) {
            errors.put(parameter, "Put valid date");
        }
    }

    private enum SingletonManager {

        INSTANCE;
        private static final DefaultParseValidator singleton = new DefaultParseValidator();

        public DefaultParseValidator getSingleton() {
            return singleton;
        }
    }
}
