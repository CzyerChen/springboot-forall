package com.mybatis.self;


import com.mybatis.domain.Person1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelfExecutor  implements Executor {
    private SelfConfiguration configuration = new SelfConfiguration();

    @Override
    public <T> T query(String statement, Object parameter) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,parameter.toString());
            resultSet = preparedStatement.executeQuery();

            Person1 person1 = new Person1();

            while(resultSet.next()){
                person1.setPid(resultSet.getInt(1));
                person1.setPname(resultSet.getString(2));
                person1.setSex(resultSet.getString(3));
                person1.setPhone(resultSet.getString(4));
            }
            return (T)person1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection getConnection(){
        try{
            Connection connection = configuration.build("SqlMapConfig.xml");
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
