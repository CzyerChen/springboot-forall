/**
 * Author:   claire
 * Date:    2021-04-06 - 15:19
 * Description: 测试类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-06 - 15:19          V1.17.0          测试类
 */
package com.learning.easypoi;

import com.learning.easypoi.entity.MarketingDetailsExcel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

/**
 * 功能简述 
 * 〈测试类〉
 *
 * @author claire
 * @date 2021-04-06 - 15:19
 * @since 1.1.0
 */
@SpringBootTest(classes = EasypoiApplication.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ExcelTest {

    @Test
    public void testSingletonList(){
        List<MarketingDetailsExcel> records = Collections.singletonList(new MarketingDetailsExcel());
        if(Objects.nonNull(records)){
            Iterator<MarketingDetailsExcel> iterator = records.iterator();
            while (iterator.hasNext()){
                iterator.next();
                iterator.remove();
            }
        }
    }

    @Test
    public void testArrayList(){
        List<MarketingDetailsExcel> records = new ArrayList<>();
        records.add(new MarketingDetailsExcel());
        if(Objects.nonNull(records)){
            Iterator<MarketingDetailsExcel> iterator = records.iterator();
            while (iterator.hasNext()){
                iterator.next();
                iterator.remove();
            }
        }
    }

    @Test
    public void testArraysAsList(){
        List<MarketingDetailsExcel> records = Arrays.asList(new MarketingDetailsExcel());
        if(Objects.nonNull(records)){
            Iterator<MarketingDetailsExcel> iterator = records.iterator();
            while (iterator.hasNext()){
                iterator.next();
                iterator.remove();
            }
        }
    }

}
