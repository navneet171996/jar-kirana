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
@Document(collection = "userTokens")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserToken {
    @Id
    private String id;
    private String token;
    private Boolean isLoggedOut;
    private String username;
}
