package com.example.managementapi.Service;

import com.example.managementapi.Dto.Request.Auth.LogOutReq;
import com.example.managementapi.Dto.Request.Auth.LoginReq;
import com.example.managementapi.Dto.Request.Auth.IntrospectRequest;
import com.example.managementapi.Dto.Request.Auth.RefreshReq;
import com.example.managementapi.Dto.Response.Auth.LoginRes;
import com.example.managementapi.Dto.Response.Auth.IntrospectResponse;
import com.example.managementapi.Dto.Response.Auth.RefreshRes;
import com.example.managementapi.Entity.InvalidatedToken;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Enum.ErrorCode;
import com.example.managementapi.Exception.AppException;
import com.example.managementapi.Repository.InvalidatedTokenRepository;
import com.example.managementapi.Repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final UserRepository userRepository;

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${signer.key}")
    protected  String SIGNER_KEY;

    @NonFinal
    @Value("${expiry.date}")
    protected long EXPIRY_DATE;

    @NonFinal
    @Value("${refreshable.duration}")
    protected long REFRESH_DURATION;

    //* =================================== Auth Service =================================== //


    public LoginRes login(LoginReq request) {
        var user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncode = new BCryptPasswordEncoder(10);

        boolean authenticate = passwordEncode.matches(request.getPassword(), user.getPassword());

        if(!authenticate)
            throw  new AppException(ErrorCode.UNAUTHENTICAED);

        var token = generateToken(user);

        return LoginRes.builder()
                .token(token)
                .authenticated(true)
                .build();

    }
    public void logOut(LogOutReq request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();

            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryDate(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        }catch (AppException e){
            log.error("Token already expired");
        }
    }
    public RefreshRes refreshToken(RefreshReq request )
            throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();

        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();


        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryDate(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUserName(username).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_EXISTED));

        var token = generateToken(user);

        return RefreshRes.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    //? Check verify Token
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
            isValid = false;
        }
        catch(AppException e){
            return IntrospectResponse.builder()
                    .valid(isValid)
                    .build();
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    //* =================================== JWT Service =================================== //



    private String generateToken(User user){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName()) // --> dai dien cho user dang nhap
//                .issuer("devteria.com") // --> dinh danh token
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(EXPIRY_DATE, ChronoUnit.SECONDS ).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
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

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getUserRoles()))
            user.getUserRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getRoleName());
                if(!CollectionUtils.isEmpty(role.getPermissions()))
                {
                    role.getPermissions()
                            .forEach(permission -> stringJoiner.add(permission.getPermissionName()));
                }
            });

        return stringJoiner.toString();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet()
                .getExpirationTime()
                .toInstant()
                .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if(!verified && expiryTime.after(new Date()))
            throw  new AppException(ErrorCode.UNAUTHENTICAED);

        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICAED);

        return signedJWT;
    }
}
