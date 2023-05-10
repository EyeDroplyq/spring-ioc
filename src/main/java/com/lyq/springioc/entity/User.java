package com.lyq.springioc.entity;

/**
 * @description:
 * @author: lyq
 * @createDate: 10/5/2023
 * @version: 1.0
 */
public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
