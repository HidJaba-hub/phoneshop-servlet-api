package com.es.phoneshop.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class Product{

    private Long id;
    private String code;
    private String description;
    /**
     * null means there is no price because the product is outdated or new
     */
    private BigDecimal price;
    /**
     * can be null if the price is null
     */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceHistory> priceHistory;

    public Product() {
        this.id = UUID.randomUUID().getMostSignificantBits();
    }

    public Product(String code, String description, BigDecimal price, Currency currency,
                   int stock, String imageUrl, List<PriceHistory> priceHistoryList) {
        this.id = UUID.randomUUID().getMostSignificantBits();
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.priceHistory = priceHistoryList;
    }

}