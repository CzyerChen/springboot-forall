package com.mappers.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 25 15:10
 */
@Entity(name = "profile")
public class ProfilePO {
    @Id
    private int id;
    private String name;
    private int age;

    public ProfilePO() {
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
