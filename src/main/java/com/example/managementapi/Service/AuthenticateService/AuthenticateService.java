package com.example.managementapi.Service.AuthenticateService;

import com.example.managementapi.Dto.Request.AuthenticationRequest;
import com.example.managementapi.Dto.Request.IntrospectRequest;
import com.example.managementapi.Dto.Response.AuthenticationResponse;
import com.example.managementapi.Dto.Response.IntrospectResponse;
import com.example.managementapi.Enum.ErrorCode.ErrorCode;
import com.example.managementapi.Exception.AppException;
import com.example.managementapi.Repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticateService {
    private final UserRepository userRepository;

    @NonFinal
    protected  static final String SIGNER_KEY = "7aW5J8WZ0ck0TEg+OnxjCfK8dBqTkOjcKEEVM1UWufP3XZNfXTIcF/CBL+DyYJ52";


    //? Check verify Token
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncode = new BCryptPasswordEncoder(10);

        boolean authenticate = passwordEncode.matches(request.getPassword(), user.getPassword());

        if(!authenticate)
            throw  new AppException(ErrorCode.UNAUTHENTICAED);

        var token = generateToken(request.getUsername());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }


    private String generateToken(String username){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimSet = new JWTClaimsSet.Builder()
                .subject(username) // --> dai dien cho user dang nhap
                .issuer("devteria.com") // --> dinh danh token
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS ).toEpochMilli()
                ))
                .claim("customClaim", "Custom")
                .build();

        Payload payload = new Payload(jwtClaimSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        //? Sign Token voi thuat toan MACSigner
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }

    }
}
