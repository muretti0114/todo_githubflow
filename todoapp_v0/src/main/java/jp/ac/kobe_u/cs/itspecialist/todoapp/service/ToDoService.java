package jp.ac.kobe_u.cs.itspecialist.todoapp.service;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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

    private final Map<Pair<String, String>, BiFunction<String, Boolean, List<ToDo>>> midAndDoneFinder = generateMidAndDoneFinder();
    private final Map<Pair<String, String>, BiFunction<String, Boolean, List<ToDo>>> generateMidAndDoneFinder() {
        Map<Pair<String, String>, BiFunction<String, Boolean, List<ToDo>>> map = new HashMap<>();
        map.put(Pair.of("seq", "asc"), (mid, doneFlag) -> tRepo.findByMidAndDoneOrderBySeqAsc(mid, doneFlag));
        map.put(Pair.of("seq", "desc"), (mid, doneFlag) -> tRepo.findByMidAndDoneOrderBySeqDesc(mid, doneFlag));
        map.put(Pair.of("title", "asc"), (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByTitleAsc(mid, doneFlag));
        map.put(Pair.of("title", "desc"), (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByTitleDesc(mid, doneFlag));
        map.put(Pair.of("created_at", "asc"), (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByCreatedAtAsc(mid, doneFlag));
        map.put(Pair.of("created_at", "desc"), (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByCreatedAtDesc(mid, doneFlag));
        map.put(Pair.of("done_at", "asc"), (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByDoneAtAsc(mid, doneFlag));
        map.put(Pair.of("done_at", "desc"), (mid, doneFlag) -> tRepo.findByMidAndDoneOrderByDoneAtDesc(mid, doneFlag));
        return map;
    }

    private BiFunction<String, Boolean, List<ToDo>> selectFinderByMidAndDone(String sortBy, String order) {
        return midAndDoneFinder.getOrDefault(Pair.of(sortBy, order),
                (mid, doneFlag) -> tRepo.findByMidAndDone(mid, doneFlag));
    }

    /**
     * 全員のToDoリストを取得する (R)
     * @return
     */
    public List<ToDo> getToDoList(String sortBy, String order) {
        Function<Boolean, List<ToDo>> mapper = selectFinderByDone(sortBy, order);
        return mapper.apply(false);
    }

    /**
     * 全員のDoneリストを取得する (R)
     * @return
     */
    public List<ToDo> getDoneList(String sortBy, String order) {
        Function<Boolean, List<ToDo>> mapper = selectFinderByDone(sortBy, order);
        return mapper.apply(true);
    }

    private final Map<Pair<String, String>, Function<Boolean, List<ToDo>>> doneFinder =generateDoneFinder();
    private final Map<Pair<String, String>, Function<Boolean, List<ToDo>>> generateDoneFinder() {
        Map<Pair<String, String>, Function<Boolean, List<ToDo>>> map = new HashMap<>();
        map.put(Pair.of("seq", "asc"), (doneFlag) -> tRepo.findByDoneOrderBySeqAsc(doneFlag));
        map.put(Pair.of("seq", "desc"), (doneFlag) -> tRepo.findByDoneOrderBySeqDesc(doneFlag));
        map.put(Pair.of("title", "asc"), (doneFlag) -> tRepo.findByDoneOrderByTitleAsc(doneFlag));
        map.put(Pair.of("title", "desc"), (doneFlag) -> tRepo.findByDoneOrderByTitleDesc(doneFlag));
        map.put(Pair.of("mid", "asc"), (doneFlag) -> tRepo.findByDoneOrderByMidAsc(doneFlag));
        map.put(Pair.of("mid", "desc"), (doneFlag) -> tRepo.findByDoneOrderByMidDesc(doneFlag));
        map.put(Pair.of("created_at", "asc"), (doneFlag) -> tRepo.findByDoneOrderByCreatedAtAsc(doneFlag));
        map.put(Pair.of("created_at", "desc"), (doneFlag) -> tRepo.findByDoneOrderByCreatedAtDesc(doneFlag));
        map.put(Pair.of("done_at", "asc"), (doneFlag) -> tRepo.findByDoneOrderByDoneAtAsc(doneFlag));
        map.put(Pair.of("done_at", "desc"), (doneFlag) -> tRepo.findByDoneOrderByDoneAtDesc(doneFlag));
        return map;
    }

    private Function<Boolean, List<ToDo>> selectFinderByDone(String sortBy, String order) {
        return doneFinder.getOrDefault(Pair.of(sortBy, order),
                (doneFlag) -> tRepo.findByDone(doneFlag));
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
