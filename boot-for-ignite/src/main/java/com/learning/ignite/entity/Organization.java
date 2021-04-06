/**
 * Author:   claire
 * Date:    2021-04-06 - 10:10
 * Description: 组织
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-06 - 10:10          V1.17.0          组织
 */
package com.learning.ignite.entity;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 功能简述 
 * 〈组织〉
 *
 * @author claire
 * @date 2021-04-06 - 10:10
 * @since 1.1.0
 */
public class Organization implements Serializable {
    /** ID generator. */
    private static final AtomicLong IDGEN = new AtomicLong();

    /** Organization ID (indexed). */
    @QuerySqlField(index = true)
    private long id;

    /** Organization name (indexed). */
    @QuerySqlField(index = true)
    private String name;

    /**
     * Create organization.
     *
     * @param name Organization name.
     */
    public Organization(String name) {
        id = IDGEN.incrementAndGet();

        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return "Organization " + "[id=" + id + ", name=" + name + ']';
    }

    /** {@inheritDoc} */
    @Override public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Organization))
            return false;

        Organization that = (Organization)o;

        return id == that.id && name.equals(that.name);
    }

    /** {@inheritDoc} */
    @Override public int hashCode() {
        int res = (int)(id ^ (id >>> 32));

        res = 31 * res + name.hashCode();

        return res;
    }
}
