package com.mybatis.multidb.config;

import com.fibodt.cloud.falcon.model.DataSourceConfig;
import com.fibodt.cloud.falcon.pool.FBDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * druid 数据库连接池配置
 * @author: claire  on 2019-06-21 - 14:24
 **/
@Component
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class DruidConfig {

    private String appName;

    private String env;

    private String configCenterUrl;

    private String dbName;

    private String sourceType;

    /**
     * 初始化连接数量
     */
    private int initialSize;

    /**
     * 最小闲置连接
     */
    private int minIdle;

    /**
     * 最大存活连接
     */
    private int maxActive;

    /**
     * 配置获取连接等待超时的时间
     */
    private long maxWait;

    /**
     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     */
    private long timeBetweenEvictionRunsMillis;

    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    private long minEvictableIdleTimeMillis;

    /**
     * 配置一个连接在池中最大生存的时间，单位是毫秒
     */
    private long maxEvictableIdleTimeMillis;

    /**
     *
     */
    private boolean testWhileIdle;

    /**
     *
     */
    private boolean testOnBorrow;

    /**
     *
     */
    private boolean testOnReturn;

    /**
     *
     */
    private boolean poolPreparedStatements;

    /**
     *
     */
    private int maxOpenPreparedStatements;

    /**
     *
     */
    private boolean asyncInit;

    @Bean("mysql_dmp_datasource")
    public FBDataSource fbDataSource() throws Exception {
        DataSourceConfig config=new DataSourceConfig();
        config.setAppName(appName);
        config.setDbName(dbName);
        config.setEnv(env);
        config.setConfigCenterUrl(configCenterUrl);
        config.setSourceType(sourceType);
        config.setInitialSize(initialSize);
        config.setMaxWait(maxWait);
        config.setMinIdle(minIdle);
        config.setMaxActive(maxActive);
        config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        config.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
        config.setTestWhileIdle(testWhileIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setPoolPreparedStatements(poolPreparedStatements);
        config.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
        config.setAsyncInit(asyncInit);

        return FBDataSource.getInstance(config);
    }
}
