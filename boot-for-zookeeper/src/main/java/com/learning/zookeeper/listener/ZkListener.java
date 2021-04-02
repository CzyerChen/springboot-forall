/**
 * Author:   claire
 * Date:    2021-04-02 - 14:55
 * Description: zookeeper监听器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-02 - 14:55          V1.17.0          zookeeper监听器
 */
package com.learning.zookeeper.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * 功能简述 
 * 〈zookeeper监听器〉
 *
 * @author claire
 * @date 2021-04-02 - 14:55
 * @since 1.1.0
 */
@Component
public class ZkListener {
    @Resource
    private CuratorFramework curatorClient;

    @PostConstruct
    public void init(){
        String path = "/zktest";

        //当前节点
        CuratorCache curatorCache = CuratorCache.builder(curatorClient, path).build();
        CuratorCacheListener listener = CuratorCacheListener
                .builder()
                .forNodeCache(new NodeCacheListener(){
                    @Override
                    public void nodeChanged() throws Exception{
                        Optional<ChildData> childData = curatorCache.get(path);
                        if(childData.isPresent()) {
                            String data = new String(childData.get().getData());
                            System.out.println("--------【NodeCacheListener--------】---------nodePath:" + path + " data:" + data);
                        }
                    }
                })
                .build();
        curatorCache.listenable().addListener(listener);

        //监听子节点，不监听当前节点
        CuratorCacheListener pathCacheListener = CuratorCacheListener
                .builder()
                .forPathChildrenCache(path, curatorClient, new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                        String type = event.getType().name();
                        System.out.println("--------【PathChildrenCacheListener】-------- type:" + type);
                        Optional.ofNullable(event.getData()).ifPresent(childata -> {
                            String nodePath = childata.getPath();
                            String data = "";
                            if(Objects.nonNull(childata.getData())) {
                                data = new String(childata.getData());
                            }
                            System.out.println("--------【PathChildrenCacheListener】-------- nodePath:" + nodePath + " data:" + data + " type:" + type);
                        });
                    }
                })
                .build();
        curatorCache.listenable().addListener(pathCacheListener);

        CuratorCacheListener treeListener = CuratorCacheListener
                .builder()
                .forTreeCache(curatorClient, new TreeCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                        String type = event.getType().name();
                        System.out.println("--------【TreeCacheListener--------】-------- type:" + type);
                        Optional.ofNullable(event.getData()).ifPresent(childata -> {
                            String nodePath = childata.getPath();
                            String data = "";
                            if(Objects.nonNull(childata.getData())) {
                               data = new String(childata.getData());
                            }
                            System.out.println("--------【TreeCacheListener--------】-------- nodePath:" + nodePath + " data:" + data + " type:" + type);
                        });
                    }
                })
                .build();
        curatorCache.listenable().addListener(treeListener);

        curatorCache.start();
    }
}
