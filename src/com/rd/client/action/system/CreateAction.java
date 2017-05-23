package com.rd.client.action.system;

import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

/**
 * 系统管理->生成bean
 * @author yuanlei
 *
 */
public class CreateAction implements ClickHandler {



	public SGTable table;
	public SGPanel form;
	
	public CreateAction(SGPanel form, SGTable table) {
		this.form=form;
		this.table=table;
		
	}

	@Override
	public void onClick(ClickEvent event) {
		
			
	}
	

	
	
}
