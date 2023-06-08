package com.es.phoneshop.model.entity;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.entity.cart.Cart;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Order extends Cart {

    private String secureId;
    private BigDecimal subtotal;
    private BigDecimal deliveryCost;

    private String firstName;
    private String lastName;
    private String phone;

    private String deliveryAddress;
    private LocalDate deliveryDate;
    private PaymentMethod paymentMethod;

    public Order() {
        super();
        secureId = UUID.randomUUID().toString();
    }
}
