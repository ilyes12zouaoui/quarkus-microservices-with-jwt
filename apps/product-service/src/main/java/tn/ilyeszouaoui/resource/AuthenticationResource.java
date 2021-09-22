package tn.ilyeszouaoui.resource;

import org.eclipse.microprofile.jwt.JsonWebToken;
import tn.ilyeszouaoui.common.RoleUtils;
import tn.ilyeszouaoui.dataobject.LoginRequestDTO;
import tn.ilyeszouaoui.dataobject.LoginResponseDTO;
import tn.ilyeszouaoui.dataobject.RegisterUserDTO;
import tn.ilyeszouaoui.service.AuthenticationService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

@Path("/api/external/v1/authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AuthenticationResource {

    @Inject
    AuthenticationService authenticationService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/login")
    public Response loginUser(LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authenticationService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        return Response
                .status(Status.OK)
                .entity(loginResponseDTO)
                .build();
    }

    @POST
    @Path("/register")
    public Response registerShowOwnerUser(RegisterUserDTO registerUserDTO) {
        authenticationService.registerProductOwnerUser(
                registerUserDTO.getFirstName(),
                registerUserDTO.getLastName(),
                registerUserDTO.getEmail(),
                registerUserDTO.getPassword(),
                registerUserDTO.getAge()
        );
        return Response
                .status(Status.CREATED)
                .build();
    }

    @POST
    @Path("/register-admin")
    @RolesAllowed(RoleUtils.ADMIN)
    public Response registerAdminUser(RegisterUserDTO registerUserDTO) {
        authenticationService.registerAdminUser(
                registerUserDTO.getFirstName(),
                registerUserDTO.getLastName(),
                registerUserDTO.getEmail(),
                registerUserDTO.getPassword(),
                registerUserDTO.getAge()
        );
        return Response
                .status(Status.CREATED)
                .build();
    }


    @GET
    @Path("/my-first-name")
    @RolesAllowed({RoleUtils.ADMIN, RoleUtils.PRODUCT_OWNER})
    public Response protectedResource(@Context SecurityContext ctx) {
        return Response
                .status(Status.OK)
                .entity(jwt.getClaim("firstName").toString())
                .build();
    }

}
