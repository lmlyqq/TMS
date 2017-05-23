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
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGText;
import com.rd.client.view.tms.TmsShipmentView;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 作业单管理---派发
 * @author lijun
 *
 */
public class PayoutShpmentWin extends Window{
	private int width = 300;
	private int height = 200;
	public SectionStackSection section;
	public DynamicForm mainSearch;
	private SGPanel form;
	private ListGrid table;
	public Window window = null;
	private  SGText EXEC_ORG_ID ;
	private SGCombo TRANS_SRVC_ID;
	private TextItem ORG_ID;
	private SGCombo  SUPPLER_NAME;
//	private SGCombo  SUPPLER_ID;

	public StringBuffer msg;

	private ListGridRecord[] list;
	public TmsShipmentView view;
	
//	private boolean payout;
	
	/**
	 * 
	 * @param table
	 * @param payout is true 派发
	 */
	public PayoutShpmentWin(ListGrid table,boolean payout,TmsShipmentView view){
		this.table = table;
		this.view = view;
	}
	
	public PayoutShpmentWin(){
		
	}
	
	

	public Window getViewPanel() {
	    form = new SGPanel();
		form.setHeight(height/2);
		form.setWidth(width-11);
		form.setNumCols(8);
		form.setPadding(5);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
		form.setHeight100();
		createForm(form);
		ORG_ID = new TextItem("ORG_ID");
		
		ORG_ID.setVisible(false);
		EXEC_ORG_ID = new SGText("EXEC_ORG_ID",Util.TI18N.EXEC_ORG_ID(),true);
		Util.initOrg(EXEC_ORG_ID, ORG_ID, false, "25%", "40%");
		EXEC_ORG_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.EXEC_ORG_ID()));
		EXEC_ORG_ID.setColSpan(3);
		
		TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID",Util.TI18N.TRANS_SRVC_ID());
		Util.initComboValue(TRANS_SRVC_ID, "BAS_TRANS_SERVICE","ID","SRVC_NAME"," WHERE ENABLE_FLAG='Y'","","1");
		TRANS_SRVC_ID.setColSpan(3);
		
//		SUPPLER_ID = new SGCombo("ID",Util.TI18N.SUPLR_ID());
//		SUPPLER_ID.setVisible(false);
		SUPPLER_NAME = new SGCombo("SUPLR_ID_NAME",Util.TI18N.SUPLR_NAME(),true);
		SUPPLER_NAME.setColSpan(3);
		
		form.setItems(EXEC_ORG_ID,TRANS_SRVC_ID,ORG_ID,SUPPLER_NAME);
		
		EXEC_ORG_ID.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				SUPPLER_NAME.clearValue();
				String exec = form.getItem("EXEC_ORG_ID").getValue().toString();
				Util.initComboValue(SUPPLER_NAME, "V_BAS_SUPPLIER_ORG","SUPLR_ID","SUPLR_ID_NAME", " WHERE ORG_NAME='"+exec+"'");
			}
		});
		
		
		VLayout lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.setMembers(form);
		

	    
	    window = new Window();
		window.setTitle("派发");
		window.setLeft("35%");
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
				//form.g获取执行机构的值,如果不是null，那么就执行确定，否则返回错误
				
				
				if(EXEC_ORG_ID.getValue()==null){
					MSGUtil.sayWarning("请选择需要派发的执行机构");
				} else {
//					SC.confirm("您是否确认执行作业单派发", new BooleanCallback() {
//						public void execute(Boolean value) { 
//						 if(value!=null && value){
//							 doPayout();
//							  window.destroy();
//						 }else {
//							  window.show();
//						 }
//						}
//		            });
					
					doPayout();
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

	
	private void doPayout(){
		list = table.getSelection();
		StringBuffer sf = new StringBuffer();
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		String proName="SP_SHPM_DISPATCH(?,?,?,?,?,?,?)";
		ArrayList<String> idList;
		HashMap<String,ArrayList<String>> procesList = new HashMap<String,ArrayList<String>>();
		
		if(list != null && list.length >0){
			for(int i=0;i<list.length;i++){
				
					if(!StaticRef.SHPM_CONFIRM.equals(list[i].getAttribute("STATUS"))){
						sf.append(list[i].getAttribute("SHPM_NO"));
						sf.append("\t 处于"+list[i].getAttribute("STATUS"));
						sf.append(",");
					}else{
						idList = new ArrayList<String>();
						idList.add(list[i].getAttribute("SHPM_NO"));
						if(ObjUtil.isNotNull(TRANS_SRVC_ID)){
							form.getItem("TRANS_SRVC_ID");
							idList.add(form.getItem("TRANS_SRVC_ID").getValue().toString());
						}else {
							idList.add((String)TRANS_SRVC_ID.getValue());
						}
						idList.add(form.getItem("SUPLR_ID_NAME").getValue().toString());
						idList.add(form.getItem("SUPLR_ID_NAME").getDisplayValue());
						idList.add(ORG_ID.getValue().toString());
						idList.add(loginUser);
						procesList.put(list[i].getAttribute("SHPM_NO"),idList);
					
				}
			}
		}else {
			MSGUtil.sayWarning("请选择作业单，再执行派发！");
		}
		
		if(sf.toString().length() > 0){
			
				MSGUtil.sayError("您选择的作业单 "+sf.substring(0,sf.length()-1) +"状态, 不能执行派发操作!");
				return;
		}
        Util.async.execPro(procesList, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.subSequence(0, 2))){
					MSGUtil.showOperSuccess();
				}else{
					MSGUtil.sayError(result);
				}
				String[] soo = result.substring(3).split(",");
				HashMap<String, String> map =new HashMap<String, String>();
				for(int i=0;i<soo.length;i++){
					map.put(soo[i], "1");
				}
				//刷新状态
				for(int i=0;i<list.length;i++){
					if(ObjUtil.isNotNull(map.get(list[i].getAttribute("SHPM_NO")))){
					
							list[i].setAttribute("ASSIGN_STAT_NAME", "已派发");
							list[i].setAttribute("EXEC_ORG_ID_NAME", EXEC_ORG_ID.getValue());
							list[i].setAttribute("SUPLR_NAME",form.getItem("SUPLR_ID_NAME").getDisplayValue());
							if(form.getItem("TRANS_SRVC_ID").getDisplayValue() != null){
								list[i].setAttribute("TRANS_SRVC_ID_NAME", form.getItem("TRANS_SRVC_ID").getDisplayValue());
							}
//							table.updateData(list[i]);
							table.redraw();
					}
				}
				
				//view.freeButton.disable();
				//view.freezeButton.enable();
				view.closeButton.enable();
				view.payoutButton.enable();
				
				table.redraw();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
	}



	

}
