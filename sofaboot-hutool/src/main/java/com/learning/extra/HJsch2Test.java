/**
 * Author:   claire
 * Date:    2021-03-31 - 15:15
 * Description: jsch测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-03-31 - 15:15          V1.17.0          jsch测试
 */
package com.learning.extra;

import cn.hutool.extra.ssh.ChannelType;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * 功能简述 
 * 〈jsch测试〉
 *
 * @author claire
 * @date 2021-03-31 - 15:15
 * @since 1.1.0
 */
public class HJsch2Test {
        public static void main(String[] args) {
        Session session = null;
            UserInfo userInfo = new UserInfo() {
                @Override
                public String getPassphrase() {
                    System.out.println("getPassphrase");
                    return null;
                }
                @Override
                public String getPassword() {
                    System.out.println("getPassword");
                    return null;
                }
                @Override
                public boolean promptPassword(String s) {
                    System.out.println("promptPassword:"+s);
                    return false;
                }
                @Override
                public boolean promptPassphrase(String s) {
                    System.out.println("promptPassphrase:"+s);
                    return false;
                }
                @Override
                public boolean promptYesNo(String s) {
                    System.out.println("promptYesNo:"+s);
                    return true;//notice here!
                }
                @Override
                public void showMessage(String s) {
                    System.out.println("showMessage:"+s);
                }
            };
        try {
            session = JschUtil.getSession("***", 22, "**", "**");
            session.setUserInfo(userInfo);
//            session.connect(30000);
            Channel channel = JschUtil.createChannel(session, ChannelType.SHELL);
            channel.setInputStream(System.in);

            channel.setOutputStream(System.out);
            channel.connect(3*1000);

        }catch (Exception e){
             e.printStackTrace();
        }finally {
//            session.disconnect();
        }


    }
}
