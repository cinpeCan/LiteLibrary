package org.library.cinpe.core;

public class WebApp {

	/**
	 * 通过url获取配置文件对应的servlet
	 * @param url
	 * @return
	 */
	public static Servlet getServletFromUrl(String url) {		

		if(url.equals("index.html")){
			return new indexServlet();
		}
		return null;

	}
}
