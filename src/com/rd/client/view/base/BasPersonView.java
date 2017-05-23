package com.rd.client.view.base;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.rd.client.PanelFactory;
import com.rd.client.action.base.person.PersonBlackAction;
import com.rd.client.action.base.person.PersonBlackzeAction;
import com.rd.client.action.base.person.SavePersonAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.DeleteMultiFormAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.GetHintAction;
import com.rd.client.common.action.ImageUploadAction;
import com.rd.client.common.action.ImageViewAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.BasStaffDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.UploadFileWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.grid.ListGridField;
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
 * 人员管理
 * @author lijun
 *
 */
@ClassForNameAble
public class BasPersonView extends SGForm implements PanelFactory {
    private ValuesManager vm;
    private SGTable table;
    private DataSource DS;
    private SectionStack section;
    private SGPanel mainPanel;
    private VLayout layout; 
    private Window searchWin = null;
    private SGPanel searchForm = new SGPanel();
    private SectionStack m_section;
    private Canvas canvas;
    private Canvas tileCanvas;
    public IButton blackzeButton;
    public IButton blackButton;
	private Window uploadWin;
	public Record rec;
	/*public BasPersonView(String id) {
		super(id);
	}*/
    
    @Override
    public Canvas getViewPanel() {
    	privObj = LoginCache.getUserPrivilege().get(getFUNCID());
    	layout = new VLayout();
    	vm  = new ValuesManager();
    	DS = BasStaffDS.getInstall("V_BAS_STAFF","BAS_STAFF");
    	table = new SGTable(DS,"100%","100%",true,true,false);
    	//按钮布局
    	ToolStrip toolStrip = new ToolStrip();
    	toolStrip.setAlign(Alignment.RIGHT);
    	
    	//主布局
    	HStack stack = new HStack();
    	stack.setWidth("99%");
    	stack.setHeight100();
    	
    	TabSet rightTab = new TabSet();
    	rightTab.setWidth("80%");
    	rightTab.setHeight("100%");
    	rightTab.setMargin(0);
    	rightTab.setVisible(false);
    	
    	if(isPrivilege(BasPrivRef.STAFF_P1)) {
    		
	    	Tab mainInfo = new Tab(Util.TI18N.MAININFO());
	    	mainInfo.setPane(createMainInfo());
	    	rightTab.addTab(mainInfo);
    	}
    	
    	if(isPrivilege(BasPrivRef.STAFF_P2)) {
    		
	    	Tab pictureInfo = new Tab(Util.TI18N.IMG_INFO());
	    	pictureInfo.setPane(createImgInfo());
	    	rightTab.addTab(pictureInfo);
    	}
    	
    	
    	createListGrid(table);
    	table.setCanEdit(false);
    	table.setShowFilterEditor(false);
    	
    	m_section = new SectionStack();
    	final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
    	listItem.setItems(table);
    	listItem.setExpanded(true);
//    	listItem.setControls(addMaxBtn(m_section, stack, "200",true), new SGPage(table, true).initPageBtn());
    	listItem.setControls(new SGPage(table, true).initPageBtn());
    	m_section.addSection(listItem);
    	m_section.setWidth("100%");
    	table.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(table.getSelectedRecord()==null){
					return;
				}
				if(isMax) {
					expend();
				}
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
			}
			
		});
		
    	initVerify();
//    	addDoubeclick(table, listItem, rightTab, m_section);
    	vm.addMember(mainPanel);
    	
    	stack.addMember(m_section);
    	addSplitBar(stack);
    	stack.addMember(rightTab);
    	layout.setWidth100();
    	layout.setHeight100();
    	
    	createBtnWidget(toolStrip);
    	layout.addMember(toolStrip);
    	layout.addMember(stack);
    	return layout;
    }
    private void createListGrid(final SGTable table){
    	table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record record = event.getRecord();
				vm.editRecord(record);
                vm.setValue("OP_FLAG", StaticRef.MOD_FLAG);
				OP_FLAG ="M";
				
				initSaveBtn();
				if(record.getAttribute("BLACKLIST_FLAG")!=null){
					if(record.getAttribute("BLACKLIST_FLAG").equals("true")){
						blackButton.enable();
						blackzeButton.disable();
					}else{
						blackzeButton.enable();
						blackButton.disable();
					}
				}else{
					blackzeButton.enable();
					blackButton.disable();
				}
				
			}
		});
    	table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record record = event.getRecord();
				if(ObjUtil.isNotNull(record)){
					vm.editRecord(record);
				}
				
				initSaveBtn();
				if(record != null){
					if(record.getAttribute("BLACKLIST_FLAG")!=null){
						if(record.getAttribute("BLACKLIST_FLAG").equals("true")){
							blackButton.enable();
							blackzeButton.disable();
						}else{
							blackButton.enable();
							blackzeButton.disable();
						}
					}else{
						blackButton.enable();
						blackzeButton.disable();
					}
				}
				if(event.getRecord() != null){
					rec = event.getRecord();
					String VEHICLE_STAT = rec.getAttribute("VEHICLE_STAT");
					if("D5595E8BF25A4E0D8C46796B772FB1BA".equals(VEHICLE_STAT)){
						blackButton.enable();
						blackzeButton.disable();
					} else {
						blackzeButton.disable();
						blackButton.enable();
					}
				} else {
					blackzeButton.disable();
					blackButton.disable();
				}

			}
		});
	    
    	
    	table.setShowRowNumbers(true);
    	
