package com.rd.client.action.base.customer;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;

/**
 * 客户管理--运输信息--执行机构--调用存储过程执行保存功能 isCustomer=true
 * 供应商管理--运输信息--执行机构--调用存储过程执行保存功能 isCustomer=false
 * @author fanglm
 *
 */
public class SaveCustOrgAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	
	
	
	private ListGrid table;
	private ListGrid fTable;
	private DynamicForm form;
	private Map<String, String> record;
	private boolean isCustomer=true;
	
	/**
	 * 构造
	 * @param table 执行结构列表
	 * @param form 执行机构form
	 * @param fTable 客户列表
	 */
	public SaveCustOrgAction(ListGrid table, DynamicForm form,ListGrid fTable) {
		this.table = table;
		this.form = form;
		this.fTable = fTable;
	}
	
	/**
	 * 构造
	 * @param table 执行结构列表
	 * @param form 执行机构form
	 * @param fTable 客户列表
	 * @param isCustomer true 针对客户，false 针对供应商
	 */
	public SaveCustOrgAction(ListGrid table, DynamicForm form,ListGrid fTable,boolean isCustomer) {
		this.table = table;
		this.form = form;
		this.fTable = fTable;
		this.isCustomer = isCustomer;
	}
	@Override
	public void onClick(ClickEvent event) {
		click();
	}
	
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		// TODO Auto-generated method stub
		click();
	}
	
	@SuppressWarnings("unchecked")
	private void click() {
		String op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
		//无选中记录，返回
		if(op_flag.equals("M") && table.getSelectedRecord() == null ){
			return;
		}
		record = form.getValues();
		record.remove("OP_FLAG");
		ArrayList<String> list= new ArrayList<String>();
		
		String name = "SAVE_CUST_ORG(?,?,?,?,?,?,?,?)"; //客户执行机构保存
		if(!isCustomer){
			name = "SAVE_SUPP_ORG(?,?,?,?,?,?,?,?)";//供应商执行机构保存
		}
		if(fTable.getSelectedRecord() != null && record.size() > 0){
			if("A".equals(op_flag)){
				list.add("");
			}else{
				list.add(table.getSelectedRecord().getAttributeAsString("ID"));
			}
			list.add(fTable.getSelectedRecord().getAttributeAsString("ID"));
			list.add(getValue(record.get("ORG_ID"))); //机构名称空间隐藏ORG_ID
			list.add(getFlag(record.get("C_ORG_FLAG")));//包含下级机构
			list.add(getFlag(record.get("DEFAULT_FLAG")));
			list.add(op_flag);
			list.add(LoginCache.getLoginUser().getUSER_ID()); //当前用户
	
			Util.async.execProcedure(list, name, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(result.equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.showOperSuccess();
						Criteria crit = table.getCriteria();
						table.invalidateCache();
						if(crit == null) {
							crit = new Criteria();
							if(!isCustomer){
								crit.addCriteria("SUPLR_ID",fTable.getSelectedRecord().getAttributeAsString("ID"));
							}
							else {
								crit.addCriteria("CUSTOMER_ID",fTable.getSelectedRecord().getAttributeAsString("ID"));
							}
						}
						crit.addCriteria("OP_FLAG", "M");
						table.fetchData(crit);
						table.discardAllEdits();
						form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
					}
					else if(result.equals("02")){
						MSGUtil.sayError("执行机构已存在!");
						
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
