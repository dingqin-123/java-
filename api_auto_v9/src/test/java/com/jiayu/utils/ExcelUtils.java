package com.jiayu.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;


import com.jiayu.constants.Contants;
import com.jiayu.pojo.API;
import com.jiayu.pojo.Case;
import com.jiayu.pojo.WriteBackData;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;


public class ExcelUtils {
	
	//读取第一个sheet，API
	public static List<API> apiList= ExcelUtils.read(0,API.class);
	//读取第二个sheet，Case
	public static List<Case> caseList = ExcelUtils.read(1,Case.class);
	
	//excel回写数据集合
	public static List<WriteBackData> wbdList=new ArrayList<WriteBackData>();
	
	
	public static void batchWrite() {
		FileInputStream fis=null;
		FileOutputStream fos=null;
		try {
			fis=new FileInputStream(Contants.EXCEL_PATH);
			Workbook workbook=WorkbookFactory.create(fis);
			Sheet sheet = workbook.getSheetAt(1);
			//回写，操作行和列
			//1、遍历wbdList集合
			for (WriteBackData wbd : wbdList) {
				//2、获取行号，获取row对象
				int rowNum=wbd.getRowNum();
				Row row=sheet.getRow(rowNum);
				//3、获取列号，获取cell对象
				int cellNum=wbd.getCellNum();
				Cell cell=row.getCell(cellNum,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				//4、获取回写内容，设置到cell中
				cell.setCellType(CellType.STRING);
				String content=wbd.getContent();
				cell.setCellValue(content);
			}
			//回写到文件中
			fos=new FileOutputStream(Contants.EXCEL_PATH);
			workbook.write(fos);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			inClose(fis);
			outClose(fos);
		}
		
	}
	
	
	/*
	 * 从已经读取好的所有List<API>和所有List<Case>
	 * 两个集合中获取符合条件的数据
	 * apiId  
	 * 
	 * */
	public static Object[][] getAPIandByApiId(String apiId){
		//需要API对象
		API wantAPI=null;
		//需要Case集合
		List<Case> wantCaseList=new ArrayList<Case>();
		//匹配API对象
		for (API api : apiList) {
			//apiId和api集合中的apiId相等则返回
			if(apiId.equals(api.getId())){
				wantAPI=api;
				break;
			}
		}
		
		//匹配Case对象
		for (Case c : caseList) {
			//apiId和api集合中的apiId相等则返回
			if(apiId.equals(c.getApiId())){
				wantCaseList.add(c);
			}
		}
		//wantCaseList和wantAPI是有关联的，他们的apiId是相等的。
		Object[][] datas=new Object[wantCaseList.size()][2];
//		Object[][] datas={{api,case1},{api,case2},{api,case3}};
		//往二维数组中存储api和case数据，存几次由case确定。
		for(int i=0;i<wantCaseList.size();i++){
			 datas[i][0]=wantAPI;   			//datas[0][0]=wantAPI;
			 datas[i][1]=wantCaseList.get(i);	//datas[0][1]=wantAPI;   ....//datas[0][n]=wantAPI;
		}
		return datas;
	}
	
	
	
	public static <E> List<E> read(int startSheetIndex,Class<E> clazz){
		//1、加载excel文件
		FileInputStream fis=null;
		try {
			fis=new FileInputStream(Contants.EXCEL_PATH);
			//2、导入配置，创建空对象相当于用默认配置
			ImportParams params=new ImportParams();
			params.setStartSheetIndex(startSheetIndex);   //获取表单的索引值
			params.setNeedVerify(true);
			//3、执行导入
			List<E> list = ExcelImportUtil.importExcel(fis, clazz, params);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inClose(fis);
		} 
		return null;
		
	}



	private static void inClose(FileInputStream fis) {
		if(fis!=null){
			try {
				fis.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	
	private static void outClose(FileOutputStream fos) {
		if(fos!=null){
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
