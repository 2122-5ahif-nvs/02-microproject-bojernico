package at.htl.kochrezepte.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
@NamedQuery(
        name = "Recipe_Ingredient.findByIds",
        query = "select ri from recipe_ingredient ri " +
                "where ri.recipe.id = :recipe_id and ri.ingredient.id = :ingredient_id"
)
@NamedQuery(
        name = "Recipe_Ingredient.deleteByIds",
        query = "delete from recipe_ingredient ri where ri.recipe.id = :recipe_id and ri.ingredient.id = :ingredient_id"
)
@NamedQuery(
        name = "Recipe_Ingredient.deleteByRecipeId",
        query = "delete from recipe_ingredient ri where ri.recipe.id = :recipe_id"
)
@NamedQuery(
        name = "Recipe_Ingredient.findByIngredientId",
        query = "select ri from recipe_ingredient ri where ri.ingredient.id = :ingredient_id"
)
@NamedQuery(
        name = "Recipe_Ingredient.findByRecipeId",
        query = "select ri from recipe_ingredient ri where ri.recipe.id = :recipe_id or ri.recipe.shortName = :recipe_shortName"
)

@Entity(name = "recipe_ingredient")
@XmlRootElement
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ri_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ri_recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ri_ingredient_id")
    private Ingredient ingredient;


    private int amount;
    private String unit;
    private int calories; // values in kcal

    public RecipeIngredient() { }

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, int amount, String unit, int calories) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.unit = unit;
        this.amount = amount;
        this.calories = calories;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "amount=" + amount +
                ", unit='" + unit + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return Objects.equals(recipe, that.recipe) &&
                Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipe, ingredient);
    }
}
