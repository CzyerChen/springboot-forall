package com.redission.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 24 18:15
 */
public class  FastJsonRedisSerializer<T>  implements RedisSerializer<T> {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private Class<T> clazz;

    public FastJsonRedisSerializer(Class<T> clazz){
        super();
        this.clazz = clazz;
    }


    @Override
    public byte[] serialize(T t) throws SerializationException {
        if(null  == t){
            return new byte[0];
        }

        return JSON.toJSONString(t, SerializerFeature.WriteClassName,SerializerFeature.DisableCircularReferenceDetect).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if( null == bytes || bytes.length <=0){
            return  null;
        }
        String str = new String(bytes,DEFAULT_CHARSET);
        T result =  (T)JSON.parseObject(str,clazz);
        //T result = (T)JSON.parse(str);
        return result;
    }
}
