package com.grilo.todoservice.architecture.model.user;

import com.grilo.todoservice.architecture.commom.Role;
import lombok.Data;

@Data
public class UserCreateModel {
    private int id;
    private String name;
    private String username;
    private String password;

}
