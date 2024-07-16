package com.mysite.sbb.question;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.file3.FileEntity;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer view_count = 0;

    @ManyToMany
    @JoinTable(
            name = "question_voters", // 관계 테이블 이름 지정
            joinColumns = @JoinColumn(name = "question_id"),// 현재 엔티티의 연결 컬럼
            inverseJoinColumns = @JoinColumn(name = "user_id") // 연결된 엔티티의 컬럼
    )
    Set<SiteUser> voter;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FileEntity> files;

    public void incrementViewCount() {
        this.view_count +=1;
    }
}