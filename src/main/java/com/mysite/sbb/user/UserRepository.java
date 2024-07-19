package com.mysite.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByusername(String username); //회원가입을 통해 DB 에 저장된 유저이름을 받아온다.
}
