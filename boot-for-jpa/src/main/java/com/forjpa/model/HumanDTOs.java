package com.forjpa.model;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by claire on 2019-07-06 - 16:39
 **/
public interface HumanDTOs {
    @Value("#{target.name + ' ' + target.age}")
    String getFull();

    String  getName();

    Integer  getAge();
}
