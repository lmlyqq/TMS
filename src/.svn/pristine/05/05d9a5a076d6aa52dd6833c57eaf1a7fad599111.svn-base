package com.rd.client.common.action;

import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;

/**
 * 按钮状态置换
 * @author fanglm
 *
 */
public class SGDoubleClickAction implements DoubleClickHandler {
	
	private SGForm view;
	
	public SGDoubleClickAction(SGForm view){
		this.view = view;
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		view.enableOrDisables(view.add_map, false);//fanglm 2010-12-10
		view.enableOrDisables(view.del_map, false);
		view.enableOrDisables(view.save_map, true);
		
		view.isModify = true;

	}

}
