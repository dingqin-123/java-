package com.jiayu.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.jiayu.constants.Contants;

public class HttpUtils {
	
	public static String json2keyValue(String json){
		//{"mobilephone":"13877788811","pwd":"12345678"}
		//mobilephone=13877788811&pwd=12345678
		
		//1、json转成map
		HashMap<String, String> map=JSONObject.parseObject(json,HashMap.class);
		//2、获取所有的key
		Set<String> keyset = map.keySet();
		//3、定义返回结果字符串
		String result="";
		//4、循环遍历所有key和value
		for (String key : keyset) {
			String value=map.get(key);
			result+=key+"="+value+"&";
		}
		result=result.substring(0,result.length()-1);
		System.out.println(result);
		return result;
	}
	
	/**
	 * call方法   发起http请求
	 * @param url
	 * @param type
	 * @param param
	 * @param contentType
	 */
	public static String  call(String url,String type,String param,String contentType,boolean isAuthorization){
		//如果是rest风格接口
		try {
			if("json".equalsIgnoreCase(contentType)){
				if("post".equalsIgnoreCase(type)){
					return HttpUtils.jsonPost(url, param,isAuthorization);
				}else if ("get".equalsIgnoreCase(type)) {
					return HttpUtils.jsonGet(url);
				}else if ("patch".equalsIgnoreCase(type)) {
					return HttpUtils.jsonPatch(url, param,isAuthorization);
				}
			//如果是表单类型接口
			}else if("form".equalsIgnoreCase(contentType)){
				if("post".equalsIgnoreCase(type)){
					//json参数转成form类型的参数
					param=json2keyValue(param);
					return HttpUtils.formPost(url, param,isAuthorization);
				}else if("get".equalsIgnoreCase(type)){
					return HttpUtils.formGet(url, param);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static String jsonGet(String url) throws Exception {
		//  /member/{member_id}/info
		//参数可能在中间也可能在最后，所以不好统一处理，那么传入url必须携带参数
		HttpGet get=new HttpGet(url);
		//4、有参传参有头加头
		get.addHeader(Contants.HEAD_MEDIA_TYPE_NAME,Contants.HEAD_MEDIA_TYPE_VALUE);
		//5、点击发送按钮
		CloseableHttpClient client = HttpClients.createDefault();
		//由客户端发出请求，6、接受响应
		CloseableHttpResponse response = client.execute(get);
		//response =body + statuscode + headers
		//body
		HttpEntity entity = response.getEntity();
		//statuscode
		int statuscode=response.getStatusLine().getStatusCode();
		//headers
		Header[] allHeaders=response.getAllHeaders();
		//工具类的命名规则=处理对象+Utils/s
		String body=EntityUtils.toString(entity);
		System.out.println(body);
		System.out.println(statuscode);
		System.out.println(Arrays.toString(allHeaders));
		return body;
	}
	
	
	public static String formGet(String url,String param) throws Exception {
		//1、创建request（请求）   2、选择请求方法method   3、填写url
		//url和参数处理代码
		HttpGet get=new HttpGet(url+"?"+param);
		//4、有参传参有头加头
		get.addHeader(Contants.HEAD_MEDIA_TYPE_NAME,Contants.HEAD_MEDIA_TYPE_VALUE);
		//5、点击发送按钮
		CloseableHttpClient client = HttpClients.createDefault();
		//由客户端发出请求，6、接受响应
		CloseableHttpResponse response = client.execute(get);
		//response =body + statuscode + headers
		//body
		HttpEntity entity = response.getEntity();
		//statuscode
		int statuscode=response.getStatusLine().getStatusCode();
		//headers
		Header[] allHeaders=response.getAllHeaders();
		//工具类的命名规则=处理对象+Utils/s
		String body=EntityUtils.toString(entity);
		System.out.println(body);
		System.out.println(statuscode);
		System.out.println(Arrays.toString(allHeaders));
		return body;	
	}
	
	
	public static String jsonPost(String url,String param,boolean isAuthorization) throws Exception {
		//1、创建request（请求） 2、选择请求方法method 3、填写url
		HttpPost post=new HttpPost(url);
		//4、有参传参有头加头
		post.addHeader(Contants.HEAD_MEDIA_TYPE_NAME,Contants.HEAD_MEDIA_TYPE_VALUE);
		post.addHeader("Content-Type","application/json");
		//添加鉴权头
		if(isAuthorization){ 
			AuthorizationUtils.setTokenInRequest(post);
		}
		post.setEntity(new StringEntity(param,"UTF-8"));
		//5、点击发送按钮
		CloseableHttpClient client = HttpClients.createDefault();
		//6、接受响应
		CloseableHttpResponse response = client.execute(post);
		//7、格式化响应内容（body 、 statuscode 、 headers）
		HttpEntity entity = response.getEntity();
		//statuscode
		int statuscode=response.getStatusLine().getStatusCode();
		//headers
		Header[] allHeaders=response.getAllHeaders();
		String body=EntityUtils.toString(entity);
		System.out.println(body);
		System.out.println(statuscode);
		System.out.println(Arrays.toString(allHeaders));
		return body;
	}
	
	
	public static String formPost(String url,String param,boolean isAuthorization) throws Exception{
		//1、创建request（请求） 2、选择请求方法method 3、填写url
		HttpPost post=new HttpPost(url);
		//4、有参传参有头加头
		post.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		//添加鉴权头
		if(isAuthorization){ 
			AuthorizationUtils.setTokenInRequest(post);
		}
		post.setEntity(new StringEntity(param,"UTF-8"));
		//5、点击发送按钮
		CloseableHttpClient client = HttpClients.createDefault();
		//6、接受响应
		CloseableHttpResponse response = client.execute(post);
		//7、格式化响应内容（body 、 statuscode 、 headers）
		HttpEntity entity = response.getEntity();
		//statuscode
		int statuscode=response.getStatusLine().getStatusCode();
		//headers
		Header[] allHeaders=response.getAllHeaders();
		String body=EntityUtils.toString(entity);
		System.out.println(body);
		System.out.println(statuscode);
		System.out.println(Arrays.toString(allHeaders));
		return body;
	}
	
	public static String jsonPatch(String url,String param,boolean isAuthorization) throws Exception{
		HttpPatch patch=new HttpPatch(url);
		//4、有参传参有头加头
		patch.addHeader(Contants.HEAD_MEDIA_TYPE_NAME,Contants.HEAD_MEDIA_TYPE_VALUE);
		patch.addHeader("Content-Type", "application/json");
		//添加鉴权头
		if(isAuthorization){ 
			AuthorizationUtils.setTokenInRequest(patch);
		}
		patch.setEntity(new StringEntity(param,"UTF-8"));
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(patch);
		HttpEntity entity = response.getEntity();
		//statuscode
		int statuscode=response.getStatusLine().getStatusCode();
		//headers
		Header[] allHeaders=response.getAllHeaders();
		String body=EntityUtils.toString(entity);
		System.out.println(body);
		System.out.println(statuscode);
		System.out.println(Arrays.toString(allHeaders));
		return body;
		
	}
	
	
//	public static void main(String[] args) throws Exception {
//		
//		String url="http://api.lemonban.com/futureloan/loans";
//		String url2="http://api.lemonban.com/futureloan/member/login";
//		String param2="{\"mobile_phone\": \"15580881245\",\"pwd\": \"12345678\"}";
//		String url3="http://test.lemonban.com/ningmengban/mvc/user/login.json";
//		String param3="username=15580801102&password=e10adc3949ba59abbe56e057f20f883e";
//		String url4="http://test.lemonban.com/futureloan/mvc/api/member/login";
//		String param4="mobilephone=15580881245&pwd=12345678";
		
//		jsonGet(url);
//		jsonPost(url2, param2);
//		formPost(url3, param3);
//		formGet(url4, param4);
		
//	}
}
