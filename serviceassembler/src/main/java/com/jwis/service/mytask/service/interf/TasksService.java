package com.jwis.service.mytask.service.interf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jwis.service.adapter.SystemTasks;

public interface TasksService {
	SystemTasks getTaskList(String userId,String taskStatus,Map<String,String> systemPathMap,HttpServletRequest request);
}
