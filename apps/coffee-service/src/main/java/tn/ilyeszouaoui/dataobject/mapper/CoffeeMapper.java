package tn.ilyeszouaoui.dataobject.mapper;

import org.mapstruct.Mapper;
import tn.ilyeszouaoui.dataobject.CoffeeDTO;
import tn.ilyeszouaoui.persistence.entity.CoffeeEntity;

@Mapper(componentModel = "cdi")
public interface CoffeeMapper {

    CoffeeDTO coffeeEntityToCoffeeDTO(CoffeeEntity coffeeEntity);
    CoffeeEntity coffeeDTOToCoffeeEntity(CoffeeDTO coffeeDTO);

}
