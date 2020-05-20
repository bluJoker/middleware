package com.debug.middleware.server.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
//@Getter
//@Setter
public class User implements Serializable{
    private Integer id;
    private String userName;
    private String name;

    public User() {
    }

    public User(Integer id, String userName, String name) {
        this.id = id;
        this.userName = userName;
        this.name = name;
    }
}
