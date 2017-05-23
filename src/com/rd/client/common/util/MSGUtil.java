package com.rd.client.common.util;


import com.smartgwt.client.util.SC;

/**
 * 消息类(包含返回操作结果的消息)
 * 
 * @author yuanlei
 * 
 */
public class MSGUtil {

	public static void showOperSuccess() {
		Util.showMsg(0, Util.MI18N.UPDATE_SUCCESS());
	}

	public static void showOperError() {
		Util.showMsg(2, Util.MI18N.UDDATE_FAILURE());
	}

	public static void showWarning() {

	}
	
	public static void sayInfo(String info) {
		Util.showMsg(0, info);
	}
	
	public static void sayWarning(String info) {
		SC.warn(info);
	}

	public static void sayError(String error) {
		SC.warn(error);
	}
}
