package com.rd.client.view.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.base.range.SaveRangeAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.NewAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.SysPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.common.widgets.TreeTable;
import com.rd.client.ds.base.BasRangeHeadDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.ChangeEvent;
import com.smartgwt.client.widgets.grid.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 基础资料->服务范围
 * @author yuanlei
 *
 */
@ClassForNameAble
public class BasRangeView extends SGForm implements PanelFactory {

	private DataSource mainDS;
	private DataSource areaDs;
	private SGTable rangeTable;
	private TreeTable areaTable;
	private SectionStack stack;
	private Window searchWin;
	private SGPanel searchForm;
	private HashMap<String, String> detail_ck_map;
	private HashMap<String, IButton> save_detail_map;
	public TreeNode selectNode;
	@Override
	public void createForm(DynamicForm form) {
		
	}

	/*public BasRangeView(String id) {
	    super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		//初始化主界面、按钮布局、Section布局
		VLayout main = new VLayout();
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		detail_ck_map = new HashMap<String, String>();
		
		//创建表格和数据源
		mainDS = BasRangeHeadDS.getInstance("BAS_RANGE", "BAS_RANGE");
		rangeTable = new SGTable(mainDS, "100%", "45%", true, true, false);
		rangeTable.setCanEdit(true);
		rangeTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		rangeTable.setShowFilterEditor(false);
		
		rangeTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				
				enableOrDisables(add_map, true);
				enableOrDisables(del_map, true);
				enableOrDisables(save_map, false);
				
				Record record = event.getRecord();
				if(record != null) {
					Util.db_async.getRangeDetail(record.getAttribute("ID"), new AsyncCallback<HashMap<String,String>>() {
						
						@Override
						public void onSuccess(HashMap<String, String> result) {
							Tree troo = areaTable.getTree();
							TreeNode[] node = troo.getAllNodes();
							if(node != null) {
								for(int i = 0;i<node.length;i++){
									String area_code = node[i].getAttribute("AREA_CODE");
									if(ObjUtil.isNotNull(result.get(area_code))){
										areaTable.selectRecord(node[i]);
									}else{
										areaTable.deselectRecord(node[i]);
									}
								}
								//areaTable.getData().closeFolders(troo.getChildren(node[0]));
							}
							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
				}
			}
		});
		rangeTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
				
			}
		});
		
		areaDs = getDataSource();
		areaTable = new TreeTable(areaDs,"100%","100%");
		areaTable.setCanEdit(true);	
		areaTable.setCanAcceptDroppedRecords(false);
		areaTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		areaTable.setShowSelectedStyle(false);
		areaTable.setShowPartialSelection(true);
		areaTable.setCascadeSelection(true);
		areaTable.setShowResizeBar(false);
		
        TreeGridField orgField = new TreeGridField();
        orgField.setName("SHORT_NAME");
        areaTable.setFields(orgField);
        //Lang 注释
//		areaTable.addNodeClickHandler(new NodeClickHandler() {
//			
//			@Override
//			public void onNodeClick(NodeClickEvent event) {
//				 TreeNode node = event.getNode();
//				 selectNode = node;
////				 selectNode.setCanAcceptDrop(false);
//				 
//	                try {
//	                	Criteria criteria = new Criteria();
//	            		criteria.addCriteria("PARENT_AREA_ID",node.getAttribute("AREA_CODE"));
//	            		criteria.addCriteria("OP_FLAG","M");
//	            		criteria.addCriteria("is_curr_page","true");
//	            		areaTable.fetchData(criteria);
//	                	
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//			}
//		});
		areaTable.addDrawHandler(new DrawHandler() {  
        	public void onDraw(DrawEvent event) {  
        		Criteria criteria = new Criteria();
        		criteria.addCriteria("OP_FLAG","M");
        		areaTable.fetchData(criteria);  
        	}  
        }); 
		
		getConfigList();
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		
		//创建Section
		Canvas[] widget = new Canvas[2];
		String[] title = new String[2];
		widget[0] = rangeTable;
		widget[1] = areaTable;
		title[0] = "模版信息";
		title[1] = "范围信息";
		/*searchItem.setIcon(StaticRef.ICON_SEARCH);
		searchItem.setWidth(70);
		searchItem.setColSpan(1);
		searchItem.setStartRow(false);
		searchItem.setEndRow(false);*/
		stack = createUDFSection(widget, title, true, true);
		//创建分页
		//new PageUtil(stack.getSection(0), rangeTable, searchItem);
        
