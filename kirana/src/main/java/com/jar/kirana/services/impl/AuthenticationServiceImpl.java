package com.jar.kirana.services.impl;

import com.jar.kirana.dto.LoginRequestDTO;
import com.jar.kirana.dto.LoginResponseDTO;
import com.jar.kirana.entities.User;
import com.jar.kirana.entities.UserToken;
import com.jar.kirana.repositories.UserRepository;
import com.jar.kirana.repositories.UserTokenRepository;
import com.jar.kirana.security.JwtUtils;
import com.jar.kirana.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final JwtUtils jwtUtils;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, UserTokenRepository userTokenRepository, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<User> userOptional = userRepository.findUserByUsername(loginRequestDTO.getUsername());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            String token = jwtUtils.generateToken(authentication);
            List<UserToken> prevUserTokens = userTokenRepository.findByUsername(user.getUsername());
            prevUserTokens.forEach(prevToken -> {
                prevToken.setIsLoggedOut(true);
            });
            userTokenRepository.saveAll(prevUserTokens);

            UserToken userToken = new UserToken();
            userToken.setToken(token);
            userToken.setIsLoggedOut(false);
            userToken.setUsername(user.getUsername());
            userTokenRepository.save(userToken);

            return new LoginResponseDTO(user.getUsername(), token);
        }else {
            logger.error("Username {} not found", loginRequestDTO.getUsername());
            throw new UsernameNotFoundException(String.format("Username %s not found", loginRequestDTO.getUsername()));
        }
    }
}
