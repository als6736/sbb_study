package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration //스프링 환경설정 파일 선언
@EnableWebSecurity //모든 URL요청이 시큐리티의 제어를 받게 만듦 시큐리티 활성화
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()) //인증되지 않은 모든 요청을 허락한다.
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"),
                                //h2-console, "/h2-console/**"로 시작하는 모든 URL은 csrf 검증을 하지 않는다.
                                new AntPathRequestMatcher("/upload"),
                                new AntPathRequestMatcher("/study/api"),
                                new AntPathRequestMatcher("/findname")))
                .headers((headers) -> headers //h2-console은 스프링 프레임 워크가아니다,
                        // 스프링 시큐리티는 웹 사이트의 콘텐츠가 다른 사이트에 포함 되지 않도록 하기 위해
                        // X-Frame-Options 헤더의 기본값을 DENY로 사용하는데 프레임 구조의 웹사이트는 이 헤더의 값이 DENY인 경우 오류가 발생한다.
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                //문제를 해결하기 위해 X-Frame-Options을 DENY 대신 SAMEORIGIN으로 설정
                .formLogin((formLogin) -> formLogin // .formLogin메서드는 시큐리티에서 로그인을 담당한다.
                        .loginPage("/user/login") // 로그인 페이지의 URL설정
                        .defaultSuccessUrl("/")) // 로그인 성공시 이동할 페이지 설정
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
        //.invalidateHttpSession(true)를 통해 로그아웃 시 생성된 사용자 세션도 삭제하도록 처리
        ;
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { //해당 클래스는 비크립트 해시 함수를 사용한다,
        // 비크립트는 해시 함수의 하나로 주로 비밀번호 같은 보안 정보를 안전하게 저장하고 검증할 때 사용하는 암호화 기술이다.
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws  Exception{
        return authenticationConfiguration.getAuthenticationManager();
        //AuthenticationManager는 사용자 인증 시 앞에서 작성한 UserSecurityService 에서 불러온 비밀번호를
        // PasswordEncoder를 통해 다시 비크립트 해시 함수를 사용해 암호화 하고 같은지 확인하여
        //내부적으로 인증과 권한 부여 프로세스를 처리한다.
    }
}
