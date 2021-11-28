package at.htl.kochrezepte.control;

import at.htl.kochrezepte.control.IngredientRepository;
import at.htl.kochrezepte.control.RecipeRepository;
import at.htl.kochrezepte.dtos.PostPutIngredientDto;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class IngredientRepositoryTest {

    @Inject
    IngredientRepository ingredientRepository;

    @Inject
    RecipeRepository recipeRepository;

    @BeforeEach
    void setup() {
        this.recipeRepository.clear();
        this.ingredientRepository.clear();

        ingredientRepository.create(new PostPutIngredientDto("Milch", "Laktosefrei"));
        ingredientRepository.create(new PostPutIngredientDto("Eier", "Freilandhaltung"));
        ingredientRepository.create(new PostPutIngredientDto("Mehl", "Griffig"));
    }

    @Test
    void notNull_InjectWithCDI_True() {
        assertThat(this.ingredientRepository)
                .isNotNull();
    }

    @Test
    void hasSize_ReturnIngredientList_3() {
        assertThat(this.ingredientRepository.list())
                .hasSize(3);
    }

    @Test
    void hasSize_ReturnListAfterInserting_4() {
        var ingredient = new PostPutIngredientDto("Staubzucker", "Kristall-weiß");
        ingredientRepository.create(ingredient);
        assertThat(this.ingredientRepository.list())
                .hasSize(4);
    }

    @Test
    void updateIngredient_UpdateData_ReturnMessage() {
        var ingredient = new PostPutIngredientDto("Mehl", "Mehl sollte griffig sein");
        var ing = ingredientRepository.find("name", ingredient.getName())
                .singleResult();

        assertThat(this.ingredientRepository.update(ingredient, ing.getId()))
                .isEqualTo("Successfully updated ingredient with id 3");
    }
}
