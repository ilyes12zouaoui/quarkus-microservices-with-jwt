package tn.ilyeszouaoui.service;

import tn.ilyeszouaoui.common.JWTUtils;
import tn.ilyeszouaoui.common.PasswordUtils;
import tn.ilyeszouaoui.common.RoleUtils;
import tn.ilyeszouaoui.dataobject.LoginResponseDTO;
import tn.ilyeszouaoui.persistence.UserRepository;
import tn.ilyeszouaoui.persistence.entity.UserEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class AuthenticationService {

    @Inject
    UserRepository userRepository;

    public void registerProductOwnerUser(String firstName, String lastName, String email, String password, int age) {
        userRepository.persist(new UserEntity(firstName, lastName, email, PasswordUtils.hashPassword(password), RoleUtils.PRODUCT_OWNER, age));
    }

    public void registerAdminUser(String firstName, String lastName, String email, String password, int age) {
        userRepository.persist(new UserEntity(firstName, lastName, email, PasswordUtils.hashPassword(password), RoleUtils.ADMIN, age));
    }

    public LoginResponseDTO login(String email, String password) {
        UserEntity userEntity = userRepository.findOneByEmail(email)
                .orElseThrow(() -> new RuntimeException("wrong email or password!"));

        if (!PasswordUtils.verifyPassword(password, userEntity.getPassword())) {
            throw new RuntimeException("wrong email or password!");
        }

        String jwt = JWTUtils.generateJWT(
                userEntity.getId(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getRole(),
                userEntity.getAge()
        );

        return new LoginResponseDTO(jwt);
    }
}
