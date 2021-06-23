package jp.ac.kobe_u.cs.itspecialist.todoapp.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.ToDoForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;
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
     * @param mid
     * @param form
     * @return
     */
    public ToDo createToDo(String mid, ToDoForm form) {
        Member m = mService.getMember(mid);

        ToDo todo = new ToDo();
        todo.setTitle(form.getTitle());
        todo.setCreatedAt(new Date());
        todo.setBackground(form.getBackground());
        todo.setMid(m.getMid());
        todo.setDone(false);

        return tRepo.save(todo);
    }

    /**
     * ToDoを更新する．
     * @param mid
     * @param seq
     * @param background
     * @return
     */
    public ToDo updateToDoBackground(String mid, Long seq, String background) {
        ToDo todo = getToDo(seq);
        todo.setBackground(background);
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
     * @param mid
     * @param seq
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
