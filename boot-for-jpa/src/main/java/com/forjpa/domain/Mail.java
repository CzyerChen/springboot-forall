package com.forjpa.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by claire on 2019-07-05 - 20:26
 **/
@Entity
@Table(name = "t_mail")
public class Mail {
    private int id;
    private String detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
