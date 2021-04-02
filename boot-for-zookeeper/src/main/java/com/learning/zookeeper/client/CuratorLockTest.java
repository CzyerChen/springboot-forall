/**
 * Author:   claire
 * Date:    2021-04-02 - 16:21
 * Description: 分布式锁测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-02 - 16:21          V1.17.0          分布式锁测试
 */
package com.learning.zookeeper.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.TimeUnit;

/**
 * 功能简述 
 * 〈分布式锁测试〉
 *
 * @author claire
 * @date 2021-04-02 - 16:21
 * @since 1.1.0
 */
public class CuratorLockTest {
    private static final String ZK_ADDRESS = "127.0.0.1:2181";
    private static final String ZK_PATH = "/zktest1";

    public static void main(String[] args){
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();

        Thread thread1 = new Thread(() -> {
            accquireLock(client);
        });

        Thread thread2 = new Thread(() -> {
            accquireLock(client);
        });
        thread1.start();
        thread2.start();
    }

    private static void accquireLock(CuratorFramework client){
        InterProcessMutex lock = new InterProcessMutex(client, ZK_PATH);
        try{
            //等待60s,请求锁
           if(lock.acquire(60, TimeUnit.SECONDS)){
               System.out.println(Thread.currentThread().getName() + " get & hold the lock");
               //占用锁5s
               Thread.sleep(5000);
               System.out.println(Thread.currentThread().getName() + " release the lock");
           }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
