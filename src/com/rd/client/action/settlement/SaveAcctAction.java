package com.rd.client.action.settlement;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.SuplrFeeSettView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 财务管理--结算管理--供应商结算管理--保存按钮
 * @author fanglm
 * @create time 2011-03-01 14:21
 */
public class SaveAcctAction implements ClickHandler{
	
	private SGTable table;
	private ListGridRecord[] records;
	private SuplrFeeSettView view;
	
	public SaveAcctAction (SGTable table,SuplrFeeSettView view){
		this.table = table;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		records = table.getSelection();
		if(records.length == 0)
			return;
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		String load_no="";
		String SN = "";
		String NOTES = "";
		double mile =0.0;
		double price =0.0;
		double ratio = 0.0;
		double qnty =0.0;
		double dueFee =0.00;
		double acc = 0.00;
		String modify_flag = "N";
		
		StringBuffer sql;
		ArrayList<String> sqlList = new ArrayList<String>();
		
		for(int i=0;i<records.length;i++){
			int row = table.getRecordIndex(records[i]);
			if(i == 0){
				load_no = records[0].getAttribute("LOAD_NO");
				mile = Double.parseDouble(
						ObjUtil.ifObjNull(table.getEditValue(row, "MILE"),records[0].getAttribute("MILE")).toString());
//				SN = ObjUtil.ifObjNull(table.getEditValue(row,"SERIAL_NUM"),"").toString();
//				NOTES = ObjUtil.ifObjNull(table.getEditValue(row,"NOTES"),"").toString();
				
				SN = ObjUtil.ifObjNull(table.getEditValue(row,"SERIAL_NUM"),ObjUtil.ifObjNull(records[0].getAttribute("SERIAL_NUM"),"")).toString();
				NOTES = ObjUtil.ifObjNull(table.getEditValue(row,"NOTES"),
						ObjUtil.ifObjNull(records[0].getAttribute("NOTES"),"")).toString();
				price = Double.parseDouble(
							ObjUtil.ifObjNull(table.getEditValue(row,"PRICE"),
						ObjUtil.ifObjNull(records[0].getAttribute("PRICE"),"")).toString());
				
				acc = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "DUE_FEE"),records[i].getAttribute("DUE_FEE")).toString());
				
			}
			if(load_no.equals(records[i].getAttributeAsString("LOAD_NO"))){
				qnty = Double.parseDouble(ObjUtil.ifNull(records[i].getAttribute("UNLD_QNTY"),"0").toString());
				ratio = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "RATIO"), records[i].getAttributeAsString("RATIO")).toString());
				//price = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "PRICE"),records[i].getAttributeAsString("PRICE")).toString());
				dueFee = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "DUE_FEE"),records[i].getAttribute("DUE_FEE")).toString());
				table.setEditValue(row, "MILE", mile);
				table.setEditValue(row, "SERIAL_NUM", SN);
				table.setEditValue(row, "NOTES", NOTES);
				table.setEditValue(row,"PRICE",price);
				
				if(i==0 && dueFee - qnty*ratio*mile*price*0.001 != 0){
					table.setEditValue(row, "MODIFY_FLAG", "Y");
					modify_flag = "Y";
				}else{
					if(NOTES.indexOf("包小车") < 0){
						if(acc == 0){
							dueFee = 0.00;
						}else{
							dueFee = qnty*ratio*mile*price*0.001;
						}
						table.setEditValue(row, "MODIFY_FLAG", "N");
						modify_flag = "N";
						table.setEditValue(row,"DUE_FEE",dueFee);
					}else{
						dueFee = 0;
						table.setEditValue(row, "MODIFY_FLAG", "N");
						modify_flag = "N";
						table.setEditValue(row,"DUE_FEE",dueFee);
					}
				}
				
				sql = new StringBuffer();
				sql.append("update trans_bill_detail set ratio=");
				sql.append(ratio);
				sql.append(",price=");
				sql.append(price);
				sql.append(",mile=");
				sql.append(mile);
				sql.append(",due_fee=");
				sql.append(dueFee);
				sql.append(",modify_flag='");
				sql.append(modify_flag);
				if(ObjUtil.isNotNull(SN)){
					sql.append("',SERIAL_NUM ='");
					sql.append(SN);
				}
				if(table.getEditValue(row, "NOTES") != null){
					sql.append("',NOTES ='");
					sql.append(NOTES);
//					sql.append(table.getEditValue(row, "NOTES").toString());
				}
				sql.append("',edittime=sysdate,editwho='");
				sql.append(loginUser);
				sql.append("' where id='");
				sql.append(records[i].getAttributeAsString("ID"));
				sql.append("'");
				
				sqlList.add(sql.toString());
			}else{
				MSGUtil.sayWarning("此操作只能针对同一调度单！");
				return;
			}
		}
		
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					ListGridRecord[] selects = table.getSelection();
					ListGridRecord[] records = table.getRecords();
					int row;
					for(int i=0;i<selects.length;i++){
						row = table.getRecordIndex(selects[i]);
						Record rec = table.getEditedRecord(row);
						String[] att = rec.getAttributes();
						for(int j=0;j<att.length;j++){
							if("false".equals(rec.getAttributeAsString(att[j]))){
								records[row].setAttribute(att[j], rec.getAttributeAsBoolean(att[j]));
							}else{
								records[row].setAttribute(att[j], rec.getAttribute(att[j]));
							}
						}
					}
//					table.invalidateCache();
//					table.discardAllEdits();
//					table.redraw();
					
					ArrayList list = new ArrayList(Arrays.asList(records));
					table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); 
					
					table.selectRecord(table.getRecordIndex(selects[0]));
					view.initSaveBtn();
					
				}else{
					MSGUtil.sayError(result.substring(2));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

}
