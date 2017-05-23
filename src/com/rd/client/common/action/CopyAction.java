package com.rd.client.common.action;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.view.settlement.RateManagerRecView;
import com.rd.client.view.settlement.RateManagerView;
import com.rd.client.view.settlement.ReceRuleView;
import com.rd.client.view.settlement.StandRuleView;
import com.rd.client.view.settlement.SuplrRateManagerView;
import com.rd.client.view.settlement.SuplrRuleView;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 复制新增
 * @author fangliangmeng
 *  2012-06-18 14:51
 */
public class CopyAction implements ClickHandler{
	
	private DynamicForm form;
	private SGForm view;

	
	public CopyAction(DynamicForm form,SGForm view){
		this.form = form;
		this.view = view;
	}
	@Override
	public void onClick(ClickEvent event) {
		if(view instanceof RateManagerView){
			RateManagerView rView =(RateManagerView)view;
			rView.initAddRuleBtn();
			rView.vm.setValue("OP_FLAG", StaticRef.INS_FLAG);
			rView.vm.removeMember("EDITTIME");
			rView.vm.removeMember("EDITWHO");
		}
		else if(view instanceof SuplrRateManagerView){
			SuplrRateManagerView rView =(SuplrRateManagerView)view;
			rView.initAddRuleBtn();
			rView.vm.setValue("OP_FLAG", StaticRef.INS_FLAG);
			rView.vm.removeMember("EDITTIME");
			rView.vm.removeMember("EDITWHO");
		}
		else if(view instanceof RateManagerRecView){
			RateManagerRecView rView =(RateManagerRecView)view;
			rView.initAddRuleBtn();
			rView.vm.setValue("OP_FLAG", StaticRef.INS_FLAG);
			rView.vm.removeMember("EDITTIME");
			rView.vm.removeMember("EDITWHO");
		}
		else if(view instanceof StandRuleView){
			StandRuleView rView =(StandRuleView)view;
			rView.initAddRuleBtn();
			rView.vm.setValue("OP_FLAG", StaticRef.INS_FLAG);
			rView.vm.removeMember("EDITTIME");
			rView.vm.removeMember("EDITWHO");
		}
		else if(view instanceof SuplrRuleView){
			SuplrRuleView rView =(SuplrRuleView)view;
			rView.initAddRuleBtn();
			rView.vm.setValue("OP_FLAG", StaticRef.INS_FLAG);
			rView.vm.removeMember("EDITTIME");
			rView.vm.removeMember("EDITWHO");
		}
		else if(view instanceof ReceRuleView){
			ReceRuleView rView =(ReceRuleView)view;
			rView.initAddRuleBtn();
			rView.vm.setValue("OP_FLAG", StaticRef.INS_FLAG);
			rView.vm.removeMember("EDITTIME");
			rView.vm.removeMember("EDITWHO");
		}
		else if(view instanceof OrderView){
			final OrderView oView = (OrderView)view;
			oView.initAddBtn();
			oView.vm.setValue("OP_FLAG", StaticRef.INS_FLAG);
			ListGridRecord[] records = oView.groupTable.getRecords();
			for(int i = 0; i < records.length; i++) {
				oView.groupTable.removeData(records[i]);
			}
			oView.groupTable.redraw();
			oView.groupTable.setData(records);
			
			Util.db_async.getIdSeq("ORDER", new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					//生成订单编号
					oView.vm.setValue("ODR_NO", result);
					ListGridRecord lists[] = oView.groupTable.getRecords();
					for(int j=0;j<lists.length;j++){
						lists[j].setAttribute("ID", "");
					}
					
				}
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
		}else{
			view.initAddBtn();
			form.setValue("OP_FLAG", StaticRef.INS_FLAG);
		}
		
	}

}
