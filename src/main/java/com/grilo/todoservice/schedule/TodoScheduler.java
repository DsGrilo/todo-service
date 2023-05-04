package com.grilo.todoservice.schedule;

import com.grilo.todoservice.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TodoScheduler {

    private final TodoService todoService;


    @Async
    @Scheduled(fixedDelay = 43200000L , initialDelay = 0)
    public void updateExpirated(){
        var todos = todoService.findAll();
        var today = new Date();

        todos.forEach(todo -> {
            todo.setExpirated(todo.hasExpirated(today));
            todoService.save(todo);
        });
    }
}
