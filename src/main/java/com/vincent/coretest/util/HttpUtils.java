/**
 * 
 */
package com.vincent.coretest.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * com.vincent.coretest.util HttpUtils.java
 *
 * @author vincenttseng 2019年8月30日
 */
public class HttpUtils {
	protected static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

	public static HttpGet makeHttpGet(String callPath) {
		HttpGet get = new HttpGet(callPath);
		get.setHeader("Content-Type", "application/json");
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
		get.setConfig(requestConfig);
		return get;
	}

	public static String sendRequest(HttpRequestBase req) {
		String resultJson = null;
		try {
			CloseableHttpClient httpclient = HttpClients.custom().setConnectionManagerShared(true).build();

			CloseableHttpResponse response = httpclient.execute(req);
			resultJson = new BasicResponseHandler().handleResponse(response);
			response.close();
			httpclient.close();
		} catch (Exception e) {
			if (log != null) {
				log.error("Api Request Error", e);
			}
		}
		return resultJson;
	}

	public static String getLocalIP() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface currentInterface = interfaces.nextElement();
				if (!currentInterface.isUp() || currentInterface.isLoopback() || currentInterface.isVirtual()) {
					continue;
				}
				Enumeration<InetAddress> addresses = currentInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress inetAddress = addresses.nextElement();
					if (inetAddress.isLoopbackAddress()) {
						continue;
					} else {
						return inetAddress.getHostAddress(); // return the first available
					}
				}
			}
		} catch (SocketException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage(), e);
			}
		}
		return "";
	}

	public static String showURIWithoutQuery(String path) {
		if (StringUtils.isNotBlank(path)) {
			int index = path.indexOf("?");
			return (index > 0) ? path.substring(0, index) : path;
		} else {
			return path;
		}

	}

	public static String getPathRemovingifHttpMethod(String path) {
		int index = path.indexOf("/");
		if (index >= 0) {
			return StringUtils.trim(path.substring(index));
		} else {
			return StringUtils.trim(path);
		}
	}
}
