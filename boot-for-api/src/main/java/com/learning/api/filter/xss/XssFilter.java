/**
 * Author:   claire
 * Date:    2021-05-17 - 17:54
 * Description: XSS过滤
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-05-17 - 17:54          V1.0.0          XSS过滤
 */
package com.learning.api.filter.xss;

import cn.hutool.core.util.StrUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能简述 
 * 〈XSS过滤〉
 *
 * @author claire
 * @date 2021-05-17 - 17:54
 * @since 1.0.0
 */
public class XssFilter implements Filter {
    /**
     * 排除链接
     */
    private List<String> excludes = new ArrayList<>();

    /**
     * xss过滤开关
     */
    private boolean enabled = false;

    @Override
    public void init(FilterConfig filterConfig) {
        String tempExcludes = filterConfig.getInitParameter("excludes");
        String tempEnabled = filterConfig.getInitParameter("enabled");
        if (StrUtil.isNotEmpty(tempExcludes)) {
            String[] url = tempExcludes.split(",");
            Collections.addAll(excludes, url);
        }
        if (StrUtil.isNotEmpty(tempEnabled)) {
            enabled = Boolean.valueOf(tempEnabled);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (handleExcludeUrl(req)){
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    /**
     * 判断当前路径是否需要过滤
     */
    private boolean handleExcludeUrl(HttpServletRequest request) {
        if (!enabled) {
            return true;
        }
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        String url = request.getServletPath();
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }
}
