package com.modak.backend.filter;

import com.modak.backend.dto.CustomUserDetails;
import com.modak.backend.provider.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    public LoginFilter(AuthenticationManager authenticationManager, JWTProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);

        return authenticationManager.authenticate(token);
    }
    //요청에서 받을 때 설정할 파라미터 이름
    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }
    //로그인 성공 시 수행할 작업
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        //사용자의 아이디, 비밀번호, 역할을 갖는 CustomUserDetails 가져옴
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = customUserDetails.getUsername();

        //역할은 Collection 으로 저장되어있어 이렇게 가져옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰에 아이디, 역할만 담아서 생성
        String token = jwtProvider.createJwt(userId, role, 60*60*10L);

        //클라이언트 header 에 토큰 등록 (Bearer 토큰값)
        response.addHeader("Authorization", "Bearer " + token);
    }

    //로그인 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
