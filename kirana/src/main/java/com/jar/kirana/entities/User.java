package com.jar.kirana.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jar.kirana.dto.UserAddDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    private String id;
    private String username;
    private String password;
    private Role role;

    public User(UserAddDto userAddDto){
        this.username = userAddDto.getUsername();
        this.password = userAddDto.getPassword();
        this.role = Role.USER;
    }
}
