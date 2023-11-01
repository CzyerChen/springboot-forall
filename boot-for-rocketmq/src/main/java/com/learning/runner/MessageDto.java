/**
 * Author:   claire
 * Date:    2023/10/26 - 3:25 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/10/26 - 3:25 下午          V1.0.0
 */
package com.learning.runner;

/**
 *
 * @author claire
 * @date 2023/10/26 - 3:25 下午
 * @since 1.0.0
 */
public class MessageDto {
    private static final long serialVersionUID =1L;

    private String index;
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "index='" + index + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
