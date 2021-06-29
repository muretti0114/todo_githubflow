package jp.ac.kobe_u.cs.itspecialist.todoapp.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginForm {
    @NotBlank
    String mid;
}
