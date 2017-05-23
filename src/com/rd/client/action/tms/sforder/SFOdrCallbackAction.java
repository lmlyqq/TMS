package com.rd.client.action.tms.sforder;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.SFOrderView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 运输管理--原始订单--退回操作
 * @author fanglm
 *
 */
public class SFOdrCallbackAction implements ClickHandler,com.smartgwt.client.widgets.events.ClickHandler{
	
	private ListGrid table;
	public StringBuffer msg;
	public SFOrderView view;
	
	private ListGridRecord[] list;
	public SFOdrCallbackAction(ListGrid table,SFOrderView view){
		this.table = table;
		this.view = view;
	}

	@Override
	public void onClick(MenuItemClickEvent event) {
		doSome();
	}

	@Override
	public void onClick(ClickEvent event) {
		doSome();
	}
	
	protected void doSome(){
list = table.getSelection();
		
		if (list.length == 0)
			return;
		
		StringBuffer sf = new StringBuffer();
		
		//存储过程名称
		String proName="SP_SFORDER_CALLBACK(?,?,?)"; //odr_no,user_id,output_result

		HashMap<String,String> odrList = new HashMap<String,String>();
		for(int i=0;i<list.length;i++){
			if(!StaticRef.SFODR_STATUS_CONFIRM.equals(list[i].getAttribute("STATUS"))){
				sf.append(list[i].getAttribute("ODR_NO"));
				sf.append(",");
			}
			else{
				odrList.put(Integer.toString(i+1), list[i].getAttribute("ODR_NO"));
			}
		}
		if(sf.toString().length() > 0){
			SC.warn("订单 "+sf.substring(0,sf.length()-1) +",非【" + StaticRef.SFODR_STATUS_CONFIRM_NAME + "】状态, 不能执行退回操作！");
			return;
		}
		
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		listmap.put("1", odrList);
		listmap.put("2", LoginCache.getLoginUser().getUSER_ID());
		String json = Util.mapToJson(listmap);
		Util.async.execProcedure(json, proName, new AsyncCallback<String>() {			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					
//					view.auditBtnEnable(false,false);
					
					table.invalidateCache();
					Criteria crit = table.getFilterEditorCriteria();
					if(crit == null) {
						crit = new Criteria();
					}
					crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
					table.fetchData(crit);
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
