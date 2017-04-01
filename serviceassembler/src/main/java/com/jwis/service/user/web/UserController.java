package com.jwis.service.user.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jwis.service.user.service.inferf.UserService;
import com.jwis.serviceassembler.model.ResponseData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 第三方系统关于用户管理的服务接口
 * 错误码段： 503000~503099
 * @author samuel 
 *
 *
 */

@RestController
@RequestMapping(value = "/user")
public class UserController {
	public static Logger logger = Logger.getLogger(UserController.class);
	@Autowired  
	private UserService userService;
	
	
	/**
	 * 接收appgateway的重置第三方应用系统重置个人密码的请求，自动分发服务到各应用系统的adapter服务接口
	 * @param data
	 * [
	 * {
	 * "systemName":"Windchill",
	 * "userName":"xxxx",
	 * "oldPassword":"xxxx",
	 * "newPassword":"xxxx"
	 * }
	 * ]
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/resetPassword",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseData resetPassword(@RequestBody Object[] data,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		ResponseData responseData = new ResponseData();
		int index = 0;
		try {
			
			JSONArray result = JSONArray.fromObject(data);
			logger.info("<<<UserController.resetPassword.requestData<<<:"+data);
			for (Object object : data) {
				JSONObject jsonObj 	= JSONObject.fromObject(object);
				String systemName 	= String.valueOf(jsonObj.get("systemName")).trim();
				String userName	  	= String.valueOf(jsonObj.get("userName")).trim();
				String oldPassword 	= String.valueOf(jsonObj.get("oldPassword")).trim();
				String newPassword 	= String.valueOf(jsonObj.get("newPassword")).trim();
				Object object2 =  result.get(index++);
				JSONObject jsonObj2 = null;
				if(object2 instanceof JSONObject){
					jsonObj2 = (JSONObject)object2;
				}else {
					responseData.setCode(503002);
					responseData.setError("Resquest data is not json");
					responseData.setResult("");
					return responseData; 
				}
				if(StringUtils.isBlank(systemName)){
					logger.error("systemName must be not blank");
					jsonObj2.put("result","systemName must be not blank");
					continue;
				}
				if(StringUtils.isBlank(userName)){
					logger.error("userName must be not blank");
					jsonObj2.put("result","userName must be not blank");
					continue;
				}
				if(StringUtils.isBlank(oldPassword)){
					logger.error("oldPassword must be not blank");
					jsonObj2.put("result","oldPassword must be not blank");
					continue;
				}
				if(StringUtils.isBlank(newPassword)){
					logger.error("newPassword must be not blank");
					jsonObj2.put("result","newPassword must be not blank");
					continue;
				}
				String returnJson = userService.resetPassword(userName, oldPassword, newPassword, systemName,request);
				if(StringUtils.isBlank(returnJson) ){
					responseData.setCode(503001);
					responseData.setError("appgateway service down");
					responseData.setResult("");
					return responseData; 
				}
				JSONObject returnResult = null;
				try {
					returnResult= JSONObject.fromObject(returnJson);
				} catch (Exception e) {
					responseData.setCode(1);
					responseData.setError("");
					responseData.setResult(" password or Account  is error");
					return responseData;
				}
			
				if(StringUtils.isNotBlank(String.valueOf(returnResult.get("msg")))){
					jsonObj2.put("result",returnResult.get("msg"));
				}else {
					jsonObj2.put("result","");
				}
			}
			responseData.setError("");
			responseData.setResult(result.toString());
			logger.info("<<<UserController.resetPassword.responseData<<<:"+result.toString());
			return responseData;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			responseData.setCode(503000);
			responseData.setError("System error:"+e.getMessage());
			responseData.setResult("");
			return responseData; 
		}
	}
}
