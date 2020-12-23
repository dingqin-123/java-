package com.jiayu.utils;

import java.util.Random;

public class createMobileUtils {
	
	//获取前3位头
	public static String getHeadMobile(Integer type){
		switch (type) {
		//移动号段
		case 1:
			return "139";
		case 2:
			return "139";
		case 3:
			return "137";
		case 4:
			return "136";
		case 5:
			return "135";
		case 6:
			return "134";
		case 7:
			return "159";
		case 8:
			return "158";
		case 9:
			return "151";
		case 10:
			return "150";
		case 11:
			return "152";
		case 12:
			return "188";
		case 13:
			return "187";
		case 14:
			return "182";
		case 15:
			return "157";
		//联通号段
		case 16:
			return "130";
		case 17:
			return "131";
		case 18:
			return "132";
		case 19:
			return "156";
		case 20:
			return "155";
		case 21:
			return "185";
		case 22:
			return "186";
		//电信号段
		case 23:
			return "133";
		case 24:
			return "153";
		case 25:
			return "189";
		case 26:
			return "180";
		default:
			return "181";
		}
	}
	
	
	//获取尾号4位
	public static String getEndMobile(){
		String ychar="0,1,2,3,4,5,6,7,8,9";
		int wei=8;
		String[] ychars=ychar.split(",");
		String endMobile="";
		Random rdm=new Random();
		for(int i=0;i<wei;i++){
			int j=(rdm.nextInt()>>>1)%10;
			if(j>10)
				j=0;
			endMobile = endMobile+ychars[j];
		}
		return endMobile;
	}
	
	


	public static String getMobilePhone() {
		Integer headRandom=new Random().nextInt(25);
		String mobile= getHeadMobile(headRandom)+getEndMobile();
		return mobile;
	}

}
