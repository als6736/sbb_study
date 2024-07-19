package com.mysite.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
//스프링 시큐리티가 로그인시 사용할 서비스
//스프링 시큐리티가 제공하는 UserDetailService 인터페이스를 구현해야한다
public class UserSecurityService implements UserDetailsService {
    //스프링 시큐리티의 UserDetailsService 는 loadUserByUsername 메서드를 구현하도록 강제하는 인터페이스이다.

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //loadUserByUsername 메서드는 사용자명(username)으로 스프링 시큐리티의 사용자(User) 객체를 조회하여 리턴하는 메서드이다.
        Optional<SiteUser> _siteUser = this.userRepository.findByusername(username);
        //사용자 명으로 SiteUser 객체를 조회한다.
        if (_siteUser.isEmpty()) { // 만약 해당하는 데이터가 없을 경우
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
            //사용자를 찾을 수 없습니다 메시지를 출력한다.
        }
        SiteUser siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) { //사용자 명이 admin 인경우 권한 ROLE_ADMIN을 부여한다.
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else { // 아닐경우 권한 ROLE_USER를 부여한다.
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        //User 객체 생성 및 반환 유저의 이름과 비밀번호 권한 리스트가 전달된다.
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
        //스프링 시큐리티는 loadUserByUsername 메서드에 의해 리턴된 User 객체의 비밀번호가
        // 사용자로부터 입력받은 비밀번호와 일치하는지를 검사하는 기능을 내부에 가지고 있다.
    }
}
