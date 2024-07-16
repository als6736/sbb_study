package com.mysite.sbb.file3;

import com.mysite.sbb.question.Question;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Table(name = "file")
@Entity
@Getter
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Long id;

    private String orgNm;
    private String savedNm;
    private String savedPath;
    private String mimeType; // 파일의 MIME 타입 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Builder
    public FileEntity(Long id, String orgNm, String savedNm, Question questn,String savedPath, String mimeType) {
        this.id = id;
        this.orgNm = orgNm;
        this.savedNm = savedNm;
        this.question = questn;
        this.savedPath =savedPath;
        this.mimeType = mimeType;
    }
}
