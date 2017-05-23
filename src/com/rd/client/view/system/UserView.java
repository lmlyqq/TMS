package com.rd.client.view.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.system.NewUserAction;
import com.rd.client.action.system.NewUserSystemAction;
import com.rd.client.action.system.SaveUserAction;
import com.rd.client.action.system.SaveUserCustAction;
import com.rd.client.action.system.SaveUserDefOrgAction;
import com.rd.client.action.system.SaveUserOrgAction;
import com.rd.client.action.system.SaveUserWhseAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.DeleteProAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.SysPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.common.widgets.TreeTable;
import com.rd.client.ds.base.OrgDS;
import com.rd.client.ds.system.ModelDS;
import com.rd.client.ds.system.SysUserDS;
import com.rd.client.ds.system.SystemUserDs;
import com.rd.client.ds.system.UserWhseDS;
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
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FocusEvent;
import com.smartgwt.client.widgets.form.fields.events.FocusHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.ChangeHandler;
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
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;

/**
 * 安全->用户管理
 * @author fanglm
 */
@ClassForNameAble
public class UserView extends SGForm implements PanelFactory {

	private SGTable table;
	private DataSource userDS;
	private Window searchWin;//
	private SGPanel mainForm;
    private SGPanel searchForm;//
    public TreeGrid treeGrid;
    private DataSource custDS;
    private DataSource whseDS;
   // private DataSource addrDS;
    private DataSource systemDS;
	private SGTable userCustTable;
	private SGTable userWhseTable;
	private SGTable systemTable;
	private String user_id;
	private int pageNum=0;
	private SectionStack section;
	private ValuesManager vm;
	public ToolStrip toolStrip;
	public ToolStrip toolStrip2;
	public ToolStrip toolStrip3;
	public ToolStrip toolStrip4;
	public ToolStrip toolStrip5;
	private Tree tree;
	private boolean flag = false;
	private SGPanel userCustFilter;
	private static int findDepth = 3;
	public String cuscode;
	public String cusname;
	private HashMap<String, String> check_system_map;
	private HashMap<String, String> cache_system_map;
	
	/*public UserView(String id) {
	    super(id);
	}*/
	
    //创建按钮布局
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		cuscode = "";
		cusname = "";
		VLayout main = new VLayout();
		main.setWidth("99%");
		main.setHeight100();
		
	    ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.RIGHT);
	    vm = new ValuesManager();
	    
	    check_system_map = new HashMap<String, String>();
	    cache_system_map = new HashMap<String, String>();
	    
		userDS = SysUserDS.getInstance("SYS_USER", "SYS_USER");
		custDS = ModelDS.getInstance("V_USER_ORG_CUST", "");
		whseDS = UserWhseDS.getInstance("V_USER_ORG_WHSE","");
		//addrDS = UserAddrDS.getInstance("V_USER_ADDR", "");
		systemDS = SystemUserDs.getInstance("SYS_USER_SYSTEM","SYS_USER_SYSTEM");
        
		
//		table = new SGTable(userDS, "100%", "70%", true, true, false);
//		table.setAutoSaveEdits(false);//自动保存 编辑信息  即不设为自动保存信息
//		table.setCanEdit(true);//表示可以被编辑
//		table.setEditEvent(ListGridEditEvent.DOUBLECLICK);  
//		table.OP_FLAG = "M";
	    
		//主布局
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();
		
		//STACK的左边列表
		table = new SGTable(userDS, "100%", "100%",false,true,false);
		table.OP_FLAG = "M";
		createListField(table);
		
		table.setCanEdit(false);
		section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
	    listItem.setItems(table);
	    listItem.setExpanded(true);
//	    listItem.setControls(addMaxBtn(section, stack, "200", true), new SGPage(table, true).initPageBtn());
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    section.addSection(listItem);
	    section.setWidth("100%");
		stack.addMember(section); 
		addSplitBar(stack);
		//STACK的右边布局
		
		
        TabSet leftTabSet = new TabSet();  
        leftTabSet.setWidth("80%");   
        leftTabSet.setHeight("100%"); 
        leftTabSet.setMargin(0);
        leftTabSet.setVisible(false);
        mainForm = new SGPanel();
        mainForm.setWidth("40%");
        mainForm.setHeight("100%");
        createForm(mainForm);
        
        if(isPrivilege(SysPrivRef.USERMANAGE_P1)) {	
	        
        	Tab tab = new Tab("主信息");
	        tab.setPane(createBtnWidgetByMain());
	        leftTabSet.addTab(tab);
        }
        
        if(isPrivilege(SysPrivRef.USERMANAGE_P2)) {	
	        
        	Tab tab1 = new Tab("组织机构");
			//组织明细的FORM布局
	
			tab1.setPane(createMainInfo());
	        leftTabSet.addTab(tab1);
        }
        
        //运输信息
        if(isPrivilege(SysPrivRef.USERMANAGE_P3)) {	
	        
        	Tab tab2 = new Tab("客户");
	        tab2.setPane(createCustTable());
	        leftTabSet.addTab(tab2);
        }
        
      //仓库信息
        if(isPrivilege(SysPrivRef.USERMANAGE_P4)) {	
	        
        	Tab tab3 = new Tab(Util.TI18N.WMS_INFO());
	        tab3.setPane(createWhseTable());
	        leftTabSet.addTab(tab3);
        }
        
      //经销商信息
