package jp.ac.kobe_u.cs.itspecialist.todoapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.ToDo;

public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    List<ToDo> findAll();
    List<ToDo> findByDone(boolean done);
    List<ToDo> findByMid(String mid);
    List<ToDo> findByMidAndDone(String mid, boolean done);
}
