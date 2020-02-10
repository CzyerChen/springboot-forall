/**
 * Author:   claire
 * Date:    2020-02-09 - 09:41
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-09 - 09:41          V1.3.1
 */
package com.mybatis.multidb.config.selfdefined;

import com.fibodt.cloud.falcon.model.DataSourceConfig;
import com.fibodt.cloud.falcon.pool.FBDataSource;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Properties;

import static com.alibaba.druid.util.Utils.getBoolean;

/**
 * 功能简述 <br/>
 * 〈〉
 *
 * @author claire
 * @date 2020-02-09 - 09:41
 */
@Slf4j
public class MultiFbDatasource {
    public final static String DEFAULT_APP_NAME = "FIBODT";
    public final static String DEFAULT_APP_ENV = "dev";
    public final static String DEFAULT_CONFIG_CENTER_URL="http://121.36.32.42:12305";
    public final static int DEFAULT_INITIAL_SIZE = 0;
    public final static int DEFAULT_MAX_ACTIVE_SIZE = 8;
    public final static int DEFAULT_MAX_IDLE = 8;
    public final static int DEFAULT_MIN_IDLE = 0;
    public final static int DEFAULT_MAX_WAIT = -1;
    public final static String DEFAULT_VALIDATION_QUERY = null;
    public final static boolean DEFAULT_TEST_ON_BORROW = false;
    public final static boolean DEFAULT_TEST_ON_RETURN = false;
    public final static boolean                        DEFAULT_WHILE_IDLE                        = true;
    public static final long                           DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 60 * 1000L;
    public static final long                           DEFAULT_TIME_BETWEEN_CONNECT_ERROR_MILLIS = 500;
    public static final int                            DEFAULT_NUM_TESTS_PER_EVICTION_RUN        = 3;

    public static final long                           DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS    = 1000L * 60L * 30L;
    public static final long                           DEFAULT_MAX_EVICTABLE_IDLE_TIME_MILLIS    = 1000L * 60L * 60L * 7;
    public static final long                           DEFAULT_PHY_TIMEOUT_MILLIS                = -1;


    private String appName = DEFAULT_APP_NAME;
    private String env = DEFAULT_APP_ENV;
    private String configCenterUrl = DEFAULT_CONFIG_CENTER_URL;
    private String dbName;
    private String driverClassName;
    private String type;
    private String sourceType;
    private String platform;
    private int initialSize = DEFAULT_INITIAL_SIZE;
    private int minIdle = DEFAULT_MIN_IDLE;
    private int maxActive = DEFAULT_MAX_ACTIVE_SIZE;
    private int maxWait = DEFAULT_MAX_WAIT;

    /**
     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     */
    private long timeBetweenEvictionRunsMillis = DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;

    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    private long minEvictableIdleTimeMillis = DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

    /**
     * 配置一个连接在池中最大生存的时间，单位是毫秒
     */
    private long maxEvictableIdleTimeMillis = DEFAULT_MAX_EVICTABLE_IDLE_TIME_MILLIS;

    /**
     *
     */
    private boolean testWhileIdle = DEFAULT_WHILE_IDLE;

    /**
     *
     */
    private boolean testOnBorrow = DEFAULT_TEST_ON_BORROW;

    /**
     *
     */
    private boolean testOnReturn = DEFAULT_TEST_ON_RETURN;

    /**
     *
     */
    private boolean poolPreparedStatements = false;

    /**
     *
     */
    private int maxOpenPreparedStatements =10;

    /**
     *
     */
    private boolean asyncInit =false;


    private DataSourceConfig dataSourceConfig;

    public MultiFbDatasource()  {
        configFromPropety(System.getProperties());
    }

