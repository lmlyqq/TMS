package com.rd.client.view.system;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.reflection.ClassForNameAble;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 系统管理-业务日志配置
 * @author fanglm
 *
 */
@ClassForNameAble
public class SFBizLogView extends SGForm implements PanelFactory{
	private SGTable B2BGrid;
	private SGTable B2CGrid;
	private SGTable LDGrid;
	private SGTable DPGrid;
	private DataSource b2bds;
	private DataSource b2cds;
	private DataSource ldds;
	private DataSource dpds;
	private TabSet leftTabSet;
	
	/*public SFBizLogView(String id) {
	    super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		b2bds = getDataSource(StaticRef.B2B);
		b2cds = getDataSource(StaticRef.B2C);
		ldds = getDataSource(StaticRef.LD);
		dpds = getDataSource(StaticRef.SHOP);
	    
		B2BGrid =  createTable(b2bds);
		B2CGrid = createTable(b2cds);
		LDGrid = createTable(ldds);
		DPGrid = createTable(dpds);
        
		SGPanel detailForm = new SGPanel();
	    detailForm.setHeight("5%");
	    createForm(detailForm);
        

        leftTabSet = new TabSet();  
        leftTabSet.setWidth("100%");   
        leftTabSet.setHeight("100%"); 
	        
	    Tab tab = new Tab("冷运专运");
	    tab.setPane(B2BGrid);
	    leftTabSet.addTab(tab);

	    Tab tab1 = new Tab("冷运速配");
	    tab1.setPane(B2CGrid);
	    leftTabSet.addTab(tab1);
  
	    Tab tab2 = new Tab("零担");
	    tab2.setPane(LDGrid);
	    leftTabSet.addTab(tab2);
	    
	    Tab tab3 = new Tab("冷运城配");
	    tab3.setPane(DPGrid);
	    leftTabSet.addTab(tab3);
	    
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.addMember(leftTabSet);
		main.addMember(detailForm);
		
		initTableData(B2BGrid, StaticRef.B2B); //初始化表格数据
		initTableData(B2CGrid, StaticRef.B2C); //初始化表格数据
		initTableData(LDGrid, StaticRef.LD); //初始化表格数据
		initTableData(DPGrid, StaticRef.SHOP);//初始化表格数据
		
		return main;
	}
	
	private SGTable createTable(DataSource ds) {
		
		SGTable headTable = new SGTable(ds, "100%", "98%", false, true, false);
		headTable.setCanExpandRecords(false);
		headTable.setCanEdit(true);
		//headTable.setShowRowNumbers(true);
		//headTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		headTable.setEditEvent(ListGridEditEvent.CLICK);	
		
		ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
		ENABLE_FLAG.setWidth(50);
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		ENABLE_FLAG.setCanEdit(true);
	    
	    ListGridField STAT_CODE_NAME = new ListGridField("STAT_CODE_NAME",Util.TI18N.STAT_CODE_NAME());
	    STAT_CODE_NAME.setWidth(120);
	    STAT_CODE_NAME.setCanEdit(false);
	    
	    ListGridField NOTES_CODE_NAME = new ListGridField("NOTES_CODE",Util.TI18N.NOTES_CODE_NAME());
	    NOTES_CODE_NAME.setCanEdit(true);
	    NOTES_CODE_NAME.setWidth(400);
	    Util.initComboValue(NOTES_CODE_NAME, "BAS_CODES", "CODE", "NAME_C", " where prop_code = 'SF_NOTES'", " order by SHOW_SEQ ASC");
	    
	    headTable.setFields(ENABLE_FLAG,STAT_CODE_NAME,NOTES_CODE_NAME);
	    
	    return headTable;
	}


	/**
	 * 创建主信息布局
	 * @author fanglm
	 */
	@Override
	public void createForm(DynamicForm form) {		
		ButtonItem saveButton = new ButtonItem(Util.BI18N.SAVE());
		saveButton.setIcon(StaticRef.ICON_SAVE);
		saveButton.setAutoFit(true);
		saveButton.setStartRow(true);
		saveButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				int tabNum  = leftTabSet.getSelectedTabNumber();
				ListGridRecord[] records = null;
				if(tabNum == 0) {
					records = B2BGrid.getRecords();
				}
				else if(tabNum == 1) {
					records = B2CGrid.getRecords();
				}
				else if(tabNum == 2) {
					records = LDGrid.getRecords();
				}
				else if (tabNum ==3) {
					records = DPGrid.getRecords();
				}
				if(records.length > 0) {
					ArrayList<String> sqlList = new ArrayList<String>();
					String id = "",notes_code = "";
					Boolean enable_flag = null;
					StringBuffer sb = null;
					for (int i = 0; i < records.length; i++) {
						sb = new StringBuffer();
						ListGridRecord record = records[i];
						id = record.getAttribute("ID");
						enable_flag = record.getAttributeAsBoolean("ENABLE_FLAG");
						notes_code = record.getAttribute("NOTES_CODE");
						sb.append("update sf_status_config set enable_flag = '");
						sb.append(enable_flag?"Y":"N");
						sb.append("'");
						if(ObjUtil.isNotNull(notes_code)) {
							sb.append(",notes_code = '");
							sb.append(notes_code);
							sb.append("'");
						}
						sb.append(" where id = '");
						sb.append(id);
						sb.append("'");
						sqlList.add(sb.toString());
					}
					Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
						@Override
						public void onSuccess(String result) {
							MSGUtil.showOperSuccess();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.showOperError();
						}
						
					});
				}
			}
			
		});
		form.setItems(saveButton);
		
	}

	/**
	 * 获取数据
	 * @author fanglm
	 * @return
	 */
	static DataSource getDataSource(String biz_typ)
	{
		String url = "sysQueryServlet?ds_id=SF_STATUS_CONFIG&OP_FLAG=M&BIZ_TYP=" + biz_typ;
		DataSource dataSource = new DataSource();
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(url);
		dataSource.setClientOnly(true);

		// id
		DataSourceTextField idField = new DataSourceTextField("ID");
		idField.setPrimaryKey(true);
		idField.setRequired(true);
		idField.setHidden(true);

		// name
		DataSourceTextField statField = new DataSourceTextField("STAT_CODE");
		statField.setRequired(true);

		dataSource.setFields(idField, statField);

		return dataSource;
	}
	
	private void initTableData(final SGTable table,String biz_typ) {
		Criteria crit = new Criteria();
		crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
		crit.addCriteria("BIZ_TYP", biz_typ);
		table.fetchData(crit);
	}
	@Override
	public void createBtnWidget(ToolStrip strip) {
		
	}

	public void createListField(SGTable table, List<String> fldList, List<String> titList, List<String> widList, List<ListGridFieldType> typList) {
		
	}

	@Override
	public void onDestroy() {
	}
	@Override
	public void initVerify() {
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		SFBizLogView view = new SFBizLogView();
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
