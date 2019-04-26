package com.forjpa.repository;

import com.forjpa.domain.Human;

import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 15:25
 */
public interface HumanRepositoryCustom {
    List<Human> getHumanByMail(String mail);

    List<Human> getHuman(String name ,String email,int maxAge);
}