//    	ListGridField ID = new ListGridField("ID",Util.TI18N.ID(),50);
    	ListGridField STAFF_CODE = new ListGridField("STAFF_CODE",Util.TI18N.STAFF_CODE(),80);
    	STAFF_CODE.setCanEdit(true);
//    	STAFF_CODE.setTitle(ColorUtil.getRedTitle(Util.TI18N.STAFF_CODE()));
    	
    	ListGridField STAFF_NAME = new ListGridField("STAFF_NAME",Util.TI18N.STAFF_NAME(),80);
//    	STAFF_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.STAFF_NAME()));
    	
    	ListGridField STAFF_ENAME = new ListGridField("STAFF_ENAME",Util.TI18N.STAFF_ENAME(),80);
    	ListGridField HINT_CODE = new ListGridField("HINT_CODE",Util.TI18N.HINT_CODE(),80);
//    	HINT_CODE.setTitle(ColorUtil.getBlueTitle(Util.TI18N.HINT_CODE()));
    	ListGridField SEX = new ListGridField("SEX",Util.TI18N.SEX(),50);
    	ListGridField BIRTHDAY = new ListGridField("BIRTHDAY",Util.TI18N.BIRTHDAY(),120);
    	ListGridField STAFF_TYP = new ListGridField("BAS_STAFF_NAME",Util.TI18N.STAFF_TYP(),80);
    	ListGridField ORG_ID = new ListGridField("BAS_STAFF_ORG",Util.TI18N.ORG_ID(),120);
    	ListGridField DEP_ID = new ListGridField("DEP_ID_NAME",Util.TI18N.DEP_ID(),120);
    	ListGridField POSITION = new ListGridField("POSITION_NAME",Util.TI18N.POSITION(),80);
    	ListGridField EMPLOY_TIME = new ListGridField("EMPLOY_TIME",Util.TI18N.EMPLOY_TIME(),120);
    	ListGridField WORK_LIFE = new ListGridField("WORK_LIFE",Util.TI18N.WORK_LIFE(),80);
    	ListGridField MOBILE = new ListGridField("MOBILE",Util.TI18N.MOBILE(),120);
    	ListGridField EMAIL = new ListGridField("EMAIL",Util.TI18N.EMAIL(),120);
    	ListGridField ADDRESS = new ListGridField("ADDRESS",Util.TI18N.ADDRESS(),120);
    	ListGridField PHOTO_DIR = new ListGridField("PHOTO_DIR",Util.TI18N.PHOTO_DIR(),120);
    	ListGridField ID_NO = new ListGridField("ID_NO",Util.TI18N.ID_NO(),120);
    	ListGridField DRVR_LIC_NUM = new ListGridField("DRVR_LIC_NUM",Util.TI18N.DRVR_LIC_NUM(),100);
    	ListGridField LIC_CLS = new ListGridField("LIC_CLS_NAME",Util.TI18N.LIC_CLS(),80);
    	ListGridField LIC_EFCT_DT = new ListGridField("LIC_EFCT_DT",Util.TI18N.LIC_EFCT_DT(),100);
    	ListGridField LIC_EXPD_DT = new ListGridField("LIC_EXPD_DT",Util.TI18N.LIC_EXPD_DT(),100);
    	ListGridField OPER_LICENSE = new ListGridField("OPER_LICENSE",Util.TI18N.OPER_LICENSE(),100);
    	ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ",Util.TI18N.SHOW_SEQ(),80);
    	ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),50);
    	ListGridField MODIFY_FLAG = new ListGridField("MODIFY_FLAG",Util.TI18N.MODIFY_FLAG(),50);
    	
    	table.setFields(STAFF_CODE,STAFF_NAME,STAFF_ENAME,HINT_CODE,SEX,BIRTHDAY,STAFF_TYP
    			        ,ORG_ID,DEP_ID,POSITION,EMPLOY_TIME,WORK_LIFE,MOBILE,EMAIL
    			        ,ADDRESS,PHOTO_DIR,ID_NO,DRVR_LIC_NUM,LIC_CLS,LIC_EFCT_DT,LIC_EXPD_DT
    			        ,OPER_LICENSE,SHOW_SEQ,ENABLE_FLAG,MODIFY_FLAG);
    	
    }
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(3);
        toolStrip.setSeparatorSize(5);
		
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.STAFF);
		toolStrip.addMember(searchButton);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchWin = new SearchWin(DS,createSearchForm(searchForm),m_section.getSection(0),vm).getViewPanel();
				}else{
					searchWin.show();
				}
				
			}
		});
		
		IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.STAFF_P0_01);
		toolStrip.addMember(newButton);
		newButton.addClickHandler(new NewMultiFormAction(vm, cache_map,this));
		
		IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.STAFF_P0_02);
		toolStrip.addMember(saveButton);
		saveButton.addClickHandler(new SavePersonAction(table, vm, check_map, this));
		
		IButton deleteButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.STAFF_P0_03);
		toolStrip.addMember(deleteButton);
		deleteButton.addClickHandler(new DeleteMultiFormAction(table, vm));
		
		IButton canlButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.STAFF_P0_04);
		toolStrip.addMember(canlButton);
		canlButton.addClickHandler(new CancelMultiFormAction(table, vm,this));
		
		IButton inputButton = createBtn(StaticRef.IMPORT_BTN, BasPrivRef.STAFF_P0_08);
		inputButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(uploadWin == null){
					JavaScriptObject jsobject = m_section.getSection(0).getAttributeAsJavaScriptObject("controls");
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
					uploadWin = new UploadFileWin().getViewPanel("staff.xls","TMP_STAFF_IMPORT","SP_IMPORT_STAFF",table,pageForm);
				}else{
					uploadWin.show();
				}
			}
		});
		toolStrip.addMember(inputButton);
		
		
		IButton exportButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.STAFF_P0_05);
		toolStrip.addMember(exportButton);
		exportButton.addClickHandler(new ExportAction(table, "addtime desc"));
		
		//加入黑名单
        blackzeButton = createBtn("黑名单",BasPrivRef.STAFF_P0_06);
        blackzeButton.addClickHandler(new PersonBlackzeAction(table, vm,this));
		toolStrip.addMember(blackzeButton);

        //取消黑名单
		blackButton = createBtn("取消黑名单",BasPrivRef.STAFF_P0_07);
		blackButton.addClickHandler(new PersonBlackAction(table, vm,this));
		toolStrip.addMember(blackButton);

		toolStrip.setMembersMargin(4);
		
        add_map.put(BasPrivRef.STAFF_P0_01, newButton);
        del_map.put(BasPrivRef.STAFF_P0_03, deleteButton);
        save_map.put(BasPrivRef.STAFF_P0_02, saveButton);
        save_map.put(BasPrivRef.STAFF_P0_04, canlButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        blackzeButton.disable();
        blackButton.disable();

	}

	private SectionStack createMainInfo(){
		LinkedHashMap<String,String> mapvalue = new LinkedHashMap<String,String>();
    	//mapvalue.put("mail", "男");
    	//mapvalue.put("femail", "女");
		mapvalue.put("男", "男");//wangjun
		mapvalue.put("女", "女");//wangjun
		
		mainPanel = new SGPanel();
		mainPanel.setWidth(20);
    	//1
    	SGText STAFF_NAME = new SGText("STAFF_NAME",ColorUtil.getRedTitle(Util.TI18N.STAFF_NAME()));
		SGText STAFF_CODE = new SGText("STAFF_CODE",ColorUtil.getRedTitle(Util.TI18N.STAFF_CODE()),true);
		SGText STAFF_ENAME = new SGText("STAFF_ENAME",Util.TI18N.STAFF_ENAME());
		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
		
		//2
		SGText HINT_CODE = new SGText("HINT_CODE",Util.TI18N.HINT_CODE(),true);
		SGCombo SEX = new SGCombo("SEX",Util.TI18N.SEX());
		SEX.setValueMap(mapvalue);
		STAFF_NAME.addBlurHandler(new GetHintAction(STAFF_NAME,HINT_CODE));
//		SGDate BIRTHDAY = new SGDate("BIRTHDAY",Util.TI18N.BIRTHDAY());
		SGText BIRTHDAY=new SGText("BIRTHDAY",Util.TI18N.BIRTHDAY());
		Util.initDateTime(mainPanel, BIRTHDAY);
		SGText MOBILE = new SGText("MOBILE","联系电话");
		
		//3
		SGCombo STAFF_TYP = new SGCombo("STAFF_TYP",ColorUtil.getRedTitle(Util.TI18N.STAFF_TYP()),true);
		Util.initCodesComboValue(STAFF_TYP,"STAFF_TYP");
		SGText ID_NO = new SGText("ID_NO",Util.TI18N.ID_NO());
		SGText EMAIL = new SGText("EMAIL",Util.TI18N.EMAIL());
		SGText DRVR_LIC_NUM = new SGText("DRVR_LIC_NUM",Util.TI18N.DRVR_LIC_NUM());
		
		//4
		SGLText ADDRESS = new SGLText("ADDRESS",Util.TI18N.ADDRESS(),true);
		SGText WORK_LIFE  = new SGText("WORK_LIFE",Util.TI18N.WORK_LIFE());
		SGText OPER_LICENSE = new SGText("OPER_LICENSE",Util.TI18N.OPER_LICENSE());
		
		//5
		SGCombo C_ORG_ID = new SGCombo("ORG_ID",Util.TI18N.C_ORG_ID(),true);
		Util.initOrg(C_ORG_ID, "", "");
		C_ORG_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.C_ORG_ID()));
		SGCombo LIC_CLS  = new SGCombo("LIC_CLS",Util.TI18N.LIC_CLS());
        Util.initCodesComboValue(LIC_CLS,"LIC_CLS");
        SGText LIC_EFCT_DT=new SGText("LIC_EFCT_DT",Util.TI18N.LIC_EFCT_DT());
        Util.initDateTime(mainPanel, LIC_EFCT_DT);
        SGText LIC_EXPD_DT=new SGText("LIC_EXPD_DT","到");
        Util.initDateTime(mainPanel, LIC_EXPD_DT);
		
        //6
        SGCombo DEP_ID = new SGCombo("DEP_ID",Util.TI18N.DEP_ID(),true);
        Util.initCodesComboValue(DEP_ID,"DEPARTMENT");
        SGText POSITION = new SGText("POSITION",Util.TI18N.POSITION());
        SGText EMPLOY_TIME=new SGText("EMPLOY_TIME",Util.TI18N.EMPLOY_TIME());
		Util.initDateTime(mainPanel, EMPLOY_TIME);
		SGCheck BLACKLIST_FLAG = new SGCheck("BLACKLIST_FLAG",Util.TI18N.BLACKLIST_FLAG());
        
		
		mainPanel.setWidth("40%");
		mainPanel.setItems(STAFF_CODE,STAFF_NAME,STAFF_ENAME,ENABLE_FLAG,HINT_CODE,SEX,BIRTHDAY,
				MOBILE,STAFF_TYP,ID_NO,EMAIL,DRVR_LIC_NUM,ADDRESS,WORK_LIFE,OPER_LICENSE,C_ORG_ID,
				LIC_CLS,LIC_EFCT_DT,LIC_EXPD_DT,DEP_ID,POSITION,EMPLOY_TIME,BLACKLIST_FLAG);
		
		
		section = new SectionStack();
		section.setBackgroundColor(ColorUtil.BG_COLOR);
		
		SectionStackSection basInfo = new SectionStackSection(Util.TI18N.BAS_INFO());
		basInfo.addItem(mainPanel);
		basInfo.setExpanded(true);
		
		section.addSection(basInfo);
		
		section.setVisibilityMode(VisibilityMode.MULTIPLE);
		section.setAnimateSections(true);
		
		
	  return section;	
	}
	
	private VLayout createImgInfo(){
		VLayout vLay = new VLayout();
        vLay.setWidth100();
        vLay.setBackgroundColor(ColorUtil.BG_COLOR);
        
        /**
        final FormPanel uploadForm = new FormPanel();  
        uploadForm.setAction("uploadServlet");
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadForm.setMethod(FormPanel.METHOD_POST);
        uploadForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if(StaticRef.SUCCESS_CODE.equals(event.getResults())){
					SC.say("上传成功。");
				}
			}
		});
        
        VerticalPanel verticalPanel = new VerticalPanel();
        final FileUpload imageItem = new FileUpload();
        imageItem.setName("图片");
        imageItem.setWidth("140px");
        verticalPanel.add(imageItem);
        
        Button saveItem = new Button("上传");  
        saveItem.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
			
			@Override
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				uploadForm.submit();
			}
		});
        verticalPanel.add(saveItem);
        uploadForm.setWidget(verticalPanel);
        RootPanel.get().add(uploadForm);
        **/
        
        HTMLPane htmlPane = new HTMLPane();
		htmlPane.setContentsType(ContentsType.PAGE);
		htmlPane
				.setContents("<iframe name='foo' style='position:absolute;width:0;height:0;border:0'></iframe>");
		htmlPane.setWidth("1");
		htmlPane.setHeight("1");
		
        final DynamicForm uploadForm = new DynamicForm();
        uploadForm.setAction("uploadServlet");
        uploadForm.setEncoding(Encoding.MULTIPART);
        uploadForm.setMethod(FormMethod.POST);
        uploadForm.setTarget("foo");
        
        
        
        final UploadItem imageItem = new UploadItem("image","图片");
        
        TextItem notes = new TextItem("NOTES","描述");
        SGButtonItem saveItem = new SGButtonItem("上传","上传");
        setButtonItemEnabled(BasPrivRef.STAFF_P2_01,saveItem,true);
//        saveItem.setAutoFit(true);
        saveItem.setIcon(StaticRef.ICON_NEW);
        saveItem.setWidth(60);
        saveItem.setAutoFit(true);
        saveItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(table.getSelectedRecord() !=  null){
					if(table.getSelectedRecord().getAttribute("STAFF_CODE") != null){
						Map<String, String> map = uploadForm.getValues();
						if(map.get("image") != null){
							String image = map.get("image").toString();
							new ImageUploadAction(table.getSelectedRecord().getAttribute("STAFF_CODE").toString(),StaticRef.BAS_STAFF_URL,image,uploadForm).onClick(event);
							if(table.getSelectedRecord() != null){
								if(canvas != null){
									if(canvas.isCreated()){
										canvas.destroy();
									}
								}
								canvas = new Canvas();
								tileCanvas.addChild(canvas);
								canvas.setHeight(163);
								canvas.setWidth(780);
								if(table.getSelectedRecord().getAttribute("STAFF_CODE") != null){
						        	ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.BAS_STAFF_PREVIEW_URL,table.getSelectedRecord().getAttribute("STAFF_CODE"));
						        	action.getName();
						        }
							}
						}else{
							MSGUtil.sayWarning("请选择所要上传的图片");
						}
					}
				} else {
					MSGUtil.sayWarning("请选择上传图片对应的人员编号.");
				}
				
			}
		});
