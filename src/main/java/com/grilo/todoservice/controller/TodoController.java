package com.grilo.todoservice.controller;

import com.grilo.todoservice.architecture.commom.Mapper;
import com.grilo.todoservice.architecture.commom.exception.GenericException;
import com.grilo.todoservice.architecture.model.todo.TodoCreateModel;
import com.grilo.todoservice.architecture.model.todo.TodoEditModel;
import com.grilo.todoservice.architecture.model.todo.TodoFindModel;
import com.grilo.todoservice.architecture.model.todo.TodoListModel;
import com.grilo.todoservice.architecture.model.user.JwtUser;
import com.grilo.todoservice.service.TodoService;
import com.grilo.todoservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@Transactional
public class TodoController {
    private final TodoService service;
    private final UserService userService;
    private final Mapper mapper;



    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/create")
    public void create(@RequestBody TodoCreateModel model, @AuthenticationPrincipal JwtUser user){
        service.create(model, user.getUsername());
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public void edit(@RequestBody TodoEditModel model, @AuthenticationPrincipal JwtUser user){
        var todo = service.findById(model.getId());

        if(todo.getCreator().getId() != user.getId()) throw new GenericException("Not Authorized");

        todo.setIcon(model.getIcon());
        todo.setCategory(model.getCategory());
        todo.setDescription(model.getDescription());
        todo.setUpdatedAt(new Date());
        todo.setExpiration(model.getExpiration());
        todo.setTitle(model.getTitle());
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public List<TodoListModel> list(@AuthenticationPrincipal JwtUser jwtUser){
        var user = userService.getUser(jwtUser.getUsername());
        return service.list(user).stream().map($ -> {
            var todo = mapper.convert($, TodoListModel.class);
            todo.setResponsible($.getCreator().getId() == jwtUser.getId());
            return todo;
        }).toList();
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public TodoFindModel findById(@PathVariable("id") int id){
        return mapper.convert(service.findById(id), TodoFindModel.class);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public void deleteById(@PathVariable("id") int id){
        service.delete(id);
    }

    @PatchMapping("/finish/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public void finishTodo(@PathVariable("id") int id, @AuthenticationPrincipal JwtUser jwtUser){
        service.finished(id, jwtUser.getId());
    }
}
