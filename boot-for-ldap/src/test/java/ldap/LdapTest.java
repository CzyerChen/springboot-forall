package ldap;

import boot.ldap.LdapApplication;
import boot.ldap.dao.TestDAO;
import boot.ldap.domain.Person;
import boot.ldap.repository.PersonRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by claire on 2019-09-12 - 14:55
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LdapApplication.class)
public class LdapTest {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TestDAO testDAO;

    @Test
    public void testLdapLogin(){
        Person person = personRepository.findByCommonName("claire.chen");
        //Person person = personRepository.findByUid("10000");
        System.out.println(person.toString());
        Assert.assertNotNull(person);
    }

    @Test
    public void testFindByDn(){
        Person person = testDAO.findByUid("10000");
        System.out.println(person.toString());
        Assert.assertNotNull(person);
    }

}
