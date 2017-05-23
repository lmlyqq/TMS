package com.rd.client.view.system;

import java.util.LinkedHashMap;

import com.rd.client.PanelFactory;
import com.rd.client.action.system.DeleteU8ordAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.U8InterLogDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * U8接口数据异常查询界面
 * @author fanglm
 *
 */
@ClassForNameAble
public class U8InterLogView extends SGForm implements PanelFactory {
	
	private DataSource DS;
	private SGTable Table;
	private SectionStack section;
	private Window searchWin;
	private SGPanel searchForm;
	
	/*public U8InterLogView(String id) {
		super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(fid);
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		
		DS = U8InterLogDS.getInstance("U8_INTER_LOG", "U8_ALTER_LOG");
		Table = new SGTable(DS,"100%","70%");
		
		listInfo(Table);
		createBtnWidget(toolStrip);
		section = createSection(Table,null,true,true);
		section.setHeight("92%");
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.addMember(toolStrip);
		layout.addMember(section);
		return layout;
	}
	private void listInfo(SGTable table){
		ListGridField DOC_NO = new ListGridField("DOC_NO","编号",110);
		ListGridField QNTY = new ListGridField("QNTY","数量",70);
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME","客户",90);
		ListGridField DTYPE = new ListGridField("DTYPE","日志类型",80);
		ListGridField LOGS = new ListGridField("LOGS","日志信息",460);
		ListGridField ADDTIME = new ListGridField("ADDTIME",Util.TI18N.ADDTIME(),120);
		DTYPE.setCanEdit(false);
		ADDTIME.setCanEdit(false);
		table.setFields(DTYPE,CUSTOMER_NAME,DOC_NO,QNTY,LOGS,ADDTIME);
		
	}
	
	public DynamicForm createSerchForm(SGPanel form){
		form.setDataSource(DS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		form.setNumCols(8);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		
		final SGCombo customer_id = new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER());
		Util.initCustComboValue(customer_id, "", "",LoginCache.getDefCustomer().get("CUSTOMER_ID"));
		SGText CUSTOM_ODR_NO = new SGText("DOC_NO","单号");
		SGCombo LOAD_NO = new SGCombo("DTYPE","日志类型");
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("托运单", "托运单");
		map.put("作业单", "作业单");
		map.put("货品", "货品");
		map.put("地址点", "地址点");
		map.put("货品分类", "货品分类");
		LOAD_NO.setValueMap(map);
		
		
//		SGText SKU = new SGText("ADDTIME_FROM","添加时间从",true);
//		SGText SKU_NAME = new SGText("ADDTIME_TO","到");
		
		SGDateTime FROM  = new SGDateTime("ADDTIME_FROM", "添加时间从",true);
		SGDateTime TO  = new SGDateTime("ADDTIME_TO", "到");
//		Util.initDateTime(form, SKU);
//		Util.initDateTime(form, SKU_NAME);
		
		form.setItems(customer_id,LOAD_NO,CUSTOM_ODR_NO,FROM,TO);
		return form;
		
	}
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(16);
		toolStrip.addSeparator();
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchForm = new SGPanel();
					searchWin =  new SearchWin(DS, createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				} else {
					searchWin.show();
				}
				
			}
		});
		
		 //删除按钮
		IButton delButton = createBtn(StaticRef.DELETE_BTN);
        delButton.addClickHandler(new DeleteU8ordAction(Table));
        
		IButton expButton = createBtn(StaticRef.EXPORT_BTN);
	    expButton.addClickHandler(new ExportAction(Table, "addtime desc"));
	    
	    toolStrip.setMembersMargin(4);
	    toolStrip.setMembers(searchButton,delButton,expButton);
	    
	}

	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void initVerify() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		if(searchWin != null){
			searchWin.destroy();
			searchForm.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		U8InterLogView view = new U8InterLogView();
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
