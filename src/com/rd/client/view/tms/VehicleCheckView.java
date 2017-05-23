package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.dispatch.ChangedTotalQntyAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.VehicleCheckDS;
import com.rd.client.ds.tms.VehicleInspectionDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.RecordSummaryFunctionType;
import com.smartgwt.client.types.SummaryFunctionType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运输管理--运输执行--车辆检查
 * @author Administrator
 *
 */
@ClassForNameAble
public class VehicleCheckView extends SGForm implements PanelFactory {

	private DataSource loadDS;
	private DataSource vehicleDS;
	private SGTable table;
	private SGTable vehicleTable;
	private SectionStack section;
	private String canModify; 
	private Window searchWin;
	private SGPanel searchForm;
	private DynamicForm form;
	public IButton canfirmButton;
    public IButton confirmButton;
	
	/*public VehicleCheckView(String id) {
		super(id);
	}*/
	
	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		loadDS = VehicleCheckDS.getInstance("V_LOAD_HEADER1", "TRANS_LOAD_HEADER");
		
		// 主布局
		HStack Stack = new HStack();
		Stack.setWidth("76%");
		Stack.setHeight100();
		
		// STACK的左边列表
		table = new SGTable(loadDS,"100%", "100%", true, true, false);
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		createListField();
		
		section = new SectionStack();
		SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		listItem.setControls(new SGPage(table, true).initPageBtn());
		section.addSection(listItem);
		section.setWidth("99%");
		Stack.addMember(section);
		
		// STACK的右边布局
		TabSet leftTabSet = new TabSet();
		leftTabSet.setWidth("32%");
		leftTabSet.setHeight("100%");
		leftTabSet.setMargin(0);
		
		Tab tab = new Tab("车辆检查");
		tab.setPane(createMainInfo());
		leftTabSet.addTab(tab);
		Stack.addMember(leftTabSet);
		
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(Stack);
		
		
		table.addSelectionChangedHandler(new SelectionChangedHandler(){

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(vehicleTable==null)return ;
				vehicleTable.discardAllEdits();
				ListGridRecord record1=table.getSelectedRecord();
				if(record1 != null) {
					ListGridRecord[] rec = vehicleTable.getRecords();
					for(int i = 0;i < rec.length; i++) {
						rec[i].setAttribute("INSPECTION_FLAG", false);
						rec[i].setAttribute("REASON_CODE", "");
					}
					form.getItem("NOTES").setValue("");
					vehicleTable.redraw();
					if(ObjUtil.isNotNull(table.getSelectedRecord().getAttribute("CHECK_FLAG"))){
						if(table.getSelectedRecord().getAttribute("CHECK_FLAG").toString().equals("true")){
							canfirmButton.enable();
							confirmButton.disable();
						} else {
							canfirmButton.disable();
							confirmButton.enable();
						}
					}else{
						canfirmButton.disable();
						confirmButton.enable();
					}
					Util.db_async.getRecord("ITEM1_FLAG,ITEM2_FLAG,ITEM3_FLAG,ITEM4_FLAG,ITEM5_FLAG,ITEM6_FLAG,ITEM7_FLAG,ITEM8_FLAG,REASON_CODE1,REASON_CODE2,REASON_CODE3,REASON_CODE4,REASON_CODE5,REASON_CODE6,REASON_CODE7,REASON_CODE8,NOTES"," TRANS_VEH_CHECK"," where TRS_ID='"+record1.getAttribute("LOAD_NO")+"'",null, new AsyncCallback<ArrayList<HashMap<String, String>>>(){

						@Override
						public void onFailure(Throwable caught) {
							
						}

						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							if(result != null && result.size() > 0){
								ListGridRecord[] rec = vehicleTable.getRecords();
								HashMap<String, String> iter = result.get(0);
								for(int s=1; s<=rec.length;s++){
									String key = "ITEM"+s+"_FLAG";
									String key2 = "REASON_CODE"+s;
									if(iter.get(key).equals("N")){
										rec[s-1].setAttribute("INSPECTION_FLAG",false);
									}else{
										rec[s-1].setAttribute("INSPECTION_FLAG",true);
									}
									rec[s-1].setAttribute("REASON_CODE",iter.get(key2));
								}
								form.getItem("NOTES").setValue(iter.get("NOTES"));
								vehicleTable.redraw();
							}
						}
						
					});
				}
			}
			
		});
		
		return main;
	}
	
	public void createListField() {
		
    	boolean isDigitCanEdit = false;
		if(ObjUtil.ifNull(canModify,"N").equals("Y")) {
			isDigitCanEdit = true;
		}
    	ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),100);//调度单编号
		LOAD_NO.setShowGridSummary(true);
		LOAD_NO.setSummaryFunction(SummaryFunctionType.COUNT);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),70);//车牌号
		
		ListGridField STATUS=new ListGridField("STATUS",Util.TI18N.STATUS(),50);
		STATUS.setHidden(true);
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME",Util.TI18N.STATUS(),60);//状态
		ListGridField DISPATCH_STAT_NAME = new ListGridField("DISPATCH_STAT_NAME", Util.TI18N.DISPATCH_STAT_NAME(), 60);  //配车状态
		
