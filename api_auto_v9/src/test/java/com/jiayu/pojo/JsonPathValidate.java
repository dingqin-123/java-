package com.jiayu.pojo;

/*
 * JsonPath多关键字匹配实体类
 * */
public class JsonPathValidate {
	//jsonpath表达式
	private String expression;
	//jsonpath表达式期望找到的值
	private String value;

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
