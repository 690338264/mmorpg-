package com.database.entity;

import java.io.Serializable;

public class user implements Serializable {
    private Long id;
    private String name;
    private String pwd;
    private static final long serialVersionID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
