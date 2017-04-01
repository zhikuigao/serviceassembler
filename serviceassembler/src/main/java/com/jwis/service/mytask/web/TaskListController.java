package com.jwis.service.mytask.web;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jwis.service.adapter.AdapterUtil;
import com.jwis.service.adapter.SystemTasks;
import com.jwis.service.mytask.model.AssemblerConfig;
import com.jwis.service.mytask.service.interf.TasksService;
import com.jwis.serviceassembler.model.ResponseData;

import net.sf.json.JSONObject;
/**
 * 第三方任务相关的服务接口
 * 错误码段： 503100~503199
 * @author samuel
 *
 */

@RestController
@RequestMapping(value = "/myTask")
public class TaskListController {
	public static Logger logger = Logger.getLogger(TaskListController.class);
	@Autowired  
    private AssemblerConfig assemblerConfig;
	@Autowired  
	private TasksService tasksService;
	
	@RequestMapping("/getTaskList/{userId}/{taskStatus}/{source}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseData getTaskList(@PathVariable String userId,@PathVariable String taskStatus,@PathVariable String source,HttpServletRequest request,HttpServletResponse response) 
			throws UnsupportedEncodingException{
		ResponseData responseData = new ResponseData();
		try {
			logger.info("<<<TaskListController.getTaskList.requestData<<<userId.taskStatus.source:"+userId+taskStatus+source);
			String systemsServicePath = assemblerConfig.getSystems_service_path();
			Map<String,String> systemPathMap = AdapterUtil.getPathBySys(systemsServicePath, source);
			SystemTasks systemTasksList = tasksService.getTaskList(userId, taskStatus, systemPathMap,request);
			JSONObject jsonObj = JSONObject.fromObject(systemTasksList);
			responseData.setError("");
			responseData.setResult(jsonObj.toString());
			logger.info("<<<UserController.resetPassword.responseData<<<:"+jsonObj.toString());
			return responseData;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			responseData.setCode(503100);
			responseData.setError("System error:"+e.getMessage());
			responseData.setResult("");
			return responseData; 
		}
	}
}
