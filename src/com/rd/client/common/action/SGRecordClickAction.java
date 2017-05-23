package com.rd.client.common.action;

import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class SGRecordClickAction implements RecordClickHandler {

	private SGForm view;
	
	public SGRecordClickAction(SGForm view){
		this.view = view;
	}
	@Override
	public void onRecordClick(RecordClickEvent event) {
		if(view.isModify){
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
//                    	doDelete();
                    }
                }
            });
		}
	}

}
