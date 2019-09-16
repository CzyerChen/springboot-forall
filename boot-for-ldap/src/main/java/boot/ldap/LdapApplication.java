package boot.ldap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;

/**
 * Created by claire on 2019-09-12 - 14:22
 **/
@SpringBootApplication
@EnableLdapRepositories(basePackages = {"boot.ldap.repository"})
public class LdapApplication {

    public static void main(String[] args){
        SpringApplication.run(LdapApplication.class,args);
    }
}
