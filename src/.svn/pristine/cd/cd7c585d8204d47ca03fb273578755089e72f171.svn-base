package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 待调右键保存
 * @author wangjun
 *
 */
public class UnShpmSaveAction implements ClickHandler {

	private SGTable table = null;
	private String MOBILE ="";
	private String VEHICLE_TYP_ID="" ;
	private String PLATE_NO ="";
	private String DRIVER ="";
	private Record record;
	
	public UnShpmSaveAction(SGTable p_table) {
		table = p_table;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(MenuItemClickEvent event) {
		try {
			ArrayList<String> sqlList = new ArrayList<String>();
			
			record = table.getSelectedRecord();
			Map map2 = table.getEditValues(record);
			
//			String VEHICLE_TYP_ID = "";
//			String PLATE_NO = "";5
//			String DRIVER = "";
			
			
//			if(map2 != null && (map2.get("VEHICLE_TYP_ID") != null)) {
//				VEHICLE_TYP_ID = map2.get("VEHICLE_TYP_ID").toString();
//			}else if(map2 != null && (map2.get("VEHICLE_TYP_ID") =="")){
//				VEHICLE_TYP_ID = "";
//			}else {
//				VEHICLE_TYP_ID =record.getAttribute("VEHICLE_TYP_ID");
//			}
//			if(map2 != null && (map2.get("DRIVER")!= null)) {
//				DRIVER = map2.get("DRIVER").toString();
//			}else if(map2 != null && (map2.get("DRIVER") =="") ){
//				DRIVER = "";
//			}else {
//				DRIVER = record.getAttribute("DRIVER");
//			}
//			if(map2 != null && (map2.get("MOBILE") != null) ) {
//				MOBILE = map2.get("MOBILE").toString();
//			}else if(map2 != null && (map2.get("MOBILE") =="")){
//				MOBILE = "";
//			}else{
//				MOBILE = record.getAttribute("MOBILE");
//			}
			
			if(map2 != null && ObjUtil.isNotNull(ObjUtil.ifObjNull(record.getAttribute("VEHICLE_TYP_ID"),map2.get("VEHICLE_TYP_ID")))) {
				VEHICLE_TYP_ID = ObjUtil.ifObjNull(map2.get("VEHICLE_TYP_ID"),record.getAttribute("VEHICLE_TYP_ID")).toString();
			}
			if(map2 != null && ObjUtil.isNotNull(ObjUtil.ifObjNull(record.getAttribute("DRIVER"),map2.get("DRIVER")))) {
				DRIVER = ObjUtil.ifObjNull(map2.get("DRIVER"),record.getAttribute("DRIVER")).toString();
			}
			if(map2 != null && ObjUtil.isNotNull(ObjUtil.ifObjNull(record.getAttribute("MOBILE"),map2.get("MOBILE")))) {
				MOBILE = ObjUtil.ifObjNull(map2.get("MOBILE"),record.getAttribute("MOBILE")).toString();
			}
				
			if(map2 != null && ObjUtil.isNotNull(ObjUtil.ifObjNull(record.getAttribute("PLATE_NO"),map2.get("PLATE_NO")))) {
				PLATE_NO = ObjUtil.ifObjNull(map2.get("PLATE_NO"),record.getAttribute("PLATE_NO")).toString();
			}else{
				SC.warn("请填写车牌号 !");
				return;
			}
			table.updateData(table.getSelectedRecord()); 
			
			if(map2 != null){
				StringBuffer sf = new StringBuffer();
				sf.append(" update TRANS_SHIPMENT_HEADER SET VEHICLE_TYP_ID = '");
				sf.append(VEHICLE_TYP_ID);
				sf.append("',");
				sf.append("PLATE_NO = '");
				sf.append(PLATE_NO);
				sf.append("',");
				sf.append("DRIVER = '");
				sf.append(DRIVER);
				sf.append("',");
				sf.append("MOBILE = '");
				sf.append(MOBILE);
				sf.append("' where ID = '");
				sf.append(map2.get("ID"));
				sf.append("'");
				sqlList.add(sf.toString());
			}
	            
			if(sqlList.size() > 0) {
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						MSGUtil.sayError(caught.getMessage());
					}
					@Override
					public void onSuccess(String result) {
						MSGUtil.showOperSuccess();
						
						record.setAttribute("VEHICLE_TYP_ID", VEHICLE_TYP_ID);
						record.setAttribute("DRIVER", DRIVER);
						record.setAttribute("MOBILE", MOBILE);
						record.setAttribute("PLATE_NO", PLATE_NO);
						table.updateData(record); 
					}
				});
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
