package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.tms.ScreenWinDS;
import com.rd.client.ds.tms.ScreenWinDS_;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class ScreenWin extends Window {
	private SGTable loadLeftTable;
	private SGTable loadRightTable;
	private SGTable loadRightTable2;
	private SGTable loadLeftTable2;
	private SGTable loadLeftTable3;
	private SGTable loadLeftTable4;
	private SGTable loadLeftTable5;
	private SGTable loadLeftTable6;
	private SGTable loadLeftTable7;
	private SGTable loadLeftTable8;
//	private SGPanel table;
	private int width = Page.getScreenWidth();
	private int height = Page.getScreenHeight();
	private String top = "0";
	private String left = "0";
	private String title = "大屏幕显示";
	public  Window window;
	private DataSource left_ds;
	private DataSource right_ds;
	private boolean  crit_flag = true;
	private boolean  crit_flag2 = true;
	private final int  schedule_ = 9000;
	private Timer timer ;
	private int i = 1;
	private int a = 1;
	private int b = 0;
	private int number = 5;
	private int crit_1 = 1;
	private int crit_2 = 1;
	private int crit_3 = 1;
	private int crit_4 = 1;
	private int crit_5 = 1;
	private int crit_6 = 1;
	private Criteria crit1 = new Criteria();
	private Criteria crit2 = new Criteria();
	private Criteria crit3 = new Criteria();
	private Criteria crit4 = new Criteria();
	private Criteria crit5 = new Criteria();
	private Criteria crit6 = new Criteria();
	private Criteria crit_onload = new Criteria();
	private Criteria cirt_willload = new Criteria();
	private String s;
	
	
	public ScreenWin(){
		
	}
	
	public ScreenWin(int width, int height, String top, String left,
			String title, Window window, DataSource ds) {
		this.width = width;
		this.height = height;
		this.top = top;
		this.left = left;
		this.title = title;
		this.window = window;
		
		
	}
	
	public Window getViewPanel1(){
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width-15);  
		window.setHeight(height-20); 
		window.setCanDrop(false);
		window.setCanDragScroll(false);
		
		left_ds=ScreenWinDS.getInstance("V_TRANS_LOAD_SCREEN","TRANS_LOAD_JOB");
		right_ds=ScreenWinDS_.getInstance("V_TRANS_LOAD_SCREEN_","TRANS_LOAD_JOB");
		loadLeftTable =  new SGTable(left_ds, "100%", "100%", false, true, false);
		loadLeftTable2 = new SGTable(left_ds, "100%", "100%", false, true, false);
		loadLeftTable3 = new SGTable(left_ds, "100%", "100%", false, true, false);
		loadLeftTable4 = new SGTable(left_ds, "100%", "100%", false, true, false);
		loadLeftTable5 = new SGTable(left_ds, "100%", "100%", false, true, false);
		loadLeftTable6 = new SGTable(left_ds, "100%", "100%", false, true, false);
		loadLeftTable7 = new SGTable(left_ds, "100%", "100%", false, true, false);
		loadLeftTable8 = new SGTable(left_ds, "100%", "100%", false, true, false);
		loadRightTable = new SGTable(left_ds, "100%", "100%", false, true, false);
		loadRightTable2 = new SGTable(left_ds, "100%", "100%", false, true, false);
		
		createRightFields(loadRightTable);
		createRightFields2(loadRightTable2);
		createLeftFields(loadLeftTable);
		createLeftFields2(loadLeftTable2);
		createLeftFields3(loadLeftTable3);
		createLeftFields4(loadLeftTable4);
		createLeftFields5(loadLeftTable5);
		createLeftFields6(loadLeftTable6);
		createLeftFields7(loadLeftTable7);
		createLeftFields8(loadLeftTable8);
        
		//四屏全部为正在装货效果
		TabSet ScreenSet1 = new TabSet();
		ScreenSet1.setWidth("100%");
		ScreenSet1.setHeight100();
			
		Tab tab1 = new Tab("正在装货");
			
		VLayout layout1 = new VLayout();
		layout1.setWidth("50%");
		layout1.addMember(loadLeftTable);
		layout1.addMember(loadLeftTable2);
	
		VLayout layout2 = new VLayout();
		layout2.setWidth("50%");
		layout2.addMember(loadLeftTable3);
		layout2.addMember(loadLeftTable4);
			
		HStack stack1 = new HStack();
		stack1.setWidth100();
		stack1.setHeight100();
		stack1.addMember(layout1);
		stack1.addMember(layout2);
		tab1.setPane(stack1);
		ScreenSet1.addTab(tab1);
			
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();
		stack.addMember(ScreenSet1);
		
		final Canvas canvas = new Canvas();
		canvas.addChild(stack);
		canvas.draw();
		canvas.hide();
		
		//两屏一半为正在装货，一半为等待装货
		TabSet ScreenSet3 = new TabSet();
		ScreenSet3.setWidth("45%");
		ScreenSet3.setHeight100();
			
		TabSet ScreenSet4 = new TabSet();
		ScreenSet4.setWidth("55%");
		ScreenSet4.setHeight100();
			
		Tab tab3 = new Tab("正在装货");
		VLayout layout3 = new VLayout();
		layout3.setHeight100();
		layout3.addMember(loadLeftTable5);
		tab3.setPane(layout3);
		ScreenSet3.addTab(tab3);
			
		Tab tab4 = new Tab("等待装货");
		VLayout layout4 = new VLayout();
		layout4.setHeight100();
		layout4.addMember(loadRightTable);
		tab4.setPane(layout4);
		ScreenSet4.addTab(tab4);
			
		HStack stack2 = new HStack();
		stack2.setWidth100();
		stack2.setHeight100();
		stack2.addMember(ScreenSet3);
		stack2.addMember(ScreenSet4);
		
		final Canvas canvas1 = new Canvas();
		canvas1.addChild(stack2);
		canvas1.draw();
//		canvas1.hide();
		
		//两屏正在装货一屏等待装货
		TabSet ScreenSet5 = new TabSet();
		ScreenSet5.setWidth("55%");
		ScreenSet5.setHeight100();
		
		Tab tab5 = new Tab("正在装货");
		VLayout layout5 = new VLayout();
		layout5.addMember(loadLeftTable6);
		layout5.addMember(loadLeftTable7);
		tab5.setPane(layout5);
		ScreenSet5.addTab(tab5);
		
		TabSet ScreenSet6 = new TabSet();
		ScreenSet6.setWidth("45%");
		ScreenSet6.setHeight100();
		
		Tab tab6 = new Tab("等待装货");
		VLayout layout6 = new VLayout();
		layout6.addMember(loadRightTable2);
		tab6.setPane(layout6);
		ScreenSet6.addTab(tab6);
		
		HStack stack3 = new HStack();
		stack3.setWidth100();
		stack3.setHeight100();
		stack3.addMember(ScreenSet5);
		stack3.addMember(ScreenSet6);
		
		final Canvas canvas3 = new Canvas();
		canvas3.addChild(stack3);
		canvas3.draw();
		canvas3.hide();
		
		String flds = "WHSE_ID";
		String fields = " WHERE USER_ID='"+ LoginCache.getLoginUser().getUSER_ID()+"'";
		String tableName = "SYS_USER_WHSE";
		Util.db_async.getRecord(flds, tableName, fields, null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
			
			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> result) {
				if(result.size() > 0){
					if(result.get(0).get("WHSE_ID") != null){
						crit1.addCriteria("LOAD_WHSE",result.get(0).get("WHSE_ID"));
						crit5.addCriteria("LOAD_WHSE",result.get(0).get("WHSE_ID"));
					}
				}
				if(result.size() > 1){
					if(result.get(1).get("WHSE_ID") != null){
						crit2.addCriteria("LOAD_WHSE",result.get(1).get("WHSE_ID"));
						crit6.addCriteria("LOAD_WHSE",result.get(1).get("WHSE_ID"));
					}
				}
				if(result.size() > 2){
					if(result.get(2).get("WHSE_ID") != null){
						crit3.addCriteria("LOAD_WHSE",result.get(2).get("WHSE_ID"));
					}
				}
				if(result.size() > 3){
					if(result.get(3).get("WHSE_ID") != null){
						crit4.addCriteria("LOAD_WHSE",result.get(3).get("WHSE_ID"));
					}
				}
				
				crit1.addCriteria("OP_FLAG","M");
				crit1.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
//				crit1.addCriteria("USER_ID", LoginCache.getLoginUser().getUSER_ID());
				crit1.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
				crit1.addCriteria("QUEUE",crit_1*number);
				crit1.addCriteria("QUEUE2",(crit_1-1)*number);
				
				crit2.addCriteria("OP_FLAG","M");
				crit2.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
//				crit2.addCriteria("EXEC_ORG_ID",ObjUtil.ifNull(LoginCache.getLoginUser().getDEFAULT_ORG_ID()," "));
				crit2.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
				crit2.addCriteria("QUEUE",crit_2*number);
				crit2.addCriteria("QUEUE2",(crit_2-1)*number);
				
				crit3.addCriteria("OP_FLAG","M");
				crit3.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
//				crit3.addCriteria("EXEC_ORG_ID",ObjUtil.ifNull(LoginCache.getLoginUser().getDEFAULT_ORG_ID()," "));
				crit3.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
				crit3.addCriteria("QUEUE",crit_3*number);
				crit3.addCriteria("QUEUE2",(crit_3-1)*number);
				
				crit4.addCriteria("OP_FLAG","M");
				crit4.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
//				crit4.addCriteria("EXEC_ORG_ID",ObjUtil.ifNull(LoginCache.getLoginUser().getDEFAULT_ORG_ID()," "));
				crit4.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
				crit4.addCriteria("QUEUE",crit_4*number);
				crit4.addCriteria("QUEUE2",(crit_4-1)*number);
				
				crit5.addCriteria("OP_FLAG","M");
				crit5.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
				crit5.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
				crit5.addCriteria("QUEUE",crit_5*number);
				crit5.addCriteria("QUEUE2",(crit_5-1)*number);
				
				crit6.addCriteria("OP_FLAG","M");
				crit6.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
				crit6.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
				crit6.addCriteria("QUEUE",crit_6*number);
				crit6.addCriteria("QUEUE2",(crit_6-1)*number);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
		window.addItem(canvas1);
		
    	crit_onload.addCriteria("OP_FLAG", "M");
    	crit_onload.addCriteria("LOAD_STATUS", StaticRef.TRANS_LOADING);
    	crit_onload.addCriteria("USER_ID", LoginCache.getLoginUser().getUSER_ID());
    	crit_onload.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
    	crit_onload.addCriteria("QUEUE",i*number);
    	crit_onload.addCriteria("QUEUE2",(i-1)*number);
    	i++;
    	
    	cirt_willload.addCriteria("OP_FLAG", "M");
    	cirt_willload.addCriteria("LOAD_STATUS", StaticRef.TRANS_EXPECT);
    	cirt_willload.addCriteria("USER_ID", LoginCache.getLoginUser().getUSER_ID());
    	cirt_willload.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
    	cirt_willload.addCriteria("QUEUE",a*number);
    	cirt_willload.addCriteria("QUEUE2",(a-1)*number);
    	a++;
    	
		loadLeftTable5.invalidateCache();
		loadLeftTable5.fetchData(crit_onload, new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				if(ObjUtil.isNotNull(loadLeftTable5.getRecordList())){
					if(loadLeftTable5.getRecordList().getLength() != 0){
						String num=	loadLeftTable5.getRecordList().get(loadLeftTable5.getRecordList().getLength()-1).getAttribute("NUM");
						String load_rownum=	loadLeftTable5.getRecordList().get(loadLeftTable5.getRecordList().getLength()-1).getAttribute("ROWNUM_");
						int num_ = Integer.parseInt(num);
						int load_rownum_ = Integer.parseInt(load_rownum);
						if(num_ == load_rownum_){
							i = 1;
						} 
					}
				}else {
					i = 1;
				}
				loadRightTable.invalidateCache();
				loadRightTable.fetchData(cirt_willload, new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						if(ObjUtil.isNotNull(loadRightTable.getRecordList())){
							if(loadLeftTable5.getRecordList().getLength()!=0){
								String num=	loadRightTable.getRecordList().get(loadRightTable.getRecordList().getLength()-1).getAttribute("NUM");
								String load_rownum=	loadRightTable.getRecordList().get(loadRightTable.getRecordList().getLength()-1).getAttribute("ROWNUM_");
								int num_ = Integer.parseInt(num);
								int load_rownum_ = Integer.parseInt(load_rownum);
								if(num_ == load_rownum_){
									a = 1;
									b = 0;
								}
							}else {
								a = 1;
								b = 0;
							}
						} 
//						b = 4;
					}
				});
				
				
			}
		});
		
		
		timer = new Timer(){

			@Override
			public void run() {
				    if(b == 0){
					    	loadLeftTable5.invalidateCache();
					    	crit_onload.setAttribute("QUEUE", i*number);
					    	crit_onload.setAttribute("QUEUE2", (i-1)*number);
					    	i++;
						
				    	loadLeftTable5.fetchData(crit_onload, new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								if(ObjUtil.isNotNull(loadLeftTable5.getRecordList())){
									if(loadLeftTable5.getRecordList().getLength()!=0){
										String num=	loadLeftTable5.getRecordList().get(loadLeftTable5.getRecordList().getLength()-1).getAttribute("NUM");
										String load_rownum=	loadLeftTable5.getRecordList().get(loadLeftTable5.getRecordList().getLength()-1).getAttribute("ROWNUM_");
										int num_ = Integer.parseInt(num);
										int load_rownum_ = Integer.parseInt(load_rownum);
										if(num_ == load_rownum_){
											i = 1;
										} 
									}
								}else {
									i = 1;
								}
								loadRightTable.invalidateCache();
								cirt_willload.setAttribute("QUEUE", a*number);
								cirt_willload.setAttribute("QUEUE2", (a-1)*number);
								a++;
								loadRightTable.fetchData(cirt_willload,new DSCallback() {
									
									@Override
									public void execute(DSResponse response, Object rawData, DSRequest request) {
										if(ObjUtil.isNotNull(loadRightTable.getRecordList().getLength())||loadLeftTable5.getRecordList().getLength()!=0){
											String num=	loadRightTable.getRecordList().get(loadRightTable.getRecordList().getLength()-1).getAttribute("NUM");
											String load_rownum=	loadRightTable.getRecordList().get(loadRightTable.getRecordList().getLength()-1).getAttribute("ROWNUM_");
											int num_ = Integer.parseInt(num);
											int load_rownum_ = Integer.parseInt(load_rownum);
											if(num_ == load_rownum_){
												a = 1;
//												b = 1;
											}
										}
									}
								});
							}
						});
				    	
				    }
				
					if(b == 1){
						window.removeItem(canvas1);
						canvas1.hide();
						canvas.show();
						window.addItem(canvas);
						loadLeftTable.invalidateCache();
					
						loadLeftTable.fetchData(crit1,new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								if(ObjUtil.isNotNull(loadLeftTable.getRecordList())){
									if(loadLeftTable.getRecordList().getLength()!=0){
										String num = loadLeftTable.getRecordList().get(loadLeftTable.getRecordList().getLength()-1).getAttribute("NUM");
										String load_rownum = loadLeftTable.getRecordList().get(loadLeftTable.getRecordList().getLength()-1).getAttribute("ROWNUM_");
										int num_ = Integer.parseInt(num);
										int load_rownum_ = Integer.parseInt(load_rownum);
										if(num_ == load_rownum_){
											crit_1 = 1;
										}
									}else {
										crit_1 = 1;
									}
								}
								loadLeftTable2.invalidateCache();
								loadLeftTable2.fetchData(crit2 ,new DSCallback() {
									
									@Override
									public void execute(DSResponse response, Object rawData, DSRequest request) {
										if(ObjUtil.isNotNull(loadLeftTable2.getRecordList())){
											if(loadLeftTable2.getRecordList().getLength()!=0){
												String num=	loadLeftTable2.getRecordList().get(loadLeftTable2.getRecordList().getLength()-1).getAttribute("NUM");
												String load_rownum=	loadLeftTable2.getRecordList().get(loadLeftTable2.getRecordList().getLength()-1).getAttribute("ROWNUM_");
												int num_ = Integer.parseInt(num);
												int load_rownum_ = Integer.parseInt(load_rownum);
												if(num_ == load_rownum_){
													crit_2 = 1;
												}
											} else {
												crit_2 = 1;
											}
										}
										loadLeftTable3.invalidateCache();
										loadLeftTable3.fetchData(crit3, new DSCallback() {
											
											@Override
											public void execute(DSResponse response, Object rawData, DSRequest request) {
												if(ObjUtil.isNotNull(loadLeftTable3.getRecordList())){
													if(loadLeftTable3.getRecordList().getLength()!=0){
														String num=	loadLeftTable3.getRecordList().get(loadLeftTable3.getRecordList().getLength()-1).getAttribute("NUM");
														String load_rownum=	loadLeftTable3.getRecordList().get(loadLeftTable3.getRecordList().getLength()-1).getAttribute("ROWNUM_");
														int num_ = Integer.parseInt(num);
														int load_rownum_ = Integer.parseInt(load_rownum);
														if(num_ == load_rownum_){
															crit_3 = 1;
														}
														
													}else {
														crit_3 = 1;
													}
												}
												loadLeftTable4.invalidateCache();
												loadLeftTable4.fetchData(crit4, new DSCallback() {
													
													@Override
													public void execute(DSResponse response, Object rawData, DSRequest request) {
														if(ObjUtil.isNotNull(loadLeftTable4.getRecordList())){
															if(loadLeftTable4.getRecordList().getLength()!=0){
																String num=	loadLeftTable4.getRecordList().get(loadLeftTable4.getRecordList().getLength()-1).getAttribute("NUM");
																String load_rownum=	loadLeftTable4.getRecordList().get(loadLeftTable4.getRecordList().getLength()-1).getAttribute("ROWNUM_");
																int num_ = Integer.parseInt(num);
																int load_rownum_ = Integer.parseInt(load_rownum);
																if(num_ == load_rownum_){
																	crit_4 = 1;
																	b=3;
																}
																
															} else {
																crit_4 = 1;
															}
														}
													}
												});
											}
										});
									}
								});
							}
						});
						
					}
					else if(b ==3){
						crit1.setAttribute("QUEUE", crit_1*number);
						crit1.setAttribute("QUEUE2", (crit_1-1)*number);
						crit_1++;
						loadLeftTable.invalidateCache();
						loadLeftTable.fetchData(crit1, new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								if(ObjUtil.isNotNull(loadLeftTable.getRecordList())){
									if(loadLeftTable.getRecordList().getLength()!=0){
										String num=	loadLeftTable.getRecordList().get(loadLeftTable.getRecordList().getLength()-1).getAttribute("NUM");
										String load_rownum=	loadLeftTable.getRecordList().get(loadLeftTable.getRecordList().getLength()-1).getAttribute("ROWNUM_");
										int num_ = Integer.parseInt(num);
										int load_rownum_ = Integer.parseInt(load_rownum);
										if(num_ == load_rownum_){
											crit_1 = 1;
										}
										
									} else {
										crit_1 = 1;
									}
								}
								
								crit2.setAttribute("QUEUE", crit_2*number);
								crit2.setAttribute("QUEUE2", (crit_2-1)*number);
								crit_2++;
								loadLeftTable2.invalidateCache();
								loadLeftTable2.fetchData(crit2, new DSCallback() {
									
									@Override
									public void execute(DSResponse response, Object rawData, DSRequest request) {
										if(ObjUtil.isNotNull(loadLeftTable2.getRecordList())){
											if(loadLeftTable2.getRecordList().getLength()!=0){
												String num=	loadLeftTable2.getRecordList().get(loadLeftTable2.getRecordList().getLength()-1).getAttribute("NUM");
												String load_rownum=	loadLeftTable2.getRecordList().get(loadLeftTable2.getRecordList().getLength()-1).getAttribute("ROWNUM_");
												int num_ = Integer.parseInt(num);
												int load_rownum_ = Integer.parseInt(load_rownum);
												if(num_ == load_rownum_){
													crit_2 = 1;
												}
											} else {
												crit_2 = 1;
											}
										}
										
										crit3.setAttribute("QUEUE", crit_3*number);
										crit3.setAttribute("QUEUE2", (crit_3-1)*number);
										crit_3++;
										loadLeftTable3.invalidateCache();
										loadLeftTable3.fetchData(crit3, new DSCallback() {
											
											@Override
											public void execute(DSResponse response, Object rawData, DSRequest request) {
												if(ObjUtil.isNotNull(loadLeftTable3.getRecordList())){
													if(loadLeftTable3.getRecordList().getLength()!=0){
														
														String num=	loadLeftTable3.getRecordList().get(loadLeftTable3.getRecordList().getLength()-1).getAttribute("NUM");
														String load_rownum=	loadLeftTable3.getRecordList().get(loadLeftTable3.getRecordList().getLength()-1).getAttribute("ROWNUM_");
														int num_ = Integer.parseInt(num);
														int load_rownum_ = Integer.parseInt(load_rownum);
														if(num_ == load_rownum_){
															crit_3 = 1;
														}
													} else {
														crit_3 = 1;
													}
												}
												
												crit4.setAttribute("QUEUE", crit_4*number);
												crit4.setAttribute("QUEUE2", (crit_4-1)*number);
												crit_4++;
												loadLeftTable4.invalidateCache();
												loadLeftTable4.fetchData(crit4, new DSCallback() {
													
													@Override
													public void execute(DSResponse response, Object rawData, DSRequest request) {
														if(ObjUtil.isNotNull(loadLeftTable4.getRecordList())){
															if(loadLeftTable4.getRecordList().getLength()!=0){
																String num=	loadLeftTable4.getRecordList().get(loadLeftTable4.getRecordList().getLength()-1).getAttribute("NUM");
																String load_rownum=	loadLeftTable4.getRecordList().get(loadLeftTable4.getRecordList().getLength()-1).getAttribute("ROWNUM_");
																int num_ = Integer.parseInt(num);
																int load_rownum_ = Integer.parseInt(load_rownum);
																if(num_ == load_rownum_){
																	crit_4 = 1;
																	b=4;
																}
																
															} else {
																crit_4 = 1;
															}
														}
													}
												});
												
											}
										});
									}
								});
								
								
							}
						});
					}
					else if(b == 4){
						window.removeItem(canvas);
						canvas.hide();
						canvas3.show();
						window.addItem(canvas3);
						loadLeftTable6.invalidateCache();
						loadLeftTable6.fetchData(crit5,new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								if(ObjUtil.isNotNull(loadLeftTable6.getRecordList())){
									if(loadLeftTable6.getRecordList().getLength()!=0){
										String num=	loadLeftTable6.getRecordList().get(loadLeftTable6.getRecordList().getLength()-1).getAttribute("NUM");
										String load_rownum=	loadLeftTable6.getRecordList().get(loadLeftTable6.getRecordList().getLength()-1).getAttribute("ROWNUM_");
										int num_ = Integer.parseInt(num);
										int load_rownum_ = Integer.parseInt(load_rownum);
										if(num_ == load_rownum_){
											crit_5 = 1;
										}
									} else {
										crit_5 = 1;
									}
								}
								
								loadLeftTable7.invalidateCache();
								loadLeftTable7.fetchData(crit6, new DSCallback() {
									
									@Override
									public void execute(DSResponse response, Object rawData, DSRequest request) {
										if(ObjUtil.isNotNull(loadLeftTable7.getRecordList())){
											if(loadLeftTable7.getRecordList().getLength()!=0){
												String num=	loadLeftTable7.getRecordList().get(loadLeftTable7.getRecordList().getLength()-1).getAttribute("NUM");
												String load_rownum=	loadLeftTable7.getRecordList().get(loadLeftTable7.getRecordList().getLength()-1).getAttribute("ROWNUM_");
												int num_ = Integer.parseInt(num);
												int load_rownum_ = Integer.parseInt(load_rownum);
												if(num_ == load_rownum_){
													crit_6 = 1;
												}
											} else {
												crit_6 = 1;
											}
										}
										
										loadRightTable2.invalidateCache();
										loadRightTable2.fetchData(cirt_willload, new DSCallback() {
											
											@Override
											public void execute(DSResponse response, Object rawData, DSRequest request) {
												
												if(ObjUtil.isNotNull(loadRightTable2.getRecordList())){
													if(loadLeftTable2.getRecordList().getLength()!=0){
														String num=	loadRightTable2.getRecordList().get(loadRightTable2.getRecordList().getLength()-1).getAttribute("NUM");
														String load_rownum=	loadRightTable2.getRecordList().get(loadRightTable2.getRecordList().getLength()-1).getAttribute("ROWNUM_");
														int num_ = Integer.parseInt(num);
														int load_rownum_ = Integer.parseInt(load_rownum);
														if(num_ == load_rownum_){
															a = 1;
															b = 5;
														}
													} else {
														a = 1;
														b = 5;
													}
												}
											}
										});
									}
								});
							}
						});
					} else if( b == 5){
						crit5.setAttribute("QUEUE", crit_5*number);
						crit5.setAttribute("QUEUE2", (crit_5-1)*number);
						crit_5 ++;
						
						loadLeftTable6.invalidateCache();
						loadLeftTable6.fetchData(crit5, new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								if(ObjUtil.isNotNull(loadLeftTable6.getRecordList())){
									if(loadLeftTable6.getRecordList().getLength()!=0){
										String num=	loadLeftTable6.getRecordList().get(loadLeftTable6.getRecordList().getLength()-1).getAttribute("NUM");
										String load_rownum=	loadLeftTable6.getRecordList().get(loadLeftTable6.getRecordList().getLength()-1).getAttribute("ROWNUM_");
										int num_ = Integer.parseInt(num);
										int load_rownum_ = Integer.parseInt(load_rownum);
										if(num_ == load_rownum_){
											crit_5 = 1;
										}
										
									} else {
										crit_5 = 1;
									}
								}
								crit6.setAttribute("QUEUE", crit_6*number);
								crit6.setAttribute("QUEUE2", (crit_6-1)*number);
								crit_6 ++;
								loadLeftTable7.fetchData(crit6, new DSCallback() {
									
									@Override
									public void execute(DSResponse response, Object rawData, DSRequest request) {
										if(ObjUtil.isNotNull(loadLeftTable7.getRecordList())){
											if(loadLeftTable7.getRecordList().getLength()!=0){
												String num=	loadLeftTable7.getRecordList().get(loadLeftTable7.getRecordList().getLength()-1).getAttribute("NUM");
												String load_rownum=	loadLeftTable7.getRecordList().get(loadLeftTable7.getRecordList().getLength()-1).getAttribute("ROWNUM_");
												int num_ = Integer.parseInt(num);
												int load_rownum_ = Integer.parseInt(load_rownum);
												if(num_ == load_rownum_){
													crit_6 = 1;
												}
												
											} else {
												crit_6 = 1;
											}
										}
										loadRightTable2.invalidateCache();
										cirt_willload.setAttribute("QUEUE", a*number);
										cirt_willload.setAttribute("QUEUE2", (a-1)*number);
										a++;
										loadRightTable2.fetchData(cirt_willload,new DSCallback() {
											
											@Override
											public void execute(DSResponse response, Object rawData, DSRequest request) {
												if(ObjUtil.isNotNull(loadRightTable2.getRecordList())){
													System.out
															.println(loadLeftTable2.getRecordList().getLength());
													if(loadLeftTable2.getRecordList().getLength()!=0){
														String num=	loadRightTable2.getRecordList().get(loadRightTable2.getRecordList().getLength()-1).getAttribute("NUM");
														String load_rownum=	loadRightTable2.getRecordList().get(loadRightTable2.getRecordList().getLength()-1).getAttribute("ROWNUM_");
														int num_ = Integer.parseInt(num);
														int load_rownum_ = Integer.parseInt(load_rownum);
														if(num_ == load_rownum_){
															a = 1;
															b = 2;
														}
														
													}else {
														a = 1;
													}
												}
											}
										});
									}
								});
								
								
							}
						});
					}
					else if( b == 2){
						window.removeItem(canvas3);
						canvas3.hide();
						canvas1.show();
						window.addItem(canvas1);
						b = 0;
					}
			}
		};
