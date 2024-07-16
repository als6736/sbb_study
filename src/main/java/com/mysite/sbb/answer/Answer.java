package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    @ManyToMany
    @JoinTable(
            name = "answer_voters", // 관계 테이블 이름 지정
            joinColumns = @JoinColumn(name = "answer_id"),// 현재 엔티티의 연결 컬럼
            inverseJoinColumns = @JoinColumn(name = "user_id") // 연결된 엔티티의 컬럼
    )
    Set<SiteUser> voter;
}
