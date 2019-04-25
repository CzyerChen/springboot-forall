package com.forjpa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 25 20:11
 */
@Entity // 标识实体类
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class User {
    //标识主键，指定主键生成策略
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false)//有很多标识的注解，这边也不一一描述作用，可以看源码的时候再说
    private int id;
    @Column(name = "name")
    private String name;
    private int sex;
    private String phone;
    private String address;
    private String email;
}
