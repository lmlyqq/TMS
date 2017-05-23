package com.rd.client.action.tms.odrreceipt;

import java.util.HashMap;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * 运输管理-->托运单回单-->【货损货差】保存按钮
 * @author wangjun
 *
 */
public class SaveManageAction extends SaveAction {
	
	private SGTable table;
	private Record curRecord;
	@SuppressWarnings("unused")
	private HashMap<String, String> map;
	private int[] edit_rows = null;
	@SuppressWarnings("unused")
	private HashMap<String, String> check_map;
	public SaveManageAction(SGTable damageTable,HashMap<String, String> p_map,SGForm view) {
		super(damageTable,p_map,view);
		this.table=damageTable;
		check_map = p_map;
	}
	
	@SuppressWarnings("unchecked")
	public void onClick(ClickEvent event) {
		edit_rows = table.getAllEditRows(); 
		for(int i = 0; i < edit_rows.length; i++) {
		    curRecord = table.getEditedRecord(edit_rows[i]);  //获取所有修改的记录
			map = (HashMap<String, String>)table.getEditValues(edit_rows[i]);    //获取记录修改过的值
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
		
		
		if(ObjUtil.isNotNull(curRecord.getAttribute("AMOUNT"))){
		  String AMOUNT=curRecord.getAttribute("AMOUNT");//  残损金额  
		  double T_AMOUNT = Double.parseDouble(AMOUNT);
		  if(T_AMOUNT<0){
			MSGUtil.sayError("【残损金额】不能小于0！");
			return;
		  }
		}else{
			MSGUtil.sayError("【残损金额】不能为空！");
			return;
		}
		super.onClick(event);
//		new SaveAction(table).onClick(event);
		
	}
}
