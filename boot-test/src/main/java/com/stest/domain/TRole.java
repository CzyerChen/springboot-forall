package com.stest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 16:24
 */
@Entity
@Table(name ="t_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String rolename;
    @OneToMany(mappedBy = "role", fetch=FetchType.EAGER)
    private List<TPermission> permissionList;// 一个角色对应多个权限
    @ManyToMany
    @JoinTable(name = "t_user_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = "user_id") })
    private List<TUser> userList;// 一个角色对应多个用户


    @Transient
    public List<String> getPermissionsName() {
        List<String> list = new ArrayList<String>();
        List<TPermission> perlist = getPermissionList();
        for (TPermission per : perlist) {
            list.add(per.getPermissionname());
        }
        return list;
    }
}
