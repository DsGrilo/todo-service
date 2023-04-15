package com.grilo.todoservice.architecture.model.todo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodoListModel {
    private int id;
    private String title;
    private String category;
    private Date expiration;
    private String icon;
}
