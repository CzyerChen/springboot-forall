package com.notes.domain;

import javax.persistence.*;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 19 16:20
 */
@Embeddable
public class Phone {

    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isValidNumber(){
        if(11 == this.number.length()){
            return true;
        }
        return false;
    }

}
