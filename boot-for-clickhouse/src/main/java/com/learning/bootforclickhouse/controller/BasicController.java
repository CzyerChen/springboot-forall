/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.learning.bootforclickhouse.controller;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.learning.bootforclickhouse.domain.MyFirstTable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BasicController {
    // http://127.0.0.1:8080/hello?name=lisi
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }

    // http://127.0.0.1:8080/html
    @RequestMapping("/html")
    public String html() {
        return "index.html";
    }

    @GetMapping("/batchQuery")
    public String batchQuery() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String url = clickhouseUrl; // use http protocol and port 8123 by default
                    Properties properties = new Properties();
                    List<HitsAll> list = new ArrayList<>();
                    try {
                        ClickHouseDataSource dataSource = new ClickHouseDataSource(url, properties);
                        try (Connection conn = dataSource.getConnection("default", "");
                             Statement stmt = conn.createStatement()) {
                            ResultSet rs = stmt.executeQuery("select * from tutorial.hits_all ");
                            while (rs.next()) {
                                int id = rs.getInt("id");
                                HitsAll hitsAll = new HitsAll();
                                hitsAll.setId(id);
                                list.add(hitsAll);
                            }
                        }
                        System.out.println("第" + finalI + "次:" + list.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return "index.html";
    }
}
