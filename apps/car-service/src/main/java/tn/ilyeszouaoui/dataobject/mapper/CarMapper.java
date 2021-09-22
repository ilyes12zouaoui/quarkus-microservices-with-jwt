package tn.ilyeszouaoui.dataobject.mapper;

import org.mapstruct.Mapper;
import tn.ilyeszouaoui.dataobject.CarDTO;
import tn.ilyeszouaoui.persistence.entity.CarEntity;

@Mapper(componentModel = "cdi")
public interface CarMapper {

    CarDTO carEntityToCarDTO(CarEntity carEntity);
    CarEntity carDTOToCarEntity(CarDTO carDTO);

}
