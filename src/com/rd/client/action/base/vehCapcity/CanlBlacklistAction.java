package com.rd.client.action.base.vehCapcity;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.base.BasVehCapacityView;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CanlBlacklistAction implements ClickHandler{
	
	private SGTable table;
	private ValuesManager vm;
	private BasVehCapacityView view;
	
	public CanlBlacklistAction(SGTable table,ValuesManager vm,BasVehCapacityView view){
		this.table=table;
		this.vm=vm;
		this.view = view;
	}

	@Override
	public void onClick(ClickEvent event) {
		if(table.getSelectedRecord() == null){
			return;
		}
		SC.confirm("确定要取消黑名单吗？", new BooleanCallback() {
			
			@Override
			public void execute(Boolean value) {
				if(value !=null&&value){
//					if(table.getSelectedRecord().getAttribute("BLACKLIST_FLAG")!="N"){
						ArrayList<String> sqlList = new ArrayList<String>();
						StringBuffer sf = new StringBuffer();
						sf.append("update BAS_VEHICLE set BLACKLIST_FLAG = 'N' where PLATE_NO=");
						sf.append("'"+table.getSelectedRecord().getAttribute("PLATE_NO")+"'");
						sqlList.add(sf.toString());
						Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
	
							@Override
							public void onFailure(Throwable caught) {
								MSGUtil.sayError(caught.getMessage());
							}
	
							@Override
							public void onSuccess(String result) {
								ListGridRecord rec = table.getSelectedRecord();
								rec.setAttribute("BLACKLIST_FLAG", false);
								vm.setValue("BLACKLIST_FLAG", false);
								table.redraw();
								MSGUtil.sayInfo("取消黑名单成功！");
								view.PullControl.enable();
								view.CanlControl.disable();
							}
							
						});
//					}else{
//						SC.say("这条记录未被拉入黑名单！");
//					}
					
				}
			}
		});
	}

}
