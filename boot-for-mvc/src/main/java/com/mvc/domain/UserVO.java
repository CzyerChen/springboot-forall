package com.mvc.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 03 15:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "User")
public class UserVO {
    @JacksonXmlProperty(localName = "name")
    private String username;
    @JacksonXmlProperty(localName = "age")
    private int age;
}
