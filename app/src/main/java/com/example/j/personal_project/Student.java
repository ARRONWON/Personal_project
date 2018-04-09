package com.example.j.personal_project;

import java.io.Serializable;

/**
 * Created by wgs on 2018/4/8.
 */

class Student implements Serializable {
    private int id;
    private String name;
    private String grade;

    public Student(){}
    public Student(int id, String name, String grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public Student(String name, String grade) {
        this.name = name;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
