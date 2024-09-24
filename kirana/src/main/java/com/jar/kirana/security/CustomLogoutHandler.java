package com.jar.kirana.security;

import com.jar.kirana.entities.UserToken;
import com.jar.kirana.repositories.UserTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

    private final UserTokenRepository userTokenRepository;

    public CustomLogoutHandler(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        String token = authHeader.substring(7);

        Optional<UserToken> tokenOptional = userTokenRepository.findByToken(token);
        if(tokenOptional.isPresent()){
            UserToken userToken = tokenOptional.get();
            userToken.setIsLoggedOut(true);
            userTokenRepository.save(userToken);
        }else {
            logger.error("Token {} not found", token);
        }
    }
}
