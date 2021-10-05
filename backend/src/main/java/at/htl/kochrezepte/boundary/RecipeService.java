package at.htl.kochrezepte.boundary;

import at.htl.kochrezepte.control.RecipeRepository;
import at.htl.kochrezepte.dtos.DetailedRecipeDto;
import at.htl.kochrezepte.dtos.RecipeDto;
import at.htl.kochrezepte.dtos.RecipeShortNameDto;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonValue;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("api/recipe")
@Tag(name = "Recipe")
@RequestScoped
public class RecipeService {
    @Inject
    RecipeRepository recipeRepository;

    @Operation(
            summary = "Gets all recipes",
            description = "Gets the recipes from a collection in the repository"
    )
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<RecipeDto> list() {
        return this.recipeRepository.list();
    }

    @GET
    @Operation(
            summary = "Get single recipe by id",
            description = "Get detailed recipe with all ingredients and instructions with given id"
    )
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DetailedRecipeDto findById(@PathParam("id") Long id) {
        return this.recipeRepository.findDto(id);
    }


    @Operation(
            summary = "Gets all recipes by meal type",
            description = "Gets the recipes with the given meal type (STARTER, ENTREE, DESSERT)"
    )
    @GET
    @Path("type/{mealType}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<RecipeDto> findByMealType(@PathParam("mealType") String mealType) {
        return this.recipeRepository.findByMealType(mealType);
    }


    @Operation(
            summary = "Saves a recipe or more recipes",
            description = "Calls method parseJsonObject, which parses input data into a recipe and " +
                    "saves it into a collection"
    )
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(JsonValue jsonValue, @Context UriInfo info) {
        if(jsonValue.getValueType().equals(JsonValue.ValueType.OBJECT)) {
            try {
                return Response
                        .ok(this.recipeRepository.parseJsonObject(jsonValue.asJsonObject()))
                        .build();
            } catch (NullPointerException ex) {
                return Response.status(400).header("reason", ex.getMessage()).build();
            }
        }
        else
        {
            jsonValue.asJsonArray()
                    .forEach(v -> this.recipeRepository.parseJsonObject(v.asJsonObject()));
            return Response
                    .ok("Successfully added multiple recipes.")
                    .build();
        }
    }

    @Operation(
            summary = "Updates a recipe",
            description = "Updates an existing recipe, which is filtered by the id, and replaces it with the given data"
    )
    @PUT
    @Path("update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response update(JsonValue jsonValue, @PathParam("id") Long id) {
        try {
        return Response
                .ok(this.recipeRepository.update(jsonValue.asJsonObject(), id))
                .build();
        } catch (NullPointerException ex) {
            return Response
                    .status(400)
                    .header("reason", ex.getMessage())
                    .build();
        }
    }

    @Operation(
            summary = "Deletes a recipe",
            description = "Checks if the recipe exists via their id. If it is existing, it gets deleted."
    )
    @DELETE
    @Path("delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(@PathParam("id") Long id) {
        return Response
                .ok(this.recipeRepository.delete(id))
                .build();
    }

    @Operation(
            summary = "Get calories for a recipe",
            description = "Calculates the total calories of a recipe, which is specified with the id"
    )
    @GET
    @Path("get-calories-by-id/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCaloriesById(@PathParam("id") Long id) {
        try {
            return Response
                    .ok(this.recipeRepository.getCaloriesForUniqueIdentifier(id) + " kcal")
                    .build();
        } catch (NullPointerException ex) {
            return Response
                    .status(400)
                    .header("reason", ex.getMessage())
                    .build();
        }
    }

    @Operation(
            summary = "Get calories for a recipe",
            description = "Calculates the total calories of a recipe, which is indicated with the short name"
    )
    @GET
    @Path("get-calories")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCaloriesByName(@QueryParam("shortName") String shortName) {
        return Response
                .ok(this.recipeRepository
                        .getCaloriesForUniqueIdentifier(shortName) + " kcal")
                .build();
    }

    @PATCH
    @Operation(
            summary = "Updates shortname of recipe with given id",
            description = "Updates the shortname of the recipe with the given id"
    )
    @Path("update-shortname")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateShortName(RecipeShortNameDto dto) {
        var response = this.recipeRepository.updateShortNameById(dto);
        return response.equals("Not found") ?
                Response
                        .status(404)
                        .build() :
                Response
                        .ok(response)
                        .build();
    }

    @POST
    @Operation(hidden = true)
    @Path("clear")
    @Consumes()
    @Produces()
    public Response clear() {
        this.recipeRepository.clear();
        return Response
                .ok()
                .build();
    }
}
