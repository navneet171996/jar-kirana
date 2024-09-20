package com.jar.kirana.controllers;

import com.jar.kirana.dto.TransactionAddDTO;
import com.jar.kirana.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/record")
    public ResponseEntity<?> recordTransaction(@RequestBody TransactionAddDTO transactionAddDTO){
        try{
            String transactionId = userService.recordTransaction(transactionAddDTO);
            return new ResponseEntity<>(transactionId, HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Transaction Not Recorded", HttpStatus.BAD_GATEWAY);
        }
    }
}
