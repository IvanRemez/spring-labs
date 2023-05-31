package com.cydeo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Address extends BaseEntity {

    private String name;
    private String street;
    private String zipCode;

    @ManyToOne  // MANY Addresses for ONE Customer
    private Customer customer;

}
