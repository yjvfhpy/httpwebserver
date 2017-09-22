/**
 * 
 */
package com.iammical.servlet;

import java.io.IOException;

/**
 * @Description:静态资源请求处理
 * @author micalliu
 * @date 2017年9月20日
 */
public class StaticResourceProcessor {
	public void process(Request request, Response response) {
		try {
			response.sendStaticResource();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
