package com.grilo.todoservice.architecture.model.todo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodoFindModel {
    private int id;
    private String title;
    private String description;
    private String category;
    private Date expiration;
    private boolean expirated;
    private String icon;
}
