package com.rd.client.action.tms;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class DeleteOrderItemAction implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler{

	private ListGrid table;
	private ValuesManager vm;
	private DataSource ds;
	private ListGridRecord[] records = null;
	private DynamicForm  form;
	private int row=0;
	private SGTable c_table;
	private OrderView view;
	private String odr_no;
	
	public DeleteOrderItemAction(SGTable p_table,OrderView view) {
		table = p_table;
		this.view = view;
	}

	@Override
	public void onClick(ClickEvent event) {
		String rowNum = JSOHelper.getAttribute(table.getJsObj(), "selectedRowNum");
		ds= table.getDataSource();
		records = table.getSelection();
		
		if(records  != null && records.length > 0) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	doDelete();
                    }
                }
            });
		}else if(ObjUtil.isNotNull(rowNum)){
			table.discardAllEdits(new int[]{Integer.parseInt(rowNum)}, false);
			table.setProperty("selectedRowNum", "");
		}
		else{
			MSGUtil.sayError("请先勾选待删除记录！");
			return;
		}
	}
	
	private void doDelete() {
		//String tableName = ds.getAttribute("tableName");
		String tableName = ds.getTableName();
		String id = table.getSelectedRecord().getAttributeAsString("ID");
		row = table.getRecordIndex(table.getSelectedRecord());
		odr_no = view.table.getSelectedRecord().getAttribute("ODR_NO");
		if(id == null)
			return;
		
    	Util.db_async.deletePro(id, tableName,LoginCache.getLoginUser().getUSER_ID(), new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					if(vm != null){
						vm.clearValues();
					}else if(form != null){
						form.clearValues();
					}
					RefreshRecord();
					
					table.redraw();
					
					ArrayList<String> sqlList = new ArrayList<String>();
					StringBuffer sf = new StringBuffer();
					sf.append("UPDATE TRANS_ORDER_HEADER SET (LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2," +
							"LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE)= ( select LOAD_ID,LOAD_NAME,LOAD_AREA_ID," +
							"LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE " +
							" from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = " +
							"(select min(odr_row) from trans_order_item where ODR_NO = '"+odr_no+"')) where TRANS_ORDER_HEADER.ODR_NO = '"+odr_no+"'");
					sqlList.add(sf.toString());
					StringBuffer sf1 = new StringBuffer();
					sf1.append("UPDATE TRANS_ORDER_HEADER SET (UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2," +
							"UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE)= ( select UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID," +
							"UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE " +
							" from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = " +
							"(select max(odr_row) from trans_order_item where ODR_NO = '"+odr_no+"')) where TRANS_ORDER_HEADER.ODR_NO = '"+odr_no+"'");
					sqlList.add(sf1.toString());
					
					Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
								if(table.getRecords().length==1){
									view.table.getSelectedRecord().setAttribute("LOAD_NAME", table.getRecord(0).getAttribute("LOAD_NAME"));
									view.table.getSelectedRecord().setAttribute("LOAD_AREA_NAME2", table.getRecord(0).getAttribute("LOAD_AREA_NAME2"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_NAME", table.getRecord(0).getAttribute("UNLOAD_NAME"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_ADDRESS", table.getRecord(0).getAttribute("UNLOAD_ADDRESS"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_AREA_NAME2", table.getRecord(0).getAttribute("UNLOAD_AREA_NAME2"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_TEL", table.getRecord(0).getAttribute("UNLOAD_TEL"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_CONTACT", table.getRecord(0).getAttribute("UNLOAD_CONTACT"));
								}else if(table.getRecords().length==0){
									view.table.getSelectedRecord().setAttribute("LOAD_NAME", "");
									view.table.getSelectedRecord().setAttribute("LOAD_AREA_NAME2", "");
									view.table.getSelectedRecord().setAttribute("UNLOAD_NAME", "");
									view.table.getSelectedRecord().setAttribute("UNLOAD_ADDRESS", "");
									view.table.getSelectedRecord().setAttribute("UNLOAD_AREA_NAME2", "");
									view.table.getSelectedRecord().setAttribute("UNLOAD_TEL", "");
									view.table.getSelectedRecord().setAttribute("UNLOAD_CONTACT", "");
								}else{
									view.table.getSelectedRecord().setAttribute("LOAD_NAME", table.getRecord(0).getAttribute("LOAD_NAME"));
									view.table.getSelectedRecord().setAttribute("LOAD_AREA_NAME2", table.getRecord(0).getAttribute("LOAD_AREA_NAME2"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_NAME", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_NAME"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_ADDRESS", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_ADDRESS"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_AREA_NAME2", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_AREA_NAME2"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_TEL", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_TEL"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_CONTACT", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_CONTACT"));
								}
								view.table.redraw();
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}

	private void RefreshRecord() {
		RecordList list = table.getRecordList();
		RecordList newList = new  RecordList();
		for(int i = 0; i < list.getLength(); i++) {
            if(!list.get(i).getAttributeAsString("ID").equals(table.getSelectedRecord().getAttributeAsString("ID")))
            	newList.add(list.get(i));
		}
		
		table.setData(newList);
		if(newList.getLength() > 0){
			if(row >0 )
				table.selectRecord(row - 1);
			else
				table.selectRecord(0);
		}else{
			if(vm != null)
				vm.clearValues();
			if(form != null)
				form.clearValues();
		}
		
		//fanglm 2011-2-25
		if(c_table != null){
			c_table.setData(new RecordList());
		}
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		String rowNum = JSOHelper.getAttribute(table.getJsObj(), "selectedRowNum");
		ds= table.getDataSource();
		records = table.getSelection();
		if(records  != null && records.length > 0) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	doDelete();
                    }
                }
            });
		}else if(ObjUtil.isNotNull(rowNum)){
			table.discardAllEdits(new int[]{Integer.parseInt(rowNum)}, false);
			table.setProperty("selectedRowNum", "");
		}
		else{
			MSGUtil.sayError("请先选择待删除记录！");
			return;
		}
	}
	
}
