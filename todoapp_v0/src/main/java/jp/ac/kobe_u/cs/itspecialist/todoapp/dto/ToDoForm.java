package jp.ac.kobe_u.cs.itspecialist.todoapp.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * ToDoの入力フォーム
 */
@Data
public class ToDoForm {
    String title; //ToDo題目
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime due;     //締め切り

    public Date getDueDate() {
        if(due == null) {
            return null;
        }
        return toDate(due);
    }

    private Date toDate(LocalDateTime ldt) {
        ZoneId id = ZoneId.systemDefault();
        ZonedDateTime zdt = ZonedDateTime.of(ldt, id);
        Instant instant = zdt.toInstant();
        return Date.from(instant);
    }
}
