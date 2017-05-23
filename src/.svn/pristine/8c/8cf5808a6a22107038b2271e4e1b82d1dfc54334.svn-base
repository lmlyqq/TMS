package com.rd.client.action.base.customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.action.SaveMultiFormAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 客户管理--保存按钮
 * @author Administrator
 *
 */
public class SaveCustomerAction extends SaveMultiFormAction {
	
	private Map<String, String> record;
	private ValuesManager form;
	private ListGrid table;
	private String proName;
	private SGForm view;
    private SGCombo PARENT_CUSTOMER_ID;
	
	public SaveCustomerAction(ListGrid pTable, ValuesManager pForm,
			HashMap<String, String> pMap,SGForm view,SGCombo PARENT_CUSTOMER_ID) {
		super(pTable, pForm, pMap,view);
		this.table = pTable;
		this.form = pForm;
		this.view = view;
		this.PARENT_CUSTOMER_ID=PARENT_CUSTOMER_ID;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		//检查登录信息
		if(LoginCache.getLoginUser() == null){
			MSGUtil.sayError(Util.MI18N.UN_LOAD_LOGIN_USER());
			return;
		}
		String CUSTOM_ATTR=form.getItem("CUSTOM_ATTR").getValue().toString();
		if (CUSTOM_ATTR==null || CUSTOM_ATTR.length()==0){
			MSGUtil.sayError("请填写客户属性");
			return;
		}
		String TAX_RATE = "";
		if(ObjUtil.isNotNull(form.getItem("UDF2")) && ObjUtil.isNotNull(form.getItem("UDF2").getValue())){
			TAX_RATE=ObjUtil.ifNull(form.getItem("UDF2").getValue().toString(), "1");
			try {
				if(Float.valueOf(TAX_RATE)>100 || Float.valueOf(TAX_RATE)<=0){
					MSGUtil.sayError("税率必须在0 - 100之间");
					return;
				}
			} catch (Exception e) {
				MSGUtil.sayError("输入的数值有误，税率必须在0 - 100之间");
				return;
			}
		}else{
			TAX_RATE="1";
		}
		if(TAX_RATE.substring(0,1).equals(".")){
			TAX_RATE="0"+TAX_RATE;
		}
		form.setValue("UDF2", TAX_RATE);
		String op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
		if(op_flag.equals("A")){ //调用存储过程实现插入操作
			
			record = Util.illegalMapFilter(form.getValues());
			
			Object CUSTOM_ACCOUNT = record.get("CUSTOM_ACCOUNT");
			if(ObjUtil.isNotNull(CUSTOM_ACCOUNT)){
				String regex = "^[0-9]{0,8}$";
				if(!CUSTOM_ACCOUNT.toString().matches(regex)){
					MSGUtil.sayWarning("客户账期只能输入数字类型且不能大于8位！");
					return;
				}
			}
			
			StringBuffer msg = new StringBuffer();
			if(!ObjUtil.isNotNull(record.get("CUSTOMER_CODE"))){
				msg.append("[");
				msg.append(Util.TI18N.CUSTOMER_CODE());
				msg.append("],");
			}
			if(!ObjUtil.isNotNull(record.get("CUSTOMER_CNAME"))){
				msg.append("[");
				msg.append(Util.TI18N.CUSTOMER_CNAME());
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
		String keys ="CUSTOMER_CODE,CUSTOMER_CNAME,CUSTOMER_ENAME,SHORT_NAME,HINT_CODE,CUSTOMER_FLAG,PAYER_FLAG,TRANSPORT_FLAG,WAREHOUSE_FLAG"
            + ",BILL_TO,INDUSTRY,PROPERTY,REPRESENTATIVE,GRADE,AREA_ID,ADDRESS,ZIP,CONT_NAME,CONT_TEL,CONT_FAX,CONT_EMAIL,URL,MAINT_ORG_ID,PARENT_CUSTOMER_ID"
            + ",CONTACTER_FLAG,SKU_ATTR,INVOICE_FLAG,INV_DEADLINE,BANK,ACC_NUM,TAXNO,INVOICE_TITLE,SETT_TYP,PAY_TYP,SETT_RUL,SETT_CYC,CREDIT_LIMIT"
            + ",CURRENCY,AR_DEADLINE,FOLLOWUP,DFT_SUPLR_ID,LOAD_ID,UNLOAD_ID,PACK_ID,TRANS_UOM,LENGTH_UNIT,WEIGHT_UNIT,VOLUME_UNIT,MATCHROUTE_FLAG,UNIQ_CONO_FLAG"
            + ",UNIQ_ADDR_FLAG,DFT_SKU_ID,SKU_EDIT_FLAG,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,COD_FLAG,POD_FLAG,REP_TIL,GEN_METHOD,NOTES,ENABLE_FLAG,SKU_NAME,ADDR_EDIT_FLAG," +
    		"CUSTOM_ATTR,CUSTOM_ACCOUNT,VOL_GWT_RATIO,UDF1,UDF2,UDF3,UDF4";
	
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
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);

					MSGUtil.showOperSuccess();
					
					view.initSaveBtn();
					
					final String swhere = " WHERE CUSTOMER_FLAG='Y' AND ENABLE_FLAG='Y' ";

					Util.db_async.getRecord("ID,SHORT_NAME","BAS_CUSTOMER", swhere, null,new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							if(result!=null&&result.size()>0){
							
								LinkedHashMap< String, String> valuemap=new LinkedHashMap<String, String>();
								
								for(int i=0;i<result.size();i++){
									
									valuemap.put(result.get(i).get("ID"), result.get(i).get("SHORT_NAME"));
								}
								
								PARENT_CUSTOMER_ID.setValueMap(valuemap);
							}
							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
							
				}
				else if(result.substring(0, 2).equals("02")){
					MSGUtil.sayError(Util.TI18N.CUSTOMER_CODE()+","+Util.TI18N.CUSTOMER_CNAME()+","
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
		name.append("SAVE_CUSTOMER(");
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
		name.append("?,?,?,?)"); //user_id,org_id,org_name,out_return_code
		
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
