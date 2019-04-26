package com.forjpa.domain;

import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 8:54
 */
@Entity
@Table(name = "t_address")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aid",unique = true,nullable = false)
    private int aid;
    @Column(name = "country",length = 64)
    private String country;
    @Column(name = "province",length = 64)
    private String province;
    @Column(name = "city",length = 64)
    private String city;
    @Column(name = "area",length = 128)
    private String area;
    @Column(name = "detail")
    private String detail;


   /* @OneToOne(mappedBy = "address",cascade = {CascadeType.MERGE,CascadeType.REFRESH},optional = false)
    private Customer customer;*/
}
