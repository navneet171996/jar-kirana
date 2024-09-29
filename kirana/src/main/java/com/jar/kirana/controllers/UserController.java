package com.jar.kirana.controllers;

import com.jar.kirana.config.rateLimiter.WithRateLimitProtection;
import com.jar.kirana.dto.ReportGetDTO;
import com.jar.kirana.dto.TransactionAddDTO;
import com.jar.kirana.exceptions.CurrencyNotAvailableException;
import com.jar.kirana.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @WithRateLimitProtection
    @PostMapping(path = "/record")
    public ResponseEntity<String> recordTransaction(@RequestBody TransactionAddDTO transactionAddDTO){
        try{
            logger.info("Adding transaction for user {}", transactionAddDTO.getUserId());
            String transactionId = userService.recordTransaction(transactionAddDTO);
            logger.info("Successfully added transaction with id {}", transactionId);
            return new ResponseEntity<>(transactionId, HttpStatus.CREATED);
        } catch (CurrencyNotAvailableException e) {
            logger.error("Currency not available in currency API, reason:{}", e.getMessage());
            return new ResponseEntity<>("Transaction not recorded", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("Unexpected error while recording transaction, reason:{} ",e.getMessage());
            return new ResponseEntity<>("Transaction Not Recorded", HttpStatus.INTERNAL_SERVER_ERROR);
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
