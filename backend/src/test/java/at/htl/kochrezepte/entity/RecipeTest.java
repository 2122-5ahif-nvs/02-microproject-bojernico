package at.htl.kochrezepte.entity;

import at.htl.kochrezepte.control.RecipeRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class RecipeTest {
    @Inject
    RecipeRepository recipeRepository;

    @BeforeEach
    void setup() {
        recipeRepository.clear();
    }

    @Test
    void toString_CreateAndCompare_True() {
        Recipe recipe = new Recipe("Wiener Schnitzel für 2 Personen", "Musterfrau Maria", MealType.ENTREE, "mach das");

        recipeRepository.add(recipe);

        recipe = recipeRepository.findById(1L);

        assertThat(recipe.toString())
                .isEqualTo("Recipe Wiener Schnitzel für 2 Personen has id 1 and got written by Musterfrau Maria");
    }

    @Test
    void equals_CreateWithSameName_True() {
        Recipe recipe = new Recipe("Wiener Schnitzel für 2 Personen", "Musterfrau Maria", MealType.ENTREE, "mach das");
        Recipe recipe1 = new Recipe("Wiener Schnitzel für 2 Personen", "Mustermann Max", MealType.ENTREE, "mach das");

        assertThat(recipe)
                .isEqualTo(recipe1);
    }
}
