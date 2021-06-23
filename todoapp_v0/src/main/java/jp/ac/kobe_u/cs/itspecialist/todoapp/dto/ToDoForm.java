package jp.ac.kobe_u.cs.itspecialist.todoapp.dto;

import lombok.Data;

/**
 * ToDoの入力フォーム
 */
@Data
public class ToDoForm {
    String title;      // ToDo題目
    String background; // 背景色
}
