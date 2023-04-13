package com.cydeo.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Balance extends BaseEntity {

    private Integer amount;

    @OneToOne
    private Customer customer;

}
