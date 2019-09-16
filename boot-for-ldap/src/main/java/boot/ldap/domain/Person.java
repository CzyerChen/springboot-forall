package boot.ldap.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

/**
 * Created by claire on 2019-09-12 - 14:24
 **/
@Data
@ToString
@Entry(base = "", objectClasses = "inetOrgPerson")//it's wrong when I set base ="ou=People,dc=demo,dc=com"
public class Person {
    @Id
    @JsonIgnore
    private Name id;
    @DnAttribute(value = "uid", index = 3)
    private String uid;
    @Attribute(name = "cn")
    private String commonName;
    @Attribute(name = "sn")
    private String suerName;
    private String userPassword;
}

