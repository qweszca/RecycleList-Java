package com.example.expandableview.po;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;


public class Task  extends LitePalSupport {
    private int id;
    private String name;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time='" + time+'}';
    }
}
