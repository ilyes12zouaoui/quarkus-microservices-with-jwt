package tn.ilyeszouaoui.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.ilyeszouaoui.adapter.FoodRestClient;
import tn.ilyeszouaoui.dataobject.CarDTO;
import tn.ilyeszouaoui.persistence.CarRepository;
import tn.ilyeszouaoui.persistence.entity.CarEntity;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class CarResourceTest {

    @Inject
    CarRepository carRepository;

    @InjectMock
    FoodRestClient foodRestClient;

    @BeforeEach
    void init() {
        carRepository.deleteAll();
    }

    @Test
    public void createCarTest() {
        given()
                .contentType(ContentType.JSON)
                .body(new CarDTO("BMW", 13.123))
                .when().post("/api/external/v1/cars")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
        CarEntity createdCarEntity = carRepository.findOneByName("BMW");
        assertThat(createdCarEntity.getName()).isEqualTo("BMW");
        assertThat(createdCarEntity.getPrice()).isEqualTo(13.123);

    }

    @Test
    public void updateCarTest() {
        CarEntity audiCar = new CarEntity("Audi", 111.33);
        carRepository.persistAndFlush(audiCar);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", audiCar.getId())
                .body(new CarDTO("Toyota", 15.123))
                .when().put("/api/external/v1/cars/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        CarEntity updatedCarEntity = carRepository.findById(audiCar.getId());

        assertThat(updatedCarEntity.getName()).isEqualTo("Toyota");
        assertThat(updatedCarEntity.getPrice()).isEqualTo(15.123);
    }

    @Test
    public void deleteCarTest() {
        CarEntity audiCar = new CarEntity("Audi", 111.33);
        carRepository.persistAndFlush(audiCar);

        given()
                .pathParam("id", audiCar.getId())
                .when().delete("/api/external/v1/cars/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        assertThat(carRepository.findOneByName("Audi")).isNull();
    }

    @Test
    public void getCarsTest() {

        carRepository.persistAndFlush(new CarEntity("Toyota", 15.2));
        carRepository.persistAndFlush(new CarEntity("Audi", 11.23));
        carRepository.persistAndFlush(new CarEntity("BMW", 1512.25));

        List<CarDTO> carEntityList = given()
                .when().get("/api/external/v1/cars")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().jsonPath().getList(".", CarDTO.class);

        assertThat(carEntityList)
                .extracting(CarDTO::getName, CarDTO::getPrice)
                .containsExactlyInAnyOrder(
                        Tuple.tuple("Audi", 11.23),
                        Tuple.tuple("Toyota", 15.2),
                        Tuple.tuple("BMW", 1512.25)
                );
    }

}
