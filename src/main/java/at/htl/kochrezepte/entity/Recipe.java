package at.htl.kochrezepte.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "recipe")
@Schema(description = "This is a recipe")
@XmlRootElement
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long id;

    @Column(name = "r_name")
    private String name;
    @Column(name = "r_author")
    private String author;

    @Column(name = "r_short_name")
    private String shortName;

    @Column(length = 4096, name = "r_instructions")
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(name = "r_meal_type")
    private MealType mealType;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "recipe", fetch = FetchType.LAZY)
    private List<RecipeIngredient> ingredients;

    @JsonbTransient
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "r_image_id")
    private Image image;

    public Recipe() { }

    public Recipe(String name, String author, MealType mealType, String instructions) {
        this.name = name;
        this.author = author;
        this.mealType = mealType;
        this.instructions = instructions;
        this.ingredients = new ArrayList<>();
        this.shortName = name.substring(0, name.indexOf("für"))
                .toLowerCase()
                .trim()
                .replace(" ", "-");
        this.image = new Image();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

   public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Recipe " +this.getName()+
                " has id " +this.getId()+
                " and got written by " +this.getAuthor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return recipe.getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName());
    }
}
