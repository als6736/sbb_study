package com.mysite.sbb.user;

import com.mysite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private static final String ADMIN_ADDRESS = "whdals6736@naver.com";

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); //비밀번호를 설정할때 비크립트 암호화
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    @Transactional
    public String setTemporaryPW(SiteUser user) {
        // 임시 비밀번호 생성 및 암호화
        String temporaryPassword = createRandomPassword();
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);
        return temporaryPassword;
    }

    @Async
    public void sendEmail(String email, String userName, String tempPW) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(ADMIN_ADDRESS);
        message.setSubject(userName+"님의 임시비밀번호 안내 메일입니다.");
        message.setText("안녕하세요 "+userName+"님의 임시 비밀번호는 [" + tempPW +"] 입니다.");

        mailSender.send(message);
    }

    private String createRandomPassword() {
        int leftLimit = 48; // number '0'
        int rightLimit =  122; // letter 'z'
        int targetStringLenght = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLenght)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    @Transactional
    public void updatePassWord(SiteUser user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
