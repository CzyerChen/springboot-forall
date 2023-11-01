/**
 * Author:   claire
 * Date:    2023/8/15 - 7:18
 * Description:
 * History:
 * author          time                   version          desc
 * claire          2023/8/15 - 7:18      V1.0.0
 */

package com.learning.k8sclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @date 2023/8/15 - 7:18
 * @since 1.0.0
 */
@Slf4j
public class Executor {

    public static void main(String[] args) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://127.0.0.1:8998/api/v1/namespaces/bootdemo/configmaps?labelSelector=ingress-boot");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            log.info(statusCode + "");
            if (statusCode == 200) {
                InputStream inputStream = null;
                inputStream = entity.getContent();
                String content = IOUtils.toString(inputStream, Charset.defaultCharset());
                log.info("content:" + content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
