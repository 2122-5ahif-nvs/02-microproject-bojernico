package at.htl.kochrezepte.entity;

import at.htl.kochrezepte.entity.Ingredient;
import at.htl.kochrezepte.entity.MealType;
import at.htl.kochrezepte.entity.Recipe;
import at.htl.kochrezepte.entity.RecipeIngredient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeIngredientTest {
    @Test
    void equals_CreateRecipeIngredientsWithSameValues_True() {
        Recipe recipe = new Recipe("Wiener Schnitzel für 2 Personen", "Musterfrau Maria", MealType.ENTREE, "mach das");
        Ingredient ingredient = new Ingredient("Kalbsschnitzel");
        RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, ingredient, 2, "kg", 25);
        RecipeIngredient recipeIngredient1 = new RecipeIngredient(recipe, ingredient, 1000, "g", 15);

        assertThat(recipeIngredient)
                .isEqualTo(recipeIngredient1);
    }
}
