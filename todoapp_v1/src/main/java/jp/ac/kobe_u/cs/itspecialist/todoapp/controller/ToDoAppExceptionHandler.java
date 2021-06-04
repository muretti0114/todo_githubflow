package jp.ac.kobe_u.cs.itspecialist.todoapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ToDoAppExceptionHandler {

    /**
     * 全部の例外をハンドルし，エラーページを表示する
     */
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex, Model model) {
        model.addAttribute("exception", ex);

        return "error";
    }

}
