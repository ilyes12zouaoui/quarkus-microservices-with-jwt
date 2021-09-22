package tn.ilyeszouaoui.service;

import tn.ilyeszouaoui.dataobject.UserDTO;
import tn.ilyeszouaoui.dataobject.mapper.UserMapper;
import tn.ilyeszouaoui.persistence.UserRepository;
import tn.ilyeszouaoui.persistence.entity.UserEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class UserService {

    @Inject
    UserMapper userMapper;

    @Inject
    UserRepository userRepository;

    public void updateUser(int id, String firstName, String lastName, int age) {
        UserEntity userEntity = userRepository.findById(id);
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setAge(age);
        userEntity.persist();
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public List<UserDTO> findUsers() {
        return userRepository
                .listAll()
                .stream()
                .map(userEntity -> userMapper.userEntityToUserDTO(userEntity))
                .collect(Collectors.toList());
    }

    public UserDTO findUserById(int id) {
        UserEntity userEntity = userRepository.findById(id);
        return userMapper.userEntityToUserDTO(userEntity);

    }

}
