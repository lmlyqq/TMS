package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.report.PoOdrKPIDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;


//ＴＭＳ运输管理系统——ＫＰＩ报表——异常回单统计分析汇总表


@ClassForNameAble
public class PodOverdueReportView extends SGForm implements PanelFactory {
	
	private DataSource ds ;
	private SGTable table;
	private SectionStack stack;
	private SGPanel form;
	private Window searchWin;
	private SGDateTime ODR_TIME_FROM;
	private SGDateTime ODR_TIME_TO;
	private RadioGroupItem radioGroupItem;
	private Object cur_value;
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchWin = new SearchWin(ds, createSearchForm(form), stack.getSection(0)).getViewPanel();
				}else {
					searchWin.show();
				}
			}
		});
		
//		IButton expButton = createBtn(StaticRef.EXPORT_BTN);
//		expButton.addClickHandler(new ExportAction(table));
		
		strip.setMembers(searchButton);
	}
	
	private DynamicForm createSearchForm(SGPanel form){
		/**
	     *订单时间 从  到														
		 */
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM",Util.TI18N.ODR_TIME_FROM());
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
		
	    TextItem EXEC_ORG_ID=new TextItem("EXEC_ORG_ID");
	    EXEC_ORG_ID.setVisible(false);
	    SGText EXEC_ORG_ID_NAME=new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
	    Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false,"50%","50%");
	    EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
	    EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
	    
	    SGCheck C_ORG_FLAG=new SGCheck("C_ORG_FLAG",Util.TI18N.C_ORG_FLAG());
	    C_ORG_FLAG.setValue(true);
	    
	    SGCombo SUPLR_ID = new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());  //供应商
		Util.initSupplier(SUPLR_ID, "");
		
		form.setItems(ODR_TIME_FROM,ODR_TIME_TO,radioGroupItem,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG,SUPLR_ID);
		
		return form;
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		VLayout lay = new VLayout();
		ds = PoOdrKPIDS.getInstance("PodOverdueReport","PodOverdueReport");
		form = new SGPanel();
		
		createList();
		
		ToolStrip strip =new ToolStrip();
		strip.setWidth("100%");
		strip.setHeight(20);
		strip.setAlign(Alignment.RIGHT);
		strip.setMembersMargin(4);
		createBtnWidget(strip);
		
		lay.setMembers(strip,stack);
		
		return lay;
	}
	
	private void createList(){
		table = new SGTable(ds,"100%","100%",false,true,false);
		
		ListGridField SUPPLIER_TYP_NAME = new ListGridField("SUPPLIER_TYP_NAME","承运商类别",80);
		ListGridField SUPPLIER_NAME = new ListGridField("SUPPLIER_NAME","承运商名称",120);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",120);
		ListGridField DRIVER = new ListGridField("DRIVER","驾驶员",60);
		ListGridField TEL = new ListGridField("TEL","联系方式",100);
		ListGridField PRE_POD_TIME = new ListGridField("PRE_POD_TIME","回单应回时间",120);
		ListGridField CUR_TIME = new ListGridField("CUR_TIME","当前日期 ",120);
		ListGridField POD_DELAY_DAYS = new ListGridField("POD_DELAY_DAYS","超期时间",120);
		ListGridField NOTES = new ListGridField("NOTES","备注",80);
		
		
		table.setFields(SUPPLIER_TYP_NAME,SUPPLIER_NAME,PLATE_NO,DRIVER,TEL,PRE_POD_TIME,CUR_TIME,POD_DELAY_DAYS,NOTES);
		
		stack = new SectionStack();
		stack.setWidth("100%");
		stack.setHeight("100%");
		SectionStackSection list = new SectionStackSection(Util.TI18N.LISTINFO());
		list.addItem(table);
		list.setExpanded(true);
		list.setControls(new SGPage(table, true).initPageBtn());
		
		stack.addSection(list);
	}
	
	@Override
	public void initVerify() {
		
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			form.destroy();
			searchWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		PodOverdueReportView view = new PodOverdueReportView();
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