//        saveItem.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				if(table.getSelectedRecord() !=  null){
//						if(table.getSelectedRecord().getAttribute("STAFF_CODE") != null){
//							if(!"{}".equals(uploadForm.getValues().toString())){
//								new ImageUploadAction(table.getSelectedRecord().getAttribute("STAFF_CODE").toString(),StaticRef.BAS_STAFF_URL,uploadForm.getValues().toString(),uploadForm).onClick(event);
////								new ImageUploadAction(table.getSelectedRecord().getAttribute("STAFF_CODE").toString(),StaticRef.BAS_STAFF_URL,uploadForm.getValues().toString(),uploadForm);
//							}else{
//								MSGUtil.sayWarning("请选择所要上传的图片");
//							}
//						}
//					} else {
//						MSGUtil.sayWarning("请选择上传图片对应的人员编号.");
//					}
//			}
//		});
        
        
//        uploadForm.setItems(imageItem,notes,saveItem);
        uploadForm.setItems(imageItem,notes,saveItem);
        
        tileCanvas = new Canvas();
        tileCanvas.setBorder("1px solid black");
        tileCanvas.setHeight(200);  
        tileCanvas.setWidth100(); 
        table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(table.getSelectedRecord() != null){
					
					if(canvas != null){
						if(canvas.isCreated()){
							canvas.destroy();
						}
					}
					canvas = new Canvas();
					tileCanvas.addChild(canvas);
					canvas.setHeight(163);
					canvas.setWidth(1000);
					if(table.getSelectedRecord().getAttribute("STAFF_CODE") != null){
			        	ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.BAS_STAFF_PREVIEW_URL,table.getSelectedRecord().getAttribute("STAFF_CODE"));
			        	action.getName();
			        }
				}
			}
		});
		
