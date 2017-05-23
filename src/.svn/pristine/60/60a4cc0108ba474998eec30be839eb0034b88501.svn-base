package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.BasVehSuplierDS;
import com.rd.client.ds.report.R_KPI_LOADWH_RATE_DS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 准时到库率
 * @author fanglm
 *
 */
@ClassForNameAble
public class R_KPI_LOADWH_RATE extends SGForm implements PanelFactory {
	private DataSource DS;
	private SGTable table;
	private Window searchWin;
    private SGPanel searchForm;
	private SectionStack section;
	private SGDateTime ODR_TIME_FROM;
	private SGDateTime ODR_TIME_TO;
	private Object cur_value;
	private RadioGroupItem radioGroupItem;
	
	/*public R_KPI_LOADWH_RATE(String id) {
		super(id);
	}*/

	

	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub

	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolstrip = new ToolStrip();
		toolstrip.setAlign(Alignment.RIGHT);
		
		DS = R_KPI_LOADWH_RATE_DS.getInstance("R_KPI_LOADWH_RATE","R_KPI_LOADWH_RATE");
		table = new SGTable(DS,"100%","70%");
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		
		createListFields(table);
		createBtnWidget(toolstrip);
		section = createSection(table, null,true,true); 
		
		VLayout layout = new VLayout();
		layout.setHeight100();
		layout.setWidth100();
		
		layout.addMember(toolstrip);
		layout.addMember(section);
		
		return layout;
	}
	
	private void createListFields(SGTable table){
		ListGridField SUPLR_NAME = new ListGridField("suplr_name",Util.TI18N.SUPLR_NAME(),120);
//		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME(),120);
		ListGridField ODR_QNTY = new ListGridField("num","作业单票数",120);
		ListGridField R_ON_TIME = new ListGridField("per","准时到库率",120);
		ListGridField R_1DAY = new ListGridField("day1",Util.TI18N.R_1DAY(),70);
		ListGridField R_2DAY = new ListGridField("day2",Util.TI18N.R_2DAY(),70);
		ListGridField R_3DAY = new ListGridField("day3",Util.TI18N.R_3DAY(),70);
		ListGridField R_4DAY = new ListGridField("day4",Util.TI18N.R_4DAY(),70);
		ListGridField R_5DAY = new ListGridField("day5",Util.TI18N.R_5DAY(),70);
		ListGridField R_6DAY = new ListGridField("day6",Util.TI18N.R_6DAY(),70);
		ListGridField EXEC_ORG_ID = new ListGridField("EXEC_ORG_ID");
		EXEC_ORG_ID.setHidden(true);
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),100);
		table.setFields(EXEC_ORG_ID,EXEC_ORG_ID_NAME,SUPLR_NAME,ODR_QNTY,R_ON_TIME,R_1DAY,R_2DAY,R_3DAY,R_4DAY,R_5DAY,R_6DAY);
	}
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.addSeparator();
         
        IButton searchButton = createBtn(StaticRef.FETCH_BTN);
        searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchForm = new SGPanel();
					searchWin = new SearchWin(DS, createSerchForm(searchForm),section.getSection(0)).getViewPanel();
				}else{
					searchWin.show();
				}
				
			}
		});
        IButton expButton = createBtn(StaticRef.EXPORT_BTN);
        expButton.addClickHandler(new ExportAction(table));
        
