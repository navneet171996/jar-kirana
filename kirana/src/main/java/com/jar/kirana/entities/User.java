package com.jar.kirana.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jar.kirana.dto.UserAddDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


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

    public User(UserAddDTO userAddDto){
        this.username = userAddDto.getUsername();
        this.password = userAddDto.getPassword();
        this.role = Role.USER;
    }
}
