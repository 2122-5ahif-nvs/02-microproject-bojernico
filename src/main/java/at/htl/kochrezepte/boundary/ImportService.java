package at.htl.kochrezepte.boundary;

import at.htl.kochrezepte.control.ImportController;
import at.htl.kochrezepte.control.RecipeRepository;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/import")
@Tag(name = "Import")
@RequestScoped
public class ImportService {

    @Inject
    RecipeRepository recipeRepository;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes()
    public Response ImportRecipes() {
        recipeRepository.clear();
        try {
        var recipes = ImportController.ReadRecipesFromJson();
            assert recipes != null;
            for (var recipe: recipes ) {
                recipeRepository.add(recipe);
            }
            return Response
                    .ok("Successfully added pre-built recipes")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response
                    .serverError()
                    .build();
        }

    }

}
