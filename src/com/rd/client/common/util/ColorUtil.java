package com.rd.client.common.util;

public class ColorUtil {
	public final static String BG_COLOR = "#FFFFFF";
	
	public final static String TITLE_COLOR = "#000079";
	
	public static String getTitleNameWithCol(String name){
		return "<font style=\"color: "+TITLE_COLOR+";\">"+name+"</font>";
	}
	
	public static String getRedTitle(String name){
//		return "<font style=\"color: red;\">" + name +"</font>";
		return "<font style=\"font-weight:bold;\">" + name +"</font>";
	}
	public static String getBlueTitle(String name){
		return "<font style=\"color: blue;\">" + name +"</font>";
	}
	public static String getWhiteTitle(String name){
		return "<font style=\"color: white;font-size:10pt;\">" + name + "</font>";
	}
	public static String getBlackTitle(String name){
		return "<font style=\"color: black;\">" + name + "</font>";
	}
}
