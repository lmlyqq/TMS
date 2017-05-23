package com.rd.client.action.base.supplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.action.SaveMultiFormAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 供应商管理--保存按钮
 * @author Administrator
 *
 */
public class SaveSupplierAction extends SaveMultiFormAction {
	
	private Map<String, String> record;
	private ValuesManager form;
	private ListGrid table;
//	private HashMap<String, String> map;
	private String proName;
	private SGForm view;

	public SaveSupplierAction(ListGrid pTable, ValuesManager pForm,
			HashMap<String, String> pMap, SGForm view) {
		super(pTable, pForm, pMap, view);
		this.table = pTable;
		this.form = pForm;
		this.view = view;
//		this.map = pMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		//检查登录信息
		if(LoginCache.getLoginUser() == null){
			MSGUtil.sayError(Util.MI18N.UN_LOAD_LOGIN_USER());
			return;
		}
		
	
		String op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
		if(op_flag.equals("A")){ //调用存储过程实现插入操作
			record = form.getValues();
			
			StringBuffer msg = new StringBuffer();
			if(!ObjUtil.isNotNull(record.get("SUPLR_CODE"))){
				msg.append("[");
				msg.append(Util.TI18N.SUP_SUPLR_CODE());
				msg.append("],");
			}
			if(!ObjUtil.isNotNull(record.get("SUPLR_CNAME"))){
				msg.append("[");
				msg.append(Util.TI18N.SUP_SUPLR_CNAME());
				msg.append("],");
			}
			if(!ObjUtil.isNotNull(record.get("SHORT_NAME"))){
				msg.append("[");
				msg.append(Util.TI18N.SHORT_NAME());
				msg.append("],");
			}
			
			if(msg.length() > 0){
				MSGUtil.sayWarning(msg.toString().substring(0,msg.length() -1 ) + "不能为空！");
				return;
			}
			
			doSave();//插入保存
		}else{
			//通用方法实现修改操作
			super.onClick(event);
		}
		
	}
	private void doSave(){
		String keys ="SUPLR_CODE,SUPLR_CNAME,SUPLR_ENAME,SHORT_NAME,HINT_CODE,TRANSPORT_FLAG,WAREHOUSE_FLAG,SUPLR_TYP,"
			+"CONTRACT_FLAG,INTL_FLAG,BILL_TO,PROPERTY,GRADE,REPRESENTATIVE,AREA_ID,ADDRESS,ZIP,CONT_NAME,CONT_TEL,"
			+"CONT_FAX,CONT_EMAIL,URL,MAINT_ORG_ID,BANK,ACC_NUM,TAXNO,INVOICE_TITLE,INV_DEADLINE,SETT_TYP,"
			+"PAY_TYP,SETT_CYC,CREDIT_LIMIT,CURRENCY,AP_DEADLINE,WHSE_ID,EQMT_NUM,VEHICLE_FOR_FLAG,INVOICE_FLAG,"
			+"INS_FLAG,INS_AMT,INS_EFCT_DT,INS_EXP_DT,NOTES,REP_TIL,ENABLE_FLAG,BLACKLIST_FLAG,UDF2,UDF4";
	
		Util.async.execProcedure(recordToList(keys), proName, new AsyncCallback<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					ListGridRecord record = new ListGridRecord();
					record.setAttribute("ID", result.substring(2));
					Util.updateToRecord(form, table, record);   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
					ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
					list.add(0,record);
					table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()]));
					table.redraw();
					table.selectRecord(record);

					MSGUtil.showOperSuccess();
					
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
					view.initSaveBtn();
				}
				else if(result.substring(0, 2).equals("02")){
					MSGUtil.sayError(Util.TI18N.SUP_SUPLR_CODE()+","+Util.TI18N.SUP_SUPLR_CNAME()+","
							+Util.TI18N.SHORT_NAME()+","+Util.TI18N.HINT_CODE()+";"+Util.MI18N.CHK_MUSTUNIQUE());
				}
				else{ 
					MSGUtil.sayError(result);
				}
				//System.out.println(result);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	private ArrayList<String> recordToList(String keys){
		
		String[] key = keys.split(",");
		ArrayList<String> list= new ArrayList<String>();
		StringBuffer name = new StringBuffer();
		name.append("SAVE_SUPPLIER(");
		int i=0;
		for(i=0;i<key.length;i++){
			String keyName = key[i];
			if(keyName.indexOf("_FLAG") >= 0){
				list.add(getFlag(record.get(keyName)));
			}else{
				list.add(getValue(record.get(keyName)));
			}
			name.append("?,");
		}
		
		list.add(LoginCache.getLoginUser().getUSER_ID()); //当前用户
		list.add(LoginCache.getLoginUser().getDEFAULT_ORG_ID()); //当前用户默认组织机构ID
		list.add(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		name.append("?,?,?,?)"); //user_id,org_id,org_name,out_result_code
		
		proName = name.toString();
		return list;
	}
	
	private String getValue(Object obj){
		if(obj == null){
			return "";
		}else{
			return obj.toString();
		}
	}
	private String getFlag(Object obj){
		if(obj == null || obj.toString().equals("false")){
			return "N";
		}else{
			return "Y";
		}
	}
	

}
