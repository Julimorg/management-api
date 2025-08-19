package com.example.managementapi.Configuration;

import com.example.managementapi.Dto.Request.IntrospectRequest;
import com.example.managementapi.Service.AuthenticateService;
import com.nimbusds.jose.JOSEException;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @NonFinal
    protected  static final String SIGNER_KEY = "7aW5J8WZ0ck0TEg+OnxjCfK8dBqTkOjcKEEVM1UWufP3XZNfXTIcF/CBL+DyYJ52";

    @Autowired
    private AuthenticateService authenticateService;

    private NimbusJwtDecoder nimbusJwtDecoder  = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = authenticateService.introspect(IntrospectRequest.builder()
                    .token(token)
                    .build());

            if (!response.isValid())
                throw new JwtException("Token invalid");
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
