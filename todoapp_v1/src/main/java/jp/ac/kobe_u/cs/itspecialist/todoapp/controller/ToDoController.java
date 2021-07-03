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

import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.LoginForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.ToDoForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.ToDo;
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
    @GetMapping("/")
    String showIndex(@ModelAttribute(name = "loginForm") LoginForm loginForm, Model model) {
        //model.addAttribute("loginForm", loginForm);
        return "index";
    }

    /**
     * ログイン処理．midの存在確認をして，ユーザページにリダイレクト
     */
    @PostMapping("/login")
    String login(@Validated @ModelAttribute(name = "loginForm") LoginForm loginForm, BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) { // バリデーション
            return showIndex(loginForm, model);
        }

        Member m = mService.getMember(loginForm.getMid()); // 存在チェック
        return "redirect:/" + m.getMid() + "/todos";
    }

    /**
     * ユーザのToDoリストのページ
     */
    @GetMapping("/{mid}/todos")
    String showToDoList(@PathVariable String mid, @ModelAttribute(name = "ToDoForm") ToDoForm form, Model model) {
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
        tService.done(mid, seq);
        return "redirect:/" + mid + "/todos";
    }
}