    public FBDataSource getInstance(){
        wraperDatasourceConfig();
        if (Objects.nonNull(dataSourceConfig)) {
            try {
                return FBDataSource.getInstance(dataSourceConfig);
            } catch (Exception e) {
                log.error("fail to initial MultiFbDatasource");
            }
        }
        return null;
    }
    public void configFromPropety(Properties properties) {
        {
            String property = properties.getProperty("fibo.env");
            if (property != null) {
                this.setEnv(property);
            }
        }
        {
            String property = properties.getProperty("fibo.dbName");
            if (property != null) {
                this.setDbName(property);
            }
        }
        {
            String property = properties.getProperty("fibo.driverClassName");
            if (property != null) {
                this.setDriverClassName(property);
            }
        }
        {
            String property = properties.getProperty("fibo.type");
            if (property != null) {
                this.setType(property);
            }
        }
        {
            String property = properties.getProperty("fibo.sourceType");
            if (property != null) {
                this.setSourceType(property);
            }
        }
        {
            String property = properties.getProperty("fibo.platform");
            if (property != null && property.length() > 0) {
                this.setPlatform(property);
            }
        }

        {
            String property = properties.getProperty("fibo.initialSize");
            if (property != null && property.length() > 0) {
                try {
                    int value = Integer.parseInt(property);
                    this.setInitialSize(value);
                } catch (NumberFormatException e) {
                    log.error("illegal property 'fibo.initialSize'", e);
                }
            }
        }
        {
            String property = properties.getProperty("fibo.minIdle");
            if (property != null && property.length() > 0) {
                try {
                    int value = Integer.parseInt(property);
                    this.setMinIdle(value);
                } catch (NumberFormatException e) {
                    log.error("illegal property 'fibo.minIdle'", e);
                }
            }
        }
        {
            String property = properties.getProperty("fibo.maxActive");
            if (property != null && property.length() > 0) {
                try {
                    int value = Integer.parseInt(property);
                    this.setMaxActive(value);
                } catch (NumberFormatException e) {
                    log.error("illegal property 'fibo.maxActive'", e);
                }
            }
        }
        {
            String property = properties.getProperty("fibo.maxWait");
            if (property != null && property.length() > 0) {
                try {
                    int value = Integer.parseInt(property);
                    this.setMaxWait(value);
                } catch (NumberFormatException e) {
                    log.error("illegal property 'fibo.maxWaitThreadCount'", e);
                }
            }
        }
        {
            String property = properties.getProperty("fibo.timeBetweenEvictionRunsMillis");
            if (property != null && property.length() > 0) {
                try {
                    long value = Long.parseLong(property);
                    this.setTimeBetweenEvictionRunsMillis(value);
                } catch (NumberFormatException e) {
                    log.error("illegal property 'fibo.timeBetweenEvictionRunsMillis'", e);
                }
            }
        }
        {
            String property = properties.getProperty("fibo.minEvictableIdleTimeMillis");
            if (property != null && property.length() > 0) {
                try {
                    long value = Long.parseLong(property);
                    this.setMinEvictableIdleTimeMillis(value);
                } catch (NumberFormatException e) {
                    log.error("illegal property 'fibo.minEvictableIdleTimeMillis'", e);
                }
            }
        }
        {
            String property = properties.getProperty("fibo.maxEvictableIdleTimeMillis");
            if (property != null && property.length() > 0) {
                try {
                    long value = Long.parseLong(property);
                    this.setMaxEvictableIdleTimeMillis(value);
                } catch (NumberFormatException e) {
                    log.error("illegal property 'druid.maxEvictableIdleTimeMillis'", e);
                }
            }
        }
        {
            Boolean value = getBoolean(properties, "fibo.testWhileIdle");
            if (value != null) {
                this.testWhileIdle = value;
            }
        }
        {
            Boolean value = getBoolean(properties, "fibo.testOnBorrow");
            if (value != null) {
                this.testOnBorrow = value;
            }
        }
        {
            Boolean value = getBoolean(properties, "fibo.poolPreparedStatements");
            if (value != null) {
                this.setPoolPreparedStatements(value);
            }
        }
        {
            Boolean value = getBoolean(properties, "fibo.asyncInit"); // compatible for early versions
            if (value != null) {
                this.setAsyncInit(value);
            }
        }
    }

    private void wraperDatasourceConfig() {
        DataSourceConfig config = new DataSourceConfig();
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
        this.dataSourceConfig = config;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getConfigCenterUrl() {
        return configCenterUrl;
    }

    public void setConfigCenterUrl(String configCenterUrl) {
        this.configCenterUrl = configCenterUrl;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public long getMaxEvictableIdleTimeMillis() {
        return maxEvictableIdleTimeMillis;
    }

    public void setMaxEvictableIdleTimeMillis(long maxEvictableIdleTimeMillis) {
        this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public int getMaxOpenPreparedStatements() {
        return maxOpenPreparedStatements;
    }

    public void setMaxOpenPreparedStatements(int maxOpenPreparedStatements) {
        this.maxOpenPreparedStatements = maxOpenPreparedStatements;
    }

    public boolean isAsyncInit() {
        return asyncInit;
    }

    public void setAsyncInit(boolean asyncInit) {
        this.asyncInit = asyncInit;
    }
}
