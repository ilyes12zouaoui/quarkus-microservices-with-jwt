package tn.ilyeszouaoui.service;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import tn.ilyeszouaoui.adapter.FoodRestClient;
import tn.ilyeszouaoui.dataobject.FoodRestClientDTO;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@QuarkusTest
public class FoodRestClientServiceTest {

    @InjectMock
    @RestClient
    FoodRestClient foodRestClient;

    @Inject
    FoodRestClientService foodRestClientService;
    @Test
    public void getFoodByNameTest() {
        when(foodRestClient.getFoodName(any())).thenReturn(new FoodRestClientDTO("food image test"));

        FoodRestClientDTO foodDTO = foodRestClientService.findFoodUsingRestClientByName("food name");

        assertThat(foodDTO.getImage()).isEqualTo("food image test");
    }

}
