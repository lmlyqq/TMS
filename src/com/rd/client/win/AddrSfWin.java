package com.rd.client.win;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.Cookies;
import com.rd.client.action.base.address.AreaChangeAction;
import com.rd.client.action.tms.OrderSaveFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.AddrsfDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

/**
 * 地址点公用弹出窗口
 * @author Administrator
 * 
 */
public class AddrSfWin extends Window {
	
	private int width = 540;
	private int height = 400;
	private String top = "33%";
	private String left = "35%";
	private String title = "地址点";
	public Window window;
	private DataSource ds;
	private ListGrid table;
	private int row;
	private String customer_id;
	private String customer_name;
	private ValuesManager vm;
	private String flag;
	private TabSet bottoTabSet;
	private HashMap<String, String> check_map = new HashMap<String, String>();
	private SectionStack section;
	public DynamicForm pageForm; 
	private String full_index;
	private SGTable addrList;
	private TextItem FULL_INDEX ;
	private SGPanel searchPanel;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */

	
	
	public AddrSfWin(ListGrid table,String customer_id,String customer_name,int row,String top,String left,String flag){
		this.table = table;
		this.row = row;
		this.top = top;
		this.left = left;
		this.customer_id = customer_id;
		this.customer_name = customer_name;
		this.flag = flag;
	}
	
	public AddrSfWin(ListGrid table,String customer_id,String customer_name,int row,String top,String left,String flag,String full_index){
		this.table = table;
		this.row = row;
		this.top = top;
		this.left = left;
		this.customer_id = customer_id;
		this.customer_name = customer_name;
		this.flag = flag;
		this.full_index = full_index;
	}
	