//        if(isPrivilege(SysPrivRef.USERMANAGE_P5)) {	
//	       
//        	Tab tab4 = new Tab("经销商");
//	        tab4.setPane(createAddrTable());
//	        leftTabSet.addTab(tab4);
//        }
        
        if(isPrivilege(SysPrivRef.USERMANAGE_P5)) {	
 	       
        	Tab tab5 = new Tab("系统信息");
	        tab5.setPane(createSystemTable());
	        leftTabSet.addTab(tab5);
        }
        
		stack.addMember(leftTabSet);
		
		vm.addMember(mainForm);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(stack);
		
		
		initverify();
		
		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
		        enableOrDisables(save_map, true);
		        
		        vm.getItem("USER_ID").setDisabled(true);
		        
		        if(isMax){
		        	expend();
		        }
			}
		});
		
		
		leftTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				int num = event.getTabNum();//页签num
				pageNum = num;
				tableSelectedChanged(num);
			}
		});
		  
		return main;
	}
	
	private void tableSelectedChanged(int num){
		if(ObjUtil.isNotNull(user_id) && num != 0){
			Criteria criteria = new Criteria();
            criteria.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
            criteria.addCriteria("USER_ID", user_id);
            if(num == 1){
            	if(table.getSelectedRecord() == null)
            		return;
            	
            	user_id =table.getSelectedRecord().getAttribute("USER_ID");
            	Util.db_async.getUserOrg(user_id, new AsyncCallback<HashMap<String,String>>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(HashMap<String, String> result) {
						treeGridHandler(result, null, 0);
						
					}
				});
//            	LoginCache.getUserOrg(user_id, new AsyncCallback<LinkedHashMap<String, String>>() {
//
//					@Override
//					public void onFailure(Throwable caught) {
//						
//					}
//
//					public void onSuccess(LinkedHashMap<String, String> result) {
//						treeGridHandler(result, null);
//					}
//				});
            }else if(num == 2){
            	criteria.addCriteria("CUSTOMER_CODE", userCustFilter.getItem("CUSTOMER_CODE").getDisplayValue());
            	criteria.addCriteria("CUSTOMER_NAME", userCustFilter.getItem("CUSTOMER_NAME").getDisplayValue());
            	userCustTable.discardAllEdits();
                userCustTable.fetchData(criteria,new DSCallback() {
					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
						ListGridRecord[] rec = userCustTable.getRecords();
						if(rec != null && rec.length > 0) {
							Record record = null;
							for(int i = 0;i < rec.length; i++) {
								record = rec[i];
								if(record.getAttribute("USE_FLAG").equals("Y") || 
										record.getAttribute("USE_FLAG").equals("true")) {
									userCustTable.selectRecord(record);
								}
							}
						}
					}
                	
                });
                
			}else if(num == 3){
				userWhseTable.discardAllEdits();
				userWhseTable.fetchData(criteria);
			}else if(num ==4){
				systemTable.fetchData(criteria);
			}
		}
	}
	
	private int treeGridHandler(Map<String, String> result, TreeNode node, int depth){
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		flag = true;	//点击取消选中机构同时取消选中默认机构
		boolean defaultFlag = false;	//默认机构标识
		tree = tree == null?treeGrid.getTree():tree;
		TreeNode[] nodes = null;
		if(node == null){
			node = tree.findById(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
			treeGrid.discardAllEdits();
			treeGrid.redraw();
			treeGrid.deselectAllRecords();
			nodes = tree.getChildren(node);
			treeGrid.deselectRecords(nodes);
			tree.closeAll();
			int row = treeGrid.getRecordIndex(node);
			defaultFlag = "Y".equals(result.get(node.getAttribute("ID")));
			if(row >= 0 ){
				treeGrid.setEditValue(row, "DEFAULT_FLAG", defaultFlag);
			}
			node.setAttribute("DEFAULT_FLAG", defaultFlag);
			tree.openFolder(node);
		}else{
			nodes = tree.getChildren(node);
//			treeGrid.deselectRecords(nodes);
			if(!isExistSelectedNode(result, node)){
				flag = false;
				return 0;
			}
		}
		for(int i = 0;i<nodes.length;i++){
			String org_id = nodes[i].getAttribute("ID");
			if(ObjUtil.isNotNull(result.get(org_id))){
				treeGrid.selectRecord(nodes[i]);
				defaultFlag = "Y".equals(result.get(org_id));
				int row = treeGrid.getRecordIndex(nodes[i]);
				if(row >= 0 ){
					treeGrid.setEditValue(row, "DEFAULT_FLAG", defaultFlag);
				}
				nodes[i].setAttribute("DEFAULT_FLAG", defaultFlag);
//				openFolderByNode(nodes[i]);
				if(depth > 0){
					depth = findDepth;
					break;
				}
			}else{
				nodes[i].setAttribute("DEFAULT_FLAG", false);
				if(tree.isFolder(nodes[i]) && !(tree.isOpen(nodes[i]) || depth >= findDepth)){
					if(isExistSelectedNode(result, nodes[i])){
						nodeList.add(nodes[i]);
					}
				}
			}
		}
		if(!(nodeList.isEmpty() || depth >= findDepth)){
			depth++;
			for (TreeNode treeNode : nodeList) {
				if(treeGridHandler(result, treeNode, depth) >= findDepth && depth > 1){
					depth = findDepth;
					break;
				}
			}
		}
		flag = false;
		return depth;
	}
	
//	private void openFolderByNode(TreeNode node){
//		if(!(tree.getParent(node) == null || 
//				tree.isOpen(tree.getParent(node)))){
//			openFolderByNode(tree.getParent(node));
//			tree.openFolder(tree.getParent(node));
//		}
//	}
	
	private boolean isExistSelectedNode(Map<String, String> result, TreeNode node){
		Set<String> tempSet = result.keySet();
		for (String str : tempSet) {
			if(tree.isDescendantOf(tree.findById(str), node)){
				return true;
			}
		}
		return false;
	}
	
	private void initverify(){
		check_map.put("TABLE", "SYS_USER");
		check_map.put("USER_ID", StaticRef.CHK_NOTNULL + Util.TI18N.USER_ID());
		check_map.put("USER_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.USER_NAME());
		check_map.put("PASSWORD", StaticRef.CHK_NOTNULL + Util.TI18N.USER_PWD());
		check_map.put("DEFAULT_ORG_ID", StaticRef.CHK_NOTNULL + Util.TI18N.ORG_NAME());
		//check_map.put("ROLE_ID", StaticRef.CHK_NOTNULL + Util.TI18N.ROLE_NAME());
//		check_map.put("USERGRP_ID", StaticRef.CHK_NOTNULL + Util.TI18N.USER_GROUP());
		
		cache_map.put("ACTIVE_FLAG", "Y");
		cache_map.put("DEFAULT_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		cache_map.put("DEFAULT_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		check_system_map.put("TABLE","SYS_USER_SYSTEM");
		check_system_map.put("PRI_SYSTEM",StaticRef.CHK_NOTNULL+"系统");
		check_system_map.put("USER_ID,PRI_SYSTEM",StaticRef.CHK_UNIQUE+"用户"+","+"系统");
		check_system_map.put("USER_ID,DEFAULT_FLAG",StaticRef.CHK_UNIQUE+"用户"+","+"默认值");
		
		cache_system_map.put("DEFAULT_FLAG", "N");
		
	}
	
	private VLayout createMainInfo(){
		VLayout layOut = new VLayout();
		layOut.setWidth100();
		layOut.setHeight100();
		
		DataSource orgDS = OrgDS.getInstance("BAS_ORG");
		treeGrid = new TreeTable(orgDS, "100%", "100%");
		treeGrid.setCanEdit(true);
        treeGrid.setShowHeader(true);
//        treeGrid.setSelectionType(SelectionStyle.MULTIPLE);
        treeGrid.setCanAcceptDroppedRecords(false);
        treeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        treeGrid.setShowSelectedStyle(false);
        treeGrid.setShowPartialSelection(true);
        treeGrid.setCascadeSelection(true);
        treeGrid.setShowResizeBar(false);
        treeGrid.addDrawHandler(new DrawHandler() {  
        	public void onDraw(DrawEvent event) {
        		
        	}  
        }); 
        
        TreeGridField funcField = new TreeGridField();
        funcField.setName("SHORT_NAME");
        funcField.setWidth(240);
        funcField.setCanEdit(false);
        
        TreeGridField funcField1 = new TreeGridField("DEFAULT_FLAG","默认");
        funcField1.setWidth(60);
        funcField1.setCanEdit(false);
        funcField1.setType(ListGridFieldType.BOOLEAN);
        
        treeGrid.setFields(funcField,funcField1);
        
        treeGrid.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				int row = event.getRecordNum();
				Record r = event.getRecord();
				Object defaultFlag = ObjUtil.ifObjNull(treeGrid.getEditValue(row, "DEFAULT_FLAG"), r.getAttribute("DEFAULT_FLAG"));
				boolean flag = (defaultFlag == null || defaultFlag.toString().equals("false")) ? false:true;
				treeGrid.setEditValue(row, "DEFAULT_FLAG", !flag);
			}
		});
        
        Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("PARENT_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		criteria.addCriteria("INCLUDE_PARENT", "Y");
		treeGrid.fetchData(criteria,new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				tree = treeGrid.getTree();
			}
		});
		treeGrid.addFolderOpenedHandler(new FolderOpenedHandler() {
			@Override
			public void onFolderOpened(FolderOpenedEvent event) {
				if(table.getSelectedRecord() == null) return;
            	final TreeNode node = event.getNode();
            	user_id =table.getSelectedRecord().getAttribute("USER_ID");
            	Util.db_async.getUserOrg(user_id, new AsyncCallback<HashMap<String,String>>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(HashMap<String, String> result) {
						treeGridHandler(result, node, 0);
						
					}
				});
//            	LoginCache.getUserOrg(user_id, new AsyncCallback<LinkedHashMap<String, String>>() {
//
//					@Override
//					public void onFailure(Throwable caught) {
//						
//					}
//
//					public void onSuccess(LinkedHashMap<String, String> result) {
//						treeGridHandler(result, node);
//					}
//				});
			}
				
		});
		treeGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(!(flag || event.getState())){
					int rowNum = treeGrid.getRecordIndex(event.getRecord());
					if(rowNum > -1){
						treeGrid.setEditValue(rowNum, "DEFAULT_FLAG", false);
						event.getRecord().setAttribute("DEFAULT_FLAG", false);
					}
				}
				
			}
		});
		
        SGPanel panel = new SGPanel();
        final SGText ORG_NAME = new SGText("ORG_NAME", Util.TI18N.ORG_NAME());
        ORG_NAME.setDisabled(true);
        ORG_NAME.setShowTitle(true);
        ORG_NAME.setWidth(180);
        
        final TextItem ORG_ID = new TextItem("ORG_ID");
        ORG_ID.setVisible(false);
        
        
		IButton saveBtn = new IButton(Util.BI18N.SAVE());
		saveBtn.setIcon(StaticRef.ICON_SAVE);
		saveBtn.setWidth(60);
		saveBtn.setAutoFit(true);
		saveBtn.setAlign(Alignment.RIGHT);
		saveBtn.addClickHandler(new SaveUserOrgAction(treeGrid, table,vm));
		
		
		IButton saveDef = new IButton(StaticRef.SAVE_BTN);
        saveDef.setTitle("设置默认");
        saveDef.addClickHandler(new SaveUserDefOrgAction(treeGrid, table, ORG_ID));
        
        
		IButton cancelBtn = new IButton(Util.BI18N.CANCEL());
		cancelBtn.setIcon(StaticRef.ICON_CANCEL);
		cancelBtn.setWidth(60);
		cancelBtn.setAutoFit(true);
		cancelBtn.setAlign(Alignment.RIGHT);
		cancelBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				treeGrid.discardAllEdits();
			}
		});
		
		
		panel.setItems(ORG_NAME,ORG_ID);
		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
	    toolStrip.setMembers(saveBtn,cancelBtn);  
		
        
        layOut.addMember(treeGrid);
        layOut.addMember(toolStrip);
        return layOut;
        
	}
	
	private VLayout createCustTable(){
		VLayout layOut = new VLayout();
		layOut.setWidth100();
		layOut.setHeight100();
		
		userCustFilter = new SGPanel();
		userCustFilter.setTitleWidth(64);
		userCustFilter.setCellPadding(1);
		userCustFilter.setTitleOrientation(TitleOrientation.LEFT);
		
		TextItem CUSTOMER_CODE_F = new TextItem("CUSTOMER_CODE", "");
		CUSTOMER_CODE_F.setShowTitle(true);
		CUSTOMER_CODE_F.setWidth(140);

		TextItem CUSTOMER_NAME_F = new TextItem("CUSTOMER_NAME", "");
		CUSTOMER_NAME_F.setShowTitle(false);
		CUSTOMER_NAME_F.setWidth(140);
		
		/*
		CUSTOMER_CODE_F.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if (!ObjUtil.isNotNull(event.getForm().getItem("CUSTOMER_CODE").getDisplayValue())) {
					filterUserTable(event.getForm().getItem("CUSTOMER_CODE").getDisplayValue(), 
							event.getForm().getItem("CUSTOMER_NAME").getDisplayValue());
				}
			}
		});
		*/
		
		CUSTOMER_CODE_F.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if ("Enter".equals(event.getKeyName())) {
					filterUserTable(event.getForm().getItem("CUSTOMER_CODE").getDisplayValue(), 
							event.getForm().getItem("CUSTOMER_NAME").getDisplayValue());
				}
			}
		});
		
		CUSTOMER_NAME_F.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if ("Enter".equals(event.getKeyName())) {
					filterUserTable(event.getForm().getItem("CUSTOMER_CODE").getDisplayValue(), 
							event.getForm().getItem("CUSTOMER_NAME").getDisplayValue());
				}
			}
		});
		
		/*
		CUSTOMER_NAME_F.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if (!ObjUtil.isNotNull(event.getForm().getItem("CUSTOMER_NAME").getDisplayValue())) {
					filterUserTable(event.getForm().getItem("CUSTOMER_CODE").getDisplayValue(), 
							event.getForm().getItem("CUSTOMER_NAME").getDisplayValue());
				}
			}
		});
		*/
		
		userCustFilter.setItems(CUSTOMER_CODE_F, CUSTOMER_NAME_F);
		
		layOut.addMember(userCustFilter);
		
		userCustTable = new SGTable(custDS);
		userCustTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		userCustTable.setShowRowNumbers(true);
		userCustTable.setShowFilterEditor(false);
		ListGridField CUSTOMER_CODE = new ListGridField("CUSTOMER_CODE",Util.TI18N.CUSTOMER_CODE(),140);
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME","客户名称",140);
		
		userCustTable.setFields(CUSTOMER_CODE,CUSTOMER_NAME);
		
	        
	        IButton saveBtn = createBtn(StaticRef.SAVE_BTN,SysPrivRef.USERMANAGE_P3_01);
			saveBtn.setIcon(StaticRef.ICON_SAVE);
			saveBtn.setWidth(60);
