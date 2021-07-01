package jp.ac.kobe_u.cs.itspecialist.todoapp.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jp.ac.kobe_u.cs.itspecialist.todoapp.exception.ToDoAppException;
import lombok.extern.slf4j.Slf4j;

/**
 * ToDoアプリの例外ハンドラクラス
 */
@Slf4j
@ControllerAdvice
public class ToDoErrorHandler {
    /**
     * アプリケーション例外をハンドルし，エラーページを表示する
     */
    @ExceptionHandler(ToDoAppException.class)
    public String handleToDoException(ToDoAppException ex, Model model) {
        model.addAttribute("advice", "アプリケーション例外が発生しました．メッセージを確認してください");
        model.addAttribute("exception", ex);
        log.error(ex.getMessage());
        return "error";
    }

    /**
     * その他の例外をハンドルし，エラーページを表示する
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("advice", "深刻なエラーが発生しました．管理者にお問い合わせください");
        model.addAttribute("exception", ex);
        log.error(ex.getMessage(), ex.getCause());
        return "error";
    }
}
