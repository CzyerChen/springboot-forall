package com.notes.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 28 17:31
 */
@JacksonXmlRootElement(localName = "Person")
//@Entry(base = "ou=people,dc=xxx,dc=com",objectClasses = "inetOrgPerson")
public class Person {
    @JacksonXmlProperty(localName = "id")
    //@Id
    private int id;




    //@DnAttribute(value = "uid",index = 3)
    private String uid;

    @JacksonXmlProperty(localName = "name")
    //@Attribute(name = "cn")
    private String name;

    //@Attribute(name = "sn")
    private String suerName;

    private String userPassword;
    @JacksonXmlProperty(localName = "age")
    private int age;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSuerName() {
        return suerName;
    }

    public void setSuerName(String suerName) {
        this.suerName = suerName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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
