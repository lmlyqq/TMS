package com.rd.client.common.action;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
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

/**
 * 调用存储过程DEL_PRO执行删除操作
 * @author fanglm
 *
 */
public class DeleteProAction implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler {

	private ListGrid table;
	private ValuesManager vm;
	private DataSource ds;
	private ListGridRecord[] records = null;
	private DynamicForm  form;
	private int row=0;
	private SGTable c_table;
	public DeleteProAction(ListGrid p_table,ValuesManager vm) {
		table = p_table;
		this.vm = vm;
	}
	
	public DeleteProAction(SGTable p_table,DynamicForm form) {
		table = p_table;
		this.form = form;
	}
	public DeleteProAction(SGTable p_table) {
		table = p_table;
	}
	
	public DeleteProAction(SGTable p_table,SGTable c_table){
		table = p_table;
		this.c_table = c_table;
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
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
//	private void RefreshRecord() {
//         table.removeData(table.getSelectedRecord());
//         table.deselectAllRecords();
//	}
	
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
