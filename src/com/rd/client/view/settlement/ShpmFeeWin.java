package com.rd.client.view.settlement;





import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.action.settlement.DeletePayShpmAction;
import com.rd.client.action.settlement.SavePayShpmAction;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.BillShpmFeeDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 应付费用录入窗口
 * @author fangliangmeng
 *
 */
public class ShpmFeeWin extends Window {
	
	private int width = 600;
	private int height = 300;
	private String top = "30%";
	private String left = "30%";
	private String title = "应收费用录入";
	public Window window;
	private DataSource ds;
	private ListGrid table;
	
	private SGTable addrList;
	private ToolStrip toolStrip;
	private SuplrFeeSettView view;
	
	private ValuesManager vm = new ValuesManager();
	
	//按钮权限
	public IButton newBtn;
	public IButton saveBtn;
	public IButton delBtn;
	public IButton cancelBtn;
	
	
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */

	
	public ShpmFeeWin(ListGrid table,SuplrFeeSettView view){
		this.table = table;
		this.view = view;
	}
	
	public Window getViewPanel() {
		VLayout lay = new VLayout();
		ds = BillShpmFeeDS.getInstance("SETT_SHPM_FEE_DETAIL");
		
		SGText shpm_no = new SGText("DOC_NO", "");
		shpm_no.setVisible(false);
		shpm_no.setValue(table.getSelectedRecord().getAttributeAsString("SHPM_NO"));
		
		SGCombo FEE_ID  = new SGCombo("FEE_ID", Util.TI18N.FEE_NAME());
		Util.initComboValue(FEE_ID, "V_TRANS_CHARGE_TYPE", "FEE_CODE", "FEE_NAME", " WHERE FEE_ATTR_CODE='YF'","SHOW_SEQ");
		FEE_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_NAME()));
		
		final SGCombo FEE_BAS = new SGCombo("FEE_BAS",Util.TI18N.FEE_BASE());
		Util.initCodesComboValue(FEE_BAS, "FEE_BASE");
		FEE_BAS.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_BASE()));
		
		
		final SGText BAS_VALUE  = new SGText("BAS_VALUE", Util.TI18N.BAS_VALUE());
		Util.initFloatTextItem(BAS_VALUE, StaticRef.GWT_FLOAT);
		BAS_VALUE.setTitle(ColorUtil.getRedTitle(Util.TI18N.BAS_VALUE()));
		
		final SGText MILE = new SGText("MILE", Util.TI18N.MILE());
		Util.initFloatTextItem(MILE, StaticRef.QNTY_FLOAT);
		
		final SGText PRICE = new SGText("PRICE", Util.TI18N.PRICE(),true);
		Util.initFloatTextItem(PRICE, StaticRef.PRICE_FLOAT);
		
		final SGText DUE_FEE = new SGText("DUE_FEE", Util.TI18N.DUE_FEE());
		Util.initFloatTextItem(DUE_FEE, StaticRef.PRICE_FLOAT);
		
		final SGCheck IS_RDC = new SGCheck("IS_RDC", "RDC计费");
		IS_RDC.setColSpan(2);
		IS_RDC.setValue(true);		
		
		SGCheck UP_DOWN = new SGCheck("UP_DOWN", "分摊费用");
		UP_DOWN.setValue(true);
		
		final SGPanel searchPanel = new SGPanel();
		searchPanel.setTitleWidth(100);
		searchPanel.setItems(FEE_ID,FEE_BAS,BAS_VALUE,MILE,PRICE,DUE_FEE,IS_RDC,UP_DOWN,shpm_no);
		
		FEE_BAS.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Util.async.getBasVal(view.headTable.getSelectedRecord().getAttributeAsString("SHPM_NO"),FEE_BAS.getDisplayValue(),IS_RDC.getValueAsBoolean(), new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						;
					}

					@Override
					public void onSuccess(String result) {
						BAS_VALUE.setValue(result);
						if(FEE_BAS.getDisplayValue().indexOf("公里") >= 0)
							MILE.setValue("1");
						else
							MILE.setValue("0");
					}
					
				});
			}
		});
		
		PRICE.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(FEE_BAS.getDisplayValue().indexOf("公里") >= 0)
					DUE_FEE.setValue(Double.parseDouble(BAS_VALUE.getValue().toString()) * Double.parseDouble(PRICE.getValue().toString())
							* Double.parseDouble(MILE.getValue().toString()));
				else	
					DUE_FEE.setValue(Double.parseDouble(BAS_VALUE.getValue().toString()) * Double.parseDouble(PRICE.getValue().toString()));
			}
		});
		/**
		DUE_FEE.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				PRICE.setValue(Double.parseDouble(DUE_FEE.getValue().toString()) / Double.parseDouble(BAS_VALUE.getValue().toString()));
			}
		});**/
		
		vm.addMember(searchPanel);		
		
		lay.addMember(createListTable());
        lay.addMember(searchPanel);
        lay.addMember(createFeeBtn());
        
        Criteria crit = new Criteria();
        crit.addCriteria("OP_FLAG", "M");
        crit.addCriteria("DOC_NO",table.getSelectedRecord().getAttributeAsString("SHPM_NO"));
		crit.addCriteria("REFENENCE2",table.getSelectedRecord().getAttributeAsString("REFENENCE2"));
		addrList.fetchData(crit);
		
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
		
		ListGridField SHPM_NO = new ListGridField("DOC_NO",Util.TI18N.SHPM_NO(),80);
		ListGridField FEE_NAME = new ListGridField("FEE_NAME",Util.TI18N.FEE(),80);
		ListGridField FEE_BAS_NAME = new ListGridField("FEE_BAS_NAME",Util.TI18N.FEE_BASE(),80);
		ListGridField BAS_VALUE = new ListGridField("BAS_VALUE",Util.TI18N.BAS_VALUE(),80);
		ListGridField PRICE = new ListGridField("PRICE",Util.TI18N.PRICE(),70);
		ListGridField MILE = new ListGridField("MILE",Util.TI18N.MILE(),80);
		Util.initFloatListField(MILE, StaticRef.QNTY_FLOAT);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE",Util.TI18N.DUE_FEE(),80);
		
		addrList.setFields(SHPM_NO,FEE_NAME,FEE_BAS_NAME,BAS_VALUE,PRICE,MILE,DUE_FEE);
		
		
		addrList.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				vm.editRecord(event.getRecord());
				delBtn.enable();
			}
		});
		
		addrList.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				newBtn.disable();
				saveBtn.enable();
				delBtn.disable();
				cancelBtn.enable();
			}
		});
		
		return addrList;
	}
	
	private ToolStrip createFeeBtn(){
		newBtn = new IButton(Util.BI18N.NEW());
        newBtn.setIcon(StaticRef.ICON_NEW);
        newBtn.setWidth(60);
        newBtn.setAutoFit(true);
        newBtn.setAlign(Alignment.RIGHT);
        
        
		saveBtn = new IButton(Util.BI18N.SAVE());
		saveBtn.setIcon(StaticRef.ICON_SAVE);
		saveBtn.setWidth(60);
		saveBtn.setAutoFit(true);
		saveBtn.setAlign(Alignment.RIGHT);
		saveBtn.addClickHandler(new SavePayShpmAction(view, vm,addrList,this));
		
		delBtn = new IButton(Util.BI18N.DELETE());
        delBtn.setIcon(StaticRef.ICON_DEL);
		delBtn.setWidth(60);
		delBtn.setAutoFit(true);
		delBtn.setAlign(Alignment.RIGHT);
		delBtn.addClickHandler(new DeletePayShpmAction(vm,addrList,this));
		
		cancelBtn = new IButton(Util.BI18N.CANCEL());
		cancelBtn.setIcon(StaticRef.ICON_CANCEL);
		cancelBtn.setWidth(60);
		cancelBtn.setAutoFit(true);
		cancelBtn.setAlign(Alignment.RIGHT);
		
		newBtn.enable();
		saveBtn.disable();
		delBtn.disable();
		cancelBtn.disable();
		
		newBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				vm.editNewRecord();
				newBtn.disable();
				saveBtn.enable();
				delBtn.disable();
				cancelBtn.enable();
			}
		});
		
		saveBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				newBtn.enable();
				saveBtn.disable();
				delBtn.enable();
				cancelBtn.disable();
			}
		});
		
		delBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		cancelBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				vm.clearValues();
				newBtn.enable();
				saveBtn.disable();
				delBtn.enable();
				cancelBtn.disable();
			}
		});
        
		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
	    toolStrip.setMembers(newBtn,saveBtn,delBtn,cancelBtn); 
	    
	    return toolStrip;
	}
	
}
