package com.forjpa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 14:50
 */
@Entity
@Table(name = "t_human")//Defaults to the entity name.
@Access(AccessType.FIELD)
@NamedNativeQuery(name = "Human.testq",query = "select name,phone from t_human where name like ?1 ",resultSetMapping = "HumanEntry")
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private String name;
    private int emailId;
    @Transient
    private String mobile;
    private int age;
    private String address;
    private Date createTime;

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

    public int getEmailId() {
        return emailId;
    }

    public void setEmailId(int emailId) {
        this.emailId = emailId;
    }

    @Access(AccessType.PROPERTY)
    @Column(name = "phone")
    public String getPhoneFromDb() {
        return mobile;
    }

    public void setPhoneFromDb(String phone) {
        this.mobile = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
