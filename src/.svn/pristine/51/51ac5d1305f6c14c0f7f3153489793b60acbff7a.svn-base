package com.rd.client.action.tms.order;

import com.rd.client.common.action.DeleteProAction;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * 运输管理--托运单管理--托运单删除
 * @author fanglm
 *
 */
public class DeleteTransOrderAction extends DeleteProAction {

//	private SGTable table;
	
	private ValuesManager vm;
	private OrderView view;
	
	public DeleteTransOrderAction(SGTable pTable,ValuesManager vm,OrderView view) {
		super(pTable,vm);
//		this.table = pTable;
		this.vm = vm;
		this.view = view;
		
	}
	@Override
	public void onClick(ClickEvent event) {
//		ListGridRecord record = table.getSelectedRecord();
		if(StaticRef.INS_FLAG.equals(vm.getValueAsString("OP_FLAG"))){
			MSGUtil.sayError("新增状态下，不允许删除订单！");
			return;
		}else if(!StaticRef.ORD_STATUS_CREATE.equals(vm.getValueAsString("STATUS"))){
			MSGUtil.sayError("订单非已创建状态，不能删除！");
			return;
		}
		super.onClick(event);
		
//		Criteria criteria = new Criteria();
//		criteria.addCriteria("OP_FLAG","M");
//		criteria.addCriteria("ODR_NO","SS");
//		view.groupTable.fetchData(criteria);
		view.groupTable.removeSelectedData();
	}

}
