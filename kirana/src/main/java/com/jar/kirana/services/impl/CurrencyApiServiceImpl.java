package com.jar.kirana.services.impl;

import com.jar.kirana.dto.CurrencyConverterResponseDTO;
import com.jar.kirana.services.CurrencyApiService;
import com.jar.kirana.services.RedisService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyApiServiceImpl implements CurrencyApiService {

    String CURRENCY_API_URL = "https://api.fxratesapi.com/latest";
    Long expiryForCache = 300l;

    private final RestTemplate restTemplate;
    private final RedisService redisService;

    public CurrencyApiServiceImpl(RestTemplate restTemplate, RedisService redisService) {
        this.restTemplate = restTemplate;
        this.redisService = redisService;
    }

    @Override
    public CurrencyConverterResponseDTO getRecentCurrencyConversion() {
        CurrencyConverterResponseDTO currency = redisService.get("currency", CurrencyConverterResponseDTO.class);
        if(currency != null){
            return currency;
        }
        CurrencyConverterResponseDTO responseDTO = restTemplate.getForObject(CURRENCY_API_URL, CurrencyConverterResponseDTO.class);
        redisService.set("currency", responseDTO, expiryForCache);
        return  responseDTO;
    }
}
