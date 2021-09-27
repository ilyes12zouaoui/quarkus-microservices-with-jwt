package tn.ilyeszouaoui.resource;

import tn.ilyeszouaoui.service.CarService;
import tn.ilyeszouaoui.service.FoodRestClientService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/api/external/v1/food-using-rest-client")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FoodRestClientResource {

    @Inject
    FoodRestClientService foodRestClientService;

    @GET
    @Path("/{name}")
    public Response findFoodCars(@PathParam("name") String name) {
        return Response
                .status(Status.OK)
                .entity(foodRestClientService.findFoodUsingRestClientByName(name))
                .build();
    }

}
