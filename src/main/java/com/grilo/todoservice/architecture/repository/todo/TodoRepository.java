package com.grilo.todoservice.architecture.repository.todo;

import com.grilo.todoservice.architecture.entity.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByCreator_Id(int id);

    List<Todo> findByCreator_IdOrViewers_Id(int id, int id2);



}