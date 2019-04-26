package com.forjpa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 14:50
 */
@Entity(name = "t_human")
@Table(name = "t_human")//Defaults to the entity name.
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private String name;
    private String email;
    private String phone;
    private int age;
}
