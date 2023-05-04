package com.grilo.todoservice.service;

import com.grilo.todoservice.architecture.commom.exception.GenericException;
import com.grilo.todoservice.architecture.commom.Mapper;
import com.grilo.todoservice.architecture.entity.todo.Todo;
import com.grilo.todoservice.architecture.entity.user.User;
import com.grilo.todoservice.architecture.model.todo.TodoCreateModel;
import com.grilo.todoservice.architecture.repository.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        if(model.getViewers() != null && model.getViewers().size() > 0){
            var viewers = new ArrayList<User>();

            for(var i = 0; i < model.getViewers().size(); i++){
                var viewer = userService.findById(model.getViewers().get(i));
                viewers.add(viewer);
            }

            todo.setViewers(viewers);
        }

        todo.setCreator(user);

        repository.save(todo);
    }

    public void save(Todo todo){repository.save(todo);}

    public Todo findById(int id) {
        var opt = repository.findById(id);
        if(opt.isEmpty()){
            throw new GenericException("Not Found");
        }

        return opt.get();
    }

    public List<Todo> list(User user) {
        return repository.findByCreator_IdOrViewers_Id(user.getId(), user.getId());
    }

    public List<Todo> findAll(){return repository.findAll();}
    public void delete(int id) {
        var opt = repository.findById(id);
        if(opt.isEmpty()){
            throw new GenericException("Not Found");
        }
        repository.delete(opt.get());
    }

    public void finished(int id, int userId){
        var todo = findById(id);
        if(todo.getCreator().getId() != userId){
            throw new GenericException("Only the person in charge can finish a task");
        }
        todo.setFinished(!todo.isFinished());
    }
}
