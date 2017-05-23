package com.rd.client.action.base.area;

import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.base.BasAreaView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * 行政区域 ---新增
 * @author fanglm
 *
 */
public class NewAreaAction implements ClickHandler {

	private SGTable table = null;
	private BasAreaView view = null;
	public NewAreaAction(SGTable p_table,BasAreaView view) {
		table = p_table;
		this.view = view;
	}

	@Override
	public void onClick(ClickEvent event) {
		if(view.selectNode != null){
			table.startEditingNew();
			table.OP_FLAG = "A";
		}else{
			MSGUtil.sayError("请先选择上级行政区域！");
			return;
		}
		view.initAddBtn();
	}
}
