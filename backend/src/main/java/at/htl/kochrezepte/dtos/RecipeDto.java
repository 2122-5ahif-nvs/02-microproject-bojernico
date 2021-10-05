package at.htl.kochrezepte.dtos;

import at.htl.kochrezepte.entity.MealType;

public class RecipeDto {
    private Long id;
    private String name;
    private String author;
    private MealType mealType;

    public RecipeDto() {}


    public RecipeDto(Long id, String name, String author, MealType mealType) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.mealType = mealType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }
}
