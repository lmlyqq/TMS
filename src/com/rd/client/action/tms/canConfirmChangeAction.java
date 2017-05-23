package com.rd.client.action.tms;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;

public class canConfirmChangeAction implements ClickHandler{

	private ValuesManager form;
	private ListGrid loadTable;
	
	public canConfirmChangeAction(ListGrid loadTable,ValuesManager p_form) {
		this.loadTable = loadTable;
		this.form = p_form;  
	}
	
	@Override
	public void onClick(ClickEvent event) {
		SC.confirm("确定执行取消换车操作?", new BooleanCallback() {
			public void execute(Boolean value) {
                if (value != null && value) {
                	HashMap<String,Object> map = new HashMap<String, Object>();
            		map.put("1", loadTable.getSelectedRecord().getAttribute("LOAD_NO"));
            		map.put("2", LoginCache.getLoginUser().getUSER_ID());
            		String json = Util.mapToJson(map);
            		Util.async.execProcedure(json, "SP_LOAD_CANCEL_CHANGE(?,?,?)",new AsyncCallback<String>(){

            			@Override
            			public void onFailure(Throwable caught) {
            				MSGUtil.sayError(caught.getMessage());
            			}

            			@Override
            			public void onSuccess(String result) {
            				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
            					MSGUtil.showOperSuccess();
            					loadTable.invalidateCache();
            					Criteria cri = loadTable.getCriteria();
            					cri.addCriteria("OP_FLAG", "M");
            					loadTable.fetchData(cri);
            					form.clearValues();
            				}else{
            					MSGUtil.sayError(result.substring(2));
            				}
            			}
            			
            		});
                }
			}
		});
		
	}

}