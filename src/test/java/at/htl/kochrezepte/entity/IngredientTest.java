package at.htl.kochrezepte.entity;

import at.htl.kochrezepte.entity.Ingredient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IngredientTest {
    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setup() {
    }

    @Test
    void toString_CreateAndCompare_True() {
        Ingredient ingredient = new Ingredient( "Milch");

        assertThat(ingredient.toString())
                .isEqualTo("Milch");
    }

    @Test
    void equals_CreateWithSameName_True() {
        Ingredient ingredient = new Ingredient("Milch");
        Ingredient ingredient1 = new Ingredient("Milch");

        assertThat(ingredient)
                .isEqualTo(ingredient1);
    }
}