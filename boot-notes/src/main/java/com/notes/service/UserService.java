package com.notes.service;

import com.google.common.collect.Maps;
import com.notes.domain.Person;
import com.notes.domain.Phone;
import com.notes.domain.ReflectTest;
import com.notes.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 19 16:26
 */
@Service
public class UserService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save() {
        User user = new User();
        Phone phone = new Phone();
        phone.setNumber("11111111111");
        user.setPhone(phone);
        user.setId(1);

        phone.isValidNumber();
    }

    public static void main(String[] args) {
      /*  ArrayList<String> strings = new ArrayList<>();
        strings.stream().forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {

            }
        });

        List<Integer> collect = strings.stream().map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return null;
            }
        }).collect(Collectors.toList());


        Map<String, Consumer<ReflectTest>> functionMap = Maps.newHashMap();
        functionMap.put("one", ReflectTest::methodOne);
        functionMap.put("two", ReflectTest::methodTwo);
        functionMap.put("three", ReflectTest::methodThree);
        functionMap.put("four", ReflectTest::methodThree);
        functionMap.get("one").accept(new ReflectTest());*/


        Map<String, Function<ReflectTest,String>> map = Maps.newHashMap();
        map.put("one", ReflectTest::methodOne);
        map.put("two", ReflectTest::methodTwo);
        map.put("three", ReflectTest::methodThree);
        map.put("four", ReflectTest::methodThree);
        String apply = map.get("one").apply(new ReflectTest());
        System.out.println(apply);
/*
        Map<String, Function<String,Integer>> functionMap = Maps.newHashMap();
        functionMap.put("one", ReflectTest::methodOne);
        functionMap.put("two", ReflectTest::methodTwo);
        functionMap.put("three", ReflectTest::methodThree);
        functionMap.put("four", ReflectTest::methodThree);
        functionMap.get(input).accept(new ReflectTest());*/
    }
}
