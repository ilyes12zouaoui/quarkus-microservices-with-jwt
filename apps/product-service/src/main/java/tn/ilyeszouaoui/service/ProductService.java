package tn.ilyeszouaoui.service;

import tn.ilyeszouaoui.dataobject.ProductDTO;
import tn.ilyeszouaoui.dataobject.mapper.ProductMapper;
import tn.ilyeszouaoui.persistence.ProductRepository;
import tn.ilyeszouaoui.persistence.entity.ProductEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class ProductService {

    @Inject
    ProductMapper productMapper;

    @Inject
    ProductRepository foodRepository;

    public void createProduct(int userId, String name, String type, double price) {
        ProductEntity productEntity = new ProductEntity(userId, name, type, price);
        foodRepository.persist(productEntity);
    }

    public void updateProduct(int userId, int productId, String name, String type, double price) {
        if (!foodRepository.doesUserOwnProduct(userId, productId)) {
            throw new RuntimeException("forbidden user does not own the product!");
        }
        ProductEntity productEntity = foodRepository.findById(productId);
        productEntity.setName(name);
        productEntity.setType(type);
        productEntity.setPrice(price);
        productEntity.persist();
    }

    public void deleteProduct(int userId, int productId) {
        if (!foodRepository.doesUserOwnProduct(userId, productId)) {
            throw new RuntimeException("forbidden user does not own the product!");
        }
        foodRepository.deleteById(productId);
    }

    public List<ProductDTO> findProducts() {
        return foodRepository
                .listAll()
                .stream()
                .map(productEntity -> productMapper.productEntityToProductDTO(productEntity))
                .collect(Collectors.toList());
    }

    public List<ProductDTO> findProductsByTypeOrElseFindAll(String type) {
        List<ProductEntity> productEntityList = type == null ? foodRepository.listAll() : foodRepository.findAllByType(type);
        return productEntityList
                .stream()
                .map(productEntity -> productMapper.productEntityToProductDTO(productEntity))
                .collect(Collectors.toList());
    }

    public ProductDTO findProductById(int id) {
        return productMapper.productEntityToProductDTO(foodRepository.findById(id));
    }

}
