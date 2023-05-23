package com.es.phoneshop.utils;

import java.util.Map;

public interface ParseValidator {

    boolean validate(String stringToValidate, Map<Long, String> errors, Long productId);
}
