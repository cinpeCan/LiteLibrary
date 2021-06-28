package org.library.cinpe.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 分发器：加入状态内容处理  404 505 及首页
 * 
 * @author 黄灿
 *
 */
public class Dispatcher implements Runnable {
	private Socket client;
	private Request request;
	private Response response ;
	public Dispatcher(Socket client) {
		this.client = client;
		try {
//			//获取请求协议
//			InputStream is=client.getInputStream();
//			request =new Request(is);
//			//获取响应协议
//			OutputStream os=client.getOutputStream();
//			response =new Response(os);

			request =new Request(client);
			response =new Response(client);

		} catch (IOException e) {
			e.printStackTrace();
			this.release();
		}
	}
	@Override
	public void run() {	
		
		try {
			if(null== request.getUrl() || request.getUrl().equals("")) {
				System.out.println("失败");

				InputStream is =Thread.currentThread().getContextClassLoader().getResourceAsStream("../index.html");
                byte[] bytes=new byte[1024*1024*100];
                int i=is.read(bytes);
				response.print((new String(bytes,"utf-8")));
				response.pushToBrowser(200);
				is.close();
				return ;
			}
			Servlet servlet= WebApp.getServletFromUrl(request.getUrl());
			System.out.println("====================url:"+request.getUrl());

			if(null!=servlet) {
				System.out.println("没问题!!!!!!!!!!!!!!!!!");

				servlet.service(request, response);
				//关注了状态码
				response.pushToBrowser(200);
			}else {
				//错误....
				System.out.println("错误了!!!!!!!!!!!!!!!!!");
				InputStream is =Thread.currentThread().getContextClassLoader().getResourceAsStream("../index.html");

				byte[] bytes=new byte[1024*1024*100];
				int i=is.read(bytes);

				response.print((new String(bytes,"utf-8")));
				response.pushToBrowser(404);
				is.close();
			}		
		}catch(Exception e) {
			try {
				System.out.println("完全出错!!!!!!!!!!");
				response.println("好烦啊.");
				response.pushToBrowser(500);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}		
		release();
	}
	//释放资源
	private void release() {
		try {
			client.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
