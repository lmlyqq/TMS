package com.rd.client.action.tms.track;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.DateUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 运输管理--运输执行--运输跟踪--标记签收按钮
 * @author fanglm
 *
 */
public class SignConfirmReceiptAction implements ClickHandler {
	
	private TmsTrackView view;
	public Record item;
	//private boolean isCustomer = false; 
	
	public SignConfirmReceiptAction(TmsTrackView view,boolean isCust){
		this.view = view;
	}

	//执行确认
	private void doConfirm(final String unload_time){
		String load_no  = view.loadReocrd[0].getAttribute("LOAD_NO"); //选中记录调度单号
		final String ABNOMAL_STAT = ObjUtil.ifObjNull(view.panel.getItem("ABNOMAL_STAT").getValue()," ").toString();//运输异常
		final String ABNOMAL_NOTE = ObjUtil.ifObjNull(view.panel.getItem("ABNOMAL_NOTES").getValue()," ").toString();//异常描述
		final String UDF1 = ObjUtil.ifObjNull(view.panel.getItem("UDF1").getValue()," ").toString();//开门温度
		final String UDF2 = ObjUtil.ifObjNull(view.panel.getItem("UDF2").getValue()," ").toString();//关门温度
		final String NOTES = ObjUtil.ifObjNull(view.panel.getItem("TRACK_NOTES").getValue()," ").toString();//备注
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		String SERVICE_CODE = ObjUtil.ifObjNull(view.panel.getItem("SERVICE_CODE").getValue()," ").toString();
		String SATISFY_CODE = ObjUtil.ifObjNull(view.panel.getItem("SATISFY_CODE").getValue()," ").toString();
		
		HashMap<String, String> shpm_no_list = new HashMap<String, String>(); // 作业单号
		HashMap<String, String> shpm_row_list = new HashMap<String, String>(); //明细行号
		HashMap<String, String> load_qnty_list = new HashMap<String, String>(); //发货数量
		HashMap<String, String> unld_qnty_list = new HashMap<String, String>(); //收货适量
		HashMap<String, String> unld_vol_list = new HashMap<String, String>(); //收货体积
		HashMap<String, String> unld_gwgt_list = new HashMap<String, String>();//收货毛重
		HashMap<String, String> unld_nwgt_list = new HashMap<String, String>();//收货净重
		HashMap<String, String> unld_worth_list = new HashMap<String, String>();//收货货值
		
		StringBuffer sb = new StringBuffer();
		ListGridRecord[] shpm_list = view.shpmTable.getSelection();
		for(int i=0 ; i<shpm_list.length ; i++){
			String status = shpm_list[i].getAttribute("STATUS");
			if(StaticRef.SHPM_LOAD.equals(status)){
				shpm_no_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(shpm_list[i].getAttribute("SHPM_NO")," ").toString());
			}
			else{
				sb.append(shpm_list[i].getAttribute("SHPM_NO"));
				sb.append(",");
			}
		}
		
		if(sb.length() > 0){
			MSGUtil.sayError("部分作业单非【已发运】状态:"+ sb.substring(0,sb.length()-1) + ",不能进行标记签收操作！");
			return;
		}
		
		if(shpm_list.length == 0){
			MSGUtil.sayError("未选择作业单，不能执行【标记签收】！");
			return ;
		}
		
