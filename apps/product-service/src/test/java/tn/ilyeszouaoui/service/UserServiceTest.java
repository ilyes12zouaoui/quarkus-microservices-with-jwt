package tn.ilyeszouaoui.service;

import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.ilyeszouaoui.common.RoleUtils;
import tn.ilyeszouaoui.dataobject.UserDTO;
import tn.ilyeszouaoui.persistence.UserRepository;
import tn.ilyeszouaoui.persistence.entity.UserEntity;

import javax.inject.Inject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
public class UserServiceTest {

    @Inject
    UserService userService;
    @Inject
    UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
    }

    @Test
    public void updateUserTest() {
        UserEntity userEntity = new UserEntity("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "nop", RoleUtils.PRODUCT_OWNER, 26);
        userRepository.persistAndFlush(userEntity);
        int entityId = userEntity.getId();

        userService.updateUser(entityId, "newFN", "newLN", 27);

        UserEntity updatedEntity = userRepository.findById(entityId);

        assertThat(updatedEntity.getFirstName()).isEqualTo("newFN");
        assertThat(updatedEntity.getLastName()).isEqualTo("newLN");
        assertThat(updatedEntity.getAge()).isEqualTo(27);
    }

    @Test
    public void deleteUserTest() {
        UserEntity userEntity = new UserEntity("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "nop", RoleUtils.PRODUCT_OWNER, 26);
        userRepository.persistAndFlush(userEntity);
        int entityId = userEntity.getId();

        userService.deleteUser(entityId);

        UserEntity resultEntity = userRepository.findById(entityId);
        assertThat(resultEntity).isNull();
    }

    @Test
    public void getUsersTest() {
        userRepository.persistAndFlush(new UserEntity("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", "nop", RoleUtils.PRODUCT_OWNER, 26));
        userRepository.persistAndFlush(new UserEntity("ilyes2", "zouaoui2", "ilyes.zouaoui.2@test.mail", "nop", RoleUtils.PRODUCT_OWNER, 26));
        userRepository.persistAndFlush(new UserEntity("ilyes3", "zouaoui3", "ilyes.zouaoui.3@test.mail", "nop", RoleUtils.PRODUCT_OWNER, 26));

        List<UserDTO> userEntityList = userService.findUsers();

        assertThat(userEntityList)
                .extracting("firstName","lastName","email","role","age")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("ilyes", "zouaoui", "ilyes.zouaoui@test.mail", RoleUtils.PRODUCT_OWNER, 26),
                        Tuple.tuple("ilyes2", "zouaoui2", "ilyes.zouaoui.2@test.mail", RoleUtils.PRODUCT_OWNER, 26),
                        Tuple.tuple("ilyes3", "zouaoui3", "ilyes.zouaoui.3@test.mail", RoleUtils.PRODUCT_OWNER, 26)
                    );
    }


}
