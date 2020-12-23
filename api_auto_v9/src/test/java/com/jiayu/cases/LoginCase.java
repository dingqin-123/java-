package com.jiayu.cases;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.jiayu.constants.Contants;
import com.jiayu.pojo.API;
import com.jiayu.pojo.Case;
import com.jiayu.pojo.JsonPathValidate;
import com.jiayu.pojo.WriteBackData;
import com.jiayu.utils.AuthorizationUtils;
import com.jiayu.utils.ExcelUtils;
import com.jiayu.utils.HttpUtils;
import com.jiayu.utils.SqlUtils;

import io.qameta.allure.Description;

public class LoginCase extends BaseCase{
	
	@Test(dataProvider="datas",description="登录")
	@Description("登录------xxxx------xxxx")
	public void testLogin(API api,Case c) throws Exception{
		//1、参数化替换
		String params=replace(c.getParams());
		String sql=replace(c.getSql());
		c.setParams(params);
		c.setSql(sql);
		//2、数据库前置查询结果（测试断言必须在接口执行前后都执行）
		Object beforeSqlResult = SqlUtils.querySingle(c.getSql());
		//3、调用接口
		String body = call(api, c,false);
		//3.1  token存储   登录接口特有
		AuthorizationUtils.storeTokenAndMemberId(body);
		//4、断言响应结果
		//[{"expression":"$.code","value":"0"},{"expression":"$.msg","value":"ok"}]
		boolean assertResponseFlag = assertResponse(c, body);
		//5、添加接口响应回写内容
		addWBD(Integer.parseInt(c.getId()),Contants.ACTUAL_WRITER_BACK_CELL_NUM,body);
		//6、数据库后置查询结果
		Object afterSqlResult = SqlUtils.querySingle(c.getSql());
		//7、数据库断言
		
		//8、添加断言回写内容
		String assertContent=(assertResponseFlag) ? "PASS" : "FAIL";
		addWBD(Integer.parseInt(c.getId()),Contants.ASSERT_RESULT_CELL_NUM,assertContent);
		
		//9、添加日志
		//10、报表断言
		Assert.assertEquals(assertContent, "PASS");
		
		}

	@DataProvider(name="datas")
	public Object[][] datas(){	
		//1、怎么把API和Case联系起来
		//2、把API和Case放入Object[][]
		Object[][] datas = ExcelUtils.getAPIandByApiId("2");
		return datas;
	}
	


}
