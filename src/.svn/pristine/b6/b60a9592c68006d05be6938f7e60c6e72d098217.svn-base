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
 * 运输管理--运输执行--运输跟踪--确认签收按钮
 * @author fanglm
 *
 */
public class ConfirmReceiptAction implements ClickHandler {
	
	private TmsTrackView view;
	public Record item;
	private boolean isCustomer = false;
//	private String sign_atary;
	
	public ConfirmReceiptAction(TmsTrackView view,boolean isCust){
		this.view = view;
		this.isCustomer = isCust;
	}

	//执行确认
	private void doConfirm(final String unload_time,final String start_unload_time,final String cast_bill_time){
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
		StringBuffer sb2 = new StringBuffer();
		String load_no  = view.loadReocrd[0].getAttribute("LOAD_NO"); //选中记录调度单号
		ListGridRecord[] shpm_list = view.shpmTable.getSelection();
		for(int i=0 ; i<shpm_list.length ; i++){
			String status = shpm_list[i].getAttribute("STATUS");
			if(isCustomer){
				if(StaticRef.SHPM_LOAD.equals(status) || StaticRef.SHPM_UNLOAD.equals(status)){
					shpm_no_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(shpm_list[i].getAttribute("SHPM_NO")," ").toString());
				}
				else{
					sb2.append(shpm_list[i].getAttribute("SHPM_NO"));
					sb2.append(",");
				}
			}else{
				if(StaticRef.SHPM_LOAD.equals(status)){
					shpm_no_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(shpm_list[i].getAttribute("SHPM_NO")," ").toString());
				}
				else{
					sb.append(shpm_list[i].getAttribute("SHPM_NO"));
					sb.append(",");
				}
			}
		}
		if(sb.length() > 0 && !isCustomer){
			MSGUtil.sayError("部分作业单非【已发运】状态:"+ sb.substring(0,sb.length()-1) + ",不能进行签收操作！");
			return;
		}
			
		if(shpm_list.length == 0){
			MSGUtil.sayError("未选择作业单，不能执行【确认签收】！");
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
		listMap.put("3", cast_bill_time);
		listMap.put("4", start_unload_time);
		listMap.put("5", unload_time);
		listMap.put("6", ABNOMAL_STAT);
		listMap.put("7", ABNOMAL_NOTE);
		listMap.put("8", SERVICE_CODE);
		listMap.put("9", SATISFY_CODE);
		listMap.put("10", UDF1);
		listMap.put("11", UDF2);
		listMap.put("12", NOTES);
		listMap.put("13", shpm_row_list );
		listMap.put("14", load_qnty_list);
		listMap.put("15",unld_qnty_list);
		listMap.put("16",unld_vol_list);
		listMap.put("17",unld_gwgt_list);
		listMap.put("18",unld_nwgt_list);
		listMap.put("19",unld_worth_list);
//		listMap.put("18", sign_atary);
		listMap.put("20",login_user);
		
		String json = Util.mapToJson(listMap);
		Util.async.execProcedure(json, "SP_LOAD_RECEIPT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){ 
					String date = result.substring(2,result.length());
					MSGUtil.showOperSuccess();
					view.status = StaticRef.SHPM_UNLOAD;
//					String[] load_status = result.substring(2).split(",");
//					String unload_time = view.panel.getItem("UNLOAD_TIME").toString();
					if(view.shpmTable != null){
						ListGridRecord[] shpm_list=view.shpmTable.getSelection();
						for(int i=0;i<shpm_list.length;i++){
							
							//wangjun 更新【调度单状态】
							
							if(result.length()<7){
								if(StaticRef.TRANS_PART_UNLOAD_NAME.equals(result.substring(2, result.length()))){ 
									shpm_list[i].setAttribute("LOAD_STATUS_NAME", StaticRef.TRANS_PART_UNLOAD_NAME);
								}else{
									shpm_list[i].setAttribute("LOAD_STATUS_NAME", StaticRef.TRANS_UNLOAD_NAME);
								}
							}else{
								if(StaticRef.TRANS_PART_UNLOAD_NAME.equals(result.substring(4, result.length()))){ 
									shpm_list[i].setAttribute("LOAD_STATUS_NAME", StaticRef.TRANS_PART_UNLOAD_NAME);
								}else{	
									shpm_list[i].setAttribute("LOAD_STATUS_NAME", StaticRef.TRANS_UNLOAD_NAME);
								}
							}
							
							
							shpm_list[i].setAttribute("STATUS", StaticRef.SHPM_UNLOAD);
							shpm_list[i].setAttribute("STATUS_NAME", StaticRef.SHPM_UNLOAD_NAME);
//							shpm_list[i].setAttribute("UNLOAD_TIME", unload_time);
							shpm_list[i].setAttribute("ABNOMAL_STAT", view.panel.getItem("ABNOMAL_STAT").getValue().toString());
							shpm_list[i].setAttribute("ABNOMAL_STAT_NAME", view.panel.getItem("ABNOMAL_STAT").getDisplayValue());
							shpm_list[i].setAttribute("ABNOMAL_NOTES",  ObjUtil.ifObjNull(view.panel.getItem("ABNOMAL_NOTES").getValue()," ").toString());
							shpm_list[i].setAttribute("TRACK_NOTES", ObjUtil.ifObjNull(view.panel.getItem("TRACK_NOTES").getValue()," ").toString());
							shpm_list[i].setAttribute("UNLOAD_DELAY_DAYS"," ");
							shpm_list[i].setAttribute("UNLOAD_TIME",unload_time);
							shpm_list[i].setAttribute("SERVICE_CODE", view.panel.getItem("SERVICE_CODE").getDisplayValue());
							shpm_list[i].setAttribute("SATISFY_CODE", view.panel.getItem("SATISFY_CODE").getDisplayValue());
							shpm_list[i].setAttribute("UDF1",  ObjUtil.ifObjNull(view.panel.getItem("UDF1").getValue()," ").toString());
							shpm_list[i].setAttribute("UDF2",  ObjUtil.ifObjNull(view.panel.getItem("UDF2").getValue()," ").toString());
							
							if(item!=null){
								if(!item.getAttribute("LD_QNTY").toString().equals(item.getAttribute("UNLD_QNTY").toString())){
									shpm_list[i].setAttribute("LOSDAM_FLAG", "Y");
								}
							}
						  shpm_list[i].setAttribute("UNLOAD_DELAY_DAYS", date);
//							view.shpmTable.updateData(shpm_list[i]);
						}
						
//						view.panel.getItem("UNLOAD_TIME").setValue("");
						view.shpmTable.redraw();
					}
//					view.loadReocrd.setAttribute("STATUS", load_status[1]);
//					view.loadReocrd.setAttribute("STATUS_NAME", load_status.[2]);

//					view.loadTable.updateData(view.loadReocrd);
//					view.loadTable.redraw();
					//刷新按钮状态
//					if(StaticRef.SHPM_UNLOAD.equals(load_status[1])){
//					}
					view.initButton(false, true, false, false, false);
				}else{
					MSGUtil.sayError(result);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				SC.say("服务器连接已中断，请重新登录!");
			}
		});
		
	}
	
	//执行确认
	private void doLoadConfirm(final String unload_time,final String start_unload_time,final String cast_bill_time){
		final String ABNOMAL_STAT = ObjUtil.ifObjNull(view.panel.getItem("ABNOMAL_STAT").getValue()," ").toString();//运输异常
		final String ABNOMAL_NOTE = ObjUtil.ifObjNull(view.panel.getItem("ABNOMAL_NOTES").getValue()," ").toString();//异常描述
		final String UDF1 = ObjUtil.ifObjNull(view.panel.getItem("UDF1").getValue()," ").toString();//开门温度
		final String UDF2 = ObjUtil.ifObjNull(view.panel.getItem("UDF2").getValue()," ").toString();//关门温度
		final String NOTES = ObjUtil.ifObjNull(view.panel.getItem("TRACK_NOTES").getValue()," ").toString();//备注
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		String SERVICE_CODE = ObjUtil.ifObjNull(view.panel.getItem("SERVICE_CODE").getValue()," ").toString();
		String SATISFY_CODE = ObjUtil.ifObjNull(view.panel.getItem("SATISFY_CODE").getValue()," ").toString();
		
		String load_no=" ";
		ListGridRecord[] load_list = view.loadTable.getSelection();
		
		if(load_list.length == 0){
			MSGUtil.sayError("未选择调度单，不能执行【确认签收】！");
			return ;
		}else{
			load_no=load_list[0].getAttribute("LOAD_NO");
		}
		
		String status = load_list[0].getAttribute("STATUS_NAME");
		if(!(StaticRef.TRANS_DEPART_NAME.equals(status) || StaticRef.TRANS_PART_UNLOAD_NAME.equals(status))){
			MSGUtil.sayError("调度单状态不能进行签收操作！");
			return;
		}
		
		HashMap<String, Object> listMap = new HashMap<String, Object>();
		listMap.put("1", load_no);		
		listMap.put("2", cast_bill_time);
		listMap.put("3", start_unload_time);
		listMap.put("4", unload_time);
		listMap.put("5", ABNOMAL_STAT);
		listMap.put("6", ABNOMAL_NOTE);
		listMap.put("7", SERVICE_CODE);
		listMap.put("8", SATISFY_CODE);
		listMap.put("9", UDF1);
		listMap.put("10", UDF2);
		listMap.put("11", NOTES);
//		listMap.put("10", sign_atary);
		listMap.put("12",login_user);
		
		String json = Util.mapToJson(listMap);
		Util.async.execProcedure(json, "SP_LOAD_RECEIPT_BYLOADNO(?,?,?,?,?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){ 
					MSGUtil.showOperSuccess();
					if(view.loadTable != null){
						ListGridRecord load_list = view.loadTable.getSelectedRecord();
						load_list.setAttribute("STATUS", StaticRef.TRANS_UNLOAD);
						load_list.setAttribute("STATUS_NAME", StaticRef.TRANS_UNLOAD_NAME);
						load_list.setAttribute("ABNOMAL_STAT", view.panel.getItem("ABNOMAL_STAT").getValue().toString());
						load_list.setAttribute("ABNOMAL_STAT_NAME", view.panel.getItem("ABNOMAL_STAT").getDisplayValue());
						load_list.setAttribute("ABNOMAL_NOTES",  ObjUtil.ifObjNull(view.panel.getItem("ABNOMAL_NOTES").getValue()," ").toString());
						load_list.setAttribute("TRACK_NOTES", ObjUtil.ifObjNull(view.panel.getItem("TRACK_NOTES").getValue()," ").toString());
						load_list.setAttribute("UNLOAD_DELAY_DAYS"," ");
						load_list.setAttribute("UNLOAD_TIME",unload_time);
						load_list.setAttribute("SERVICE_CODE", view.panel.getItem("SERVICE_CODE").getDisplayValue());
						load_list.setAttribute("SATISFY_CODE", view.panel.getItem("SATISFY_CODE").getDisplayValue());
						load_list.setAttribute("UDF1",  ObjUtil.ifObjNull(view.panel.getItem("UDF1").getValue()," ").toString());
						load_list.setAttribute("UDF2",  ObjUtil.ifObjNull(view.panel.getItem("UDF2").getValue()," ").toString());
						view.loadTable.redraw();
					}
					view.initButton(false, true, false, false, false);
				}else{
					MSGUtil.sayError(result);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				SC.say("服务器连接已中断，请重新登录!");
			}
		});
		
	}
	
	private void doUnloadConfirm(final String unload_time,final String start_unload_time){
		ListGridRecord[] unload_list=null;
		if(ObjUtil.isNotNull(view.unloadList)){
			unload_list=view.unloadList.getSelection();
		}else{
			MSGUtil.sayError("请选择卸货点");
			return;
		}
		String load_no=" ";
		if(unload_list.length==0){
			MSGUtil.sayError("请选择卸货点");
			return;
		}
		String unload_id=view.unloadList.getSelectedRecord().getAttribute("UNLOAD_ID");
		if(ObjUtil.isNotNull(view.unloadTable.getSelectedRecord())){
			load_no=view.unloadTable.getSelectedRecord().getAttribute("LOAD_NO");
		}else{
			MSGUtil.sayError("请选择一个调度单");
			return;
		}
		final String ABNOMAL_STAT = ObjUtil.ifObjNull(view.panel.getItem("ABNOMAL_STAT").getValue()," ").toString();//运输异常
		final String ABNOMAL_NOTE = ObjUtil.ifObjNull(view.panel.getItem("ABNOMAL_NOTES").getValue()," ").toString();//异常描述
		final String UDF1 = ObjUtil.ifObjNull(view.panel.getItem("UDF1").getValue()," ").toString();//开门温度
		final String UDF2 = ObjUtil.ifObjNull(view.panel.getItem("UDF2").getValue()," ").toString();//关门温度
		final String NOTES = ObjUtil.ifObjNull(view.panel.getItem("TRACK_NOTES").getValue()," ").toString();//备注
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		String SERVICE_CODE = ObjUtil.ifObjNull(view.panel.getItem("SERVICE_CODE").getValue()," ").toString();
		String SATISFY_CODE = ObjUtil.ifObjNull(view.panel.getItem("SATISFY_CODE").getValue()," ").toString();
		
		HashMap<String,Object> listMap=new HashMap<String,Object>();
		listMap.put("1", load_no);
		listMap.put("2", unload_id);
		listMap.put("3", start_unload_time);
		listMap.put("4", unload_time);
		listMap.put("5", ABNOMAL_STAT);
		listMap.put("6", ABNOMAL_NOTE);
		listMap.put("7", SERVICE_CODE);
		listMap.put("8", SATISFY_CODE);
		listMap.put("9", UDF1);
		listMap.put("10", UDF2);
		listMap.put("11", NOTES);
		listMap.put("12", "");
//		listMap.put("12", sign_atary);
		listMap.put("13",login_user);
		
		String json=Util.mapToJson(listMap);
		Util.async.execProcedure(json, "SP_LOAD_RECEIPT_BYUNLOAD(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				SC.say("服务器连接已中断，请重新登录!");
			}
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					ListGridRecord rec = view.unloadList.getSelectedRecord();
					rec.setAttribute("STATUS", StaticRef.SHPM_UNLOAD);
					rec.setAttribute("STATUS_NAME", StaticRef.SHPM_UNLOAD_NAME);
					view.initButton(false, true, false, false, false);
					view.unloadList.redraw();
				}else{
					MSGUtil.sayError(result);
				}
			}
		});
	}
	

