package com.forjpa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 8:49
 */
@Entity
@Table(name = "t_customer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid",unique = true,nullable = false)
    private int cid;
    @Column(name = "cname",nullable = false,length = 255)
    private String cname;
    @Column(name = "sex",nullable = false)
    private int sex;
    @Column(name = "phone",nullable = false)
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)//Customer是关系的维护端，当删除 customer，会级联删除 address
    @JoinColumn(name = "address_id",referencedColumnName = "aid")//customer中的address_id字段参考address表中的aid字段
    private Address address;
}
