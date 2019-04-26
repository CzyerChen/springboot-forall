package com.forjpa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 9:54
 */
@Entity
@Table(name = "t_product")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int pid;
    private String pname;
    private double price;
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH},optional = false)//optional=false,表示store不能为空
    @JoinColumn(name = "s_id")
    private Store store;


}
