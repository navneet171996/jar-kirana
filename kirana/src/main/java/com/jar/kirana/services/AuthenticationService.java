package com.jar.kirana.services;

import com.jar.kirana.dto.LoginRequestDTO;
import com.jar.kirana.dto.LoginResponseDTO;

public interface AuthenticationService {
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO);
}
