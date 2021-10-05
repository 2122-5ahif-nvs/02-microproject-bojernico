package at.htl.kochrezepte.control;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class RecipeRepositoryTest {
    @Inject
    RecipeRepository recipeRepository;

    @BeforeEach
    void setup() {
        this.recipeRepository.clear();

        this.recipeRepository.changePathForTesting();
        ImportController.changePathForTesting();
        var recipes = ImportController.ReadRecipesFromJson();
        assert recipes != null;
        recipes.forEach(r -> recipeRepository.add(r));
    }

    @Test
    void notNull_InjectWithCDI_True() {
        assertThat(this.recipeRepository)
                .isNotNull();
    }

    @Test
    void hasSize_ReturnList_3() {
        assertThat(this.recipeRepository.list())
                .hasSize(6);
    }

    @Test
    void hasSize_DeleteAndReturnList_2() {
        this.recipeRepository.delete(1L);
        assertThat(this.recipeRepository.list())
                .hasSize(5);
    }
}
