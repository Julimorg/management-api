package com.example.managementapi.Configuration.Security;


import lombok.experimental.NonFinal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final String[] PUBLIC_POST_ENDPOINTS = {"/users/create-users", "/auth/log-in"};
    private final String[] PUBLIC_GET_ENPOINTS = {"users/get-user "};
    private final String[] PUBLIC_SWAGGER = {"/swagger-ui/**","/v3/api-docs/**", "/webjars/**"};

    @NonFinal
    protected  static final String SIGNER_KEY = "7aW5J8WZ0ck0TEg+OnxjCfK8dBqTkOjcKEEVM1UWufP3XZNfXTIcF/CBL+DyYJ52";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 1. HttpSecurity se cung cap cac class de config Security
        // 2. Config httpSecurity tu authorizeHttpRequests
        // 3. requestMatchers de xac dinh enpoint
        // 4. secure cac enpoint khac
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENPOINTS).permitAll()
                        .requestMatchers(PUBLIC_SWAGGER).permitAll()
                        .anyRequest().authenticated());

        //? Config OAuth2
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);


        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder(){

        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");

        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
