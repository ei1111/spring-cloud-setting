package com.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.dto.RequestLogin;
import com.userservice.dto.UserDto;
import com.userservice.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;
    private final Environment env;
    private final AuthenticationManager authenticationManager;

    //로그인을 시도할때
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        try {

            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(),
                    RequestLogin.class);

            //인증 처리 시작
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //인증 처리 성공할 경우 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDto = userService.getUserDetailsByEmail(email);

        //yml에 등록된 token.secret
        byte[] secrectKeyBytes = env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8);
        //HMAC-sha-256(무결성과 인증을 위한 해시 기반 메시지 코드)
        //32byte의 키가 필요하다(키의 글자수가 모자르면 에러발생)
        SecretKey secretKey = Keys.hmacShaKeyFor(secrectKeyBytes);

        Instant now = Instant.now();

        //토큰 생성
        String token = Jwts.builder()
                .setSubject(userDto.getUserId()) //UUID
                .expiration(Date.from(now.plusMillis(Long.parseLong(env.getProperty("token.expiration-time")))))
                //발행날짜
                .issuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();

        response.addHeader("Authorization",token);
        response.addHeader("userId", userDto.getUserId());
    }
}
