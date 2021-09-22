package tn.ilyeszouaoui.resource;

import tn.ilyeszouaoui.common.RoleUtils;
import tn.ilyeszouaoui.dataobject.UserDTO;
import tn.ilyeszouaoui.service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/api/external/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @GET
    @Path("/")
    @RolesAllowed({RoleUtils.ADMIN, RoleUtils.PRODUCT_OWNER})
    public Response findUsers() {
        return Response
                .status(Status.OK)
                .entity(userService.findUsers())
                .build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({RoleUtils.ADMIN, RoleUtils.PRODUCT_OWNER})
    public Response findUserById(@PathParam("id") int id) {
        return Response
                .status(Status.OK)
                .entity(userService.findUserById(id))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({RoleUtils.ADMIN, RoleUtils.PRODUCT_OWNER})
    public Response updateUser(@PathParam("id") int id, UserDTO userDTO) {
        userService.updateUser(
                id,
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getAge()
        );
        return Response.status(Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({RoleUtils.ADMIN, RoleUtils.PRODUCT_OWNER})
    public Response deleteUser(@PathParam("id") int id) {
        userService.deleteUser(id);
        return Response.status(Status.NO_CONTENT).build();
    }

}
