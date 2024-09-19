package com.jar.kirana.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "currencies")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Currency {

    @Id
    private String id;
    private String currencyName;
    private Double conversionRate;
}