package com.mybatis;

import net.minidev.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
public class CommonJdbcTest {

    @Test
    public void testCommonJdbc() {
        List<Map<String,String>> list  = new ArrayList<Map<String,String>>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/testdb", "root", "09876");
            preparedStatement = connection.prepareStatement("select * from t_people where pid =1");
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String,String> map = new HashMap<String, String>();
                for (int i = 0; i < columnCount; i++) {
                    String name = metaData.getColumnName(i+1);
                    map.put(name,resultSet.getString(name));
                }
                list.add(map);
            }
            System.out.println(JSONArray.toJSONString(list));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet =null;
                }
                if(preparedStatement != null){
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if(connection != null){
                    connection.close();
                    connection = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
