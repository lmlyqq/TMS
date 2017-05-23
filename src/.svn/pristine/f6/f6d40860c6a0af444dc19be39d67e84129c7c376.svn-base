package com.rd.client.view.settlement;





import java.util.ArrayList;
import java.util.List;

import com.rd.client.action.settlement.settle.ImpSettToInvoAction;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.BillSettleInfoTDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 应付费用录入窗口
 * @author fangliangmeng
 *
 */
public class SettleItemWin extends Window {
	
	private int width = 635;
	private int height = 300;
	private String top = "30%";
	private String left = "25%";
	private String title = "添加结算单明细";
	public Window window;
	private DataSource ds;
	
	public SGTable addrList;
	private ListGrid table;
	private InvoiceView view;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */
	
	public static interface CloseHandler{
		void onClosed(String message);
	}
	private List<CloseHandler> closeHandlers=new ArrayList<CloseHandler>();
	public void addCloseHandler(CloseHandler handler){
		closeHandlers.add(handler);
	}
	public void removeHandler(CloseHandler handler){
		closeHandlers.remove(handler);
	}
	public void fireCloseEvent(String message){
		for(CloseHandler handler:closeHandlers)
			handler.onClosed(message);
	}

	
	public SettleItemWin(ListGrid table,InvoiceView view){
		this.table = table;
		this.view = view;
	}
	
	public Window getViewPanel() {
		
		VLayout lay = new VLayout();
		ds = BillSettleInfoTDS.getInstance("V_BILL_SETTLE_INFO_","BILL_SETTLE_INFO");
		
		final SGText SETT_NO = new SGText("SETT_NO", Util.TI18N.SETT_NO());
		
		final SGDate ODR_TIME_FROM = new SGDate("SETT_TIME_FROM", Util.TI18N.ODR_TIME_FROM());
		
		final SGDate ODR_TIME_TO = new SGDate("SETT_TIME_TO", "到");
		
		SGButtonItem searchBtn = new SGButtonItem(StaticRef.FETCH_BTN);
		searchBtn.setAlign(Alignment.RIGHT);
		searchBtn.setColSpan(2);
		
		SGButtonItem confirmBtn = new SGButtonItem(StaticRef.CONFIRM_BTN);
		confirmBtn.setAlign(Alignment.RIGHT);
		confirmBtn.addClickHandler(new ImpSettToInvoAction(view,this));
		
		final SGPanel searchPanel = new SGPanel();
		searchPanel.setTitleWidth(100);
		searchPanel.setItems(SETT_NO,ODR_TIME_FROM,ODR_TIME_TO,searchBtn,confirmBtn);
		
		searchBtn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
						
						Criteria criteria = new Criteria();
						criteria.addCriteria("OP_FLAG","M");
						
						criteria.addCriteria("AUDIT_STAT","20");
						criteria.addCriteria("SETT_NAME",table.getSelectedRecord().getAttribute("SETT_NAME"));
					
						criteria.addCriteria(searchPanel.getValuesAsCriteria());

		        		addrList.fetchData(criteria,new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								if(addrList.getRecordList().getLength() > 0){
									addrList.selectRecord(0);
								}
								
							}
						});  
					
				}
			});
		
		lay.addMember(searchPanel);
		lay.addMember(createListTable());
        
		
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(lay);
		window.setShowCloseButton(true);
		window.show();
		
		return window;
	}
	
	private SGTable createListTable(){
		addrList = new SGTable(ds, "100%", "60%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		ListGridField DOC_NO = new ListGridField("SETT_NO", Util.TI18N.SETT_NO(), 100);
		ListGridField SETT_ORG_ID_NAME = new ListGridField("SETT_ORG_ID_NAME", "结算机构", 100);
		ListGridField SETT_NAME = new ListGridField("SETT_NAME", Util.TI18N.SETT_NAME(), 100);
		ListGridField PRE_FEE = new ListGridField("SETT_CASH", Util.TI18N.SETT_CASH(), 100);
		ListGridField AUDIT_STAT = new ListGridField("AUDIT_STAT_NAME", Util.TI18N.AUDIT_STAT(), 100);
		Util.initFloatListField(PRE_FEE, StaticRef.PRICE_FLOAT);
		
		addrList.setFields(DOC_NO,SETT_ORG_ID_NAME,SETT_NAME,PRE_FEE,AUDIT_STAT);
		
		return addrList;
	}
	
}
