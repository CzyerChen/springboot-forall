package boot.ldap.repository;

import boot.ldap.domain.Person;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.data.repository.CrudRepository;

import javax.naming.Name;

/**
 * Created by claire on 2019-09-12 - 14:33
 **/
public interface PersonRepository  extends CrudRepository<Person,Name> {

    Person findByUid(String uid);

    Person findByCommonName(String commonName);
}
