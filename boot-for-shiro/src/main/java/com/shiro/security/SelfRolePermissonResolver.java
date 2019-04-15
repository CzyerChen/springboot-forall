package com.shiro.security;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Arrays;
import java.util.Collection;

/**
 * Desciption 根据角色字符串来解析得到权限集合
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 09 18:27
 */
public class SelfRolePermissonResolver  implements RolePermissionResolver {
    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        if("admin1".equals(roleString)){
            return Arrays.asList((Permission)new WildcardPermission("user:*"));
        }
        return null;
    }
}
