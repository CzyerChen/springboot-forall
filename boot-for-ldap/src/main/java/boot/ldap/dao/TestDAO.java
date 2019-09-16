package boot.ldap.dao;

import boot.ldap.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * Created by claire on 2019-09-12 - 17:02
 **/
@Service
public class TestDAO {
    @Autowired
    private LdapTemplate ldapTemplate;


    public Person findByCn(String cn){
        return ldapTemplate.findOne(query().where("cn").is(cn), Person.class);
    }

    public Person findByUid(String uid){
        return ldapTemplate.findOne(query().where("uid").is(uid), Person.class);
    }
}
