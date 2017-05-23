package com.rd.client.action.tms.changerecord;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.ChangeRecordView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConfirmChangeAction implements ClickHandler{
	
	private ValuesManager form;
	private ListGrid loadTable;
	private ChangeRecordView view;

	public ConfirmChangeAction(ListGrid loadTable,ValuesManager p_form,ChangeRecordView view) {
		this.loadTable = loadTable;
		this.form = p_form;  
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		HashMap<String,Object> map = new HashMap<String, Object>();
		HashMap<String,String> shpm_no = new HashMap<String,String>();
		if(view.shpmTable!=null && view.shpmTable.getRecords().length!=view.shpmTable.getSelection().length){
			ListGridRecord[] rec = view.shpmTable.getSelection();
			for(int i = 0 ; i <rec.length ; i++){
				shpm_no.put(String.valueOf(i+1), rec[i].getAttribute("SHPM_NO"));
			}
			map.put("1", view.shpmTable.getSelectedRecord().getAttribute("LOAD_NO"));
			map.put("2", shpm_no);
		}else{
			shpm_no.put("1", " ");
			map.put("1", loadTable.getSelectedRecord().getAttribute("LOAD_NO"));
			map.put("2",  shpm_no);
		}
		map.put("3", loadTable.getSelectedRecord().getAttribute("SUPLR_ID"));
		map.put("4", form.getItem("PLATE_NO1").getValue());
		map.put("5", form.getItem("VEHICLE_TYP_ID1").getValue());
		map.put("6", form.getItem("DRIVER1").getValue());
		map.put("7", form.getItem("MOBILE1").getValue());
		map.put("8", form.getItem("SUPLR_ID2").getValue());
		map.put("9", form.getItem("PLATE_NO2").getValue());
		map.put("10", form.getItem("VEHICLE_TYP_ID2").getValue());
		map.put("11", form.getItem("DRIVER2").getValue());
		map.put("12", form.getItem("MOBILE2").getValue());
		map.put("13", form.getItem("CARD_NO1").getValue());
		map.put("14", form.getItem("CARD_NO2").getValue());
		map.put("15", form.getItem("CARD_NO3").getValue());
		map.put("16", null);
		map.put("17", form.getItem("CHANGE_TIME").getValue());
		map.put("18", form.getItem("CHANGE_AREA_ID").getValue());
		map.put("19", form.getItem("CHANGE_REASON").getValue());
		map.put("20", form.getItem("TRACK_NOTES").getValue());
		map.put("21", LoginCache.getLoginUser().getUSER_ID());
		String json = Util.mapToJson(map);
		Util.async.execProcedure(json, "SP_LOAD_CHANGE_RECORD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
//					Criteria findValues = new Criteria();
					System.out.println(loadTable.getSelectedRecord().getAttribute("LOAD_NO"));
//					table.invalidateCache();
//		            findValues.addCriteria("LOAD_NO1", loadTable.getSelectedRecord().getAttribute("LOAD_NO").toString());
//		            findValues.addCriteria("OP_FLAG", "M");
//					table.fetchData(findValues);
//					if(view.shpmTable!=null && view.shpmTable.getRecords().length!=view.shpmTable.getSelection().length){
					if(view.shpmTable!=null){
						loadTable.collapseRecord(loadTable.getSelectedRecord());
					}
					loadTable.invalidateCache();
					Criteria cri = loadTable.getCriteria();
					cri.addCriteria("OP_FLAG", "M");
					loadTable.fetchData(cri);
//					}else{
//						loadTable.getSelectedRecord().setAttribute("PLATE_NO", form.getItem("PLATE_NO2").getValue());
//						loadTable.getSelectedRecord().setAttribute("VEHICLE_TYP_ID", form.getItem("VEHICLE_TYP_ID2").getValue());
//						loadTable.getSelectedRecord().setAttribute("DRIVER1", form.getItem("DRIVER2").getValue());
//						loadTable.getSelectedRecord().setAttribute("MOBILE1", form.getItem("MOBILE2").getValue());
//						loadTable.getSelectedRecord().setAttribute("SUPLR_ID", form.getItem("SUPLR_ID2").getValue());
//						loadTable.redraw();
//					}
					form.clearValues();
				}else{
					MSGUtil.sayError(result.substring(2));
				}
			}
			
		});
	}

}