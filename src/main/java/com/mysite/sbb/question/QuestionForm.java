package com.mysite.sbb.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class QuestionForm {
    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max = 200)
    private String subject;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    private List<MultipartFile> files;

    //기본 생성자
    public QuestionForm() {}

    // 제목과 내용을 매개변수로 받는 생성자
    public QuestionForm(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}