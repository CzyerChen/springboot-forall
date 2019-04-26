package com.forjpa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 9:19
 */
@Entity
@Table(name = "t_people")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class People {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid",unique = true,nullable = false)
    private int pid;
    @Column(name = "pname",nullable = false,length = 255)
    private String pname;
    @Column(name = "sex",nullable = false)
    private int sex;
    @Column(name = "phone",nullable = false)
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)//people是关系的维护端，当删除 people，会级联删除 address
    @JoinTable(name = "t_people_address",joinColumns = @JoinColumn(name = "pid"),inverseJoinColumns = @JoinColumn(name = "aid"))
    private Address address;
}
