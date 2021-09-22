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
import tn.ilyeszouaoui.dataobject.ProductDTO;
import tn.ilyeszouaoui.persistence.ProductRepository;
import tn.ilyeszouaoui.persistence.UserRepository;
import tn.ilyeszouaoui.persistence.entity.ProductEntity;
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
public class ProductResourceTest {

    static final String userId = "1";
    @Inject
    ProductRepository productRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    EntityManager em;

    @BeforeEach
    @Transactional
    void init() throws SQLException {
        productRepository.deleteAll();
        em.createNativeQuery("TRUNCATE TABLE users CASCADE").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE users_id_sequence RESTART").executeUpdate();
    }

    @Test
    @TestSecurity(user = "ilyes", roles = {RoleUtils.PRODUCT_OWNER})
    @JwtSecurity(claims = {
            @Claim(key = "id", value = userId)
    })
    public void createProductTest() {
        createUserAndReturnUserId();
        given()
                .contentType(ContentType.JSON)
                .body(new ProductDTO("Banana", "product", 13.123))
                .when().post("/api/external/v1/products")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
        ProductEntity createdProductEntity = productRepository.findOneByName("Banana");
        assertThat(createdProductEntity.getUserId()).isEqualTo(Integer.valueOf(userId));
        assertThat(createdProductEntity.getName()).isEqualTo("Banana");
        assertThat(createdProductEntity.getType()).isEqualTo("product");
        assertThat(createdProductEntity.getPrice()).isEqualTo(13.123);
    }

    @Test
    @TestSecurity(user = "ilyes", roles = {RoleUtils.PRODUCT_OWNER})
    @JwtSecurity(claims = {
            @Claim(key = "id", value = userId)
    })
    public void getProductByIdTest() {
        int userId = createUserAndReturnUserId();
        ProductEntity bananaProduct = new ProductEntity(userId, "Banana", "product", 111.33);
        productRepository.persistAndFlush(bananaProduct);

        ProductDTO productDTO = given()
                .contentType(ContentType.JSON)
                .pathParam("id", bananaProduct.getId())
                .when().get("/api/external/v1/products/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(ProductDTO.class);

        assertThat(productDTO.getName()).isEqualTo("Banana");
        assertThat(productDTO.getType()).isEqualTo("product");
        assertThat(productDTO.getPrice()).isEqualTo(111.33);
    }

    @Test
    @TestSecurity(user = "ilyes", roles = {RoleUtils.PRODUCT_OWNER})
    @JwtSecurity(claims = {
            @Claim(key = "id", value = userId)
    })
    public void updateProductTest() {
        int userId = createUserAndReturnUserId();
        ProductEntity bananaProduct = new ProductEntity(userId, "Banana", "product", 111.33);
        productRepository.persistAndFlush(bananaProduct);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", bananaProduct.getId())
                .body(new ProductDTO("Apple", "product", 15.123))
                .when().put("/api/external/v1/products/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        ProductEntity updatedProductEntity = productRepository.findOneByName("Apple");

        assertThat(updatedProductEntity.getName()).isEqualTo("Apple");
        assertThat(updatedProductEntity.getType()).isEqualTo("product");
        assertThat(updatedProductEntity.getPrice()).isEqualTo(15.123);
    }

    @Test
    @TestSecurity(user = "ilyes", roles = {RoleUtils.PRODUCT_OWNER})
    @JwtSecurity(claims = {
            @Claim(key = "id", value = userId)
    })
    public void deleteProductTest() {
        int userId = createUserAndReturnUserId();
        ProductEntity bananaProduct = new ProductEntity(userId, "Banana", "product", 111.33);
        productRepository.persistAndFlush(bananaProduct);

        given()
                .pathParam("id", bananaProduct.getId())
                .when().delete("/api/external/v1/products/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        assertThat(productRepository.findOneByName("Banana")).isNull();
    }

    @Test
    @TestSecurity(user = "ilyes", roles = {RoleUtils.PRODUCT_OWNER})
    @JwtSecurity(claims = {
            @Claim(key = "id", value = userId)
    })
    public void getProductsTest() {
        int userId = createUserAndReturnUserId();

        productRepository.persistAndFlush(new ProductEntity(userId, "Apple", "product", 15.2));
        productRepository.persistAndFlush(new ProductEntity(userId, "Banana", "product", 11.23));
        productRepository.persistAndFlush(new ProductEntity(userId, "BMW", "car", 1512.25));

        List<ProductDTO> productEntityList = given()
                .when().get("/api/external/v1/products")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().jsonPath().getList(".", ProductDTO.class);

        assertThat(productEntityList)
                .extracting(ProductDTO::getName, ProductDTO::getType, ProductDTO::getPrice)
                .containsExactlyInAnyOrder(
                        Tuple.tuple("Banana", "product", 11.23),
                        Tuple.tuple("Apple", "product", 15.2),
                        Tuple.tuple("BMW", "car", 1512.25)
                );
    }

    @Test
    @TestSecurity(user = "ilyes", roles = {RoleUtils.PRODUCT_OWNER})
    @JwtSecurity(claims = {
            @Claim(key = "id", value = userId)
    })
    public void getProductsByTypeTest() {
        int userId = createUserAndReturnUserId();

        productRepository.persistAndFlush(new ProductEntity(userId, "Apple", "product", 15.2));
        productRepository.persistAndFlush(new ProductEntity(userId, "Banana", "product", 11.23));
        productRepository.persistAndFlush(new ProductEntity(userId, "BMW", "car", 1512.25));

        List<ProductDTO> productEntityList = given()
                .queryParam("type", "product")
                .when().get("/api/external/v1/products")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().jsonPath().getList(".", ProductDTO.class);

        assertThat(productEntityList)
                .extracting(ProductDTO::getName, ProductDTO::getType, ProductDTO::getPrice)
                .containsExactlyInAnyOrder(
                        Tuple.tuple("Banana", "product", 11.23),
                        Tuple.tuple("Apple", "product", 15.2)
                );
    }

    private int createUserAndReturnUserId() {
        UserEntity userEntity = new UserEntity("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "nop", RoleUtils.PRODUCT_OWNER, 26);
        userRepository.persistAndFlush(userEntity);
        return userEntity.getId();
    }
}
