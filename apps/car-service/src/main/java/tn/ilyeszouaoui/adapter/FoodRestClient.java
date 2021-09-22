package tn.ilyeszouaoui.adapter;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import tn.ilyeszouaoui.dataobject.FoodRestClientDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey="food-api")
public interface FoodRestClient {

    @GET
    @Path("/images/{name}")
    // food name value example "biryani"
    FoodRestClientDTO getFoodName(@PathParam String name);
}
