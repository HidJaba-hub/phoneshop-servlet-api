package com.es.phoneshop;

import org.apache.maven.shared.utils.StringUtils;

import java.util.Arrays;
import java.util.function.BiPredicate;

public class StringChecker implements BiPredicate<String, String> {
    @Override
    public boolean test(String string, String search) {
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(search)) {
            return false;
        }
        String[] searchWords = search.toLowerCase().split("\\s+");
        return Arrays.stream(searchWords)
                .anyMatch(word -> word.toLowerCase().contains(string.toLowerCase()));
    }
}
