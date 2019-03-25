package com.mappers.domain.dto;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 25 15:11
 */
public class ProfileDTO {
    private int id;
    private String name;
    private int age;

    public ProfileDTO() {
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

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
