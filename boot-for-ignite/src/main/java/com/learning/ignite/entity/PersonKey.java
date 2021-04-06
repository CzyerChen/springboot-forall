/**
 * Author:   claire
 * Date:    2021-04-06 - 10:11
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-06 - 10:11          V1.17.0
 */
package com.learning.ignite.entity;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

import java.io.Serializable;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2021-04-06 - 10:11
 * @since 1.1.0
 */
public class PersonKey implements Serializable {
    /** Person ID. */
    private final long id;

    /** Organization ID. */
    @AffinityKeyMapped
    private final long orgId;

    /**
     * @param id Person ID.
     * @param orgId Organization ID.
     */
    public PersonKey(long id, long orgId) {
        this.id = id;
        this.orgId = orgId;
    }

    /**
     * @return Person ID.
     */
    public long getId() {
        return id;
    }

    /**
     * @return Organization ID.
     */
    public long getOrganizationId() {
        return orgId;
    }

    /** {@inheritDoc} */
    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PersonKey)) {
            return false;
        }

        PersonKey personKey = (PersonKey)o;

        return id == personKey.id && orgId == personKey.orgId;
    }

    /** {@inheritDoc} */
    @Override  public int hashCode() {
        int res = (int)(id ^ (id >>> 32));

        res = 31 * res + (int)(orgId ^ (orgId >>> 32));

        return res;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return Long.toString(id);
    }
}
