package com.es.phoneshop.validators;

import java.util.Map;

public interface ParseValidator {

    void validate(String stringToValidate, Map<Long, String> errors, Long productId);
}
