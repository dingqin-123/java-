package com.jiayu.pojo;

import javax.validation.constraints.NotNull;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class API {
	
	//接口编号
	@Excel(name="接口编号")
	@NotNull
	private String id;
	//接口名称
	@Excel(name="接口名称")
	private String name;
	//接口提交方式
	@Excel(name="接口提交方式")
	private String method;
	//接口地址
	@Excel(name="接口地址")
	private String url;
	//参数类型
	@Excel(name="参数类型")
	private String contentType;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	@Override
	public String toString() {
		return "API [id=" + id + ", name=" + name + ", method=" + method + ", url=" + url + ", contentType="
				+ contentType + "]";
	}
}
