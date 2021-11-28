package at.htl.kochrezepte.boundary;

import at.htl.kochrezepte.control.ImportController;
import at.htl.kochrezepte.control.RecipeRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.LinkedHashMap;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class RecipeServiceTest {
    @Inject
    RecipeRepository recipeRepository;

    @BeforeEach
    void setup() {
        this.recipeRepository.clear();

        var recipes = ImportController.ReadRecipesFromJson();
        assert recipes != null;
        recipes.forEach(r -> recipeRepository.add(r));
    }

    @Test
    void get_NameOfFirstRecipeAuthor_Heislmair_Emma() {
        var recipes = given()
                .get("api/recipe")
                .then()
                .log()
                .body()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("");

        var hashMap = (LinkedHashMap) recipes.get(0);

        var name=  hashMap.get("author").toString();

        assertThat(name)
                .isEqualTo("Heislmair Emma");
    }

    @Test
    void delete_Recipe_ReturnMessage() {
        String response = given()
                .delete("api/recipe/delete/1")
                .asString();

        assertThat(response)
                .isEqualTo("Successfully deleted recipe with id 1");
    }
}
