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
import tn.ilyeszouaoui.dataobject.UserDTO;
import tn.ilyeszouaoui.dataobject.UserUpdateRequestDTO;
import tn.ilyeszouaoui.persistence.UserRepository;
import tn.ilyeszouaoui.persistence.entity.UserEntity;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class UserResourceTest {

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
    @TestSecurity(user = "ilyes", roles = {RoleUtils.PRODUCT_OWNER})
    @JwtSecurity(claims = {
            @Claim(key = "id", value = userId)
    })
    public void updateUserTest() {
        int userId = createUserAndReturnUserId();

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .body(new UserUpdateRequestDTO("newFN", "newLN", 21))
                .when().put("/api/external/v1/users/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        UserEntity updatedUserEntity = userRepository.findById(userId);

        assertThat(updatedUserEntity.getFirstName()).isEqualTo("newFN");
        assertThat(updatedUserEntity.getLastName()).isEqualTo("newLN");
        assertThat(updatedUserEntity.getAge()).isEqualTo(21);
    }

    @Test
    @TestSecurity(user = "ilyes", roles = {RoleUtils.PRODUCT_OWNER})
    @JwtSecurity(claims = {
            @Claim(key = "id", value = userId)
    })
    public void deleteUserTest() {
        int userId = createUserAndReturnUserId();

        given()
                .pathParam("id", userId)
                .when().delete("/api/external/v1/users/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        assertThat(userRepository.findById(userId)).isNull();
    }

    @Test
    @TestSecurity(user = "ilyes", roles = {RoleUtils.PRODUCT_OWNER})
    @JwtSecurity(claims = {
            @Claim(key = "id", value = userId)
    })
    public void getUsersTest() {
        userRepository.persistAndFlush(new UserEntity("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "nop", RoleUtils.PRODUCT_OWNER, 26));
        userRepository.persistAndFlush(new UserEntity("ilyes2", "zouaoui2", "ilyes.zouaoui.2@test.mail", "nop", RoleUtils.PRODUCT_OWNER, 26));
        userRepository.persistAndFlush(new UserEntity("ilyes3", "zouaoui3", "ilyes.zouaoui.3@test.mail", "nop", RoleUtils.ADMIN, 26));

        List<UserDTO> UserEntityList = given()
                .when().get("/api/external/v1/users")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().jsonPath().getList(".", UserDTO.class);

        assertThat(UserEntityList)
                .extracting("firstName","lastName","email","role","age")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", RoleUtils.PRODUCT_OWNER, 26),
                        Tuple.tuple("ilyes2", "zouaoui2", "ilyes.zouaoui.2@test.mail", RoleUtils.PRODUCT_OWNER, 26),
                        Tuple.tuple("ilyes3", "zouaoui3", "ilyes.zouaoui.3@test.mail", RoleUtils.ADMIN, 26)
                );
    }

    private int createUserAndReturnUserId() {
        UserEntity userEntity = new UserEntity("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "nop", RoleUtils.PRODUCT_OWNER, 26);
        userRepository.persistAndFlush(userEntity);
        return userEntity.getId();
    }
}
