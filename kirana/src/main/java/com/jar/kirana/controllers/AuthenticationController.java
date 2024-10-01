package com.jar.kirana.controllers;

import com.jar.kirana.dto.LoginRequestDTO;
import com.jar.kirana.dto.LoginResponseDTO;
import com.jar.kirana.exceptions.AuthenticationFailedException;
import com.jar.kirana.exceptions.UserNotFoundException;
import com.jar.kirana.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        try {
            logger.info("Login attempt for user {}", loginRequestDTO.getUsername());
            return new ResponseEntity<LoginResponseDTO>(authenticationService.authenticate(loginRequestDTO), HttpStatus.OK);
        }catch (UserNotFoundException e){
            logger.warn("Login failed: User {} not found", loginRequestDTO.getUsername());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (AuthenticationFailedException e){
            logger.warn("Login failed: Authentication failed for user {}", loginRequestDTO.getUsername());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("Unexpected error {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