//        String fieldname="SUPLR_NAME,sum(days0)as num,sum(days1) as day1 ,sum(days2)as day2,sum(days3)as day3,sum(days4)as day4,sum(days5)as day5,sum(days6)as day6,to_char(round(sum(days) * 100/ sum(days0),2)) || '%' as per";
//        expButton.addClickHandler(new KPIExportAction(table,fieldname,"addtime desc"));
   
        toolStrip.setMembersMargin(2);
        toolStrip.setMembers(searchButton,expButton);

	}
	
	public DynamicForm createSerchForm(SGPanel form) {
		ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM","下单时间从");
		ODR_TIME_FROM.setColSpan(2);
		ODR_TIME_FROM.setTitleOrientation(TitleOrientation.TOP);
		
		ODR_TIME_TO = new SGDateTime("ODR_TIME_TO","到");
		ODR_TIME_TO.setColSpan(2);
		ODR_TIME_TO.setTitleOrientation(TitleOrientation.TOP);
		
		String PreMonDate=Util.getMonthPreDay();
		ODR_TIME_FROM.setDefaultValue(PreMonDate);
		ODR_TIME_TO.setDefaultValue(Util.getCurTime());
		
		radioGroupItem = new RadioGroupItem();  
		radioGroupItem.setShowTitle(false); 
		radioGroupItem.setVertical(false);
		radioGroupItem.setValueMap("本月", "本季度","半年度", "全年");  
	    radioGroupItem.setDefaultValue("本月");
	    radioGroupItem.setColSpan(4);
	  
	    radioGroupItem.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				cur_value=event.getValue();
				
				String PreMonDate=Util.getMonthPreDay();
				
				String YearPreDay=Util.getAllYearPreDay();
				
				String BanYearPreDay=Util.getHalfYearPreDay();
				
				String jiduPreDay=Util.getQuarterPreDay();
				
				if(cur_value.equals("本月")){
					ODR_TIME_FROM.setValue(PreMonDate);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value.equals("本季度")){
					ODR_TIME_FROM.setValue(jiduPreDay);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    } else if(cur_value.equals("半年度")){
					ODR_TIME_FROM.setValue(BanYearPreDay);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value.equals("全年")){
					ODR_TIME_FROM.setValue(YearPreDay);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }
			}
		});
	    final SGText SUPLR_ID = new SGText("SUPLR_ID", Util.TI18N.SUPLR_ID());
		SUPLR_ID.setVisible(false);
		final ComboBoxItem SUPLR_NAME = new ComboBoxItem("SUPLR_NAME", "承运商");
		SUPLR_NAME.setTitleOrientation(TitleOrientation.TOP);
		SUPLR_NAME.setWidth(FormUtil.Width);
		SUPLR_NAME.setColSpan(2);
		initSuplr(SUPLR_NAME,SUPLR_ID);
		
	    TextItem EXEC_ORG_ID=new TextItem("EXEC_ORG_ID");
	    EXEC_ORG_ID.setVisible(false);
	    SGText EXEC_ORG_ID_NAME=new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
	    Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false,"50%","50%");
	    EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
	    EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
	    
	    SGCheck C_ORG_FLAG=new SGCheck("C_ORG_FLAG",Util.TI18N.C_ORG_FLAG());
	    C_ORG_FLAG.setValue(true);
		
		form.setItems(ODR_TIME_FROM,ODR_TIME_TO,radioGroupItem,SUPLR_ID,SUPLR_NAME,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG);
		return form;
	}

	@Override
	public void initVerify() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (searchWin != null ) {
			searchWin.destroy();
		}
	}



	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		R_KPI_LOADWH_RATE view = new R_KPI_LOADWH_RATE();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	private void initSuplr(final ComboBoxItem suplr_name,final TextItem suplr_id){
		DataSource supDS = BasVehSuplierDS.getInstall("BAS_VEH_SUPPLIER1", "BAS_SUPPLIER");
		ListGridField SUPLR_CODE = new ListGridField("SUPLR_CODE","承运商代码",80);
		ListGridField SUPLR_CNAME = new ListGridField("SUPLR_CNAME","承运商名称",80);
		suplr_name.setOptionDataSource(supDS);  
		suplr_name.setDisabled(false);
		suplr_name.setShowDisabled(false);
		suplr_name.setDisplayField("FULL_INDEX");  
		suplr_name.setPickListBaseStyle("myBoxedGridCell");
		suplr_name.setPickListWidth(180);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		//criteria.addCriteria("LOGIN_USER",LoginCache.getLoginUser().getUSER_ID());
		suplr_name.setPickListCriteria(criteria);
		
		suplr_name.setPickListFields(SUPLR_CODE, SUPLR_CNAME);
		suplr_name.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				final Record selectedRecord  = suplr_name.getSelectedRecord();
				if(selectedRecord != null){				
					suplr_name.setValue(selectedRecord.getAttribute("SUPLR_CNAME"));
					suplr_id.setValue(selectedRecord.getAttribute("ID"));				
				}
			}
		});
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}

}
