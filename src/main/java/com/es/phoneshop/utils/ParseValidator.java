package com.es.phoneshop.utils;

public class ParseValidator {

    public static boolean validateQuantity(String quantityStr) {
        quantityStr = removeMinusesFromString(quantityStr);
        if (quantityStr.matches("^\\d+([\\.\\,]\\d+)?$")) {
            return true;
        } else {
            return false;
        }
    }
    public static String removeMinusesFromString(String input) {
        return input.replaceAll("-", "");
    }
}
