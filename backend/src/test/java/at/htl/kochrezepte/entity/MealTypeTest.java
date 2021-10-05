package at.htl.kochrezepte.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTypeTest {

    @Test
    void equals_CreateOfString_True() {
        MealType mealType = MealType.valueOf("DESSERT");

        assertThat(mealType)
                .isEqualTo(MealType.DESSERT);
    }
}
