/**
 * Author:   claire
 * Date:    2021-04-02 - 16:28
 * Description: leader选举测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-02 - 16:28          V1.17.0          leader选举测试
 */
package com.learning.zookeeper.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.EnsurePath;

/**
 * 功能简述 
 * 〈leader选举测试〉
 *
 * @author claire
 * @date 2021-04-02 - 16:28
 * @since 1.1.0
 */
public class CuratorLeaderTest {
    private static final String ZK_ADDRESS = "127.0.0.1:2181";
    private static final String ZK_PATH = "/zktest1";

    public static void main(String[] args) throws InterruptedException {


        LeaderSelectorListener selectorListener = new LeaderSelectorListener() {

            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {

            }

            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println(Thread.currentThread().getName() + " take leadership");
                //保留5s
                Thread.sleep(5000);
                System.out.println(Thread.currentThread().getName() + " give up leadership");
            }
        };

        Thread thread1 = new Thread(() -> {
            registerListener(selectorListener);
        });

        Thread thread2 = new Thread(() -> {
            registerListener(selectorListener);
        });

        Thread thread3 = new Thread(() -> {
            registerListener(selectorListener);
        });

        thread1.start();
        thread2.start();
        thread3.start();

        Thread.sleep(5*60*1000);
    }

    private static void registerListener(LeaderSelectorListener listener) {
        //创建连接
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();

        //确保path存在
        try{
            client.create().creatingParentContainersIfNeeded().forPath(ZK_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //进行选举
        LeaderSelector leaderSelector = new LeaderSelector(client, ZK_PATH, listener);
        leaderSelector.autoRequeue();
        leaderSelector.start();
    }
}
