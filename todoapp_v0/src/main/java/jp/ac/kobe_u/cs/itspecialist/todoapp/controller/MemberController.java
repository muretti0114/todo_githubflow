package jp.ac.kobe_u.cs.itspecialist.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.MemberForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;
import jp.ac.kobe_u.cs.itspecialist.todoapp.service.MemberService;

@Controller
public class MemberController {
    @Autowired
    MemberService mService;

    @GetMapping("/register")
    String showUserForm(Model model) {
        MemberForm form = new MemberForm();
        model.addAttribute("MemberForm", form);
        
        return "register";
    }

    @PostMapping("/check") 
    String checkUserForm(@Validated @ModelAttribute(name = "MemberForm") MemberForm form,  Model model) {
        model.addAttribute("MemberForm", form);

        return "check";
    }

    @PostMapping("/register")
    String createUser(@Validated @ModelAttribute(name = "MemberForm") MemberForm form,  Model model) {
        Member m =  mService.createMember(form);
        model.addAttribute("MemberForm", m);

        return "registered";
    }
    
}
