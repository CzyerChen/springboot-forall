/**
 * Author:   claire
 * Date:    2023/8/14 - 4:33
 * Description:
 * History:
 * author          time                  version          desc
 * claire          2023/8/14 - 4:33       V1.0.0
 */

package com.learning.k8sclient.runner;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @date 2023/8/14 - 4:33
 * @since 1.0.0
 */
@Slf4j
@Component
public class ClientWatchRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
//        Config config = new ConfigBuilder()
////                .withMasterUrl("https://kubernetes.docker.internal:6443")
////                .withMasterUrl("http://127.0.0.1:8001")
//                .withMasterUrl("https://127.0.0.1:56427")
//                .build();
        final KubernetesClient client = new KubernetesClientBuilder()
//                .withConfig(config)
                .build();
//        final SharedIndexInformer informer =
        client
                .configMaps()
                .inNamespace("bootdemo")
//                        .withLabelSelector("app=ingress-boot")
//                        .inform();
                .watch(new Watcher<ConfigMap>() {
                    @Override
                    public void eventReceived(Action action, ConfigMap resource) {
                        log.info("action:" + action.name() + ", resource:" + resource.toString());
                    }

                    @Override
                    public void onClose(WatcherException cause) {

                    }
                });
//                .withLabelSelector("ingress-boot")
//                .inform(new ResourceEventHandler<ConfigMap>() {
//                    @Override
//                    public void onAdd(ConfigMap obj) {
//                        log.info("onadd:" + obj.toString());
//                    }
//
//                    @Override
//                    public void onUpdate(ConfigMap oldObj, ConfigMap newObj) {
//                        log.info("onupdate:" + newObj.toString());
//                    }
//
//                    @Override
//                    public void onDelete(ConfigMap obj, boolean deletedFinalStateUnknown) {
//                        log.info("ondel:" + obj.toString());
//                    }
//                },10000);

//        Lister configMapLister = new Lister<>(informer.getIndexer());
//        final List<ConfigMap> list = configMapLister.list();
//        if (list != null) {
//            list.forEach(cf -> {
//                Map<String, String> data = cf.getData();
//                if (data == null) {
//                    return;
//                }
//                log.info(data);
//            });
//        }
    }

    public void UUUSE(){
        System.out.println(1);
        System.out.println(1+1);
    }
}
