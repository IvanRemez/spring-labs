package com.cydeo.lab04springmvc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Login {
    private String email;
    private String phoneNumber;
    private String password;
}
