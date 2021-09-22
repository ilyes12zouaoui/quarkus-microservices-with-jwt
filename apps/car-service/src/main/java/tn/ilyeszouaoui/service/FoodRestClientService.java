package tn.ilyeszouaoui.service;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import tn.ilyeszouaoui.adapter.FoodRestClient;
import tn.ilyeszouaoui.dataobject.FoodRestClientDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class FoodRestClientService {

    @Inject
    @RestClient
    FoodRestClient foodRestClient;

    public FoodRestClientDTO findFoodUsingRestClientByName(String name) {
        return foodRestClient.getFoodName(name);
    }

}
