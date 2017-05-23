package com.rd.client.action.tms.sforder;

import com.rd.client.common.action.DeleteProAction;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.SFOrderView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * 运输管理--原始订单--订单删除
 * @author fanglm
 *
 */
public class DeleteSFOrderAction extends DeleteProAction {
	
	private ValuesManager vm;
	private SFOrderView view;
	
	public DeleteSFOrderAction(SGTable pTable,ValuesManager vm,SFOrderView view) {
		super(pTable,vm);
		this.vm = vm;
		this.view = view;
		
	}
	@Override
	public void onClick(ClickEvent event) {
		if(StaticRef.INS_FLAG.equals(vm.getValueAsString("OP_FLAG"))){
			MSGUtil.sayError("新增状态下，不允许删除订单！");
			return;
		}else if(!StaticRef.SFODR_STATUS_CREATE.equals(vm.getValueAsString("STATUS"))){
			MSGUtil.sayError("订单非" + StaticRef.SFODR_STATUS_CREATE_NAME + "状态，不能删除！");
			return;
		}
		super.onClick(event);
		view.groupTable.removeSelectedData();
	}

}
