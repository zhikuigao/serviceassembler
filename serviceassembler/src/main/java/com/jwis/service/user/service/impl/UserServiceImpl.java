package com.jwis.service.user.service.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwis.service.adapter.AssemblerUtil;
import com.jwis.service.adapter.ServiceHttpClient;
import com.jwis.service.mytask.model.AssemblerConfig;
import com.jwis.service.user.service.inferf.UserService;

@Service
public class UserServiceImpl implements UserService {
	public static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired  
    private AssemblerConfig assemblerConfig;
	@Override
	public String resetPassword(String userName, String oldPassword, String newPassword,String targetSystem,HttpServletRequest request) throws Exception{
		//1、获取appgateway服务的提供者
		Map<String,String> headerMap = new HashMap<String,String>();
		StringBuilder systemUrl = new StringBuilder(assemblerConfig.getApp_gateway_address());
		if(!StringUtils.endsWith(systemUrl.toString(),"/")){
			systemUrl.append("/");
		}
		//2、调用目标系统的adapter服务，
		if(StringUtils.isBlank(targetSystem)){
			logger.error("target system have not been setted");
		}
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()){
			String nextElement = headerNames.nextElement();
			headerMap.put(nextElement, request.getHeader(nextElement));
		}
		headerMap.remove("content-length");
		systemUrl.append("service/").append(targetSystem.toLowerCase()).append("adapter").append("/user/resetPassword");
		systemUrl.append("/").append(userName).append("/").append(oldPassword).append("/").append(newPassword);
		
		
		if(StringUtils.isNotBlank(userName)&&StringUtils.isNotBlank(oldPassword)){
			String authorization = AssemblerUtil.getAuthorizationBySysType(targetSystem, userName, oldPassword);
			headerMap.put("authorization", authorization);
		}else {
			headerMap.put("authorization", "Basic d2NhZG1pbjp3Y2FkbWlu");
			
		}
		String resultData =  ServiceHttpClient.sendGetRequest(systemUrl.toString(), headerMap);
		return resultData;
	}
	
	
}