	public Window getViewPanel() {
	
		bottoTabSet = new TabSet();
		
		VLayout lay = new VLayout();
		VLayout lay1 = new VLayout();
		Tab tab1 = new Tab("查询");
		tab1.setPane(lay);
		bottoTabSet.addTab(tab1);
		
		Tab tab2 = new Tab("地址信息");
		tab2.setPane(lay1);
		tab2.setID("add");
		bottoTabSet.addTab(tab2);
		
		bottoTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				bottoTabSet.disableTab("add");
			}
		});
		
		
		
		//树形结构
		ds = AddrsfDS.getInstance("BAS_ADDRESSsf");
		FULL_INDEX= new TextItem("FULL_INDEX",Util.TI18N.FUZZYQRY());
		FULL_INDEX.setShowTitle(false);
		FULL_INDEX.setColSpan(5);
		FULL_INDEX.setWidth(220);
		FULL_INDEX.setValue(full_index);
		
		SGButtonItem searchBtn = new SGButtonItem(StaticRef.FETCH_BTN);
		SGButtonItem writeBtn = new SGButtonItem("录入",StaticRef.ICON_NEW);
		writeBtn.setWidth(60);

		FULL_INDEX.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
			
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doSearch();
				}
			}
		});
		
		
		searchPanel= new SGPanel();
		searchPanel.setTitleWidth(100);
		searchPanel.setItems(FULL_INDEX,searchBtn,writeBtn);
		
		
		addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		section = new SectionStack();
		
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE",80);
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME",100);
		ListGridField AREA_NAME = new ListGridField("AREA_NAME2",Util.TI18N.AREA_ID_NAME(),80);
		ListGridField ADDRESS = new ListGridField("ADDRESS",200);
		addrList.setFields(ADDR_CODE,ADDR_NAME,AREA_NAME,ADDRESS);
		
		SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(addrList);
    	listItem.setExpanded(true);
    	pageForm =new SGPage(addrList, true).initPageBtn();
    	listItem.setControls(pageForm);
    	section.addSection(listItem);
		
		if(flag.equals("AND_RECV_FLAG")){
			searchBtn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					addrList.discardAllEdits();
					addrList.invalidateCache();
					final Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG","M");
					criteria.addCriteria("ENABLE_FLAG","Y");
					criteria.addCriteria("AND_RECV_FLAG","Y");
					if(ObjUtil.isNotNull(customer_id)){
						criteria.addCriteria("CUSTOMER_ID",customer_id);
					}
					if(FULL_INDEX.getValue() != null){
						criteria.addCriteria(searchPanel.getValuesAsCriteria());
					}
					addrList.fetchData(criteria, new DSCallback() {

						@SuppressWarnings("unchecked")
						@Override
						public void execute(DSResponse response, Object rawData,DSRequest request) {
							if(pageForm != null) {
								pageForm.getField("CUR_PAGE").setValue("1");
								pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
								pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
								String sqlwhere = Cookies.getCookie("SQLWHERE");
								if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
									table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
									//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
								}
							}
							LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
							if(map.get("criteria") != null) {
								map.remove("criteria");
							}
							if(map.get("_constructor") != null) {
								map.remove("_constructor");
							}
							if(map.get("C_ORG_FLAG") != null) {
								Object obj = map.get("C_ORG_FLAG");
								Boolean c_org_flag = (Boolean)obj;
								map.put("C_ORG_FLAG",c_org_flag.toString());
							}			
						}
					}); 
				}
			});
	        
	        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
				
				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					Record record = event.getRecord();
					table.setEditValue(row, "UNLOAD_NAME", record.getAttribute("ADDR_NAME"));
					table.setEditValue(row, "UNLOAD_ID", record.getAttribute("ID"));
					table.setEditValue(row, "UNLOAD_AREA_ID", record.getAttribute("AREA_ID"));
					table.setEditValue(row, "UNLOAD_AREA_NAME", record.getAttribute("AREA_NAME"));
					table.setEditValue(row, "UNLOAD_AREA_ID2", record.getAttribute("AREA_ID2"));
					table.setEditValue(row, "UNLOAD_AREA_NAME2", record.getAttribute("AREA_NAME2"));
					table.setEditValue(row, "UNLOAD_AREA_ID3", record.getAttribute("AREA_ID3"));
					table.setEditValue(row, "UNLOAD_AREA_NAME3", record.getAttribute("AREA_NAME3"));
					table.setEditValue(row, "UNLOAD_ADDRESS", record.getAttribute("ADDRESS"));
					table.setEditValue(row, "UNLOAD_CONTACT", record.getAttribute("CONT_NAME"));
					table.setEditValue(row, "UNLOAD_TEL", record.getAttribute("CONT_TEL"));
					table.setEditValue(row, "UNLOAD_CODE", record.getAttribute("ADDR_CODE"));
					window.hide();
					window.destroy();
					
				}
			});
		}else{
			searchBtn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					addrList.discardAllEdits();
					addrList.invalidateCache();
					final Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG","M");
					criteria.addCriteria("ENABLE_FLAG","Y");
					criteria.addCriteria("AND_LOAD_FLAG","Y");
					if(ObjUtil.isNotNull(customer_id)){
						criteria.addCriteria("CUSTOMER_ID",customer_id);
					}
					if(FULL_INDEX.getValue() != null){
						criteria.addCriteria(searchPanel.getValuesAsCriteria());
					}
					addrList.fetchData(criteria,new DSCallback() {

						@SuppressWarnings("unchecked")
						@Override
						public void execute(DSResponse response, Object rawData,DSRequest request) {
							if(pageForm != null) {
								pageForm.getField("CUR_PAGE").setValue("1");
								pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
								pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
								String sqlwhere = Cookies.getCookie("SQLWHERE");
								if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
									table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
									//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
								}
							}
							LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
							if(map.get("criteria") != null) {
								map.remove("criteria");
							}
							if(map.get("_constructor") != null) {
								map.remove("_constructor");
							}
							if(map.get("C_ORG_FLAG") != null) {
								Object obj = map.get("C_ORG_FLAG");
								Boolean c_org_flag = (Boolean)obj;
								map.put("C_ORG_FLAG",c_org_flag.toString());
							}			
						}
					}); 
				}
			});
	        
	        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
				
				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					Record record = event.getRecord();
					table.setEditValue(row, "LOAD_NAME", record.getAttribute("ADDR_NAME"));
					table.setEditValue(row, "LOAD_ID", record.getAttribute("ID"));
					table.setEditValue(row, "LOAD_AREA_ID", record.getAttribute("AREA_ID"));
					table.setEditValue(row, "LOAD_AREA_NAME", record.getAttribute("AREA_NAME"));
					table.setEditValue(row, "LOAD_AREA_ID2", record.getAttribute("AREA_ID2"));
					table.setEditValue(row, "LOAD_AREA_NAME2", record.getAttribute("AREA_NAME2"));
					table.setEditValue(row, "LOAD_AREA_ID3", record.getAttribute("AREA_ID3"));
					table.setEditValue(row, "LOAD_AREA_NAME3", record.getAttribute("AREA_NAME3"));
					table.setEditValue(row, "LOAD_ADDRESS", record.getAttribute("ADDRESS"));
					table.setEditValue(row, "LOAD_CONTACT", record.getAttribute("CONT_NAME"));
					table.setEditValue(row, "LOAD_TEL", record.getAttribute("CONT_TEL"));
					table.setEditValue(row, "LOAD_CODE", record.getAttribute("ADDR_CODE"));
					window.hide();
					window.destroy();
				}
			});
		}
        
		
        lay.addMember(searchPanel);
        lay.addMember(section);
		
        SGText ADDR_CODE1 = new SGText("ADDR_CODE",ColorUtil.getRedTitle(Util.TI18N.ADDR_CODE()));
        SGLText ADDR_NAME1 = new SGLText("ADDR_NAME",ColorUtil.getRedTitle("地址名称"));
        
        SGCombo AREA_ID = new SGCombo("AREA_ID",ColorUtil.getRedTitle(Util.TI18N.PROVINCE()),true);
        Util.initComboValue(AREA_ID, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " AREA_LEVEL = '3'", "");
        SGCombo AREA_ID2 = new SGCombo("AREA_ID2",ColorUtil.getRedTitle(Util.TI18N.CITY()));
        SGCombo AREA_ID3 = new SGCombo("AREA_ID3",Util.TI18N.AREA());
        SGText AREA_NAME1 = new SGText("AREA_NAME",Util.TI18N.PROVINCE());
        AREA_NAME1.setVisible(false);
        SGText AREA_NAME2 = new SGText("AREA_NAME2",Util.TI18N.CITY());
        AREA_NAME2.setVisible(false);
        final SGText AREA_NAME3 = new SGText("AREA_NAME3",Util.TI18N.AREA());
        AREA_NAME3.setVisible(false);
        AREA_ID.addChangedHandler(new AreaChangeAction(AREA_NAME1,AREA_ID2,AREA_NAME2,AREA_ID3));
        AREA_ID2.addChangedHandler(new AreaChangeAction(AREA_NAME2,AREA_ID3,AREA_NAME3));
        AREA_ID3.addChangedHandler(new ChangedHandler() {
        
			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem = (SGCombo)event.getSource();
				String value = fatherItem.getDisplayValue();
				if(ObjUtil.isNotNull(value) && AREA_NAME3 != null){
					AREA_NAME3.setValue(value);
				}
			}
        	
        });
        
        SGLText ADDRESS1 = new SGLText("ADDRESS",ColorUtil.getRedTitle(Util.TI18N.ADDRESS()),true);
        ADDRESS1.setWidth(FormUtil.longWidth+FormUtil.Width);
        ADDRESS1.setColSpan(6);
        
        SGText CONT_NAME = new SGText("CONT_NAME","联系人",true);
        SGText CONT_TEL = new SGText("CONT_TEL",Util.TI18N.CONT_TEL());
        
        IButton saveBtn = createBtn(Util.BI18N.SAVE(), StaticRef.ICON_SAVE);
