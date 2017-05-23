package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 作业单卸货顺序保存
 * @author yuanlei
 *
 */
public class ShpmSaveAction implements ClickHandler {

	private SGTable table = null;
	private int[] edit_rows = null;               //列表中所有被编辑过的行号
	private HashMap<String, String> map;
	public ShpmSaveAction(SGTable p_table) {
		table = p_table;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(MenuItemClickEvent event) {
//		try {
//			ArrayList<String> sqlList = new ArrayList<String>();
//			
//			ListGridRecord[] records = table.getRecords();
//			String unload_seq = "";
//			HashMap<String, String> seq_map = new HashMap<String, String>();
//			if(records != null && records.length > 0) {
//				for(int i = 0; i < records.length; i++) {
//					map = (HashMap<String, String>)table.getEditValues(i);
//					if(map != null && ObjUtil.isNotNull(map.get("UNLOAD_SEQ"))) {
//						unload_seq = map.get("UNLOAD_SEQ");
//					}
//					else {
//						unload_seq = records[i].getAttribute("UNLOAD_SEQ");
//					}
//					if(ObjUtil.isNotNull(unload_seq)) {
//						if(ObjUtil.isNotNull(seq_map.get(unload_seq))) {
//							if(!records[i].getAttribute("UNLOAD_NAME").equals(seq_map.get(unload_seq))) {
//								SC.warn("相同卸货顺序对应作业单的卸货点必须相同!");
//								return;
//							}
//						}
//						else {
//							seq_map.put(unload_seq, records[i].getAttribute("UNLOAD_NAME"));
//						}
//					}
//				}
//				edit_rows = table.getAllEditRows();   //获取所有修改过的记录行        
//				if(edit_rows != null && edit_rows.length > 0) {                          //--修改     
//					for(int i = 0; i < edit_rows.length; i++) {
//						map = (HashMap<String, String>)table.getEditValues(edit_rows[i]);    //获取记录修改过的值
//						if(map != null && ObjUtil.isNotNull(map.get("UNLOAD_SEQ"))) {
//							StringBuffer sf = new StringBuffer();
//							sf.append(" update TRANS_SHIPMENT_HEADER SET UNLOAD_SEQ = '");
//							sf.append(map.get("UNLOAD_SEQ"));
//							sf.append("' where ID = '");
//							sf.append(map.get("ID"));
//							sf.append("'");
//							sqlList.add(sf.toString());
//						}
//					}
//					if(sqlList.size() > 0) {
//						Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
//	
//							@Override
//							public void onFailure(Throwable caught) {
//								MSGUtil.sayError(caught.getMessage());
//							}
//	
//							@Override
//							public void onSuccess(String result) {
//								MSGUtil.showOperSuccess();
//							}
//							
//						});
//					}
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
		
		try {
			ArrayList<String> sqlList = new ArrayList<String>();
			
			ListGridRecord[] records = table.getRecords();
			if(records != null && records.length > 0) {
				edit_rows = table.getAllEditRows();   //获取所有修改过的记录行        
				if(edit_rows != null && edit_rows.length > 0) {                          //--修改     
					for(int i = 0; i < edit_rows.length; i++) {
						map = (HashMap<String, String>)table.getEditValues(edit_rows[i]);    //获取记录修改过的值
						if(map != null && ObjUtil.isNotNull(map.get("PLATE_NO"))) {
							StringBuffer sf = new StringBuffer();
							sf.append(" update TRANS_SHIPMENT_HEADER SET PLATE_NO = '");
							sf.append(map.get("PLATE_NO"));
							sf.append("' where ID = '");
							sf.append(map.get("ID"));
							sf.append("'");
							sqlList.add(sf.toString());
						}
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
							}
							
						});
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
