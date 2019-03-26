package com.swaggerui.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 05 16:18
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
public class UserPO {
    @Id
    private int id;
    @NotBlank
    private String name;
    private int sex;
    @NotNull
    private String phone;
    private String address;
    private String email;

}
