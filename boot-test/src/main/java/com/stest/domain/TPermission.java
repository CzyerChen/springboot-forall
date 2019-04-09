package com.stest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 16:25
 */
@Entity
@Table(name = "t_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String permissionname;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private TRole role;// 一个权限对应一个角色

}
