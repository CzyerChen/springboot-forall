package com.redission.serializer;

import org.hibernate.result.Output;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayOutputStream;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 25 10:50
 */
public class KryoRedisSerializer implements RedisSerializer {


    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return null;
    }
}
