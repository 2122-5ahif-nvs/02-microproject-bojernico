package at.htl.kochrezepte.boundary;

import at.htl.kochrezepte.control.MenuRepository;
import at.htl.kochrezepte.dtos.GetMenuDto;
import at.htl.kochrezepte.dtos.MenuDto;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonValue;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("api/menu")
@Tag(name = "Menu")
@RequestScoped
public class MenuService {
    @Inject
    MenuRepository menuRepository;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<GetMenuDto> list() {
        return this.menuRepository.list();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public MenuDto findById(@PathParam("id") Long id) {
        var menu = this.menuRepository.find(id);
        return new MenuDto(
                menu.getStarter().getId(),
                menu.getEntree().getId(),
                menu.getDessert().getId(),
                menu.getName());
    }

    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(MenuDto dto) {
        try {
            return Response
                    .ok(this.menuRepository.add(dto))
                    .build();
        } catch (NullPointerException ex) {
            return Response.status(400).header("reason", ex.getMessage()).build();
        }
    }

    @PUT
    @Path("update/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(MenuDto dto, @PathParam("id") Long id) {
        return Response
                .ok(this.menuRepository.update(dto, id))
                .build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        return Response
                .ok(this.menuRepository.deleteMenuById(id))
                .build();
    }

    @POST
    @Operation(hidden = true)
    @Path("clear")
    @Consumes()
    @Produces()
    public void clear() {
        this.menuRepository.clear();
    }
}
