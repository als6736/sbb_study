package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.file3.FileEntity;
import com.mysite.sbb.file3.FileRepository;
import com.mysite.sbb.file3.FileService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    private final FileRepository fileRepository;
    private final FileService fileService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        question.incrementViewCount();
        model.addAttribute("question",question);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm, Model model) {
        model.addAttribute("action", "create");
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 Principal principal,
                                 @RequestParam("files")List<MultipartFile> files,
                                 RedirectAttributes redirectAttributes) throws IOException {
        if (files.size() > 3) {
            redirectAttributes.addFlashAttribute("error", "파일은 최대 3개까지 업로드 가능합니다.");
            return "redirect:/create";
        }

        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser, files);
        return "redirect:/question/list"; // 질문 등록후 저장목록으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(@PathVariable("id") Integer id, Model model, Principal principal) {
        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }
        QuestionForm questionForm = new QuestionForm();
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());

        List<FileEntity> files = fileRepository.findByQuestion(question);

        model.addAttribute("questionForm", questionForm);
        model.addAttribute("question", question);
        model.addAttribute("files", files);
        model.addAttribute("action", "modify");
        return "question_form";
    }
//    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Model model, Principal principal) {
//        Question question = this.questionService.getQuestion(id);

//        List<FileEntity> files = fileRepository.findByQuestion(question);
//        model.addAttribute("files",files);
//        questionForm.setSubject(question.getSubject());
//        questionForm.setContent(question.getContent());
//        model.addAttribute("question", question);
//        return "question_form";
//    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 @RequestParam("files") List<MultipartFile> files,
                                 @RequestParam(value = "deleteFiles", required = false) List<Long> deleteFileIds, //삭제할 첨부파일 아이디
                                 Principal principal, @PathVariable("id") Integer id,
                                 RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        if (files.size() > 3) {
            redirectAttributes.addFlashAttribute("error", "파일은 최대 3개까지 업로드 가능합니다.");
            return "redirect:/question/modify/" +id;
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent(), files, deleteFileIds);
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        fileService.deleteAllFilesForQuestion(question);
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }

}