package at.htl.kochrezepte.dtos;

import java.util.ArrayList;
import java.util.List;

public class DetailedRecipeDto {
    private String name;
    private String shortName;
    private String author;
    private final List<IngredientDto> ingredients = new ArrayList<>();
    private String instructions;

    public DetailedRecipeDto() {
    }

    public DetailedRecipeDto(String name, String shortName, String author, String instructions) {
        this.name = name;
        this.author = author;
        this.instructions = instructions;
        this.shortName = shortName;
    }

    public void addIngredient(IngredientDto ingredientDto) {
        ingredients.add(ingredientDto);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<IngredientDto> getIngredients() {
        return ingredients;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
