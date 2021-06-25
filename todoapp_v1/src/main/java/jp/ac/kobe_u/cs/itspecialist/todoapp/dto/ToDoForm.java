package jp.ac.kobe_u.cs.itspecialist.todoapp.dto;

import java.util.Date;

import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.ToDo;
import lombok.Data;

/**
 * ToDoの入力フォーム
 */
@Data
public class ToDoForm {
    String title; //ToDo題目

    public ToDo toEntity() {
        ToDo t = new ToDo();
        t.setTitle(title);
        t.setCreatedAt(new Date());
        t.setDone(false);
        return t;
    }
}
