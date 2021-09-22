package tn.ilyeszouaoui.dataobject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import tn.ilyeszouaoui.dataobject.ProductDTO;
import tn.ilyeszouaoui.persistence.entity.ProductEntity;

@Mapper(componentModel = "cdi",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductDTO productEntityToProductDTO(ProductEntity productEntity);
    ProductEntity productDTOToProductEntity(ProductDTO productDTO);

}
