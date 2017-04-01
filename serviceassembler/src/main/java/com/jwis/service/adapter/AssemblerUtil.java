package com.jwis.service.adapter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class AssemblerUtil {
	public static String getAuthorizationBySysType(String systemType,String userName,String userPassword){
		String result = "";
		if(systemType.equalsIgnoreCase(PLMSystemEnum.WINDCHILL.getName())){
			result =  "Basic "+Base64.getEncoder().encodeToString(new String(userName+":"+userPassword).getBytes());
		}else if(systemType.equalsIgnoreCase(PLMSystemEnum.ARAS.getName())){
			
		}
		return result;
	}
	
	/**
	 * 返回当前服务器的时间
	 * @return
	 */
	public static String getCurrentTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
	
	/**
	 * 返回当前请求方式
	 * @return
	 */
	public static String isHttpsOrhttp(String urls) throws Exception{
		String scheme = "https";
		URL url = new URL(urls);
		URI uri = url.toURI();
		scheme = uri.getScheme();
		return scheme;
	}
	

}
