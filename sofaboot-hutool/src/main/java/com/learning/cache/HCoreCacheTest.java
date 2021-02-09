/**
 * Author:   claire
 * Date:    2021-02-09 - 17:01
 * Description: 缓存类测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 17:01          V1.17.0          缓存类测试
 */
package com.learning.cache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.file.LFUFileCache;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.cache.impl.WeakCache;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.thread.ThreadUtil;

/**
 * 功能简述 
 * 〈缓存类测试〉
 *
 * @author claire
 * @date 2021-02-09 - 17:01
 */
public class HCoreCacheTest {
    public static void main(String[] args){
        /*===============缓存工具-CacheUtil======================*/
        //新建FIFOCache
        Cache<String,String> fifoCache = CacheUtil.newFIFOCache(3);

        /*===============先入先出-FIFOCache======================*/
        Cache<String,String> fifoCache2 = CacheUtil.newFIFOCache(3);

        //加入元素，每个元素可以设置其过期时长，DateUnit.SECOND.getMillis()代表每秒对应的毫秒数，在此为3秒
        fifoCache2.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        fifoCache2.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        fifoCache2.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);

        //由于缓存容量只有3，当加入第四个元素的时候，根据FIFO规则，最先放入的对象将被移除
        fifoCache2.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

        //value1为null
        String value1 = fifoCache2.get("key1");

        /*===============最少使用-LFUCache======================*/
        Cache<String, String> lfuCache = CacheUtil.newLFUCache(3);
        //通过实例化对象创建
        //LFUCache<String, String> lfuCache = new LFUCache<String, String>(3);

        lfuCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        lfuCache.get("key1");//使用次数+1
        lfuCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        lfuCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
        lfuCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

        //由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2,3被移除）
        String value2 = lfuCache.get("key2");//null
        String value3 = lfuCache.get("key3");//null

        /*===============最近最久未使用-LRUCache======================*/
        Cache<String, String> lruCache = CacheUtil.newLRUCache(3);
        //通过实例化对象创建
        //LRUCache<String, String> lruCache = new LRUCache<String, String>(3);
        lruCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        lruCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        lruCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
        lruCache.get("key1");//使用时间推近
        lruCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

        //由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2被移除）
        String value4 = lruCache.get("key");//null

        /*===============超时-TimedCache======================*/
        //创建缓存，默认4毫秒过期
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(4);
        //实例化创建
        //TimedCache<String, String> timedCache = new TimedCache<String, String>(4);

        timedCache.put("key1", "value1", 1);//1毫秒过期
        timedCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 5);
        timedCache.put("key3", "value3");//默认过期(4毫秒)

        //启动定时任务，每5毫秒秒检查一次过期
        timedCache.schedulePrune(5);

        //等待5毫秒
        ThreadUtil.sleep(5);

        //5毫秒后由于value2设置了5毫秒过期，因此只有value2被保留下来
        String value5 = timedCache.get("key1");//null
        String value6 = timedCache.get("key2");//value2

        //5毫秒后，由于设置了默认过期，key3只被保留4毫秒，因此为null
        String value7 = timedCache.get("key3");//null

        //取消定时清理
        timedCache.cancelPruneSchedule();
        /*===============弱引用-WeakCache======================*/
        WeakCache<String, String> weakCache = CacheUtil.newWeakCache(DateUnit.SECOND.getMillis() * 3);


        /*===============文件缓存-FileCache======================*/
        //参数1：容量，能容纳的byte数
        //参数2：最大文件大小，byte数，决定能缓存至少多少文件，大于这个值不被缓存直接读取
        //参数3：超时。毫秒
        LFUFileCache cache = new LFUFileCache(1000, 500, 2000);
        byte[] bytes = cache.getFileBytes("d:/a.jpg");

    }
}
