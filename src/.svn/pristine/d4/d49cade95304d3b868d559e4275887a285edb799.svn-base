package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.DispatchWinView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CreateStrDisAction implements ClickHandler{
	private DispatchWinView view;
	ListGridRecord[] newrecords;
	
	public CreateStrDisAction(DispatchWinView view){
		this.view=view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		ListGridRecord[] records=null;
		if(ObjUtil.isNotNull(view.table)){
			records=view.table.getSelection();
		}
		if(records==null || records.length==0){
			MSGUtil.sayError("未选择作业单");
			return;
		}
		
		String exec_org_id =LoginCache.getLoginUser().getDEFAULT_ORG_ID();
		String user=LoginCache.getLoginUser().getUSER_ID();
		String biz_typ=ObjUtil.ifNull(view.biz_typ, " ");
		
		HashMap<String,String> load_area_list=new HashMap<String,String>();  //起运地
		HashMap<String,String> unload_area_list=new HashMap<String,String>();  //目的地
		HashMap<String,String> addtime_list=new HashMap<String,String>(); //创建时间
		HashMap<String,String> TOT_GROSS_W=new HashMap<String,String>();  //总重量
		HashMap<String,String> TOT_VOL=new HashMap<String,String>();  //总体积
		HashMap<String,String> TEMPERATURE_ID=new HashMap<String,String>();  //温层
		HashMap<String, String> ODR_COU = new HashMap<String, String>();
//		HashMap<String,String> TEMPERATURE_CHK=new HashMap<String,String>();  //温层检查
//		HashMap<String,String> LOAD_CHK=new HashMap<String,String>();
//		HashMap<String,String> UNLOAD_CHK=new HashMap<String,String>();
		
		ArrayList<ListGridRecord> list=new ArrayList<ListGridRecord>(Arrays.asList(view.table.getRecords()));
		ArrayList<ListGridRecord> cache_list=new ArrayList<ListGridRecord>();
		int pos=0;
		for(int i=0;i<records.length;i++){
			load_area_list.put(String.valueOf(i+1), ObjUtil.ifNull(records[i].getAttribute("LOAD_AREA_ID2"), ""));
			unload_area_list.put(String.valueOf(i+1), ObjUtil.ifNull(records[i].getAttribute("UNLOAD_AREA_ID2"), ""));
			addtime_list.put(String.valueOf(i+1), ObjUtil.ifNull(records[i].getAttribute("ADDTIME"), ""));
			TOT_GROSS_W.put(String.valueOf(i+1), ObjUtil.ifNull(records[i].getAttribute("TOT_GROSS_W"), ""));
			TOT_VOL.put(String.valueOf(i+1), ObjUtil.ifNull(records[i].getAttribute("TOT_VOL"), ""));
			TEMPERATURE_ID.put(String.valueOf(i+1), ObjUtil.ifNull(records[i].getAttribute("TEMPERATURE_ID"), ""));
			ODR_COU.put(String.valueOf(i+1), ObjUtil.ifNull(records[i].getAttribute("QNTY"), ""));
			
			pos=view.table.getRecordIndex(records[i]);
			cache_list.add(list.get(pos));
		}
		//过滤重复的温层
		Object[] keys_TEM=TEMPERATURE_ID.keySet().toArray();
		Set<String> temSet=new HashSet<String>();
		for(int i=0;i<keys_TEM.length;i++){
			temSet.add(TEMPERATURE_ID.get(keys_TEM[i]));
		}
		Object[] keys_LOAD=load_area_list.keySet().toArray();
		Set<String> loadSet=new HashSet<String>();
		for(int i=0;i<keys_LOAD.length;i++){
			loadSet.add(load_area_list.get(keys_LOAD[i]));
		}
		Object[] keys_UNLOAD=load_area_list.keySet().toArray();
		Set<String> unloadSet=new HashSet<String>();
		for(int i=0;i<keys_UNLOAD.length;i++){
			unloadSet.add(unload_area_list.get(keys_UNLOAD[i]));
		}
		
		
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("1", load_area_list);
		map.put("2", unload_area_list);
		map.put("3", addtime_list);
		map.put("4", TOT_GROSS_W);
		map.put("5", TOT_VOL);
		map.put("6", TEMPERATURE_ID);
		map.put("7", temSet.size());
		map.put("8", loadSet.size());
		map.put("9", unloadSet.size());
		map.put("10", biz_typ);
		map.put("11", ODR_COU);
		map.put("12", exec_org_id);
		map.put("13", user);
		newrecords=(ListGridRecord[])cache_list.toArray(new ListGridRecord[cache_list.size()]);
		list.removeAll(Arrays.asList(newrecords));
		newrecords=(ListGridRecord[])list.toArray(new ListGridRecord[list.size()]);
		String json=Util.mapToJson(map);
		Util.async.execProcedure(json, "SP_LOADVIEW_CREATE(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.sayInfo(result.substring(2));
					view.table.setRecords(newrecords);
					view.table.redraw();
				}else{
					MSGUtil.sayError(result);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError("服务器连接已中断，请重新登录!");
			}
		});
		
	}

}
