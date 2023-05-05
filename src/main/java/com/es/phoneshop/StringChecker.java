package com.es.phoneshop;

import org.apache.maven.shared.utils.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class StringChecker {
    public static double calculateStringSimilarity(String sourceString, String criteriaString) {
        if (StringUtils.isEmpty(sourceString)) {
            return 0;
        }

        Set<String> criteriaWords = Arrays.stream(criteriaString.toLowerCase().split("\\s+"))
                .collect(Collectors.toSet());

        double wordsCount=Arrays.stream(sourceString.toLowerCase().split("\\s+"))
                                .filter(criteriaWords::contains)
                                .count();
        return wordsCount/sourceString.length();
    }
}
