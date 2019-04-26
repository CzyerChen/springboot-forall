package com.jpatest;

import com.forjpa.JpaTestApplication;
import com.forjpa.domain.*;
import com.forjpa.repository.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 25 20:18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JpaTestApplication.class)
@WebAppConfiguration
public class ApplicationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PeopleRepository peopleRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private PermissonRepository permissonRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private HumanRepository humanRepository;

    @Test
    public void test1() {
        /*User user = new User(0, "claire", 2, "11111111111", "hz", "xxxx@gmail.com");
        userRepository.save(user);*/
    }

    @Test
    public void test2() {
        Customer claire = customerRepository.findByCname("claire");
        Address address = claire.getAddress();
    }

    @Test
    public void test3() {
        People claire = peopleRepository.findByPname("claire");
        Address address = claire.getAddress();
    }

    @Test
    public void test4() {
        peopleRepository.deleteById(2);
    }

    @Test
    public void test5() {
        customerRepository.deleteById(1);
    }

    @Test
    public void test6() {
        Store achuor = storeRepository.findBySname("Auchor");
        List<Product> product = achuor.getProduct();
        int size = product.size();
    }

    @Test
    public void test7() {
        storeRepository.deleteById(1);
    }

    @Test
    public void test8() {
        Permisson permisson = new Permisson();
        permisson.setTab("add");
        permissonRepository.save(permisson);

    }
    @Test
    public void test9(){
        User user= new User();
        user.setName("claire1");
        user.setAddress("baker street");
        user.setEmail("xxx@gmail.com");
        List<Permisson> list = new ArrayList<Permisson>();
        list.add(permissonRepository.findById(1).get());
        user.setPermissonList(list);
        userRepository.save(user);
    }

    @Test
    public void test10(){
        List<Permisson> add = permissonRepository.findByTab("add");
    }
    @Test
    public void test11(){
        User claire1 = userRepository.findByName("claire1");
        List<Permisson> permissonList = claire1.getPermissonList();
        int size = permissonList.size();
    }
    @Test
    public void test12(){
        permissonRepository.deleteById(2);
    }

    @Test
    public void test13(){
        userRepository.deleteById(7);
    }

    @Test
    public void test14(){
        Product milk = productRepository.findByPname("milk");
        productRepository.delete(milk);//外键关系，删不了，需要把级联权限改成游离就可以，CascadeType.DETACH
    }

    @Test
    public void  test15(){
        Human humanWithMail = humanRepository.findByEmailMatch("@gmail");
        Assert.assertNotNull(humanWithMail);
    }

    @Test
    public void  test16(){
        Human human = humanRepository.findHumanByName("claire");
        Assert.assertNotNull(human);
    }


    @Test
    public void  test17(){
        List<Human> humans = humanRepository.findByName("claire");
        Assert.assertNotNull(humans);
    }

    @Test
    public void test18(){
        List<Human> humans = humanRepository.findByName1("claire");
        Assert.assertNotNull(humans);
    }

    @Test
    public  void test19(){
        List<Human> humanByMail = humanRepository.getHumanByMail("@gmail");
        Assert.assertNotNull(humanByMail);
    }
    @Test
    public void test20(){
        List<Human> humans = humanRepository.getHuman("claire", "@gmail", 25);
        Assert.assertNotNull(humans);
    }

}

