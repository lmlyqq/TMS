package com.rd.client.view.tms;

import java.util.ArrayList;

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
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 运输管理--运输执行 --车辆登记--车辆检查 按钮
 * @author wangjun 
 *
 */
public class VechCenterWin extends Window{
	private int width=620;
	private int height=300;
	public ButtonItem confirmItem;
	public Window window = null;
	public DynamicForm form;
	public ListGrid table;
	public DynamicForm mainItem;
	public DataSource ds;
	public SectionStackSection section;
	public VechRegistView view;
	private boolean pass= true;
	public ToolStrip toolStrip;


	public VechCenterWin(DataSource ds, DynamicForm form,
			SectionStackSection p_section,VechRegistView view) {
		this.form = form;
		this.ds = ds;
		this.section = p_section;
		this.view = view;
	}

	public void createBtnWidget(ToolStrip strip) {

	}

	public void createForm(DynamicForm searchForm) {
		
		IButton passItem = new IButton(Util.BI18N.PASS());
		passItem.setIcon(StaticRef.ICON_SAVE);
		passItem.setWidth(60);
//		clearBtn.setStartRow(false);
		passItem.setAutoFit(true);
		passItem.setAlign(Alignment.RIGHT);
		passItem.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				doOperate(pass);
				window.hide();
			}
		});
		
//		ButtonItem passItem = new ButtonItem(Util.BI18N.PASS());//合格通过
//		passItem.setIcon(StaticRef.ICON_SAVE);
//		passItem.setTitleOrientation(TitleOrientation.TOP);
//		passItem.setColSpan(1);
//		passItem.setStartRow(false);
//		passItem.setEndRow(false);
//		passItem.setAutoFit(true);
//		passItem.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				
//				doOperate(pass);
//				window.hide();
//			}
//		});
		
		IButton nopassItem = new IButton(Util.BI18N.NOPASS());
		nopassItem.setIcon(StaticRef.ICON_CONFIRM);
		nopassItem.setWidth(60);
//		clearBtn.setStartRow(false);
		nopassItem.setAutoFit(true);
		nopassItem.setAlign(Alignment.RIGHT);
		nopassItem.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				doOperate(false);
//				form.clearValues();
				window.hide();
			}
		});
		
//		ButtonItem nopassItem = new ButtonItem(Util.BI18N.NOPASS());//不合格退回
//		nopassItem.setIcon(StaticRef.ICON_SAVE);
//		nopassItem.setTitleOrientation(TitleOrientation.TOP);
//		nopassItem.setColSpan(1);
//		nopassItem.setStartRow(false);
//		nopassItem.setEndRow(false);
//		nopassItem.setAutoFit(true);
//		nopassItem.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				doOperate(false);
////				form.clearValues();
//				window.hide();
//			}
//		});

		IButton cancelItem = new IButton(Util.BI18N.CANCEL());
		cancelItem.setIcon(StaticRef.ICON_CANCEL);
		cancelItem.setWidth(60);
//		clearBtn.setStartRow(false);
		cancelItem.setAutoFit(true);
		cancelItem.setAlign(Alignment.RIGHT);
		cancelItem.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				form.clearValues();
							
			}
		});
		
//		ButtonItem cancelItem = new ButtonItem(Util.BI18N.CANCEL());//取消
//		cancelItem.setIcon(StaticRef.ICON_SAVE);
//		cancelItem.setColSpan(1);
//		cancelItem.setAutoFit(true);
//		cancelItem.setStartRow(false);
//		cancelItem.setEndRow(false);
//		cancelItem.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				form.clearValues();
//				
//			}
//		});

