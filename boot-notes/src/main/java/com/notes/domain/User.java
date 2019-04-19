package com.notes.domain;

import com.sun.javafx.beans.IDProperty;

import javax.annotation.sql.DataSourceDefinition;
import javax.persistence.*;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 19 16:18
 */
@Entity
@Table(name = "test_table")
public class User {

    @Id
    private int id;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="number", column=@Column(name="手机号字段名", columnDefinition="varchar(20)"))
    })
    private Phone phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}
