package com.es.phoneshop.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@EqualsAndHashCode
public class GenericEntity {

    private Long id;

    public GenericEntity() {
        this.id = UUID.randomUUID().getMostSignificantBits();
    }
}
