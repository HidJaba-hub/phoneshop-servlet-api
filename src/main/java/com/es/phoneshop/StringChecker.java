package com.es.phoneshop;

import java.util.Arrays;

public class StringChecker {
    public static boolean containsWords(String string, String search) {
        if (string == null || search == null) {
            return false;
        }
        String[] searchWords = search.toLowerCase().split("\\s+");
        return Arrays.stream(searchWords)
                .anyMatch(word -> word.toLowerCase().contains(string));
    }
}
