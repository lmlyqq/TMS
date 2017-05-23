package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.settlement.ApproveCustomerDS;
import com.rd.client.action.tms.DeleteApproveAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.action.SaveMultiFormAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.tms.ApproveHeadDS;
import com.rd.client.ds.tms.ApproveSetDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运输管理--运输执行--单据审批设置
 * @author Administrator
 *
 */
@ClassForNameAble
public class ApproveSetView extends SGForm implements PanelFactory {

	private DataSource approveSetDS;
	private DataSource approveHeadDS;
	private DataSource approveCustomerDS;
	private SGTable table;
	private SGTable headTbale;
	private SGTable customerTbale;
	private SectionStack section;
	public DynamicForm pageForm; 
	public SGPanel panel;
	private Window searchWin;
	private SGPanel searchForm;
	private ValuesManager vm;
	
	//private HashMap<String, Boolean> map1;
	
	/*public ApproveSetView(String id) {
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
		vm  = new ValuesManager();
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		ToolStrip downtoolStrip = new ToolStrip();
		downtoolStrip.setAlign(Alignment.RIGHT);
		
		ToolStrip downtoolStrip2 = new ToolStrip();
		downtoolStrip2.setAlign(Alignment.RIGHT);
		
		approveHeadDS = ApproveHeadDS.getInstance("SYS_APPROVE_HEAD");
		approveSetDS = ApproveSetDS.getInstance("SYS_APPROVE_SET");
		approveCustomerDS = ApproveCustomerDS.getInstance("BAS_CUSTOMER");
		
		HStack Stack = new HStack();
		Stack.setWidth("100%");
		Stack.setHeight("40%");
		
		headTbale = new SGTable(approveHeadDS, "100%", "100%", true, true, false);
		headTbale.setCanEdit(false);
		headTbale.setShowFilterEditor(false);
		createListField();
		
		section = new SectionStack();
		SectionStackSection listItem = new SectionStackSection("单据类型");
		listItem.setItems(headTbale);
		listItem.setExpanded(true);
		//pageForm = new SGPage(headTbale, true).initPageBtn();
		//listItem.setControls(pageForm);
		section.addSection(listItem);
		section.setWidth("35%");
		Stack.addMember(section);
		
		TabSet leftTabSet = new TabSet();
		leftTabSet.setWidth("65%");
		
		Tab tab = new Tab("主信息");
		tab.setPane(createMainInfo());
		leftTabSet.addTab(tab);
		Stack.addMember(leftTabSet);
		
		
		final TabSet bottomTabSet = new TabSet();
		bottomTabSet.setWidth100();
		bottomTabSet.setHeight("60%");	
		
		if(isPrivilege(TrsPrivRef.ApproveSet_P0)){
			Tab tab1 = new Tab("审批设置");
			VLayout layout=new VLayout();
			table=new SGTable(approveSetDS,"100%","100%");
			table.setCanEdit(true);
			table.setEditEvent(ListGridEditEvent.DOUBLECLICK);
			table.setShowFilterEditor(false);
			table.setShowRowNumbers(false);
			createMainfo(table);
			layout.addMember(table);
			tab1.setPane(layout);
			downBtnWidget(downtoolStrip);
			layout.addMember(downtoolStrip);
			bottomTabSet.addTab(tab1);
		}
		if(isPrivilege(TrsPrivRef.ApproveSet_P2)){
			Tab tab2 = new Tab("指定客户");
			VLayout layout2=new VLayout();
			customerTbale=new SGTable(approveCustomerDS,"100%","100%");
			customerTbale.setCanEdit(true);
			customerTbale.setEditEvent(ListGridEditEvent.DOUBLECLICK);
			customerTbale.setShowFilterEditor(false);
			customerTbale.setShowRowNumbers(true);
			customerTbale.setEditEvent(ListGridEditEvent.CLICK);
			createMainfo2(customerTbale);
			layout2.addMember(customerTbale);
			tab2.setPane(layout2);
			downBtnWidget2(downtoolStrip2);
			layout2.addMember(downtoolStrip2);
			bottomTabSet.addTab(tab2);
		}
		
		bottomTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				String title=event.getTab().getTitle();
				if(title.equals("指定客户")){
					customerTbale.invalidateCache();
					Criteria cri = new Criteria();
					cri.addCriteria("OP_FLAG","M");
					customerTbale.fetchData(cri,new DSCallback() {

						@Override
						public void execute(DSResponse response,
								Object rawData, DSRequest request) {
							customerTbale.discardAllEdits();
							if(headTbale.getSelectedRecord()==null) return;
		    				ListGridRecord[] rec = customerTbale.getRecords();
		    				for(int i=0;i<rec.length;i++){
		    					rec[i].setAttribute("USE_FLAG", false);
		    				}
		    				customerTbale.redraw();
		    				Util.db_async.getRecord("CUSTOMER_ID"," SYS_APPROVE_CUSTOMER"," where HEAD_ID='"+headTbale.getSelectedRecord().getAttribute("ID")+"'", null, new AsyncCallback<ArrayList<HashMap<String, String>>>(){

								@Override
								public void onFailure(Throwable caught) {
									
								}

								@Override
								public void onSuccess(ArrayList<HashMap<String, String>> result) {
									if(result != null && result.size() > 0){
		    							ListGridRecord[] rec = customerTbale.getRecords();
		    							for(int i=0;i<result.size();i++){
		    								HashMap<String, String> map = result.get(i);
		    								String key = "";
		    								for(int j=0;j<map.size();j++){
		    									key = map.get("CUSTOMER_ID");
		    									for(int n=0;n<rec.length;n++){
		    										if (key.equals(rec[n].getAttribute("ID"))) {									
		    											rec[n].setAttribute("USE_FLAG", true);
		    											break;
		    										}
		    									}
		    								}
		    							}
		    							customerTbale.redraw();
		    						}
								}
		    				});
						}
					});
				}
			}
		});
		
		
		headTbale.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(headTbale.getSelectedRecord()==null) return;
				Record record = event.getRecord();
				vm.editRecord(record);
                vm.setValue("OP_FLAG", StaticRef.MOD_FLAG);
                initSaveBtn();
                if(bottomTabSet.getSelectedTabNumber()==0){
                	table.discardAllEdits();
    				ListGridRecord[] rec = table.getRecords();
    				for(int i=0;i<rec.length;i++){
    					rec[i].setAttribute("USE_FLAG", false);
    					rec[i].setAttribute("ROLE_ID", "");
    				}
    				table.redraw();
    				Util.db_async.getRecord("SHOW_SEQ,ROLE_ID"," SYS_APPROVE_SET"," where HEAD_ID='"+headTbale.getSelectedRecord().getAttribute("ID")+"' order by SHOW_SEQ ", null, new AsyncCallback<ArrayList<HashMap<String, String>>>(){

    					@Override
    					public void onFailure(Throwable caught) {
    							
    					}

    					@Override
    					public void onSuccess(ArrayList<HashMap<String, String>> result) {
    						if(result != null && result.size() > 0){
    							ListGridRecord[] rec = table.getRecords();
    							for(int i=0;i<result.size();i++){
    								HashMap<String, String> map = result.get(i);
    								String key = "",value = "";
    								for(int j=0;j<map.size();j++){
    									key = map.get("SHOW_SEQ");
    									value = map.get("ROLE_ID");
    									for(int n=0;n<rec.length;n++){
    										if (key.equals(rec[n].getAttribute("SHOW_SEQ"))) {									
    											rec[n].setAttribute("USE_FLAG", true);
    											if(ObjUtil.isNotNull(value)) {
    												rec[n].setAttribute("ROLE_ID", value);
    											}
    											break;
    										}
    									}
    								}
    							}
    							table.redraw();
    						}
    					}
    				});
                }else if(bottomTabSet.getSelectedTabNumber()==1){	
                	customerTbale.discardAllEdits();
    				ListGridRecord[] rec = customerTbale.getRecords();
    				for(int i=0;i<rec.length;i++){
    					rec[i].setAttribute("USE_FLAG", false);
    				}
    				customerTbale.redraw();
    				Util.db_async.getRecord("CUSTOMER_ID"," SYS_APPROVE_CUSTOMER"," where HEAD_ID='"+headTbale.getSelectedRecord().getAttribute("ID")+"'", null, new AsyncCallback<ArrayList<HashMap<String, String>>>(){

						@Override
						public void onFailure(Throwable caught) {
							
						}

						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							if(result != null && result.size() > 0){
    							ListGridRecord[] rec = customerTbale.getRecords();
    							for(int i=0;i<result.size();i++){
    								HashMap<String, String> map = result.get(i);
    								String key = "";
    								for(int j=0;j<map.size();j++){
    									key = map.get("CUSTOMER_ID");
    									for(int n=0;n<rec.length;n++){
    										if (key.equals(rec[n].getAttribute("ID"))) {									
    											rec[n].setAttribute("USE_FLAG", true);
    											break;
    										}
    									}
    								}
    							}
    							customerTbale.redraw();
    						}
						}
    					
    				});
                }
			}
		});
		
		headTbale.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(headTbale.getSelectedRecord()==null){
					return;
				}
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
			}
			
		});
		
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(Stack);
		main.addMember(bottomTabSet);
		initVerify();
		
		return main;
	}
	
	public void createListField() {
		ListGridField DOC_NO = new ListGridField("DOC_NO","审批单据",100);
		Util.initComboValue(DOC_NO, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'APPROVE_DOC'", " SHOW_SEQ ASC");
		ListGridField DOC_DESCR = new ListGridField("DOC_DESCR","内容描述",200);
		headTbale.setFields(DOC_NO,DOC_DESCR);
	}
	
	private Canvas createMainInfo() {
		HLayout hLayout =new HLayout();
		hLayout.setHeight("100%");
		hLayout.setWidth("40%");
		panel = new SGPanel();
		panel.setWidth("40%");
		SGCombo DOC_NO = new SGCombo("DOC_NO", ColorUtil.getRedTitle("审批单据"), true);
		Util.initComboValue(DOC_NO, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'APPROVE_DOC'", " SHOW_SEQ ASC");
		
		SGLText DOC_DESCR = new SGLText("DOC_DESCR", "内容描述");
		panel.setItems(DOC_NO,DOC_DESCR);
		hLayout.addMember(panel);
		vm.addMember(panel);
		return panel;
	}
	
	private void createMainfo(SGTable table) {
		ListGridField USE_FLAG = new ListGridField("USE_FLAG", "选择", 60);
		USE_FLAG.setType(ListGridFieldType.BOOLEAN);
		USE_FLAG.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				Record record=event.getRecord();
				if(record==null){
					return;
				}
				if(record.getAttribute("USE_FLAG")!=null&&("true").equals(record.getAttribute("USE_FLAG"))){
					record.setAttribute("USE_FLAG",false);
				}else{
					record.setAttribute("USE_FLAG",true);
				}
			}
		});
		
		
		ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ", "审批顺序", 80);
		SHOW_SEQ.setCanEdit(false);
		ListGridField SHOW_NODE = new ListGridField("SHOW_NODE", "审核节点", 80);
		SHOW_NODE.setCanEdit(false);
		ListGridField ROLE_ID = new ListGridField("ROLE_ID", "审批角色", 160);
		Util.initComboValue(ROLE_ID, "SYS_ROLE", "ID","ROLE_NAME","","");
		
		ListGridRecord[] date = new ListGridRecord[]{
			createRecord(false,"1","一级审批",""),
			createRecord(false,"2","二级审批",""),
			createRecord(false,"3","三级审批",""),
			createRecord(false,"4","四级审批",""),
			createRecord(false,"5","五级审批",""),
			createRecord(false,"6","六级审批",""),
		};
		
		table.setEditEvent(ListGridEditEvent.CLICK);
		table.setFields(USE_FLAG,SHOW_SEQ,SHOW_NODE,ROLE_ID);
		table.setRecords(date);
	}

	private void createMainfo2(final SGTable table) {
		ListGridField USE_FLAG = new ListGridField("USE_FLAG", "选择", 40);
		USE_FLAG.setType(ListGridFieldType.BOOLEAN);
		USE_FLAG.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				Record rec = event.getRecord();
				if(rec.getAttribute("USE_FLAG")==null){
					rec.setAttribute("USE_FLAG", true);
				}else if(rec.getAttribute("USE_FLAG").equals("true")){
					rec.setAttribute("USE_FLAG", false);
				}else{
					rec.setAttribute("USE_FLAG", true);
				}
			}
		});
		ListGridField ID = new ListGridField("ID", "ID", 120);
		ID.setCanEdit(false);
		ID.setHidden(true);
		ListGridField CUSTOMER_CODE = new ListGridField("CUSTOMER_CODE", "客户代码", 120);
		CUSTOMER_CODE.setCanEdit(false);
		ListGridField CUSTOMER_CNAME = new ListGridField("SHORT_NAME", "项目名称", 200);
		CUSTOMER_CNAME.setCanEdit(false);
		table.setFields(USE_FLAG,ID,CUSTOMER_CODE,CUSTOMER_CNAME);
		
		final Menu menu = new Menu();
	    menu.setWidth(140);
		MenuItem allSelect = new MenuItem("全选",StaticRef.ICON_CONFIRM);
		allSelect.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				ListGridRecord[] rec = customerTbale.getRecords();
				for(int i=0;i<rec.length;i++){
					rec[i].setAttribute("USE_FLAG", true);
				}
				customerTbale.redraw();
			}
		});
		menu.addItem(allSelect);
		MenuItem unselect = new MenuItem("反选",StaticRef.ICON_CANCEL);
		unselect.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				ListGridRecord[] rec = customerTbale.getRecords();
				for(int i=0;i<rec.length;i++){
					rec[i].setAttribute("USE_FLAG", false);
				}
				customerTbale.redraw();
			}
		});
		menu.addItem(unselect);
		
		table.addShowContextMenuHandler(new ShowContextMenuHandler() {
            public void onShowContextMenu(ShowContextMenuEvent event) {
            	menu.showContextMenu();
                event.cancel();
            }
        });
		
	}
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		strip.setWidth("100%");
		strip.setHeight("20");
		strip.setPadding(2);
		strip.setSeparatorSize(12);
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN,TrsPrivRef.ApproveSet);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if(searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(approveHeadDS, createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        });
        
        IButton newButton = createBtn(StaticRef.CREATE_BTN,TrsPrivRef.ApproveSet_P1_03);
        newButton.addClickHandler(new NewMultiFormAction(vm,cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.ApproveSet_P1_04);
        saveButton.addClickHandler(new SaveMultiFormAction(headTbale,vm, check_map, this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,TrsPrivRef.ApproveSet_P1_05);
        delButton.addClickHandler(new DeleteApproveAction(headTbale,vm));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.ApproveSet_P1_06);
        canButton.addClickHandler(new CancelMultiFormAction(headTbale,vm,this));
        
        add_map.put(TrsPrivRef.ApproveSet_P1_03, newButton);
        del_map.put(TrsPrivRef.ApproveSet_P1_05, delButton);
        save_map.put(TrsPrivRef.ApproveSet_P1_04, saveButton);
        save_map.put(TrsPrivRef.ApproveSet_P1_06, canButton);
        
        enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        enableOrDisables(save_map, false);
        
        strip.setMembersMargin(4);
        strip.setMembers(searchButton, newButton, saveButton, delButton, canButton);
		
	}
	
	public void downBtnWidget(ToolStrip downtoolStrip) {
		downtoolStrip.setWidth("100%");
		downtoolStrip.setHeight("20");
		downtoolStrip.setPadding(2);
		downtoolStrip.setSeparatorSize(12);
		downtoolStrip.addSeparator();
		downtoolStrip.setMembersMargin(4);
		downtoolStrip.setAlign(Alignment.RIGHT);
		
		IButton saveButton1 = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.ApproveSet_P1_01);
		saveButton1.addClickHandler( new ClickHandler(){

			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if(headTbale.getSelectedRecord()==null){
					return;
				}else{
					int[] record = table.getAllEditRows();
					String n="";
					for(int j=0;j<record.length;j++){
						n=n+record[j]+",";
					}
					//System.out.println(n);
					ListGridRecord[] records=table.getRecords();
					ArrayList<String> sqlList = new ArrayList<String>();
					StringBuffer sf = new StringBuffer();
					sf.append("delete from SYS_APPROVE_SET where HEAD_ID='"+headTbale.getSelectedRecord().getAttribute("ID")+"'");
					sqlList.add(sf.toString());
					for(int i = 0; i < records.length; i++) {
						Record rec = records[i];
						if(n.contains(i+",")){
							if(table.getEditedRecord(i).getAttribute("USE_FLAG").equals("true")){
								sf = new StringBuffer();
								sf.append("insert into SYS_APPROVE_SET(HEAD_ID,DOC_NAME,SHOW_SEQ,ROLE_ID,ADDTIME,ADDWHO,EDITTIME,EDITWHO)");
								sf.append(" values('"+headTbale.getSelectedRecord().getAttribute("ID")+"',");
								sf.append("'"+panel.getItem("DOC_NO").getDisplayValue()+"',");
								if(rec.getAttribute("SHOW_SEQ")==null){
									sf.append("'"+table.getSelection()[i].getAttribute("SHOW_SEQ")+"'");
								}else{
									sf.append("'"+rec.getAttribute("SHOW_SEQ")+"',");
								}
								if(rec.getAttribute("ROLE_ID")==null){
									sf.append("'"+table.getSelection()[i].getAttribute("ROLE_ID")+"'");
								}else{
									sf.append("'"+rec.getAttribute("ROLE_ID")+"',");
								}
								sf.append("sysdate,");
								sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
								sf.append("sysdate,");
								sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"')");
								sqlList.add(sf.toString());
							}
						}else{
							if(rec.getAttribute("USE_FLAG").equals("true")){
								sf = new StringBuffer();
								sf.append("insert into SYS_APPROVE_SET(HEAD_ID,DOC_NAME,SHOW_SEQ,ROLE_ID,ADDTIME,ADDWHO,EDITTIME,EDITWHO)");
								sf.append(" values('"+headTbale.getSelectedRecord().getAttribute("ID")+"',");
								sf.append("'"+panel.getItem("DOC_NO").getDisplayValue()+"',");
								if(rec.getAttribute("SHOW_SEQ")==null){
									sf.append("'"+table.getSelection()[i].getAttribute("SHOW_SEQ")+"'");
								}else{
									sf.append("'"+rec.getAttribute("SHOW_SEQ")+"',");
								}
								if(rec.getAttribute("ROLE_ID")==null){
									sf.append("'"+table.getSelection()[i].getAttribute("ROLE_ID")+"'");
								}else{
									sf.append("'"+rec.getAttribute("ROLE_ID")+"',");
								}
								sf.append("sysdate,");
								sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
								sf.append("sysdate,");
								sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"')");
								sqlList.add(sf.toString());
							}
						}
//					int[] records = table.getAllEditRows();
//					ArrayList<String> sqlList = new ArrayList<String>();
//					StringBuffer sf = new StringBuffer();
//					sf.append("delete from SYS_APPROVE_SET where DOC_NO='"+form.getItem("DOC_NAME").getValue()+"'");
//					sqlList.add(sf.toString());
//					for(int i = 0; i < records.length; i++) {
//						Record rec = table.getEditedRecord(records[i]);
//						map1 = (HashMap<String, Boolean>)table.getEditValues(records[i]);
//						sf = new StringBuffer();
//						if(map1.get("USE_FLAG")){
//							sf.append("insert into SYS_APPROVE_SET(DOC_NO,DOC_NAME,SHOW_SEQ,ROLE_ID,ADDTIME,ADDWHO,EDITTIME,EDITWHO)");
//							sf.append(" values('"+form.getItem("DOC_NAME").getValue()+"',");
//							sf.append("'"+form.getItem("DOC_NAME").getDisplayValue()+"',");
//							if(rec.getAttribute("SHOW_SEQ")==null){
//								sf.append("'"+table.getSelection()[i].getAttribute("SHOW_SEQ")+"'");
//							}else{
//								sf.append("'"+rec.getAttribute("SHOW_SEQ")+"',");
//							}
//							if(rec.getAttribute("ROLE_ID")==null){
//								sf.append("'"+table.getSelection()[i].getAttribute("ROLE_ID")+"'");
//							}else{
//								sf.append("'"+rec.getAttribute("ROLE_ID")+"',");
//							}
////							sf.append("'"+rec.getAttribute("SHOW_SEQ")+"',");
////							sf.append("'"+rec.getAttribute("ROLE_ID")+"',");
//							sf.append("sysdate,");
//							sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
//							sf.append("sysdate,");
//							sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"')");
//							sqlList.add(sf.toString());
//						}
					}
					Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
								
						}
						
						@Override
						public void onSuccess(String result) {
							MSGUtil.showOperSuccess();
							ListGridRecord[] rec = table.getRecords();
							for(int i=0;i<rec.length;i++){
								rec[i].setAttribute("USE_FLAG", false);
								rec[i].setAttribute("ROLE_ID", "");
							}
							table.redraw();
							Util.db_async.getRecord("SHOW_SEQ,ROLE_ID"," SYS_APPROVE_SET"," where HEAD_ID='"+headTbale.getSelectedRecord().getAttribute("ID")+"' order by SHOW_SEQ ", null, new AsyncCallback<ArrayList<HashMap<String, String>>>(){

								@Override
								public void onFailure(Throwable caught) {
										
								}

								@Override
								public void onSuccess(ArrayList<HashMap<String, String>> result) {
									if(result != null && result.size() > 0){
										ListGridRecord[] rec = table.getRecords();
										for(int i=0;i<result.size();i++){
											HashMap<String, String> map = result.get(i);
											String key = "",value = "";
											for(int j=0;j<map.size();j++){
												key = map.get("SHOW_SEQ");
												value = map.get("ROLE_ID");
												for(int n=0;n<rec.length;n++){
													if (key.equals(rec[n].getAttribute("SHOW_SEQ"))) {									
														rec[n].setAttribute("USE_FLAG", true);
														if(ObjUtil.isNotNull(value)) {
															rec[n].setAttribute("ROLE_ID", value);
														}
														break;
													}
												}
											}
										}
										table.redraw();
									}
								}
							});
						}
					});
				}
			}
		});
		
		IButton canButton1 = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.ApproveSet_P1_02);
		canButton1.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if(headTbale.getSelectedRecord()==null) return;
				table.discardAllEdits();
			}
			
		});
		
		downtoolStrip.setMembersMargin(4);
        downtoolStrip.setMembers(saveButton1, canButton1);
	}
	
	public void downBtnWidget2(ToolStrip downtoolStrip) {
		downtoolStrip.setWidth("100%");
		downtoolStrip.setHeight("20");
		downtoolStrip.setPadding(2);
		downtoolStrip.setSeparatorSize(12);
		downtoolStrip.addSeparator();
		downtoolStrip.setMembersMargin(4);
		downtoolStrip.setAlign(Alignment.RIGHT);
		
		IButton saveButton2 = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.ApproveSet_P2_01);
		saveButton2.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(headTbale.getSelectedRecord()==null){
					return;
				}else{
					final int[] record = customerTbale.getAllEditRows();
					String n="";
					for(int j=0;j<record.length;j++){
						n=n+record[j]+",";
					}
					Util.db_async.getRecord("CUSTOMER_ID", " (select t.HEAD_ID,t.CUSTOMER_ID,h.DOC_NO from sys_approve_customer t,sys_approve_head h where t.head_id = h.id  and h.doc_no = '"+headTbale.getSelectedRecord().getAttribute("DOC_NO")+"')", "", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							if(result != null && result.size() > 0){
								ListGridRecord[] rec = customerTbale.getRecords();
								for(int i=0;i<result.size();i++){
									HashMap<String, String> map = result.get(i);
									String key = "";
									for(int j=0;j<map.size();j++){
										key = map.get("CUSTOMER_ID");
										for(int s=0;s<rec.length;s++){
											if (key.equals(rec[s].getAttribute("ID"))) {	
												if(rec[s].getAttribute("USE_FLAG").toString().equals("true")){
													SC.say(rec[s].getAttribute("SHORT_NAME")+"已经被指定");
													return;
												}
											}
										}
									}
								}
							}
							ListGridRecord[] records=customerTbale.getRecords();
							ArrayList<String> sqlList = new ArrayList<String>();
							StringBuffer sf = new StringBuffer();
							sf.append("delete from SYS_APPROVE_CUSTOMER where HEAD_ID='"+headTbale.getSelectedRecord().getAttribute("ID")+"'");
							sqlList.add(sf.toString());
							String o="";
							for(int a=0;a<record.length;a++){
								o=o+record[a]+",";
							}
							for(int b = 0; b < records.length; b++) {
								Record rec = records[b];
								if(o.contains(b+",")){
									if(customerTbale.getEditedRecord(b).getAttribute("USE_FLAG").equals("true")){
										sf = new StringBuffer();
										sf.append("insert into SYS_APPROVE_CUSTOMER(HEAD_ID,CUSTOMER_ID)");
										sf.append(" values('"+headTbale.getSelectedRecord().getAttribute("ID")+"',");
										sf.append("'"+rec.getAttribute("ID")+"')");
										sqlList.add(sf.toString());
									}
								}else{
									if(rec.getAttribute("USE_FLAG").equals("true")){
										sf = new StringBuffer();
										sf.append("insert into SYS_APPROVE_CUSTOMER(HEAD_ID,CUSTOMER_ID)");
										sf.append(" values('"+headTbale.getSelectedRecord().getAttribute("ID")+"',");
										sf.append("'"+rec.getAttribute("ID")+"')");
										sqlList.add(sf.toString());
									}
								}
							}
							Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
										
								}
								
								@Override
								public void onSuccess(String result) {
									MSGUtil.showOperSuccess();
									ListGridRecord[] rec = customerTbale.getRecords();
									for(int i=0;i<rec.length;i++){
										rec[i].setAttribute("USE_FLAG", false);
									}
									customerTbale.redraw();
									Util.db_async.getRecord("CUSTOMER_ID"," SYS_APPROVE_CUSTOMER"," where HEAD_ID='"+headTbale.getSelectedRecord().getAttribute("ID")+"'", null, new AsyncCallback<ArrayList<HashMap<String, String>>>(){

										@Override
										public void onFailure(Throwable caught) {
												
										}

										@Override
										public void onSuccess(ArrayList<HashMap<String, String>> result) {
											if(result != null && result.size() > 0){
												ListGridRecord[] rec = customerTbale.getRecords();
												for(int i=0;i<result.size();i++){
													HashMap<String, String> map = result.get(i);
													String key = "";
													for(int j=0;j<map.size();j++){
														key = map.get("CUSTOMER_ID");
														for(int n=0;n<rec.length;n++){
															if (key.equals(rec[n].getAttribute("ID"))) {									
																rec[n].setAttribute("USE_FLAG", true);
																break;
															}
														}
													}
												}
												customerTbale.redraw();
											}
										}
									});
								}
							});
							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
					
				}
			}
		});
		
		IButton canButton2 = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.ApproveSet_P2_02);
		canButton2.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if(headTbale.getSelectedRecord()==null) return;
				customerTbale.discardAllEdits();
			}
			
		});
		
		downtoolStrip.setMembersMargin(4);
        downtoolStrip.setMembers(saveButton2, canButton2);
	}
	
	public DynamicForm createSerchForm(SGPanel form) {
		form.setDataSource(approveHeadDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		
		SGCombo DOC_NO = new SGCombo("DOC_NO","审批单据", true);
		Util.initComboValue(DOC_NO, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'APPROVE_DOC'", " SHOW_SEQ ASC");
		
		SGDateTime ADDTIME = new SGDateTime("ADDTIME","创建时间从");
		SGDateTime ADDTIME_TO = new SGDateTime("ADDTIME_TO","到");
		
		form.setItems(DOC_NO,ADDTIME,ADDTIME_TO);
		return form;
	}
	
	@Override
	public void initVerify() {
		check_map.put("TABLE", "SYS_APPROVE_HEAD");
		check_map.put("DOC_NO", StaticRef.CHK_NOTNULL + "审批单据");
	}

	@Override
	public void onDestroy() {
		
	}

	private ListGridRecord createRecord(boolean USE_FLAG, String SHOW_SEQ, String SHOW_NODE,String ROLE_ID){  
        ListGridRecord record = new ListGridRecord(); 
         record.setAttribute("USE_FLAG", USE_FLAG);
         record.setAttribute("SHOW_SEQ", SHOW_SEQ);  
         record.setAttribute("SHOW_NODE", SHOW_NODE);  
         record.setAttribute("ROLE_ID", ROLE_ID);  
         return record;  
     }

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		ApproveSetView view = new ApproveSetView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}  
	
}
