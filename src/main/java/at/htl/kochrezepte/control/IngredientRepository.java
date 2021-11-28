package at.htl.kochrezepte.control;

import at.htl.kochrezepte.dtos.GetIngredientDto;
import at.htl.kochrezepte.dtos.PostPutIngredientDto;
import at.htl.kochrezepte.entity.Ingredient;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.dom4j.IllegalAddException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class IngredientRepository implements PanacheRepository<Ingredient> {

    @Inject
    RecipeRepository recipeRepository;

    public IngredientRepository() {}

    @Transactional
    public List<GetIngredientDto> list() {
        return this.listAll()
                .stream()
                .map(i -> new GetIngredientDto(i.getId(), i.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    public Ingredient findByObject(JsonObject ingredient) {
        var name = ingredient.getString("name");
        return findByName(name);
    }

    @Transactional
    public Ingredient findByName(String name) {
        try {
            return this.find("name", name).singleResult();
        } catch (NoResultException ex)
        {
            return null;
        }
    }

    @Transactional
    public Ingredient add(JsonObject ingredient) {
        var ing = new Ingredient(
                ingredient.getString("name"));
        this.persist(ing);
        return ing;
    }

    @Transactional
    public Ingredient find(Long ingredientId) {
        try {
            return this.findById(ingredientId);
        } catch (NoResultException ex) {
            throw new NoResultException("Ingredient doesn't exist");
        }
    }

    @Transactional
    public Ingredient create(PostPutIngredientDto dto) {
        var ingredient = this.findByName(dto.getName());
        if (ingredient != null) {
            throw new IllegalAddException("Ingredient already exists");
        }
        ingredient = new Ingredient(dto.getName());
        ingredient.setDescription(dto.getDescription());
        this.persist(ingredient);
        return ingredient;
    }

    @Transactional
    public String update(PostPutIngredientDto updatedIngredient, Long ingredientId) {
        var ingredient = this.find(ingredientId);
        if(ingredient == null) {
            throw new NullPointerException("Ingredient doesn't exist");
        }

        ingredient.setDescription(updatedIngredient.getDescription());
        ingredient.setName(updatedIngredient.getName());
        return "Successfully updated ingredient with id " +ingredientId;
    }

    @Transactional
    public String delete(Long ingredientId) {
        if(this.find(ingredientId) == null) {
            throw new NullPointerException("Ingredient doesn't exist");
        }

        if(this.recipeRepository.containsIngredient(ingredientId)) {
            throw new IllegalArgumentException("Ingredient is used in a recipe, can't be deleted");
        }

        this.deleteById(ingredientId);

        return "Successfully deleted ingredient with id " +ingredientId;
    }

    @Transactional
    public void clear() {
        this.deleteAll();
        this.getEntityManager().createNativeQuery("ALTER TABLE ingredient ALTER COLUMN in_id RESTART WITH 1")
                .executeUpdate();
    }

}
