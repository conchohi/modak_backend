package com.modak.backend.config;

import com.modak.backend.filter.CustomLogoutFilter;
import com.modak.backend.filter.JwtAuthenticationFilter;
import com.modak.backend.filter.LoginFilter;
import com.modak.backend.handler.CustomSuccessHandler;
import com.modak.backend.provider.JWTProvider;
import com.modak.backend.service.impl.CustomOAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Collections;

@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTProvider jwtProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomLogoutFilter customLogoutFilter;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
//        loginFilter.setFilterProcessesUrl(""); 지정 시 post 요청 url 변경 가능

        http
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource()))
                //formLogin 방식은 disable 해서 usernamePasswordAuthenticationFilter 직접 등록
                .formLogin(AbstractHttpConfigurer::disable)

                .csrf(CsrfConfigurer::disable)

                .httpBasic(HttpBasicConfigurer::disable)

                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .oauth2Login(oauth2 -> {
                    oauth2.userInfoEndpoint(userInfoEndpointConfig -> {
                                userInfoEndpointConfig.userService(customOAuth2UserService);
                            })
                            .successHandler(customSuccessHandler);
                })

                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/login", "/reissue","/api/v1/auth/**", "/api/camp/**", "/api/weather/**", "/api/notice/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/review/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/user/**").permitAll()
                        .requestMatchers("/api/v1/user/**").hasRole("USER")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint()))

                //로그인 필터 이전에 jwt 인증 필터 등록
                .addFilterBefore(jwtAuthenticationFilter, LoginFilter.class)
                .addFilterBefore(customLogoutFilter, LogoutFilter.class)

                //로그인 필터를 usernamePasswordAuthenticationFilter 로 등록
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setMaxAge(3600L);

        corsConfiguration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
        corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
        corsConfiguration.setExposedHeaders(Collections.singletonList("access"));


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

}

class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("{\"code\" : \"NP\", \"message\" : \"No Permission\"}");
        // {"code" : "NP", "message" : "No Permission"}
    }

}