//		timer.scheduleRepeating(schedule_);
		Util.db_async.getRecord("DESCR", "SYS_PARAM", "WHERE CONFIG_CODE ='IMPORT_INFO' ", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
			
			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> result) {
			 s = result.get(0).get("DESCR");
			 Label text = new Label("<font style=\"color: red;font-size:22px;\">" + s +"</font>");
			 text.setPadding(1000);
			 text.setWidth(1000);
			 text.setLeft(-220);
			 text.setValign(VerticalAlignment.CENTER); 
			 text.setAlign(Alignment.CENTER);
			 text.setAnimateTime(32000); // milliseconds 
			 text.animateMove(1200, null); 
			 Canvas ca = new Canvas();
			 ca.setHeight(10);
			 ca.addChild(text);
			 window.addMember(ca);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
////		getInfo(table);
//		Canvas ca = new Canvas();
//		ca.setHeight(20);
////		ca.addChild(table);
//		Label text = new Label("重要信息"+s);
//		
//		ca.addChild(text);
//		window.addItem(ca);
		
		
//		getInfo(table);
		
		
		window.show();
		window.addCloseClickHandler(new CloseClickHandler() {

			//for smartgwt3.0
			/*@Override
			public void onCloseClick(CloseClickEvent event) {
				canvas.hide();
				window.destroy();
				timer.cancel();
			}*/

			@Override
			public void onCloseClick(CloseClientEvent event) {
				canvas.hide();
				window.destroy();
				timer.cancel();
			}
		});
		
		return window;
	}
	

	public Window getViewPanel() {
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width-15);  
		window.setHeight(height-20); 
		window.setCanDrop(false);
		window.setCanDragScroll(false);
		
		left_ds=ScreenWinDS.getInstance("V_TRANS_LOAD_SCREEN","TRANS_LOAD_JOB");
		right_ds=ScreenWinDS_.getInstance("V_TRANS_LOAD_SCREEN_","TRANS_LOAD_JOB");
		loadLeftTable = new SGTable(left_ds, "100%", "50%", false, true, false);
		loadLeftTable2 = new SGTable(left_ds, "100%", "50%", false, true, false);
		loadRightTable = new SGTable(right_ds, "100%", "100%", false, true, false);
		

		String flds = "WHSE_ID";
		String fields = " WHERE USER_ID='"+ LoginCache.getLoginUser().getUSER_ID()+"'";
		String tableName = "SYS_USER_WHSE";
		Util.db_async.getRecord(flds, tableName, fields,null,new AsyncCallback<ArrayList<HashMap<String,String>>>() {
			
			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> result) {
				crit1.addCriteria("LOAD_WHSE",result.get(0).get("WHSE_ID"));
				if(result.get(1).get("WHSE_ID") != null){
					crit2.addCriteria("LOAD_WHSE",result.get(1).get("WHSE_ID"));
				}
				
				crit1.addCriteria("OP_FLAG","M");
				crit1.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
				crit1.addCriteria("EXEC_ORG_ID",ObjUtil.ifNull(LoginCache.getLoginUser().getDEFAULT_ORG_ID()," "));
				crit1.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
				crit1.addCriteria("QUEUE",i*3);
				crit1.addCriteria("QUEUE2",(i-1)*3);
				crit1.addCriteria("A",!crit_flag);
				crit_flag = !crit_flag;
				i++;
				loadLeftTable.fetchData(crit1,new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						String num=	loadLeftTable.getRecordList().get(loadLeftTable.getRecordList().getLength()-1).getAttribute("NUM");
						String load_rownum=	loadLeftTable.getRecordList().get(loadLeftTable.getRecordList().getLength()-1).getAttribute("ROWNUM_");
						if(num.equals(load_rownum)){
						  i = 1;		
						}
						
						crit2.addCriteria("OP_FLAG","M");
						crit2.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
						crit2.addCriteria("EXEC_ORG_ID",ObjUtil.ifNull(LoginCache.getLoginUser().getDEFAULT_ORG_ID()," "));
						crit2.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
						crit2.addCriteria("QUEUE",a*3);
						crit2.addCriteria("QUEUE2",(a-1)*3);
						crit2.addCriteria("A",!crit_flag2);
						crit_flag2 = !crit_flag2;
						a++;
						loadLeftTable2.fetchData(crit2 , new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								Criteria crit = new Criteria();
								crit.addCriteria("OP_FLAG","M");
								crit.addCriteria("LOAD_STATUS",StaticRef.TRANS_EXPECT);
								crit.addCriteria("EXEC_ORG_ID",ObjUtil.ifNull(LoginCache.getLoginUser().getDEFAULT_ORG_ID()," "));
								crit.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
								crit.addCriteria("A",!crit_flag);
								loadRightTable.fetchData(crit);
							}
						});
					}
				});
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		

		
		
		
		createLeftFields(loadLeftTable);
		createLeftFields2(loadLeftTable2);
		createRightFields(loadRightTable);
		
		
		timer = new Timer() {
			
			@Override
			public void run() {
				loadLeftTable.setData(new RecordList());
				
				crit1.addCriteria("OP_FLAG","M");
				crit1.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
				crit1.addCriteria("EXEC_ORG_ID",ObjUtil.ifNull(LoginCache.getLoginUser().getDEFAULT_ORG_ID()," "));
				crit1.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
				crit1.addCriteria("QUEUE",i*3);
				crit1.addCriteria("QUEUE2",(i-1)*3);
				crit1.addCriteria("A",!crit_flag);
				crit_flag = !crit_flag;
				i++;
				
				loadLeftTable.fetchData(crit1,new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						String num=	loadLeftTable.getRecordList().get(loadLeftTable.getRecordList().getLength()-1).getAttribute("NUM");
						String load_rownum=	loadLeftTable.getRecordList().get(loadLeftTable.getRecordList().getLength()-1).getAttribute("ROWNUM_");
						if(num.equals(load_rownum)){
						  i = 1;		
						}
						loadLeftTable2.setData(new RecordList());
						
						crit2.addCriteria("OP_FLAG","M");
						crit2.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
						crit2.addCriteria("EXEC_ORG_ID",ObjUtil.ifNull(LoginCache.getLoginUser().getDEFAULT_ORG_ID()," "));
						crit2.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
						crit2.addCriteria("QUEUE",a*3);
						crit2.addCriteria("QUEUE2",(a-1)*3);
						crit2.addCriteria("A",!crit_flag2);
						crit_flag2 = !crit_flag2;
						a++;
						loadLeftTable2.fetchData(crit2 ,new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								String num=	loadLeftTable2.getRecordList().get(loadLeftTable2.getRecordList().getLength()-1).getAttribute("NUM");
								String load_rownum=	loadLeftTable2.getRecordList().get(loadLeftTable2.getRecordList().getLength()-1).getAttribute("ROWNUM_");
								if(num.equals(load_rownum)){
								  a = 1;
								}
							}
						});
						
					}
				 
				});
			}
		};
		timer.scheduleRepeating(schedule_);
		

		TabSet ScreenSet1 = new TabSet();
		ScreenSet1.setWidth("55%");
		ScreenSet1.setHeight100();
		
		Tab tab1 = new Tab("正在装货");
		VLayout layout1 = new VLayout();
		layout1.addMember(loadLeftTable);
		layout1.addMember(loadLeftTable2);
		tab1.setPane(layout1);
		ScreenSet1.addTab(tab1);
		
		TabSet ScreenSet2 = new TabSet();
		ScreenSet2.setWidth("45%");
		ScreenSet2.setHeight100();
		
		Tab tab2 = new Tab("等待装货");
		VLayout layout2 = new VLayout();
		layout2.addMember(loadRightTable);
		tab2.setPane(layout2);
		ScreenSet2.addTab(tab2);
		
		
		
		
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();
		stack.addMember(ScreenSet1);
		stack.addMember(ScreenSet2);
		
		window.addItem(stack);
		window.addCloseClickHandler(new CloseClickHandler() {

			//for smartgwt3.0
			/*@Override
			public void onCloseClick(CloseClickEvent event) {
				timer.cancel();
				window.hide();
			}*/

			@Override
			public void onCloseClick(CloseClientEvent event) {
				timer.cancel();
				window.hide();
			}
		});
		
		
		window.show();
		return window;
		
	}
	
	private void createLeftFields(SGTable loadLeftTable) {
	   loadLeftTable.setShowRowNumbers(false);
	   ListGridField load_rownum = new ListGridField("ROWNUM_","序号",30);
	   ListGridField load_status = new ListGridField("LOAD_STATUS_NAME",Util.TI18N.LOAD_STAT(),60);
	   ListGridField plate_no = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);
	   ListGridField unload_area_name = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME(),90);
	   ListGridField qnty = new ListGridField("QNTY",Util.TI18N.QNTY(),50);
	   ListGridField load_whse = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),115);
	   ListGridField DOCK = new ListGridField("DOCK_NAME","DOCK",60);
	   ListGridField start_load_time = new ListGridField("START_LOAD_TIME",Util.TI18N.START_LOAD_TIME(),100);
	   ListGridField end_load_time = new ListGridField("END_LOAD_TIME",Util.TI18N.END_LOAD_TIME(),100);
	   ListGridField NUM = new ListGridField("NUM","",30);
	   NUM.setHidden(true);
	   loadLeftTable.setFields(load_rownum,load_status,plate_no,unload_area_name,qnty,load_whse,DOCK,start_load_time,end_load_time,NUM);
    }
	
	private void createLeftFields2(SGTable loadLeftTable2) {
		   loadLeftTable2.setShowRowNumbers(false);
		   ListGridField load_rownum = new ListGridField("ROWNUM_","序号",30);
		   ListGridField load_status = new ListGridField("LOAD_STATUS_NAME",Util.TI18N.LOAD_STAT(),60);
		   ListGridField plate_no = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);
		   ListGridField unload_area_name = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME(),90);
		   ListGridField qnty = new ListGridField("QNTY",Util.TI18N.QNTY(),50);
		   ListGridField load_whse = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),115);
		   ListGridField DOCK = new ListGridField("DOCK_NAME","DOCK",60);
		   ListGridField start_load_time = new ListGridField("START_LOAD_TIME",Util.TI18N.START_LOAD_TIME(),100);
		   ListGridField end_load_time = new ListGridField("END_LOAD_TIME",Util.TI18N.END_LOAD_TIME(),100);
		   ListGridField NUM = new ListGridField("NUM","",30);
		   NUM.setHidden(true);
		   loadLeftTable2.setFields(load_rownum,load_status,plate_no,unload_area_name,qnty,load_whse,DOCK,start_load_time,end_load_time,NUM);
	    }
	
	private void createLeftFields3(SGTable loadLeftTable3) {
		   loadLeftTable3.setShowRowNumbers(false);
		   ListGridField load_rownum = new ListGridField("ROWNUM_","序号",30);
		   ListGridField load_status = new ListGridField("LOAD_STATUS_NAME",Util.TI18N.LOAD_STAT(),60);
		   ListGridField plate_no = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);
		   ListGridField unload_area_name = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME(),90);
		   ListGridField qnty = new ListGridField("QNTY",Util.TI18N.QNTY(),50);
		   ListGridField load_whse = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),115);
		   ListGridField DOCK = new ListGridField("DOCK_NAME","DOCK",60);
		   ListGridField start_load_time = new ListGridField("START_LOAD_TIME",Util.TI18N.START_LOAD_TIME(),100);
		   ListGridField end_load_time = new ListGridField("END_LOAD_TIME",Util.TI18N.END_LOAD_TIME(),100);
		   ListGridField NUM = new ListGridField("NUM","",30);
		   NUM.setHidden(true);
		   loadLeftTable3.setFields(load_rownum,load_status,plate_no,unload_area_name,qnty,load_whse,DOCK,start_load_time,end_load_time,NUM);
	    }
	
	private void createLeftFields4(SGTable loadLeftTable4) {
		   loadLeftTable4.setShowRowNumbers(false);
		   ListGridField load_rownum = new ListGridField("ROWNUM_","序号",30);
		   ListGridField load_status = new ListGridField("LOAD_STATUS_NAME",Util.TI18N.LOAD_STAT(),60);
		   ListGridField plate_no = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);
		   ListGridField unload_area_name = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME(),90);
		   ListGridField qnty = new ListGridField("QNTY",Util.TI18N.QNTY(),50);
		   ListGridField load_whse = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),115);
		   ListGridField DOCK = new ListGridField("DOCK_NAME","DOCK",60);
		   ListGridField start_load_time = new ListGridField("START_LOAD_TIME",Util.TI18N.START_LOAD_TIME(),100);
		   ListGridField end_load_time = new ListGridField("END_LOAD_TIME",Util.TI18N.END_LOAD_TIME(),100);
		   ListGridField NUM = new ListGridField("NUM","",30);
		   NUM.setHidden(true);
		   loadLeftTable4.setFields(load_rownum,load_status,plate_no,unload_area_name,qnty,load_whse,DOCK,start_load_time,end_load_time,NUM);
	    }
	
	private void createLeftFields5(SGTable loadLeftTable5) {
		   loadLeftTable5.setShowRowNumbers(false);
		   ListGridField load_rownum = new ListGridField("ROWNUM_","序号",30);
		   ListGridField load_status = new ListGridField("LOAD_STATUS_NAME",Util.TI18N.LOAD_STAT(),60);
		   ListGridField plate_no = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);
		   ListGridField unload_area_name = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME(),90);
		   ListGridField qnty = new ListGridField("QNTY",Util.TI18N.QNTY(),50);
		   ListGridField load_whse = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),115);
		   ListGridField DOCK = new ListGridField("DOCK_NAME","DOCK",60);
		   ListGridField start_load_time = new ListGridField("START_LOAD_TIME",Util.TI18N.START_LOAD_TIME(),100);
		   ListGridField end_load_time = new ListGridField("END_LOAD_TIME",Util.TI18N.END_LOAD_TIME(),100);
		   ListGridField NUM = new ListGridField("NUM","",30);
		   NUM.setHidden(true);
		   loadLeftTable5.setFields(load_rownum,load_status,plate_no,unload_area_name,qnty,load_whse,DOCK,start_load_time,end_load_time,NUM);
	    }
	private void createLeftFields6(SGTable loadLeftTable6) {
		   loadLeftTable6.setShowRowNumbers(false);
		   ListGridField load_rownum = new ListGridField("ROWNUM_","序号",30);
		   ListGridField load_status = new ListGridField("LOAD_STATUS_NAME",Util.TI18N.LOAD_STAT(),60);
		   ListGridField plate_no = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);
		   ListGridField unload_area_name = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME(),90);
		   ListGridField qnty = new ListGridField("QNTY",Util.TI18N.QNTY(),50);
		   ListGridField load_whse = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),115);
		   ListGridField DOCK = new ListGridField("DOCK_NAME","DOCK",60);
		   ListGridField start_load_time = new ListGridField("START_LOAD_TIME",Util.TI18N.START_LOAD_TIME(),100);
		   ListGridField end_load_time = new ListGridField("END_LOAD_TIME",Util.TI18N.END_LOAD_TIME(),100);
		   ListGridField NUM = new ListGridField("NUM","",30);
		   NUM.setHidden(true);
		   loadLeftTable6.setFields(load_rownum,load_status,plate_no,unload_area_name,qnty,load_whse,DOCK,start_load_time,end_load_time,NUM);
	    }
	private void createLeftFields7(SGTable loadLeftTable7) {
		   loadLeftTable7.setShowRowNumbers(false);
		   ListGridField load_rownum = new ListGridField("ROWNUM_","序号",30);
		   ListGridField load_status = new ListGridField("LOAD_STATUS_NAME",Util.TI18N.LOAD_STAT(),60);
		   ListGridField plate_no = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);
		   ListGridField unload_area_name = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME(),90);
		   ListGridField qnty = new ListGridField("QNTY",Util.TI18N.QNTY(),50);
		   ListGridField load_whse = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),115);
		   ListGridField DOCK = new ListGridField("DOCK_NAME","DOCK",60);
		   ListGridField start_load_time = new ListGridField("START_LOAD_TIME",Util.TI18N.START_LOAD_TIME(),100);
		   ListGridField end_load_time = new ListGridField("END_LOAD_TIME",Util.TI18N.END_LOAD_TIME(),100);
		   ListGridField NUM = new ListGridField("NUM","",30);
		   NUM.setHidden(true);
		   loadLeftTable7.setFields(load_rownum,load_status,plate_no,unload_area_name,qnty,load_whse,DOCK,start_load_time,end_load_time,NUM);
	    }
	private void createLeftFields8(SGTable loadLeftTable8) {
		   loadLeftTable8.setShowRowNumbers(false);
		   ListGridField load_rownum = new ListGridField("ROWNUM_","序号",30);
		   ListGridField load_status = new ListGridField("LOAD_STATUS_NAME",Util.TI18N.LOAD_STAT(),60);
		   ListGridField plate_no = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);
		   ListGridField unload_area_name = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME(),90);
		   ListGridField qnty = new ListGridField("QNTY",Util.TI18N.QNTY(),50);
		   ListGridField load_whse = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),115);
		   ListGridField DOCK = new ListGridField("DOCK_NAME","DOCK",60);
		   ListGridField start_load_time = new ListGridField("START_LOAD_TIME",Util.TI18N.START_LOAD_TIME(),100);
		   ListGridField end_load_time = new ListGridField("END_LOAD_TIME",Util.TI18N.END_LOAD_TIME(),100);
		   ListGridField NUM = new ListGridField("NUM","",30);
		   NUM.setHidden(true);
		   loadLeftTable8.setFields(load_rownum,load_status,plate_no,unload_area_name,qnty,load_whse,DOCK,start_load_time,end_load_time,NUM);
	    }
	
	private void createRightFields(SGTable loadRightTable){
		loadRightTable.setShowRowNumbers(false);
		ListGridField load_rownum = new ListGridField("ROWNUM_","序号",30);
		ListGridField load_whse_name = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),130);
		ListGridField queue_seq = new ListGridField("QUEUE_SEQ",Util.TI18N.QUEUE(),60);
		ListGridField load_status = new ListGridField("LOAD_STATUS_NAME",Util.TI18N.LOAD_STAT(),80);
		ListGridField plate_no = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),80);
		ListGridField unload_area_name = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME(),100);
		ListGridField qnty = new ListGridField("QNTY",Util.TI18N.QNTY(),60);
		ListGridField NUM = new ListGridField("NUM","",30);
		NUM.setHidden(true);
		
		loadRightTable.setFields(load_rownum,load_whse_name,queue_seq,load_status,plate_no,unload_area_name,qnty,NUM);
	}
	
	private void createRightFields2(SGTable loadRightTable2){
		loadRightTable2.setShowRowNumbers(false);
		ListGridField load_rownum = new ListGridField("ROWNUM_","序号",30);
		ListGridField load_whse_name = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),130);
		ListGridField queue_seq = new ListGridField("QUEUE_SEQ",Util.TI18N.QUEUE(),60);
		ListGridField load_status = new ListGridField("LOAD_STATUS_NAME",Util.TI18N.LOAD_STAT(),80);
		ListGridField plate_no = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),80);
		ListGridField unload_area_name = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME(),100);
		ListGridField qnty = new ListGridField("QNTY",Util.TI18N.QNTY(),60);
		ListGridField NUM = new ListGridField("NUM","",30);
		NUM.setHidden(true);
		
		loadRightTable2.setFields(load_rownum,load_whse_name,queue_seq,load_status,plate_no,unload_area_name,qnty,NUM);
	}
	
	
	
}
