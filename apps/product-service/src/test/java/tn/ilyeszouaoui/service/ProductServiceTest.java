package tn.ilyeszouaoui.service;

import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.ilyeszouaoui.common.RoleUtils;
import tn.ilyeszouaoui.dataobject.ProductDTO;
import tn.ilyeszouaoui.persistence.ProductRepository;
import tn.ilyeszouaoui.persistence.UserRepository;
import tn.ilyeszouaoui.persistence.entity.ProductEntity;
import tn.ilyeszouaoui.persistence.entity.UserEntity;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
public class ProductServiceTest {

    @Inject
    ProductService productService;
    @Inject
    ProductRepository productRepository;
    @Inject
    UserRepository userRepository;

    @BeforeEach
    void init() {
        productRepository.deleteAll();
    }

    @Test
    public void createProductTest() {
        int userId = createUserAndReturnUserId();

        productService.createProduct(userId, "baguette", "food", 12.44);

        ProductEntity productEntity = productRepository.findOneByName("baguette");
        assertThat(productEntity.getUserId()).isEqualTo(userId);
        assertThat(productEntity.getName()).isEqualTo("baguette");
        assertThat(productEntity.getType()).isEqualTo("food");
        assertThat(productEntity.getPrice()).isEqualTo(12.44);

    }


    @Test
    public void updateProductTest() {
        int userId = createUserAndReturnUserId();

        ProductEntity baguetteEntity = new ProductEntity(userId, "baguette", "food", 11.2);
        productRepository.persistAndFlush(baguetteEntity);
        int productId = baguetteEntity.getId();

        productService.updateProduct(userId, productId, "BMW", "car", 112.22);

        ProductEntity productEntity = productRepository.findById(productId);
        assertThat(productEntity.getUserId()).isEqualTo(userId);
        assertThat(productEntity.getName()).isEqualTo("BMW");
        assertThat(productEntity.getType()).isEqualTo("car");
        assertThat(productEntity.getPrice()).isEqualTo(112.22);
    }

    @Test
    public void deleteProductTest() {
        int userId = createUserAndReturnUserId();
        ProductEntity baguetteEntity = new ProductEntity(userId, "baguette", "food", 11.2);
        productRepository.persistAndFlush(baguetteEntity);
        int productId = baguetteEntity.getId();
        productService.deleteProduct(userId, productId);

        ProductEntity productEntity = productRepository.findById(productId);

        assertThat(productEntity).isNull();
    }

    @Test
    public void getProductByIdTest() {
        int userId = createUserAndReturnUserId();
        ProductEntity baguetteEntity = new ProductEntity(userId, "baguette", "food", 11.2);
        productRepository.persistAndFlush(baguetteEntity);
        int productId = baguetteEntity.getId();

        ProductDTO productDTO = productService.findProductById(productId);

        assertThat(productDTO.getName()).isEqualTo("baguette");
        assertThat(productDTO.getType()).isEqualTo("food");
        assertThat(productDTO.getPrice()).isEqualTo(11.2);
    }

    @Test
    public void getProductsByTypeTest() {
        int userId = createUserAndReturnUserId();

        productRepository.persistAndFlush(new ProductEntity(userId, "baguette", "food", 15.2));
        productRepository.persistAndFlush(new ProductEntity(userId, "audi", "car", 1431.23));
        productRepository.persistAndFlush(new ProductEntity(userId, "BMW", "car", 1512.25));
        productRepository.persistAndFlush(new ProductEntity(userId, "apple", "IT", 9999.9));

        List<ProductDTO> productEntityList = productService.findProductsByTypeOrElseFindAll("car");

        assertThat(productEntityList)
                .extracting(ProductDTO::getName, ProductDTO::getType, ProductDTO::getPrice)
                .containsExactly(
                        Tuple.tuple("audi", "car", 1431.23),
                        Tuple.tuple("BMW", "car", 1512.25)
                );
    }

    @Test
    public void getProductsTest() {
        int userId = createUserAndReturnUserId();

        productRepository.persistAndFlush(new ProductEntity(userId, "baguette", "food", 15.2));
        productRepository.persistAndFlush(new ProductEntity(userId, "audi", "car", 1431.23));
        productRepository.persistAndFlush(new ProductEntity(userId, "BMW", "car", 1512.25));

        List<ProductDTO> productEntityList = productService.findProductsByTypeOrElseFindAll(null);

        assertThat(productEntityList)
                .extracting(ProductDTO::getName, ProductDTO::getType, ProductDTO::getPrice)
                .containsExactly(
                        Tuple.tuple("baguette", "food", 15.2),
                        Tuple.tuple("audi", "car", 1431.23),
                        Tuple.tuple("BMW", "car", 1512.25)
                );
    }

    private int createUserAndReturnUserId() {
        UserEntity userEntity = new UserEntity("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "nop", RoleUtils.PRODUCT_OWNER, 26);
        userRepository.persistAndFlush(userEntity);
        return userEntity.getId();
    }
}
