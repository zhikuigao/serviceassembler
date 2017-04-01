package com.jwis.service.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class ServiceHttpClient {
	private static final Logger logger = Logger.getLogger(ServiceHttpClient.class);
	private static final Map<String,String> baseHeaderMap = new HashMap<String,String>();
	static{
		baseHeaderMap.put("accept","*/*");
		baseHeaderMap.put("content-type","application/json");
		baseHeaderMap.put("accept-encoding","UTF-8");
		baseHeaderMap.put("accept-language","en-US,en;q=0.9");
		baseHeaderMap.put("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
	}
	/**
	 * 发送HTTP 请求
	 * 所使用的java库为 HttpClient
	 * 
	 */
	public static String sendGetRequest(String postUrl,Map<String,String> customHeaderMap){
		String respContent = "";
		RequestConfig requestConfig = RequestConfig.custom()  
	            .setSocketTimeout(15000)  
	            .setConnectTimeout(15000)  
	            .setConnectionRequestTimeout(15000)  
	            .build(); 
		
		HttpGet httpGet = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try{
			Map<String,String> newHeaderMapper = new HashMap<String,String>();
			newHeaderMapper.putAll(baseHeaderMap);
			httpGet = new HttpGet(postUrl);
			if(customHeaderMap != null && customHeaderMap.size() > 0){
				newHeaderMapper.putAll(customHeaderMap);
			}
			Set<String> keys = newHeaderMapper.keySet();
			for(String key : keys){
				httpGet.setHeader(key, newHeaderMapper.get(key));
			}
			httpGet.setConfig(requestConfig);  
			httpClient = HttpClients.createDefault(); 
			response = httpClient.execute(httpGet);  
			entity = response.getEntity();
			respContent = EntityUtils.toString(entity, "UTF-8");
		}catch (Exception e) {
			logger.error("send Http Request to:"+postUrl+" ,has errors:"+e.getMessage());
		}finally {
			try{
				if(response != null)
					response.close();
				if(httpClient != null)
					httpClient.close();
			}catch (Exception e) {
				logger.error("close Response OR httpClient to:"+postUrl+" ,has errors:"+e.getMessage());
			}
		}
		return respContent;
	}
	
    /** 
     * 发送get请求 
     * @param url       链接地址 
     * @param charset   字符编码，若为null则默认utf-8 
     * @return 
     */  
    public  static String doGet(String url,Map<String,String> customHeaderMap){  
		String respContent = "";
        String charset = "utf-8";  
        HttpClient httpClient = null;  
        HttpGet httpGet= null;  
        HttpResponse response = null;
    	RequestConfig requestConfig = RequestConfig.custom()  
	            .setSocketTimeout(15000)  
	            .setConnectTimeout(15000)  
	            .setConnectionRequestTimeout(15000)  
	            .build(); 
        try {  
        	Map<String,String> newHeaderMapper = new HashMap<String,String>();
			newHeaderMapper.putAll(baseHeaderMap);
			if(customHeaderMap != null && customHeaderMap.size() > 0){
				newHeaderMapper.putAll(customHeaderMap);
			}
	        httpClient = new SSLClient();  
            httpGet = new HttpGet(url);  
			Set<String> keys = newHeaderMapper.keySet();
			for(String key : keys){
				httpGet.setHeader(key, newHeaderMapper.get(key));
			}
            httpGet.setConfig(requestConfig);
             response = httpClient.execute(httpGet);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                	respContent = EntityUtils.toString(resEntity,charset);  
                }  
            }  
        } catch (Exception e) {  
        	logger.error("close Response OR httpClient to:"+url+" ,has errors:"+e.getMessage());
        }
        return respContent;  
    }  
    
    public  static String doRequest(String url,Map<String,String> customHeaderMap){  
    	String data = "";
    	try {
    		String requestType = AssemblerUtil.isHttpsOrhttp(url);
    		
    		if("http".equals(requestType)){
    			data = ServiceHttpClient.sendGetRequest(url,customHeaderMap);
    		}else{
    			data = 	ServiceHttpClient.doGet(url,customHeaderMap);
    		}
		} catch (Exception e) {
		 	logger.error("is http or https:"+url+" ,has errors:"+e.getMessage());
		}
		return data;
    }
    
}