//		ListGridField LOAD_STAT=new ListGridField("LOAD_STAT",Util.TI18N.LOAD_STAT());//装车状态
//		LOAD_STAT.setHidden(true);
//		ListGridField LOAD_STAT_NAME=new ListGridField("LOAD_STAT_NAME","装车状态",60);
		
		ListGridField VEHICLE_TYP = new ListGridField("VEHICLE_TYP_ID",Util.TI18N.VEHICLE_TYP(),70);//车辆类型
		Util.initComboValue(VEHICLE_TYP, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", " SHOW_SEQ ASC");  //车辆类型
		
		ListGridField TRANS_SRVC_ID = new ListGridField("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID(),60);
		Util.initTrsService(TRANS_SRVC_ID, "");
		
		ListGridField START_AREA_ID = new ListGridField("START_AREA_ID",Util.TI18N.AREA_NAME(),0);
		ListGridField START_AREA = new ListGridField("START_AREA_NAME",Util.TI18N.LOAD_AREA_NAME(),60);//起点区域
		START_AREA_ID.setHidden(true);
		Util.initArea(table,START_AREA,"START_AREA_ID", "START_AREA_NAME", "");
		ListGridField END_AREA_ID = new ListGridField("END_AREA_ID",Util.TI18N.AREA_NAME(),0);
		ListGridField END_AREA = new ListGridField("END_AREA_NAME",Util.TI18N.END_AREA(),60);//终点区域
		END_AREA_ID.setHidden(true);
		Util.initArea(table,END_AREA, "END_AREA_ID", "END_AREA_NAME", "");
		ListGridField DEPART_TIME = new ListGridField("DEPART_TIME", Util.TI18N.END_LOAD_TIME(), 110);  //发运时间
		//Util.initDateTime(table,DEPART_TIME);
		
		//ListGridField DONE_TIME = new ListGridField("DONE_TIME","预计回场时间", 110);  //发运时间
		//Util.initDateTime(table,DONE_TIME);
		
		ListGridField REMAIN_GROSS_W = new ListGridField("REMAIN_GROSS_W","余量",60);//余量
		REMAIN_GROSS_W.setCanEdit(false);
		
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID_NAME(), 80);  //供应商
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_ID", Util.TI18N.SUPLR_NAME(), 70);  //供应商
		Util.initOrgSupplier(SUPLR_NAME, "");
		ListGridField DRIVER1 = new ListGridField("DRIVER1", Util.TI18N.DRIVER1(), 60);  //司机
		ListGridField MOBILE1 = new ListGridField("MOBILE1", Util.TI18N.MOBILE(), 90);  //电话
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY","总数量",60);//总数量
		TOT_QNTY.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		TOT_QNTY.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_QNTY.setShowGroupSummary(true); 
		TOT_QNTY.setAlign(Alignment.RIGHT);
		TOT_QNTY.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_QNTY.addEditorExitHandler(new ChangedTotalQntyAction(table, "TOT_QNTY", Util.TI18N.TOT_QNTY()));
		}
		ListGridField TOT_QNTY_EACH = new ListGridField("TOT_QNTY_EACH",Util.TI18N.R_EA(),60);//总数量
		TOT_QNTY_EACH.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		TOT_QNTY_EACH.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_QNTY_EACH.setShowGroupSummary(true); 
		TOT_QNTY_EACH.setAlign(Alignment.RIGHT);
		TOT_QNTY_EACH.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_QNTY_EACH.addEditorExitHandler(new ChangedTotalQntyAction(table, "TOT_QNTY_EACH", Util.TI18N.R_EA()));
		}
		ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W",Util.TI18N.TOT_GROSS_W(),60);//总毛重
		TOT_GROSS_W.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		TOT_GROSS_W.setSummaryFunction(SummaryFunctionType.SUM); 
		TOT_GROSS_W.setShowGroupSummary(true); 
		TOT_GROSS_W.setAlign(Alignment.RIGHT);
		TOT_GROSS_W.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_GROSS_W.addEditorExitHandler(new ChangedTotalQntyAction(table, "TOT_GROSS_W", Util.TI18N.TOT_GROSS_W()));
		}
		ListGridField TOT_VOL = new ListGridField("TOT_VOL",Util.TI18N.TOT_VOL(),60);//总体积
		TOT_VOL.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_VOL.setAlign(Alignment.RIGHT);
		TOT_VOL.setShowGroupSummary(true); 
		TOT_VOL.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_VOL.addEditorExitHandler(new ChangedTotalQntyAction(table, "TOT_VOL", Util.TI18N.TOT_VOL()));
		}
		final ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),65);//备注
		
		ListGridField CHECK_FLAG = new ListGridField("CHECK_FLAG","车检标记",60);
		CHECK_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		table.setFields(LOAD_NO,STATUS,STATUS_NAME,CHECK_FLAG, DISPATCH_STAT_NAME, TRANS_SRVC_ID, START_AREA_ID,START_AREA, END_AREA_ID, END_AREA, SUPLR_NAME, PLATE_NO,VEHICLE_TYP
				,DRIVER1, MOBILE1, REMAIN_GROSS_W,DEPART_TIME,TOT_QNTY,TOT_QNTY_EACH,NOTES, TOT_VOL, TOT_GROSS_W,EXEC_ORG_ID_NAME);
		
	}

	private VLayout createMainInfo() {
		VLayout layOut = new VLayout();
		layOut.setWidth("100%");
		layOut.setHeight(280);
		
		vehicleDS = VehicleInspectionDS.getInstance("TRANS_VEH_CHECK");
		vehicleTable = new SGTable(vehicleDS);
		vehicleTable.setShowFilterEditor(false);
		vehicleTable.setEditEvent(ListGridEditEvent.CLICK);
		vehicleTable.setCanEdit(true);
		vehicleTable.setShowRowNumbers(true);
		
    	Util.db_async.getRecord("NAME_C","bas_codes"," where prop_code = 'VEH_CHK'", null, new AsyncCallback<ArrayList<HashMap<String, String>>>(){

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> result) {
				if(result != null && result.size() > 0){
					ListGridField INSPECTION_NAME = new ListGridField("INSPECTION_NAME","检查项目",80);
					INSPECTION_NAME.setCanEdit(false);
					ListGridField INSPECTION_FLAG = new ListGridField("INSPECTION_FLAG","合格",50);
					INSPECTION_FLAG.addRecordClickHandler(new RecordClickHandler() {
						
						@Override
						public void onRecordClick(RecordClickEvent event) {
							Record rec = event.getRecord();
							if(rec.getAttribute("INSPECTION_FLAG")==null){
								rec.setAttribute("INSPECTION_FLAG", true);
							}else if(rec.getAttribute("INSPECTION_FLAG").equals("true")){
								rec.setAttribute("INSPECTION_FLAG", false);
							}else{
								rec.setAttribute("INSPECTION_FLAG", true);
							}
						}
					});
					INSPECTION_FLAG.setType(ListGridFieldType.BOOLEAN);
					ListGridField REASON_CODE = new ListGridField("REASON_CODE","不合格原因",100);
					Util.initCodesComboValue(REASON_CODE,"OFFGRADE_REASON");
//					Util.initComboValue(REASON_CODE, "BAS_CODES", "NAME_C","NAME_E"," where PROP_CODE='OFF_GRADE'","");
					String NAME = "";
					ListGridRecord[] date = new ListGridRecord[result.size()];
					for(int i =0;i<result.size();i++){
						NAME = result.get(i).get("NAME_C");
						date[i]=createRecord(NAME,false,"");
					}
					vehicleTable.setFields(INSPECTION_NAME,INSPECTION_FLAG,REASON_CODE);
					vehicleTable.setRecords(date);
				}
			}
    	});
    	form = new DynamicForm();
		form.setWidth("99%");
		form.setHeight(60);
		form.setPadding(4);
		
		TextAreaItem NOTES = new TextAreaItem("NOTES","备注");
		NOTES.setStartRow(true);
		NOTES.setColSpan(4);
		NOTES.setHeight(50);
		NOTES.setWidth(260);
		NOTES.setTitleOrientation(TitleOrientation.TOP);
		NOTES.setTitleVAlign(VerticalAlignment.TOP);
		
		form.setItems(NOTES);
		
		layOut.addMember(vehicleTable);
		layOut.addMember(form);
		return layOut;
	}
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		strip.setWidth("100%");
		strip.setHeight("20");
		strip.setPadding(2);
		strip.setSeparatorSize(12);

		IButton searchButton = createUDFBtn(Util.BI18N.SEARCHLOAD(),StaticRef.ICON_SEARCH,TrsPrivRef.VehicleCheck);
		strip.addMember(searchButton);
		
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(loadDS, createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				} else {
					searchWin.show();
				}
			}
		});
		
		confirmButton = createUDFBtn("确认",StaticRef.ICON_CONFIRM,TrsPrivRef.VehicleCheck_P1_01);
		confirmButton.disable();
		strip.addMember(confirmButton);
		
		confirmButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(table==null)return;
					if(ObjUtil.isNotNull(table.getSelectedRecord().getAttribute("CHECK_FLAG")) && table.getSelectedRecord().getAttribute("CHECK_FLAG").equals("true")){
						SC.say("车辆已检查！");
						return;
					}else{
						final ListGridRecord[] records = vehicleTable.getRecords();
						for(int i=0;i<records.length;i++){
							if(vehicleTable.getRecords()[i].getAttribute("INSPECTION_FLAG").equals("false") && !ObjUtil.isNotNull(vehicleTable.getEditedRecord(i).getAttribute("REASON_CODE"))){
								SC.say("车检不合格项目必须选择不合格原因！");
								return;
							}
							if(vehicleTable.getRecords()[i].getAttribute("INSPECTION_FLAG").equals("true") && ObjUtil.isNotNull(vehicleTable.getEditedRecord(i).getAttribute("REASON_CODE"))){
								SC.say("车检合格项目不需要选择不合格原因！");
								return;
							}
						}
						int num=0;
						for(int i=0;i<records.length;i++){
							if(vehicleTable.getRecords()[i].getAttribute("INSPECTION_FLAG").equals("true")){
								num = num+1;
							}
						}
						if(records != null && records.length > 0) {
							String loadno = table.getSelectedRecord().getAttribute("LOAD_NO");
							ArrayList<String> sqlList = new ArrayList<String>();
							StringBuffer sf = new StringBuffer();
							sf.append("insert into TRANS_VEH_CHECK(TRS_ID");
								for(int j=1;j<=records.length;j++){
									sf.append(",ITEM"+j+"_FLAG");
								}
								for(int j=1;j<=records.length;j++){
									sf.append(",REASON_CODE"+j);
								}
								sf.append(",ADDTIME,ADDWHO,EDITTIME,EDITWHO,NOTES) values(");
								sf.append("'"+loadno+"'");
								for(int n=0;n<records.length;n++){
									if(vehicleTable.getEditValue(n, "INSPECTION_FLAG")==null){
										sf.append(",'N'");
									}else{
										sf.append(",'Y'");
									}
								}
								for(int n=0;n<records.length;n++){
									sf.append(",'"+vehicleTable.getEditedRecord(n).getAttribute("REASON_CODE")+"'");
								}
								sf.append(",sysdate,");
								sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"'");
								sf.append(",sysdate,");
								sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"'");
								sf.append(",'"+form.getItem("NOTES").getValue()+"'");
								sf.append(")");
								sqlList.add(sf.toString());
								StringBuffer sf1 = new StringBuffer();
								sf1.append("update TRANS_LOAD_HEADER set CHECK_FLAG='Y'");
								if(num==records.length){
									sf1.append(",QUALIFIED_FLAG='N'");
								}else{
									sf1.append(",QUALIFIED_FLAG='Y'");
								}
								sf1.append(" where LOAD_NO = '"+table.getSelectedRecord().getAttribute("LOAD_NO")+"'");
								sqlList.add(sf1.toString());
								Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									
								}

								@Override
								public void onSuccess(String result) {
//									ListGridRecord[] rec = vehicleTable.getRecords();
//									for(int i = 0;i < rec.length; i++) {
//										rec[i].setAttribute("INSPECTION_FLAG", false);
//										rec[i].setAttribute("REASON_CODE", "");
//									}
//									form.getItem("NOTES").setValue("");
//									vehicleTable.redraw();
//									Util.db_async.getRecord("ITEM1_FLAG,ITEM2_FLAG,ITEM3_FLAG,ITEM4_FLAG,ITEM5_FLAG,ITEM6_FLAG,ITEM7_FLAG,ITEM8_FLAG,REASON_CODE1,REASON_CODE2,REASON_CODE3,REASON_CODE4,REASON_CODE5,REASON_CODE6,REASON_CODE7,REASON_CODE8,NOTES"," TRANS_VEH_CHECK"," where TRS_ID='"+table.getSelectedRecord().getAttribute("LOAD_NO")+"'",null, new AsyncCallback<ArrayList<HashMap<String, String>>>(){

//										@Override
//										public void onFailure(Throwable caught) {
//											
//										}

//										@Override
//										public void onSuccess(ArrayList<HashMap<String, String>> result) {
											MSGUtil.showOperSuccess();
//											if(result != null && result.size() > 0){
//												ListGridRecord[] rec = vehicleTable.getRecords();
//												HashMap<String, String> iter = result.get(0);
//												for(int s=1; s<=rec.length;s++){
//													String key = "ITEM"+s+"_FLAG";
//													String key2 = "REASON_CODE"+s;
//													if(iter.get(key).equals("N")){
//														rec[s-1].setAttribute("INSPECTION_FLAG",false);
//													}else{
//														rec[s-1].setAttribute("INSPECTION_FLAG",true);
//													}
//													rec[s-1].setAttribute("REASON_CODE",iter.get(key2));
//												}
//												form.getItem("NOTES").setValue(iter.get("NOTES"));
												table.getSelectedRecord().setAttribute("CHECK_FLAG",true);
												table.redraw();
												confirmButton.disable();
												canfirmButton.enable();
//												vehicleTable.invalidateCache();
//												vehicleTable.discardAllEdits();
//												vehicleTable.redraw();
//											}
//										}
										
//									});
								}
								
							});
						}else{
							return;
						}
					}
					
				
			}
		});
		
		canfirmButton = createUDFBtn("取消确认",StaticRef.ICON_CONFIRM,TrsPrivRef.VehicleCheck_P1_02);
		canfirmButton.disable();
		strip.addMember(canfirmButton);
		
		canfirmButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(table==null)return;
					final ListGridRecord[] records = vehicleTable.getRecords();
					if(records != null && records.length > 0) {
						String loadno = table.getSelectedRecord().getAttribute("LOAD_NO");
						ArrayList<String> sqlList = new ArrayList<String>();
						StringBuffer sf = new StringBuffer();
						sf.append("delete  from TRANS_VEH_CHECK where TRS_ID='"+loadno+"'");

						sqlList.add(sf.toString());
						
						StringBuffer sf1= new StringBuffer();
						sf1.append("update TRANS_LOAD_HEADER set CHECK_FLAG='N',QUALIFIED_FLAG='Y' where LOAD_NO='"+loadno+"'");
	
						sqlList.add(sf1.toString());
						
						Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								SC.say("操作失败！");
							}

							@Override
							public void onSuccess(String result) {
								table.getSelectedRecord().setAttribute("CHECK_FLAG",false);
								table.redraw();
								
								ListGridRecord[]  list=vehicleTable.getRecords();
								for(int s=1; s<=list.length;s++){
									list[s-1].setAttribute("INSPECTION_FLAG",false);
									list[s-1].setAttribute("REASON_CODE","");
								}
								form.getItem("NOTES").setValue("");
								vehicleTable.redraw();
								canfirmButton.disable();
								confirmButton.enable();
								MSGUtil.showOperSuccess();
								
							}
						});
					}else{
						return;
					}
			}
		});
		
		
		
		strip.setMembersMargin(5);
	}
	
	private DynamicForm createSerchForm(DynamicForm searchForm) {
		searchForm.setDataSource(loadDS);
		searchForm.setAutoFetchData(false);
		searchForm.setWidth100();
		searchForm.setCellPadding(2);
		
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		Util.initCustComboValue(CUSTOMER, "");
	
		SGText LOAD_NO=new SGText("LOAD_NO",Util.TI18N.LOAD_NO());//调度单号

		SGText CUSTOM_ODR_NO_NAME=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号
		  
		SGText SHPM_NO=new SGText("SHPM_NO",Util.TI18N.SHPM_NO());//

		//2
		SGCombo STATUS_FROM=new SGCombo("STATUS_FROM", Util.TI18N.STATUS(),true);//状态 从 到 
		SGCombo STATUS_TO=new SGCombo("STATUS_TO", "到");
		Util.initStatus(STATUS_FROM, StaticRef.LOADNO_STAT, "35");
		Util.initStatus(STATUS_TO, StaticRef.LOADNO_STAT, "40");
		
		//二级窗口 SUPLR_ID_NAME
		SGCombo SUPLR_ID =new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());//供应商
		Util.initSupplier(SUPLR_ID, "");
		
		SGText PLATE_NO=new SGText("PLATE_NO",Util.TI18N.PLATE_NO());//

		//3
		ComboBoxItem START_AREA=new ComboBoxItem("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME());//起点区域
		START_AREA.setWidth(FormUtil.Width);
		START_AREA.setTitleOrientation(TitleOrientation.TOP);
		TextItem START_AREA_ID=new TextItem("START_AREA_ID", Util.TI18N.START_ARAE());
		Util.initArea(START_AREA, START_AREA_ID);
		
		ComboBoxItem END_AREA=new ComboBoxItem("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME());//
		END_AREA.setWidth(FormUtil.Width);
		END_AREA.setTitleOrientation(TitleOrientation.TOP);
		TextItem END_AREA_ID=new TextItem("END_AREA_ID", Util.TI18N.END_AREA());
		Util.initArea(END_AREA, END_AREA_ID);

		SGDateTime ORD_ADDTIME_FROM = new SGDateTime("ADDTIME", Util.TI18N.ORD_ADDTIME_FROM());//创建时间 从  到  
		SGDateTime ORD_ADDTIME_TO = new SGDateTime("ADDTIME_TO", "到");
		
		//4
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),true);
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME()); 
		
		SGText ADDWHO=new SGText("ADDWHO",Util.TI18N.ADDWHO());//制单人
		
		SGCheck C_ORG_FLAG=new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());// 包含下级机构	
		C_ORG_FLAG.setWidth(120);
