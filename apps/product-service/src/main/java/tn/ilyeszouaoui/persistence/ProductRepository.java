package tn.ilyeszouaoui.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import tn.ilyeszouaoui.persistence.entity.ProductEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class ProductRepository implements PanacheRepositoryBase<ProductEntity, Integer> {

    public ProductEntity findOneByName(String name) {
        return find("name", name).firstResult();
    }

    public List<ProductEntity> findAllByType(String type) {
        return list("type", type);
    }

    public boolean doesUserOwnProduct(int userId, int productId) {
        return count("user_id = :userId AND id = :productId",
                Parameters.with("userId", userId).and("productId", productId).map()
        ) > 0;
    }
}