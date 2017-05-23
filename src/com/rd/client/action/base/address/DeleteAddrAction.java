package com.rd.client.action.base.address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DeleteAddrAction implements ClickHandler{


	private ValuesManager form;
	private ListGrid table;
	private ListGridRecord[] records;
//	private HashMap<String, String> map;
	public DeleteAddrAction(ListGrid p_table,ValuesManager p_form) {
		table = p_table;
		form = p_form;
//		this.map = null;
	}
	
	public DeleteAddrAction(ListGrid p_table, ValuesManager p_form,HashMap<String, String> map) {
		table = p_table;
		form = p_form;
//		this.map = map;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		records = table.getSelection();
		if(records  != null && records.length > 0) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	doDelete();
                    }
                }
            });
		}
	}
	
	private void doDelete() {
    	ArrayList<String> sqlList = new ArrayList<String>();
    	ArrayList<String> descrList = new ArrayList<String>();//仅用作写入用户登录日志
    	String descr = "";                     //仅用作写入用户登录日志
    	StringBuffer sf = null;
    	//String tableName = table.getDataSource().getAttribute("tableName");
    	//String tableName2 = table2.getDataSource().getAttribute("tableName");
    	String tableName = table.getDataSource().getTableName();
    	String tableName3 = "RDC_ADDRESS";
    	for(int i = 0; i < records.length; i++) {
    		ListGridRecord rec = records[i]; 
    		sf = new StringBuffer();
    		sf.append("delete from ");
    		sf.append(tableName);
    		sf.append(" where ");
    		sf.append("ADDR_CODE='"+rec.getAttribute("ADDR_CODE")+"' and ADDR_NAME='"+rec.getAttribute("ADDR_NAME")+"'");
    		sqlList.add(sf.toString());
    		
    		descr = StaticRef.ACT_DELETE + Util.getPropTitle(tableName)[0] + "【" + rec.getAttribute(Util.getPropField(tableName)[0]) + "】";
    		descrList.add(descr);
    		
    		//顺丰网点信息表删除
//    		sf=new StringBuffer();
//        	sf.append("delete from ");
//        	sf.append(tableName2);
//        	sf.append(" where ");
//        	if(vFlag.equals("SF")){
//        		sf.append("SF_ADDR_CODE='"+rec.getAttribute("ADDR_CODE")+"'");
//        	}else if (vFlag.equals("TMS")){
//        		sf.append("TMS_ADDR_CODE='"+rec.getAttribute("ADDR_CODE")+"'");
//        	}
//        	sqlList.add(sf.toString());
        		
//        	descr = StaticRef.ACT_DELETE + Util.getPropTitle(tableName2)[0] + "【" + rec.getAttribute(Util.getPropField(tableName2)[0]) + "】";
//        	descrList.add(descr);
        	
        	//RDC-冷运信息表删除
        	sf = new StringBuffer();
        	sf.append("delete from ");
        	sf.append(tableName3);
        	sf.append(" where ");
        	if (StaticRef.RDC.equals(rec.getAttribute("ADDR_TYP"))) {
				sf.append("RDC_CODE = '"+rec.getAttributeAsString("ID")+"'");
			}else {
				sf.append("TMS_ADDR_CODE = '"+rec.getAttributeAsString("ID")+"'");
			}
        	sqlList.add(sf.toString());
        	
        	descr = StaticRef.ACT_DELETE + Util.getPropTitle(tableName3)[0] + "【" + rec.getAttribute(Util.getPropField(tableName3)[0]) + "】";
        	descrList.add(descr);
    	}
    	
    	Util.async.doDelete(descrList, sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					form.clearValues();
					//Criteria criteria = table.getFilterEditorCriteria();
					RefreshRecord();
					/*table.invalidateCache();
					if(criteria == null) {
						criteria = new Criteria();
					}
					criteria.addCriteria("OP_FLAG", "M");
					table.filterData(criteria);
					//table.removeData(table.getSelectedRecord());
					table.redraw();*/
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void RefreshRecord() {
		ListGridRecord[] lstRecords = table.getRecords();
		ArrayList<ListGridRecord> list = new ArrayList(Arrays.asList(lstRecords));
		int index = 0;
		if(records != null && records.length > 0) {
			for(int i = 0; i < records.length; i++) {
				int pos = table.getRecordIndex(records[i]);
				list.remove(pos-i);
				if(i == 0) {
					index = pos;
				}
			}
		}
		table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); //wangjun
		table.redraw();
		if(table.getRecords().length > 0) {
			if (index > 0) 
				table.selectRecord(index - 1);
			else 
				table.selectRecord(0);
		}else{
			form.clearValues();
		}
	}

}
