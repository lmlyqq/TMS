package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.report.Supplr_Job_Header_DS;
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
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 供应商业务汇总表
 * @author lijun
 *
 */
@ClassForNameAble
public class Supplr_Job_Header extends SGForm implements PanelFactory {
	 private DataSource DS;
	 private SGTable table;
	 private Window searchWin;
     private SGPanel searchForm;
	 private SectionStack section;
	 private SGDateTime ODR_TIME_FROM;
	 private SGDateTime ODR_TIME_TO;
	 private Object cur_value;
	 private RadioGroupItem radioGroupItem;
	 
	/*public Supplr_Job_Header(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}*/

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
        toolStrip.setMembersMargin(2);
        toolStrip.setMembers(searchButton,expButton);

	}
	
public DynamicForm createSerchForm(SGPanel form) {
		
		
		
//		SGCombo TRANS_SRVC_ID= new SGCombo("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID(),true);//运输服务
//		Util.initTrsService(TRANS_SRVC_ID, "");
//		TRANS_SRVC_ID.setWidth(127);
		
		ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM",Util.TI18N.ODR_TIME_FROM(),true);
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
	    
	    SGCombo SUPLR_ID=new SGCombo("SUPLR_ID",Util.TI18N.SUPLR_NAME());
	    Util.initSupplier(SUPLR_ID, "");
	    
	    TextItem EXEC_ORG_ID=new TextItem ("EXEC_ORG_ID");
	    EXEC_ORG_ID.setVisible(false);
	    TextItem EXEC_ORG_ID_NAME=new TextItem("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
	    Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
	    EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
	    EXEC_ORG_ID_NAME.setWidth(120);
	    EXEC_ORG_ID_NAME.setColSpan(2);
	    EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
	    EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
	    
	    SGCheck C_ORG_FLAG=new SGCheck("C_ORG_FLAG",Util.TI18N.C_ORG_FLAG());
	    C_ORG_FLAG.setValue(true);
		
		form.setItems(ODR_TIME_FROM,ODR_TIME_TO,radioGroupItem,SUPLR_ID,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG);
		return form;
	}

	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub

	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolstrip = new ToolStrip();
		toolstrip.setAlign(Alignment.RIGHT);
		
		DS = Supplr_Job_Header_DS.getInstance("R_MNG_SUPLR_SUM");
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
		ListGridField CAR_ORG_ID = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),90);
//		ListGridField TRANS_UOM = new ListGridField("TRANS_UOM",Util.TI18N.ORD_PACK_ID(),70);
		ListGridField UNLOAD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.R_LOAD_QNTY(),90);
//		ListGridField LOSDAM = new ListGridField("DAM_QNTY",Util.TI18N.LOSDAM_FLAG(),90);
		ListGridField UNLOAD_VOL = new ListGridField("LD_VOL",Util.TI18N.R_UNLOAD_VOL(),90);
		ListGridField UNLOAD_NWGT = new ListGridField("LD_GWGT",Util.TI18N.R_NWGT(),90);
//		ListGridField COST = new ListGridField("PRE_FEE",Util.TI18N.R_COST(),90);
//		ListGridField PRECENT = new ListGridField("PER",Util.TI18N.R_PRCENT(),90);
		
		Util.initFloatListField(UNLOAD_QNTY, StaticRef.QNTY_FLOAT);
//		Util.initFloatListField(LOSDAM, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(UNLOAD_VOL, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(UNLOAD_NWGT, StaticRef.QNTY_FLOAT);
//		Util.initFloatListField(COST, StaticRef.PRICE_FLOAT);
		
		table.setFields(CAR_ORG_ID,UNLOAD_QNTY,UNLOAD_VOL,UNLOAD_NWGT);
		
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
		Supplr_Job_Header view = new Supplr_Job_Header();
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
