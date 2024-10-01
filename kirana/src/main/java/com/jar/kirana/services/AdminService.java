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

    /**
     * Takes in a username and a password to store a user.
     * <p>
     * The method checks whether the username already exists. If not then it creates a new user with the given username and password.
     * The password is encoded using BCrypt password encoder and stored in the DB.
     * It returns a String which is the user id of the saved user
     * @param userAddDto: username(string), password(string)
     * @return userId(string)
     * @throws UserAlreadyExistsException if the username already exists
     */
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
