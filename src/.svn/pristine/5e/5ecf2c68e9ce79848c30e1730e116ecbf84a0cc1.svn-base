package com.rd.client.common.util;


/**
 * 时间比较、计算公共方法类
 * @author fanglm
 *
 */
public class DateUtil {

	public static native int compare(String date1, String date2) /*-{
	    var s1 = date1;  
		var date1 = new Date(s1.replace(/-/g, "/")); 
		var s2 = date2;
		var date2 = new Date(s2.replace(/-/g, "/"));  
		return (date1-date2)/1000;
	}-*/;
	

	/**
	 * 判断date2是否大于date1
	 * @param date1  起始时间
	 * @param date2 终止时间
	 * @return 正确返回true、否则返回false
	 */
	public static boolean isAfter(String date1, String date2){
		if(compare(date2, date1) >= 0){
			return true;
		}else{
			return false;
		}
	} 
	
	/**
	 * 判断date2是否大于date1
	 * @param date1  起始时间
	 * @param date2 终止时间
	 * @return 正确返回true、否则返回false
	 */
	public static boolean isAfter2(String date1, String date2){
		if(compare(date2, date1) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断date2是否大于date1
	 * @param date1  起始时间
	 * @param date2 终止时间
	 * @return 正确返回true、否则返回false
	 */
	public static boolean isBefore(String date1, String date2){
		if(compare(date2, date1) < 0){
			return true;
		}else{
			return false;
		}
	} 
	
	/**
	 * 计算两个时间的时间差
	 * @param date1 被减数
	 * @param date2 减数
	 * @return 返回时间差分钟数
	 */
	public static int lassMinutes(String date1,String date2){
		int secend = compare(date1, date2);
		return secend/60;
	}
	
	public static native String currentDate() /*-{
		var d = new Date(),
		String str = '';
		str += d.getFullYear()+'/';
		str += ((d.getMonth() + 1) < 10 ? "0"+(d.getMonth() + 1):(d.getMonth() + 1)) + '/';
		str += (d.getDate() < 10 ? "0"+d.getDate() : d.getDate());
		return str;
	}-*/;
	
}
