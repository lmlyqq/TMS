package com.rd.client.action.tms;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
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

/**
 * 冻结/释放
 * @author lijun
 *
 */
public class FreezeShpmHeadAction extends Window {
	private int width = 300;
	private int height = 200;
	public Window window = null;
    private SGTable headTable;
    private ListGridRecord[] record;
    private SGPanel form;
    public DynamicForm mainSearch;
    private SGCombo FREEZE_REASON;
    private SGLText FREEZE_NOTES;
    private TmsShipmentView view;
    
    
    public FreezeShpmHeadAction(SGTable headTable,ListGridRecord[] record,TmsShipmentView view){
    	this.headTable = headTable;
    	this.record = record;
    	this.view = view;
    }
	
    public Window getviewPanel(){
//    	record = headTable.getSelection();
    	form = new SGPanel();
		form.setHeight(height/2);
		form.setWidth(width-11);
		form.setNumCols(8);
		form.setPadding(5);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
		form.setHeight100();
		createForm(form);
		FREEZE_REASON = new SGCombo("FREEZE_REASON", Util.TI18N.VEH_LOCK_REASON());
		Util.initCodesComboValue(FREEZE_REASON, "FREEZE_REASON");
		FREEZE_REASON.setTitle(ColorUtil.getRedTitle(Util.TI18N.VEH_LOCK_REASON()));
//		VEH_LOCK_REASON.setDisabled(false);
//		VEH_LOCK_REASON.setColSpan(2);
		FREEZE_NOTES = new SGLText("FREEZE_NOTES", Util.TI18N.REASON(), true);
		FREEZE_NOTES.setWidth(256);
		form.setItems(FREEZE_REASON,FREEZE_NOTES);
		
		VLayout lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.setMembers(form);
		
		window = new Window();
		window.setTitle("冻结");
		window.setLeft("33%");
		window.setTop("25%");
		window.setWidth(width);  
		window.setHeight(height); 
		window.setAlign(Alignment.CENTER);
		window.setCanDragReposition(true);  
		window.setCanDragResize(true);
		window.addItem(lay1);
		window.addItem(mainSearch);
		return window;
    	
//		return null;
    	
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
				if(!ObjUtil.isNotNull(FREEZE_REASON.getValue())){
					MSGUtil.sayWarning("请选择冻结原因.");
				}  else {
					doFreeze(record);
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
//				if(FREEZE_REASON.getValue() != null){
					form.clearValues();
//				}  else {
//					SC.say("please input the freeze reason.");
//				}  
					
			}
		});
		
		mainSearch = new DynamicForm();
		mainSearch.setCellPadding(5);
		mainSearch.setNumCols(5);
		mainSearch.setItems(confirmItem,clearItem);
		mainSearch.setBackgroundColor(ColorUtil.BG_COLOR);
    }
    
    private void doFreeze(final ListGridRecord[] record){
    	StringBuffer sf = new StringBuffer();
    	StringBuffer sf2 = new StringBuffer();
    	String loginUserID = LoginCache.getLoginUser().getUSER_ID();
    	
    	String proName="SP_SHPM_FROZEN(?,?,?,?,?)"; //shpm_no,freeze_reason,freeze_notes,output_result
    	ArrayList<String> idList;
		HashMap<String,ArrayList<String>> procesList = new HashMap<String,ArrayList<String>>();
		for (int i = 0; i < record.length; i++) {
			if(!StaticRef.SHPM_CONFIRM.equals(record[i].getAttribute("STATUS"))){
				sf.append(record[i].getAttribute("SHPM_NO"));
			} else if (StaticRef.SHPM_FROZEN.equals(record[i].getAttribute("STATUS"))){
				sf2.append(record[i].getAttribute("SHPM_NO"));
			} else {
				idList = new ArrayList<String>();
				idList.add(record[i].getAttribute("SHPM_NO"));
				idList.add(FREEZE_REASON.getValue().toString());
//				idList.add(FREEZE_NOTES.getValue().toString());
				idList.add((String) ObjUtil.ifObjNull(FREEZE_NOTES.getValue(), ""));
				idList.add(loginUserID);
				procesList.put(record[i].getAttribute("SHPM_NO"),idList);
				
			}
		}
		
		if(sf != null && sf.length() > 0){
			MSGUtil.sayError("作业单号"+sf.substring(0, sf.length()-1)+",非【已确认】状态, 不能执行冻结操作!");
			return ;
		}
		if(sf2 != null && sf2.length() > 0){
			MSGUtil.sayError("作业单号"+sf2.substring(0, sf2.length()-1)+",已经处于【冻结】状态, 不能执行冻结操作!");
			return ;
		}
		
		Util.async.execPro(procesList, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){
					MSGUtil.showOperSuccess();
				}else{
					MSGUtil.sayError(result);
				}
				
				String[] soo = result.split(",");
				HashMap<String, String> map =new HashMap<String, String>();
				for(int i=0;i<soo.length;i++){
					map.put(soo[i], "1");
				}
				for(int i=0;i<record.length;i++){
					if(map.get(record[i].getAttribute("SHPM_NO")) == null){
						record[i].setAttribute("STATUS", "90");
						record[i].setAttribute("STATUS_NAME", "已冻结");
//						record[i].setAttribute("EXEC_ORG_ID_NAME", EXEC_ORG_ID.getValue());
						
						headTable.updateData(record[i]);
					}
				}
				//view.freeButton.enable();
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
