/*
package boot.ldap.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;

*/

package boot.ldap.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

/**
 * Created by claire on 2019-09-12 - 14:24
 **//*

@Data
@ToString
@Entry(base = "ou=People,dc=demo,dc=com", objectClasses = "inetOrgPerson")//inetOrgPerson//it's wrong when I set base ="ou=People,dc=demo,dc=com"
public class Person {
    @Id
    @JsonIgnore
    private Name id;
    @DnAttribute(value = "uid")
    private String uid;
    @Attribute(name = "cn")
    private String commonName;
    @Attribute(name = "sn")
    private String suerName;
    @Attribute(name="userPassword")
    private String userPassword;
   */
/*
    @Id
    @JsonIgnore
    private Name dn;

    @DnAttribute(value = "uid")
    private String uid;

    @Attribute(name="cn")
    private String commonName;

    @Attribute(name="sn")
    private String suerName;

    @Attribute(name="userPassword")
    private String userPassword;

    public Person(String cn) {
        Name dn = LdapNameBuilder.newInstance()
                .add("dc","com")
                .add("dc","fibodt")
                .add("cn","accounts")
                .add("cn","users")
                .add("cn", cn)
                .build();
        this.dn = dn;
    }
    public Person(){}

    public Name getDn() {
        return dn;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getSuerName() {
        return suerName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public void setCommonName(String cn) {
        this.commonName = cn;
        if(this.dn==null){
            Name dn = LdapNameBuilder.newInstance()
                    .add("dc","com")
                    .add("dc","fibodt")
                    .add("cn","accounts")
                    .add("cn","users")
                    .add("cn",cn)
                    .build();
            this.dn = dn;
        }
    }

    public void setSuerName(String sn) {
        this.suerName = sn;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "Person{" +
                "dn=" + dn.toString() +
                ", cn='" + commonName + '\'' +
                ", sn='" + suerName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }*//*

}

*/
@Data
@ToString
@Entry(base = "", objectClasses = "inetorgperson")//it's wrong when I set base ="ou=People,dc=demo,dc=com"
public class Person {
    @Id
    @JsonIgnore
    private Name dn;
    @DnAttribute(value = "uid")
    private String uid;
    @Attribute(name = "cn")
    private String commonName;
    @Attribute(name = "sn")
    private String suerName;
    private String userPassword;
    @Attribute(name = "mail")
    private String mail;
}