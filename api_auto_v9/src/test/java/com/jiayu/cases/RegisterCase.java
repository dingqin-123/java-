package com.jiayu.cases;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.jiayu.constants.Contants;
import com.jiayu.pojo.API;
import com.jiayu.pojo.Case;
import com.jiayu.pojo.WriteBackData;
import com.jiayu.utils.AuthorizationUtils;
import com.jiayu.utils.ExcelUtils;
import com.jiayu.utils.HttpUtils;
import com.jiayu.utils.SqlUtils;

public class RegisterCase extends BaseCase{
	
	@Test(dataProvider="datas")
	public void testRegister(API api,Case c) throws Exception{
		//1、参数化替换
		String params=replace(c.getParams());
		String sql=replace(c.getSql());
		c.setParams(params);
		c.setSql(sql);
		//2、数据库前置查询结果（测试断言必须在接口执行前后都执行）
		Object beforeSqlResult = SqlUtils.querySingle(c.getSql());
		//3、调用接口
		String body = call(api, c,false);
		//4、断言响应结果
		//[{"expression":"$.code","value":"0"},{"expression":"$.msg","value":"ok"}]
		boolean assertResponseFlag = assertResponse(c, body);
		//5、添加接口响应回写内容
		addWBD(Integer.parseInt(c.getId()),Contants.ACTUAL_WRITER_BACK_CELL_NUM,body);
		//6、数据库后置查询结果
		Object afterSqlResult = SqlUtils.querySingle(c.getSql());
		//7、数据库断言
		boolean sqlFlag = sqlAssert(c, beforeSqlResult, afterSqlResult);
		System.out.println("数据库断言结果："+sqlFlag);
		//8、添加断言回写内容
		String assertContent=(assertResponseFlag) ? "PASS" : "FAIL";
		addWBD(Integer.parseInt(c.getId()),Contants.ASSERT_RESULT_CELL_NUM,assertContent);
		//9、添加日志
		//10、报表断言		
		Assert.assertEquals(assertContent, "PASS");
		}
		
		
	public boolean sqlAssert(Case c,Object beforeSqlResult,Object afterSqlResult){
		String sql=c.getSql();
		//如果sql为空，说明不需要数据库断言
		if(StringUtils.isBlank(sql)){
			return true;
		}else{
			//注册逻辑：前置sql结果为0，后置sql结果为1，那么断言成功
			long beforeValue=(Long) beforeSqlResult;
			long afterValue=(Long) afterSqlResult;
			System.out.println("sql语句："+sql);
			System.out.println("beforeValue:"+beforeValue);
			System.out.println("afterValue:"+afterValue);
			if(beforeValue==0 && afterValue==1){
				return true;
			}else{
				return false;
			}
		}
	}
		
	@DataProvider(name="datas")
	public Object[][] datas(){	
		
		Object[][] datas = ExcelUtils.getAPIandByApiId("1");
		
		return datas;
	}

}
