package at.htl.kochrezepte.boundary;

import at.htl.kochrezepte.control.IngredientRepository;
import at.htl.kochrezepte.dtos.GetIngredientDto;
import at.htl.kochrezepte.dtos.PostPutIngredientDto;
import at.htl.kochrezepte.entity.Ingredient;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonValue;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Path("api/ingredient")
@Tag(name = "Ingredient")
@RequestScoped
public class IngredientService {
    @Inject
    IngredientRepository ingredientRepository;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<GetIngredientDto> list() {
        return this.ingredientRepository.list();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Ingredient findById(@PathParam("id") Long id) {
        return this.ingredientRepository.find(id);
    }

    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(PostPutIngredientDto[] ingredientDtos) {
        if(ingredientDtos.length == 1) {
            var ingredient = ingredientDtos[0];
            return Response
                    .ok(this.ingredientRepository.create(ingredient))
                    .build();
        }
        else
        {
            Arrays.stream(ingredientDtos)
                    .forEach(i -> this.ingredientRepository.create(i));
            return Response
                    .ok("Successfully added multiple ingredients.")
                    .build();
        }
    }

    @PUT
    @Path("update/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(PostPutIngredientDto ingredient, @PathParam("id") Long id) {
        return Response
                .ok(this.ingredientRepository.update(ingredient, id))
                .build();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("delete/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response
                .ok(this.ingredientRepository.delete(id))
                .build();
    }

    @POST
    @Operation(hidden = true)
    @Path("clear")
    @Consumes()
    @Produces()
    public void clear() {
        this.ingredientRepository.clear();
    }
}
