/**
 * Author:   claire
 * Date:    2021-02-09 - 17:59
 * Description: 附加功能测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 17:59          V1.17.0          附加功能测试
 */
package com.learning.extra;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.db.Session;
import cn.hutool.extra.emoji.EmojiUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;

import java.io.IOException;
import java.util.Iterator;

/**
 * 功能简述
 * 〈附加功能测试〉
 *
 * @author claire
 * @date 2021-02-09 - 17:59
 */
public class HCoreExtraTest {
    public static void main(String[] args) throws IOException {
        /*=============== 邮件工具-MailUtil ======================*/

        MailUtil.send("test@foxmail.com", "测试", "邮件来自测试", false);

        /*=============== 二维码工具-QrCodeUtil ======================*/
//        QrCodeUtil.generate("https://zy5205.cn/", 300, 300, FileUtil.file("/home/qrcode.jpg"));

        /*=============== Servlet工具-ServletUtil ======================*/

        /*=============== 模板引擎封装-TemplateUtil ======================*/

        /*=============== Jsch(SSH)工具-JschUtil ======================*/
        /*
         *           <dependency>
         *     <groupId>com.jcraft</groupId>
         *     <artifactId>jsch</artifactId>
         *     <version>0.1.54</version>
         *          </dependency>
         */
        //新建会话，此会话用于ssh连接到跳板机（堡垒机），此处为10.1.1.1:22
//        Session session = JschUtil.getSession("10.1.1.1", 22, "test", "123456");

        // 将堡垒机保护的内网8080端口映射到localhost，我们就可以通过访问http://localhost:8080/访问内网服务了
//        JschUtil.bindPort(session, "172.20.12.123", 8080, 8080);
        /*=============== FTP封装-Ftp ======================*/
        //匿名登录（无需帐号密码的FTP服务器）
        Ftp ftp = new Ftp("172.0.0.1");
        //进入远程目录
        ftp.cd("/opt/upload");
        //上传本地文件
        ftp.upload("/opt/upload", FileUtil.file("e:/test.jpg"));
        //下载远程文件
        ftp.download("/opt/upload", "test.jpg", FileUtil.file("e:/test2.jpg"));

        //关闭连接
        ftp.close();

        /*=============== EMOJI转换工具 EmojiUtil ======================*/
        /*
         *      <dependency>
         *     <groupId>com.vdurmont</groupId>
         *     <artifactId>emoji-java</artifactId>
         *     <version>4.0.0</version>
         *     </dependency>
         */
        String alias = EmojiUtil.toAlias("😄");//:smile:
        
        /*=============== 中文分词封装-TokenizerUtil ======================*/
        //自动根据用户引入的分词库的jar来自动选择使用的引擎
        TokenizerEngine engine = TokenizerUtil.createEngine();

        //解析文本
        String text = "这两个方法的区别在于返回值";
        Result result = engine.parse(text);
        //输出：这 两个 方法 的 区别 在于 返回 值
        String resultStr = CollUtil.join((Iterator<Word>)result, " ");

        /*===============  拼音工具-PinyinUtil ======================*/
        String pinyin = PinyinUtil.getPinyin("你好", " ");

        /*===============  图形验证码 CaptchaUtil ======================*/

        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);


    }
}
