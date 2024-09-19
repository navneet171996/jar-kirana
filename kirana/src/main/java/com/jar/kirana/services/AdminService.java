package com.jar.kirana.services;

import com.jar.kirana.dto.UserAddDto;
import com.jar.kirana.entities.User;
import com.jar.kirana.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String addUser(UserAddDto userAddDto){
        Optional<User> userOptional = userRepository.findUserByUsername(userAddDto.getUsername());
        if(userOptional.isPresent()){
            throw new RuntimeException(String.format("User %s already exists", userOptional.get().getUsername()));
        }
        User user = new User(userAddDto);
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
}
