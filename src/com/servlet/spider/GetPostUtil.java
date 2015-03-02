package com.servlet.spider;
import java.io.*;
import java.net.*;

/**
 * Post 和 Get两个方法的代码总结
 * 
 * @author javer
 * 
 */
public class GetPostUtil {
	private static URL connectedUrl;		
	private static String untreatedCookie;
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数,请求参数应该为 name1 = value1 & name2= value2的形式
	 * @param cookie
	 * 				cookie为获取资源所需要的cookie,假如不需要cookie,可传参数null
	 * @return String result 所代表远程资源的响应(一般为html文档)
	 */
	static String sendGet(String url, String params, String cookie) {
		String result = "";	
		BufferedReader in = null;
		try {
			if (params != null) {
				url = url + "?" + params;
			}
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			if (cookie != null)
				conn.setRequestProperty("cookie", cookie);
			// 建立实际的连接
			conn.connect();
			connectedUrl = conn.getURL();
			// 遍历所有的相应头字段
			// 定义BufferedReader输入流来读取URL的响应, GB2312表明是国标
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GB2312"));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常!" + e);
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {	in.close(); }
				} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数,请求参数应该为 name1 = value1 & name2 = value2的形式
	 * @return URL 所代表远程资源的响应
	 */
	static String sendPost(String url, String params) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			HttpURLConnection.setFollowRedirects(false);// 重定向设为false,为了cookie还是在原来的网站上取
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			System.out.println("zheng zai zhi xing post");
			out.print(params);
			// flush输出流的缓冲
			out.flush();
			untreatedCookie = conn.getHeaderField("Set-Cookie");
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常! :  " + e);
			e.printStackTrace();
		}
		// 使用finally快来关闭输入流, 输出流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * @return  	 返回上一次连接的URL
	 */
	static URL getConnectedUrl() {
		return connectedUrl;
	}
	/**
	 * @return 有效的cookie值,假如返回的是null, 证明cookie未能有效获取
	 */
	static String getCookie() {
		if(untreatedCookie != "" && untreatedCookie != null)
			return untreatedCookie.substring(0, 30);
		else
			return null;
	}
}
