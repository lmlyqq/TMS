package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsSelfControl;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class SelfConShpmSplitWin extends Window{
	private int width = 340;
	private int height = 200;
	public SectionStackSection section;
	public DynamicForm mainSearch;
	private SGPanel form;
	public Window window = null;
	public StringBuffer msg;
	
	private TmsSelfControl view;
	private SGTable unshpmlstTable;
	private SGCombo SPLIT_REASON_CODE;
	private TextAreaItem SPLIT_REASON;

	public SelfConShpmSplitWin(SGTable table, SGTable p_unshpmTable, TmsSelfControl p_view){
		this.unshpmlstTable = table;
		this.view = p_view;
	}

	public Window getViewPanel() {
	    form = new SGPanel();
		form.setHeight(height/2);
		form.setWidth(width-20);
		form.setNumCols(6);
		form.setPadding(5);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
		form.setHeight100();
		createForm(form);
		
		SPLIT_REASON_CODE = new SGCombo("SPLIT_REASON_CODE",ColorUtil.getRedTitle("拆分原因"));
		Util.initCodesComboValue(SPLIT_REASON_CODE, "SPLIT_REASON");
		
		SPLIT_REASON = new TextAreaItem("SPLIT_REASON", "拆分备注");
		SPLIT_REASON.setStartRow(true);
		SPLIT_REASON.setColSpan(6);
		SPLIT_REASON.setHeight(50);
		SPLIT_REASON.setWidth(306);
		SPLIT_REASON.setTitleOrientation(TitleOrientation.TOP);
		SPLIT_REASON.setTitleVAlign(VerticalAlignment.TOP);
		form.setItems(SPLIT_REASON_CODE,SPLIT_REASON);
		
		VLayout lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.setMembers(form);
	    
	    window = new Window();
		window.setTitle("作业单拆分");
		window.setLeft("40%");
		window.setTop("25%");
		window.setWidth(width);  
		window.setHeight(height); 
		window.setAlign(Alignment.CENTER);
		window.setCanDragReposition(true);  
		window.setCanDragResize(true);
		window.addItem(lay1);
		window.addItem(mainSearch);
		return window;
	}
	public void createForm(DynamicForm confirmForm) {
		ButtonItem confirmItem = new ButtonItem(Util.BI18N.CONFIRM());
		confirmItem.setIcon(StaticRef.ICON_SEARCH);
		confirmItem.setColSpan(1);
		confirmItem.setStartRow(false);
		confirmItem.setEndRow(false);
		confirmItem.setAutoFit(true);
		confirmItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(SPLIT_REASON_CODE.getValue()== null || SPLIT_REASON_CODE.getValue().toString().equals("")){
					SC.warn("请选择拆分原因");
				} else {					
					doSplit();
					window.destroy();
				}
			}
			
		});
		
		ButtonItem clearItem = new ButtonItem(Util.BI18N.CLEAR());
		clearItem.setIcon(StaticRef.ICON_CANCEL);
		clearItem.setColSpan(1);
		clearItem.setAutoFit(true);
		clearItem.setStartRow(false);
		clearItem.setEndRow(true);
		clearItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				    form.clearValues();
			}
		});
		
		
        
		mainSearch = new DynamicForm();
		mainSearch.setCellPadding(5);
		mainSearch.setNumCols(5);
		mainSearch.setItems(confirmItem,clearItem);
		mainSearch.setBackgroundColor(ColorUtil.BG_COLOR);
	}
	
	private void doSplit(){
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> row_map = new HashMap<String, String>();   //行号
		HashMap<String, String> qnty_map = new HashMap<String, String>();  //数量
		HashMap<String, String> vol_map = new HashMap<String, String>();   //体积
		HashMap<String, String> gw_map = new HashMap<String, String>();    //毛重
		HashMap<String, String> nw_map = new HashMap<String, String>();    //净重 
		HashMap<String, String> worth_map = new HashMap<String, String>(); //货值   
		
		if(unshpmlstTable != null) {
			ListGridRecord[] records = unshpmlstTable.getSelection();
			int[] edit_rows = new int[records.length];
			for(int i = 0; i < records.length; i++) {
				edit_rows[i] = unshpmlstTable.getRecordIndex(records[i]);
			}
			String shpm_no = "";
			Record rec = null;
			
			for(int i = 0; i < edit_rows.length; i++) {
				
				rec = unshpmlstTable.getEditedRecord(edit_rows[i]);
				shpm_no = rec.getAttribute("SHPM_NO");
				row_map.put(Integer.toString(i+1), rec.getAttribute("SHPM_ROW"));
				qnty_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("QNTY"),"0").toString());
				vol_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("VOL"),"0").toString());
				gw_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("G_WGT"),"0").toString());
				nw_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("N_WGT"),"0").toString());
				worth_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("WORTH"),"0").toString());
			}
			listmap.put("1", shpm_no);
			listmap.put("2", row_map);
			listmap.put("3", qnty_map);
			listmap.put("4", vol_map);
			listmap.put("5", gw_map);
			listmap.put("6", nw_map);
			listmap.put("7", worth_map);
			listmap.put("8", SPLIT_REASON_CODE.getValue().toString());
			listmap.put("9", ObjUtil.ifObjNull(SPLIT_REASON.getValue(),"").toString());
			listmap.put("10", LoginCache.getLoginUser().getUSER_ID());
			String json = Util.mapToJson(listmap);
			Util.async.execProcedure(json, "SP_SHPM_SPLIT_QNTY(?,?,?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.sayInfo(result.substring(2));
						
						//刷新待调订单列表	
//						view.qryUnshpmTableAction.doRefresh(true);
						view.refreshTableAction.doRefresh(true);	
						//unshpmTable.selectRecord(0);
					}
					else{
						MSGUtil.sayError(result.substring(2));
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError(caught.getMessage());
				}
			});
		}
	}
}
