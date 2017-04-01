package com.jwis.service.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AdapterUtil {
	/**
	 * 公共方法
	 * 说明:用于解析从user center获取到的数据，并解析成SystemAuthorth对象
	 * @param author
	 * @return
	 */
	public static SystemAuthorth parseAuthInfo(String author){
		SystemAuthorth sysAuthor = null;
		JSONObject json = JSONObject.fromObject(author);
		JSONObject info = json.getJSONObject("result");
		if(!info.isNullObject() && !info.isEmpty()){
			sysAuthor = new SystemAuthorth();
			sysAuthor.setUserName(info.getString("username"));
			sysAuthor.setPassword(info.getString("password"));
		}
		return sysAuthor;
	}
	
	/**
	 * 公共方法
	 * 说明:解析从adapater获取到的数据，并反序列化成SystemTasks对象
	 * @param taskInfo
	 * @return
	 */
	public static SystemTasks parseTasksList(String taskInfo){
		SystemTasks systemTask = null;
		JSONObject json = JSONObject.fromObject(taskInfo);
		if(!json.isNullObject() && !json.isEmpty()){
			systemTask = new SystemTasks();
			systemTask.setResult(json.getInt("result"));
			systemTask.setErrorMsg(json.getString("errorMsg"));
			systemTask.setUserId(json.getString("userId"));
			systemTask.setData(parseTaskDatas(json.getString("data")));
		}
		return systemTask;
	}
	
	/**
	 * 公共方法
	 * 说明:用于解析从Windchill或aras等PLM系统获取到的任务数据集
	 * @param taskArray
	 * @return
	 */
	public static List<TaskData> parseTaskDatas(String taskArray){
		List<TaskData> taskDataList = new ArrayList<TaskData>();
		if(taskArray.startsWith("[")){
			JSONArray jsonArray = JSONArray.fromObject(taskArray);
			if(jsonArray.isArray() && !jsonArray.isEmpty()){
				taskDataList = new ArrayList<TaskData>();
				for(int index = 0 ;index < jsonArray.size() ; index ++){
					JSONObject taskObj =  jsonArray.getJSONObject(index);
					if(!taskObj.isNullObject() && !taskObj.isEmpty()){
						TaskData taskData = (TaskData)JSONObject.toBean(taskObj, TaskData.class);
						taskDataList.add(taskData);
					}
				}
			}
		}
		return taskDataList;
	}
	
	/**
	 * 公共方法
	 * 说明:解析assembler系统中的配置文件，解析成映射表，并通过系统名称查询出对应的系统路径
	 * @param sysMapping
	 * @param sysName
	 * @return
	 */
	public static Map<String,String> getPathBySys(String sysMapping,String sysName){
		Map<String,String> sysPath = null;
		if(sysMapping != null && StringUtils.isNotEmpty(sysMapping)){
			Map<String,String> pathsMapper = new HashMap<String,String>();
			String[] sysArray = sysMapping.split(",");
			for(String sys : sysArray){
				if(sys != null && StringUtils.isNotEmpty(sys)){
					String[] sysPaths = sys.split("[|]");
					pathsMapper.put(sysPaths[0].toLowerCase(), sysPaths[1]);
				}
			}
			if(pathsMapper != null && pathsMapper.size() > 0){
				sysPath = new HashMap<String, String>();
				if(sysName.equalsIgnoreCase(PLMSystemEnum.ALL.getName())){
					for(String name : pathsMapper.keySet()){
						String mySysPath = pathsMapper.get(name);
						if(mySysPath != null && StringUtils.isNotBlank(mySysPath)){
							sysPath.put(name, mySysPath);
						}
					}
				}else{
					String mySysPath = pathsMapper.get(sysName.toLowerCase());
					if(mySysPath != null && StringUtils.isNotBlank(mySysPath)){
						sysPath.put(sysName, mySysPath);
					}
				}
			}
		}
		return sysPath;
	}
}
