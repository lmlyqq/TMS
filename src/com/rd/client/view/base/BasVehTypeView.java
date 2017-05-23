package com.rd.client.view.base;

import com.google.gwt.core.client.JavaScriptObject;
import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteProAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.GetHintAction;
import com.rd.client.common.action.NewAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.CarModerManagerDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.UploadFileWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 车型管理
 * @author lijun
 * 
 *  @param CarDS
 *  @param table
 *  @param searchWin
 *  @param searchForm
 *  @param searchItem
 */
@ClassForNameAble
public class BasVehTypeView extends SGForm implements PanelFactory {
    private DataSource CarDS;
    private SGTable table;
    private Window searchWin;
	private SGPanel searchForm;
	private SectionStack section;
	private Window uploadWin;
	
	/*public BasVehTypeView(String id) {
		super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolstrip = new ToolStrip();
		toolstrip.setAlign(Alignment.RIGHT);
		//数据源在这里必须用两个，因为该处的数据从数据库中拉出来时可能需要进行分页操作。所以需要两个数据源
		CarDS = CarModerManagerDS.getInstance("V_VEHICLE_TYPE","BAS_VEHICLE_TYPE");
		
		table = new SGTable(CarDS,"100%","70%");
		createListFields(table);
		table.setShowFilterEditor(false);
		
		
		createBtnWidget(toolstrip);
		

		section = createSection(table, null,true,true); 
		
		initVerify();
		VLayout layout = new VLayout();
		layout.setHeight100();
		layout.setWidth100();
		
		layout.addMember(toolstrip);
		layout.addMember(section);
		
		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
			}
		});
		
		table.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				initSaveBtn();
			}
		});
		
		
		return layout;
	}
	
	private void createListFields(final SGTable table2){
		//final SGTable table2 = table;  --yuanlei 2011-2-15
		ListGridField VEHICLE_TYPE = new ListGridField("VEHICLE_TYPE",ColorUtil.getRedTitle(Util.TI18N.VEHICLE_TYPE()),66);
		ListGridField HINT_CODE = new ListGridField("HINT_CODE",Util.TI18N.HINT_CODE(),60);
		
		ListGridField LENGTH_UNIT = new ListGridField("LENGTH_UNIT",Util.TI18N.LENGTH_UNIT(),60);
        Util.initComboValue(LENGTH_UNIT, "V_BAS_MSRMNT", "UNIT", "UNIT_NAME", " WHERE MSRMNT_CODE='LENGTH'", "");
        

        
		ListGridField LENGTH = new ListGridField("LENGTH",Util.TI18N.LENGTH1(),90);
		ListGridField WIDTH = new ListGridField("WIDTH","宽(内径)",90);
	    ListGridField HEIGHT = new ListGridField("HEIGHT","高(内径)",90);
	    ListGridField LENGTH2 = new ListGridField("LENGTH2",Util.TI18N.LENGTH2(),90);
		ListGridField WIDTH2= new ListGridField("WIDTH2",Util.TI18N.WIDTH2(),90);
	    ListGridField HEIGHT2 = new ListGridField("HEIGHT2",Util.TI18N.HEIGHT2(),90);
		ListGridField WEIGHT_UNIT = new ListGridField("WEIGHT_UNIT",Util.TI18N.WEIGHT_UNIT(),60);
		Util.initComboValue(WEIGHT_UNIT, "V_BAS_MSRMNT", "UNIT", "UNIT_NAME"," WHERE MSRMNT_CODE='WEIGHT'","");
		
		ListGridField MAX_WEIGHT = new ListGridField("MAX_WEIGHT",Util.TI18N.MAX_WEIGHT(),70);
		ListGridField VOLUME_UNIT = new ListGridField("VOLUME_UNIT",Util.TI18N.VOLUME_UNIT(),70);
		Util.initComboValue(VOLUME_UNIT, "V_BAS_MSRMNT", "UNIT", "UNIT_NAME", " WHERE MSRMNT_CODE='VOLUME'","");
		
		ListGridField MAX_VOLUME = new ListGridField("MAX_VOLUME",Util.TI18N.MAX_VOLUME(),70);
		ListGridField RATIO = new ListGridField("RATIO",Util.TI18N.RATIO(),70);
		//ListGridField ENGINE_POWER = new ListGridField("ENGINE_POWER",Util.TI18N.ENGINE_POWER(),70);
		//ListGridField FUEL = new ListGridField("FUEL",Util.TI18N.FUEL(),70);
		//Util.initCodesComboValue(FUEL,"FUEL");
		
		ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ",Util.TI18N.SHOW_SEQ(),60);
		ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),70);
		ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),60);
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		Util.initFloatListField(LENGTH, StaticRef.VOL_FLOAT);
		Util.initFloatListField(WIDTH, StaticRef.VOL_FLOAT);
		Util.initFloatListField(HEIGHT, StaticRef.VOL_FLOAT);
		Util.initFloatListField(LENGTH2, StaticRef.VOL_FLOAT);
		Util.initFloatListField(WIDTH2, StaticRef.VOL_FLOAT);
		Util.initFloatListField(HEIGHT2, StaticRef.VOL_FLOAT);
		Util.initFloatListField(MAX_WEIGHT, StaticRef.GWT_FLOAT);
		Util.initFloatListField(MAX_VOLUME, StaticRef.VOL_FLOAT);
	
		VEHICLE_TYPE.addEditorExitHandler(new GetHintAction(table));
		table.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				final int col = event.getColNum();
				final int row = event.getRowNum();
				if(col == 6){
					double len = Double.valueOf(table2.getEditValue(row, "LENGTH").toString());
					double wid = Double.valueOf(table2.getEditValue(row, "WIDTH").toString());
					double hei = Double.valueOf(table2.getEditValue(row, "HEIGHT").toString());
					table2.setEditValue(row, "MAX_VOLUME", len*wid*hei);
				}
			}
		});
		table.setFields(VEHICLE_TYPE,HINT_CODE,LENGTH_UNIT,LENGTH,WIDTH,HEIGHT,LENGTH2,WIDTH2,HEIGHT2,WEIGHT_UNIT
				        ,MAX_WEIGHT,VOLUME_UNIT,MAX_VOLUME,RATIO,SHOW_SEQ,NOTES,ENABLE_FLAG);
		//table =table2;
	}
	
	public void initVerify() {
		check_map.put("TABLE", "BAS_VEHICLE_TYPE");
		check_map.put("VEHICLE_TYPE", StaticRef.CHK_NOTNULL+Util.TI18N.VEHICLE_TYPE());
//		check_map.put("HINT_CODE", StaticRef.CHK_NOTNULL+Util.TI18N.HINT_CODE());
//		check_map.put("HINT_CODE", StaticRef.CHK_UNIQUE+Util.TI18N.HINT_CODE());
		
		cache_map.put("ENABLE_FLAG", "true");
		cache_map.put("LENGTH_UNIT", "M");
//		cache_map.put("WEIGHT_UNIT", "T");
		cache_map.put("VOLUME_UNIT", "M3");
		
		
		
	}
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		    toolStrip.setWidth("100%");
	        toolStrip.setHeight("20");
	        toolStrip.setPadding(2);
	        toolStrip.setSeparatorSize(12);
	        toolStrip.addSeparator();
	         
	        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.VEHTYP);
	        searchButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(searchWin == null){
						searchForm = new SGPanel();
						searchWin = new SearchWin(CarDS, createSerchForm(searchForm),section.getSection(0)).getViewPanel();
					}else{
						searchWin.show();
					}
					
				}
			});
	        		
	        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.VEHTYP_P0_01);
	        newButton.addClickHandler(new NewAction(table,cache_map,this));
	        
	        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.VEHTYP_P0_02);
	        saveButton.addClickHandler(new SaveAction(table,check_map,this));
	        
	        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.VEHTYP_P0_03);
	        delButton.addClickHandler(new DeleteProAction(table));
	        
	        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.VEHTYP_P0_04);
	        canButton.addClickHandler(new CancelAction(table,this));
	        
	        IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.VEHTYP_P0_05);
	        expButton.addClickHandler(new ExportAction(table));
	    
			IButton inputButton = createBtn(StaticRef.IMPORT_BTN, BasPrivRef.VEHTYP_P0_06);
			inputButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(uploadWin == null){
						JavaScriptObject jsobject = section.getSection(0).getAttributeAsJavaScriptObject("controls");
						Canvas[] canvas = null;
						DynamicForm pageForm=null;
						if(jsobject != null) {
							canvas = Canvas.convertToCanvasArray(jsobject);
						}
						for(int i = 0; i < canvas.length; i++) {
							if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
								pageForm = (DynamicForm)canvas[i];
								break;
							}
						}
						uploadWin = new UploadFileWin().getViewPanel("vehicletype.xls","TMP_VEHTYPE_IMPORT","SP_IMPORT_VEHTYPE",table,pageForm);
					}else{
						uploadWin.show();
					}
				}
			});
	        
	        add_map.put(BasPrivRef.VEHTYP_P0_01, newButton);
	        del_map.put(BasPrivRef.VEHTYP_P0_03, delButton);
	        save_map.put(BasPrivRef.VEHTYP_P0_02, saveButton);
	        save_map.put(BasPrivRef.VEHTYP_P0_04, canButton);
	        this.enableOrDisables(add_map, true);
	        enableOrDisables(del_map, false);
	        this.enableOrDisables(save_map, false);
	        toolStrip.setMembersMargin(4);
	        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton, expButton,inputButton);

		
	}
	
	//查询二级视图
	public DynamicForm createSerchForm(SGPanel form) {
		
		//模糊查询
		TextItem FULL_INDEX = new TextItem("FULL_INDEX",Util.TI18N.FUZZYQRY()); 
		FULL_INDEX.setTitleOrientation(TitleOrientation.LEFT);
		FULL_INDEX.setWidth(352);
		FULL_INDEX.setColSpan(5);
		FULL_INDEX.setEndRow(true);
		FULL_INDEX.setTitleOrientation(TitleOrientation.TOP);
		
		//激活
		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
		ENABLE_FLAG.setValue(true);
		ENABLE_FLAG.setEndRow(true);
		

		
		
		form.setItems(FULL_INDEX,ENABLE_FLAG);
		return form;
	}

	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onDestroy() {
		if(searchWin!=null){
			searchWin.destroy();
			searchForm.destroy();
		}
		table.destroy();
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasVehTypeView view = new BasVehTypeView();
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