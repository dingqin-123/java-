package com.jiayu.pojo;

//excel回写封装对象

public class WriteBackData {
	
	//回写行号   对应的caseId
	private int rowNum;
	//回写列号  写死第5列
	private int cellNum;
	//回写内容  接口响应内容
	private String content;
	
	public WriteBackData() {
		super();
	}
	public WriteBackData(int rowNum, int cellNum, String content) {
		super();
		this.rowNum = rowNum;
		this.cellNum = cellNum;
		this.content = content;
	}
	
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public int getCellNum() {
		return cellNum;
	}
	public void setCellNum(int cellNum) {
		this.cellNum = cellNum;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
