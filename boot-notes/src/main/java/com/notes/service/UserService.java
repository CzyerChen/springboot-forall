package com.notes.service;

import com.notes.domain.Phone;
import com.notes.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 19 16:26
 */
@Service
public class UserService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save() {
        User user = new User();
        Phone phone = new Phone();
        phone.setNumber("11111111111");
        user.setPhone(phone);
        user.setId(1);

        phone.isValidNumber();
    }
}