//        table.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				if(table.getSelectedRecord() != null){
//				
//					if(canvas != null){
//						if(canvas.isCreated()){
//							canvas.destroy();
//						}
//					}
//					canvas = new Canvas();
//					tileCanvas.addChild(canvas);
//					canvas.setHeight(163);
//					canvas.setWidth(1000);
//					if(table.getSelectedRecord().getAttribute("STAFF_CODE") != null){
//			        	ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.BAS_STAFF_PREVIEW_URL,table.getSelectedRecord().getAttribute("STAFF_CODE"));
//			        	action.getName();
//			        }
//				}
//			}
//		});
        vLay.setMembers(uploadForm,tileCanvas,htmlPane);
        
       
        
        return vLay;
	}
	
	public DynamicForm createSearchForm(DynamicForm form){
		
		
		//1
		TextItem FULL_INDEX = new TextItem("FULL_INDEX",Util.TI18N.FUZZYQRY());
		FULL_INDEX.setTitleOrientation(TitleOrientation.LEFT);
		FULL_INDEX.setWidth(352);
		FULL_INDEX.setColSpan(5);
		FULL_INDEX.setEndRow(true);
		FULL_INDEX.setTitleOrientation(TitleOrientation.TOP);
		
		//2
		SGCombo STAFF_TYP = new SGCombo("STAFF_TYP",Util.TI18N.STAFF_TYP(),true);
		Util.initCodesComboValue(STAFF_TYP,"STAFF_TYP");
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID", "");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem EXEC_ORG_ID_NAME = new TextItem("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setColSpan(2);
		EXEC_ORG_ID_NAME.setWidth(120);
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, true, "50%", "40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		//3
		SGCombo DEP_ID = new SGCombo("DEP_ID",Util.TI18N.DEP_ID(),true);
		Util.initCodesComboValue(DEP_ID,"DEPARTMENT");
		
		//4
		SGCheck INCLUDE = new SGCheck("C_ORG_FLAG","包含下级机构",true);
		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
		INCLUDE.setValue(true);
		ENABLE_FLAG.setValue(true);
		
		//
		SGCheck BLACKLIST_FLAG = new SGCheck("BLACKLIST_FLAG", "黑名单");
		BLACKLIST_FLAG.setValue(false);
		
		form.setItems(FULL_INDEX,STAFF_TYP,EXEC_ORG_ID,EXEC_ORG_ID_NAME,DEP_ID,INCLUDE,ENABLE_FLAG,BLACKLIST_FLAG);
		
		
		return form;
	}
	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub

	}


	@Override
	public void initVerify() {
		check_map.put("TABLE", "BAS_STAFF");
		check_map.put("STAFF_CODE", StaticRef.CHK_NOTNULL+Util.TI18N.STAFF_CODE());
		check_map.put("STAFF_CODE", StaticRef.CHK_UNIQUE+Util.TI18N.STAFF_CODE());
		check_map.put("STAFF_NAME", StaticRef.CHK_NOTNULL+Util.TI18N.STAFF_NAME());
		check_map.put("BIRTHDAY", StaticRef.CHK_DATE+Util.TI18N.BIRTHDAY());
		check_map.put("LIC_EFCT_DT", StaticRef.CHK_DATE+Util.TI18N.LIC_EFCT_DT());
		check_map.put("LIC_EXPD_DT", StaticRef.CHK_DATE+Util.TI18N.LIC_EXPD_DT());
		check_map.put("EMPLOY_TIME", StaticRef.CHK_DATE+Util.TI18N.EMPLOY_TIME());
		check_map.put("ORG_ID",StaticRef.CHK_NOTNULL+Util.TI18N.C_ORG_ID());
		check_map.put("STAFF_TYP",StaticRef.CHK_NOTNULL+Util.TI18N.STAFF_TYP());
		
		cache_map.put("ENABLE_FLAG", "true");
//		cache_map.put("OPEN_POSITIONING", "true");
//		cache_map.put("WHETHER_GEGIST", "true");
		
		cache_map.put("ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
//		cache_map.put("SEX", "男");
		
		
		

	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasPersonView view = new BasPersonView();
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