//			saveBtn.setEndRow(false);
			saveBtn.setAutoFit(true);
			saveBtn.setAlign(Alignment.RIGHT);
			saveBtn.addClickHandler(new SaveUserCustAction(userCustTable, table,this));
			
			IButton cancelBtn = createBtn(StaticRef.CANCEL_BTN,SysPrivRef.USERMANAGE_P3_02);
			cancelBtn.setIcon(StaticRef.ICON_CANCEL);
			cancelBtn.setWidth(60);
//			saveBtn.setEndRow(false);
			cancelBtn.setAutoFit(true);
			cancelBtn.setAlign(Alignment.RIGHT);
			cancelBtn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					userCustTable.discardAllEdits();
				}
			});
	        
			toolStrip2 = new ToolStrip();//按钮布局
			toolStrip2.setAlign(Alignment.RIGHT);
			toolStrip2.setWidth("100%");
			toolStrip2.setHeight("20");
			toolStrip2.setPadding(2);
			toolStrip2.setSeparatorSize(12);
			toolStrip2.addSeparator();
			toolStrip2.setMembersMargin(4);
		    toolStrip2.setMembers(saveBtn,cancelBtn); 
		    
	        layOut.addMember(userCustTable);
//	        layOut.addMember(panel);
	        layOut.addMember(toolStrip2);
	        
	        return layOut;
	}
	
	private void filterUserTable(String codeValue, String nameValue){
		cuscode = codeValue;
		cusname = nameValue;
		Criteria c = userCustTable.getCriteria();
		if(ObjUtil.isNotNull(user_id)){
			c = c == null?new Criteria():c;
			c.addCriteria("CUSTOMER_CODE", codeValue);
			c.addCriteria("CUSTOMER_NAME", nameValue);
			c.addCriteria("USER_ID", user_id);
			c.addCriteria("OP_FLAG", "M");
			
			userCustTable.invalidateCache();
			userCustTable.filterData(c, new DSCallback() {
				
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					ListGridRecord[] rec = userCustTable.getRecords();
					if(rec != null && rec.length > 0) {
						Record record = null;
						for(int i = 0;i < rec.length; i++) {
							record = rec[i];
							if(record.getAttribute("USE_FLAG").equals("Y") || 
									record.getAttribute("USE_FLAG").equals("true")) {
								userCustTable.selectRecord(record);
							}
						}
					}
				}
			});
		}
	}
	
	//仓库页签
	private VLayout createWhseTable(){
		VLayout layOut = new VLayout();
		layOut.setWidth100();
		layOut.setHeight100();
		
		userWhseTable = new SGTable(whseDS);
		userWhseTable.setEditEvent(ListGridEditEvent.CLICK);
		userWhseTable.setShowRowNumbers(true);
		ListGridField CUSTOMER_CODE = new ListGridField("WHSE_CODE",Util.TI18N.WHSE_CODE(),140);
		ListGridField CUSTOMER_NAME = new ListGridField("WHSE_NAME",Util.TI18N.WHSE_NAME(),140);
		ListGridField USE_FLAG = new ListGridField("USE_FLAG","选择",60);
		USE_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField DEFAULT_FLAG = new ListGridField("DEFAULT_FLAG","默认",60);
		DEFAULT_FLAG.setType(ListGridFieldType.BOOLEAN);
		USE_FLAG.setCanEdit(true);
		DEFAULT_FLAG.setCanEdit(true);
		
		userWhseTable.setFields(USE_FLAG,CUSTOMER_CODE,CUSTOMER_NAME,DEFAULT_FLAG);
		DEFAULT_FLAG.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(
					com.smartgwt.client.widgets.grid.events.ChangeEvent event) {
				if(event.getValue().toString().equals("true")){
					userWhseTable.setEditValue(event.getRowNum(), "USE_FLAG", true);
				}
			}
		});
		
		USE_FLAG.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(
					com.smartgwt.client.widgets.grid.events.ChangeEvent event) {
				if(event.getValue().toString().equals("false")){
					userWhseTable.setEditValue(event.getRowNum(), "DEFAULT_FLAG", false);
				}
			}
		});
		
		
	        
	        IButton saveBtn = createBtn(StaticRef.SAVE_BTN,SysPrivRef.USERMANAGE_P4_01);
			saveBtn.setIcon(StaticRef.ICON_SAVE);
			saveBtn.setWidth(60);
			saveBtn.setAutoFit(true);
			saveBtn.setAlign(Alignment.RIGHT);
			saveBtn.addClickHandler(new SaveUserWhseAction(userWhseTable, table));
			
			IButton cancelBtn = createBtn(StaticRef.CANCEL_BTN,SysPrivRef.USERMANAGE_P4_02);
			cancelBtn.setIcon(StaticRef.ICON_CANCEL);
			cancelBtn.setWidth(60);
			cancelBtn.setAutoFit(true);
			cancelBtn.setAlign(Alignment.RIGHT);
			cancelBtn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					userWhseTable.discardAllEdits();
				}
			});
	        
			toolStrip3 = new ToolStrip();//按钮布局
			toolStrip3.setAlign(Alignment.RIGHT);
			toolStrip3.setWidth("100%");
			toolStrip3.setHeight("20");
			toolStrip3.setPadding(2);
			toolStrip3.setSeparatorSize(12);
			toolStrip3.addSeparator();
			toolStrip3.setMembersMargin(4);
		    toolStrip3.setMembers(saveBtn,cancelBtn); 
	        
	        layOut.addMember(userWhseTable);
	        layOut.addMember(toolStrip3);
	        
	        return layOut;
	}
	
