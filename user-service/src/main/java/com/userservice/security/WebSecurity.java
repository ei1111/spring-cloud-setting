package com.userservice.security;

import com.userservice.jpa.UserRepository;
import com.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {
    private final UserService userService;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;

    public static final String ALLOWED_IP_ADDRESS = "127.0.0.1";
    public static final String SUBNET = "/32";
    public static final IpAddressMatcher ALLOWED_IP_ADDRESS_MATCHER = new IpAddressMatcher(ALLOWED_IP_ADDRESS + SUBNET);


    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
                AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder( passwordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.csrf( (csrf) -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/swagger-ui/**", "/catalog-service/**").permitAll()  // 특정 경로 허용
                        .requestMatchers("/**").access(
                                new WebExpressionAuthorizationManager(
                                        " hasIpAddress('127.0.0.1') or hasIpAddress('::1') or " +
                                        " hasIpAddress('172.30.1.53') or hasIpAddress('::1')")) //host pc ip address 호출 허용, 이 ip로 들어오는 것만 허용
                        .anyRequest().authenticated()              // 그 외는 인증 필요
                )
                // 사용자가 user서비스를 사용에 따라 필요한 인증처리를 authenticationManager를 사용하겠다
                .authenticationManager(authenticationManager)
                .addFilter(getAuthenticationFilter(authenticationManager))
                .httpBasic(Customizer.withDefaults())  // ← Basic 인증 추가
                .headers((headers) -> headers
                        .frameOptions((frameOptions) -> frameOptions.sameOrigin()));

        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, env, authenticationManager);
        authenticationFilter.setAuthenticationManager(authenticationManager);

        return authenticationFilter;
    }
}
