package com.rd.client.action.tms;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class FeeWarnCalAction implements ClickHandler{
//	private FeeWarnView view;
	private SGTable table;
	private Record record;
	
	public FeeWarnCalAction(SGTable table){
//		this.view=view;
		this.table=table;
	}
	@Override
	public void onClick(ClickEvent event) {
		if(ObjUtil.isNotNull(table.getSelectedRecord())){
			record=table.getSelectedRecord();
			String user_id=LoginCache.getLoginUser().getUSER_ID();
			String DOC_TYP=record.getAttribute("DOC_TYPE");
			String proName="";
			String DOC_NO=record.getAttribute("DOC_NO");
			HashMap<String , Object> map=new HashMap<String  , Object>();
			if("ODR_NO".equalsIgnoreCase(DOC_TYP)){
				map.put("1", DOC_NO);
				map.put("2", user_id);
				proName="SP_REC_CALCUALTE(?,?,?)";
			}else if("SHPM_NO".equalsIgnoreCase(DOC_TYP)){
				map.put("1", " ");
				map.put("2", DOC_NO);
				map.put("3", user_id);
				map.put("4", " ");
				proName="SP_PAY_CALCUALTE(?,?,?,?,?)";
			}else if("LOAD_NO".equalsIgnoreCase(DOC_TYP)){
				map.put("1", DOC_NO);
				map.put("2", " ");
				map.put("3", user_id);
				map.put("4", " ");
				proName="SP_PAY_CALCUALTE(?,?,?,?,?)";
			}
			Util.async.execProcedure(Util.mapToJson(map), proName, new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
						MSGUtil.showOperSuccess();
						Criteria cri=table.getCriteria();
						if(cri == null){
							cri=new Criteria();
							cri.addCriteria("OP_FLAG",Util.TI18N.MODIFY_FLAG());
						}
						table.fetchData(cri);
					}else if(StaticRef.FAILURE_CODE.equals(result.substring(0,2)) && result.length()>2){
						MSGUtil.sayError(result.substring(2,result.length()));
					}else{
						MSGUtil.sayError(result);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			
		}else{
			MSGUtil.sayError("请选择一条单据");
		}
	}

}
