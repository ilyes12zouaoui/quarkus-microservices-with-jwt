package tn.ilyeszouaoui.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;
import io.restassured.http.ContentType;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.ilyeszouaoui.common.RoleUtils;
import tn.ilyeszouaoui.dataobject.*;
import tn.ilyeszouaoui.persistence.UserRepository;
import tn.ilyeszouaoui.persistence.entity.ProductEntity;
import tn.ilyeszouaoui.persistence.entity.UserEntity;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class AuthenticationResourceTest {

    static final String userId = "1";
    @Inject
    UserRepository userRepository;

    @Inject
    EntityManager em;

    @BeforeEach
    @Transactional
    void init() throws SQLException {
        em.createNativeQuery("TRUNCATE TABLE users CASCADE").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE users_id_sequence RESTART").executeUpdate();
    }

    @Test
    public void registerProductOwnerTest() {
        given()
                .contentType(ContentType.JSON)
                .body(new RegisterUserDTO("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "myPassword", 26))
                .when().post("/api/external/v1/authentication/register")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
        Optional<UserEntity> registeredUser = userRepository.findOneByEmail("ilyes.zouaoui@test.mail");
        assertThat(registeredUser).isNotEmpty();

        assertThat(registeredUser.get().getEmail()).isEqualTo("ilyes.zouaoui@test.mail");
        assertThat(registeredUser.get().getFirstName()).isEqualTo("ilyes");
        assertThat(registeredUser.get().getLastName()).isEqualTo("zouaoui");
        assertThat(registeredUser.get().getAge()).isEqualTo(26);
        assertThat(registeredUser.get().getRole()).isEqualTo(RoleUtils.PRODUCT_OWNER);
    }

    @Test
    @TestSecurity(user = "ilyes", roles = {RoleUtils.ADMIN})
    public void registerAdminTest() {
        given()
                .contentType(ContentType.JSON)
                .body(new RegisterUserDTO("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "myPassword", 26))
                .when().post("/api/external/v1/authentication/register-admin")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
        Optional<UserEntity> registeredUser = userRepository.findOneByEmail("ilyes.zouaoui@test.mail");
        assertThat(registeredUser).isNotEmpty();

        assertThat(registeredUser.get().getEmail()).isEqualTo("ilyes.zouaoui@test.mail");
        assertThat(registeredUser.get().getFirstName()).isEqualTo("ilyes");
        assertThat(registeredUser.get().getLastName()).isEqualTo("zouaoui");
        assertThat(registeredUser.get().getAge()).isEqualTo(26);
        assertThat(registeredUser.get().getRole()).isEqualTo(RoleUtils.ADMIN);
    }

    @Test
    public void loginTest() {

        given()
                .contentType(ContentType.JSON)
                .body(new RegisterUserDTO("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "myPassword", 26))
                .when().post("/api/external/v1/authentication/register")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

        LoginResponseDTO loginResponseDTO = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequestDTO("ilyes.zouaoui@test.mail", "myPassword"))
                .when().get("/api/external/v1/authentication/login")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(LoginResponseDTO.class);

        assertThat(loginResponseDTO.getJwt()).isNotNull().isNotBlank();
    }

}
