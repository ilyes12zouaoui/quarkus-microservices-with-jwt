package tn.ilyeszouaoui.service;

import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.ilyeszouaoui.common.RoleUtils;
import tn.ilyeszouaoui.dataobject.LoginResponseDTO;
import tn.ilyeszouaoui.dataobject.ProductDTO;
import tn.ilyeszouaoui.persistence.ProductRepository;
import tn.ilyeszouaoui.persistence.UserRepository;
import tn.ilyeszouaoui.persistence.entity.ProductEntity;
import tn.ilyeszouaoui.persistence.entity.UserEntity;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
public class AuthenticationServiceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    AuthenticationService authenticationService;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
    }

    @Test
    public void registerProductOwnerUserTest() {
        authenticationService.registerProductOwnerUser("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "secretPass", 26);

        UserEntity userEntity = userRepository.findOneByEmail("ilyes.zouaoui@test.mail").orElse(null);
        assertThat(userEntity)
                .isEqualToComparingOnlyGivenFields(new UserEntity("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "nop", RoleUtils.PRODUCT_OWNER, 26),
                       "firstName" , "lastName", "email", "role","age");
    }


    @Test
    public void registerAdminUserTest() {
        authenticationService.registerAdminUser("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "secretPass", 26);

        UserEntity userEntity = userRepository.findOneByEmail("ilyes.zouaoui@test.mail").orElse(null);
        assertThat(userEntity)
                .isEqualToComparingOnlyGivenFields(new UserEntity("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "nop", RoleUtils.ADMIN, 26),
                        "firstName" , "lastName", "email", "role","age");
    }

    @Test
    public void loginUserTest() {
        authenticationService.registerProductOwnerUser("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "secretPass", 26);

        LoginResponseDTO loginResponseDTO = authenticationService.login("ilyes.zouaoui@test.mail", "secretPass");
        assertThat(loginResponseDTO.getJwt()).isNotNull().isNotBlank();
    }

}
