package com.cydeo;

/*
http://ec2-3-84-192-243.compute-1.amazonaws.com:8080/swagger-ui/index.html
^^ DOCUMENTATION LINK (Swagger)
 */

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab08RestApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab08RestApplication.class, args);
    }

    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }

}
