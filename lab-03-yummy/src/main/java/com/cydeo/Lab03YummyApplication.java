package com.cydeo;

import com.cydeo.config.AuthorConfig;
import com.cydeo.service.RecipeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Lab03YummyApplication {

    public static void main(String[] args) {

        ApplicationContext container = SpringApplication.run(Lab03YummyApplication.class, args);

        RecipeService recipeService = container.getBean(RecipeService.class);
        recipeService.prepareRecipe();

        AuthorConfig authorConfig = container.getBean(AuthorConfig.class);

        System.out.println("Author: " + authorConfig.getName() + " " + authorConfig.getSurname());

    }
}
