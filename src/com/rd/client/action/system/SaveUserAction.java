package com.rd.client.action.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 系统管理--用户管理--调用存储过程执行保存操作
 * @author fanglm
 *
 */
public class SaveUserAction implements ClickHandler{
	
	private SGTable table;
	private ValuesManager form;
	private HashMap<String, String> map;
	private Map<String, String> record;
	private String proName;
	private String op_flag="M";
	private String passwd="";
	private String salt="";
	private SGForm view;
	public SaveUserAction(SGTable pTable, ValuesManager pForm,
			HashMap<String, String> pMap,SGForm view) {
		this.table = pTable;
		this.form = pForm;
		this.map = pMap;
		this.view = view;
		
	}
	

	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
        
		op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
		form.setValue("USER_ID", form.getValueAsString("USER_ID").trim());
        record = form.getValues(); 
        if(record == null) {
        	record = new HashMap<String, String>();
        }
        record.remove("OP_FLAG");
        record.put("ID", "");
        if(isNull(true, "ROLE_ID", "USERGRP_ID")){
        	MSGUtil.sayError("角色名称,用户组至少填写一个!");
        	return;
        }
		if(op_flag.equals("M")){
//			if(table.getSelectedRecord() == null){
//				return;
//			}
			record.put("ID", table.getSelectedRecord().getAttributeAsString("ID"));
			passwd = record.get("PASSWORD");
			if(passwd == null) {
				MSGUtil.sayError("密码不能为空!");
				return;
			}
			if(!table.getSelectedRecord().getAttribute("PASSWORD").equals(record.get("PASSWORD"))) {
//				if(!isValidPwd(passwd)) {
//					MSGUtil.sayError("密码必须由6-16位的数字和字母组成!");
//					return;
//				}
			}
			salt = table.getSelectedRecord().getAttribute("SALT");
		}
		else {
			if(record.get("PASSWORD") == null) {
				MSGUtil.sayError("密码不能为空!");
				return;
			}
		}
		if(record != null) {
			ArrayList<Object> obj = Util.getCheckResult(record, map);
			if(obj != null && obj.size() > 1) {
				String result = obj.get(0).toString();
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					doSave();
				}
				else {
					MSGUtil.sayError(obj.get(1).toString());
				}
			}
		}
	}
	
	private void doSave(){
		String keys ="ID,USER_ID,USER_NAME,PASSWORD,SALT,ACTIVE_FLAG,TEL,USERGRP_ID,EMAIL,DESCR,ROLE_ID,UDF1,UDF2,DEFAULT_ORG_ID";
		
		Util.db_async.saveUser(recordToList(keys), proName,passwd,salt, new AsyncCallback<String>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
//					Criteria crit = table.getCriteria();
//					table.invalidateCache();
//					if(crit == null) {
//						crit = new Criteria();
//					}
//					crit.addCriteria("OP_FLAG", "M");
//					table.fetchData(crit);
//					table.discardAllEdits();
//					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
					if(StaticRef.INS_FLAG.equals(op_flag)){
						ListGridRecord record = new ListGridRecord();
						record.setAttribute("ID", result.substring(2));
						form.setValue("ID", result.substring(2));
						Util.updateToRecord(form, table, record);   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
						ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
						list.add(0,record);
						table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); //wangjun
						table.redraw();
						table.selectRecord(record);	
					}else{
						Util.updateToRecord(form, table, table.getSelectedRecord());   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
						table.updateData(table.getSelectedRecord());
						table.redraw();
					}
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
					form.getItem("USER_ID").setDisabled(true);
					
					view.initSaveBtn();
				}
				else if(result.equals("02")){
					MSGUtil.sayError(Util.TI18N.USER_ID()+Util.MI18N.CHK_MUSTUNIQUE());
				}
				else{ 
					MSGUtil.sayError(result);
				}
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
		name.append("SAVE_USER(");
		int i=0;
		for(i=0;i<key.length;i++){
			String keyName = key[i];
			if(keyName.indexOf("_FLAG") >= 0){
				try {
					list.add(illegalFilter(getFlag(record.get(keyName))));
				}
				catch(Exception e) {
					list.add(getFlag(record.get(keyName)));
				}
			}else{
				try {
					list.add(illegalFilter(getValue(record.get(keyName))));
				}
				catch(Exception e) {
					list.add(getValue(record.get(keyName)));
				}
			}
			name.append("?,");
		}
		
		list.add(LoginCache.getLoginUser().getUSER_ID()); //当前用户
		list.add(op_flag); //当前用户默认组织机构ID
		name.append("?,?,?)"); //user_id,org_id,org_name,out_return_code
		//System.out.println(list.toString());
		
		proName = name.toString();
		return list;
	}
	
	private boolean isNull(boolean isOr, String... fieldNames){
		boolean isnull = false;
		if(fieldNames != null){
			for (String fieldName : fieldNames) {
				if(isOr && ObjUtil.isNotNull(record.get(fieldName))){
					return false;
				}else if(!(isOr || ObjUtil.isNotNull(record.get(fieldName)))){
					return true;
				}
			}
			return isOr;
		}
		return isnull;
	}
	
	private String getValue(Object obj){
		if(obj == null){
			return "";
		}else{
			return obj.toString();
		}
	}
	private String getFlag(Object obj){
		if(obj == null || obj.toString().equals("false") || obj.toString().equals("N")){
			return "N";
		}else{
			return "Y";
		}
	}
//	private boolean isValidPwd(String password) {
//		if(password.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$")){
//			return true;
//		}
//		return false;
//	}
	
    /**
     * 非法字符串过滤
     * @author Administrator
     * @param str
     * @return
     */
    private static String illegalFilter(String value) {
    	if(value != null) {
    		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    	 	value = value.replaceAll("\'", "").replaceAll("\"", "").replaceAll("#", "");
         	value = value.replaceAll("javascript", "").replaceAll("expression", "").replaceAll("alert", "");
    	}
         return value;
    }
}
