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
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;

/**
 * 客户管理--运输信息--订单类型--调用存储过程执行保存功能
 * @author fanglm
 *
 */
public class SaveCustOrdAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	
	private ListGrid table;
	private ListGrid fTable;
	private DynamicForm form;
	private Map<String, String> record;
	
	
	/**
	 * 构造
	 * @param table 订单类型列表
	 * @param form 订单类型form
	 * @param fTable 客户列表
	 */
	public SaveCustOrdAction(ListGrid table, DynamicForm form,ListGrid fTable) {
		this.table = table;
		this.form = form;
		this.fTable = fTable;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		click();
	}
	
	@Override
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
		String name = "SAVE_CUST_ORD(?,?,?,?,?,?,?)";
		if(fTable.getSelectedRecord() != null && record.size() > 0){
			if("A".equals(op_flag)){
				list.add("");
			}else{
				list.add(table.getSelectedRecord().getAttributeAsString("ID"));
			}
			list.add(fTable.getSelectedRecord().getAttributeAsString("ID"));
			list.add(getValue(record.get("ORD_NAME")));
			list.add(getFlag(record.get("DEFAULT_FLAG")));
			list.add(LoginCache.getLoginUser().getUSER_ID()); //当前用户
			list.add(op_flag);
	
			Util.async.execProcedure(list, name, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(result.equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.showOperSuccess();
						Criteria crit = table.getCriteria();
						table.invalidateCache();
						if(crit == null) {
							crit = new Criteria();
						}
						crit.addCriteria("OP_FLAG", "M");
						table.fetchData(crit);
						table.discardAllEdits();
						form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
					}
					else if(result.equals("02")){
						MSGUtil.sayError("订单类型已存在!");
						
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
