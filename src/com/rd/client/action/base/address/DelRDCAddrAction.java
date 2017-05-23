package com.rd.client.action.base.address;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.base.BasAddressView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DelRDCAddrAction implements ClickHandler{
	
	private SGTable table;
	private SGTable rdctable;
//	private BasAddressView view;
	
	public DelRDCAddrAction(SGTable table,SGTable rdctable,BasAddressView view){
		this.table = table;
		this.rdctable = rdctable;
//		this.view = view;
	}
	@Override
	public void onClick(ClickEvent event) {
		final ListGridRecord[] recs=rdctable.getSelection();
		if(recs != null && recs.length > 0){
			SC.confirm("是否删除指定记录", new BooleanCallback() {
				
				@Override
				public void execute(Boolean value) {
					if (value != null && value) {
						StringBuffer sf = new StringBuffer();
						sf.append("delete from RDC_ADDRESS");
						if (Boolean.valueOf(rdctable.getCriteria().getAttribute("ISRDC"))) {
							sf.append(" where RDC_CODE='"+table.getSelectedRecord().getAttributeAsString("ID")+"'");
							sf.append(" and TMS_ADDR_CODE='"+recs[0].getAttribute("TMS_ADDR_ID")+"'");
						}else {
							sf.append(" where TMS_ADDR_CODE='"+table.getSelectedRecord().getAttributeAsString("ID")+"'");
							sf.append(" and RDC_CODE='"+recs[0].getAttribute("TMS_ADDR_ID")+"'");
						}
						Util.async.excuteSQL(sf.toString(), new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								if (StaticRef.SUCCESS_CODE.equals(result.substring(0,2))) {
									MSGUtil.showOperSuccess();
									rdctable.invalidateCache();
									Criteria cri = new Criteria();
									cri.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
									if(StaticRef.RDC.equals(table.getSelectedRecord().getAttribute("ADDR_TYP"))){
										cri.addCriteria("ISRDC","true");
					                    cri.addCriteria("RDC_ID",table.getSelectedRecord().getAttributeAsString("ID"));
									}else {
										cri.addCriteria("ISRDC","false");
					                	cri.addCriteria("TMS_ADDR_ID",table.getSelectedRecord().getAttributeAsString("ID"));
									}
									rdctable.fetchData(cri);
									
//									rdctable.redraw();
								}
							}
							
							@Override
							public void onFailure(Throwable caught) {
								
							}
						});
					}
				}
			});
		}else {
			MSGUtil.sayError("请选择映射关系");
		}
	}
}