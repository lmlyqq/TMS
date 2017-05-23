package com.rd.client.view.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.rd.client.PanelFactory;
import com.rd.client.action.base.area.DeleteAreaAction;
import com.rd.client.action.base.area.NewAreaAction;
import com.rd.client.action.base.area.SaveAreaAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.GetHintAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.common.widgets.TreeTable;
import com.rd.client.ds.base.AreaDS;
import com.rd.client.ds.base.AreaDS1;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;

/**
 * 行政区划管理	
 * @author fanglm
 * @created Time 2010-09-16 09:54
 */
@ClassForNameAble
public class BasAreaView extends SGForm implements PanelFactory {
	
	private DataSource ds;
	private DataSource treeDs;
	private SGTable table;
	private TreeTable treeGrid;
	private Window searchWin = null;
	public TreeNode selectNode;
	public Record selectRecord;
	public SGPanel searchForm = new SGPanel();
	private SectionStack section;
	private DynamicForm form;
	
	/*public BasAreaView(String id) {
		super(id);
	}*/
    
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		ds = AreaDS.getInstance("BAS_AREA");
		//treeDs = getDataSource();
		treeDs = AreaDS1.getInstance("BAS_AREA1", "BAS_AREA");
		selectNode=new TreeNode();
		
		table = new SGTable(ds, "100%", "100%");
		table.setShowFilterEditor(false);
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				selectRecord = event.getRecord();
			}
		});
		
		table.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				selectRecord = event.getRecord();
				enableOrDisables(del_map, true);
			}
		});
		
		treeGrid = new TreeTable(treeDs,"20%","100%");
        getConfigList();
        treeGrid.setCanAcceptDroppedRecords(false);
        treeGrid.addNodeClickHandler(new NodeClickHandler() {
			
			@Override
			public void onNodeClick(NodeClickEvent event) {
				 TreeNode node = event.getNode();
				 selectNode = node;
//				 selectNode.setCanAcceptDrop(false);
				 
	                try {
	                	Criteria criteria = new Criteria();
	            		criteria.addCriteria("PARENT_AREA_ID",node.getAttribute("AREA_CODE"));
	            		criteria.addCriteria("OP_FLAG","M");
	            		criteria.addCriteria("is_curr_page","true");
	                	table.fetchData(criteria,new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								Object[] obj=JSOHelper.convertToJavaObjectArray((JavaScriptObject)rawData);
								int tot_count=obj.length;
								int sum_page=tot_count/50+1;
								form.getItem("SUM_PAGE").setValue(sum_page);
								form.getItem("TOTAL_COUNT").setValue(tot_count);
							}
						});
	                	
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});
        
        
		createListFields();
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		  
//		section = createSection(table, null, true, true);
		
		section=new SectionStack();
		form=new DynamicForm();
		
		section.setWidth("79%");
		section.setHeight100();
		
		SectionStackSection listItem=new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.addItem(table);
		form=new SGPage(table,true).initPageBtn();
		listItem.setControls(form);
		section.addSection(listItem);
		
		initVerify();
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();
		
		stack.setMembers(treeGrid);
		stack.addMember(section);
		
	        
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(stack);
		
		return main;
	}
	
	private void getConfigList() {
		
		treeGrid.addDrawHandler(new DrawHandler() {  
        	public void onDraw(DrawEvent event) { 
        		Criteria criteria = new Criteria();
        		criteria.addCriteria("OP_FLAG","M");
        		treeGrid.fetchData(criteria,new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						treeGrid.selectRecord(0);
					}
				}); 
        		
        	}  
        }); 
		
        TreeGridField orgField = new TreeGridField();
        orgField.setName("SHORT_NAME");
        treeGrid.setFields(orgField);
	}
	//创建列表
	private void createListFields(){
		  ListGridField AREA_CODE = new ListGridField("AREA_CODE", Util.TI18N.AREA_CODE(), 80);
		  AREA_CODE.setTitle(ColorUtil.getRedTitle(Util.TI18N.AREA_CODE()));
		  ListGridField AREA_CNAME = new ListGridField("AREA_CNAME", Util.TI18N.AREA_CNAME(), 140);
		  AREA_CNAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.AREA_CNAME()));
		  ListGridField AREA_ENAME = new ListGridField("AREA_ENAME",Util.TI18N.AREA_ENAME(),140);  
		  ListGridField SHORT_NAME = new ListGridField("SHORT_NAME",Util.TI18N.SHORT_NAME(),80); 
		  SHORT_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.SHORT_NAME()));
		  ListGridField HINT_CODE = new ListGridField("HINT_CODE",Util.TI18N.HINT_CODE(),60);
		  HINT_CODE.setTitle(Util.TI18N.HINT_CODE());
		  ListGridField PARENT_AREA_ID = new ListGridField("PARENT_AREA_ID",Util.TI18N.PARENT_AREA_ID(),80);
		  PARENT_AREA_ID.setCanEdit(false);
		  
		  ListGridField AREA_TYPE = new ListGridField("AREA_TYPE",Util.TI18N.AREA_TYPE(),60);
		  Util.initCodesComboValue(AREA_TYPE, "AREA_TYPE");
