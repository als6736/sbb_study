package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.file3.FileService;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final FileService fileService;

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); //중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), //제목
                        cb.like(q.get("content"),"%" + kw + "%"), //내용
                        cb.like(u1.get("username"),"%" + kw + "%"), // 질문 작성자
                        cb.like(a.get("content"),"%" + kw + "%"), // 답변 내용
                        cb.like(u2.get("username"),"%" + kw + "%")); // 답변 작성자
            }
        };
    }

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
    }

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question1 = this.questionRepository.findById(id);
        if (question1.isPresent()) {
            Question question = question1.get();

            //조회수 증가 로직
            question.incrementViewCount(); // Question 클래스의 조회수 증가 메서드 호출
            this.questionRepository.save(question); // 변경된 객체 저장

            return question;
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    @Transactional
    public Question create(String subject, String content, SiteUser user, List<MultipartFile> files, boolean isSecret) throws IOException {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        q.setSecret(isSecret);
        q.setView_count(0);
        this.questionRepository.save(q);

        for (MultipartFile file : files) {
            fileService.saveFile(file, q);
        }
        return q;
    }

    public void modify(Question question, String subject,String content, List<MultipartFile> newfiles, List<Long> deleteFileIds, boolean isSecret ) throws IOException {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        question.setSecret(isSecret);
        this.questionRepository.save(question);

        // 선택적 파일 삭제 로직
        if (deleteFileIds != null && !deleteFileIds.isEmpty()){
            fileService.deleteFilesByIds(deleteFileIds);
        }
        for (MultipartFile file : newfiles) {
            if (!file.isEmpty()) {
                fileService.saveFile(file, question);
            }
        }
    }

    public Page<Question> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        // page - 1을 사용하여 사용자의 입력(1부터 시작)을 Pageable의 0부터 시작하는 인덱스에 맞춥니다.
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));
        return this.questionRepository.findAll(pageable);
    }

    @Transactional
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    @Transactional
    public void vote(Question question, SiteUser siteUser) {
        if (question.getVoter().contains(siteUser)){
            question.getVoter().remove(siteUser);
        }else {
            question.getVoter().add(siteUser);
        }
        this.questionRepository.save(question);
    }

    public Long getQuestionCount(SiteUser author) {
        return questionRepository.countByAuthor(author);
    }

    public List<Question> getQuestionTop5LatesByUser(SiteUser author) {
        return questionRepository.findTop5ByAuthorOrderByCreateDateDesc(author);
    }

    // 유저 개인별 질문 모음(질문자)
    public Page<Question> getPersonalQuestionListByQuestionAuthorId(int page, String kw, Long authorId) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); //페이지 번호, 갯수
        return questionRepository.findAllByKeywordAndAuthorId(kw, authorId, pageable);
    }
    // 유저 개인별 질문 모음(답변자)
    public Page<Question> getPersonalQuestionListByAnswer_AuthorId(int page, String kw, Long answerAuthorId) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); //페이지 번호, 갯수
        return questionRepository.findAllByKeywordAndAndAnswer_AuthorId(kw, answerAuthorId, pageable);
    }
}