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
    List<ToDo> findByDoneOrderBySeqDesc(boolean done);
    List<ToDo> findByDoneOrderByTitle(boolean done);
    List<ToDo> findByDoneOrderByTitleDesc(boolean done);
    List<ToDo> findByDoneOrderByMid(boolean done);
    List<ToDo> findByDoneOrderByMidDesc(boolean done);
    List<ToDo> findByDoneOrderByCreatedAt(boolean done);
    List<ToDo> findByDoneOrderByCreatedAtDesc(boolean done);
    List<ToDo> findByDoneOrderByDoneAt(boolean done);
    List<ToDo> findByDoneOrderByDoneAtDesc(boolean done);

    // ソート機能を追加する．
    List<ToDo> findByMidAndDoneOrderBySeqDesc(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByTitle(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByTitleDesc(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByCreatedAt(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByCreatedAtDesc(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByDoneAt(String mid, boolean done);
    List<ToDo> findByMidAndDoneOrderByDoneAtDesc(String mid, boolean done);
}
