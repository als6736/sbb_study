package com.mysite.sbb.apistudy;


import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Member;
import java.util.List;

@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/api/members")
    public List<Question> members() {
        List<Question> questionList = this.questionService.getList();
        return questionList;
    }

    @GetMapping("/api/members/{id}")
    public Question member(@PathVariable Integer id) {
        Question member = this.questionService.getQuestion(id);
        if (member == null) {
            return null;
        } else {
            return member;
        }
    }

    @GetMapping("/api/getmember/{username}")
    public SiteUser siteUser(@PathVariable String username) {
        SiteUser siteUser = this.userService.getUser(username);
        return siteUser;
    }
}
