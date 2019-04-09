package com.shiro.security;

import com.shiro.domain.TPermission;
import com.shiro.domain.TRole;
import com.shiro.domain.TUser;
import com.shiro.repository.TRoleRepository;
import com.shiro.repository.TUserRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 16:32
 */
public class SelfRealm extends AuthorizingRealm {
    @Autowired
    private TUserRepository userRepository;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String username = (String)authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());

        String passwordByUsername = userRepository.findPasswordByUsername(username);
        if(passwordByUsername == null){
            throw  new UnknownAccountException("用户不存在");
        }else  if(!passwordByUsername.equals(password)){
            throw  new IncorrectCredentialsException("用户认证失败");
        }
        return new SimpleAuthenticationInfo(username,password,getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String)SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        TUser byUsername = userRepository.findByUsername(username);
        Set<String> roles;
        Set<String> permissions = new HashSet<>();
        if(byUsername == null) {
            throw new UnknownAccountException("用户权限不存在");
        }else if(byUsername.getRoleList() == null){
            throw  new UnknownAccountException("用户权限不存在");
        }else{
            roles = byUsername.getRoleList().stream().map(TRole::getRolename).collect(Collectors.toSet());
            byUsername.getRoleList().forEach( r -> permissions.addAll(r.getPermissionList().stream().map(TPermission::getPermissionname).collect(Collectors.toSet())));
        }
        if(!roles.isEmpty()){
            simpleAuthorizationInfo.setRoles(roles);
        }
        if(!permissions.isEmpty()){
            simpleAuthorizationInfo.setStringPermissions(permissions);
        }
        return simpleAuthorizationInfo;
    }
}
