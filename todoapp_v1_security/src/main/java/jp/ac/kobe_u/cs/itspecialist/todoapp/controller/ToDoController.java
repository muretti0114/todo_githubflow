package jp.ac.kobe_u.cs.itspecialist.todoapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.LoginForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.ToDoForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.UserDetailsImpl;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.ToDo;
import jp.ac.kobe_u.cs.itspecialist.todoapp.exception.ToDoAppException;
import jp.ac.kobe_u.cs.itspecialist.todoapp.service.MemberService;
import jp.ac.kobe_u.cs.itspecialist.todoapp.service.ToDoService;

@Controller
public class ToDoController {
    @Autowired
    MemberService mService;
    @Autowired
    ToDoService tService;

    /**
     * トップページ
     */
    @GetMapping("/sign_in")
    String showIndex(@RequestParam Map<String, String> params, @ModelAttribute LoginForm form, Model model) {
        //パラメータ処理．ログアウト時は?logout, 認証失敗時は?errorが帰ってくる（WebSecurityConfig.java参照） 
		if (params.containsKey("sign_out")) {
			model.addAttribute("message", "サインアウトしました");
		} else if (params.containsKey("error")) {
			model.addAttribute("message", "サインインに失敗しました");
		} 
        //model.addAttribute("loginForm", loginForm);
        return "signin";
    }

    /**
     * ログイン処理．midの存在確認をして，ユーザページにリダイレクト
     */
    @GetMapping("/sign_in_success")
    String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member m  = ((UserDetailsImpl) auth.getPrincipal()).getMember();
        if (m.getRole().equals("ADMIN")) {
            return "redirect:/admin/register";
        }
        return "redirect:/" + m.getMid() + "/todos";
    }

    /**
     * ユーザのToDoリストのページ
     */
    @GetMapping("/{mid}/todos")
    String showToDoList(@PathVariable String mid, @ModelAttribute(name = "ToDoForm") ToDoForm form, Model model) {
        checkIdentity(mid);

        Member m = mService.getMember(mid);
        model.addAttribute("member", m);
        model.addAttribute("ToDoForm", form);
        List<ToDo> todos = tService.getToDoList(mid);
        model.addAttribute("todos", todos);
        List<ToDo> dones = tService.getDoneList(mid);
        model.addAttribute("dones", dones);
        return "list";
    }

    /**
     * 全員のToDoリストのページ
     */
    @GetMapping("/{mid}/todos/all")
    String showAllToDoList(@PathVariable String mid, Model model) {
        checkIdentity(mid);
        Member m = mService.getMember(mid);
        model.addAttribute("member", m);
        List<ToDo> todos = tService.getToDoList();
        model.addAttribute("todos", todos);
        List<ToDo> dones = tService.getDoneList();
        model.addAttribute("dones", dones);
        return "alllist";
    }

    /**
     * ToDoの作成．作成処理後，ユーザページへリダイレクト
     */
    @PostMapping("/{mid}/todos")
    String createToDo(@PathVariable String mid, @Validated @ModelAttribute(name = "ToDoForm") ToDoForm form,
            BindingResult bindingResult, Model model) {
        checkIdentity(mid);

        if (bindingResult.hasErrors()) {
            return showToDoList(mid, form, model);
        }
        tService.createToDo(mid, form);
        return "redirect:/" + mid + "/todos";
    }

    /**
     * ToDoの完了．完了処理後，ユーザページへリダイレクト
     */
    @GetMapping("/{mid}/todos/{seq}/done")
    String doneToDo(@PathVariable String mid, @PathVariable Long seq, Model model) {
        checkIdentity(mid);
        tService.done(mid, seq);
        return "redirect:/" + mid + "/todos";
    }


    /**
     * 認可チェック．与えられたmidがログイン中のmidに等しいかチェックする
     */
    private void checkIdentity(String mid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member m  = ((UserDetailsImpl) auth.getPrincipal()).getMember();
        if (!mid.equals(m.getMid())) {
            throw new ToDoAppException(ToDoAppException.INVALID_TODO_OPERATION, 
            m.getMid() + ": not authorized to access resources of " + mid);
        }
    }


}
