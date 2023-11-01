/**
 * Author:   claire
 * Date:    2023/8/15 - 6:22 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/8/15 - 6:22 下午          V1.0.0
 */
package com.learning.k8sapp.config;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import org.springframework.stereotype.Component;

/**
 *
 * @author claire
 * @date 2023/8/15 - 6:22 下午
 * @since 1.0.0
 */
@Component
public class ConfigMapListener {

    public ConfigMapListener(){
        Config config = new ConfigBuilder().withMasterUrl("http://127.0.0.1:8998").build();
        final KubernetesClient client = new KubernetesClientBuilder()
                .withConfig(config)
                .build();
        final SharedIndexInformer informer = client
                .configMaps()
                .inNamespace("bootdemo")
                .withLabelSelector("ingress-boot")
//                .watch(new Watcher<ConfigMap>() {
//                    @Override
//                    public void eventReceived(Action action, ConfigMap resource) {
//
//                    }
//
//                    @Override
//                    public void onClose(WatcherException cause) {
//
//                    }
//                })
                .inform(new ResourceEventHandler<ConfigMap>() {
                    @Override
                    public void onAdd(ConfigMap obj) {
                        System.out.println("onadd:" + obj.toString());
                    }

                    @Override
                    public void onUpdate(ConfigMap oldObj, ConfigMap newObj) {
                        System.out.println("onupdate:" + newObj.toString());
                    }

                    @Override
                    public void onDelete(ConfigMap obj, boolean deletedFinalStateUnknown) {
                        System.out.println("ondel:" + obj.toString());
                    }
                },10);
    }
}
