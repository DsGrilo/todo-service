package com.grilo.todoservice.architecture.repository.todo;

import com.grilo.todoservice.architecture.entity.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
}