package com.demo.mvc.send;


import com.demo.mvc.bean.KeyValue;
import com.demo.mvc.util.HttpUtils;
import com.demo.mvc.util.Utils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class BatchUtils {
    private static org.slf4j.Logger Logger = LoggerFactory.getLogger(BatchUtils.class);

    static class Config {
        private String uid;
        private String password;
        private String channelId;
        private String uploadUrl;
        private String sendUrl;
        private String extNo;

        public Config(String uid, String password, String channelId, String uploadUrl, String sendUrl, String extNo) {
            this.uid = uid;
            this.password = password;
            this.channelId = channelId;
            this.uploadUrl = uploadUrl;
            this.sendUrl = sendUrl;
            this.extNo = extNo;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getSendUrl() {
            return sendUrl;
        }

        public void setSendUrl(String sendUrl) {
            this.sendUrl = sendUrl;
        }

        public String getUploadUrl() {
            return uploadUrl;
        }

        public void setUploadUrl(String uploadUrl) {
            this.uploadUrl = uploadUrl;
        }

        public String getExtNo() {
            return extNo;
        }

        public void setExtNo(String extNo) {
            this.extNo = extNo;
        }
    }

    private static Config getConfig(String information) {
        String[] str = information.split(",");
        return new Config(str[0], str[1], str[2], str[3], str[4], str[5]);
    }

//    public static KeyValue smsSend(List rysecretList, String message, String information) {
//
//        //String fileParentPath = File.separator + "home" + File.separator + "soft" + File.separator + "tomcat_loan_sms" + File.separator + "sendpackage";
//        String fileParentPath = "/home/soft/creditRySendPage";
//
//        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
//        String localPath = fileParentPath + File.separator + fileName + ".txt";
//
//        Config config = getConfig(information);
//        KeyValue keyValue = new KeyValue();
//        Map<String, String> uploadMap = new HashMap<>();
//        uploadMap.put("t", String.valueOf(System.currentTimeMillis()));
//        String uploadSign = GetSmsSignature(config.getUid(), config.getPassword(), uploadMap);
//        uploadMap.put("uid", config.getUid());
//        uploadMap.put("sign", uploadSign);
//
//        StopWatch clock = new StopWatch("发送");
//        clock.start("uploadFile");
//        Map<String, String> uploadResult = null;//LoanRYChannelPageSend.uploadFile(localPath, config.getUploadUrl(), uploadMap, rysecretList);
//        Logger.info("uploadFile :: {} ,{}", fileName, uploadResult.get("result"));
//        clock.stop();
//        clock.start("submitFileId");
//        if (uploadResult.get("status").equals("success")) {
//            SubmitResult upload = Utils.json(uploadResult.get("result"), SubmitResult.class);
//            if (upload.getCode().equals("0000")) {
//                String fileId = upload.getData().getFileID();
//
//                Map<String, String> parmMap = new HashMap<>();
//                parmMap.put("msgbody", message);//不要超过500字
//                parmMap.put("fileid", fileId);
//                parmMap.put("channelid", config.getChannelId());
//                parmMap.put("smstype", "3");
//                parmMap.put("priority", "2");
//                //parmMap.put("sender", config.getExtNo());
//                parmMap.put("t", String.valueOf(System.currentTimeMillis()));//时间戳
//                String mySignature = GetSmsSignature(config.getUid(), config.getPassword(), parmMap);
//                parmMap.put("uid", config.getUid());
//                parmMap.put("sign", mySignature);
//                String result;
//                try {
//                    result = HttpUtils.post(config.getSendUrl(), parmMap);
//                } catch (Exception e) {
//                    Logger.warn("统一平台批量发送异常：{}", e);
//                    throw new RuntimeException(e);
//                }
//                Logger.info("RYChannel request :: {}, result :: {}", parmMap, result);
//                if (!StringUtils.isEmpty(result)) {
//                    SubmitResult sendResult = Utils.json(result, SubmitResult.class);
//                    if (sendResult != null) {
//                        if ("0000".equals(sendResult.getCode())) {
//                            keyValue.setKey("success");
//                            keyValue.setValue(sendResult.getData().getBulkid());
//                        } else {
//                            keyValue.setKey("fail");
//                            keyValue.setValue(sendResult.getMsg());
//                        }
//                    } else {
//                        keyValue.setKey("fail");
//                        keyValue.setValue("提交发送失败");
//                    }
//                }
//                clock.stop();
//                Logger.info("统一平台批量发送：{}", clock);
//            } else {
//                String remark = upload.getCode() + upload.getMsg();
//                keyValue.setKey("fail");
//                keyValue.setValue(remark);
//            }
//        } else {
//            String remark;
//            if (uploadResult == null) {
//                remark = "package return map is null";
//            } else {
//                remark = uploadResult.get("result");
//            }
//            keyValue.setKey("fail");
//            keyValue.setValue(remark);
//          }
//        return keyValue;
//    }

    public static KeyValue smsFileSend(File file, String information,String uploadfileUrl,String sendUrl, String prodName,String bizName,String priority,String signName,String channel,String ruleName) {

        KeyValue keyValue = new KeyValue();
        Map<String, String> uploadResult = null;
        try{
			String[] str = information.split(",");
            Config config=new Config(str[0], str[1], str[2], uploadfileUrl, sendUrl,"");
            Logger.info("平台通道扩展码：{}", config.getExtNo());
            Map<String, String> uploadMap = new HashMap<>();
            uploadMap.put("t", String.valueOf(System.currentTimeMillis()));
            String uploadSign = GetSmsSignature(config.getUid(), config.getPassword(), uploadMap);
            uploadMap.put("uid", config.getUid());
            uploadMap.put("sign", uploadSign);

            String fileName = file.getName();
            StopWatch clock = new StopWatch("发送");
            clock.start("uploadFile");
            uploadResult = ChannelPageSend.uploadFile(file, config.getUploadUrl(), uploadMap);
            Logger.info("uploadFile :: {} ,{}", fileName, uploadResult.get("result"));
            clock.stop();
            clock.start("submitFileId");
            if (uploadResult.get("status").equals("success")) {
                SubmitResult upload = Utils.json(uploadResult.get("result"), SubmitResult.class);
                if (upload.getCode().equals("0000")) {
                    String fileId = upload.getData().getFileID();
                    Map<String, String> parmMap = new HashMap<>();
                    parmMap.put("fileid", fileId);
                    parmMap.put("channelid", config.getChannelId());
                    parmMap.put("smstype", "3");
                    parmMap.put("priority", priority);
                    //parmMap.put("sender", config.getExtNo());
                    parmMap.put("t", String.valueOf(System.currentTimeMillis()));//时间戳
                    parmMap.put("biz", bizName);
                    parmMap.put("prod", prodName);
                    String mySignature = GetSmsSignature(config.getUid(), config.getPassword(), parmMap);
                    parmMap.put("uid", config.getUid());
                    if(Objects.nonNull(signName)){
                        parmMap.put("signName", signName);
                    }
                    if(Objects.nonNull(channel)){
                        parmMap.put("channel", channel);
                    }
                    if(Objects.nonNull(ruleName)){
                        parmMap.put("ruleName", ruleName);
                    }
                    parmMap.put("sign", mySignature);
                    String result;
                    try {
                        result = HttpUtils.post(config.getSendUrl(), parmMap);
                    } catch (Exception e) {
                        Logger.warn("平台批量发送异常：{}", e);
                        throw new RuntimeException(e);
                    }
                    Logger.info("RYChannel request :: {}, result :: {}", parmMap, result);
                    if (!StringUtils.isEmpty(result)) {
                        SubmitResult sendResult = Utils.json(result, SubmitResult.class);
                        if (sendResult != null) {
                            if ("0000".equals(sendResult.getCode())) {
                                keyValue.setKey("success");
                                keyValue.setValue(sendResult.getData().getBulkid());
                            } else {
                                keyValue.setKey("fail");
                                keyValue.setValue(sendResult.getMsg());
                            }
                        } else {
                            keyValue.setKey("fail");
                            keyValue.setValue("提交发送失败");
                        }
                    }
                    clock.stop();
                    Logger.info("平台批量发送：{}", clock);
                } else {
                    String remark = upload.getCode() + upload.getMsg();
                    keyValue.setKey("fail");
                    keyValue.setValue(remark);
                }
            } else {
                String remark;
                if (uploadResult == null) {
                    remark = "package return map is null";
                } else {
                    remark = uploadResult.get("result");
                }
                keyValue.setKey("fail");
                keyValue.setValue(remark);
            }
        }catch (Exception e){
            if(uploadResult!=null){
                Logger.error("uploadResult="+uploadResult.toString());
            }
            e.printStackTrace();
            keyValue.setKey("fail");
            keyValue.setValue(e.getMessage());
        }
        return keyValue;
    }

    public static KeyValue smsFileSend(File file, String information,int identity) {

        KeyValue keyValue = new KeyValue();
        Map<String, String> uploadResult = null;
        try{
            String[] str = information.split(",");
            Config config=new Config(str[0], str[1], str[2], str[3], str[4],str[5]);
            Logger.info("平台通道扩展码：{}", config.getExtNo());
            Map<String, String> uploadMap = new HashMap<>();
            uploadMap.put("t", String.valueOf(System.currentTimeMillis()));
            String uploadSign = GetSmsSignature(config.getUid(), config.getPassword(), uploadMap);
            uploadMap.put("uid", config.getUid());
            uploadMap.put("sign", uploadSign);

            String fileName = file.getName();
            StopWatch clock = new StopWatch("发送");
            clock.start("uploadFile");
            uploadResult = ChannelPageSend.uploadFile(file, config.getUploadUrl(), uploadMap);
            Logger.info("uploadFile :: {} ,{}", fileName, uploadResult.get("result"));
            clock.stop();
            clock.start("submitFileId");
            if (uploadResult.get("status").equals("success")) {
                SubmitResult upload = Utils.json(uploadResult.get("result"), SubmitResult.class);
                if (upload.getCode().equals("0000")) {
                    String fileId = upload.getData().getFileID();
                    Map<String, String> parmMap = new HashMap<>();
                    parmMap.put("fileid", fileId);
                    parmMap.put("channelid", config.getChannelId());
                    parmMap.put("smstype", "3");
                    parmMap.put("priority", str[6]);
                    //parmMap.put("sender", config.getExtNo());
                    parmMap.put("t", String.valueOf(System.currentTimeMillis()));//时间戳
                    parmMap.put("biz", str[7]);
                    parmMap.put("prod", str[8]);
                    parmMap.put("uid", config.getUid());
                    if(identity<=0){
                        parmMap.put("isidentity","false");
                    }
                    if(Objects.nonNull(str[9])){
                        parmMap.put("signName", str[9]);
                    }
                    if(Objects.nonNull(str[10])){
                        parmMap.put("channel", str[10]);
                    }
                    if(Objects.nonNull(str[11])){
                        parmMap.put("ruleName", str[11]);
                    }
                    parmMap.put("userbulkid",UUID.randomUUID().toString().replace("-",""));
                    String mySignature = GetSmsSignature(config.getUid(), config.getPassword(), parmMap);
                    parmMap.put("sign", mySignature);
                    String result;
                    try {
                        result = HttpUtils.post(config.getSendUrl(), parmMap);
                    } catch (Exception e) {
                        Logger.warn("平台批量发送异常：{}", e);
                        throw new RuntimeException(e);
                    }
                    Logger.info("RYChannel request :: {}, result :: {}", parmMap, result);
                    if (!StringUtils.isEmpty(result)) {
                        SubmitResult sendResult = Utils.json(result, SubmitResult.class);
                        if (sendResult != null) {
                            if ("0000".equals(sendResult.getCode())) {
                                keyValue.setKey("success");
                                keyValue.setValue(sendResult.getData().getBulkid());
                            } else {
                                keyValue.setKey("fail");
                                keyValue.setValue(sendResult.getMsg());
                            }
                        } else {
                            keyValue.setKey("fail");
                            keyValue.setValue("提交发送失败");
                        }
                    }
                    clock.stop();
                    Logger.info("平台批量发送：{}", clock);
                } else {
                    String remark = upload.getCode() + upload.getMsg();
                    keyValue.setKey("fail");
                    keyValue.setValue(remark);
                }
            } else {
                String remark;
                if (uploadResult == null) {
                    remark = "package return map is null";
                } else {
                    remark = uploadResult.get("result");
                }
                keyValue.setKey("fail");
                keyValue.setValue(remark);
            }
        }catch (Exception e){
            if(uploadResult!=null){
                Logger.error("uploadResult="+uploadResult.toString());
            }
            e.printStackTrace();
            keyValue.setKey("fail");
            keyValue.setValue(e.getMessage());
        }
        return keyValue;
    }

    /*
    * 计算签名
    * */
    public static String GetSmsSignature(String uid, String appkey, Map<String, String> parmMap) {
        List<String> keys = new ArrayList<String>(parmMap.keySet());
        Collections.sort(keys);
        String prestr = uid;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if("uid".equals(key)){
                continue;
            }
            String value = (String) parmMap.get(key);
            prestr = prestr + key + "=" + value;
        }
        prestr += appkey;
        String signature = null;

        try {
            signature = DigestUtils.md5Hex(prestr.getBytes("UTF-8")).toLowerCase();
            System.out.println("before:"+prestr+"，after:"+signature);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return signature;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class SubmitResult {
        private String code;
        private String msg;
        private Data data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    static class Data {
        private String bulkid;
        private String fileID;

        public String getBulkid() {
            return bulkid;
        }

        public void setBulkid(String bulkid) {
            this.bulkid = bulkid;
        }

        public String getFileID() {
            return fileID;
        }

        public void setFileID(String fileID) {
            this.fileID = fileID;
        }
    }

    public static void main(String[] args){
        KeyValue keyValue = new KeyValue();
        Map<String, String> uploadResult = null;
//        String[] str = information.split(",");
//        Config config=new Config(str[0], str[1], str[2], str[3], str[4],str[5]);
        Map<String, String> uploadMap = new HashMap<>();
        uploadMap.put("t", String.valueOf(System.currentTimeMillis()));
        String uploadSign = GetSmsSignature("2", "170e4ccb2093393b531b36476becc6c6", uploadMap);
        uploadMap.put("uid", "2");
        uploadMap.put("sign", uploadSign);

        String fileName = "test.txt";
        File file = new File("/Users/chenzy/Downloads/test.txt");
        StopWatch clock = new StopWatch("发送");
        clock.start("uploadFile");
        uploadResult = ChannelPageSend.uploadFile(file, "http://192.168.2.180:8080/sms-web/v2/uploadFile", uploadMap);
        Logger.info("uploadFile :: {} ,{}", fileName, uploadResult.get("result"));
        clock.stop();
        clock.start("submitFileId");
        if (uploadResult.get("status").equals("success")) {
            SubmitResult upload = Utils.json(uploadResult.get("result"), SubmitResult.class);
            if (upload.getCode().equals("0000")) {
        String fileId = upload.getData().getFileID();
        Map<String, String> parmMap = new HashMap<>();
        parmMap.put("fileid", fileId);
        parmMap.put("channelid", "84");
        parmMap.put("smstype", "3");
        parmMap.put("priority", "3");
        //parmMap.put("sender", config.getExtNo());
        parmMap.put("t", String.valueOf(System.currentTimeMillis()));//时间戳
        parmMap.put("userbulkid",UUID.randomUUID().toString().replace("-",""));
        String mySignature = GetSmsSignature("2", "170e4ccb2093393b531b36476becc6c6", parmMap);
        parmMap.put("uid", "2");
        parmMap.put("sign", mySignature);
        String result;
        try {
            result = HttpUtils.post("http://192.168.2.180:8080/sms-web/v3/sendBulkMsg", parmMap);
        } catch (Exception e) {
            Logger.warn("平台批量发送异常：{}", e);
            throw new RuntimeException(e);
        }
        Logger.info("RYChannel request :: {}, result :: {}", parmMap, result);
        if (!StringUtils.isEmpty(result)) {
            SubmitResult sendResult = Utils.json(result, SubmitResult.class);
            if (sendResult != null) {
                if ("0000".equals(sendResult.getCode())) {
                   System.out.println(sendResult.toString());
                } else {
                    System.out.println(sendResult.toString());
                }
            } else {
            }
        }
            } else {
                String remark = upload.getCode() + upload.getMsg();
                keyValue.setKey("fail");
                keyValue.setValue(remark);
            }
        } else {
            String remark;
            if (uploadResult == null) {
                remark = "package return map is null";
            } else {
                remark = uploadResult.get("result");
            }
            keyValue.setKey("fail");
            keyValue.setValue(remark);
        }
    }

}
