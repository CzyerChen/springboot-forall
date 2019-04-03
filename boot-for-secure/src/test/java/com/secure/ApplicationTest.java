package com.secure;

import com.secure.domain.Reader;
import com.secure.repository.ReaderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 03 16:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SecureApplication.class)
@WebAppConfiguration
public class ApplicationTest {
    @Autowired
    private ReaderRepository readerRepository;

    @Test
    public void testReader(){
        Optional<Reader> czy = readerRepository.findById("czy");
        Assert.assertNotNull(czy.get());
    }
}
