package com.rd.client.action.tms.sforder;


import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.SFOrderView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 运输管理--托运单管理--托运单明细--右键--保存
 * @author fanglm
 *
 */
public class SaveSFOrderItemAction implements ClickHandler,com.smartgwt.client.widgets.events.ClickHandler{

	private SGTable table = null;
	private SFOrderView view;
	private HashMap<String, String> map;
	private String odr_no;
	private ArrayList<String> sqlList;
	private boolean boo = true;
	
	private HashMap<String, String> ck_map = new HashMap<String, String>();
	
	public SaveSFOrderItemAction(SGTable p_table,SFOrderView view,HashMap<String, String> ck_map) {
		table = p_table;
		this.view = view;
		this.ck_map = ck_map;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		doSave(true);
		//是否与头信息一起保存
	}
	
	public void doSave(final boolean asyn){
		doSave(asyn, null);
	}
	
	public void doSave(final boolean asyn, final AsyncCallback<String> callBack){
		if(!ObjUtil.isNotNull(view.vm.getValueAsString("ID"))){
			MSGUtil.sayError("订单主信息未保存,不能保存明细！");
			return;
		}
		if(!(view.selectRecord == null || 
				StaticRef.ORD_STATUS_CREATE.equals(view.selectRecord.getAttribute("STATUS"))) && 
				view.vm.getValueAsString("STATUS") != null && 
				view.vm.getValueAsString("STATUS").equals(view.selectRecord.getAttribute("STATUS"))){
			MSGUtil.sayError("订单非" + StaticRef.SO_CREATE_NAME + "状态，不能修改！");
			return;
		}
		int[] edit_row = table.getAllEditRows();
		if(edit_row.length == 0){
			if(callBack != null){
				callBack.onSuccess(StaticRef.SUCCESS_CODE);
			}
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
		String appRow = " and ODR_NO='" + odr_no + "'";
		
		
		Util.async.doSave(null, sqlList,appRow, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					if(asyn){
						MSGUtil.showOperSuccess();
					}
					view.enableOrDisables(view.sav_detail_map, false);
					view.enableOrDisables(view.add_map, true);
//					view.enableOrDisables(view.save_map, false);
					view.enableOrDisables(view.del_map, true);
					table.OP_FLAG = "M";
					Criteria crit = table.getCriteria();
					table.invalidateCache();
					crit.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
					crit.addCriteria("ODR_NO",odr_no);
					table.fetchData(crit,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
//							ListGridRecord[] records = table.getRecords();
//							double qty=0.00,vol=0.00,gwt = 0.00;
//							for(int i=0;i<records.length;i++){
//								qty = qty + Double.parseDouble(records[i].getAttributeAsString("QNTY"));
//								vol = vol + Double.parseDouble(ObjUtil.ifNull(records[i].getAttributeAsString("VOL"),"0"));
//								gwt = gwt + Double.parseDouble(ObjUtil.ifNull(records[i].getAttributeAsString("G_WGT"),"0"));
//							}
							
//							//汇总信息
//							view.totalPanel.setValue("TOT_QNTY_SUM", qty);
//							view.totalPanel.setValue("TOT_VOL_SUM", vol + "立方米");
//							view.totalPanel.setValue("TOT_GROSS_W_SUM", gwt + "吨");
						}
					});
					table.discardAllEdits();
				}else {
					MSGUtil.sayError(result);
					
				}
				if(callBack != null){
					callBack.onSuccess(result);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if(callBack != null){
					callBack.onFailure(caught);
				}
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
			
			//fanglm 2011-2-27
			if(table.getRecord(edit_row[i]) != null){
				HashMap<String, String> record_map = Util.putRecordToModel(table.getRecord(edit_row[i]));
				Object[] key = map.keySet().toArray();
				for(int j=0;j<key.length;j++){
					if(key[j].toString().equals("QNTY")||key[j].toString().equals("VOL")||key[j].toString().equals("G_WGT")){
						record_map.put(key[j].toString(), ObjUtil.ifObjNull(map.get(key[j]),0.00).toString());
					}	
					else if(key[j].toString().equals("LOAD_NAME")){
						if(!ObjUtil.isNotNull(map.get("LOAD_NAME"))){
							record_map.put("LOAD_ID","");
							record_map.put("LOAD_NAME","");
						}else{
							record_map.put("LOAD_NAME",map.get("LOAD_NAME"));
							record_map.put("LOAD_ID",map.get("LOAD_ID"));
						}		
					}
					else if(key[j].toString().equals("UNLOAD_NAME")){
						if(!ObjUtil.isNotNull(map.get("UNLOAD_NAME"))){
							record_map.put("UNLOAD_ID","");
							record_map.put("UNLOAD_NAME","");
						}else{
							record_map.put("UNLOAD_NAME",map.get("UNLOAD_NAME"));
							record_map.put("UNLOAD_ID",map.get("UNLOAD_ID"));
						}		
					}else{
						record_map.put(key[j].toString(), ObjUtil.ifObjNull(map.get(key[j]),"").toString());
					}
					
				}
				map = record_map;
			
			}
			
			
			ArrayList<Object> obj = Util.getCheckResult(map, ck_map);
			if(obj != null && obj.size() > 1) {
				String result = obj.get(0).toString();
				if(!result.equals(StaticRef.SUCCESS_CODE)) {
					msg.append(obj.get(1).toString());
					boo = false;
				}
			}
			if(map.get("QNTY") != null && Double.valueOf(String.valueOf(map.get("QNTY"))) <= 0){
				msg.append("货品明细第");
				msg.append(edit_row[i]+1);
				msg.append("行数量必须大于0！");
				boo = false;
			}
			map.put("TABLE", "SF_ORDER_ITEM");
			map.put("ADDWHO", login_user);
			map.put("ODR_NO", view.vm.getValueAsString("ODR_NO"));
			/*if(rList.get(edit_row[i]) != null){
				map.put("ID", rList.get(edit_row[i]).getAttribute("ID"));
			}*/
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
