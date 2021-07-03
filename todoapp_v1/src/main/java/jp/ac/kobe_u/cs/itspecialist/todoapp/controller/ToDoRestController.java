package jp.ac.kobe_u.cs.itspecialist.todoapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.ToDoForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.ToDo;
import jp.ac.kobe_u.cs.itspecialist.todoapp.exception.ToDoAppException;
import jp.ac.kobe_u.cs.itspecialist.todoapp.service.MemberService;
import jp.ac.kobe_u.cs.itspecialist.todoapp.service.ToDoService;

/**
 * ToDoの操作，CRUDを行うAPI
 */
@RestController
@RequestMapping("/api")
public class ToDoRestController {
    @Autowired
    ToDoService todoService;
    @Autowired
    MemberService memberService;

    /* --- C: ToDoを作成する --- */
    @PostMapping("/{mid}/todos")
    ToDo createToDo(@PathVariable String mid, @Validated @RequestBody ToDoForm form) {
        return todoService.createToDo(mid, form);
    }

    /* --- R: ToDoを取得する (1件) --- */
    @GetMapping("/{mid}/todos/{seq}")
    ToDo getToDoList(@PathVariable String mid, @PathVariable Long seq) {
        return todoService.getToDo(seq);
    }

    /* --- R: ToDoを取得する (リスト) --- */
    @GetMapping("/{mid}/todos")
    List<ToDo> getToDoList(@PathVariable String mid) {
        return todoService.getToDoList(mid);
    }

    /* --- R: Doneを取得する (リスト) --- */
    @GetMapping("/{mid}/dones")
    List<ToDo> getDoneList(@PathVariable String mid) {
        return todoService.getDoneList(mid);
    }

    /* --- U: ToDoを完了する --- */
    @PutMapping("/{mid}/todos/{seq}/done")
    ToDo done(@PathVariable String mid, @PathVariable Long seq) {
        return todoService.done(mid, seq);
    }

    /* --- U: ToDoを更新する --- */
    @PutMapping("/{mid}/todos/{seq}")
    ToDo updateToDo(@PathVariable String mid, @PathVariable Long seq, @Validated @RequestBody ToDoForm form) {
        return todoService.updateToDo(mid, seq, form);
    }

    /* --- D: ToDoを削除する --- */
    @DeleteMapping("/{mid}/todos/{seq}")
    boolean deleteToDo(@PathVariable String mid, @PathVariable Long seq) {
        todoService.deleteToDo(mid, seq);
        return true;
    }

    /*--------------------- エラーハンドラー -----------------------------*/
    /*
     * 本当は@RestControllerAdviceにまとめて書きたいが，@ControllerAdviceと競合するので，
     * Controllerに書いている．
     */
    @ExceptionHandler(ToDoAppException.class)
    public ResponseEntity<Object> handleToDoException(ToDoAppException ex) {
        HttpStatus status;
        switch (ex.getCode()) {
            // 存在しない系例外
            case ToDoAppException.NO_SUCH_MEMBER_EXISTS:
            case ToDoAppException.NO_SUCH_TODO_EXISTS:
                status = HttpStatus.NOT_FOUND;
                break;
            // パラメタ異常系例外
            case ToDoAppException.MEMBER_ALREADY_EXISTS:
            case ToDoAppException.INVALID_MEMBER_INFO:
            case ToDoAppException.INVALID_TODO_INFO:
                status = HttpStatus.BAD_REQUEST;
                break;
            // 認可失敗系例外
            case ToDoAppException.INVALID_MEMBER_OPERATION:
            case ToDoAppException.INVALID_TODO_OPERATION:
                status = HttpStatus.FORBIDDEN;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(ex, status);
    }

    /* -- バリデーション失敗 -- */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }

    /* -- その他の例外 -- */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
