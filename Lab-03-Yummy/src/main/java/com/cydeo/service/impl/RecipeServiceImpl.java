package com.cydeo.service.impl;

import com.cydeo.enums.QuantityType;
import com.cydeo.enums.RecipeType;
import com.cydeo.model.Ingredient;
import com.cydeo.model.Recipe;
import com.cydeo.repository.RecipeRepository;
import com.cydeo.service.RecipeService;
import com.cydeo.service.ShareService;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final ShareService shareService;
    private final Faker faker;

    @Override
    public boolean prepareRecipe() {
// create one Recipe object:
        Recipe recipe = new Recipe();
// set the fields:
        recipe.setRecipeID(UUID.randomUUID());
        recipe.setName(faker.food().dish());
        recipe.setDuration(generateRandomValue());
        recipe.setPreparation(faker.lorem().paragraph(5));
        recipe.setIngredients(prepareIngredient());
        recipe.setRecipeType(RecipeType.BREAKFAST);

// save the recipe
        recipeRepository.save(recipe);
// share the recipe:
        shareService.share(recipe);
// return true:
        return true;
    }

    private List<Ingredient> prepareIngredient() {

        List<Ingredient> ingredientsList = new ArrayList<>();

        for (int i = 0; i < generateRandomValue(); i++) {

            Ingredient ingredient = new Ingredient();
            ingredient.setName(faker.food().ingredient());
            ingredient.setQuantity(generateRandomValue());
            ingredient.setQuantityType(QuantityType.TBSP);
            ingredientsList.add(ingredient);
        }
        return ingredientsList;
    }

    private int generateRandomValue() {
        return new Random().nextInt(20);
    }
}
