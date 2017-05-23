package com.rd.client.action.tms.track;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 车辆定位右键
 * @author fanglm 
 *
 */
public class VechPositionAction implements ClickHandler {

	private ListGrid table;
	

	public VechPositionAction(ListGrid table) {
		this.table = table;
	}


	@Override
	public void onClick(MenuItemClickEvent event) {
		ListGridRecord record = table.getSelectedRecord();
		if (record == null)
			return;
		
		if(!"已发运".equals(record.getAttribute("STATUS_NAME"))){
			MSGUtil.sayError("操作失败！只能对【已发运】的作业单做车辆定位！");
			return;
		}
		
		
		
		String mobile = record.getAttribute("MOBILE");
		
		String load_no = record.getAttribute("LOAD_NO");
		
		//管车宝接口提交数据，处理数据
		
		Util.db_async.getPost(load_no,mobile,LoginCache.getLoginUser().getUSER_ID(), new AsyncCallback<HashMap<String, String>>() {
			
			@Override
			public void onSuccess(HashMap<String, String> result) {
				try{
					if(result.get("result_id").equals("3001")){
						/*if(Cookies.getCookie("IP_ADDR").indexOf(StaticRef.OUT_IP) < 0) {
							com.google.gwt.user.client.Window.open(result.get("img_url").replaceFirst("googleMap", "googleOutMap"), "", "");
						}else{
							com.google.gwt.user.client.Window.open(result.get("img_url"), "", "");
						}*/
						com.google.gwt.user.client.Window.open(result.get("img_url"), "", "");
					}else{
						MSGUtil.sayError("定位失败！" + result.get("result_info"));
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});

	}

}