		if(shpm_list.length == 1 && view.shpmlstTable != null){ //一条作业单签收可能存在货损货差
			ListGridRecord[] item_list  = view.shpmlstTable.getRecords();
			for(int i=0 ; i<item_list.length ; i++){
			    item = view.shpmlstTable.getEditedRecord(i);
				if(shpm_list[0].getAttribute("SHPM_NO").equals(item.getAttribute("SHPM_NO"))){
					shpm_row_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(item.getAttribute("SHPM_ROW")," ").toString());
					load_qnty_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(item.getAttribute("QNTY")," ").toString());
					unld_qnty_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(item.getAttribute("UNLD_QNTY")," ").toString());
					unld_vol_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(item.getAttribute("UNLD_VOL")," ").toString());
					unld_gwgt_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(item.getAttribute("UNLD_GWGT")," ").toString());
					unld_nwgt_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(item.getAttribute("UNLD_NWGT")," ").toString());
					unld_worth_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(item.getAttribute("UNLD_WORTH")," ").toString());
				}else{
					shpm_row_list.put("1", " ");
					load_qnty_list.put("1", " ");
					unld_qnty_list.put("1", " ");
					unld_vol_list.put("1", " ");
					unld_gwgt_list.put("1", " ");
					unld_nwgt_list.put("1", " ");
					unld_worth_list.put("1", " ");
				}
			}
		}else{ //同时选中多条作业单是视为完好签收
			shpm_row_list.put("1", " ");
			load_qnty_list.put("1", " ");
			unld_qnty_list.put("1", " ");
			unld_vol_list.put("1", " ");
			unld_gwgt_list.put("1", " ");
			unld_nwgt_list.put("1", " ");
			unld_worth_list.put("1", " ");
		}
		
		HashMap<String, Object> listMap = new HashMap<String, Object>();
		listMap.put("1", load_no);
		listMap.put("2", shpm_no_list);
		listMap.put("3", unload_time);
		listMap.put("4", ABNOMAL_STAT);
		listMap.put("5", ABNOMAL_NOTE);
		listMap.put("6", SERVICE_CODE);
		listMap.put("7", SATISFY_CODE);
		listMap.put("8", UDF1);
		listMap.put("9", UDF2);
		listMap.put("10", NOTES);
		listMap.put("11",login_user);
		
		
		String json = Util.mapToJson(listMap);
		Util.async.execProcedure(json, "SP_LOAD_PRERECEIPT(?,?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){ 
					MSGUtil.showOperSuccess();
					
					if(view.shpmTable != null){
						ListGridRecord[] shpm_list = view.shpmTable.getSelection();
						for(int i=0;i<shpm_list.length;i++){							
							shpm_list[i].setAttribute("RECE_FLAG", "Y");
						}
						view.shpmTable.redraw();
					}
				
				}else{
					MSGUtil.sayError(result);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				SC.say("服务器连接已中断，请重新登录!");
			}
		});
		
	}
	

	private void gettime(){
		 //String load_no = view.shpmTable.getSelection()[0].getAttribute("LOAD_NO");
		 String depart_time = view.shpmTable.getSelection()[0].getAttribute("DEPART_TIME");
		 String unload_time = view.panel.getValue("UNLOAD_TIME").toString();
		 if(!DateUtil.isAfter(unload_time, depart_time)){
				doConfirm(view.panel.getValue("UNLOAD_TIME").toString());
			}else {
				MSGUtil.sayWarning("到货签收时间不能小于实际发货时间！");
			}
		 /*Util.db_async.getSingleRecord("MAX(DEPART_TIME)", "V_SHIPMENT_HEADER", " WHERE LOAD_NO='"+load_no+"'", null, new AsyncCallback<HashMap<String,String>>() {
			
			@Override
			public void onSuccess(HashMap<String, String> result) {
				String depart_time = result.get("MAX(DEPART_TIME)");
				String unload_time = view.panel.getValue("UNLOAD_TIME").toString();
//				boolean x =DateUtil.isAfter(unload_time, depart_time);
				if(!DateUtil.isAfter(unload_time, depart_time)){
					doConfirm(view.panel.getValue("UNLOAD_TIME").toString());
				}else {
					MSGUtil.sayWarning("到货签收时间不能小于实际发货时间！");
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});*/
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		final Object unload_time = view.panel.getValue("UNLOAD_TIME"); //实际签收时间
		if(!ObjUtil.isNotNull(unload_time)){
			MSGUtil.sayError("到货签收时间不能为空！");
			return;
		} 
		
		//校验到货签收时间不能大于当前系统时间
		Util.async.getServTime("yyyy/MM/dd HH:mm",new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				 if(DateUtil.isAfter(unload_time.toString(),result)){
					 gettime();
					 //执行确认签收操作
//					 doConfirm(unload_time.toString());
				 }else{
					 MSGUtil.sayError("到货签收时间不能大于当前系统时间！");
					 return;
				 }
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});

	}

}
