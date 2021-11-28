package at.htl.kochrezepte.control;

import at.htl.kochrezepte.control.ImportController;
import at.htl.kochrezepte.control.MenuRepository;
import at.htl.kochrezepte.control.RecipeRepository;
import at.htl.kochrezepte.entity.MealType;
import at.htl.kochrezepte.entity.Menu;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class MenuRepositoryTest {
    @Inject
    MenuRepository menuRepository;
    @Inject
    RecipeRepository recipeRepository;

    @BeforeEach
    void setup() {
        this.menuRepository.clear();
        this.recipeRepository.clear();

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
    void notNull_InjectWithCDI_True() {
        assertThat(this.menuRepository)
                .isNotNull();
    }

    @Test
    void hasSize_ReturnList_2() {
        assertThat(this.menuRepository.list())
                .hasSize(2);
    }
}
