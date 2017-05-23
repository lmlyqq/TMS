package com.rd.client.action.base.customer;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 客户管理--运输信息--运输服务--调用存储过程执行保存功能
 * @author fanglm
 *
 */
public class SaveCustSrvcAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	
	
	private ListGrid table;
	private ListGrid fTable;
	private boolean isCustomer = true;
	private String tableName="BAS_CUSTOMER_TRANS_SRVC";
	private int count=0;
	
	
	/**
	 * 构造
	 * @param table 订单类型列表
	 * @param form 订单类型form
	 * @param fTable 客户列表
	 */
	public SaveCustSrvcAction(ListGrid table, DynamicForm form,ListGrid fTable) {
		this.table = table;
//		this.form = form;
		this.fTable = fTable;
	}
	
	/**
	 * 构造
	 * @param table 订单类型列表
	 * @param form 订单类型form
	 * @param fTable 客户列表
	 * @patam isCustomer 区别客户运作机构保存和供应商运作机构保存
	 */
	public SaveCustSrvcAction(ListGrid table, DynamicForm form,ListGrid fTable,boolean isCustomer) {
		this.table = table;
//		this.form = form;
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
		
		count = 0;
		String login_user = LoginCache.getLoginUser().getUSER_ID(); //当前登录用户
		int[] edit_rows = table.getAllEditRows();//所有被修改列
		Map<String, Object> map;
		Record record;
		ArrayList<String> sqlList = new ArrayList<String>();//生成的sqlList
		StringBuffer sql;
		ListGridRecord[] gridRecord = table.getRecords();//所有数据
		for(int i=0; i<gridRecord.length; i++){
			if(gridRecord[i].getAttributeAsBoolean("DEFAULT_FLAG")){
				count = count+1;
			}
		}
		for(int i=0; i<edit_rows.length; i++){
			map = table.getEditValues(edit_rows[i]);
			record = table.getRecord(edit_rows[i]);
			sql = new StringBuffer();
			if(!isCustomer){
				tableName = "BAS_SUPLR_TRANS_SRVC";
			}
			
				if("Y".equals(getFlag(map.get("USE_FLAG"))) && !record.getAttributeAsBoolean("USE_FLAG")){
					if(isCustomer){
						sql.append("insert into bas_customer_trans_srvc(id,customer_id,trans_srvc_id,service_name,default_flag,addwho,addtime) values(");
					}else{
						sql.append("insert into bas_suplr_trans_srvc(id,suplr_id,trans_srvc_id,service_name,default_flag,addwho,addtime) values(");
					}
					sql.append("(select sys_guid() from dual),'");
					sql.append(fTable.getSelectedRecord().getAttributeAsString("ID"));
					sql.append("','");
					sql.append(record.getAttributeAsString("TRANS_SRVC_ID"));
					sql.append("','");
					sql.append(record.getAttributeAsString("SERVICE_NAME"));
					sql.append("','");
					sql.append(getFlag(map.get("DEFAULT_FLAG")));
					sql.append("','");
					sql.append(login_user);
					sql.append("',sysdate)");
					
					
				}else if(map.get("USE_FLAG") != null && "N".equals(getFlag(map.get("USE_FLAG"))) && record.getAttributeAsBoolean("USE_FLAG")){
					sql.append("delete from " + tableName +" where id='");
					sql.append(record.getAttributeAsString("ID"));
					sql.append("'");
				}else if(map.get("USE_FLAG") == null){
					sql.append("update " + tableName + " set DEFAULT_FLAG='");
					sql.append(getFlag(map.get("DEFAULT_FLAG")));
					sql.append("' where id='");
					sql.append(record.getAttributeAsString("ID"));
					sql.append("'");
				}
			
			sqlList.add(sql.toString());
			
			if("Y".equals(getFlag(map.get("DEFAULT_FLAG")))){
				count = count+1;
			}else if(map.get("DEFAULT_FLAG") != null){
				count = count-1;
			}
		}
		//System.out.println(sqlList);
		
		if(count == 0 ){
			MSGUtil.sayError(Util.MI18N.DEFAULT_SERV_NULL());
			return;
		}else if(count > 1){
			MSGUtil.sayError(Util.MI18N.DEFAULT_SERV_MUSTUNIQUE());
			return;
		}
		
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				MSGUtil.showOperSuccess();
				Criteria crit = table.getCriteria();
				table.invalidateCache();
				if(crit == null) {
					crit = new Criteria();
				}
				crit.addCriteria("OP_FLAG", "M");
				table.fetchData(crit);
				table.discardAllEdits();
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
		/**
		String op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
		//无选中记录，返回
		if(op_flag.equals("M") && table.getSelectedRecord() == null ){
			return;
		}
		record = form.getValues();
		record.remove("OP_FLAG");
		ArrayList<String> list= new ArrayList<String>();
		String name = "SAVE_CUST_TRANS_SRVC(?,?,?,?,?,?,?)";
		if(!isCustomer){
			name = "SAVE_SUPP_TRANS_SRVC(?,?,?,?,?,?,?)";
		}
		if(fTable.getSelectedRecord() != null && record.size() > 0){
			if("A".equals(op_flag)){
				list.add("");
			}else{
				list.add(table.getSelectedRecord().getAttributeAsString("ID"));
			}
			list.add(fTable.getSelectedRecord().getAttributeAsString("ID"));
			list.add(getValue(record.get("TRANS_SRVC_ID")));
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
						MSGUtil.sayError("运输服务已存在!");
						
					}
					else{ 
						MSGUtil.sayError(result);
					}
					System.out.println(result);
					
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
	**/
	private String getFlag(Object obj){
		if(obj == null || obj.toString().equals("false")){
			return "N";
		}else{
			return "Y";
		}
	}
}
