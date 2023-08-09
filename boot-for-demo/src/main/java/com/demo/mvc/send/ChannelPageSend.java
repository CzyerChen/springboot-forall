package com.demo.mvc.send;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

public class ChannelPageSend {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelPageSend.class);

    public static Map<String, String> uploadFile(String localFilePath, String uploadUrl, Map map, List<String> list) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        Map<String, String> resultMap = new HashMap<>();
        try {
            FileWriter fw = new FileWriter(localFilePath);
            if (!CollectionUtils.isEmpty(list)) {
                for (String mobile : list) {
                    fw.write(mobile + "\n");
                }
                fw.close();
            }
            LOGGER.info("-- Create File End --");
            httpClient = HttpClients.createDefault();
            // 把一个普通参数和文件上传给下面这个地址 是一个servlet
            HttpPost httpPost = new HttpPost(uploadUrl);
            // 把文件转换成流对象FileBody
            FileBody bin = new FileBody(new File(localFilePath));

            StringBody t = new StringBody(map.get("t").toString(), ContentType.create(
                    "text/plain", Consts.UTF_8));
            StringBody uid = new StringBody(map.get("uid").toString(), ContentType.create(
                    "text/plain", Consts.UTF_8));
            StringBody sign = new StringBody(map.get("sign").toString(), ContentType.create(
                    "text/plain", Consts.UTF_8));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    // 相当于<input type="file" name="file"/>
                    .addPart("file", bin)
                    // 相当于<input type="text" name="userName" value=userName>
                    .addPart("t", t)
                    .addPart("uid", uid)
                    .addPart("sign", sign)
                    .build();
            httpPost.setEntity(reqEntity);
            // 发起请求 并返回请求的响应
            response = httpClient.execute(httpPost);

            // 获取响应对象
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String result = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                resultMap.put("status", "success");
                resultMap.put("result", result);
                LOGGER.info("upload package result :: {}", result);
            } else {
                resultMap.put("status", "fail");
                resultMap.put("result", "upload return Empty");
                LOGGER.info("-- upload return Empty --");
            }
            // 销毁
            EntityUtils.consume(resEntity);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("打包请求异常 Error :: {}", e.getMessage());
            resultMap.put("status", "fail");
            resultMap.put("result", "package Error");
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    public static String upload(String localFile) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            // 把一个普通参数和文件上传给下面这个地址 是一个servlet
            HttpPost httpPost = new HttpPost("http://192.168.1.7:84/web/v2/uploadFile");
            // 把文件转换成流对象FileBody
            FileBody bin = new FileBody(new File(localFile));


            String appkey = "3db24de87e2b1d7f636b7f2c8d08bda7";//分配的appkey
            Map<String, Object> parmMap = new HashMap<String, Object>();
            parmMap.put("t", String.valueOf(System.currentTimeMillis()));
            String mySignature = GetSmsSignature("212", appkey, parmMap);
            parmMap.put("uid", "212");
            parmMap.put("sign", mySignature);

            StringBody t = new StringBody(parmMap.get("t").toString(), ContentType.create(
                    "text/plain", Consts.UTF_8));
            StringBody uid = new StringBody(parmMap.get("uid").toString(), ContentType.create(
                    "text/plain", Consts.UTF_8));
            StringBody sign = new StringBody(parmMap.get("sign").toString(), ContentType.create(
                    "text/plain", Consts.UTF_8));


            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    // 相当于<input type="file" name="file"/>
                    .addPart("file", bin)

                    // 相当于<input type="text" name="userName" value=userName>
                    .addPart("t", t)
                    .addPart("uid", uid)
                    .addPart("sign", sign)
                    .build();

            httpPost.setEntity(reqEntity);

            // 发起请求 并返回请求的响应
            response = httpClient.execute(httpPost);

            System.out.println("The response value of token:" + response.getFirstHeader("token"));

            // 获取响应对象
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                // 打印响应长度
                System.out.println("Response content length: " + resEntity.getContentLength());
                // 打印响应内容
                System.out.println(EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
            }

            // 销毁
            EntityUtils.consume(resEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String GetSmsSignature(String uid, String appkey, Map<String, Object> parmMap) {
        List<String> keys = new ArrayList<String>(parmMap.keySet());
        Collections.sort(keys);
        String prestr = uid;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = (String) parmMap.get(key);
            prestr = prestr + key + "=" + value;
        }
        prestr += appkey;
        String signature = null;
        System.out.println("prestr:" + prestr);
        try {
            signature = DigestUtils.md5Hex(prestr.getBytes("UTF-8")).toLowerCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("signature:" + signature);
        return signature;
    }


    public static Map<String, String> uploadFile(File file, String uploadUrl, Map map) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        Map<String, String> resultMap = new HashMap<>();
        try {
            httpClient = HttpClients.createDefault();
            // 把一个普通参数和文件上传给下面这个地址 是一个servlet
            HttpPost httpPost = new HttpPost(uploadUrl);
            // 把文件转换成流对象FileBody
            FileBody bin = new FileBody(file);
            StringBody t = new StringBody(map.get("t").toString(), ContentType.create(
                    "text/plain", Consts.UTF_8));
            StringBody uid = new StringBody(map.get("uid").toString(), ContentType.create(
                    "text/plain", Consts.UTF_8));
            StringBody sign = new StringBody(map.get("sign").toString(), ContentType.create(
                    "text/plain", Consts.UTF_8));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    // 相当于<input type="file" name="file"/>
                    .addPart("file", bin)
                    // 相当于<input type="text" name="userName" value=userName>
                    .addPart("t", t)
                    .addPart("uid", uid)
                    .addPart("sign", sign)
                    .build();
            httpPost.setEntity(reqEntity);
            // 发起请求 并返回请求的响应
            response = httpClient.execute(httpPost);

            // 获取响应对象
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String result = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                resultMap.put("status", "success");
                resultMap.put("result", result);
                LOGGER.info("upload package result :: {}", result);
            } else {
                resultMap.put("status", "fail");
                resultMap.put("result", "upload return Empty");
                LOGGER.info("-- upload return Empty --");
            }
            // 销毁
            EntityUtils.consume(resEntity);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("打包请求异常 Error :: {}", e.getMessage());
            resultMap.put("status", "fail");
            resultMap.put("result", "package Error");
        } finally {
        	try{
				file.delete();
			}catch (Exception e){
        		e.printStackTrace();
			}
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

}
