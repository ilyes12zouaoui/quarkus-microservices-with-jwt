package tn.ilyeszouaoui.service;

import tn.ilyeszouaoui.dataobject.CarDTO;
import tn.ilyeszouaoui.dataobject.mapper.CarMapper;
import tn.ilyeszouaoui.persistence.CarRepository;
import tn.ilyeszouaoui.persistence.entity.CarEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class CarService {

    @Inject
    CarMapper carMapper;

    @Inject
    CarRepository carRepository;

    public void createCar(String name, double price) {
        CarEntity carEntity = new CarEntity(name, price);
        carRepository.persist(carEntity);
    }

    public void updateCar(int id, String name, double price) {
        CarEntity carEntity = carRepository.findById(id);
        carEntity.setName(name);
        carEntity.setPrice(price);
        carEntity.persist();
    }

    public void deleteCar(int id) {
        carRepository.deleteById(id);
    }


    public List<CarDTO> findCars() {
        return carRepository.listAll()
                .stream()
                .map(carEntity -> carMapper.carEntityToCarDTO(carEntity))
                .collect(Collectors.toList());
    }

    public CarDTO findCarById(int id) {
        return carMapper.carEntityToCarDTO(carRepository.findById(id));
    }


}
