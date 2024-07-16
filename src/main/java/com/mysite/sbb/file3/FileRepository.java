package com.mysite.sbb.file3;

import com.mysite.sbb.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByQuestion(Question question);
}
