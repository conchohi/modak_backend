package com.modak.backend.filter;

import com.modak.backend.dto.UserDto;
import com.modak.backend.dto.jwt.CustomUserDetails;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.provider.JWTProvider;
import com.modak.backend.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtProvider.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            //401 에러 코드, access token expired 왔을 때 에러 처리 해야 함
            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtProvider.getCategory(accessToken);

        // request header에서 access로 온 토큰이 진짜 access 인지 확인
        if (!category.equals("access")) {
            //401 에러 코드, invalid access token 왔을 때 에러 처리 해야 함
            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // username, role 값을 획득
        String username = jwtProvider.getUsername(accessToken);
        String role = jwtProvider.getRole(accessToken);
        String nickname = jwtProvider.getNickname(accessToken);

        UserDto userDto = UserDto.builder()
                .username(username)
                .role(role)
                .nickname(nickname)
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(userDto);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}