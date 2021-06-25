package jp.ac.kobe_u.cs.itspecialist.todoapp.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.ToDoForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.ToDo;
import jp.ac.kobe_u.cs.itspecialist.todoapp.exception.ToDoAppException;
import jp.ac.kobe_u.cs.itspecialist.todoapp.repository.ToDoRepository;

@Service
public class ToDoService {
    @Autowired
    MemberService mService;
    @Autowired
    ToDoRepository tRepo;
    /**
     * ToDoを作成する (C)
     * @param mid 作成者
     * @param form フォーム
     * @return
     */
    public ToDo createToDo(String mid, ToDoForm form) {
        mService.getMember(mid); //実在メンバーか確認
        ToDo todo = form.toEntity();
        todo.setMid(mid);
        return tRepo.save(todo);
    }

    /**
     * ToDoを1つ取得する (R)
     * @param seq
     * @return
     */
    public ToDo getToDo(Long seq) {
        ToDo todo = tRepo.findById(seq).orElseThrow(
            () -> new ToDoAppException(ToDoAppException.NO_SUCH_TODO_EXISTS, 
            seq + ": No such ToDo exists")
        );
        return todo;
    }

    /**
     * あるメンバーのToDoリストを取得する (R)
     * @param mid
     * @return
     */
    public List<ToDo> getToDoList(String mid) {
        return tRepo.findByMidAndDone(mid, false);
    }
    /**
     * あるメンバーのDoneリストを取得する (R)
     * @param mid
     * @return
     */
    public List<ToDo> getDoneList(String mid) {
        return tRepo.findByMidAndDone(mid, true);
    }

    /**
     * 全員のToDoリストを取得する (R)
     * @return
     */
    public List<ToDo> getToDoList() {
        return tRepo.findByDone(false);
    }

    /**
     * 全員のDoneリストを取得する (R)
     * @return
     */
    public List<ToDo> getDoneList() {
        return tRepo.findByDone(true);
    }


    /**
     * ToDoを完了する
     * @param mid 完了者
     * @param seq 完了するToDoの番号
     * @return
     */
    public ToDo done(String mid, Long seq) {
        ToDo todo = getToDo(seq);
        //Doneの認可を確認する．他人のToDoを閉めたらダメ．
        if (!mid.equals(todo.getMid())) {
            throw new ToDoAppException(ToDoAppException.INVALID_TODO_OPERATION, mid 
            + ": Cannot done other's todo of " + todo.getMid());
        }
        todo.setDone(true);
        todo.setDoneAt(new Date());
        return tRepo.save(todo);
    }
}
