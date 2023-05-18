package com.es.phoneshop.model.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class PriceHistory implements Serializable {

    private Date date;
    private Currency currency;
    private BigDecimal price;
}
