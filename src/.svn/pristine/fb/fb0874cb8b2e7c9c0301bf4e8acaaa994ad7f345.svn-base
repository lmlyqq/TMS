package com.rd.client.action.tms.track;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SaveTrasFollowAction implements ClickHandler{
    private SGTable shpmTable;//作业单主表
	private SGTable groupTable;//运输跟踪表
    private ArrayList<String> logList;   //日志信息
	private int[] edit_rows = null;               //列表中所有被编辑过的行号
	private HashMap<String, String> map;
	private HashMap<String, String> check_map;
	private TmsTrackView view;
	private String currentLoc ;
	private ListGridRecord[]grouprecords;
	private ListGridRecord[] shpmrecords;
	private ArrayList<ListGridRecord> listrecord;
	
	
    public SaveTrasFollowAction(SGTable shpmTable,SGTable groupTable,TmsTrackView view,HashMap<String, String> check_map){

    	this.shpmTable = shpmTable;
    	this.groupTable = groupTable;
    	this.view = view;
    	this.check_map = check_map;
    	
    }
	
	@Override
	public void onClick(ClickEvent event) {
		grouprecords = view.groupTable1.getRecords();
		
		if(grouprecords.length == 0){
			doOperate(false);
		}else {
			String time = grouprecords[0].getAttribute("ADDTIME");
			ArrayList<String> list = new ArrayList<String>();
			list.add("TRANS_TIME");
			list.add(time);
			Util.async.execProcedure(list,"GETSERVERTIME_PRO(?,?,?)", new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals("00")){
						doOperate(true);
					}else {
						doOperate(false);
					}
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		

//		Util.async.getServTime("yyyy/MM/dd HH:mm",new AsyncCallback<String>() {
//
//			@Override
//			public void onSuccess(String result) {
//				
//				grouprecords = view.groupTable1.getRecords();
//				if(grouprecords.length>=1){
//					minutes =DateUtil.lassMinutes(result,grouprecords[0].getAttribute("ADDTIME"));
//					if(minutes>5){
//						doOperate(false);
//					} else {
//						doOperate(true);
//					}
//				}else {
//					doOperate(false);
//				}
//			}
//
//			public void onFailure(Throwable caught) {
//
//				
//			}
//		});
//		
		
		
	
	}
	
	@SuppressWarnings("unchecked")
	private void doOperate(boolean isupdate){
		
		shpmrecords = view.shpmTable.getSelection();//获取被选中的作业单记录
		Record loadrecords = view.shpmnorecord;    //获取选中记录
		StringBuffer errmsg = new StringBuffer();
		ListGridRecord finishRec = null;
		listrecord = new ArrayList<ListGridRecord>();
		for(int i = 0 ;i < shpmrecords.length ; i++){
			if(shpmrecords[i].getAttribute("LOAD_NO").equals(loadrecords.getAttribute("LOAD_NO"))){
				listrecord.add(shpmrecords[i]);
				
			} else {
				errmsg.append(shpmrecords[i].getAttribute("SHPM_NO"));
				errmsg.append(",");
			}
		}
		
		ArrayList<String> sqlList = new ArrayList<String>();
		logList = new ArrayList<String>();
		edit_rows = groupTable.getAllEditRows();   //获取所有修改过的记录行
		map = (HashMap<String, String>)groupTable.getEditValues(edit_rows[0]);//获得跟踪信息
		
		ArrayList<String> extra = new ArrayList<String>();
		StringBuffer sf2 = new StringBuffer();
		StringBuffer msg = new StringBuffer();
		
		for(int i = 0;i < listrecord.size(); i++){
			StringBuffer sf = new StringBuffer();
			finishRec = listrecord.get(i);                    //获得作业单信息
			
			ArrayList<Object> obj = Util.getCheckResult(map, check_map);
			if(obj != null && obj.size() > 1) {
				String result = obj.get(0).toString();
				if(!result.equals(StaticRef.SUCCESS_CODE)) {
					
				}
			}
			
			currentLoc = map.get("CURRENT_LOC");
			if(!ObjUtil.isNotNull(currentLoc)){
				msg.append(Integer.toString(edit_rows[0]));  //yuanlei 2011-2-14
				msg.append(",");
			}
			map.put("TABLE", "TRANS_TRACK_TRACE");
			//map.put("ADDWHO", LoginCache.getLoginUser().getUSER_ID());  //yuanlei 2011-3-2
			map.put("TRACE_TIME", StaticRef.SYS_DATE);
			if(isupdate){
				map.put("TRANS_TRACK_APPEND", " SHPM_NO='" + finishRec.getAttribute("SHPM_NO") + 
						"' AND to_char(ADDTIME,'yyyy/mm/dd hh24:mi')=to_char(to_date('" +
						grouprecords[0].getAttribute("ADDTIME") +"','yyyy/mm/dd hh24:mi'),'yyyy/mm/dd hh24:mi')");
			}
			map.put("SHPM_NO", finishRec.getAttribute("SHPM_NO"));
			
			sqlList.add(Util.mapToJson(map));
			
			sf.append("UPDATE TRANS_SHIPMENT_HEADER SET ABNOMAL_STAT='");
			sf.append(map.get("ABNOMAL_STAT"));
			sf.append("'");
			sf.append(" , CURRENT_LOC ='");
			sf.append(map.get("CURRENT_LOC"));
			sf.append("'");
			sf.append(" , ABNOMAL_NOTES ='");
			sf.append(map.get("ABNOMAL_NOTE"));
			sf.append("'");
			sf.append(",PLATE_NO='");
			sf.append(map.get("PLATE_NO"));
			sf.append("',DRIVER='");
			sf.append(map.get("DRIVER"));
			sf.append("',MOBILE='");
			sf.append(map.get("MOBILE"));
			sf.append("' ");
			sf.append(" WHERE SHPM_NO ='");
			sf.append(finishRec.getAttribute("SHPM_NO"));
			sf.append("'");
			extra.add(sf.toString());	
		} 
        if(errmsg.length() > 0){
        	MSGUtil.sayError("作业单"+errmsg.substring(0,errmsg.length()-1)+"，非【已发运】状态，不能跟踪！");
			return;
        }
		
		if(msg.length() > 0){
			MSGUtil.sayError("跟踪记录第"+msg.substring(0,msg.length()-1)+"行，【当前位置】为空！");
			return;
		}
		
		sf2 = new StringBuffer();
		sf2.append("UPDATE TRANS_LOAD_HEADER SET ABNOMAL_STAT='");
		sf2.append(map.get("ABNOMAL_STAT"));
		sf2.append("'");
		sf2.append(" , CURRENT_LOC ='");
		sf2.append(map.get("CURRENT_LOC"));
		sf2.append("'");
		sf2.append(" , ABNOMAL_NOTES ='");
		sf2.append(map.get("ABNOMAL_NOTE"));
		sf2.append("'");
		sf2.append(" WHERE LOAD_NO ='");
		sf2.append(loadrecords.getAttribute("LOAD_NO"));
		sf2.append("'");
		extra.add(sf2.toString());
		
		if(isupdate){
			doUpdate(sqlList,extra);
		}else{
			doInsert(sqlList, extra);
		}
			
	}
	private void doInsert(ArrayList<String> sqlList,ArrayList<String> extra){
		Util.async.doInsert(logList, sqlList, extra, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					for (int i = 0; i < shpmrecords.length; i++) {
						shpmrecords[i].setAttribute("ABNOMAL_NOTES", map.get("ABNOMAL_NOTE"));
					    shpmrecords[i].setAttribute("ABNOMAL_STAT", map.get("ABNOMAL_STAT"));
					    shpmrecords[i].setAttribute("ABNOMAL_STAT_NAME",LoginCache.getBizCodes().get("ABNORMAL_STAT").get(map.get("ABNOMAL_STAT")));
					    shpmrecords[i].setAttribute("PRE_UNLOAD_TIME",map.get("PRE_UNLOAD_TIME"));
					    shpmrecords[i].setAttribute("CURRENT_LOC",map.get("CURRENT_LOC"));
					    shpmrecords[i].setAttribute("PLATE_NO", map.get("PLATE_NO"));
						shpmrecords[i].setAttribute("DRIVER", map.get("DRIVER"));
						shpmrecords[i].setAttribute("MOBILE", map.get("MOBILE"));
//					    shpmTable.updateData(shpmrecords[i]);
					}
					
					Criteria crit = new Criteria();
					groupTable.invalidateCache();
					crit.addCriteria("OP_FLAG", groupTable.OP_FLAG);
					crit.addCriteria("SHPM_NO", map.get("SHPM_NO"));
					groupTable.fetchData(crit);
					groupTable.discardAllEdits();
					view.initSaveBtn();
				}
				else {
					MSGUtil.sayError(result);
				}
				
				groupTable.redraw();
				shpmTable.redraw();
			}
		});
	
	}
	
	private void doUpdate(ArrayList<String> sqlList,ArrayList<String> extra) {
		
		Util.async.transFollowDoUpdate(logList,sqlList,extra, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					
					Criteria crit = new Criteria();
					groupTable.invalidateCache();
					crit.addCriteria("OP_FLAG", groupTable.OP_FLAG);
					crit.addCriteria("SHPM_NO", map.get("SHPM_NO"));
					
				for (int i = 0; i < shpmrecords.length; i++) {
					shpmrecords[i].setAttribute("ABNOMAL_NOTES", map.get("ABNOMAL_NOTE"));
					shpmrecords[i].setAttribute("ABNOMAL_STAT", map.get("ABNOMAL_STAT"));
					shpmrecords[i].setAttribute("ABNOMAL_STAT_NAME",LoginCache.getBizCodes().get("ABNORMAL_STAT").get(map.get("ABNOMAL_STAT")));
					shpmrecords[i].setAttribute("PRE_UNLOAD_TIME",map.get("PRE_UNLOAD_TIME"));
					shpmrecords[i].setAttribute("CURRENT_LOC",map.get("CURRENT_LOC"));
					shpmrecords[i].setAttribute("PLATE_NO", map.get("PLATE_NO"));
					shpmrecords[i].setAttribute("DRIVER", map.get("DRIVER"));
					shpmrecords[i].setAttribute("MOBILE", map.get("MOBILE"));
					shpmTable.updateData(shpmrecords[i]);
				}
					
					groupTable.fetchData(crit);
					groupTable.discardAllEdits();
					view.initSaveBtn();
				}
				else {
					MSGUtil.sayError(result);
				}
				groupTable.redraw();
				shpmTable.redraw();
			}
		});
	}

}