//	//经销商页签
//	private VLayout createAddrTable(){
//		VLayout layOut = new VLayout();
//		layOut.setWidth100();
//		layOut.setHeight100();
//		
//		addrTable = new SGTable(addrDS);
//		addrTable.setEditEvent(ListGridEditEvent.CLICK);
//		addrTable.setShowRowNumbers(true);
////		addrTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
//		addrTable.setShowSelectedStyle(true);
//		
//		ListGridField CUSTOMER_CODE = new ListGridField("ADDR_NAME","经销商",140);
//		ListGridField CUSTOMER_NAME = new ListGridField("ADDRESS",Util.TI18N.ADDRESS(),260);
//		addrTable.setFields(CUSTOMER_CODE,CUSTOMER_NAME);
//	        
//	        IButton newBtn = createBtn(StaticRef.CREATE_BTN,SysPrivRef.USERMANAGE_P5_01);
//	        newBtn.setIcon(StaticRef.ICON_NEW);
//	        newBtn.setWidth(60);
//	        newBtn.setAutoFit(true);
//	        newBtn.setAlign(Alignment.RIGHT);
//	        newBtn.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					new UserAddrWin(addrTable, "30%", "30%").getViewPanel().show();
//				}
//			});
//			IButton saveBtn = createBtn(StaticRef.SAVE_BTN,SysPrivRef.USERMANAGE_P5_02);
//			saveBtn.setIcon(StaticRef.ICON_SAVE);
//			saveBtn.setWidth(60);
//			saveBtn.setAutoFit(true);
//			saveBtn.setAlign(Alignment.RIGHT);
//			saveBtn.addClickHandler(new SaveUserAddrAction(addrTable, table));
//			
//	        IButton delBtn = createBtn(StaticRef.DELETE_BTN,SysPrivRef.USERMANAGE_P5_03);
//	        delBtn.setIcon(StaticRef.ICON_DEL);
//			delBtn.setWidth(60);
//			delBtn.setAutoFit(true);
//			delBtn.setAlign(Alignment.RIGHT);
//			 delBtn.addClickHandler(new DeleteUserAddrAction(addrTable,table));
//			
//			IButton cancelBtn = createBtn(StaticRef.CANCEL_BTN,SysPrivRef.USERMANAGE_P5_04);
//			cancelBtn.setIcon(StaticRef.ICON_CANCEL);
//			cancelBtn.setWidth(60);
//			cancelBtn.setAutoFit(true);
//			cancelBtn.setAlign(Alignment.RIGHT);
//			cancelBtn.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					addrTable.invalidateCache();
//					Criteria criteria = new Criteria();
//					criteria.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
//					criteria.addCriteria("USER_ID", user_id);
//					addrTable.fetchData(criteria);
//				}
//			});
//	        
//			toolStrip4 = new ToolStrip();//按钮布局
//			toolStrip4.setAlign(Alignment.RIGHT);
//			toolStrip4.setWidth("100%");
//			toolStrip4.setHeight("20");
//			toolStrip4.setPadding(2);
//			toolStrip4.setSeparatorSize(12);
//			toolStrip4.addSeparator();
//			toolStrip4.setMembersMargin(4);
//		    toolStrip4.setMembers(newBtn,saveBtn,delBtn,cancelBtn); 
//	        
//	        
//	        layOut.addMember(addrTable);
////	        layOut.addMember(panel);
//	        layOut.addMember(toolStrip4);
//	        return layOut;
//	}
	
	//系统页签
	private VLayout createSystemTable(){
		VLayout layOut = new VLayout();
		layOut.setWidth100();
		layOut.setHeight100();
		
		systemTable = new SGTable(systemDS);
		systemTable.setEditEvent(ListGridEditEvent.CLICK);
		systemTable.setShowRowNumbers(true);
		systemTable.setShowSelectedStyle(true);
		
		ListGridField PRI_SYSTEM = new ListGridField("PRI_SYSTEM","系统",140);
		Util.initCodesComboValue(PRI_SYSTEM, "SYS_TYP");
		PRI_SYSTEM.setCanEdit(true);
		ListGridField DEFAULT_FLAG = new ListGridField("DEFAULT_FLAG","默认",60);
		DEFAULT_FLAG.setType(ListGridFieldType.BOOLEAN);
		DEFAULT_FLAG.setCanEdit(true);
		systemTable.setFields(PRI_SYSTEM,DEFAULT_FLAG);
		
		IButton newBtn = createBtn(StaticRef.CREATE_BTN,SysPrivRef.USERMANAGE_P5_01);
        newBtn.setIcon(StaticRef.ICON_NEW);
        newBtn.setWidth(60);
        newBtn.setAutoFit(true);
        newBtn.setAlign(Alignment.RIGHT);
        newBtn.addClickHandler(new NewUserSystemAction(systemTable,table,cache_system_map,this));
        
		IButton saveBtn = createBtn(StaticRef.SAVE_BTN,SysPrivRef.USERMANAGE_P5_02);
		saveBtn.setIcon(StaticRef.ICON_SAVE);
		saveBtn.setWidth(60);
		saveBtn.setAutoFit(true);
		saveBtn.setAlign(Alignment.RIGHT);
		saveBtn.addClickHandler(new SaveAction(systemTable,check_system_map, this));
		
        IButton delBtn = createBtn(StaticRef.DELETE_BTN,SysPrivRef.USERMANAGE_P5_03);
        delBtn.setIcon(StaticRef.ICON_DEL);
		delBtn.setWidth(60);
		delBtn.setAutoFit(true);
		delBtn.setAlign(Alignment.RIGHT);
		delBtn.addClickHandler(new DeleteAction(systemTable));
		
		IButton cancelBtn = createBtn(StaticRef.CANCEL_BTN,SysPrivRef.USERMANAGE_P5_04);
		cancelBtn.setIcon(StaticRef.ICON_CANCEL);
		cancelBtn.setWidth(60);
		cancelBtn.setAutoFit(true);
		cancelBtn.setAlign(Alignment.RIGHT);
		cancelBtn.addClickHandler(new CancelAction(systemTable,this));
        
		toolStrip5 = new ToolStrip();//按钮布局
		toolStrip5.setAlign(Alignment.RIGHT);
		toolStrip5.setWidth("100%");
		toolStrip5.setHeight("20");
		toolStrip5.setPadding(2);
		toolStrip5.setSeparatorSize(12);
		toolStrip5.addSeparator();
		toolStrip5.setMembersMargin(4);
	    toolStrip5.setMembers(newBtn,saveBtn,delBtn,cancelBtn); 
        
        layOut.addMember(systemTable);
        layOut.addMember(toolStrip5);
		return layOut;
	}
	
	/**
	 * 创建主信息的布局
	 * @author yuanlei
	 */
	public void createForm(DynamicForm form) {
		SGText USER_ID = new SGText("USER_ID", Util.TI18N.USER_ID(),true);//用户编号
		USER_ID.setDisabled(true);
		USER_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.USER_ID()));
		SGText USER_NAME = new SGText("USER_NAME", Util.TI18N.USER_NAME(),true);//姓名
		USER_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.USER_NAME()));
		
		final PasswordItem PASSWORD = new PasswordItem("PASSWORD", Util.TI18N.USER_PWD());//密码
		PASSWORD.setColSpan(2);
		PASSWORD.setWidth(120);
		PASSWORD.setTitleOrientation(TitleOrientation.TOP);
		PASSWORD.addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				PASSWORD.selectValue();
			}
		});
		
		SGCombo ROLE_ID = new SGCombo("ROLE_ID", Util.TI18N.ROLE_NAME());//角色名称
		Util.initComboValue(ROLE_ID, "SYS_ROLE", "ID", "ROLE_NAME", " WHERE ENABLE_FLAG='Y'","", "");
		
		SGCombo USER_GROUP = new SGCombo("USERGRP_ID", Util.TI18N.USER_GROUP());//用户组
		Util.initComboValue(USER_GROUP, "SYS_USERGRP", "ID", "GRP_NAME");
		
		SGText TEL = new SGText("TEL", Util.TI18N.USER_TEL(),true);//电话
		SGText EMAIL = new SGText("EMAIL", Util.TI18N.USER_EMAIL());//邮箱
		
		SGText DEFAULT_ORG_ID_NAME = new SGText("DEFAULT_ORG_ID_NAME", Util.TI18N.USER_ORG_ID(),true);//默认组织
