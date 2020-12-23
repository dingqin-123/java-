package com.jiayu.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;

import com.alibaba.fastjson.JSONPath;

public class AuthorizationUtils {
	
	public static final Map<String, String> env=new HashMap<String, String>();
	
	/*
	 * 1、从接口响应中获取token信息
	 * 2、把token信息存储到环境变量中
	 * */
	
	public static void storeTokenAndMemberId(String response){
		//从接口响应中获取token
		Object token=JSONPath.read(response, "$.data.token_info.token");
		//token不等于空，说明登录成功
		if(token!=null){
			//存储token到环境变量中
			env.put("${token}", token.toString());
			//token不为空那么获取id肯定是memberId
			Object memberId=JSONPath.read(response, "$.data.id");
			if(memberId!=null){
				//存储memberId
				env.put("${member_id}", memberId.toString());
			}
			
		}
	}
	
	public static void setTokenInRequest(HttpRequest request){
		//从环境变量中取出token
		String token=env.get("token");
		//如果token存在
		if(StringUtils.isNotBlank(token)){
			//添加鉴权头
			request.setHeader("Authorization", "Bearer "+token);
		}
	}

}
