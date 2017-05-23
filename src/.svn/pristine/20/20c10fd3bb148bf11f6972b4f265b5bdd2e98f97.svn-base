package com.rd.client.action.settlement;

import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * 财务管理--结算管理--供应商费用结算--删除按钮
 * @author fanglm
 *
 */
public class DeleteSuplrFeeAction extends DeleteAction {

	private SGTable table;
	public DeleteSuplrFeeAction(SGTable pTable) {
		super(pTable);
		this.table = pTable;
		
	}
	@Override
	public void onClick(final ClickEvent event) {
		
		if (table.getSelectedRecord() == null)
			return;
		if("已对账".equals(table.getSelectedRecord().getAttributeAsString("ACCOUNT_STAT_NAME"))){
			SC.warn("费用已对账，不能删除！");
			return;
		}
		
		if(ObjUtil.isNotNull(table.getSelectedRecord())) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
					doDelete(event);
                }
            });
		}
		
	}
	
	private void  doDelete(ClickEvent event){
		super.onClick(event);
	}
}
