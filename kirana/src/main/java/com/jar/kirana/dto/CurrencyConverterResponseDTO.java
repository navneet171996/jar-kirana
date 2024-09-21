package com.jar.kirana.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyConverterResponseDTO {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("rates")
    private Map<String, Double> rates;

    @Override
    public String toString() {
        return "CurrencyConverterResponseDTO{" +
                "success=" + success +
                ", rates=" + rates +
                '}';
    }
}
