package com.rd.client.action.tms;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.VehicleReportView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 运输管理--车辆车型上报
 * 上报
 * @author yuanlei
 *
 */
public class VehicleReportAction implements ClickHandler,com.smartgwt.client.widgets.events.ClickHandler{
	
	private ListGrid table;
	
	public StringBuffer msg;
	
	public VehicleReportView view;
	
	private ListGridRecord[] list;
	public VehicleReportAction(ListGrid table,VehicleReportView view){
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
	
	private void doSome() {
		list = table.getRecords();
		
		if (list.length == 0)
			return;
		
		StringBuffer msg = new StringBuffer();
		StringBuffer sf = new StringBuffer();
		final String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		ArrayList<String> sqlList = new ArrayList<String>();
		for(int i=0;i<list.length;i++){
			if(!StaticRef.UNREPORT.equals(list[i].getAttribute("REPORT_STATUS"))){
				msg.append(list[i].getAttribute("PLATE_NO"));
				msg.append(",");
			}
			else{
				sf = new StringBuffer();
				sf.append("update trans_veh_report set report_status = '");
				sf.append(StaticRef.REPORTED);
				sf.append("',reporter = '");
				sf.append(loginUser);
				sf.append("',report_time = sysdate");
				sf.append(" where id = '");
				sf.append(list[i].getAttribute("ID"));
				sf.append("'");
				sqlList.add(sf.toString());
			}
		}
		if(msg.toString().length() > 0){
			SC.warn("车辆【"+msg.substring(0,msg.length()-1) +"】已上报！");
			return;
		}
		
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					
					//刷新车型信息
					ListGridRecord[] records = table.getRecords();
					for(int i = 0; i < records.length; i++) {
						records[i].setAttribute("REPORT_STATUS_NAME", StaticRef.REPORTED_NAME);
						records[i].setAttribute("REPORTER", loginUser);
						records[i].setAttribute("REPORT_TIME", Util.getCurTime());
						table.updateData(records[i]);
					}
					table.redraw();
					
					view.setVehBtnStatus(false);
				}else{
					MSGUtil.sayWarning(result.substring(2));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});

	}

	

}
