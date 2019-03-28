package com.notes;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 25 17:22
 */
@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = "com.notes")
public class NotesApplication {

    public  static void main(String[] args){

    }
}
