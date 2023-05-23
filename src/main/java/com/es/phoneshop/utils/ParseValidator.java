package com.es.phoneshop.utils;

public class ParseValidator {

    public static boolean validateQuantity(String quantityStr) {
        if (quantityStr.matches("^\\d+([\\.\\,]\\d+)?$")) {
            return true;
        } else {
            return false;
        }
    }
}
