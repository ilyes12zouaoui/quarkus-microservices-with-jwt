package tn.ilyeszouaoui.service;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.assertj.core.groups.Tuple;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.ilyeszouaoui.adapter.FoodRestClient;
import tn.ilyeszouaoui.dataobject.CarDTO;
import tn.ilyeszouaoui.dataobject.FoodRestClientDTO;
import tn.ilyeszouaoui.persistence.CarRepository;
import tn.ilyeszouaoui.persistence.entity.CarEntity;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@QuarkusTest
public class CarServiceTest {

    @InjectMock
    @RestClient
    FoodRestClient foodRestClient;

    @Inject
    CarService carService;
    @Inject
    CarRepository carRepository;

    @BeforeEach
    void init() {
        carRepository.deleteAll();
    }

    @Test
    public void getFoodByNameTest() {
        when(foodRestClient.getFoodName(any())).thenReturn(new FoodRestClientDTO("food image test"));

        FoodRestClientDTO foodDTO = carService.findFoodRestClientByName("food name");

        assertThat(foodDTO.getImage()).isEqualTo("food image test");
    }


    @Test
    public void createCarTest() {
        carService.createCar("BMW 1", 123.44);

        CarEntity carEntity = carRepository.findOneByName("BMW 1");
        assertThat(carEntity.getName()).isEqualTo("BMW 1");
        assertThat(carEntity.getPrice()).isEqualTo(123.44);
    }


    @Test
    public void updateCarTest() {
        CarEntity AudiEntity = new CarEntity("Audi", 17.2);
        carRepository.persistAndFlush(AudiEntity);
        int entityId = AudiEntity.getId();

        carService.updateCar(entityId, "BMW", 112.22);

        CarEntity carEntity = carRepository.findById(entityId);

        assertThat(carEntity.getName()).isEqualTo("BMW");
        assertThat(carEntity.getPrice()).isEqualTo(112.22);
    }

    @Test
    public void deleteCarTest() {
        carRepository.persistAndFlush(new CarEntity("BMW", 15.2));
        int AppleCarId = carRepository.findOneByName("BMW").getId();
        carService.deleteCar(AppleCarId);

        CarEntity carEntity = carRepository.findById(AppleCarId);

        assertThat(carEntity).isNull();
    }

    @Test
    public void getCarsTest() {
        carRepository.persistAndFlush(new CarEntity("BMW", 1532.2));
        carRepository.persistAndFlush(new CarEntity("audi", 1123.2));
        carRepository.persistAndFlush(new CarEntity("toyota", 1222.2));
        List<CarDTO> shopEntityList = carService.findCars();

        assertThat(shopEntityList)
                .extracting(CarDTO::getName, CarDTO::getPrice)
                .containsExactly(
                        Tuple.tuple("BMW",  1532.2),
                        Tuple.tuple("audi", 1123.2),
                        Tuple.tuple("toyota", 1222.2)
                );
    }

}
