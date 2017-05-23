package com.rd.client.action.tms.shpmreceipt;

import java.util.ArrayList;
import java.util.HashMap;

import com.rd.client.common.action.SaveAction;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;

public class SaveShpmAction extends SaveAction {
	private SGTable table;
	private int[] edit_rows = null;
	private Record curRecord;
	private SGForm view;
//	private HashMap<String, String> map;
	
	public SaveShpmAction(SGTable damageTable,HashMap<String, String> p_map,SGForm view) {
		super(damageTable,p_map,view);
		this.table=damageTable;
		this.view = view;
	}
	public void onClick(ClickEvent event) {
		edit_rows = table.getAllEditRows(); 
		for(int i = 0; i < edit_rows.length; i++) {
		    curRecord = table.getEditedRecord(edit_rows[i]);  //获取所有修改的记录
//			map = (HashMap<String, String>)table.getEditValues(edit_rows[i]);    //获取记录修改过的值
		}
		
		String SKU_NAME=curRecord.getAttribute("SKU_NAME");
		if(!ObjUtil.isNotNull(SKU_NAME) ){
			MSGUtil.sayError("【货品名称】不能为空！");
			return;
		}
		String QNTY = curRecord.getAttribute("QNTY");
//		if(!ObjUtil.isNotNull(QNTY) ){
//			MSGUtil.sayError("【残损数量】不能为空！");
//			return;
//		}
		
		String TRANS_UOM = curRecord.getAttribute("TRANS_UOM");
		if(!ObjUtil.isNotNull(TRANS_UOM) ){
			MSGUtil.sayError("【单位】不能为空！");
			return;
		}
		
		if(ObjUtil.isNotNull(QNTY)){
		  String TRANS_QNTY=curRecord.getAttribute("QNTY");//货损数量 
		  double T_TRANS_QNTY = Double.parseDouble(TRANS_QNTY);
		  if(T_TRANS_QNTY<=0){
				MSGUtil.sayError("【残损数量】必须大于0！");
				return;
	      }
		}else{
			MSGUtil.sayError("【残损数量】不能为空！");
			return;
		}
		
		  String AMOUNT=curRecord.getAttribute("AMOUNT");		 
		  if(AMOUNT==null){
			  AMOUNT="0";
		  }
		  double amount=Double.parseDouble(AMOUNT);
		  
		  String COMPANY_ACOUNT=curRecord.getAttribute("COMPANY_ACOUNT");		 
		  if(COMPANY_ACOUNT==null){
			  COMPANY_ACOUNT="0";
		  }
		  double c_acount=Double.parseDouble(COMPANY_ACOUNT);
		  
		  String DRIVER_ACOUNT=curRecord.getAttribute("DRIVER_ACOUNT");		 
		  if(DRIVER_ACOUNT==null){
			  DRIVER_ACOUNT="0";
		  }
		  double d_acount=Double.parseDouble(DRIVER_ACOUNT);
		  
		  if(amount>(c_acount+d_acount)){
			  MSGUtil.sayError("货损金额不能大于公司和司机的承担总额");
			  return;
		  }
//		if(ObjUtil.isNotNull(curRecord.getAttribute("AMOUNT"))){
//		  String AMOUNT=curRecord.getAttribute("AMOUNT");//  残损金额  
//		  double T_AMOUNT = Double.parseDouble(AMOUNT);
//		  if(T_AMOUNT<0){
//			MSGUtil.sayError("【残损金额】不能小于0！");
//			return;
//		  }
//		}else{
//			MSGUtil.sayError("【残损金额】不能为空！");
//			return;
//		}
		ArrayList<String> list= new ArrayList<String>();
		list.add(curRecord.getAttribute("SHPM_NO"));
		list.add(curRecord.getAttribute("SKU_ID"));
		
		setProName("SP_SETT_SAVE_DAMAGE_FEE(?,?,?)");
		setValList(list);
		
		super.onClick(event);
		
		if(view instanceof TmsTrackView){
			((TmsTrackView)view).initDMSaveBtn();
		}
	}
	
	
}
