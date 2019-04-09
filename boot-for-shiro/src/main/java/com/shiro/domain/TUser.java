package com.shiro.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 16:24
 */
@Entity
@Table(name = "t_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "t_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private List<TRole> roleList;


    @Transient
    public Set<String> getRolesName() {
        List<TRole> roles = getRoleList();
        Set<String> set = new HashSet<String>();
        for (TRole role : roles) {
            set.add(role.getRolename());
        }
        return set;
    }
}
