package com.rd.client.common.action;

import com.rd.client.common.util.MSGUtil;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 列表右键选择同一XXX
 * @author fanglm
 *
 */
public class SameChoseAction implements ClickHandler {
	
	private ListGrid table;
	private String num;
	
	public SameChoseAction(ListGrid table,String num){
		this.table = table;
		this.num = num;
	}
	@Override
	public void onClick(MenuItemClickEvent event) {
		
		ListGridRecord record = table.getSelectedRecord();
		table.deselectRecords(table.getSelection());
		if(record != null){
			String id = record.getAttribute(num);
			RecordList list = table.getRecordList();
			for(int i = 0;i<list.getLength();i++){
				if(id.equals(list.get(i).getAttribute(num))){
					table.selectRecord(i);
				}
			}
		}else{
			MSGUtil.sayWarning("必须选择一条记录!");
			return;
		}
	}

}
