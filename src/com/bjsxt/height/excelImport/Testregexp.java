package com.bjsxt.height.excelImport;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Testregexp {
	public static void main(String[] args) {
		String checkValue = "2007-02-23";
		// String eL =
		// "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))
		// (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";

		String eL = "^((\\d{2}(([02468][048])|([13579][26]) )[\\-\\/\\s]?"
				+ "((( (0?[13578])|(1[02]))[\\-\\/\\s]"
				+ "?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))"
				+ "[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))"
				+ "|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|"
				+ "(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		
		
		String el2="^(^(\\d{4}|\\d{2})(\\-|\\/|\\.|\\_)\\d{1,2}\\3\\d{1,2}$)|(\\d)$\r\n" ;
		//带.  带  - 带 /  演化的日期判断（YYYY/MM/DD| YY/MM/DD）
		String e15=("^\\d{4}年\\d{0,2}月\\d{0,2}日$");
		Pattern p = Pattern.compile(el2);
		Matcher m = p.matcher(checkValue);
		boolean b = m.matches();
		if (b) {
			System.out.println("格式正确");
		} else {
			System.out.println("格式错误");
		}

	}
	
	private static Date macthDate(String checkValue) {
		Date date ;
		Pattern p = null;
		if(checkValue.contains("\\.")) {
			String el2="^(^(\\d{4}|\\d{2})(\\-|\\/|\\.|\\_)\\d{1,2}\\3\\d{1,2}$)|(\\d)$\r\n" ;
			p = Pattern.compile(el2);
		}else if (checkValue.contains("_")) {
			
		}else if (checkValue.contains("-")) {
			
		}else if (checkValue.contains("/")) {
			
		}
		 
		Matcher m = p.matcher(checkValue);
		return null;
	}

}
