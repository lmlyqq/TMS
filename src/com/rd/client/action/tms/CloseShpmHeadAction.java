package com.rd.client.action.tms;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsShipmentView;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

public class CloseShpmHeadAction extends Window {
	private int width = 320;
	private int height = 200;
	public Window window = null;
    private SGTable headTable;
    private ListGridRecord[] record;
    private SGPanel form;
    public DynamicForm mainSearch;
    private SGCombo CLOSE_REASON;
    private SGLText CLOSE_NOTES;
    private TmsShipmentView view;
    
    public CloseShpmHeadAction(SGTable headTable,ListGridRecord[] record,TmsShipmentView view){
    	this.headTable = headTable;
    	this.record = record;
    	this.view = view;
    }
    
    public Window getviewPanel(){
    	form = new SGPanel();
		form.setHeight(height);
		form.setWidth(width-11);
		form.setNumCols(8);
		form.setPadding(5);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
		form.setHeight100();
		createForm(form);
		
		CLOSE_REASON = new SGCombo("CLOSE_REASON", Util.TI18N.CLOSE_REASON());
		Util.initCodesComboValue(CLOSE_REASON, "CLOSE_REASON");
		CLOSE_REASON.setTitle(ColorUtil.getRedTitle(Util.TI18N.CLOSE_REASON()));
//		VEH_LOCK_REASON.setDisabled(false);
//		VEH_LOCK_REASON.setColSpan(2);
		CLOSE_NOTES = new SGLText("CLOSE_NOTES", Util.TI18N.REASON(), true);
		CLOSE_NOTES.setWidth(256);
		form.setItems(CLOSE_REASON,CLOSE_NOTES);
		
		VLayout lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.setMembers(form);
		
		window = new Window();
		window.setTitle("关闭");
		window.setLeft("30%");
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
		confirmItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(!ObjUtil.isNotNull(CLOSE_REASON.getValue())){
					MSGUtil.sayWarning("请选择冻结原因.");
				}  else {
					doClose();
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
		clearItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
					form.clearValues();
			}
		});
		
		mainSearch = new DynamicForm();
		mainSearch.setCellPadding(5);
		mainSearch.setNumCols(5);
		mainSearch.setItems(confirmItem,clearItem);
		mainSearch.setBackgroundColor(ColorUtil.BG_COLOR);
    }
    
    private void doClose(){
    	StringBuffer sf = new StringBuffer();
    	StringBuffer sf2 = new StringBuffer();
    	String proName	= "SP_SHPM_CLOSE(?,?,?,?)"; 
    	ArrayList<String> idList;
		HashMap<String,ArrayList<String>> procesList = new HashMap<String,ArrayList<String>>();
		final ListGridRecord [] rec = headTable.getSelection();
		for (int i = 0; i < rec.length; i++) {
			if(!StaticRef.SHPM_CONFIRM.equals(rec[i].getAttribute("STATUS"))){
				sf.append(rec[i].getAttribute("SHPM_NO"));
			}else
			if (StaticRef.SHPM_CLOSED.equals(record[i].getAttribute("STATUS"))){
				sf2.append(rec[i].getAttribute("SHPM_NO"));
			}else {
				idList = new ArrayList<String>();
				idList.add(rec[i].getAttribute("SHPM_NO"));
				idList.add(CLOSE_REASON.getValue().toString());
				idList.add((String) ObjUtil.ifObjNull(CLOSE_NOTES.getValue(), " "));
				procesList.put(rec[i].getAttribute("SHPM_NO"),idList);
			}
			if(sf != null && sf.length() > 0){
				MSGUtil.sayError("作业单号"+sf.substring(0, sf.length()-1)+"处于【"+rec[i].getAttribute("STATUS")+"】状态, 不能执行关闭操作!");
				return ;
			}
			if(sf2 != null && sf2.length() > 0){
				MSGUtil.sayError("作业单号"+sf2.substring(0, sf2.length()-1)+",已经处于【关闭】状态, 不能执行关闭操作!");
				return ;
			}
			
			
		}
		Util.async.execPro(procesList, proName,new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result)){
					MSGUtil.showOperSuccess();
				}else{
					MSGUtil.sayError(result);
					return;
				}
				String[] soo = result.split(",");
				HashMap<String, String> map =new HashMap<String, String>();
				for(int i=0;i<soo.length;i++){
					map.put(soo[i], "1");
				}
				for(int i=0;i<rec.length;i++){
					if(map.get(rec[i].getAttribute("SHPM_NO")) == null){
						rec[i].setAttribute("STATUS", "99");
						rec[i].setAttribute("STATUS_NAME", "已关闭");
						
						headTable.updateData(record[i]);
					}
				}
				//view.freeButton.disable();
				//view.freezeButton.disable();
				view.closeButton.disable();
				view.payoutButton.disable();
				headTable.redraw();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
    }
}
