package com.mybatis.self;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelfConfiguration {

    private static ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    //---------------获取基础配置文件，获取数据库链接----------------------//
    public Connection build(String resource){
        try{
            InputStream stream = classLoader.getResourceAsStream(resource);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element rootElement = document.getRootElement();
           //解析内容
            return evalDataSource(rootElement);
        }catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("error occured while evaling xml " + resource);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Connection evalDataSource(Element node) throws ClassNotFoundException {
        if(!node.getName().equals("database")){
            throw new RuntimeException("miss database element");
        }

        String driveClass = null;
        String url = null;
        String username = null;
        String password = null;

        for(Object item : node.elements("property")){
            Element i = (Element)item;
            String value = getValue(i);
            String name = i.attributeValue("name");
            if(name == null || value ==null){
                throw new RuntimeException("name or value can not be null");
            }

            switch (name){
                case "driverClassName":driveClass = value;break;
                case "url":url=value;break;
                case "username":username = value;break;
                case "password":password=value;break;
                default:throw new RuntimeException("label is illegal,please check the config file");
            }
        }
        Class.forName(driveClass);
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return  connection;
    }

    private String getValue(Element node){
        return node.hasContent()?node.getText():node.attributeValue("value");
    }

    //-----------------读取Mapper文件------------------------//
    public MapperBean readerMapper(String path){
        MapperBean mapperBean = new MapperBean();
        try{
            InputStream stream = classLoader.getResourceAsStream(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element rootElement = document.getRootElement();
            mapperBean.setInterfaceName(rootElement.attributeValue("namespace").trim());
            List<Function> list = new ArrayList<>();
            for(Iterator iterator = rootElement.elementIterator();iterator.hasNext();){
                Function function = new Function();
                Element e = (Element)iterator.next();
                String sqlType = rootElement.getName().trim();
                String funcName = e.attributeValue("id").trim();
                String sql = e.getText();
                String resultType = e.attributeValue("resultType");
                String parameterType = e.attributeValue("parameterType");
                function.setSqltype(sqlType);
                function.setFuncName(funcName);
                function.setParameterType(parameterType);
                Object instance = null;
                try {
                    instance = Class.forName(resultType).newInstance();

                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }

                function.setSql(sql);
                function.setResultType(instance);
                list.add(function);

            }
            mapperBean.setList(list);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return mapperBean;
    }
}
