package com.jar.kirana.security.config;

import com.jar.kirana.entities.Role;
import com.jar.kirana.entities.User;
import com.jar.kirana.repositories.UserRepository;
import com.jar.kirana.security.CustomLogoutHandler;
import com.jar.kirana.security.CustomUserDetailsService;
import com.jar.kirana.security.JwtTokenFilter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

    private final JwtTokenFilter jwtTokenFilter;
    private final CustomLogoutHandler customLogoutHandler;
    private final UserRepository userRepository;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, CustomLogoutHandler customLogoutHandler, UserRepository userRepository) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.customLogoutHandler = customLogoutHandler;
        this.userRepository = userRepository;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        RequestMatcher[] requestMatchers = new RequestMatcher[]{
                new AntPathRequestMatcher("/api/v1/auth/**"),
        };
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(requestMatchers)
                                .permitAll()
                                .requestMatchers("/api/v1/user/**").hasRole("USER")
                                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                .anyRequest()
                                .authenticated())
                .sessionManagement(
                        sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .logout( l -> l.logoutUrl("/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext())))
                .build();
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CommandLineRunner initData(CustomUserDetailsService userDetailsService) {
        return args -> {
            Optional<User> adminOptional = userRepository.findUserByUsername("admin");
            if(adminOptional.isEmpty()){
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder().encode("password"));
                admin.setRole(Role.ADMIN);

                userDetailsService.createUser(admin);
            }
        };
    }


}