//		  Util.initComboValue(AREA_TYPE, "BAS_CODES", "CODE", "NAME_C"," WHERE PROP_ID=''","");//数据字段表获取数据
		  
		  ListGridField AREA_LEVEL = new ListGridField("AREA_LEVEL",Util.TI18N.AREA_LEVEL(),60);
		  AREA_LEVEL.setCanEdit(false);
		  ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ",Util.TI18N.SHOW_SEQ(),60);
		  ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),60);
		  ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ListGridField MODIFY_FLAG = new ListGridField("MODIFY_FLAG",Util.TI18N.MODIFY_FLAG(),60);
		  MODIFY_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),160);
		  table.setFields(AREA_CODE, AREA_CNAME, AREA_ENAME, SHORT_NAME,HINT_CODE,PARENT_AREA_ID, AREA_TYPE,AREA_LEVEL,SHOW_SEQ, ENABLE_FLAG, MODIFY_FLAG, NOTES);		
		  
		  //自动生成助记码
		  SHORT_NAME.addEditorExitHandler(new GetHintAction(table));
		  
		  table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, true);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
			}
		  });
	}
	
	//创建按钮
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(3);
        toolStrip.setSeparatorSize(5);
        
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.AREA);      
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchWin = new SearchWin(ds,createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
        		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.AREA_P0_01);
        newButton.addClickHandler(new NewAreaAction(table,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.AREA_P0_02);
        saveButton.addClickHandler(new SaveAreaAction(table,this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.AREA_P0_03);
        delButton.addClickHandler(new DeleteAreaAction(table,this));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.ADDR_P0_04);
        canButton.addClickHandler(new CancelAction(table,this));
        
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.AREA_P0_05);
        expButton.addClickHandler(new ExportAction(table));
        
        add_map.put(BasPrivRef.AREA_P0_01, newButton);
        del_map.put(BasPrivRef.AREA_P0_03, delButton);
        save_map.put(BasPrivRef.AREA_P0_02, saveButton);
        save_map.put(BasPrivRef.ADDR_P0_04, canButton);
        enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
		enableOrDisables(save_map, false);
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton, expButton);
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
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub

	}
	//查询窗口
	public DynamicForm createSerchForm(DynamicForm form){
		
		TextItem txt_global = new TextItem("FULL_INDEX", "模糊查询");
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(323);
		txt_global.setColSpan(5);
		txt_global.setEndRow(true);

		
		SGText txt_name = new SGText("SHORT_NAME",Util.TI18N.SHORT_NAME(),true);
		
		SGCombo com_type = new SGCombo("AREA_TYPE",Util.TI18N.AREA_TYPE());
		Util.initCodesComboValue(com_type, "AREA_TYPE");
		
		SGCheck txt_enable = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),true);
		txt_enable.setValue(true);
		
//		SGCheck txt_mofify = new SGCheck("MODIFY_FLAG",Util.TI18N.MODIFY_FLAG());
		
        form.setItems(txt_global,txt_name,com_type,txt_enable);
        
        return form;
	}
	
	public void initVerify() {
		check_map.put("TABLE", "BAS_AREA");		
		check_map.put("AREA_CODE", StaticRef.CHK_UNIQUE + Util.TI18N.AREA_CODE());		
		check_map.put("AREA_CODE", StaticRef.CHK_NOTNULL + Util.TI18N.AREA_CODE());
	
	}

	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasAreaView view = new BasAreaView();
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
