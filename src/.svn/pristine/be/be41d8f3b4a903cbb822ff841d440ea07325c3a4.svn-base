package com.rd.client.action.base.address;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.base.BasAddressView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SaveRDCAddrAction implements ClickHandler {

	private SGTable table;
	private SGTable rdctable;
	private HashMap<String, String> check_map2;
//	private BasAddressView view;
	private SGPanel form;

	public SaveRDCAddrAction(SGTable table, SGTable rdctable,SGPanel form,HashMap<String, String> check_map2, BasAddressView view) {
		this.table = table;
		this.rdctable = rdctable;
		this.check_map2 = check_map2;
//		this.view = view;
		this.form=form;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		ListGridRecord[] recs = table.getSelection();
		if (recs != null && recs.length == 0) {
			MSGUtil.sayError("请选择一个RDC");
			return;
		}
		ListGridRecord rec = recs[0];
//		if (!ObjUtil.ifNull(rec.getAttribute("AREA_ID"), "1").equals(ObjUtil.ifObjNull(form.getItem("AREA_ID").getValue(), "2").toString())) {
//			MSGUtil.sayError("RDC和分点部不在一个行政区域，请重新选择");
//			return;
//		}
		HashMap<String,String> map= new HashMap<String,String>();
		map.put("TMS_ADDR_CODE", ObjUtil.ifObjNull(form.getItem("AR_CODE").getValue(), "").toString());
		map.put("RDC_CODE", rec.getAttributeAsString("ID"));
		if(check_map2 != null){
			ArrayList<Object> obj = Util.getCheckResult(map, check_map2);
			if(obj != null && obj.size()>1){
				if(StaticRef.SUCCESS_CODE.equals(obj.get(0).toString())){
					checkUnique((HashMap<String,String>)obj.get(1),rec);
				}else{
					MSGUtil.sayError(obj.get(1).toString());
				}
			}
		}
	}
	
	private void checkUnique(HashMap<String,String> map,final ListGridRecord rec){
		Util.async.getCheckResult(Util.mapToJson(map), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if (!ObjUtil.isNotNull(result)) {
					doInsert(rec);
				}else{
					MSGUtil.sayError(result);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
		});
	}
	
	private void doInsert(final ListGridRecord rec){
		StringBuffer sf=new StringBuffer();
		sf.append("insert into RDC_ADDRESS(");
		sf.append("RDC_CODE,RDC_NAME,TMS_ADDR_CODE,TMS_ADDR_NAME,TMS_ADDR_TYPE,ADDTIME,ADDWHO");
		sf.append(")");
		sf.append(" values('"+rec.getAttributeAsString("ID")+"','"+rec.getAttribute("ADDR_NAME")+"','"+form.getItem("AR_CODE").getDisplayValue()+"'," +
				"'"+form.getItem("AR_NAME").getDisplayValue()+"','"+form.getItem("AR_TYP").getDisplayValue()+"',sysdate,'"+LoginCache.getLoginUser().getUSER_ID()+"')");
		Util.async.excuteSQL(sf.toString(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					rdctable.invalidateCache();
					Criteria cri = new Criteria();
					cri.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
					if(StaticRef.RDC.equals(rec.getAttribute("ADDR_TYP"))){
						cri.addCriteria("ISRDC","true");
	                    cri.addCriteria("RDC_ID",rec.getAttributeAsString("ID"));
					}else {
						cri.addCriteria("ISRDC","false");
	                	cri.addCriteria("TMS_ADDR_ID",rec.getAttributeAsString("ID"));
					}
					rdctable.fetchData(cri);
					form.clearValues();
				}else{
					MSGUtil.sayError(result);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

}
