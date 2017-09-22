/**
 * 
 */
package com.iammical.thread;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description:http://127.0.0.1:8784/
 */
public class MultipThreadHttpServer {
	public static void main(String[] args) throws Exception {
		int port = 8784;
		ServerSocket serverSocket = null;
		
        System.out.println("开始监听，端口号："+port);

		while (true) {
			Socket socket = null;
			serverSocket = new ServerSocket(port);
			try {
				socket = serverSocket.accept(); 
				Thread workThread = new Thread(new Handler(socket)); 
				System.out.println("线程名称：" + workThread.getName());
				workThread.start(); 
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (serverSocket != null)
					serverSocket.close();
			}
		}

	}
}
