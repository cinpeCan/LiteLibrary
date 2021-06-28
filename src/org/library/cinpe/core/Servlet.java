package org.library.cinpe.core;
/**
 * 服务器小脚本接口
 * 
 * @author 黄灿
 *
 */
public interface Servlet {
	void service(Request request, Response response);
}
