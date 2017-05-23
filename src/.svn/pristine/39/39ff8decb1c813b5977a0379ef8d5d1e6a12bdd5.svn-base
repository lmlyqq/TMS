package com.rd.client.action.system;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.system.UserView;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

/**
 * 系统管理-用户管理--客户页签-保存按钮
 * @author fanglm
 *
 */
public class SaveUserCustAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	
	
	private ListGrid list;
	private ListGrid fTable;
	private String user_id;
	private UserView view;

	public SaveUserCustAction(ListGrid list,ListGrid fTable){
		this.list = list;
		this.fTable = fTable;
	}
	
	public SaveUserCustAction(ListGrid list,ListGrid fTable , UserView view){
		this.list = list;
		this.fTable = fTable;
		this.view = view;
	}
	
	public void onClick(ClickEvent event) {
		click();
	}
	
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		// TODO Auto-generated method stub
		click();
	}
	
	private void click() {
		if(fTable.getSelectedRecord() == null){
			MSGUtil.sayError("保存失败，请选择用户");
			return;
		}
		if (list.getRecords().length < 1) {
			MSGUtil.sayError("列表中没有客户记录，请重新查询");
			return;
		}
		StringBuffer sf = new StringBuffer();
		ArrayList<String> sqlList = new ArrayList<String>();
		user_id = fTable.getSelectedRecord().getAttribute("USER_ID");
		sf.append("delete from sys_user_customer t1 where user_id='");
		sf.append(user_id);
		sf.append("'");
		String customer_code = view.cuscode;
		String customer_name = view.cusname;
		if (ObjUtil.isNotNull(customer_code) || ObjUtil.isNotNull(customer_name)) {
			sf.append(" and exists (select 'x' from bas_customer t2 where t1.customer_id = t2.id and t2.customer_code like '%"+ObjUtil.ifNull(customer_code, "")+"%'" +
					" and t2.customer_cname like '%"+ObjUtil.ifNull(customer_name, "")+"%')");
		}
		sqlList.add(sf.toString());
		
		ListGridRecord[] recs = list.getSelection();
		if (recs != null && recs.length > 0) {
			for (ListGridRecord rec : recs) {
				sf = new StringBuffer();
				sf.append("insert into sys_user_customer(id,user_id,customer_id)");
				sf.append(" values(sys_guid(),'"+user_id+"','"+rec.getAttribute("CUSTOMER_ID")+"')");
				sqlList.add(sf.toString());
			}
		}
		
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
					list.discardAllEdits();
					list.invalidateCache();
					SelectionEvent.fire(fTable, fTable.getConfig());
					
				}else{
					MSGUtil.showOperError();
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
