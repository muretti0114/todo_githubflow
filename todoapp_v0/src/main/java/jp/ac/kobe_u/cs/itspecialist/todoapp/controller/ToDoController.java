package jp.ac.kobe_u.cs.itspecialist.todoapp.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    String showIndex(Model model) {
        return "index";
    }

    /**
     * ログイン処理．midの存在確認をして，ユーザページにリダイレクト
     */
    @PostMapping("/login")
    String login(@RequestParam String mid, Model model) {
        Member m = mService.getMember(mid);

        return "redirect:/" + m.getMid() + "/todos";
    }

    /**
     * ユーザのToDoリストのページ
     */
    @GetMapping("/{mid}/todos")
    String showToDoList(@PathVariable String mid, Model model) {
        Member m = mService.getMember(mid);
        ToDoForm form = new ToDoForm();
        List<ToDo> todos = tService.getToDoList(mid);
        List<ToDo> dones = tService.getDoneList(mid);
        model.addAttribute("member", m);
        model.addAttribute("todos", todos);
        model.addAttribute("dones", dones);
        model.addAttribute("ToDoForm", form);
        return "list";
    }

    /**
     * 全員のToDoリストのページ
     */
    @GetMapping("/{mid}/todos/all")
    String showAllToDoList(@PathVariable String mid, Model model) {
        Member m = mService.getMember(mid);
        List<ToDo> todos = tService.getToDoList();
        List<ToDo> dones = tService.getDoneList();
        model.addAttribute("member", m);
        model.addAttribute("todos", todos);
        model.addAttribute("dones", dones);
        return "alllist";
    }

    /**
     * ToDoの作成．処理後，ユーザページへリダイレクト
     */
    @PostMapping("/{mid}/todos")
    String createToDo(@PathVariable String mid, @Validated @ModelAttribute(name = "ToDoForm") ToDoForm form,
            Model model) {
        tService.createToDo(mid, form);
        return "redirect:/" + mid + "/todos";
    }

    @PutMapping("/{mid}/todos/{seq}/due")
    String updateToDoDue(@PathVariable String mid, @PathVariable Long seq, @Validated @ModelAttribute(name = "ToDoForm") ToDoForm form, Model model) {
        tService.updateDue(mid, seq, form.getDueDate());
        return "redirect:/" + mid + "/todos";
    }

    /**
     * ToDoの完了．処理後，ユーザページへリダイレクト
     */
    @GetMapping("/{mid}/todos/{seq}/done")
    String doneToDo(@PathVariable String mid, 
        @PathVariable Long seq, Model model) {
            tService.done(mid, seq);
        return "redirect:/" + mid + "/todos";
    }


}