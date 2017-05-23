package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.KPIExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.report.R_KPI_COLL_DS;
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
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
@ClassForNameAble
public class R_KPI_COLL_VIEW extends SGForm implements PanelFactory {
	private DataSource DS;
	private SGTable table;
	private Window searchWin;
    private SGPanel searchForm;
	private SectionStack section;
	private SGDateTime ODR_TIME_FROM;
	private SGDateTime ODR_TIME_TO;
	private Object cur_value;
	private RadioGroupItem radioGroupItem;

	/*public R_KPI_COLL_VIEW(String id) {
		super(id);
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
//      expButton.addClickHandler(new ExportAction(table));
        
        String fieldname="suplr_name,sum(S_FATHER) as NUM,sum(tot_qnty) as tot_qnty,sum(tot_qnty_each) as tot_qnty_each,trans_uom,sum(tot_vol) as tot_vol,sum(tot_gross_w) as tot_grossw,to_char(round(sum(S_CHILD) * 100/ sum(S_FATHER),2)) || '%' as load_rate,to_char(round(sum(R_CHILD) * 100/ sum(S_FATHER), 2)) || '%' as unload_rate,sum(S_FATHER) - sum(R_CHILD) as unload_count,to_char(round(sum(P_CHILD) * 100/ sum(S_FATHER),2)) || '%' as pod_rate,sum(S_FATHER) - sum(P_CHILD) as pod_count,to_char(round(sum(LOS_COUNT) * 100/ sum(S_FATHER), 2)) || '%' as loss_rate,sum(LOS_QNTY) as LOS_QNTY";
        expButton.addClickHandler(new KPIExportAction(table,fieldname,"addtime desc"));
       
        
        toolStrip.setMembersMargin(2);
        toolStrip.setMembers(searchButton,expButton);

	}
	
	public DynamicForm createSerchForm(SGPanel form) {
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
		// TODO Auto-generated method stub

	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolstrip = new ToolStrip();
		toolstrip.setAlign(Alignment.RIGHT);
		
		DS = R_KPI_COLL_DS.getInstance("R_KPI_COLL");
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
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),77);
		ListGridField ODR_NUM = new ListGridField("NUM",Util.TI18N.ODR_NUM(),60);
		ListGridField R_TOT_QNTY = new ListGridField("TOT_QNTY",Util.TI18N.R_TOT_QNTY(),85);
		ListGridField R_TOT_QNTY_EACH = new ListGridField("TOT_QNTY_EACH",Util.TI18N.R_TOT_QNTY_EACH(),85);
		ListGridField ORD_PACK_ID = new ListGridField("TRANS_UOM",Util.TI18N.ORD_PACK_ID(),65);
		ListGridField VOLUME = new ListGridField("TOT_VOL",Util.TI18N.VOLUME(),55);
		VOLUME.setAlign(Alignment.RIGHT);
		ListGridField GROSSWEIGHT = new ListGridField("TOT_GROSSW",Util.TI18N.GROSSWEIGHT(),55);
		GROSSWEIGHT.setAlign(Alignment.RIGHT);
		ListGridField R_ON_TIME = new ListGridField("LOAD_RATE",Util.TI18N.R_ON_TIME(),80);
		ListGridField R_UNLOAD_RATE = new ListGridField("UNLOAD_RATE",Util.TI18N.R_UNLOAD_RATE(),80);
		ListGridField R_UNLOAD_ACCOUNT = new ListGridField("UNLOAD_COUNT",Util.TI18N.R_UNLOAD_ACCOUNT(),85);
		ListGridField R_POD_RATE = new ListGridField("POD_RATE",Util.TI18N.R_POD_RATE(),85);
		ListGridField R_POD_ACCOUNT = new ListGridField("POD_COUNT",Util.TI18N.R_POD_ACCOUNT(),85);
		ListGridField R_LOASS_RATE = new ListGridField("LOSS_RATE",Util.TI18N.R_LOASS_RATE(),85);
		ListGridField R_LOASS_QNTY = new ListGridField("LOS_QNTY",Util.TI18N.R_LOASS_QNTY(),85);
		
		Util.initFloatListField(VOLUME, StaticRef.VOL_FLOAT);
		Util.initFloatListField(GROSSWEIGHT, StaticRef.GWT_FLOAT);
		table.setFields(SUPLR_NAME,ODR_NUM,R_TOT_QNTY,R_TOT_QNTY_EACH,ORD_PACK_ID,VOLUME,GROSSWEIGHT,R_ON_TIME,
				R_UNLOAD_RATE,R_UNLOAD_ACCOUNT,R_POD_RATE,R_POD_ACCOUNT,R_LOASS_RATE,R_LOASS_QNTY);
	}
	
	@Override
	public void initVerify() {

	}

	@Override
	public void onDestroy() {
		if (searchWin != null ) {
			searchWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		R_KPI_COLL_VIEW view = new R_KPI_COLL_VIEW();
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
