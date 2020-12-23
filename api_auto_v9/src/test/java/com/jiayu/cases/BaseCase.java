package com.jiayu.cases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.Source;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.Replace;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.jiayu.pojo.API;
import com.jiayu.pojo.Case;
import com.jiayu.pojo.JsonPathValidate;
import com.jiayu.pojo.WriteBackData;
import com.jiayu.utils.AuthorizationUtils;
import com.jiayu.utils.ExcelUtils;
import com.jiayu.utils.HttpUtils;
import com.jiayu.utils.createMobileUtils;

import io.qameta.allure.Step;



public class BaseCase {
	
	public static Logger log=Logger.getLogger(BaseCase.class);
	
	
	/*
	 * 添加回写对象到回写集合中
	 * */
	public void addWBD(int rowNum,int cellNum, String body) {
		//创建一条回写的内容
		WriteBackData wbd=new WriteBackData(rowNum,cellNum,body);
		ExcelUtils.wbdList.add(wbd);
	}

	//调用接口
	public  String call(API api, Case c,boolean isAuthorization) {
		log.info("===调用接口："+api.getName()+"===");
		String url=api.getUrl();
		String type=api.getMethod();
		String contentType=api.getContentType();
		String param=c.getParams();
		System.out.println("——————————————");
		String body=HttpUtils.call(url, type, param, contentType,false);
		return body;
	}
	
	
	/*
	 * 如果case中ExpectValue是数组类型的json格式，那么采用多字段匹配断言逻辑。
	 * 如果case中ExpectValue不是数组类型的json格式，那么采用等值匹配。
	 * */
	
	@Step("断言响应结果")
	public boolean assertResponse(Case c, String body) {
		//定义断言是否成功返回值
		boolean flag =false;
		//获取expectValue
		String expactValue = c.getExpactValue();
		//调用parse方法解析json
		Object jsonObj=JSONObject.parse(expactValue);
		//如果case中ExpectValue是数组类型的json格式，那么采用多字段匹配断言逻辑。
		//jsonObj instanceof JSONArray 翻译过来就是 jsonObj是JSONArray的对象吗？
		if(jsonObj instanceof JSONArray ){
			//多字段匹配断言逻辑
			List<JsonPathValidate> list = JSONObject.parseArray(expactValue, JsonPathValidate.class);
			for (JsonPathValidate jpv : list) {
				//一个jsonpath断言表达式
				String expression = jpv.getExpression();
				//表达式期望值
				String value = jpv.getValue();
				//对响应结果进行一个jsonpath寻找实际值
				String actualValue = JSONPath.read(body, expression)==null ? "" : JSONPath.read(body, expression).toString();
				//期望值和实际值断言
				//[{"expression":"$.code","value":0},
				//{"expression":"$.msg","value":"OK"}]
				flag=value.equals(actualValue);
				if(flag==false){
					//说明断言失败
					break;
				}
				
				System.out.println("期望值："+value+", 实际值："+actualValue+"  单次断言结果："+value.equals(actualValue));
			}
		//如果case中ExpectValue不是数组类型的json格式，那么采用等值匹配。
		}else if (jsonObj instanceof JSONObject) {
			flag = body.equals(expactValue);
		}
		return flag;
		
	}
	
	/**
	 * 参数替换方法
	 * source 被替换的字符串   （params或者sql包含${XXX}）
	 * **/
	@Step("参数化替换")
	public String replace(String source){
		if(StringUtils.isBlank(source)){
			return source;
		}
		//遍历环境变量，取出所有的oldStr和newStr
		for (String oldStr : AuthorizationUtils.env.keySet()) {
			String newStr=AuthorizationUtils.env.get(oldStr);
			//判断source中是否包含oldStr，如果包含就替换newStr
			source=source.replace(oldStr, newStr);
		}
		return source;
	}
	
	
	
	@BeforeSuite
	@Step("初始化")
	public void BeforeSuite() throws Exception{
		log.info("===项目启动===");
		Properties prop=new Properties();
		FileInputStream fis=new FileInputStream("src/test/resources/params.properties");
		prop.load(fis);
//		把params.properties中读取的内容存储到env环境变量中。
		for (Object key : prop.keySet()) {
			Object value=prop.get(key);
			if(key.toString().contains("toBeRegisterMobilePhone")){
				//当你替换toBeRegisterMobilePhone值时，随机生成一个手机号码
				AuthorizationUtils.env.put(key.toString(), createMobileUtils.getMobilePhone());
			}else {
				AuthorizationUtils.env.put(key.toString(), value.toString());
			}
		}
	}
	
	
	@AfterSuite
	@Step("项目结束")
	public void finish(){
		//所有接口执行完之后
		//执行批量回写
		log.info("===批量回写===");
		ExcelUtils.batchWrite();
		log.info("===项目结束===");
	}
}
