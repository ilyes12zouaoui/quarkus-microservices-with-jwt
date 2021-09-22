package tn.ilyeszouaoui.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import tn.ilyeszouaoui.persistence.entity.CoffeeEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CoffeeRepository implements PanacheRepositoryBase<CoffeeEntity, Integer> {

    public CoffeeEntity findOneByName(String name){
        return find("name", name).firstResult();
    }

}