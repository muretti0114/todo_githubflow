package jp.ac.kobe_u.cs.itspecialist.todoapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.MemberForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;
import jp.ac.kobe_u.cs.itspecialist.todoapp.service.MemberService;

@Controller
@RequestMapping("/admin")
public class MemberController {
    @Autowired
    MemberService mService;

    /**
     * 管理者用・ユーザ登録ページ HTTP-GET /admin/register
     * 
     * @param model
     * @return
     */
    @GetMapping("/register")
    String showUserForm(@ModelAttribute(name = "MemberForm") MemberForm form, Model model) {
        List<Member> members = mService.getAllMembers();
        model.addAttribute("members", members);
        model.addAttribute("MemberForm", form);

        return "register";
    }

    /**
     * 管理者用・ユーザ登録確認ページを表示 HTTP-POST /admin/check
     * 
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/check")
    String checkUserForm(@Validated @ModelAttribute(name = "MemberForm") MemberForm form, 
            BindingResult bindingResult, Model model) {
        // 入力チェックに引っかかった場合、ユーザー登録画面に戻る
        if (bindingResult.hasErrors()) {
            // GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻る
            return showUserForm(form, model);
        }

        model.addAttribute("MemberForm", form);

        return "check";
    }

    /**
     * 管理者用・ユーザ登録処理 -> 完了ページ HTTP-POST /admin/register
     * 
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/register")
    String createUser(@Validated @ModelAttribute(name = "MemberForm") MemberForm form, 
        BindingResult bindingResult, Model model)  {
        // 入力チェックに引っかかった場合、ユーザー登録画面に戻る
        if (bindingResult.hasErrors()) {
            // GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻る
            return showUserForm(form, model);
        }

        Member m = mService.createMember(form);
        model.addAttribute("MemberForm", m);

        return "registered";
    }

    @GetMapping("/delete/{mid}")
    String deleteUser(@PathVariable String mid, Model model) {
        mService.deleteMember(mid);
        return "redirect:/admin/register";
    }

}
