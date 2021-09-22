package tn.ilyeszouaoui.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.ilyeszouaoui.common.BasicAuthRoles;
import tn.ilyeszouaoui.dataobject.CoffeeDTO;
import tn.ilyeszouaoui.persistence.CoffeeRepository;
import tn.ilyeszouaoui.persistence.entity.CoffeeEntity;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class CoffeeResourceTest {

    @Inject
    CoffeeRepository coffeeRepository;

    @BeforeEach
    void init() {
        coffeeRepository.deleteAll();
    }
    
    @Test
    @TestSecurity(user = "ilyes", roles = {BasicAuthRoles.BASIC_ADMIN})
    public void getAdminAuthenticated() {
        String response = given()
                .when().get("/api/external/v1/coffee/admin")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().asString();

        assertThat(response).isEqualTo("BASIC_ADMIN was authenticated successfully!!!");

    }

    @Test
    @TestSecurity(user = "ilyes", roles = {BasicAuthRoles.BASIC_USER})
    public void getAdminForbidden() {

        given()
                .when().get("/api/external/v1/coffee/admin")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());

    }

    @Test
    @TestSecurity(user = "ilyes", roles = {BasicAuthRoles.BASIC_USER})
    public void getUserAuthenticated() {

        String response = given()
                .when().get("/api/external/v1/coffee/user")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().asString();

        assertThat(response).isEqualTo("BASIC_USER was authenticated successfully!!!");

    }

    @Test
    @TestSecurity(user = "ilyes", roles = {BasicAuthRoles.BASIC_USER})
    public void getUserForbidden() {

        given()
                .when().get("/api/external/v1/coffee/admin")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());

    }


    @Test
    @TestSecurity(user = "ilyes", roles = {BasicAuthRoles.BASIC_USER})
    public void createCoffeeTest() {
        given()
                .contentType(ContentType.JSON)
                .body(new CoffeeEntity("express", 13.123))
                .when().post("/api/external/v1/coffee")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
        CoffeeEntity createdCoffeeEntity = coffeeRepository.findOneByName("express");
        assertThat(createdCoffeeEntity.getName()).isEqualTo("express");
        assertThat(createdCoffeeEntity.getPrice()).isEqualTo(13.123);

    }

    @Test
    public void createCoffeeUnauthorizedTest() {
        given()
                .contentType(ContentType.JSON)
                .body(new CoffeeEntity("express", 13.123))
                .when().post("/api/external/v1/coffee")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }


    @Test
    @TestSecurity(user = "ilyes", roles = {BasicAuthRoles.BASIC_USER})
    public void updateCoffeeTest() {
        CoffeeEntity cappuccinoCoffee = new CoffeeEntity("express", 111.33);
        coffeeRepository.persistAndFlush(cappuccinoCoffee);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", cappuccinoCoffee.getId())
                .body(new CoffeeDTO("cappuccino", 15.123))
                .when().put("/api/external/v1/coffee/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        CoffeeEntity updatedCoffeeEntity = coffeeRepository.findOneByName("cappuccino");

        assertThat(updatedCoffeeEntity.getName()).isEqualTo("cappuccino");
        assertThat(updatedCoffeeEntity.getPrice()).isEqualTo(15.123);
    }

    @Test
    @TestSecurity(user = "ilyes", roles = {BasicAuthRoles.BASIC_USER})
    public void deleteCoffeeTest() {
        CoffeeEntity cappuccinoCoffee = new CoffeeEntity("express", 111.33);
        coffeeRepository.persistAndFlush(cappuccinoCoffee);

        given()
                .pathParam("id", cappuccinoCoffee.getId())
                .when().delete("/api/external/v1/coffee/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        assertThat(coffeeRepository.findOneByName("express")).isNull();
    }

    @Test
    @TestSecurity(user = "ilyes", roles = {BasicAuthRoles.BASIC_USER})
    public void getCoffeesTest() {

        coffeeRepository.persistAndFlush(new CoffeeEntity("cappuccino", 15.2));
        coffeeRepository.persistAndFlush(new CoffeeEntity("express", 11.23));
        coffeeRepository.persistAndFlush(new CoffeeEntity("filter", 1512.25));

        List<CoffeeDTO> coffeeEntityList = given()
                .when().get("/api/external/v1/coffee")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().jsonPath().getList(".", CoffeeDTO.class);

        assertThat(coffeeEntityList)
                .extracting(CoffeeDTO::getName, CoffeeDTO::getPrice)
                .containsExactlyInAnyOrder(
                        Tuple.tuple("express", 11.23),
                        Tuple.tuple("cappuccino", 15.2),
                        Tuple.tuple("filter", 1512.25)
                );
    }



}
