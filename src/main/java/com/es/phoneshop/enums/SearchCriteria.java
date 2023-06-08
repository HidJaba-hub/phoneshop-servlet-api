package com.es.phoneshop.enums;

import java.util.Arrays;
import java.util.List;

public enum SearchCriteria {

    ALL_WORDS, ANY_WORDS;

    public static List<SearchCriteria> getSearchCriteria() {
        return Arrays.asList(SearchCriteria.values());
    }
}
