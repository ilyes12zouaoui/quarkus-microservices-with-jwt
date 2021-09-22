package tn.ilyeszouaoui.service;

import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.ilyeszouaoui.dataobject.CoffeeDTO;
import tn.ilyeszouaoui.persistence.CoffeeRepository;
import tn.ilyeszouaoui.persistence.entity.CoffeeEntity;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
public class CoffeeServiceTest {

    @Inject
    CoffeeService coffeeService;
    @Inject
    CoffeeRepository coffeeRepository;

    @BeforeEach
    void init() {
        coffeeRepository.deleteAll();
    }


    @Test
    public void createCoffeeTest() {
        coffeeService.createCoffee("express", 123.44);

        CoffeeEntity coffeeEntity = coffeeRepository.findOneByName("express");
        assertThat(coffeeEntity.getName()).isEqualTo("express");
        assertThat(coffeeEntity.getPrice()).isEqualTo(123.44);
    }


    @Test
    public void updateCoffeeTest() {
        CoffeeEntity cappuccinoEntity = new CoffeeEntity("express", 11.2);
        coffeeRepository.persistAndFlush(cappuccinoEntity);
        int entityId = cappuccinoEntity.getId();

        coffeeService.updateCoffee(entityId, "cappuccino", 112.22);

        CoffeeEntity coffeeEntity = coffeeRepository.findById(entityId);

        assertThat(coffeeEntity.getName()).isEqualTo("cappuccino");
        assertThat(coffeeEntity.getPrice()).isEqualTo(112.22);
    }

    @Test
    public void deleteCoffeeTest() {
        coffeeRepository.persistAndFlush(new CoffeeEntity("express", 15.2));
        int AppleCoffeeId = coffeeRepository.findOneByName("express").getId();
        coffeeService.deleteCoffee(AppleCoffeeId);

        CoffeeEntity coffeeEntity = coffeeRepository.findById(AppleCoffeeId);

        assertThat(coffeeEntity).isNull();
    }


    @Test
    public void getCoffeeTest() {
        coffeeRepository.persistAndFlush(new CoffeeEntity("express", 12.2));
        coffeeRepository.persistAndFlush(new CoffeeEntity("cappuccino", 13.2));
        coffeeRepository.persistAndFlush(new CoffeeEntity("filter", 11.1));
        List<CoffeeDTO> shopEntityList = coffeeService.findCoffees();

        assertThat(shopEntityList)
                .extracting(CoffeeDTO::getName, CoffeeDTO::getPrice)
                .containsExactly(
                        Tuple.tuple("express",  12.2),
                        Tuple.tuple("cappuccino", 13.2),
                        Tuple.tuple("filter", 11.1)
                );
    }
}
