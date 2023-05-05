package com.es.phoneshop.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class PriceHistory {
    private Date date;
    private Currency currency;
    private BigDecimal price;
}
