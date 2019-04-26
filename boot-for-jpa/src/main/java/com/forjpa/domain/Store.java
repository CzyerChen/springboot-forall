package com.forjpa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 9:51
 */
@Entity
@Table(name = "t_store")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sid;
    private String sname;
    private String address;
    @OneToMany(mappedBy = "store",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Product> product;
}
