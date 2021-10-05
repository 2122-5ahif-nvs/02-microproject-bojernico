package at.htl.kochrezepte.boundary;

import at.htl.kochrezepte.control.ImportController;
import at.htl.kochrezepte.control.MenuRepository;
import at.htl.kochrezepte.control.RecipeRepository;
import at.htl.kochrezepte.dtos.DetailedRecipeDto;
import at.htl.kochrezepte.entity.MealType;
import at.htl.kochrezepte.entity.Menu;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class MenuServiceTest {
    @Inject
    MenuRepository menuRepository;

    @Inject
    RecipeRepository recipeRepository;

    @BeforeEach
    void setup() {
        this.menuRepository.clear();
        this.recipeRepository.clear();

        this.recipeRepository.changePathForTesting();
        ImportController.changePathForTesting();
        var recipes = ImportController.ReadRecipesFromJson();
        assert recipes != null;
        var starters = recipes.stream()
                .filter(r -> r.getMealType() == MealType.STARTER).collect(Collectors.toList());
        var entrees = recipes.stream()
                .filter(r -> r.getMealType() == MealType.ENTREE).collect(Collectors.toList());
        var desserts = recipes.stream()
                .filter(r -> r.getMealType() == MealType.DESSERT).collect(Collectors.toList());

        int count = 0;
        while (!starters.isEmpty() && !entrees.isEmpty() && !desserts.isEmpty()) {
            var s = starters.remove(0);
            var e = entrees.remove(0);
            var d = desserts.remove(0);

            recipeRepository.add(s);
            recipeRepository.add(e);
            recipeRepository.add(d);

            this.menuRepository.create(new Menu("Menu"+ ++count, s, e ,d));
        }
    }

    @Test
    public void get_DessertAuthorNameOfMenu_Eichler_Roman() {
        var menus = given()
                .get("api/menu")
                .then()
                .log()
                .body()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("");

        var hashMap = (LinkedHashMap) menus.get(1);

        var dessertId= hashMap.get("dessertId").toString();

        var name = this.recipeRepository.findDto(Long.parseLong(dessertId)).getAuthor();

        assertThat(name)
                .isEqualTo("Eichler Roman");
    }

    @Test
    public void delete_Menu_ReturnMessage() {
        String response = given()
                .delete("api/menu/delete/1")
                .asString();

        assertThat(response)
                .isEqualTo("Successfully deleted menu with id 1");
    }


}
