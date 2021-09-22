package tn.ilyeszouaoui.resource;

import tn.ilyeszouaoui.common.BasicAuthRoles;
import tn.ilyeszouaoui.dataobject.CoffeeDTO;
import tn.ilyeszouaoui.service.CoffeeService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/api/external/v1/coffee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoffeeResource {

    @Inject
    CoffeeService coffeeService;

    @GET
    @Path("/admin")
    @RolesAllowed(BasicAuthRoles.BASIC_ADMIN)
    public Response basicAuthAdmin() {
        return Response
                .status(Status.OK)
                .entity("BASIC_ADMIN was authenticated successfully!!!")
                .build();
    }

    @GET
    @Path("/user")
    @RolesAllowed(BasicAuthRoles.BASIC_USER)
    public Response basicAuthUser() {
        return Response
                .status(Status.OK)
                .entity("BASIC_USER was authenticated successfully!!!")
                .build();
    }


    @POST
    @Path("/")
    @RolesAllowed({BasicAuthRoles.BASIC_USER, BasicAuthRoles.BASIC_ADMIN})
    public Response createCoffee(CoffeeDTO coffeeDTO) {
        coffeeService.createCoffee(
                coffeeDTO.getName(),
                coffeeDTO.getPrice()
        );
        return Response.status(Status.CREATED).build();
    }

    @GET
    @Path("/")
    @RolesAllowed({BasicAuthRoles.BASIC_USER, BasicAuthRoles.BASIC_ADMIN})
    public Response findCoffees() {
        return Response
                .status(Status.OK)
                .entity(coffeeService.findCoffees())
                .build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({BasicAuthRoles.BASIC_USER, BasicAuthRoles.BASIC_ADMIN})
    public Response findCoffee(@PathParam("id") int id) {
        return Response
                .status(Status.OK)
                .entity(coffeeService.findCoffeeById(id))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({BasicAuthRoles.BASIC_USER, BasicAuthRoles.BASIC_ADMIN})
    public Response updateCoffee(@PathParam("id") int id, CoffeeDTO coffeeDTO) {
        coffeeService.updateCoffee(
                id,
                coffeeDTO.getName(),
                coffeeDTO.getPrice()
        );
        return Response.status(Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({BasicAuthRoles.BASIC_USER, BasicAuthRoles.BASIC_ADMIN})
    public Response deleteCoffee(@PathParam("id") int id) {
        coffeeService.deleteCoffee(id);
        return Response.status(Status.NO_CONTENT).build();
    }

}
