package com.rd.client.common.util;


/**
 * 对于String或Object对象处理的通用类，客户端和服务端可调用
 * @author yuanlei
 *
 */
public class ObjUtil {
	/**
	 * 如果当前字符串为NULL或空值时，返回指定的值
	 * @author yuanlei
	 * @param curValue 判断值
	 * @param returnValue 预设值
	 * @return
	 */
	public static String ifNull(String curValue, String returnValue) {
		String reResult = curValue;
		if(curValue == null || curValue.trim().length() <= 0) {
			reResult = returnValue;
		}
		return reResult;
	}
	
	/**
	 * 如果当前对象的值为NULL，则返回设定的值
	 * @author yuanlei
	 * @param curValue 判断对象
	 * @param returnValue 预设对象
	 * @return
	 */
	public static Object ifObjNull(Object curValue, Object returnValue) {
		Object reResult = curValue;
		
		if(curValue == null) {
			reResult = returnValue;
		}else{
			if(curValue instanceof String)
				reResult = curValue.toString().replace("NULL", "");
		}
		return reResult;
	}
	
	/**
	 * 判断当前字符串的值是否为空
	 * @author yuanlei
	 * @param value
	 * @return
	 */
	public static boolean isNotNull(String value) {
		boolean isNull = false;
		if (value != null && value.trim().length() > 0) {
			isNull = true;
		}
		return isNull;
	}
	
	/**
	 * 判断当前字符串的值是否为空
	 * @author fanglm
	 * @param value
	 * @return
	 */
	public static boolean isNotNull(Object value) {
		boolean isNull = false;
		if (value != null && value.toString().trim().length() > 0) {
			isNull = true;
		}
		return isNull;
	}
	
	/**
	 * 如果当前值等于匹配值，则返回指定值
	 * @author yuanlei
	 * @param curValue    当前值
	 * @param matchValue  比较值
	 * @param returnValue 返回值
	 * @return
	 */
	public static String iif(String curValue, String matchValue, String returnValue) {
		String reResult = curValue;
		if(curValue != null && curValue.compareTo(matchValue) == 0) {
			reResult = returnValue;
		}
		return reResult;
	}
	
	public static boolean flagToBoolean(String flag){
		boolean boo = false;
		if("Y".equals(flag))
			boo = true;
		return boo;
	}
	
	public static boolean isNumber(Object o){
		if(isNotNull(o)){
			if(o instanceof Integer || o instanceof Double){
				return true;
			}
			String tempStr = o.toString().replace(".", "");
			for (int i = 0; i < tempStr.length(); i++) {
				int chr=tempStr.charAt(i);
				if(chr<48 || chr>57)
					return false;
			}
			return true;
		}
		return false;
	}
}

