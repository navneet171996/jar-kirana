package com.jar.kirana.controllers;

import com.jar.kirana.dto.ReportGetDTO;
import com.jar.kirana.dto.TransactionAddDTO;
import com.jar.kirana.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/record")
    public ResponseEntity<String> recordTransaction(@RequestBody TransactionAddDTO transactionAddDTO){
        try{
            String transactionId = userService.recordTransaction(transactionAddDTO);
            return new ResponseEntity<>(transactionId, HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Transaction Not Recorded", HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping(path = "/daily-report/{userId}")
    public ResponseEntity<ReportGetDTO> getDailyReport(@PathVariable String userId){
        return new ResponseEntity<>(userService.getDailyReport(userId), HttpStatus.OK);
    }

    @GetMapping(path = "/weekly-report/{userId}")
    public ResponseEntity<ReportGetDTO> getWeeklyReport(@PathVariable String userId){
        return new ResponseEntity<>(userService.getWeeklyReport(userId), HttpStatus.OK);
    }

    @GetMapping(path = "/monthly-report/{userId}")
    public ResponseEntity<ReportGetDTO> getMonthlyReport(@PathVariable String userId){
        return new ResponseEntity<>(userService.getMonthlyReport(userId), HttpStatus.OK);
    }

    @GetMapping(path = "/yearly-report/{userId}")
    public ResponseEntity<ReportGetDTO> getYearlyReport(@PathVariable String userId){
        return new ResponseEntity<>(userService.getYearlyReport(userId), HttpStatus.OK);
    }
}
