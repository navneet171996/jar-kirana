package com.jar.kirana.controllers;

import com.jar.kirana.dto.UserAddDto;
import com.jar.kirana.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(path = "/addUser")
    public ResponseEntity<String> addUser(@RequestBody UserAddDto userAddDto){
        try{
            String userId = adminService.addUser(userAddDto);
            return new ResponseEntity<>(userId, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("User already exits", HttpStatus.CONFLICT);
        }
    }
}
