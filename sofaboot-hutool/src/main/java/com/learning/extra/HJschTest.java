/**
 * Author:   claire
 * Date:    2021-03-31 - 15:00
 * Description: jsch测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-03-31 - 15:00          V1.17.0          jsch测试
 */
package com.learning.extra;

import cn.hutool.extra.ssh.ChannelType;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import java.nio.charset.Charset;

/**
 * 功能简述
 * 〈jsch测试〉
 *
 * @author claire
 * @date 2021-03-31 - 15:00
 * @since 1.1.0
 */
public class HJschTest {
    private static final String USER="***";
    private static final String PASSWORD="****";
    private static final String HOST="localhost";
    private static final int DEFAULT_SSH_PORT=22;

    public static void main(String[] arg){

        try{
            JSch jsch=new JSch();
            Session session = jsch.getSession(USER,HOST,DEFAULT_SSH_PORT);
            session.setPassword(PASSWORD);

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

            session.setUserInfo(userInfo);

            //session.connect();
            session.connect(30000);   // making a connection with timeout.

            Channel channel=session.openChannel("shell");

            channel.setInputStream(System.in);

            channel.setOutputStream(System.out);

            //channel.connect();
            channel.connect(3*1000);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

}
