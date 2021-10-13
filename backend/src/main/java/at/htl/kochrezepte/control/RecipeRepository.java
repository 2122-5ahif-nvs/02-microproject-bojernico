package at.htl.kochrezepte.control;

import at.htl.kochrezepte.dtos.*;
import at.htl.kochrezepte.entity.Image;
import at.htl.kochrezepte.entity.MealType;
import at.htl.kochrezepte.entity.Recipe;
import at.htl.kochrezepte.entity.RecipeIngredient;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.dom4j.IllegalAddException;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeRepository implements PanacheRepository<Recipe> {
    @Inject
    IngredientRepository ingredientRepository;

    public RecipeRepository() {
    }

    @Transactional
    public List<RecipeDto> list() {
       return this.listAll()
               .stream()
               .map(r -> new RecipeDto(r.getId(), r.getName(), r.getAuthor(), r.getMealType()))
               .collect(Collectors.toList());
    }

    @Transactional
    public DetailedRecipeDto findDto(Long id) {
        try {
            var recipe = this.findById(id);
            var detailedRecipe = new DetailedRecipeDto(recipe.getName(), recipe.getShortName(), recipe.getAuthor(), recipe.getInstructions());

            var ingredients = this.getEntityManager()
                            .createQuery("select CONCAT(ri.amount, ' ', ri.unit) as measurements, i.name as name " +
                                    "from recipe_ingredient ri, ingredient i " +
                                    "where ri.ingredient.id = i.id and ri.recipe.id = :id")
                            .setParameter("id", id)
                            .getResultList();

            for (var ingredient: ingredients) {
                detailedRecipe.addIngredient(
                        new IngredientDto(((Object[]) ingredient)[1].toString(),
                                ((Object[]) ingredient)[0].toString()));
            }

            return detailedRecipe;
        } catch (NoResultException ex) {
            throw new NoResultException("Recipe with id " + id + " doesn't exist");
        }
    }

    @Transactional
    public void add(Recipe recipe) {
        findByNameAndAuthor(recipe.getName(), recipe.getAuthor());
        recipe.setImage(this.getImage(recipe.getShortName()));

        this.persist(recipe);

        for (var ingredient: recipe.getIngredients()
             ) {
            var ing = this.ingredientRepository.findByName(ingredient.getIngredient().getName());
            if(ing == null) {
                ing = this.ingredientRepository
                        .create(new PostPutIngredientDto(
                                ingredient.getIngredient().getName(),
                                ingredient.getIngredient().getDescription()));
            }
            ingredient = new RecipeIngredient(recipe, ing, ingredient.getAmount(),
                    ingredient.getUnit(), ingredient.getCalories());
            this.getEntityManager().persist(ingredient);

        }
    }

    private Image getImage(String shortName){
        String filePath1 = ".";
        File f = new File(filePath1);
        String path;
        try {
            path = f.getCanonicalPath();
        } catch (IOException e) {
            throw new IllegalArgumentException("You have some big errors in your program, download Windows 10 Home");
        }


        File directory = new File(path + "/data/images");
        FileNameFilter filter = new FileNameFilter(shortName);

        var names = directory.list(filter);

        assert names != null;

        if(names.length == 0) {
            throw new IllegalArgumentException("File doesn't exist" +shortName);
        }

        try {
            var filePath = directory.getPath()+ "/" + names[0];
            BufferedImage originalImage = ImageIO.read(new File(
                    filePath));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);
            baos.flush();

            return new Image(baos.toByteArray());
        } catch (IOException e) {
            throw new IllegalArgumentException("Errors occurred parsing the file " +names[0]);
        }
    }

    @Transactional
    private void findByNameAndAuthor(String name, String author) {
        try {
            this.find("name = ?1 and author = ?2", name, author).singleResult();
            throw new IllegalAddException("Recipe already exists.");
        } catch (NoResultException ignored) {
        }
    }

    @Transactional
    public String parseJsonObject(JsonObject jsonObject) {
        assert jsonObject != null;

        try {
            var author = jsonObject.getString("author");
            var name = jsonObject.getString("name");

            var mealTypeString = jsonObject.getString("mealType");
            var mealType = MealType.valueOf(mealTypeString.toUpperCase());

            var instructions = jsonObject.getString("instructions");

            var recipe = new Recipe(name, author, mealType, instructions);
            this.add(recipe);

            this.parseRecipeIngredients(jsonObject.getJsonArray("ingredients"), recipe);

            return "Successfully added recipe " + name;
        } catch (NullPointerException ex) {
            throw new NullPointerException("JsonObject has serious errors.");
        }
    }

    @Transactional
    public String update(JsonObject jsonObject, Long recipeId) {
        assert jsonObject != null;

        var recipe = this.findById(recipeId);
        try {
            recipe.setName(jsonObject.getString("name"));
            recipe.setAuthor(jsonObject.getString("author"));
            recipe.setMealType(MealType.valueOf(jsonObject.getString("mealType").toUpperCase()));
            recipe.setInstructions(jsonObject.getString("instructions"));

            this.updateIngredientsById(jsonObject.getJsonArray("ingredients"), recipeId);

            return "Successfully updated recipe with id " + recipeId;
        } catch (NullPointerException ex) {
            throw new NullPointerException("JsonObject has serious errors.");
        }
    }

    @Transactional
    public String delete(Long id) {
        this.delete(this.findById(id));
        return "Successfully deleted recipe with id " + id;
    }

    @Transactional
    public List<RecipeDto> findByMealType(String mealTypeString) {
        var mealType = MealType.valueOf(mealTypeString.toUpperCase());
        return this.list("mealType", mealType)
                .stream()
                .map(r -> new RecipeDto(r.getId(), r.getName(), r.getAuthor(), r.getMealType()))
                .collect(Collectors.toList());
    }

    @Transactional
    private RecipeIngredient getRecipeIngredientByIds(Long recipeId, Long ingredientId) {
        try {
            return this.getEntityManager().createNamedQuery
                    ("Recipe_Ingredient.findByIds", RecipeIngredient.class)
                    .setParameter("recipe_id", recipeId)
                    .setParameter("ingredient_id", ingredientId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw new NoResultException("RecipeIngredient doesn't exist");
        }
    }

    @Transactional
    public void updateIngredientsById(JsonArray jsonArray, Long recipeId) {
        assert jsonArray != null;
        for (var jsonValue : jsonArray) {
            this.updateRecipeIngredient(jsonValue.asJsonObject(), recipeId);
        }
    }

    @Transactional
    private void updateRecipeIngredient(JsonObject jsonObject, Long recipeId) {
        assert jsonObject != null;

        var ingredient = this.ingredientRepository.findByObject(jsonObject);
        var recipeIngredient = this.getRecipeIngredientByIds(recipeId, ingredient.getId());

        recipeIngredient.setAmount(jsonObject.getInt("amount"));
        recipeIngredient.setCalories(jsonObject.getInt("calories"));
        recipeIngredient.setUnit(jsonObject.getString("unit"));
    }

    @Transactional
    public void parseRecipeIngredients(JsonArray recipeIngredients, Recipe recipe) {
        assert recipeIngredients != null;

        try {
            for (var ingredient : recipeIngredients) {
                var obj = ingredient.asJsonObject();
                var ing = this.ingredientRepository.findByObject(obj);
                if(ing == null) {
                    ing = this.ingredientRepository.add(obj);
                }
                var recipeIngredient = new RecipeIngredient(recipe, ing, obj.getInt("amount"),
                        obj.getString("unit"), obj.getInt("calories"));
                this.getEntityManager().persist(recipeIngredient);
            }
        } catch (NullPointerException ex) {
            throw new NullPointerException("JsonObject has serious errors.");
        }
    }

    @Transactional
    public boolean containsIngredient(Long ingredientId) {
        return this.getEntityManager().createNamedQuery("Recipe_Ingredient.findByIngredientId", RecipeIngredient.class)
                .setParameter("ingredient_id", ingredientId)
                .getResultList().size() > 0;
    }

    @Transactional
    public <T> int getCaloriesForUniqueIdentifier(T identifier) {
        List<RecipeIngredient> ingredients;
        if (identifier.getClass() == String.class) {
            ingredients = this.getEntityManager().createNamedQuery("Recipe_Ingredient.findByRecipeId", RecipeIngredient.class)
                    .setParameter("recipe_shortName", identifier)
                    .setParameter("recipe_id", 0L)
                    .getResultList();
        } else if (identifier.getClass() == Long.class) {
            ingredients = this.getEntityManager().createNamedQuery("Recipe_Ingredient.findByRecipeId", RecipeIngredient.class)
                    .setParameter("recipe_id", identifier)
                    .setParameter("recipe_shortName", "")
                    .getResultList();
        } else {
            throw new IllegalArgumentException("Type of parameter is invalid.");
        }
        if(ingredients.isEmpty()) {
            throw new NullPointerException(String.format("Recipe with identifier %s doesn't exist", identifier));
        }
        var calorieNumbers = new ArrayList<Integer>();
        ingredients.forEach(i -> calorieNumbers.add(i.getCalories()));

        return calorieNumbers.stream()
                .reduce(0, Integer::sum);
    }


    @Transactional
    public String updateShortNameById(RecipeShortNameDto dto) {
        var recipe = findById(dto.getId());
        if (recipe == null) {
            return "Not found";
        }

        recipe.setShortName(dto.getShortName());
        return "Successfully updated";
    }


    @Transactional
    public void clear() {
        this.getEntityManager().createNativeQuery("truncate recipe_ingredient cascade").executeUpdate();
        this.getEntityManager().createNativeQuery("truncate image cascade").executeUpdate();
        this.getEntityManager().createNativeQuery("truncate menu cascade").executeUpdate();
        this.deleteAll();

        this.getEntityManager()
                .createNativeQuery("ALTER TABLE recipe ALTER COLUMN r_id RESTART WITH 1")
                .executeUpdate();
    }
}
