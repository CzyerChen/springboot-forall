/**
 * Author:   claire
 * Date:    2021-07-08 - 16:30
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-07-08 - 16:30          V1.0.0
 */
package com.boot.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.boot.dao.SmsReplyRepositoy;
import com.boot.entity.SmsReplyReceive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cn.hutool.core.util.RuntimeUtil.destroy;

/**
 * 功能简述
 * 〈〉
 *
 * @author claire
 * @date 2021-07-08 - 16:30
 * @since 1.0.0
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private SmsReplyRepositoy replyRepositoy;


    @RequestMapping("limit")
    public String getLimitCheck(String prefix, String keys, String from, String to, Integer limit) throws IOException, InterruptedException {
        List<String> words = Arrays.asList(keys.split(","));
        LocalDateTime dateFrom = LocalDateTimeUtil.parse(from, "yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTo = LocalDateTimeUtil.parse(to, "yyyy-MM-dd HH:mm:ss");
        List<String> times = new ArrayList<>();
        while (dateFrom.isBefore(dateTo)) {
            String time = LocalDateTimeUtil.format(dateFrom, "HH:mm:ss");
            times.add(time);
            dateFrom = dateFrom.plusSeconds(1);
        }
        File file = new File(prefix + "/catalina.out");
        Map<String, AtomicInteger> countMap = new HashMap<>();
        if (file.exists()) {
            FileInputStream fw = null;
            BufferedReader bufferedReader = null;
            try {
                //创建流
                fw = new FileInputStream(file);
                //新new BufferedReader对象，记得关闭回收
                bufferedReader = IoUtil.getReader(fw, "UTF-8");
                String line = null;
                //到达流末尾, 就返回null
                while ((line = bufferedReader.readLine()) != null) {
                    if (Objects.nonNull(line) && line.length() > 20) {
                        String time = line.substring(11, 19);
                        boolean matchAll = words.stream().allMatch(line::contains);
                        if (matchAll && times.contains(time)) {
                            if (countMap.containsKey(time)) {
                                AtomicInteger count = countMap.get(time);
                                count.incrementAndGet();
                                countMap.replace(time, count);
                            } else {
                                countMap.put(time, new AtomicInteger(1));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                //抛出一个运行时异常(直接停止掉程序)
                throw new RuntimeException("运行时异常", e);
            } finally {
                try {
                    //如果是空的 说明流创建失败 失败了不需要关闭
                    if (fw != null) {
                        fw.close();
                    }
                } catch (Exception e) {
                    //关闭资源失败 停止程序
                    throw new RuntimeException("关闭资源失败");
                } finally {
                    try {
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("关闭资源失败");
                    }
                }
            }
        }

        if (!countMap.isEmpty()) {
            return countMap.entrySet().stream().filter(data -> {
                int thres = 0;
                if (Objects.nonNull(limit)) {
                    thres = limit;
                }
                if (data.getValue().intValue() > thres) {
                    return true;
                } else {
                    return false;
                }
            }).map(entry -> entry.getKey() + " " + entry.getValue()).collect(Collectors.joining("\n"));
        }
        return null;
    }

    @RequestMapping("limit1")
    public String getLimitCheckOld(String prefix, String from, String to) throws IOException, InterruptedException {
        LocalDateTime dateFrom = LocalDateTimeUtil.parse(from, "yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTo = LocalDateTimeUtil.parse(to, "yyyy-MM-dd HH:mm:ss");

        List<String> resultList = new ArrayList();
        List<String> commands = new ArrayList();
        while (dateFrom.isBefore(dateTo)) {
            StringBuilder buffer = new StringBuilder("grep \"passcheck\" ").append(prefix).append("/catalina.out  | grep \"");
            String time = LocalDateTimeUtil.format(dateFrom, "HH:mm:ss");
            buffer.append(time).append("\" | wc -l");

            String str = buffer.toString();
//            System.out.println(str);
            String command = "sh /Users/chenzy/files/work/test3.sh " + " \"passcheck\" \"" + time + "\"";
            System.out.println(command);

//            resultList.add(time +" " +result);
//            result = null;
            commands.add(command);
            dateFrom = dateFrom.plusSeconds(1);


        }

        String[] commandArr = commands.toArray(new String[0]);
        if (ArrayUtil.isEmpty(commandArr)) {
            throw new NullPointerException("Command is empty !");
        }

        // 单条命令的情况
        if (1 == commandArr.length) {
            final String cmd = commandArr[0];
            if (StrUtil.isBlank(cmd)) {
                throw new NullPointerException("Command is empty !");
            }
            commandArr = StrUtil.splitToArray(cmd, StrUtil.C_SPACE);
        }

        Process process;
        try {
            process = new ProcessBuilder(commandArr).redirectErrorStream(true).start();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        InputStream in = null;
        String result = "";
        try {
            in = process.getInputStream();
            result = IoUtil.read(in, "UTF-8");
        } finally {
            IoUtil.close(in);
            destroy(process);
        }
        return String.join("\n", result);
    }

    @RequestMapping("submit")
    public String getSubmitCheck(String prefix, String from, String to) {
        LocalDateTime dateFrom = LocalDateTimeUtil.parse(from, "yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTo = LocalDateTimeUtil.parse(to, "yyyy-MM-dd HH:mm:ss");

        List<String> resultList = new ArrayList();
        while (dateFrom.isBefore(dateTo)) {
            StringBuffer buffer = new StringBuffer("grep \"CmppSubmitRequestMessage\" ").append(prefix).append("/catalina.out  | grep \"");
            String time = LocalDateTimeUtil.format(dateFrom, "HH:mm:ss");
            buffer.append(time).append("\" | wc -l");

            String result = RuntimeUtil.execForStr(buffer.toString());
            resultList.add(time + "  " + result);
            dateFrom.plusSeconds(1);
        }
        return String.join("\n", resultList);
    }

    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    @RequestMapping(value = "reply/write")
    public String refreshReply(@RequestParam String from, @RequestParam String to, @RequestParam Long channelId, @RequestParam String filePath) {
        System.out.println("param:======" + from+","+to+","+channelId+","+filePath);
        Date dateFrom = getDateFromString("yyyy-MM-dd HH:mm:ss", from + " 00:00:00");
        Date dateTo = getDateFromString("yyyy-MM-dd HH:mm:ss", to + " 23:59:59");

        List<SmsReplyReceive> createTimeBetween = replyRepositoy.findByChannelIdAndCreateTimeBetween(channelId, dateFrom, dateTo);
        if (!CollectionUtils.isEmpty(createTimeBetween)) {
            System.out.println("size:======" + createTimeBetween.size());
            File file = new File(filePath);
            FileAppender appender = new FileAppender(file, 16, true);
            createTimeBetween.forEach(data -> {
                appender.append(data.getIdentity() + ",1,1");
            });
            appender.flush();
            appender.toString();
        }
        return filePath;
    }

    public static Date getDateFromString(String format, String dateString) {
        Date date;
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }
}
