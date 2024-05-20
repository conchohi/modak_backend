package com.modak.backend.filter;

import com.modak.backend.dto.CustomUserDetails;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.provider.JWTProvider;
import com.modak.backend.repository.UserRepository;
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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = parseBearerToken(request);
        //토큰 종료 시간 검증
        if (token == null || jwtProvider.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        //토큰을 바탕으로 아이디, 역할 받아오기
        String userId = jwtProvider.getUsername(token);
        String role = jwtProvider.getRole(token);

        if (userId == null) {
            filterChain.doFilter(request, response);
            return;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        //비밀번호는 아무거나 담기
        userEntity.setPassword("abcd1234");
        userEntity.setRole(role);

        //UserDetail 에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        boolean hasAuthorization = StringUtils.hasText(authorization);
        if (!hasAuthorization) return null;

        boolean isBearer = authorization.startsWith("Bearer ");
        if (!isBearer) return null;

        String token = authorization.split(" ")[1];
        return token;
    }
}