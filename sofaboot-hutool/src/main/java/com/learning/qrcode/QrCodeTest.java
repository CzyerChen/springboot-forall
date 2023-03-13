/**
 * Author:   claire
 * Date:    2023/3/8 - 1:54 下午
 * Description: 二维码测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/3/8 - 1:54 下午          V1.0.0          二维码测试
 */
package com.learning.qrcode;

import cn.hutool.extra.qrcode.QrCodeException;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能简述 
 * 〈二维码测试〉
 *
 * @author claire
 * @date 2023/3/8 - 1:54 下午
 * @since 1.0.0
 */
public class QrCodeTest {

    @GetMapping("qrcode")
    @ResponseBody
    public void generateQrcode(HttpServletRequest request, HttpServletResponse response){
        String qrCodeUrl = "http://www.baidu.com";//需要生成的内容
        try {
            QrConfig qrConfig = new QrConfig(300, 300);
            //imageType可选："gif";"jpg";"jpeg"；"bmp";"png";"psd";"gif";"jpg";"jpeg"；"bmp";"png";"psd";
            QrCodeUtil.generate(qrCodeUrl, qrConfig, "png", response.getOutputStream());
            System.out.println("生成二维码成功!");
        } catch (QrCodeException | IOException e) {
            System.out.println("发生错误！ {}！"+ e.getMessage());
        }
    }

    public static void main(String[] args){

    }
}
