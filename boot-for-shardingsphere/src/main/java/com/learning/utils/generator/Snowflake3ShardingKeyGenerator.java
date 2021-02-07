/**
 * Author:   claire
 * Date:    2021-01-11 - 09:17
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-11 - 09:17          V1.14.0
 */
package com.learning.utils.generator;

import io.shardingsphere.core.keygen.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2021-01-11 - 09:17
 * @since 1.14.0
 */
@Slf4j
@Component
public class Snowflake3ShardingKeyGenerator implements KeyGenerator {

    /**
     * 起始的时间戳
     */
    private final long twepoch = Date
            .from(LocalDate.of(2018, 1, 1).atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant()).getTime();

    /**
     * 每一部分占用的位数
     */
    private final long sequenceBits = 12L;
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;

    /**
     * 每一部分的最大值
     */
    private final long maxWorkerId = ~(-1L << workerIdBits);
    private final long maxDatacenterId = ~(-1L << datacenterIdBits);
    private final long sequenceMask = ~(-1L << sequenceBits);

    /**
     * 每一部分向左的位移
     */
    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private long workerId;// 数据中心
    private long datacenterId;// 机器标识
    private long sequence = 0L;// 序列号
    private long lastTimestamp = -1L;// 上一次时间戳


    @Override
    public Number generateKey() {
        return nextId();
    }


    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        String ip = "127.0.0.1";
        if (StringUtils.isEmpty(ip)) {
                throw new RuntimeException("[SnowflakeShardingKeyGenerator][init]SnowflakeShardingKeyGenerator get ip is empty");
            }
        this.workerId = this.datacenterId = Math.abs(ip.hashCode() % 31);
        log.info("[SnowflakeShardingKeyGenerator][init]ip:{},workerId:{},datacenterId；{}", ip, workerId, datacenterId);
    }

    /**
     * Instantiates a new Id worker.
     */
    public Snowflake3ShardingKeyGenerator() {
        super();
    }

    /**
     * Instantiates a new Id worker.
     *
     * @param workerId     the worker id
     * @param datacenterId the datacenter id
     */
    public Snowflake3ShardingKeyGenerator(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("[SnowflakeShardingKeyGenerator][generator]worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String
                    .format("[SnowflakeShardingKeyGenerator][generator]datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * Next id long.
     *
     * @return the long
     */
    private synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("[SnowflakeShardingKeyGenerator][nextId]Clock moved backwards.  Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * Til next millis long.
     *
     * @param lastTimestamp the last timestamp
     * @return the long
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * Time gen long.
     *
     * @return the long
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
