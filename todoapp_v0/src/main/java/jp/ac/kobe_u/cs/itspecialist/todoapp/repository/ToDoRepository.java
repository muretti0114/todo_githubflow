package jp.ac.kobe_u.cs.itspecialist.todoapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.ToDo;

public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    List<ToDo> findAll();
    List<ToDo> findByDone(boolean done);
    List<ToDo> findByMid(String mid);
    List<ToDo> findByMidAndDone(String mid, boolean done);

    // ソート機能を追加する．
    List<ToDo> findByDoneOrderBySeqAsc(boolean done);
    List<ToDo> findByDoneOrderBySeqDesc(boolean done);
    List<ToDo> findByDoneOrderByTitleAsc(boolean done);
    List<ToDo> findByDoneOrderByTitleDesc(boolean done);
    List<ToDo> findByDoneOrderByMidAsc(boolean done);
    List<ToDo> findByDoneOrderByMidDesc(boolean done);
    List<ToDo> findByDoneOrderByCreatedAtAsc(boolean done);
    List<ToDo> findByDoneOrderByCreatedAtDesc(boolean done);
    List<ToDo> findByDoneOrderByDoneAtAsc(boolean done);
    List<ToDo> findByDoneOrderByDoneAtDesc(boolean done);

    // ソート機能を追加する．
    List<ToDo> findByMidAndDoneOrderBySeqAsc(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderBySeqDesc(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByTitleAsc(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByTitleDesc(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByCreatedAtAsc(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByCreatedAtDesc(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByDoneAtAsc(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByDoneAtDesc(String mid, boolean done);
}
