package com.grilo.todoservice.service;

import com.grilo.todoservice.architecture.commom.exception.GenericException;
import com.grilo.todoservice.architecture.commom.Mapper;
import com.grilo.todoservice.architecture.entity.todo.Todo;
import com.grilo.todoservice.architecture.entity.user.User;
import com.grilo.todoservice.architecture.model.todo.TodoCreateModel;
import com.grilo.todoservice.architecture.repository.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository repository;
    private final UserService userService;
    private final Mapper mapper;

    public void create(TodoCreateModel model, String creatorUsername){
        var todo = new Todo();
        var user = userService.getUser(creatorUsername);

        todo = mapper.convert(model, Todo.class);
        todo.setCreator(user);

        repository.save(todo);
    }

    public Todo findById(int id) {
        var todo = repository.findById(id);
        if(todo.isEmpty()){
            throw new GenericException("Not Found");
        }

        return todo.get();
    }

    public List<Todo> list(User user) {
        return repository.findByCreator_Id(user.getId());
    }

    public void delete(int id) {
        var todo = repository.findById(id);
        if(todo.isEmpty()){
            throw new GenericException("Not Found");
        }
        repository.delete(todo.get());
    }
}
