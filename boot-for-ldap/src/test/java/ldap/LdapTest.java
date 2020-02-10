package ldap;

import boot.ldap.LdapApplication;
import boot.ldap.dao.TestDAO;
import boot.ldap.domain.Person;
import boot.ldap.repository.PersonRepository;
import com.sun.jndi.ldap.LdapClient;
import javafx.beans.property.StringProperty;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;
import java.util.Hashtable;
import java.util.List;


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
    @Autowired
    private LdapTemplate ldapTemplate;

    @Test
    public void testLdapLogin() {
        Person person = personRepository.findByCommonName("ry_user_01");
        //Person person = personRepository.findByUid("10000");
        person.setUserPassword("tttt");
         Person person1 = personRepository.save(person);
        System.out.println(person.toString());
        Assert.assertNotNull(person);
    }

    @Test
    public void testUpdate() throws InvalidNameException {
        Person person = personRepository.findByCommonName("ry_user_01");
        ldapTemplate.unbind(new LdapName("uid=ry_user_01"));
        person.setUserPassword("qqqq");
        ldapTemplate.bind(buildDn("ry_user_01"),null,buildAttributes(person));
    }

    /**
     * 构建用户DN
     *
     * @param userName
     * @return
     * @throws InvalidNameException
     */
    private Name buildDn(String userName) throws InvalidNameException {
        return new LdapName("uid=" + userName);
    }

    @Test
    public void testDelete() throws InvalidNameException {
        personRepository.deleteById(new LdapName("uid=test6"));
    }

    @Test
    public void testFindByDn() {
        List<Person> person = testDAO.findByUid("10000");
        System.out.println(person.size());
        Assert.assertNotNull(person);
    }


    @Test
    public void testSave() {
        Person person = new Person();
        person.setUid("uid=sk");
        person.setCommonName("sk");
        person.setSuerName("sksn");
        person.setUserPassword("123456");
        //testDAO.create(person);
        personRepository.save(person);
    }

    @Test
    public void test2() {
        String userDomain = String.format("%s@example.com", "test111");
        DirContext dirContext = null;
        try {
            ldapTemplate.getContextSource().getContext(userDomain, "123456");
        } catch (Exception e) {

        }
    }

    @Test
    public void test() {
       /* @Override
        public boolean authenticate(String userName, String password) {
*/
        //String userDomainName = getDnForUser(userName);

        String userDomainName = "uid=" + "ry_user_01" + ",cn=users,cn=accounts," + "dc=fibodt,dc=com";

        DirContext ctx = null;

        try {
            ctx = ldapTemplate.getContextSource().getContext(userDomainName, "qqqq");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LdapUtils.closeContext(ctx);
        }

    }

    @Test
    public void testAuth(){
        EqualsFilter filter = new EqualsFilter("uid", "chenzy");
        ldapTemplate.authenticate("",filter.toString(),"czy123456");
    }

    @Test
    public void test1() throws InvalidNameException {
        Person ldapUser = new Person();
        ldapUser.setDn(new LdapName("uid=test111"));
        ldapUser.setCommonName("test111");
        ldapUser.setSuerName("chen");
        ldapUser.setUid("test111");
        ldapUser.setUserPassword("1");
        //开通AD域
        try {
            Attributes attrs = buildAttributes(ldapUser);

           /* String pwd = ldapUser.getUserPassword();
            byte[] unicodePassword = null;
            try {
                unicodePassword = pwd.getBytes("UTF-16LE");
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            attrs.put("password", ldapUser.getUserPassword());
            ldapTemplate.bind(ldapUser.getDn(), null, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void create() {
        Person ldapUser = new Person();
        ldapUser.setCommonName("test333333");
        ldapUser.setSuerName("test333333");
        ldapUser.setMail("test333333@FIBODT.COM");
        ldapUser.setUid("test333333");
        ldapUser.setUserPassword("test333333");

        Name dn = buildDn(ldapUser);
        ldapTemplate.bind(dn, null, buildAttributes(ldapUser));
    }

    public Name buildDn(Person user) {
        return LdapNameBuilder.newInstance("cn=users,cn=accounts,dc=fibodt,dc=com")
                .add("uid", user.getCommonName())
                .build();
    }


    private Attributes buildAttributes(Person adUser) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectClass");
        ocAttr.add("top");
        ocAttr.add("person");
        ocAttr.add("organizationalPerson");
        ocAttr.add("inetOrgPerson");
        attrs.put(ocAttr);
        attrs.put("cn", adUser.getCommonName());
        attrs.put("sn", adUser.getSuerName());
        attrs.put("userPassword",adUser.getUserPassword());
        return attrs;
    }


    @Test
    public void lookup() {
        Object lookup = ldapTemplate.lookup("uid=test8");
        Assert.assertNotNull(lookup);
    }

/**
 * 这个不行
 */
    @Test
    public void testLDAPClient() throws InvalidNameException {
        Person person = new Person();
        person.setCommonName("test7");
        person.setSuerName("test7");
        person.setMail("test7@fibodt.com");
        person.setUid("test7");
        person.setDn(buildDn(person));
        personRepository.save(person);

    }

    /**
     * 可以
     * @throws InvalidNameException
     */
    @Test
    public void test11() throws InvalidNameException {
        Person person = new Person();
        person.setCommonName("test8");
        person.setSuerName("test8");
        person.setMail("test8@fibodt.com");
        person.setUid("test8");
        Name dn = new LdapName("uid=test8");
        DirContextAdapter context = new DirContextAdapter(dn);

        Attribute oc = new BasicAttribute("objectClass");
        oc.add("top");
        oc.add("person");
        oc.add("organizationalPerson");
        oc.add("inetOrgPerson");
        Attribute cn = new BasicAttribute("cn", "test8");
        Attribute sn = new BasicAttribute("sn", "test8");
        final Attribute userPassword = new BasicAttribute("userpassword", "test8");

        context.setAttribute(oc);
        context.setAttribute(cn);
        context.setAttribute(sn);
        context.setAttribute(userPassword);

       /* context.setAttributeValues("objectclass", new String[]{"top", "person","organizationalPerson","inetOrgPerson"});
        context.setAttributeValue("cn", person.getCommonName());
        context.setAttributeValue("sn", person.getSuerName());
        context.setAttributeValue("mail", person.getMail());
        context.setAttributeValue("userPassword",person.getUserPassword());*/

        ldapTemplate.bind(context);
    }



   /* protected Name buildDn(Person person) {
        return buildDn(person.getCommonName(), person.getCompany(), person.getCountry());
    }

    protected Name buildDn(String fullname, String company, String country) {
        return LdapNameBuilder.newInstance()
                .add("c", country)
                .add("ou", company)
                .add("cn", fullname)
                .build();
    }
*/

    /**
     * 可以
     */
    @Test
    public void testCreateUser() {
        InitialDirContext context;
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://192.168.7.10");
        env.put(Context.SECURITY_PRINCIPAL, "uid=admin,cn=users,cn=accounts,dc=fibodt,dc=com");
        env.put(Context.SECURITY_CREDENTIALS, "admin123");
        env.put(Context.REFERRAL, "follow");
        env.put("com.sun.jndi.ldap.read.timeout", "10000");

        try {
            InitialDirContext initialDirContext = new InitialDirContext(env);
            context = initialDirContext;
            String entryDN = "uid=test4,cn=users,cn=accounts,dc=fibodt,dc=com";
            Attribute cn = new BasicAttribute("cn", "test4");
            Attribute sn = new BasicAttribute("sn", "test4");
            final Attribute userPassword = new BasicAttribute("userpassword", "test4");

//            Attribute mail = new BasicAttribute("mail", "newuser@foo.com");
//            Attribute phone = new BasicAttribute("telephoneNumber", "+1 222 3334444");
            Attribute oc = new BasicAttribute("objectClass");
            oc.add("top");
            oc.add("person");
            oc.add("organizationalPerson");
            oc.add("inetOrgPerson");

            BasicAttributes entry = new BasicAttributes();
            entry.put(cn);
            entry.put(oc);
            entry.put(sn);
            entry.put(userPassword);
            context.createSubcontext(entryDN, entry);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可以
     * @throws InvalidNameException
     */
        @Test
        public void create11 () throws InvalidNameException {
            Person person = new Person();
            person.setCommonName("test7");
            person.setSuerName("test7");
            person.setMail("test7@fibodt.com");
            person.setUid("test7");
            //Name dn = buildDn(person);
            ldapTemplate.bind(new LdapName("uid=test7"), null, buildAttributes1(person));
        }

        private Attributes buildAttributes1 (Person p){
            Attributes attrs = new BasicAttributes();
            BasicAttribute ocattr = new BasicAttribute("objectclass");
            ocattr.add("top");
            ocattr.add("person");
            ocattr.add("organizationalPerson");
            ocattr.add("inetOrgPerson");
            attrs.put(ocattr);
            attrs.put("cn", p.getCommonName());
            attrs.put("sn", p.getSuerName());
            return attrs;
        }




}
