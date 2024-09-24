package com.jar.kirana.services;

import com.jar.kirana.dto.UserAddDTO;
import com.jar.kirana.entities.User;
import com.jar.kirana.exceptions.UserAlreadyExistsException;
import com.jar.kirana.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String addUser(UserAddDTO userAddDto){
        Optional<User> userOptional = userRepository.findUserByUsername(userAddDto.getUsername());
        if(userOptional.isPresent()){
            throw new UserAlreadyExistsException(String.format("User %s already exists", userAddDto.getUsername()));
        }
        User user = new User(userAddDto);
        user.setPassword(passwordEncoder.encode(userAddDto.getPassword()));
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
}
