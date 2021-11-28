package at.htl.kochrezepte.boundary;

import at.htl.kochrezepte.control.IngredientRepository;
import at.htl.kochrezepte.control.RecipeRepository;
import at.htl.kochrezepte.dtos.PostPutIngredientDto;
import at.htl.kochrezepte.entity.Ingredient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.util.LinkedHashMap;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IngredientServiceTest {

    @Inject
    IngredientRepository ingredientRepository;

    @Inject
    RecipeRepository recipeRepository;

    @BeforeEach
    void setup() {
        this.recipeRepository.clear();
        this.ingredientRepository.clear();

        var ingredient = new PostPutIngredientDto("Milch", "Laktosefrei");
        var ingredient1 = new PostPutIngredientDto("Eier", "Freilandhaltung");

        ingredientRepository.create(ingredient);
        ingredientRepository.create(ingredient1);
    }

    @Test
    @Order(1)
    void get_IngredientName_Milch() {
        var ingredients = given()
                .get("api/ingredient")
                .then()
                .log()
                .body()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("");

        var hashMap = (LinkedHashMap) ingredients.get(0);

        var name = hashMap.get("name").toString();
        assertThat(name)
                .isEqualTo("Milch");
    }

    @Test
    @Order(2)
    void delete_Ingredient_ReturnMessage() {
        String response = given()
                .delete("api/ingredient/delete/1")
                .asString();

        assertThat(response)
                .isEqualTo("Successfully deleted ingredient with id 1");
    }

    @Test
    @Order(3)
    void add_IngredientName_Tomate() {
        given()
                .contentType("application/json")
                .body("[{\"name\": \"Tomate\"}]")
                .when()
                .post("api/ingredient/create")
                .then()
                .statusCode(200);
        var ing = this.ingredientRepository.findById(3L);
        assertThat(ing.getName())
                .isEqualTo("Tomate");
    }
}
