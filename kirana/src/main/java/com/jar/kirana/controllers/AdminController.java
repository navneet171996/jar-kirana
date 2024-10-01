package com.jar.kirana.controllers;

import com.jar.kirana.dto.UserAddDTO;
import com.jar.kirana.exceptions.UserAlreadyExistsException;
import com.jar.kirana.services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Takes in a username and a password to store a user
     * <p>
     * An admin can only add users. Pass in the bearer token of an admin within the Authorization header.
     * The method passes the userAddDto object to adminService.addUser() method
     * @param userAddDto : username(string), password(string)
     * @return string: specifying the id of the newly created user enclosed within a ResponseEntity object
     * @throws UserAlreadyExistsException if the username already exists
     */
    @PostMapping(path = "/addUser")
    public ResponseEntity<String> addUser(@RequestBody UserAddDTO userAddDto){
        try{
            logger.info("Adding user {}", userAddDto.getUsername());
            String userId = adminService.addUser(userAddDto);
            logger.info("Added user {} successfully", userAddDto.getUsername());
            return new ResponseEntity<>(userId, HttpStatus.CREATED);
        }catch (UserAlreadyExistsException e){
            logger.error("Failed to add user: {} ", e.getMessage());
            return new ResponseEntity<>("User already exits", HttpStatus.CONFLICT);
        }catch (Exception e) {
            logger.error("Unexpected error occured {}", e.getMessage());
            return new ResponseEntity<>("An unexpected error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
