package com.rd.client.view.report;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.report.CustomReportDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
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
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 *   报表管理-->客户自定义报表（万能报表）
 */
@ClassForNameAble
public class Custom_Report extends SGForm implements PanelFactory{
	public SGTable table;
	private CustomReportDS ds;
	private SectionStack section;
	private Window searchWin;
	private SGPanel searchForm;
	private SGDateTime ODR_TIME_FROM;
	private SGDateTime ODR_TIME_TO;
	private Object cur_value;
	private RadioGroupItem radioGroupItem;
	
	/*public Custom_Report(String id) {
		super(id);
	}*/
   
	@Override
	public Canvas getViewPanel() {
	
		//页面总布局
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ds = CustomReportDS.getInstance("R_CUSTOM_REPORT","SYS_PARAM");
		
		//放置按钮
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		//设置详细信息布局
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();
		
		table= new SGTable(ds, "100%", "100%");
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		createFields(table,"");
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		
		//创建Section
		section = new SectionStack();
		section.setWidth("100%");
		section.setHeight("100%");
		
		final SectionStackSection listItem = new SectionStackSection("列表信息");//
	    listItem.setItems(table);
	    listItem.setExpanded(true);
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    section.addSection(listItem);
	    
        main.addMember(toolStrip);
		main.addMember(section);
		
		return main;
	}

	private void createFields(final SGTable table,final String vname) {
		/**
		 * 供应商	订单数	 准时回单率	1DAY	2DAYS	3DAYS	4DAYS	5DAYS	>5DAYS
		 */
		Util.async.getColName(vname, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				String[] cols = result.split(",");
				
				ds.destroy();
				ds = new CustomReportDS("R_CUSTOM_REPORT",vname,cols);
				
				table.setDataSource(ds);
				table.setCanEdit(false);
				table.setShowFilterEditor(true);
				
				ArrayList<ListGridField> list = new ArrayList<ListGridField>();
				for(int i=0;i<cols.length;i++){
					list.add(new ListGridField(cols[i],cols[i],120));
				}
				table.setFields((ListGridField[])(list.toArray(new ListGridField[list.size()])));
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);//查询
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				   searchForm=new SGPanel();
					searchWin = new SearchWin(ds, createSerchForm(searchForm),
							section.getSection(0)).getViewPanel();
			}
		});
		
		 //导出按钮
        IButton expButton = createBtn(StaticRef.EXPORT_BTN);
//      expButton.addClickHandler(new ExportAction(table, "addtime desc"));
        
//        String fieldname="SUPLR_NAME,sum(days0)as num,sum(days1) as day1 ,sum(days2)as day2,sum(days3)as day3,sum(days4)as day4,sum(days5)as day5,sum(days6)as day6,to_char(round(sum(days) * 100/ sum(days0),2)) || '%' as per";
        expButton.addClickHandler(new ExportAction(table));
   
		toolStrip.setMembersMargin(5);
		
		final ComboBoxItem dep = new ComboBoxItem("DEP","部门");
		final ComboBoxItem rep = new ComboBoxItem("REP","报表");
		
		Util.initComboValue(dep, "BAS_CODES", "CODE", "NAME_C", " PROP_CODE='CUSTOM_REP_DEP'");
		dep.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Util.initComboValue(rep,"SYS_REPORT_HEADER","OBJECTS","REPORTNAME_C", " ReportType='" + dep.getValue() +"'");
			}
		});
		
		rep.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				
				if(ObjUtil.isNotNull(event.getValue())){
					createFields(table,rep.getValue().toString());
					table.markForRedraw();
				}
			}
		});
		
		DynamicForm foo = new DynamicForm();
		foo.setTitleWidth(75);
		foo.setNumCols(4);
		foo.setItems(dep,rep);
	
		toolStrip.setMembers(foo,searchButton,expButton);
		
	}

	protected DynamicForm createSerchForm(SGPanel form) {
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
		
		
		
		form.setItems(ODR_TIME_FROM,ODR_TIME_TO,radioGroupItem);
		
		return form;
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public void initVerify() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		Custom_Report view = new Custom_Report();
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
