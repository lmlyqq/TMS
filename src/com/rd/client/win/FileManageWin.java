package com.rd.client.win;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.ds.system.FileManageDS;
import com.rd.client.view.system.SQLExecuteView;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.RightMouseDownEvent;
import com.smartgwt.client.widgets.events.RightMouseDownHandler;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.TileRecord;
import com.smartgwt.client.widgets.tile.events.SelectionChangedEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class FileManageWin extends Window{
	
	public TreeGrid treeGrid;
	public TileGrid tileGrid;
	public Tree tree;
	public DataSource treeDs;
	public StaticTextItem weiZhi;
	public StaticTextItem status;
	public TreeNode selectedNode;
	private Map<String, String> defaultParamMap = new HashMap<String, String>();
	public String textValue;
	public static final int NEW_DIR_OPERATE = 0;
	public static final int RENAME_DIR_OPERATE = -1;
	public static final int DEL_DIR_OPERATE = -2;
	public static final int NEW_FILE_OPERATE = 1;
	public static final int VIEW_FILE_OPERATE = 2;
	public static final int DEL_FILE_OPERATE = 3;
	public static final int EXEC_FILE_OPERATE = 4;
	public static final int FORM_TYPE_NEW = 0;
	public static final int FORM_TYPE_VIEW = 1;
	public static final int FORM_TYPE_UPLOAD = 2;
	
	public FileManageWin(){
		setTitle("文件管理");
        setWidth(Page.getWidth());
        setHeight(Page.getHeight());
		setCanDragResize(true);
		
		SGPanel panel = new SGPanel();
		panel.setWidth100();
		panel.setHeight100();
		panel.setTitleWidth(75);
		
		HStack hStack = new HStack();
		hStack.setWidth100();
		hStack.setHeight("95%");
		
		VStack vStack = new VStack();
		vStack.setWidth100();
		vStack.setHeight("5%");
		
		treeDs = FileManageDS.getInstance("FileManage");
		treeGrid = new TreeGrid();
		treeGrid.setLoadDataOnDemand(false);
		treeGrid.setDataSource(treeDs);
		treeGrid.setWidth("20%");
		treeGrid.setHeight("100%");
		treeGrid.setShowHeader(false);
        treeGrid.setCanEdit(true);
        treeGrid.setCanAcceptDroppedRecords(false);
        treeGrid.setShowSelectedStyle(false);
        treeGrid.setShowPartialSelection(true);
        treeGrid.setCascadeSelection(true);
        treeGrid.setShowResizeBar(false);
        treeGrid.setCanSelectAll(true);
        treeGrid.setNodeIcon("rd/folder_file.png");

        createTreeGridFields();
        
        tileGrid = new TileGrid();
        tileGrid.setTileHeight(72);
        tileGrid.setTileWidth(180);
        tileGrid.setWidth("80%");
        tileGrid.setHeight100();
        tileGrid.setDataSource(treeDs);
        tileGrid.setShowResizeBar(false);
        
        createTreeGridDetailFields();
		
        ToolStrip footToolStrip = new ToolStrip();
        footToolStrip.setAlign(Alignment.LEFT);
        footToolStrip.setMinHeight(30);
        footToolStrip.setWidth100();
        footToolStrip.setHeight(30);
        weiZhi = new StaticTextItem("WeiZhi", "当前位置");
        weiZhi.setTitleOrientation(TitleOrientation.LEFT);
        weiZhi.setWrapTitle(false);
        weiZhi.setWrap(false);
        weiZhi.setValue("D:/");
        footToolStrip.addFormItem(weiZhi);
        
        status = new StaticTextItem("status");
        status.setWidth("50%");
        status.setShowTitle(false);
        status.setAlign(Alignment.RIGHT);
        status.setWrapTitle(false);
        status.setWrap(false);
        status.setValueField("STATUS_VALUE");
        status.setDisplayField("STATUS_DISPLAY");
        footToolStrip.addFormItem(status);
        
        status.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				SC.say("<div style='width: 620px; max-height:480px;overflow:auto;'>"+status.getAttribute("STATUS_VALUE")+"</div>");
			}
		});
        
		hStack.setMembers(treeGrid,tileGrid);
		vStack.setMembers(footToolStrip);
        VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.addMember(hStack);
		main.addMember(vStack);
		//addItem(panel);
		addItem(main);
		draw();
	}
	
	private void createTreeGridFields(){
		treeGrid.addDrawHandler(new DrawHandler() {  
        	public void onDraw(DrawEvent event) { 
        		Criteria criteria = new Criteria();
        		treeGrid.invalidateCache();
        		treeGrid.getDataSource().setDefaultParams(null);
        		treeGrid.fetchData(criteria,new DSCallback() {
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						tree = treeGrid.getTree();
					}
				}); 
        		
        	}  
        }); 
		
		treeGrid.addNodeClickHandler(new NodeClickHandler() {
			
			@Override
			public void onNodeClick(NodeClickEvent event) {
				final TreeNode node = event.getNode();
				if(node == null || treeGrid.getSelectedRecord() == null)return ;
				final String filePath = node.getAttribute("FILE_PATH");
				if(filePath == null || (selectedNode != null && 
						filePath.equals(selectedNode.getAttribute("FILE_PATH")))) 
					return;
				weiZhi.setValue(filePath);
				Criteria c = new Criteria();
				c.addCriteria("FILE_PATH", filePath);
				tileGrid.invalidateCache();
				tileGrid.getDataSource().setDefaultParams(null);
				tileGrid.fetchData(c, new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						selectedNode = node;
					}
				});
				
				
			}
		});
		
		treeGrid.addFolderOpenedHandler(new FolderOpenedHandler() {
			
			@Override
			public void onFolderOpened(FolderOpenedEvent event) {
				TreeNode[] childNodes = tree.getChildren(event.getNode());
				for (TreeNode treeNode : childNodes) {
					defaultParamMap.put("PARENT_ID", treeNode.getAttribute("ID"));
					treeDs.setDefaultParams(defaultParamMap);
					tree.loadChildren(treeNode);
				}
			}
		});
		
		treeGrid.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				Record r = event.getRecord();
				if(r != null && ObjUtil.isNotNull(event.getNewValue())){
					String fileName = r.getAttribute("FILE_NAME");
					String filePath = r.getAttribute("FILE_PATH");
					if(!event.getNewValue().equals(fileName)){
						fileOperate(RENAME_DIR_OPERATE, "rename " + filePath + " " + event.getNewValue(), null);
					}
				}
			}
		});
		
		TreeGridField FILE_NAME = new TreeGridField("FILE_NAME");
		TreeGridField FILE_PATH = new TreeGridField("FILE_PATH");
		FILE_PATH.setHidden(true);
		TreeGridField PARENT_ID = new TreeGridField("PARENT_ID");
		PARENT_ID.setHidden(true);
		
		treeGrid.setFields(FILE_NAME, FILE_PATH, PARENT_ID);
		
		addtTreeGridRightKey();
	}
	
	private void createTreeGridDetailFields(){
		tileGrid.addDrawHandler(new DrawHandler() {  
        	public void onDraw(DrawEvent event) { 
        		if(selectedNode == null)return;
        		Criteria criteria = new Criteria();
        		criteria.addCriteria("FILE_PATH", selectedNode.getAttribute("FILE_PATH"));
        		tileGrid.invalidateCache();
        		tileGrid.getDataSource().setDefaultParams(null);
        		tileGrid.fetchData(criteria); 
        	}  
        }); 
		
		tileGrid.addSelectionChangedHandler(new com.smartgwt.client.widgets.tile.events.SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Record record = event.getRecord();
				if(record != null){
					weiZhi.setValue(record.getAttribute("ID"));
				}
				
			}
		});
		
		DetailViewerField PICTURE_FIELD = new DetailViewerField("PICTURE");
		PICTURE_FIELD.setType("image");
		PICTURE_FIELD.setImageURLPrefix("rd/");
		PICTURE_FIELD.setImageHeight(32);
		PICTURE_FIELD.setImageWidth(32);
		
		DetailViewerField FILE_NAME = new DetailViewerField("FILE_NAME");
		DetailViewerField FILE_SIZE = new DetailViewerField("FILE_SIZE");
		DetailViewerField LAST_MODIFIED = new DetailViewerField("LAST_MODIFIED");
		tileGrid.setFields(PICTURE_FIELD, FILE_NAME, FILE_SIZE, LAST_MODIFIED);
		
		addtTileGridRightKey();
	}
	
	private void addtTreeGridRightKey(){
		final Menu menu = new Menu();   //文件夹右键
	    menu.setWidth(140);
	    MenuItemSeparator itemSeparator =new MenuItemSeparator();
	    
	    MenuItem addItem = new MenuItem("新增目录");
	    addItem.setKeyTitle("Alt+N");
	    KeyIdentifier addKey = new KeyIdentifier();
	    addKey.setAltKey(true);
	    addKey.setKeyName("N");
	    addItem.setKeys(addKey);
	    menu.addItem(addItem);
	    addItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				new MinWindow("新增目录", 0, new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						if(selectedNode != null){
							String filePath = selectedNode.getAttribute("FILE_PATH");
							fileOperate(NEW_DIR_OPERATE, "create_dir "+filePath+" "+textValue, null);
						}
					}
				});
				
			}
		});
	    
	    MenuItem editItem = new MenuItem("编辑目录");
	    editItem.setKeyTitle("Alt+E");
	    KeyIdentifier editKey = new KeyIdentifier();
	    editKey.setAltKey(true);
	    editKey.setKeyName("E");
	    editItem.setKeys(editKey);
	    menu.addItem(editItem);
	    
	    editItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				MSGUtil.sayInfo("功能开发中...");
			}
		});
	    
	    MenuItem delItem = new MenuItem("删除目录");
	    delItem.setKeyTitle("Alt+D");
	    KeyIdentifier delKey = new KeyIdentifier();
	    delKey.setAltKey(true);
	    delKey.setKeyName("D");
	    delItem.setKeys(delKey);
	    menu.addItem(delItem);
	    
	    delItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				if(selectedNode != null){
					SC.confirm("删除文件", "确定要删除吗?", new BooleanCallback() {
						public void execute(Boolean value) {
	                    	String id = selectedNode.getAttribute("ID");
	    					fileOperate(DEL_DIR_OPERATE, "delete "+id, null);
		                }
		            });
					
				}
			}
		});
	    
	    menu.addItem(itemSeparator);
	    
	    MenuItem saveItem = new MenuItem("保存目录");
	    saveItem.setKeyTitle("Alt+S");
	    KeyIdentifier saveKey = new KeyIdentifier();
	    saveKey.setAltKey(true);
	    saveKey.setKeyName("S");
	    saveItem.setKeys(saveKey);
	    menu.addItem(saveItem);
	    
	    saveItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				MSGUtil.sayInfo("功能开发中...");
			}
		});
	    
	    treeGrid.setContextMenu(menu);
	    
	    treeGrid.addRightMouseDownHandler(new RightMouseDownHandler() {
			
			@Override
			public void onRightMouseDown(RightMouseDownEvent event) {
				menu.showContextMenu();
				event.cancel();
			}
		});
	}
	
	private void addtTileGridRightKey(){
		final Menu menu = new Menu();   //文件右键
	    menu.setWidth(140);
	    MenuItemSeparator itemSeparator =new MenuItemSeparator();
	    
	    MenuItem addItem = new MenuItem("新增文件");
	    addItem.setKeyTitle("Alt+N");
	    KeyIdentifier addKey = new KeyIdentifier();
	    addKey.setAltKey(true);
	    addKey.setKeyName("N");
	    addItem.setKeys(addKey);
	    menu.addItem(addItem);
	    addItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				new MinWindow("新增文件", 0, new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						if(selectedNode != null){
							String filePath = selectedNode.getAttribute("FILE_PATH");
							fileOperate(NEW_FILE_OPERATE, "create "+filePath+" "+textValue, null);
						}
					}
				});

			}
		});
	    
	    MenuItem editItem = new MenuItem("查看文件");
	    editItem.setKeyTitle("Alt+E");
	    KeyIdentifier editKey = new KeyIdentifier();
	    editKey.setAltKey(true);
	    editKey.setKeyName("E");
	    editItem.setKeys(editKey);
	    menu.addItem(editItem);
	    
	    editItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				final MinWindow mw = new MinWindow("查看文件", 1, new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
					}
				});
				TileRecord record = tileGrid.getSelectedRecord();
				if(record != null){
					String id = record.getAttribute("ID");
					fileOperate(VIEW_FILE_OPERATE, "view "+id, mw);
				}
			}
		});
	    
	    MenuItem delItem = new MenuItem("删除文件");
	    delItem.setKeyTitle("Alt+D");
	    KeyIdentifier delKey = new KeyIdentifier();
	    delKey.setAltKey(true);
	    delKey.setKeyName("D");
	    delItem.setKeys(delKey);
	    menu.addItem(delItem);
	    
	    delItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				final TileRecord record = tileGrid.getSelectedRecord();
				if(record != null){
					SC.confirm("删除文件", "确定要删除吗?", new BooleanCallback() {
						public void execute(Boolean value) {
	                    	String id = record.getAttribute("ID");
	    					fileOperate(DEL_FILE_OPERATE, "delete "+id, null);
		                }
		            });
					
				}
			}
		});
	    
	    MenuItem execItem = new MenuItem("执行SQL文件");
	    execItem.setKeyTitle("Alt+Z");
	    KeyIdentifier execKey = new KeyIdentifier();
	    execKey.setAltKey(true);
	    execKey.setKeyName("Z");
	    execItem.setKeys(execKey);
	    menu.addItem(execItem);
	    
	    execItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				final TileRecord record = tileGrid.getSelectedRecord();
				if(record != null){
					SC.confirm("执行SQL文件", "确定要执行吗?", new BooleanCallback() {
						public void execute(Boolean value) {
	                    	String id = record.getAttribute("ID");
	    					fileOperate(EXEC_FILE_OPERATE, "execute "+id, null);
		                }
		            });
					
				}
			}
		});
	    
	    menu.addItem(itemSeparator);
	    
	    MenuItem saveItem = new MenuItem("保存文件");
	    saveItem.setKeyTitle("Alt+S");
	    KeyIdentifier saveKey = new KeyIdentifier();
	    saveKey.setAltKey(true);
	    saveKey.setKeyName("S");
	    saveItem.setKeys(saveKey);
	    menu.addItem(saveItem);
	    
	    saveItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				MSGUtil.sayInfo("功能开发中...");
			}
		});
	    
	    MenuItem updateItem = new MenuItem("上传文件");
	    updateItem.setKeyTitle("Alt+U");
	    KeyIdentifier updateKey = new KeyIdentifier();
	    updateKey.setAltKey(true);
	    updateKey.setKeyName("U");
	    updateItem.setKeys(updateKey);
	    menu.addItem(updateItem);
	    
	    updateItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				if(selectedNode != null){
					new MinWindow("上传文件", 2, null);
				}
			}
		});
	    
	    MenuItem refreshItem = new MenuItem("刷新");
	    refreshItem.setKeyTitle("Alt+F");
	    KeyIdentifier refreshKey = new KeyIdentifier();
	    refreshKey.setAltKey(true);
	    refreshKey.setKeyName("F");
	    refreshItem.setKeys(refreshKey);
	    menu.addItem(refreshItem);
	    
	    refreshItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				if(selectedNode != null){
					refresh(1);
				}
			}
		});
	    
	    tileGrid.setContextMenu(menu);
	    
	    tileGrid.addRightMouseDownHandler(new RightMouseDownHandler() {
			
			@Override
			public void onRightMouseDown(RightMouseDownEvent event) {
				menu.showContextMenu();
				event.cancel();
			}
		});
	}
	
	
	private void fileOperate(final int operateType, final String filePath, final MinWindow mw){
		Util.async.executeScript("file\n"+filePath, new AsyncCallback<List<Map<String,Object>>>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(List<Map<String, Object>> result) {
				if(result == null || result.isEmpty()) return;
				StringBuilder sbAll = new StringBuilder();
				for (Map<String, Object> resultMap : result) {
					if(resultMap.get(SQLExecuteView.RESULT_MSG_KEY) != null){
						String resultMsg = (String)resultMap.get(SQLExecuteView.RESULT_MSG_KEY);
	    				sbAll.append(resultMsg);
	    				sbAll.append("; ");
	    				if(resultMap.get(SQLExecuteView.ERROR_LINE_KEY) != null){
		    				sbAll.append(resultMap.get(SQLExecuteView.ERROR_LINE_KEY));
		    				sbAll.append("; ");
		    			}
	    				if(resultMap.get(SQLExecuteView.E_KEY) != null){
	    					sbAll.append(resultMap.get(SQLExecuteView.E_KEY));
	    					sbAll.append("; ");
		    			}
					}
					if(resultMap.get(SQLExecuteView.COLUMNS_KEY) != null){
						sbAll.append(resultMap.get(SQLExecuteView.COLUMNS_KEY));
						sbAll.append(": ");
						if(resultMap.get(SQLExecuteView.VALUES_KEY) != null){
							List<String> vals = (List<String>)resultMap.get(SQLExecuteView.VALUES_KEY);
							if(!vals.isEmpty()){
								for (String val : vals) {
									sbAll.append(val);
									if(operateType == VIEW_FILE_OPERATE && mw != null){
										mw.textArea.setValue(ObjUtil.ifObjNull(mw.textArea.getValue(), "").toString() + val);
									}
								}
								
							}
							
						}
					}
				}
				String value = toHtml(sbAll.toString());
				if(sbAll.length() > 50){
					sbAll.delete(50, sbAll.length());
				}
				status.setAttribute("STATUS_VALUE", value);
				status.setValue(toHtml(sbAll.toString()));
				refresh(operateType);
			}
			
		});
	}
	
	private void refresh(int type){
		if(type <= 0){
			treeGrid.draw();
		}else{
			tileGrid.draw();
		}
	}
	
	private String toHtml(String str){
		if(str != null){
			str = str.replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt;");
		}
		return str;
	}
	
	class MinWindow extends Window{
		
		public UploadItem pathItem;
		public SGPanel panel;
		public TextItem text;
		public TextAreaItem textArea;
		
		public MinWindow getThis(){
			return this;
		}
		
		public MinWindow(String title, int formType, final ClickHandler clickHandler){
			setTitle(title);
			setTop("50%");
			setLeft("50%");
			if(formType == FORM_TYPE_NEW){
				setWidth(260);
		        setHeight(100);
			}else if(formType == FORM_TYPE_VIEW){
				setTop("20%");
				setLeft("20%");
				setWidth(620);
		        setHeight(340);
			}else if(formType == FORM_TYPE_UPLOAD){
				setWidth(480);
		        setHeight(100);
			}
	        
			setCanDragResize(true);
			
			panel = new SGPanel();
			panel.setWidth100();
			panel.setHeight100();
			panel.setTitleWidth(75);
			panel.setNumCols(2);
			if(formType == FORM_TYPE_UPLOAD){
				panel.setAction("fileManageServlet?IS_UPLOAD=Y&PATH="+selectedNode.getAttribute("ID"));
				panel.setEncoding(Encoding.MULTIPART);
				panel.setMethod(FormMethod.POST);
				panel.setTarget("foo");
				
			}
			
			text = new TextItem("textValue", "请输入:");
			text.setVisible(formType == FORM_TYPE_NEW);
			text.setColSpan(2);
			text.setWidth(200);
			text.setStartRow(true);
			
			textArea = new TextAreaItem("textAreaValue", "请输入:");
			textArea.setTitleOrientation(TitleOrientation.TOP);
			textArea.setHeight(260);
			textArea.setWidth(560);
			textArea.setVisible(formType == FORM_TYPE_VIEW);
			textArea.setColSpan(2);
			textArea.setStartRow(true);
			
			IButton submitButton = new IButton("确定");
				
			submitButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(text.getVisible()){
						textValue = ObjUtil.ifObjNull(text.getValue(), "").toString();
					}else if(textArea.getVisible()){
						textValue = ObjUtil.ifObjNull(textArea.getValue(), "").toString();
					}else if(pathItem != null){
						if(ObjUtil.isNotNull(pathItem.getValue())){
							panel.submitForm();
							getThis().hide();
							return;
						}else{
							MSGUtil.sayWarning("请选择所要上传的文件");
						}
					}
					getThis().hide();
					if(clickHandler != null)clickHandler.onClick(event);
				}
			});
			
			ToolStrip toolStrip = new ToolStrip();//按钮布局
			toolStrip.setWidth("100%");
			toolStrip.setHeight("20");
			toolStrip.setPadding(2);
			toolStrip.setSeparatorSize(8);
			toolStrip.setMembersMargin(5);
			toolStrip.setAlign(Alignment.RIGHT);
			toolStrip.setAlign(VerticalAlignment.BOTTOM);
	        toolStrip.setMembers(submitButton);
			
	        if(formType == FORM_TYPE_UPLOAD){
	        	final TextItem uploadText = new TextItem("uploadTextItem");	//用于接收结果的字段
				uploadText.setWidth(10);
				uploadText.setHeight(10);
				uploadText.setCellStyle("display_none");
				uploadText.setShowTitle(false);
				
				pathItem = new UploadItem("filePath","路径");
				pathItem.setWidth(320);
				pathItem.setStartRow(true);
				pathItem.setColSpan(2);
				
				uploadText.addChangedHandler(new ChangedHandler() {
					
					@Override
					public void onChanged(ChangedEvent event) {
						showResult(event.getValue());
						event.getItem().setValue("");
					}
				});
				
				panel.setItems(uploadText, pathItem);
				
				HTMLPane htmlPane = new HTMLPane();
				htmlPane.setContentsType(ContentsType.PAGE);
				htmlPane
						.setContents("<iframe name='foo' id='foo' style='position:absolute;width:0;height:0;border:0' onload='javascript:displayUploadResult(this)'></iframe>");
				htmlPane.setWidth("1");
				htmlPane.setHeight("1");
				addItem(htmlPane);
	        }else{
	        	panel.setItems(text, textArea);
	        }
			
			addItem(panel);
			
			addItem(toolStrip);
			draw();
		}
		
		private void showResult(Object result){
			if(result != null){
				String resultCode = result.toString().substring(0, 2);
				if("00".equals(resultCode)){
					status.setValue("执行结果: Success; 上传成功!");
					//刷新列表
					refresh(1);
				}else if("01".equals(resultCode)){
					status.setValue("执行结果: Failure; 上传失败!");
				}else{
					status.setValue("执行结果: Failure; 上传失败!");
				}
			}
		}
	}
}
