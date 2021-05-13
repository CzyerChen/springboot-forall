# 三方API鉴权指南

此处使用带时间戳的参数加密鉴权方式

身份认证通用

## 1.1 参数说明

以下为必传参数

|序号|参数|类型|说明|
|----|----|----|----|
|1	|t	|string	|当前时间戳|
|2	|uid	|string	|用户id|
|3	|sign	|string	|用户参数加密字段|
|4	|priority	|string	|优先级|


## 1.2 返回值说明

|序号|code|message|说明|
|-----|-----|-----|-----|
|1	|2003|参数范围错误||
|2	|2004|用户不存在或者不可用|UID对应用户异常|
|3	|1001|未知错误||
|4	|1002|系统异常||
|5	|1003|接口鉴权失败|用户认证失败|
|6  |1004|接口鉴权缺少参数|接口必要参数缺失|

## 1.3 Java Demo

```java
public class demo{
    public String testSendAuth(){
        String uid ="4";
        String password = "somekey";
        try {
            Map<String, Object> parmMap = new HashMap<>();
            parmMap.put("biz", "测试业务");
            parmMap.put("prod", "测试产品");
            parmMap.put("fileid", "randomfileid1");
            parmMap.put("priority", "0");
            parmMap.put("t", String.valueOf(System.currentTimeMillis()));
            String uploadSign = getSignature(uid, password, parmMap);
            parmMap.put("uid", uid);
            parmMap.put("sign", uploadSign);
     
            StopWatch clock = new StopWatch("发送");
            clock.start("postfile");
            String result = HttpUtil.post("http://127.0.0.1:8080/sms-web/test/auth", parmMap);
            clock.stop();
     
            return result;
        }catch (Exception e){
            logger.error("",e);
        }
        return null;
    }
     
     
    public static String getSignature(String uid, String appkey, Map<String, Object> parmMap) {
        List<String> keys = new ArrayList<String>(parmMap.keySet());
        Collections.sort(keys);
        StringBuffer prestr = new StringBuffer(uid);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if("uid".equals(key) || "sign".equals(key)){
                continue;
            }
            String value = (String) parmMap.get(key);
            prestr.append(key).append("=").append(value);
        }
        prestr.append(appkey);
        String signature = null;
        signature = DigestUtils.md5Hex(prestr.toString().getBytes(Charset.forName("utf-8"))).toLowerCase();
        return signature;
    }
}
```