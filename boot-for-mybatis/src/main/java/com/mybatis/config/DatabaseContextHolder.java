package com.mybatis.config;

import com.mybatis.constant.DataSourceTypeEnum;

public class DatabaseContextHolder {
    public static final DataSourceTypeEnum DEFAULT_DB= DataSourceTypeEnum.DB1;

    private static final ThreadLocal<DataSourceTypeEnum> dbs = new ThreadLocal<DataSourceTypeEnum>();

    public static void setDbs(DataSourceTypeEnum dataSourceTypeEnum){
        dbs.set(dataSourceTypeEnum);
    }

    public static DataSourceTypeEnum getDbs(){
        return dbs.get();
    }

    public static void clearDB() {
        dbs.remove();
    }
}
