package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.List;

import com.rd.client.action.settlement.settle.ImpFeeToSettleAction;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.BillAllRecordDS;
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
public class FeeItemWin extends Window {
	
	private int width = 635;
	private int height = 300;
	private String top = "30%";
	private String left = "25%";
	private String title = "上引费用明细";
	public Window window;
	private DataSource ds;
	
	public SGTable addrList;
	private ListGrid table;
	private SettlementView view;
	private SettlementRecView rview;
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

	
	public FeeItemWin(ListGrid table,SettlementView view){
		this.table = table;
		this.view = view;
	}
	
	public FeeItemWin(ListGrid table,SettlementRecView view){
		this.table = table;
		this.rview = view;
	}
	
	public Window getViewPanel() {
		
		VLayout lay = new VLayout();
		ds = BillAllRecordDS.getInstance("V_BILL_ALL","BILL_DETAIL_ALL");
		
		final SGText DOC_NO = new SGText("DOC_NO", Util.TI18N.DOC_NO());
		
		final SGDate ODR_TIME_FROM = new SGDate("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM());
		
		final SGDate ODR_TIME_TO = new SGDate("ODR_TIME_TO", "到");
		
		SGButtonItem searchBtn = new SGButtonItem(StaticRef.FETCH_BTN);
		searchBtn.setAlign(Alignment.RIGHT);
		searchBtn.setColSpan(2);
		
		SGButtonItem confirmBtn = new SGButtonItem(StaticRef.CONFIRM_BTN);
		confirmBtn.setAlign(Alignment.RIGHT);
		if(ObjUtil.isNotNull(view)){
			confirmBtn.addClickHandler(new ImpFeeToSettleAction(view,this));
		}else{
			confirmBtn.addClickHandler(new ImpFeeToSettleAction(rview,this));
		}
		
		final SGPanel searchPanel = new SGPanel();
		searchPanel.setTitleWidth(100);
		searchPanel.setItems(DOC_NO,ODR_TIME_FROM,ODR_TIME_TO,searchBtn,confirmBtn);
		
		searchBtn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
						
						Criteria criteria = new Criteria();
						criteria.addCriteria("OP_FLAG","M");
						
						criteria.addCriteria("SETT_TYPE",table.getSelectedRecord().getAttribute("SETT_TYPE"));
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
		
		ListGridField DOC_NO = new ListGridField("DOC_NO", Util.TI18N.DOC_NO(), 100);
		ListGridField FEE_TYP = new ListGridField("SETT_TYPE", Util.TI18N.SETT_TYP(), 80);
		ListGridField FEE_NAME = new ListGridField("FEE_NAME",Util.TI18N.FEE_NAME(), 80);
		ListGridField PRE_FEE = new ListGridField("CONT_FEE", Util.TI18N.SETT_CONT_CASH(), 80);
		Util.initFloatListField(PRE_FEE, StaticRef.PRICE_FLOAT);
		ListGridField BAS_VAL = new ListGridField("BAS_VALUE", Util.TI18N.BAS_VALUE(), 80);
		Util.initFloatListField(BAS_VAL, StaticRef.VOL_FLOAT);
		ListGridField PRICE = new ListGridField("PRICE", Util.TI18N.PRICE(), 80);
		Util.initFloatListField(PRICE, StaticRef.PRICE_FLOAT);
		ListGridField MILE = new ListGridField("MILE", Util.TI18N.MILE(), 80);
		Util.initFloatListField(MILE, StaticRef.QNTY_FLOAT);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE", Util.TI18N.SETT_CASH(), 80);
		Util.initFloatListField(DUE_FEE, StaticRef.PRICE_FLOAT);
//		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME", Util.TI18N.CUSTOMER_NAME(), 80);
//		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_NAME(), 80);
		
		addrList.setFields(DOC_NO,FEE_TYP,FEE_NAME,PRE_FEE,BAS_VAL,PRICE,DUE_FEE);
		
		return addrList;
	}
	
}
