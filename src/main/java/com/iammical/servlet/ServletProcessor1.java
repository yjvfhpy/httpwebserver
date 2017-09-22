/**
 * 
 */
package com.iammical.servlet;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Description:Servlet请求处理
 * @author micalliu
 * @date 2017年9月20日
 */
public class ServletProcessor1 {
	public void process(Request request, Response response) {
		String web_servlet_root = Constants.WEB_SERVLET_ROOT + "\\com\\iammical\\servlet";
		//String uri = request.getUri();
		//String servletName = uri.substring(uri.lastIndexOf("/") + 1);

		// 类加载器，用于从指定JAR文件或目录加载类
		URLClassLoader loader = null;
		try {
			URLStreamHandler streamHandler = null;
			// 创建类加载器
			loader = new URLClassLoader(new URL[] { new URL(null, "file:" + web_servlet_root, streamHandler) });
		} catch (IOException e) {
			System.out.println(e.toString());
		}

		Class<?> myClass = null;
		try {
			// 加载对应的servlet类
			myClass = loader.loadClass("com.iammical.servlet.PrimitiveServlet");
		} catch (ClassNotFoundException e) {
			System.out.println(e.toString());
		}

		Servlet servlet = null;
		try {
			// 生产servlet实例
			servlet = (Servlet) myClass.newInstance();
			// 执行ervlet的service方法
			servlet.service((ServletRequest) request, (ServletResponse) response);
		} catch (Exception e) {
			System.out.println(e.toString());
		} catch (Throwable e) {
			System.out.println(e.toString());
		}

	}
}
