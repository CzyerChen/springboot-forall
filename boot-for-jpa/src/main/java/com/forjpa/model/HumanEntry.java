package com.forjpa.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by claire on 2019-07-06 - 10:14
 **/
@Data
@Entity
@SqlResultSetMapping(name = "HumanEntry",
entities = {@EntityResult(entityClass = HumanEntry.class)})
public class HumanEntry {
    @Id
    private String name;
    private String phone;
}
