package at.htl.kochrezepte.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NaturalId;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity(name = "ingredient")
@XmlRootElement
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "in_id")
    private Long id;

    @Column(name = "in_name")
    private String name;

    @Column(name = "in_description")
    private String description;

    @Transient
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "ingredient", fetch = FetchType.LAZY)
    private List<RecipeIngredient> ingredients;

    public Ingredient() {}

    public Ingredient(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>();
        this.description = "";
    }

    public Ingredient(String name, String description) {
        this.name = name;
        this.description = description;
        this.ingredients = new ArrayList<>();
    }

    public void setName(String name) {
        this.name =  name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
