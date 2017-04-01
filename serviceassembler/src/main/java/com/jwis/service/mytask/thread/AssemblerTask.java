package com.jwis.service.mytask.thread;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jwis.service.adapter.AdapterUtil;
import com.jwis.service.adapter.AssemblerUtil;
import com.jwis.service.adapter.SystemAuthorth;
import com.jwis.service.adapter.TaskData;
import com.jwis.service.mytask.model.AssemblerConfig;
import com.jwis.service.mytask.service.impl.TasksServiceImpl;
import com.jwis.service.adapter.ServiceHttpClient;

public class AssemblerTask implements Callable<List<TaskData>> {
	public static Logger logger = Logger.getLogger(AssemblerTask.class);
	private String systemType = null;
	private String taskStatus = null;
	private String userCenterUrl = null;
	private String systemsUrl = null;
	private HttpServletRequest request = null;
	public AssemblerTask(String systemType, String taskStatus, String userCenterUrl,String systemsUrl,HttpServletRequest request){
		this.systemType = systemType;
		this.taskStatus = taskStatus;
		this.userCenterUrl = userCenterUrl;
		this.systemsUrl = systemsUrl;
		this.request = request;
	}
	@Override
	public List<TaskData> call() throws Exception {
		Map<String,String> header = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()){
			String nextElement = headerNames.nextElement();
			header.put(nextElement, request.getHeader(nextElement));
		}
//		header.put("ei", request.getHeader("ei"));
//		header.put("token", request.getHeader("token"));
//		logger.info("User Center request:"+this.userCenterUrl);
		String resultData =  ServiceHttpClient.doRequest(this.userCenterUrl,header);
//		logger.info("【data】:"+resultData);
		SystemAuthorth sysAuthor = AdapterUtil.parseAuthInfo(resultData);
//		SystemAuthorth sysAuthor = new SystemAuthorth();
//		sysAuthor.setUserName("wcadmin");
//		sysAuthor.setPassword("wcadmin");
		List<TaskData> systemTasks = new ArrayList<TaskData>();
		if(sysAuthor !=null && sysAuthor.getUserName() != null && !StringUtils.isEmpty(sysAuthor.getUserName())
				&& sysAuthor.getPassword() != null && !StringUtils.isEmpty(sysAuthor.getPassword())){
//			Map<String,String> headerMap = new HashMap<String,String>();
			if(sysAuthor != null && sysAuthor.getUserName() != null && !sysAuthor.getUserName().equals("") && sysAuthor.getPassword() != null && !sysAuthor.getPassword().equals("")){
				String authorization = AssemblerUtil.getAuthorizationBySysType(systemType, sysAuthor.getUserName(), sysAuthor.getPassword());
				header.put("authorization", authorization);
			}
//			header.put("type","tru.ptcmate");
			StringBuilder sysUrl = new StringBuilder(systemsUrl);
			sysUrl.append("/").append(sysAuthor.getUserName()).append("/").append(this.taskStatus);
			String taskListData = ServiceHttpClient.doRequest(sysUrl.toString(), header);
		System.out.println("<<<data:"+taskListData);
//			logger.info("【data2】:"+taskListData);
			systemTasks = AdapterUtil.parseTaskDatas(taskListData);
		}
		return systemTasks;
	}
	

	
}
