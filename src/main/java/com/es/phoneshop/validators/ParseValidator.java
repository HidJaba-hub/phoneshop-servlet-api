package com.es.phoneshop.validators;

import java.util.Map;

public interface ParseValidator {

    void validateQuantity(String stringToValidate, Map<Long, String> errors, Long productId);

    void validateName(String stringToValidate, Map<String, String> errors, String parameter);

    void validateNumber(String stringToValidate, Map<String, String> errors, String parameter);

    void validateDate(String stringToValidate, Map<String, String> errors, String parameter);
}
