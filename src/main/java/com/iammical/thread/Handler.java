/**
 * 
 */
package com.iammical.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Description:
 * @author micalliu
 */
public class Handler implements Runnable {
	private Socket socket;

	public Handler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			 System.out.println("新连接:"+socket.getInetAddress()+":"+socket.getPort());
			
			System.out.println(socket.getInetAddress());
			//准备读取客户端请求的数据，读取数据保存在buffer数ַ
			byte requestbuffer[] = new byte[4096];

			InputStream inp = socket.getInputStream();
			int length = inp.read(requestbuffer, 0, requestbuffer.length);
			System.out.println("rquestbuffer length :" + length);
			String requestString = new String(requestbuffer, "UTF-8");
			System.out.println(requestString);

			OutputStream out = socket.getOutputStream();
			String statusLine = "HTTP/1.1 200 OK\r\n";
			byte[] statusLineBytes = statusLine.getBytes("UTF-8");

			String responseBody = "<html><head><title>From Socket Server</title></head><body><h1>Hello, world.</h1></body></html>";
			byte[] responseBodyBytes = responseBody.getBytes("UTF-8");

			  // 回应的头部
			String responseHeader = "Content-Type: text/html; charset=UTF-8\r\nContent-Length: " + responseBody.length() + "\r\n";
			byte[] responseHeaderBytes = responseHeader.getBytes("UTF-8");
			
			// 向客户端发送状态信息
            out.write(statusLineBytes);

            // 向客户端发送回应头
            out.write(responseHeaderBytes);
            // 头部与内容的分隔行
            out.write(new byte[] { 13, 10 });
            // 向客户端发送内容部分
            out.write(responseBodyBytes);

            // 断开与客户端的连接
            socket.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				 System.out.println("关闭连接:"+socket.getInetAddress()+":"+socket.getPort());
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
