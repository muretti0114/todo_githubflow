package jp.ac.kobe_u.cs.itspecialist.todoapp.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        todo.setMid(m.getMid());
        todo.setDone(false);

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
    public List<ToDo> getToDoList(String mid, String sortBy, String order) {
        BiFunction<String, Boolean, List<ToDo>> finder = selectFinderByMidAndDone(sortBy, order);
        return finder.apply(mid, false);
    }

    /**
     * あるメンバーのDoneリストを取得する (R)
     * @param mid
     * @return
     */
    public List<ToDo> getDoneList(String mid, String sortBy, String order) {
        BiFunction<String, Boolean, List<ToDo>> finder = selectFinderByMidAndDone(sortBy, order);
        return finder.apply(mid, true);
    }

    private BiFunction<String, Boolean, List<ToDo>> selectFinderByMidAndDone(String sortBy, String order) {
        if(Objects.equals(sortBy, "seq")){
            if(Objects.equals(order, "asc"))
                return (mid, doneFlag) -> tRepo.findByMidAndDone(mid, doneFlag);
            else
                return (mid, doneFlag) -> tRepo.findByMidAndDoneOrderBySeqDesc(mid, doneFlag);
        } else if(Objects.equals(sortBy, "title")) {
            if(Objects.equals(order, "asc"))
                return (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByTitle(mid, doneFlag);
            else
                return (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByTitleDesc(mid, doneFlag);
        } else if(Objects.equals(sortBy, "created_at")) {
            if(Objects.equals(order, "asc"))
                return (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByCreatedAt(mid, doneFlag);
            else
                return (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByCreatedAtDesc(mid, doneFlag);
        } else if(Objects.equals(sortBy, "done_at")) {
            if(Objects.equals(order, "asc"))
                return (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByDoneAt(mid, doneFlag);
            else
                return (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByDoneAtDesc(mid, doneFlag);
        }
        return (mid, doneFlag) -> tRepo.findByMidAndDone(mid, doneFlag);
    }

    /**
     * 全員のToDoリストを取得する (R)
     * @return
     */
    public List<ToDo> getToDoList(String sortBy, String order) {
        Function<Boolean, List<ToDo>> mapper = selectFinderByDone(sortBy, order);
        return mapper.apply(false);
        // return tRepo.findByDone(false);
    }

    /**
     * 全員のDoneリストを取得する (R)
     * @return
     */
    public List<ToDo> getDoneList(String sortBy, String order) {
        Function<Boolean, List<ToDo>> mapper = selectFinderByDone(sortBy, order);
        return mapper.apply(true);
        // return tRepo.findByDone(true);
    }

    private Function<Boolean, List<ToDo>> selectFinderByDone(String sortBy, String order) {
        if(Objects.equals(sortBy, "seq")){
            if(Objects.equals(order, "asc"))
                return (doneFlag) -> tRepo.findByDone(doneFlag);
            else
                return (doneFlag) -> tRepo.findByDoneOrderBySeqDesc(doneFlag);
        } else if(Objects.equals(sortBy, "title")) {
            if(Objects.equals(order, "asc"))
                return (doneFlag) -> tRepo.findByDoneOrderByTitle(doneFlag);
            else
                return (doneFlag) -> tRepo.findByDoneOrderByTitleDesc(doneFlag);
        } else if(Objects.equals(sortBy, "mid")) {
            if(Objects.equals(order, "asc"))
                return (doneFlag) -> tRepo.findByDoneOrderByMid(doneFlag);
            else
                return (doneFlag) -> tRepo.findByDoneOrderByMidDesc(doneFlag);
        } else if(Objects.equals(sortBy, "created_at")) {
            if(Objects.equals(order, "asc"))
                return (doneFlag) -> tRepo.findByDoneOrderByCreatedAt(doneFlag);
            else
                return (doneFlag) -> tRepo.findByDoneOrderByCreatedAtDesc(doneFlag);
        } else if(Objects.equals(sortBy, "done_at")) {
            if(Objects.equals(order, "asc"))
                return (doneFlag) -> tRepo.findByDoneOrderByDoneAt(doneFlag);
            else
                return (doneFlag) -> tRepo.findByDoneOrderByDoneAtDesc(doneFlag);
        }
        return (doneFlag) -> tRepo.findByDone(doneFlag);
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
