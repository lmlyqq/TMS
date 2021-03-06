package com.rd.client.common.action;

import java.util.Map;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 适用列表操作的新增按钮
 * @author yuanlei
 *
 */
public class NewAction2 implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler{

	private SGTable table = null;
	private SGTable main_table = null;
	private Map<String, String> initMap = null;
	private SGForm view;
	private int initBtn =0;
	public NewAction2(SGTable p_table) {
		table = p_table;
	}
	public NewAction2(SGTable p_table,Map<String, String> initMap) {
		table = p_table;
		this.initMap = initMap;
	}
	public NewAction2(SGTable p_table,Map<String, String> initMap,SGForm view,int initBtn) {
		table = p_table;
		this.initMap = initMap;
		this.view = view;
		this.initBtn = initBtn;
	}
	
	public NewAction2(SGTable p_table,Map<String, String> initMap,SGForm view) {
		table = p_table;
		this.initMap = initMap;
		this.view = view;
	}
	
	public NewAction2(SGTable p_table, SGTable p_maintable) {
		table = p_table;
		main_table = p_maintable;
	}
	
	public NewAction2(SGTable p_table, SGTable p_maintable, Map<String, String> initMap) {
		table = p_table;
		main_table = p_maintable;
		this.initMap = initMap;
	}
	public NewAction2(SGTable p_table, SGTable p_maintable, Map<String, String> initMap,SGForm view) {
		table = p_table;
		main_table = p_maintable;
		this.initMap = initMap;
		this.view = view;
	}

	@Override
	public void onClick(ClickEvent event) {
		//newAction();
		try {
			String url = "/grss/echo/post?hello=yuanlei";// 动态生成  
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST,URL.encode(url));  
			// 用post方法提交表单数据，需要设置Content-Type  
			builder.setHeader("Content-Type", "application/x-www-form-urlencoded");  
			// 将表单的数据转为字符串，作为RequestData设置给builder  
			String form = "name=aaa&age=13";  
			// setRequestData()必须在send()之前调用才有效  
			builder.setRequestData(form);  
			  
			builder.setCallback(new RequestCallback() {
	
				@Override
				public void onError(Request request, Throwable exception) {
					// TODO Auto-generated method stub
					
				}
	
				@SuppressWarnings("deprecation")
				@Override
				public void onResponseReceived(Request request, Response response) {
					String json = response.getText();
					JSONValue result = JSONParser.parse(json);
					JSONObject object = result.isObject();
					if(object != null) {
						String status = object.get("status").toString();
						if("0".equals(status)) {
							JSONValue data = object.get("data");
							JSONArray array = data.isArray();
						    for(int i = 0 ; i < array.size(); i++) {
						    	//JSONValue json_index = array.get(i);
						    	//System.out.println(json_index);
						    }
							//System.out.println(status);
						}
						else {
							JSONValue data = object.get("errors");
							JSONArray obj = data.isArray();
							for(int i = 0 ; i < obj.size();i++) {
						    	JSONValue value = obj.get(i);
						    	JSONObject obj2 = value.isObject();
						    	if(obj2.get("errorMessage") != null) {
						    		//String message = obj2.get("errorMessage").toString();
							    	//System.out.println(message);
						    		break;
						    	}
						    }
						}
					}
					//System.out.println(json);
				}  
			    // 同上GET请求的实现  
			});  
			builder.send();  
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(MenuItemClickEvent event) {
		newAction();
		
	}
	
	private void newAction(){
		if(view != null && initBtn == 0){
			view.initAddBtn();
		}
		if(initBtn > 0){
			view.initBtn(initBtn);
		}
		table.startEditingNew();
		table.OP_FLAG = "A";
		if(main_table != null) {
			main_table.setData(new ListGridRecord[0]);
			//main_table.invalidateCache();
		}
		if(initMap != null) {
			Object[] iter = initMap.keySet().toArray();
			String key = "",i18n_key = "";
			String value = "";
			for(int i = 0; i < iter.length; i++) {
				key = (String)iter[i];
				if(key.indexOf("CUSTOMER_ID") >= 0 ){
					table.setEditValue(table.getRecords().length,key, LoginCache.getDefCustomer().get("CUSTOMER_ID")); //要求初始化客户时，查找默认客户
					
				}else{
					i18n_key = key + "_NAME";   //前台显示的是NAME的值，而后台取的是ID，所以前台展示和后台ID的差别在于对应的字段多出一个_NAME
					value = initMap.get(key);
					if(value.equals("Y") || value.equals("N")) {
						table.setEditValue(table.getRecords().length, key, value.equals("Y"));
					}
					else {
						table.setEditValue(table.getRecords().length, key, ObjUtil.ifNull(initMap.get(i18n_key), value));
					}
				}
			}
		}
	}
	public Map<String, String> getInitMap() {
		return initMap;
	}
	public void setInitMap(Map<String, String> initMap) {
		this.initMap = initMap;
	}
	

}