//		DEFAULT_ORG_ID_NAME.setWidth(140);
		TextItem DEFAULT_ORG_ID =new TextItem("DEFAULT_ORG_ID");
		DEFAULT_ORG_ID.setVisible(false);
		DEFAULT_ORG_ID_NAME.setDisabled(true);
		
//		Util.initOrg(DEFAULT_ORG_ID_NAME, DEFAULT_ORG_ID,true,"40%","40%");
		
		SGCheck ACTIVE_FLAG = new SGCheck("ACTIVE_FLAG",Util.TI18N.ENABLE_FLAG());//激活
		
		SGText UDF1 = new SGText("UDF1", Util.TI18N.USER_UDF1(),true);//预留1
		SGText UDF2 = new SGText("UDF2", Util.TI18N.USER_UDF2());//预留2
//		SGLText DESCR = new SGLText("DESCR", Util.TI18N.USER_DESCR(),true);
		
		form.setFields(USER_ID,ACTIVE_FLAG, USER_NAME, PASSWORD, TEL, EMAIL, DEFAULT_ORG_ID_NAME,
				ROLE_ID,USER_GROUP,UDF1, UDF2,DEFAULT_ORG_ID);
	}
	
	private VLayout createBtnWidgetByMain(){
		VLayout layOut = new VLayout();
		layOut.setWidth100();
		layOut.setHeight100();
		
//		IButton newButton = createBtn(StaticRef.CREATE_BTN,SysPrivRef.USERMANAGE_P0_01);
//        newButton.addClickHandler(new NewUserAction(vm, cache_map, this));
//        
//        IButton saveButton = createBtn(StaticRef.SAVE_BTN,SysPrivRef.USERMANAGE_P0_02);
//        saveButton.addClickHandler(new SaveUserAction(table,vm,check_map,this));
//        
//        IButton delButton = createBtn(StaticRef.DELETE_BTN,SysPrivRef.USERMANAGE_P0_03);
//        delButton.addClickHandler(new DeleteProAction(table, mainForm));
//        
//        IButton canButton = createBtn(StaticRef.CANCEL_BTN,SysPrivRef.USERMANAGE_P0_04);
//        canButton.addClickHandler(new CancelMultiFormAction(table,vm,this));
//        canButton.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				vm.getItem("USER_ID").setDisabled(true);
//			}
//		});
//        
//        add_map.put(SysPrivRef.USERMANAGE_P0_01, newButton);
//        del_map.put(SysPrivRef.USERMANAGE_P0_03, delButton);
//        save_map.put(SysPrivRef.USERMANAGE_P0_02, saveButton);
//        save_map.put(SysPrivRef.USERMANAGE_P0_04, canButton);
//        
//        this.enableOrDisables(add_map, true);
//        enableOrDisables(del_map, false);
//        this.enableOrDisables(save_map, false);
		
//		ToolStrip toolStripMain = new ToolStrip();//按钮布局
//		toolStripMain.setAlign(Alignment.RIGHT);
//		toolStripMain.setWidth("100%");
//		toolStripMain.setHeight("20");
//		toolStripMain.setPadding(2);
//		toolStripMain.setSeparatorSize(12);
//		toolStripMain.addSeparator();
//		toolStripMain.setMembersMargin(4);
//		toolStripMain.setMembers(newButton, saveButton, delButton, canButton);
		
		layOut.addMember(mainForm);
//		layOut.addMember(toolStripMain);
		return layOut;
	}

	/**
	 * 配置显示的列名
	 * @author yuanlei
	 */
	public void createListField(final SGTable table) {
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(SelectionEvent event) {
            	Record selectedRecord  = event.getRecord();
            	if(selectedRecord == null){
            		selectedRecord = table.getSelectedRecord();
            	}
                user_id = selectedRecord.getAttributeAsString("USER_ID");
            	vm.editRecord(selectedRecord);
            	vm.setValue("OP_FLAG", "M");
				tableSelectedChanged(pageNum);
				initSaveBtn();
            }
        });
		
		
		  ListGridField USER_ID = new ListGridField("USER_ID", Util.TI18N.USER_ID(), 80); 
		  ListGridField USER_NAME = new ListGridField("USER_NAME", Util.TI18N.USER_NAME(),80);
		  ListGridField CUR_STATUS = new ListGridField("CUR_STATUS",Util.TI18N.CUR_STATUS(),90);
		  ListGridField ROLE_ID = new ListGridField("ROLE_ID_NAME",Util.TI18N.ROLE_NAME(),100);
		  ListGridField USER_GROUP = new ListGridField("USERGRP_ID_NAME",Util.TI18N.USER_GROUP(),100); 
		  ListGridField DEFAULT_ORG_ID = new ListGridField("DEFAULT_ORG_ID_NAME", Util.TI18N.USER_ORG_ID(),80);
		  ListGridField ACTIVE_FLAG = new ListGridField("ACTIVE_FLAG", Util.TI18N.ENABLE_FLAG(),60);
		  ACTIVE_FLAG.setType(ListGridFieldType.BOOLEAN);//设置复选框
		  
		  ListGridField TEL = new ListGridField("TEL",Util.TI18N.USER_TEL(),120);  
		  ListGridField MAIL = new ListGridField("EMAIL", Util.TI18N.USER_EMAIL(),160);
		  ListGridField UDF1 = new ListGridField("UDF1", Util.TI18N.USER_UDF1(),80);
		  ListGridField UDF2 = new ListGridField("UDF2", Util.TI18N.USER_UDF2());
		  table.setFields(USER_ID, USER_NAME, CUR_STATUS,ROLE_ID,USER_GROUP, DEFAULT_ORG_ID, ACTIVE_FLAG, TEL, MAIL, UDF1, UDF2);
		  
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.addSeparator();
        
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,SysPrivRef.USERMANAGE);
        searchButton.addClickHandler(new ClickHandler() {
			/**
			 * 嵌套，事件，点击事件触发，获取记录集。
			 */
			@Override
			public void onClick(ClickEvent event) {
				
				
				if(searchWin==null){
					searchForm = new SGPanel();
					searchWin = new SearchWin(userDS, 
							createSerchForm(searchForm), section.getSection(0),vm).getViewPanel();
				}else{
					searchWin.show();
				}
			}

			private DynamicForm createSerchForm(DynamicForm searchForm) {
				
				//第一行：模糊查询
				TextItem USER_GLOBAL = new TextItem("CONTENT", Util.TI18N.FUZZYQRY());
				USER_GLOBAL.setTitleOrientation(TitleOrientation.TOP);
				USER_GLOBAL.setWidth(375);
				USER_GLOBAL.setColSpan(5);
				USER_GLOBAL.setEndRow(true);
				
				//第二行：用户编号查询：
				SGText USER_ID = new SGText("USER_ID",Util.TI18N.USER_ID(),true);
//				USER_ID.setTitleOrientation(TitleOrientation.LEFT);
				
				//第二行：用户名查询：
				SGText USER_NAME = new SGText("USER_NAME",Util.TI18N.USER_NAME());
//				USER_NAME.setEndRow(true);
				
				//第三行：默认组织
				SGText USER_ORG_ID_NAME = new  SGText("USER_ORG_ID_NAME",Util.TI18N.USER_ORG_ID(),true);
				USER_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
				
				TextItem USER_ORG_ID = new TextItem("USER_ORG_ID");
				USER_ORG_ID.setVisible(false);
				USER_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
				
				Util.initOrg(USER_ORG_ID_NAME, USER_ORG_ID, false, "30%", "38%");
				
//				Util.initComboValue(USER_ORG_ID, "BAS_ORG", "ID", "ORG_CNAME", " WHERE ENABLE_FLAG='Y'");
				
				//第三行：激活
				SGCheck CHK_ENABLE = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());
				CHK_ENABLE.setValue(true);
				
				SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
				C_ORG_FLAG.setValue(true);//包含下级机构
				C_ORG_FLAG.setColSpan(2);
				
				searchForm.setItems(USER_GLOBAL,USER_ID,USER_NAME,USER_ORG_ID_NAME,CHK_ENABLE,USER_ORG_ID,C_ORG_FLAG);
				
				return searchForm;
			}
		});
		
		IButton newButton = createBtn(StaticRef.CREATE_BTN,SysPrivRef.USERMANAGE_P0_01);
        newButton.addClickHandler(new NewUserAction(vm, cache_map, this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,SysPrivRef.USERMANAGE_P0_02);
        saveButton.addClickHandler(new SaveUserAction(table,vm,check_map,this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,SysPrivRef.USERMANAGE_P0_03);
       // delButton.setID(delButton.getID()+"_delButton");
        delButton.addClickHandler(new DeleteProAction(table, mainForm));
//        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,SysPrivRef.USERMANAGE_P0_04);
        canButton.addClickHandler(new CancelMultiFormAction(table,vm,this));
        canButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				vm.getItem("USER_ID").setDisabled(true);
			}
		});
        
        toolStrip.setMembersMargin(4);
