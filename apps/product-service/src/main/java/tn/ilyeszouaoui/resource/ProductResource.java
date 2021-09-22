package tn.ilyeszouaoui.resource;

import org.eclipse.microprofile.jwt.JsonWebToken;
import tn.ilyeszouaoui.common.RoleUtils;
import tn.ilyeszouaoui.dataobject.ProductDTO;
import tn.ilyeszouaoui.service.ProductService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/api/external/v1/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/")
    @RolesAllowed({RoleUtils.ADMIN,RoleUtils.PRODUCT_OWNER})
    public Response createProduct(ProductDTO productDTO) {
        productService.createProduct(
                Integer.parseInt(jwt.getClaim("id").toString()),
                productDTO.getName(),
                productDTO.getType(),
                productDTO.getPrice()
        );
        return Response.status(Status.CREATED).build();
    }

    @GET
    @Path("/")
    @RolesAllowed({RoleUtils.ADMIN,RoleUtils.PRODUCT_OWNER})
    public Response findProducts(@QueryParam("type") String type) {
        return Response
                .status(Status.OK)
                .entity(productService.findProductsByTypeOrElseFindAll(type))
                .build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({RoleUtils.ADMIN,RoleUtils.PRODUCT_OWNER})
    public Response findProductById(@PathParam("id") int id) {
        return Response
                .status(Status.OK)
                .entity(productService.findProductById(id))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({RoleUtils.ADMIN,RoleUtils.PRODUCT_OWNER})
    public Response updateProduct(@PathParam("id") int id, ProductDTO productDTO) {
        productService.updateProduct(
                Integer.parseInt(jwt.getClaim("id").toString()),
                id,
                productDTO.getName(),
                productDTO.getType(),
                productDTO.getPrice()
        );
        return Response.status(Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({RoleUtils.ADMIN,RoleUtils.PRODUCT_OWNER})
    public Response deleteProduct(@PathParam("id") int id) {
        productService.deleteProduct(Integer.parseInt(jwt.getClaim("id").toString()),id);
        return Response.status(Status.NO_CONTENT).build();
    }

}
