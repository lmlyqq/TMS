package com.rd.client.action.tms.order;


import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 运输管理--托运单管理--托运单明细--右键--保存
 *
 */
public class SaveOrderBillAction implements ClickHandler,com.smartgwt.client.widgets.events.ClickHandler{

	private SGTable table = null;
	private OrderView view;
	private HashMap<String, String> map;
	private String odr_no;
	private ArrayList<String> sqlList;
	private boolean boo = true;
	
//	private HashMap<String, String> ck_map = new HashMap<String, String>();
	
	public SaveOrderBillAction(SGTable p_table,OrderView view,HashMap<String, String> ck_map) {
		table = p_table;
		this.view = view;
//		this.ck_map = ck_map;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		doSave(true);
	}
	
	public void doSave(final boolean asyn){
		int[] edit_row = table.getAllEditRows();
		if(edit_row.length == 0){
			return;
		}
		String msg = "";
		if(asyn){//同步保存，不需要校验生成list
			msg = checkDetail();
		}
		odr_no = view.vm.getValueAsString("ODR_NO");
		if(!boo){
			MSGUtil.sayError(msg);
			return;
		}
		String appRow = " and DOC_NO='" + view.vm.getValueAsString("ODR_NO") + "'";
		
		
		Util.async.doSave(null, sqlList,appRow, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					if(asyn){
						MSGUtil.showOperSuccess();
					}
					table.OP_FLAG = "M";
					Criteria crit = table.getCriteria();
					table.invalidateCache();
					if(crit == null) {
						crit = new Criteria();
					}
					crit.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
					crit.addCriteria("DOC_NO",odr_no);
					//2015-07-21 v1.8 yuanlei
					table.discardAllEdits();
					table.fetchData(crit,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
						}
					});
				}
				else {
					MSGUtil.sayError(result);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public String checkDetail(){
		boo = true;
		int[] edit_row = table.getAllEditRows();
		if(edit_row.length == 0){
			return "";
		}
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		String sql;
		sqlList = new ArrayList<String>();
		StringBuffer msg = new StringBuffer();
		for(int i=0;i<edit_row.length;i++){
			map =(HashMap<String, String>) table.getEditValues(edit_row[i]);
			
			if(table.getRecord(edit_row[i]) != null){
				HashMap<String, String> record_map = Util.putRecordToModel(table.getRecord(edit_row[i]));
				Object[] key = map.keySet().toArray();
				for(int j=0;j<key.length;j++){
					record_map.put(key[j].toString(), ObjUtil.ifObjNull(map.get(key[j]),0.00).toString());
				}
				map = record_map;
			}
			if(ObjUtil.isNotNull(map.get("ODR_TIME"))){
				map.put("ODR_TIME", "to_date('"+map.get("ODR_TIME")+"','yyyy/mm/dd hh24:mi:ss')");
			}
			map.put("TABLE", "TRANS_BILL_RECE");
			map.put("ADDWHO", login_user);
			map.put("DOC_NO", view.vm.getValueAsString("ODR_NO"));
			map.put("CHARGE_TYPE", "53EB6809BFCC436799F735AAE23658B9");
			
			Object[] keys = map.keySet().toArray();
			for(Object key : keys){
				if ("FEE_ID,FEE_NAME,FEE_BAS,DISCOUNT_RATE".indexOf(key.toString()) >= 0) {
					continue;
				}
				map.put(key.toString(), ObjUtil.ifNull(map.get(key), "0"));
			}
			if (ObjUtil.isNotNull(map.get("DISCOUNT_RATE"))) {
				if (Double.valueOf(map.get("DISCOUNT_RATE")) == 0.0) {
					map.put("DISCOUNT_RATE", "1");
				}
			}else {
				map.put("DISCOUNT_RATE", "1");
			}
			
			sql = Util.mapToJson(map);
			sqlList.add(sql);
		}
		return msg.toString();
	}

	@Override
	public void onClick(ClickEvent event) {
		doSave(true);
	}
}
