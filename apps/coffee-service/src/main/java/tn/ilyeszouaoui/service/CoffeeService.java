package tn.ilyeszouaoui.service;

import tn.ilyeszouaoui.dataobject.CoffeeDTO;
import tn.ilyeszouaoui.dataobject.mapper.CoffeeMapper;
import tn.ilyeszouaoui.persistence.CoffeeRepository;
import tn.ilyeszouaoui.persistence.entity.CoffeeEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class CoffeeService {


    @Inject
    CoffeeMapper coffeeMapper;

    @Inject
    CoffeeRepository coffeeRepository;

    public void createCoffee(String name, double price) {
        CoffeeEntity coffeeEntity = new CoffeeEntity(name, price);
        coffeeRepository.persist(coffeeEntity);
    }

    public void updateCoffee(int id, String name, double price) {
        CoffeeEntity coffeeEntity = coffeeRepository.findById(id);
        coffeeEntity.setName(name);
        coffeeEntity.setPrice(price);
        coffeeEntity.persist();
    }

    public void deleteCoffee(int id) {
        coffeeRepository.deleteById(id);
    }


    public List<CoffeeDTO> findCoffees() {
        return coffeeRepository.listAll()
                .stream()
                .map(coffeeEntity -> coffeeMapper.coffeeEntityToCoffeeDTO(coffeeEntity))
                .collect(Collectors.toList());
    }

    public CoffeeDTO findCoffeeById(int id) {
        return coffeeMapper.coffeeEntityToCoffeeDTO(coffeeRepository.findById(id));
    }


}
