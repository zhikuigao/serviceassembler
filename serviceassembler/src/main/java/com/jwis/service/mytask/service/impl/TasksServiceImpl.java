package com.jwis.service.mytask.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwis.service.adapter.AssemblerUtil;
import com.jwis.service.adapter.SystemTasks;
import com.jwis.service.adapter.TaskData;
import com.jwis.service.mytask.model.AssemblerConfig;
import com.jwis.service.mytask.service.interf.TasksService;
import com.jwis.service.mytask.thread.AssemblerTask;

@Service
public class TasksServiceImpl implements TasksService {
	public static Logger logger = Logger.getLogger(TasksServiceImpl.class);
	@Autowired  
    private AssemblerConfig assemblerConfig;
	
	@Override
	public SystemTasks getTaskList(String userId, String taskStatus, Map<String,String> systemPathMap,HttpServletRequest request) {
		ExecutorService threadService = Executors.newCachedThreadPool();
		SystemTasks systemTasks = new SystemTasks();
		systemTasks.setCurrentTime(AssemblerUtil.getCurrentTime());
		systemTasks.setUserId(userId);
		List<TaskData> taskData = new ArrayList<TaskData>();
		systemTasks.setData(taskData);
		if(systemPathMap != null && systemPathMap.size() > 0){
			Set<String> systems = systemPathMap.keySet();
			for(String sys : systems){
				try {
					StringBuilder userCenterUrl = new StringBuilder(assemblerConfig.getApp_gateway_address());
					StringBuilder systemUrl = new StringBuilder(assemblerConfig.getApp_gateway_address());
					userCenterUrl.append(assemblerConfig.getUsercenter_service_path()).append("?userId=").append(userId).append("&system=").append(sys);
					systemUrl.append(systemPathMap.get(sys));
					AssemblerTask assemblerTask = new AssemblerTask(sys,taskStatus,userCenterUrl.toString(),systemUrl.toString(), request);
					Future<List<TaskData>> taskDataList =  threadService.submit(assemblerTask);
					taskData.addAll(taskDataList.get());
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				} catch (ExecutionException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		threadService.shutdown();
		while(true){
			if (threadService.isTerminated()) { 
                break; 
            } 
		}
		return systemTasks;
	}
}
