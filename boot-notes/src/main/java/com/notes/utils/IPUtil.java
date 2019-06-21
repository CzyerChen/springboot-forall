package com.notes.utils;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {

	private static final String UNKNOWN = "unknown";

	protected IPUtil(){

	}

	/**
	 * 1.在没有代理，直接是裸奔的本地测试，直接通过remote_addr就能够获取客户端的ip
	 * 2.在实际生产环境中，一定会有像nginx这样的反向代理服务器，那么如何获得客户端的真是ip呢？
	 *
	 * 配置
	 * server {
	 *
	 *         listen       80;
	 *
	 *         server_name  localhost;
	 *
	 *         location /{
	 *
	 *             root   html;
	 *
	 *             index  index.html index.htm;
	 *
	 *                             proxy_pass                  http://backend; 
	 *
	 *            proxy_redirect              off;
	 *
	 *            proxy_set_header            Host $host;
	 *
	 *            proxy_set_header            X-real-ip $remote_addr;
	 *
	 *            proxy_set_header            X-Forwarded-For $proxy_add_x_forwarded_for;
	 *
	 *          # proxy_set_header            X-Forwarded-For $http_x_forwarded_for;
	 *
	 * }
	 *
	 * 方法一：记一个X-real-ip变量，记录拿到的remote_addr，最后在应用中，就通过获取X-real-ip来拿到客户端真实IP  request.getAttribute("X-real-ip")
	 *
	 * 方法二：通过X-Forwarded-For变量来实现，这个变量常态是记录上一个流转过来的地址，
	 * 如果书写为 X-Forwarded-For $http_x_forwarded_for; 只会记录上一个流转过来的地址，很多情况就只是nginx的地址，不是客户端的真实IP
	 * 采用 X-Forwarded-For $proxy_add_x_forwarded_for;是增加的模式，而不是覆盖的模式，因而会不断向后增加，用逗号隔开，那么第一次过来请求的客户端的真实IP就一直会在第一位
	 *
	 * X-Forwarded-For的值并不止一个，而是一串IP地址，
	 * X-Forwarded-For中第一个非 unknown的有效IP字符串，则为真实IP地址
	 */
	/**
	 * 这边就是基于第二种思想，进行真实IP的获取
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

}