        main.setWidth100();
        main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(stack);
		
		initVerify();
		
		return main;
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.addSeparator();
         
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,SysPrivRef.DICTIONARY);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(mainDS, createSerchForm(searchForm), stack.getSection(0)).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.RANGE_P0_01);
        newButton.addClickHandler(new NewAction(rangeTable, cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.RANGE_P0_02);
        saveButton.addClickHandler(new SaveAction(rangeTable, check_map, this, 1));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.RANGE_P0_03);
        delButton.addClickHandler(new DeleteAction(rangeTable));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.RANGE_P0_04);
        canButton.addClickHandler(new CancelAction(rangeTable,this));
        
        IButton savSubButton = createUDFBtn(Util.BI18N.SAVEDETAIL(), StaticRef.ICON_SAVE,BasPrivRef.RANGE_P0_05);
        savSubButton.addClickHandler(new SaveRangeAction(rangeTable,areaTable));
        
        IButton canSubButton = createUDFBtn(Util.BI18N.CANCELDETAIL(), StaticRef.ICON_CANCEL,BasPrivRef.RANGE_P0_06); 
        //canSubButton.addClickHandler(new CancelAction(areaTable,this));
        
        //主表按钮联动
        add_map.put(BasPrivRef.RANGE_P0_01, newButton);
        del_map.put(BasPrivRef.RANGE_P0_03, delButton);
        save_map.put(BasPrivRef.RANGE_P0_02, saveButton);
        save_map.put(BasPrivRef.RANGE_P0_04, canButton);
        
        enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        enableOrDisables(save_map, false);
        //从表按钮联动
        save_detail_map = new HashMap<String, IButton>();
        save_detail_map.put(BasPrivRef.RANGE_P0_05, savSubButton);
        save_detail_map.put(BasPrivRef.RANGE_P0_06, canSubButton);
        enableOrDisables(save_detail_map, true);
        
            
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton, savSubButton,canSubButton);
	}
	
	private void getConfigList() {	
		List<String> fldList  = new ArrayList<String>();
		List<String> titList  = new ArrayList<String>();
		List<String> widList = new ArrayList<String>();
		List<ListGridFieldType> typList = new ArrayList<ListGridFieldType>();
		
		String[] fields = {"RANGE_NAME", "FROM_PROVINCE_ID","FROM_AREA_ID", "FROM_PROVINCE_NAME","FROM_AREA_NAME","REQ_HOURS"};
		fldList = Arrays.asList(fields);
		String[] titles = {ColorUtil.getRedTitle(Util.TI18N.RANGE_NAME()),ColorUtil.getRedTitle(Util.TI18N.FROM_PROVINCE_NAME()),ColorUtil.getRedTitle(Util.TI18N.FROM_AREA_NAME()), 
				           Util.TI18N.FROM_PROVINCE_NAME(),Util.TI18N.FROM_AREA_NAME(),ColorUtil.getRedTitle(Util.TI18N.REQ_HOURS())};
		titList = Arrays.asList(titles);
		
		String[] width = {"150", "130", "130", "1","1","90"};
		widList = Arrays.asList(width);
		
		ListGridFieldType[] types = {null, null, null, null,null,null};
		typList = Arrays.asList(types);
		createListField(rangeTable, fldList, titList, widList, typList);

		final ListGridField FROM_AREA_ID = rangeTable.getField("FROM_AREA_ID");
		Util.initComboValue(FROM_AREA_ID, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " AREA_LEVEL = '4'", "");
		
		final ListGridField FROM_PROVINCE_ID = rangeTable.getField("FROM_PROVINCE_ID");
		Util.initComboValue(FROM_PROVINCE_ID, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " AREA_LEVEL = '3'", "");
		FROM_PROVINCE_ID.addChangeHandler(new ChangeHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onChange(ChangeEvent event) {
				int row = event.getRowNum();
				String province = ObjUtil.ifObjNull(event.getValue(), "").toString();
				Map<String,String> map = FROM_PROVINCE_ID.getAttributeAsMap("valueMap");
				//FROM_PROVINCE_ID.getValuesManager().getValues()
				//String province_name = ObjUtil.ifObjNull(rangeTable.getEditValue(row, "FROM_PROVINCE_NAME"),"").toString();
				if(map!= null) {
					rangeTable.setEditValue(row, "FROM_PROVINCE_NAME", map.get(province));
				}
				rangeTable.setEditValue(row, "FROM_AREA_ID","");
				rangeTable.setEditValue(row, "FROM_AREA_NAME","");
				if(ObjUtil.isNotNull(province)) {
					Util.async.getComboValue("BAS_AREA", "AREA_CODE", "AREA_CNAME", " where parent_area_id = '" + province + "'","", new AsyncCallback<LinkedHashMap<String, String>>() {
						
						public void onFailure(Throwable caught) {	
							;
						}
						public void onSuccess(LinkedHashMap<String, String> result) {
							if(result != null && result.size() > 0) {
								FROM_AREA_ID.setValueMap(result);
							}
						}					
					});
				}
			}
			
		});
		FROM_AREA_ID.addChangedHandler(new ChangedHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onChanged(ChangedEvent event) {
				int row = event.getRowNum();
				String area_id = ObjUtil.ifObjNull(event.getValue(), "").toString();
				Map<String,String> map = FROM_AREA_ID.getAttributeAsMap("valueMap");
				if(map != null) {
					rangeTable.setEditValue(row, "FROM_AREA_NAME",map.get(area_id));
				}
			}
			
		});
	}
	
	public DynamicForm createSerchForm(SGPanel form) {
		form.setDataSource(mainDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		/*form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);*/
		
		TextItem txt_global = new TextItem("FULL_INDEX", "模糊查询");
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(323);
		txt_global.setColSpan(5);
		txt_global.setEndRow(true);
		
		
		SGText txt_name = new SGText("RANGE_NAME", Util.TI18N.RANGE_NAME());
		ComboBoxItem txt_type = new ComboBoxItem("FROM_AREA_NAME", Util.TI18N.AREA_ID_NAME());
		Util.initArea(txt_type, null);
		txt_type.setTitleOrientation(TitleOrientation.TOP);
		
		form.setItems(txt_global, txt_name, txt_type);
		
		return form;
	}
	
	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
		rangeTable.destroy();
		areaTable.destroy();
		stack.destroy();
	}

	@Override
	public void initVerify() {
		check_map.put("TABLE", "BAS_RANGE");
		
		check_map.put("RANGE_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.RANGE_NAME());
		check_map.put("FROM_AREA_ID", StaticRef.CHK_UNIQUE + Util.TI18N.FROM_AREA_NAME());
		check_map.put("REQ_HOURS", StaticRef.CHK_NOTNULL + Util.TI18N.REQ_HOURS());
		
		detail_ck_map.put("TABLE", "BAS_RANGE_DETAIL");
		detail_ck_map.put("RANGE_AREA_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.AREA_ID_NAME());
	}
		
	//树形结构数据源
	static DataSource getDataSource()
	{
		
		DataSource dataSource = new DataSource();
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(GWT.getHostPageBaseURL()+"basQueryServlet?ds_id=BAS_AREA");
		dataSource.setClientOnly(true);

		// id
		DataSourceTextField idField = new DataSourceTextField("AREA_CODE");
		idField.setPrimaryKey(true);
		idField.setRequired(true);
		idField.setHidden(true);

		// parentId
		DataSourceTextField parentIdField = new DataSourceTextField("PARENT_AREA_ID");
		parentIdField.setForeignKey("Area.AREA_CODE");
		parentIdField.setRequired(true);
		parentIdField.setHidden(true);
		parentIdField.setRootValue(0);

		// name
		DataSourceTextField nameField = new DataSourceTextField("SHORT_NAME");
		nameField.setRequired(true);

		dataSource.setFields(idField, nameField, parentIdField);
		dataSource.setClientOnly(false);

		return dataSource;
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasRangeView view = new BasRangeView();
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