//		C_ORG_FLAG.setColSpan(3);
		C_ORG_FLAG.setValue(true);
		
		//SGCheck HISTORY_FLAG=new SGCheck("HISTORY_FLAG", "查看历史数据");// 包含历史数据
		
		SGCombo ROUTE_ID = new SGCombo("ROUTE_ID", Util.TI18N.ROUTE_NAME());	//业务类型
		Util.initComboValue(ROUTE_ID, "BAS_ROUTE_HEAD", "ID", "ROUTE_NAME", "", " order by show_seq asc");
		
		SGCombo CHECK_FLAG=new SGCombo("CHECK_FLAG","车检标记");//LOAD_STAT
		LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
		map.put("", "");
		map.put("N", "未车检");
		map.put("Y", "已车检");
		CHECK_FLAG.setValueMap(map);
		CHECK_FLAG.setDefaultValues("N");
		
		SGCombo QUALIFIED_FLAG=new SGCombo("QUALIFIED_FLAG","合格标记");
		LinkedHashMap<String,String> map1=new LinkedHashMap<String,String>();
		map1.put("", "");
		map1.put("Y", "不合格");
		map1.put("N", "合格");
		QUALIFIED_FLAG.setValueMap(map1);
		
		searchForm.setItems(CUSTOMER,LOAD_NO,CUSTOM_ODR_NO_NAME,SHPM_NO,
				STATUS_FROM,STATUS_TO,SUPLR_ID,PLATE_NO,
				START_AREA,END_AREA,ORD_ADDTIME_FROM,ORD_ADDTIME_TO,
				EXEC_ORG_ID,EXEC_ORG_ID_NAME,
				ROUTE_ID,
				ADDWHO,CHECK_FLAG,QUALIFIED_FLAG,C_ORG_FLAG);
		
		return searchForm;
	}
	
	@Override
	public void initVerify() {
		
	}

	@Override
	public void onDestroy() {
		
	}
	
	private ListGridRecord createRecord(String INSPECTION_NAME,boolean INSPECTION_FLAG,String REASON_CODE){  
        ListGridRecord record = new ListGridRecord(); 
        record.setAttribute("INSPECTION_NAME", INSPECTION_NAME);  
        record.setAttribute("INSPECTION_FLAG", INSPECTION_FLAG); 
        record.setAttribute("REASON_CODE", REASON_CODE);  
        return record;  
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		VehicleCheckView view = new VehicleCheckView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}
}
