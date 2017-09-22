/**
 * 
 */
package com.iammical.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description:Servlet容器实现
 * 
 * 
 * 							1.创建一个ServerSocket对象；
 *                          2.调用ServerSocket对象的accept方法，等待连接，连接成功会返回一个Socket对象，
 *                          否则一直阻塞等待； 3.从Socket对象中获取InputStream和OutputStream字节流，
 *                          这两个流分别对应request请求和response响应；
 *                          4.处理请求：读取InputStream字节流信息，转成字符串形式，并解析，这里的解析比较简单，
 *                          仅仅获取uri(统一资源标识符)信息;
 *                          5.处理响应（分两种类型，静态资源请求响应或servlet请求响应）：如果是静态资源请求，
 *                          则根据解析出来的uri信息，从WEB_ROOT目录中寻找请求的资源资源文件,
 *                          读取资源文件，并将其写入到OutputStream字节流中；如果是Servlet请求，
 *                          则首先生成一个URLClassLoader类加载器，加载请求的servlet类，创建servlet对象，
 *                          执行service方法（往OutputStream写入响应的数据）； 6.关闭Socket对象；
 *                          7.转到步骤2，继续等待连接请求；
 * 
 * 
 * @author micalliu
 * @date 2017年9月20日
 */
public class HttpServer {
	// 关闭服务命令
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

	public static void main(String[] args) {
		HttpServer server = new HttpServer();
		// 等待连接请求
		server.await();
	}

	public void await() {
		ServerSocket serverSocket = null;
		int port = 8080;
		try {
			// 服务器套接字对象
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// 循环等待请求
		while (true) {
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try {
				// 等待连接，连接成功后，返回一个Socket对象
				socket = serverSocket.accept();
				input = socket.getInputStream();
				output = socket.getOutputStream();

				// 创建Request对象并解析
				Request request = new Request(input);
				request.parse();
				// 检查是否是关闭服务命令
				if (SHUTDOWN_COMMAND.equals(request.getUri())) {
					break;
				}

				// 创建 Response 对象
				Response response = new Response(output);
				response.setRequest(request);

				String uri = request.getUri();

				if (!"".equals(uri) && uri != null && !"null".equals(uri)) {
					if (request.getUri().startsWith("/servlet/")) {
						// 请求uri以/servlet/开头，表示servlet请求
						ServletProcessor2 processor = new ServletProcessor2();
						processor.process(request, response);
					} else {
						// 静态资源请求
						StaticResourceProcessor processor = new StaticResourceProcessor();
						processor.process(request, response);
					}

					// 关闭 socket
					socket.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
