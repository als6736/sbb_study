package com.mysite.sbb.answer;

import com.mysite.sbb.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    Long countByAuthor(SiteUser author);

    List<Answer> findTop5ByAuthorOrderByCreateDateDesc(SiteUser user);

}
