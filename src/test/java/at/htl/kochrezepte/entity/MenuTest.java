package at.htl.kochrezepte.entity;

import at.htl.kochrezepte.entity.MealType;
import at.htl.kochrezepte.entity.Menu;
import at.htl.kochrezepte.entity.Recipe;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuTest {

    private static Recipe starter;
    private static Recipe entree;
    private static Recipe dessert;

    @BeforeAll
    static void beforeAll() {
        starter = new Recipe("Nudelsuppe für 2 Personen", "Mustermann Max", MealType.STARTER, "mach das");
        entree = new Recipe("Wiener Schnitzel für 4 Personen", "Musterfrau Maria", MealType.ENTREE, "mach das");
        dessert = new Recipe("Eispalatschinken für 2 Personen", "Mustersohn Markus", MealType.DESSERT, "mach das");
    }

    @Test
    void toString_CreateAndCompare_True() {
        Menu menu = new Menu("Menu1", starter, entree, dessert);

        assertThat(menu.toString())
                .isEqualTo("Menu called Menu1 consists of a Nudelsuppe für 2 Personen as starter" +
                        ", Wiener Schnitzel für 4 Personen as entree and " +
                        "Eispalatschinken für 2 Personen as dessert.");
    }

    @Test
    void equals_CreateWithSameMeals_True() {
        Menu menu = new Menu("Menu1", starter, entree, dessert);
        Menu menu1 = new Menu("Menu2", starter, entree, dessert);

        assertThat(menu)
                .isEqualTo(menu1);
    }
}