//		mainItem = new SGPanel();
//		mainItem.setItems(passItem,nopassItem,cancelItem);
//		mainItem.setBackgroundColor(ColorUtil.BG_COLOR);
		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
        toolStrip.setMembers(passItem,nopassItem,cancelItem);
		
	}
	
	private void doOperate(boolean pass){
		final String SYS_TIME = Util.getCurTime();
		Record record = view.right_record;
		String proName = "TRANS_CHECK_VEHICLE_PRO(?,?,?,?,?,?,?,?,?,?,?)";
		ArrayList<String> idList = new ArrayList<String>();
		idList.add(LoginCache.getLoginUser().getUSER_ID());
		idList.add(form.getItem("PLATE_NO").getValue().toString());
//		idList.add(ObjUtil.ifObjNull(LoginCache.getLoginUser().getDEFAULT_WHSE_ID(),"").toString());
//		idList.add(ObjUtil.ifObjNull(LoginCache.getLoginUser().getDEFAULT_ORG_ID(),"").toString());
		idList.add(record.getAttribute("QUEUE_SEQ"));
//		idList.add(record.getAttribute("ARRIVE_WHSE_TIME"));
		idList.add(ObjUtil.ifObjNull(form.getItem("VAN_CIRCS").getValue() ,"").toString());
		idList.add(ObjUtil.ifObjNull(form.getItem("VEHL_CIRCS").getValue() ,"").toString());
		idList.add(ObjUtil.ifObjNull(form.getItem("TYRE_CIRCS").getValue() ,"").toString());
		idList.add(ObjUtil.ifObjNull(form.getItem("SANIT_CIRCS").getValue() ,"").toString());
		idList.add(ObjUtil.ifObjNull(form.getItem("DIRVER_CIRCS").getValue() ,"").toString());
		idList.add(ObjUtil.ifObjNull(form.getItem("NOTES").getValue() , " ").toString());
		if(pass){
			idList.add("pass");
		}else {
			idList.add("unpass");
		}
		
		Util.async.execProcedure(idList, proName,new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.subSequence(0,2))){
			       MSGUtil.showOperSuccess();
			       Record record = view.loadRightTable.getSelectedRecord();
			       record.setAttribute("VAN_CIRCS_NAME", ObjUtil.ifObjNull(form.getItem("VAN_CIRCS").getDisplayValue(),"").toString());
			       record.setAttribute("VEHL_CIRCS_NAME", ObjUtil.ifObjNull(form.getItem("VEHL_CIRCS").getDisplayValue(),"").toString());
			       record.setAttribute("TYRE_CIRCS_NAME", ObjUtil.ifObjNull(form.getItem("TYRE_CIRCS").getDisplayValue(),"").toString());
			       record.setAttribute("SANIT_CIRCS_NAME", ObjUtil.ifObjNull(form.getItem("SANIT_CIRCS").getDisplayValue(),"").toString());
			       record.setAttribute("DIRVER_CIRCS_NAME", ObjUtil.ifObjNull(form.getItem("DIRVER_CIRCS").getDisplayValue(),"").toString());
			       
			       if("-1".equals(result.subSequence(2,result.length()))){
			    	   record.setAttribute("QUEUE_SEQ","-1");
			    	   record.setAttribute("LOAD_STATUS",StaticRef.TRANS_DISQUALIFICATE);
			    	   record.setAttribute("LOAD_STATUS_NAME",StaticRef.TRANS_DISQUALIFICATE_NAME);
			    	   record.setAttribute("LEAVE_WHSE_TIME", SYS_TIME);
			    	   
			    	   
			       }else {
			    	   record.setAttribute("LOAD_STATUS",StaticRef.TRANS_EXPECT);
			    	   record.setAttribute("LOAD_STATUS_NAME",StaticRef.TRANS_EXPECT_NAME);
			       }
			       view.loadRightTable.updateData(record);
			       view.loadRightTable.redraw();
//			    	Criteria crit1 = new Criteria();
//			    	
//			   	    crit1.addCriteria("OP_FLAG","M");
//			   	    crit1.addCriteria("LOAD_STATUS",StaticRef.TRANS_EXPECT);
//			   	    crit1.addCriteria("EXEC_ORG_ID",ObjUtil.ifNull(LoginCache.getLoginUser().getDEFAULT_ORG_ID()," "));
//			   	    crit1.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
//			   	    crit1.addCriteria("A",!view.crit_flag);
//			   	    view.crit_flag = !view.crit_flag;
//			   	    
//			   	    view.loadRightTable.fetchData(crit1);
			   	    
//			   	    ListGridRecord[] records = view.loadRightTable.getRecords();
//					ListGridRecord record=view.loadRightTable.getSelectedRecord();
//					RecordList newList = new RecordList();
//					for(int i = 0 ; i<records.length ; i++){
//						if(!records[i].getAttribute("PLATE_NO").equals(record.getAttribute("PLATE_NO"))){
//                            newList.add(records[i]);
//					}
//					}
//					view.loadRightTable.setData(newList);
			   	    
//			       }
			       
		        }else {
		        	MSGUtil.sayWarning(result.substring(2,result.length()));
		        }
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	public Window getViewPanel() {

		//车牌号 PLATE_NO
		//篷布状况 VAN_CIRCS 
		//车况 VEHL_CIRCS
		//轮胎状况  TYRE_CIRCS
		//车厢清洁卫生 SANIT_CIRCS
		//司机精神状态 DIRVER_CIRCS
		//备注  NOTES
		
		SGText PLATE_NO=new SGText("PLATE_NO", Util.TI18N.PLATE_NO(),true);
		PLATE_NO.setDisabled(true);
		PLATE_NO.setTitle(ColorUtil.getRedTitle(Util.TI18N.PLATE_NO()));
		PLATE_NO.setDefaultValue(view.right_record.getAttribute("PLATE_NO"));
		SGCombo VAN_CIRCS=new SGCombo("VAN_CIRCS",Util.TI18N.VAN_CIRCS());
		Util.initCodesComboValue(VAN_CIRCS, "VEHICLE_PROOF");
		SGCombo VEHL_CIRCS=new SGCombo("VEHL_CIRCS",Util.TI18N.VEHL_CIRCS(),true);
		Util.initCodesComboValue(VEHL_CIRCS, "VEHICLE_CONDITION");
		SGCombo TYRE_CIRCS=new SGCombo("TYRE_CIRCS",Util.TI18N.TYRE_CIRCS());
		Util.initCodesComboValue(TYRE_CIRCS, "TYRE_CONDITION");
		SGCombo SANIT_CIRCS=new SGCombo("SANIT_CIRCS",Util.TI18N.SANIT_CIRCS(),true);
		Util.initCodesComboValue(SANIT_CIRCS, "CAR_CONDITION");
		SGCombo DIRVER_CIRCS=new SGCombo("DIRVER_CIRCS",Util.TI18N.DIRVER_CIRCS());
		Util.initCodesComboValue(DIRVER_CIRCS, "DRIVER_STAT");
		SGText NOTES=new SGText("NOTES", Util.TI18N.NOTES(),true);
		NOTES.setWidth(350);
		NOTES.setHeight(40);
		NOTES.setColSpan(6);
		
	    Util.initCodesComboValue(VAN_CIRCS, "VEHICLE_PROOF");
		Util.initCodesComboValue(VEHL_CIRCS, "VEHICLE_CONDITION");
		Util.initCodesComboValue(TYRE_CIRCS, "TYRE_CONDITION");
		Util.initCodesComboValue(SANIT_CIRCS, "CAR_CONDITION");
		Util.initCodesComboValue(DIRVER_CIRCS, "DRIVER_STAT");
		
		form = new SGPanel();
		form.setItems(PLATE_NO,VAN_CIRCS,VEHL_CIRCS,TYRE_CIRCS,SANIT_CIRCS,DIRVER_CIRCS,NOTES);
		
		form.setHeight(height / 2);
		form.setWidth(width - 30);
		form.setNumCols(8);
		form.setPadding(10);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
	//	form.setHeight("35%");
		form.setHeight100();
		createForm(form);
		
		TabSet tabSet=new TabSet();
		tabSet.setWidth("100%");
		tabSet.setHeight("100%");
		
		Tab tab=new Tab(Util.TI18N.VECH_CHECK());//车辆检查
		tab.setPane(form);
		tabSet.addTab(tab);
	
		VLayout lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.addMember(tabSet);

		window = new Window();
		window.setTitle("车辆检查"); // --yuanlei 2011-2-16
		window.setLeft("30%");
		window.setTop("35%");
		window.setWidth(width);
		window.setHeight(height);
		window.setCanDragReposition(true);
		window.setCanDragResize(true);
		
		window.addItem(lay1);
//		window.addItem(mainItem);
		window.addItem(toolStrip);

		window.setShowCloseButton(true);
		window.setShowMaximizeButton(true);
		window.show();
		window.addMinimizeClickHandler(new MinimizeClickHandler() {

			@Override
			public void onMinimizeClick(MinimizeClickEvent event) {
				window.setMinimized(false);
				window.hide();
				event.cancel();
			}

		});
	

		return window;
	}
}