//        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton);
        toolStrip.setMembers(searchButton,newButton,saveButton,delButton,canButton);
        
        
        add_map.put(SysPrivRef.USERMANAGE_P0_01, newButton);
        del_map.put(SysPrivRef.USERMANAGE_P0_03, delButton);
        save_map.put(SysPrivRef.USERMANAGE_P0_02, saveButton);
        save_map.put(SysPrivRef.USERMANAGE_P0_04, canButton);
        
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
//        add_map.put(SysPrivRef.USERMANAGE_P0_01 + "2", newButton);
//        del_map.put(SysPrivRef.USERMANAGE_P0_03+"2", delButton);
//        save_map.put(SysPrivRef.USERMANAGE_P0_02, saveButton);
//        save_map.put(SysPrivRef.USERMANAGE_P0_04, canButton);
//        
//        this.enableOrDisables(add_map, true);
//        enableOrDisables(del_map, false);
//        this.enableOrDisables(save_map, false);
	}
	
	@Override
	public void onDestroy() {
		if(searchWin!=null){
			searchWin.destroy();
			searchForm.destroy();
		}
		
	}
	@Override
	public void initVerify() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setButtonItemEnabled(String id, ButtonItem action, boolean b) {
		if(id.equals(SysPrivRef.USERMANAGE_P0_01 + "2")){
			id = SysPrivRef.USERMANAGE_P0_01;
		}
		super.setButtonItemEnabled(id, action, b);
	}
	
	@Override
	public void setButtonEnabled(String id, IButton action, boolean b) {
		if(id.equals(SysPrivRef.USERMANAGE_P0_01 + "2")){
			id = SysPrivRef.USERMANAGE_P0_01;
		}
		super.setButtonEnabled(id, action, b);
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		UserView view = new UserView();
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