package com.grilo.todoservice.architecture.entity.todo;

import com.grilo.todoservice.architecture.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String description;
    private String category;
    private Date expiration;
    private boolean expirated
    private Date createdAt;
    private Date updatedAt;
    private String icon;
    @OneToOne
    private User creator;
    @OneToMany
    private List<User> viewers;



    @PrePersist
    public void pre(){
        this.createdAt = new Date();
    }

    public boolean hasViewers(){
        return viewers != null && viewers.size() > 0;
    }
}