//	private void gettime(){
//		 //String load_no = view.shpmTable.getSelection()[0].getAttribute("LOAD_NO");
//		 String depart_time = view.shpmTable.getSelection()[0].getAttribute("DEPART_TIME");
//		 if(depart_time.length() > 19) {
//			 depart_time = depart_time.substring(0,19);
//		 }
//		 String unload_time = view.panel.getValue("UNLOAD_TIME").toString();
//		 if(!DateUtil.isAfter(unload_time, depart_time.substring(0, 19))){
//		     doConfirm(view.panel.getValue("UNLOAD_TIME").toString());
//		 }else {
//			 MSGUtil.sayWarning("到货签收时间不能小于实际发货时间！");
//		 }
//	}
	
	@Override
	public void onClick(ClickEvent event) {
		String tab_flag=view.tabSet.getSelectedTab().getID();
		final Object unload_time = view.panel.getValue("UNLOAD_TIME"); //实际签收时间
		final Object start_unload_time = view.panel.getValue("START_UNLOAD_TIME");  //开始卸货时间
		final Object cast_bill_time = view.panel.getValue("CAST_BILL_TIME"); //投单时间
//		sign_atary=ObjUtil.ifObjNull(view.panel.getValue("SIGNATARY"), " ").toString().trim();
		if(!ObjUtil.isNotNull(cast_bill_time)){
			MSGUtil.sayError("投单时间不能为空！");
			return;
		} 	
		if(!ObjUtil.isNotNull(unload_time)){
			MSGUtil.sayError("完成卸货时间不能为空！");
			return;
		} 
		if(!ObjUtil.isNotNull(start_unload_time)){
			MSGUtil.sayError("开始卸货时间不能为空！");
			return;
		}
		if(!DateUtil.isAfter(start_unload_time.toString(), unload_time.toString())){
			 MSGUtil.sayWarning("完成卸货时间不能小于开始卸货时间！");
			 return;
		}
		switch (Integer.parseInt(tab_flag.substring(0,1))){
		case 0:
			doConfirm(unload_time.toString(),start_unload_time.toString(),cast_bill_time.toString());  //按作业单签收
			break;
		case 1:
			doLoadConfirm(unload_time.toString(),start_unload_time.toString(),cast_bill_time.toString());  //按调度单签收
			break;
		case 2:
			doUnloadConfirm(unload_time.toString(),start_unload_time.toString());  //按卸货地签收
			break;
		default :
			break;
		}
	}

}
