/**
 * Author:   claire
 * Date:    2021-04-05 - 14:00
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-05 - 14:00          V1.17.0
 */
package com.learning.ignite.entity;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2021-04-05 - 14:00
 * @since 1.1.0
 */
public class Person implements Serializable {
    /** ID generator. */
    private static final AtomicLong IDGEN = new AtomicLong();

    /** Person ID (indexed). */
    @QuerySqlField(index = true)
    private long id;

    /** Organization ID (indexed). */
    @QuerySqlField(index = true)
    private long orgId;

    /** First name (not-indexed). */
    @QuerySqlField
    private String firstName;

    /** Last name (not indexed). */
    @QuerySqlField
    private String lastName;

    /** Resume text (create LUCENE-based TEXT index for this field). */
    @QueryTextField
    private String resume;

    /** Salary (create non-unique SQL index for this field). */
    @QuerySqlField
    private double salary;

    /** Affinity-aware key. */
    private transient PersonKey key;

    /**
     * Constructs person record.
     *
     * @param org Organization.
     * @param firstName First name.
     * @param lastName Last name.
     * @param salary Salary.
     * @param resume Resume text.
     */
    public Person(Organization org, String firstName, String lastName, double salary, String resume) {
        // Generate unique ID for this person.
        id = IDGEN.incrementAndGet();

        orgId = org.getId();

        this.firstName = firstName;
        this.lastName = lastName;
        this.resume = resume;
        this.salary = salary;
    }

    /**
     * @param p Person to copy.
     */
    public Person(Person p) {
        id = p.getId();
        orgId = p.getOrganizationId();
        firstName = p.getFirstName();
        lastName = p.getLastName();
        resume = p.getResume();
        salary = p.getSalary();
    }

    /**
     * @return Affinity-aware person key.
     */
    public PersonKey key() {
        if (key == null) {
            key = new PersonKey(id, orgId);
        }

        return key;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrganizationId() {
        return orgId;
    }

    public void setOrganizationId(long orgId) {
        this.orgId = orgId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return "Person [firstName=" + firstName +
                ", id=" + id +
                ", orgId=" + orgId +
                ", lastName=" + lastName +
                ", resume=" + resume +
                ", salary=" + salary + ']';
    }
}