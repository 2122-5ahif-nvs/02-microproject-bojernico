package java.at.htl.kochrezepte;

import at.htl.kochrezepte.entity.Recipe;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class RecipeRepoTest {
    @Test
    void name() {
        List<Recipe> r = given()
                .when()
                .get("api/recipe")
                .then()
                .log()
                .body()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", Recipe.class);
        assertThat(r).isNotNull();

    }
}
