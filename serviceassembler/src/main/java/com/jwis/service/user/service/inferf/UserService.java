package com.jwis.service.user.service.inferf;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
	String resetPassword(String userName,String oldPassword,String newPassword,String targetSystem,HttpServletRequest request) throws Exception;
}