//        saveBtn.setAutoFit(true);
        
        DynamicForm panel = new SGPanel();
        panel.setItems(ADDR_CODE1,ADDR_NAME1,AREA_ID,AREA_ID2,AREA_ID3,AREA_NAME2,AREA_NAME1,ADDRESS1,CONT_NAME,CONT_TEL);
        vm = new ValuesManager();
        vm.addMember(panel);
        vm.setValue("OP_FLAG", StaticRef.INS_FLAG);
        
        lay1.addMember(panel);
        lay1.addMember(saveBtn);
        
        if(flag.equals("AND_RECV_FLAG")){
        	writeBtn.addClickHandler(new ClickHandler() {
    			
    			@Override
    			public void onClick(ClickEvent event) {
    				vm.clearValues();
    				bottoTabSet.selectTab(1);
    				bottoTabSet.enableTab("add");
    				vm.setValue("ENABLE_FLAG", "Y");
    				vm.setValue("LOAD_FLAG", "N");
    				vm.setValue("RECV_FLAG", "Y");
    				vm.setValue("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
    				vm.setValue("EXEC_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
    				vm.setValue("CREATE_ORG_ID",LoginCache.getLoginUser().getDEFAULT_ORG_ID());
    				vm.setValue("CREATE_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
    				vm.setValue("CUSTOMER_ID", customer_id);
    				vm.setValue("CUSTOMER_NAME", customer_name);
    			}
    		});
        }else{
        	writeBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				vm.clearValues();
				bottoTabSet.selectTab(1);
				bottoTabSet.enableTab("add");
				vm.setValue("ENABLE_FLAG", "Y");
				vm.setValue("LOAD_FLAG", "Y");
				vm.setValue("RECV_FLAG", "N");
				vm.setValue("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
				vm.setValue("EXEC_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
				vm.setValue("CREATE_ORG_ID",LoginCache.getLoginUser().getDEFAULT_ORG_ID());
				vm.setValue("CREATE_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
				vm.setValue("CUSTOMER_ID", customer_id);
				vm.setValue("CUSTOMER_NAME", customer_name);
			}
		});
        	
        }
        
        
        saveBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			@Override
			public void onClick(
					com.smartgwt.client.widgets.events.ClickEvent event) {
				new OrderSaveFormAction(addrList, vm,check_map,bottoTabSet,flag).onClick(event);
			}
		});
        
        check_map.put("TABLE", "BAS_ADDRESS");
        check_map.put("CUSTOMER_ID,ADDR_CODE", StaticRef.CHK_UNIQUE + Util.TI18N.CUSTOMER_ID()+","+Util.TI18N.ADDR_CODE());
		check_map.put("ADDR_CODE", StaticRef.CHK_NOTNULL + Util.TI18N.ADDR_CODE());
		check_map.put("ADDRESS", StaticRef.CHK_NOTNULL + Util.TI18N.ADDRESS());
		check_map.put("AREA_ID", StaticRef.CHK_NOTNULL + Util.TI18N.PROVINCE());
		check_map.put("AREA_ID2", StaticRef.CHK_NOTNULL + Util.TI18N.CITY());
		check_map.put("ADDR_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.ADDR_NAME());
        
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(bottoTabSet);
		window.setShowCloseButton(true);
		window.show();
		
		if(flag.equals("AND_RECV_FLAG")){
			addrList.discardAllEdits();
			addrList.invalidateCache();
			final Criteria criteria = new Criteria();
			criteria.addCriteria("OP_FLAG","M");
			criteria.addCriteria("ENABLE_FLAG","Y");
			criteria.addCriteria("AND_RECV_FLAG","Y");
			if(ObjUtil.isNotNull(customer_id)){
				criteria.addCriteria("CUSTOMER_ID",customer_id);
			}
			if(FULL_INDEX.getValue() != null){
				criteria.addCriteria(searchPanel.getValuesAsCriteria());
			}
			addrList.fetchData(criteria,new DSCallback() {

				@SuppressWarnings("unchecked")
				@Override
				public void execute(DSResponse response, Object rawData,DSRequest request) {
					if(pageForm != null) {
						pageForm.getField("CUR_PAGE").setValue("1");
						pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
						pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
						String sqlwhere = Cookies.getCookie("SQLWHERE");
						if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
							table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
							//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
						}
					}
					LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
					if(map.get("criteria") != null) {
						map.remove("criteria");
					}
					if(map.get("_constructor") != null) {
						map.remove("_constructor");
					}
					if(map.get("C_ORG_FLAG") != null) {
						Object obj = map.get("C_ORG_FLAG");
						Boolean c_org_flag = (Boolean)obj;
						map.put("C_ORG_FLAG",c_org_flag.toString());
					}			
				}
			});  
		}else{
			addrList.discardAllEdits();
			addrList.invalidateCache();
			final Criteria criteria = new Criteria();
			criteria.addCriteria("OP_FLAG","M");
			criteria.addCriteria("ENABLE_FLAG","Y");
			criteria.addCriteria("AND_LOAD_FLAG","Y");
			if(ObjUtil.isNotNull(customer_id)){
				criteria.addCriteria("CUSTOMER_ID",customer_id);
			}
			if(FULL_INDEX.getValue() != null){
				criteria.addCriteria(searchPanel.getValuesAsCriteria());
			}
			addrList.fetchData(criteria,new DSCallback() {

				@SuppressWarnings("unchecked")
				@Override
				public void execute(DSResponse response, Object rawData,DSRequest request) {
					if(pageForm != null) {
						pageForm.getField("CUR_PAGE").setValue("1");
						pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
						pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
						String sqlwhere = Cookies.getCookie("SQLWHERE");
						if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
							table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
							//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
						}
					}
					LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
					if(map.get("criteria") != null) {
						map.remove("criteria");
					}
					if(map.get("_constructor") != null) {
						map.remove("_constructor");
					}
					if(map.get("C_ORG_FLAG") != null) {
						Object obj = map.get("C_ORG_FLAG");
						Boolean c_org_flag = (Boolean)obj;
						map.put("C_ORG_FLAG",c_org_flag.toString());
					}			
				}
			});   
		}
		
		return window;
	}

    public IButton createBtn(String btn_type, String iconSave) {
    	String btn_name = Util.BI18N.SAVE();
    	
		IButton button = new IButton(btn_name);
//		button.setShowRollOver(true);
//    	button.setShowDisabled(true);
//    	button.setShowDownIcon(true);
		//button.setIcon(icon_dir);
		button.setWidth(80);
		//button.setAutoFit(true);
		return button;
    }
    
    
    public void doSearch(){
		if(flag.equals("AND_RECV_FLAG")){
			addrList.discardAllEdits();
			addrList.invalidateCache();
			final Criteria criteria = new Criteria();
			criteria.addCriteria("OP_FLAG","M");
			criteria.addCriteria("ENABLE_FLAG","Y");
			criteria.addCriteria("AND_RECV_FLAG","Y");
			if(ObjUtil.isNotNull(customer_id)){
				criteria.addCriteria("CUSTOMER_ID",customer_id);
			}
			if(FULL_INDEX.getValue() != null){
				criteria.addCriteria(searchPanel.getValuesAsCriteria());
			}
			addrList.fetchData(criteria,new DSCallback() {

				@SuppressWarnings("unchecked")
				@Override
				public void execute(DSResponse response, Object rawData,DSRequest request) {
					if(pageForm != null) {
						pageForm.getField("CUR_PAGE").setValue("1");
						pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
						pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
						String sqlwhere = Cookies.getCookie("SQLWHERE");
						if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
							table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
							//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
						}
					}
					LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
					if(map.get("criteria") != null) {
						map.remove("criteria");
					}
					if(map.get("_constructor") != null) {
						map.remove("_constructor");
					}
					if(map.get("C_ORG_FLAG") != null) {
						Object obj = map.get("C_ORG_FLAG");
						Boolean c_org_flag = (Boolean)obj;
						map.put("C_ORG_FLAG",c_org_flag.toString());
					}			
				}
			});  
		}else{
			addrList.discardAllEdits();
			addrList.invalidateCache();
			final Criteria criteria = new Criteria();
			criteria.addCriteria("OP_FLAG","M");
			criteria.addCriteria("ENABLE_FLAG","Y");
			criteria.addCriteria("AND_LOAD_FLAG","Y");
			if(ObjUtil.isNotNull(customer_id)){
				criteria.addCriteria("CUSTOMER_ID",customer_id);
			}
			if(FULL_INDEX.getValue() != null){
				criteria.addCriteria(searchPanel.getValuesAsCriteria());
			}
			addrList.fetchData(criteria,new DSCallback() {

				@SuppressWarnings("unchecked")
				@Override
				public void execute(DSResponse response, Object rawData,DSRequest request) {
					if(pageForm != null) {
						pageForm.getField("CUR_PAGE").setValue("1");
						pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
						pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
						String sqlwhere = Cookies.getCookie("SQLWHERE");
						if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
							table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
							//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
						}
					}
					LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
					if(map.get("criteria") != null) {
						map.remove("criteria");
					}
					if(map.get("_constructor") != null) {
						map.remove("_constructor");
					}
					if(map.get("C_ORG_FLAG") != null) {
						Object obj = map.get("C_ORG_FLAG");
						Boolean c_org_flag = (Boolean)obj;
						map.put("C_ORG_FLAG",c_org_flag.toString());
					}			
				}
			});   
		}	
    }
}
