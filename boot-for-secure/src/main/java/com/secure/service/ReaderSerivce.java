package com.secure.service;

import com.secure.domain.Reader;
import com.secure.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 03 16:44
 */
public class ReaderSerivce implements UserDetailsService {

    @Autowired
    private ReaderRepository readerRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Reader reader = readerRepository.findByUsername(s);
        if(reader == null){
            throw  new UsernameNotFoundException("用户不存在");
        }
        return reader;
    }
}
