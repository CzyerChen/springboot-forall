package com.redission.domain;

import org.redisson.codec.JsonJacksonCodec;
import org.springframework.core.serializer.Deserializer;

import java.io.Serializable;
import java.util.Objects;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 24 10:51
 */
public class User implements Serializable{
   private int id;
   private String name;
   private int age;

    public User() {
    }

    public User(int id, String name,int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                age == user.age &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }
}
