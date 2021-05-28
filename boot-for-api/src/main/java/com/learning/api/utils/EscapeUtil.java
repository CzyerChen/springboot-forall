/**
 * Author:   claire
 * Date:    2021-05-17 - 18:01
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-05-17 - 18:01          V1.0.0
 */
package com.learning.api.utils;

import cn.hutool.core.util.StrUtil;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2021-05-17 - 18:01
 * @since 1.0.0
 */
public class EscapeUtil {
    private static final char[][] TEXT = new char[64][];

    static {
        for (int i = 0; i < 64; i++) {
            TEXT[i] = new char[] { (char) i };
        }

        // special HTML characters
        TEXT['\''] = "&#039;".toCharArray(); // 单引号
        TEXT['"'] = "&#34;".toCharArray(); // 单引号
        TEXT['&'] = "&#38;".toCharArray(); // &符
        TEXT['<'] = "&#60;".toCharArray(); // 小于号
        TEXT['>'] = "&#62;".toCharArray(); // 大于号
    }

    /**
     * Escape编码 转义文本中的HTML字符为安全的字符
     */
    public static String escape(String text) {
        int len;
        if ((text == null) || ((len = text.length()) == 0)) {
            return StrUtil.EMPTY;
        }
        StringBuilder buffer = new StringBuilder(len + (len >> 2));
        char c;
        for (int i = 0; i < len; i++) {
            c = text.charAt(i);
            if (c < 64) {
                buffer.append(TEXT[c]);
            }
            else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    /**
     * Escape解码 还原被转义的HTML特殊字符
     */
    public static String unescape(String content) {
        if (StrUtil.isEmpty(content)) {
            return content;
        }

        StringBuilder tmp = new StringBuilder(content.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < content.length()) {
            pos = content.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (content.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                }
                else {
                    ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            }
            else {
                if (pos == -1) {
                    tmp.append(content.substring(lastPos));
                    lastPos = content.length();
                }
                else {
                    tmp.append(content.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }
}
