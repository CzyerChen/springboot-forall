/**
 * Author:   claire
 * Date:    2021-04-02 - 14:37
 * Description: 测试类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-02 - 14:37                            测试类
 */
package com.learning.zookeeper.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * 功能简述 
 * 〈测试类〉
 * 主要涉及create get set ls delete等常规命令的联通测试
 * 当前测试版本号：5.1.0
 *
 * @author claire
 * @date 2021-04-02 - 14:37
 */
public class CuratorClientTest {
    private static final String ZK_ADDRESS = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    private static final String ZK_PATH = "/zktest1";

    public static void main(String[] args) throws Exception {
        // Connect to zk
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        System.out.println("zk client start successfully!");

        // Client API test
        //Create node
        String content = "hello";
        print("create", ZK_PATH, content);
        client.create().creatingParentsIfNeeded().forPath(ZK_PATH, content.getBytes());

        // Get node and data
        print("ls", "/");
        print(client.getChildren().forPath("/"));
        print("get", ZK_PATH);
        print(client.getData().forPath(ZK_PATH));

        // Modify data
        String content2 = "world";
        print("set", ZK_PATH, content2);
        client.setData().forPath(ZK_PATH, content2.getBytes());
        print("get", ZK_PATH);
        print(client.getData().forPath(ZK_PATH));

        //Remove node
        print("delete", ZK_PATH);
        client.delete().forPath(ZK_PATH);
        print("ls", "/");
        print(client.getChildren().forPath("/"));
    }

    private static void print(String... cmds) {
        StringBuilder text = new StringBuilder("$ ");
        for (String cmd : cmds) {
            text.append(cmd).append(" ");
        }
        System.out.println(text.toString());
    }

    private static void print(Object result) {
        System.out.println(
                result instanceof byte[]
                        ? new String((byte[]) result)
                        : result);
    }

}
