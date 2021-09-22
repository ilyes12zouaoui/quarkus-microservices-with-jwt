package tn.ilyeszouaoui.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import tn.ilyeszouaoui.persistence.entity.UserEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class UserRepository implements PanacheRepositoryBase<UserEntity, Integer> {
    public Optional<UserEntity> findOneByEmail(String email) {
        return find(
                "email = :email",
                Parameters.with("email", email).map()
        ).firstResultOptional();
    }
}