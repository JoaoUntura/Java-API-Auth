package dev.joaountura.auth.user.services;

import dev.joaountura.auth.user.mappers.UserMapper;
import dev.joaountura.auth.user.models.Users;
import dev.joaountura.auth.user.models.UserCreateDTO;
import dev.joaountura.auth.user.models.UserResponseDTO;
import dev.joaountura.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;


    public List<UserResponseDTO> createUser(UserCreateDTO userCreateDTO){
        Users savedUsers = userRepository.save(userMapper.toUser(userCreateDTO));
        List<Users> users = List.of(savedUsers);
        return userMapper.toUserResponse(users);
    }

    public List<UserResponseDTO> findAllUsers(){
        List<Users> users = userRepository.findAll();

        return userMapper.toUserResponse(users);

    }

    public Users findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }


}
