package com.rd.client.common.util;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.DBService;
import com.rd.client.DBServiceAsync;
import com.rd.client.GreetingService;
import com.rd.client.GreetingServiceAsync;

/**
 * 数据库交互的类
 * @author yuanlei
 *
 */
public class DBUtil {

	private String sequence;
	public final GreetingServiceAsync async = (GreetingServiceAsync)GWT.create(GreetingService.class);
	public final DBServiceAsync db_async = (DBServiceAsync)GWT.create(DBService.class);
	private String serv_time;

	public DBUtil() {
		//getSequence();
		//getServTime();
	}

	/**
	 * 获取服务器时间
	 * @author yuanlei
	 * @return
	 */
	public String getServTime() {
		async.getServTime("yyyy-MM-dd HH:mm:ss", new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				serv_time = "1999-01-01";
			}
	
			@Override
			public void onSuccess(String result) {
				serv_time = result;
			}
		});
		return serv_time;
	}

	/**
	 * 获取ORACLE的SEQUENCE
	 * @author yuanlei
	 * @return
	 */
	public String getSequence() {
		async.getIDSequence(new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				sequence = null;
			}
	
			@Override
			public void onSuccess(String result) {
				sequence = result;
			}
		});
		return sequence;
	}

	public boolean isRecordExist(String tableName, String condition) {
		boolean result = true;
		db_async.isExistRecord(tableName, condition, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(Boolean result) {
				result =  result.booleanValue();
			}
			
		});
		return result;
	}

}
