package com.forjpa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by claire on 2019-07-06 - 12:01
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HumanDTO {
    private String name;
    private int age;
}
