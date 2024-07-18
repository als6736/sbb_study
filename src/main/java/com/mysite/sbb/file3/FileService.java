package com.mysite.sbb.file3;

import com.mysite.sbb.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${file.dir}")
    private String fileDir;

    private final FileRepository fileRepository;

    public Long saveFile(MultipartFile files, Question question) throws IOException {
        if (files.isEmpty()) {
            return null;
        }

        //파일의 MINE
        String mimeType = files.getContentType();
        //원래 파일 이름 추출
        String origName = files.getOriginalFilename();
        //파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();
        //확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));
        //uuid와 확장자 결합
        String savedName = uuid + extension;
        //파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + savedName;

        // 로그 추가
        System.out.println("Saving file: " + origName + " as " + savedName + " to " + savedPath);

        //파일 엔티티 생성
        FileEntity file = FileEntity.builder()
                .orgNm(origName)
                .savedNm(savedName)
                .savedPath(savedPath)
                .mimeType(mimeType)
                .questn(question)
                .build();

        try {
            // 실제로 로컬에 uuid를 파일명으로 저장
            files.transferTo(new File(savedPath));
        } catch (IOException e) {
            System.err.println("Failed to save file: " + e.getMessage());
            throw e;
        }

        //데이터베이스에 파일 정보 저장
        FileEntity savedFile = fileRepository.save(file);

        return savedFile.getId();
    }

    public void deleteFilesByIds(List<Long> fileIds) {
        if (fileIds == null || fileIds.isEmpty()) {
            return; //아무것도 삭제하지 않음
        }
        for (Long fileId: fileIds) {
            Optional<FileEntity> fileEntity = fileRepository.findById(fileId);
            if (fileEntity.isPresent()) {
                fileRepository.delete(fileEntity.get());
                new File(fileEntity.get().getSavedPath()).delete();
            }
        }
    }

    public void deleteAllFilesForQuestion(Question question) {
        List<FileEntity> files = fileRepository.findByQuestion(question);
        for (FileEntity file : files) {
            File physicalFile = new File(file.getSavedPath());
            if (physicalFile.delete()) {
                fileRepository.delete(file);
            } else {
                // 로그 남기기 또는 예외 처리
                System.err.println("Failed to delete file: " + file.getSavedPath());
            }
        }
    }
}
