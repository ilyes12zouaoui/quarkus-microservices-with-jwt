package tn.ilyeszouaoui.resource;

import tn.ilyeszouaoui.dataobject.CarDTO;
import tn.ilyeszouaoui.service.CarService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/api/external/v1/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarResource {

    @Inject
    CarService carService;

    @POST
    @Path("/")
    public Response createCar(CarDTO carDTO) {
        carService.createCar(
                carDTO.getName(),
                carDTO.getPrice()
        );
        return Response.status(Status.CREATED).build();
    }

    @GET
    @Path("/")
    public Response findCars() {
        return Response
                .status(Status.OK)
                .entity(carService.findCars())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response findCar(@PathParam("id") int id) {
        return Response
                .status(Status.OK)
                .entity(carService.findCarById(id))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCar(@PathParam("id") int id, CarDTO carDTO) {
        carService.updateCar(
                id,
                carDTO.getName(),
                carDTO.getPrice()
        );
        return Response.status(Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCar(@PathParam("id") int id) {
        carService.deleteCar(id);
        return Response.status(Status.NO_CONTENT).build();
    }

}
