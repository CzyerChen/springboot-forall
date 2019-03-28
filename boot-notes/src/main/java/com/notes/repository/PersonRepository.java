package com.notes.repository;

import com.notes.domain.Person;
import org.springframework.data.repository.CrudRepository;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 28 19:02
 */
public interface PersonRepository  extends CrudRepository<Person,Integer> {
}
