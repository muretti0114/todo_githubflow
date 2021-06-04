package jp.ac.kobe_u.cs.itspecialist.todoapp.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * ToDoの入力フォーム
 */
@Data
public class ToDoForm {
    @NotBlank
    String title; //ToDo題目
}
