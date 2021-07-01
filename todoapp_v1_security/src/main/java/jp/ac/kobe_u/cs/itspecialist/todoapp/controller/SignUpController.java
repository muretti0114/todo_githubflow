package jp.ac.kobe_u.cs.itspecialist.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.MemberForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;
import jp.ac.kobe_u.cs.itspecialist.todoapp.service.MemberService;

@Controller
@RequestMapping("/sign_up")
public class SignUpController {
    @Autowired
    MemberService mService;

    /**
     * 一般ユーザの登録ページ HTTP-GET /sign_up
     * 
     * @param model
     * @return
     */
    @GetMapping("")
    String showSignUpForm(@ModelAttribute MemberForm form, Model model) {
        model.addAttribute("MemberForm", form);

        return "signup";
    }

    /**
     * ユーザ登録確認ページを表示 HTTP-POST /sign_up/check
     * 
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/check")
    String checkMemberForm(@Validated @ModelAttribute(name = "MemberForm") MemberForm form, BindingResult bindingResult,
            Model model) {
        // 入力チェックに引っかかった場合、ユーザー登録画面に戻る
        if (bindingResult.hasErrors()) {
            // GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻る
            return showSignUpForm(form, model);
        }

        model.addAttribute("MemberForm", form);

        return "signup_check";
    }

    /**
     * ユーザ登録処理 -> 完了ページ HTTP-POST /sign_up/register
     * 
     * @param form
     * @param model
     * @return
     */
    @PostMapping("")
    String createMember(@ModelAttribute(name = "MemberForm") MemberForm form, Model model) {
        Member m = mService.createMember(form);
        model.addAttribute("MemberForm", m);

        return "signup_complete";
    }

}
