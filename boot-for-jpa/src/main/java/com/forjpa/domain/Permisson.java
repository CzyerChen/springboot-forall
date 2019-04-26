package com.forjpa.domain;

import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 10:13
 */
@Entity // 标识实体类
@Table(name = "permisson")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Permisson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tab;
    @ManyToMany(mappedBy = "permissonList",fetch = FetchType.LAZY)
    private List<User> userList;
}
