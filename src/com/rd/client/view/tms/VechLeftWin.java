package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.LoadJob_search_DS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 
 * 运输管理--运输执行 --车辆登记--【到库登记】按钮
 * @author wangjun
 *
 */
public class VechLeftWin extends Window{
	private int width=620;
	private int height=300;
	private String title = "到库登记";  //--yuanlei 2011-2-16
	public Window window = null;
	public DynamicForm form;
	public ListGrid table;
	public DynamicForm mainItem;
	@SuppressWarnings("unused")
	private DynamicForm pageForm;
	public SectionStackSection section;
	public SGTable loadLeftTable;
	public SGTable loadRightTable;
	public Record clickrecord;
	public Criteria criteria;
	public ButtonItem cancelItem;
	public ButtonItem confirmItem;
	public ButtonItem scannerConfirmItem;
	public boolean crit_flag = true;
	public ToolStrip toolStrip;
	private Record item;
	private String scann_value = "true";
	private String s;
	
	public VechLeftWin(DataSource ds, DynamicForm form,
			SectionStackSection p_section, SGTable loadLeftTable,SGTable loadRightTable) {
		this.form = form;
		this.section = p_section;
		this.loadLeftTable=loadLeftTable;
		this.loadRightTable=loadRightTable;
	}


	public void createBtnWidget(ToolStrip strip) {

	}

	public void createForm(DynamicForm searchForm) {
		IButton scannerConfirmItem = new IButton(Util.BI18N.SCANNERCONFIRM());
		scannerConfirmItem.setIcon(StaticRef.ICON_SAVE);
		scannerConfirmItem.setWidth(60);
		scannerConfirmItem.setAutoFit(true);
		scannerConfirmItem.setAlign(Alignment.RIGHT);
		scannerConfirmItem.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if(loadLeftTable.getRecords().length >0){
					String vehicle_type = loadLeftTable.getRecords()[0].getAttribute("TRANS_VEHICLE_TYP_ID");
					String plate_no = form.getItem("TRANS_PLATE_NO").getDisplayValue();
					String driver = form.getItem("TRANS_DRIVER").getDisplayValue();
					String moblie = form.getItem("TRANS_MOBILE").getDisplayValue();
					StringBuffer sf = new StringBuffer();
					if(!ObjUtil.isNotNull(form.getItem("TRANS_PLATE_NO").getDisplayValue())){
						sf.append(Util.TI18N.PLATE_NO());
					}
					if(!ObjUtil.isNotNull(vehicle_type)){
						sf.append(Util.TI18N.VEHICLE_TYP());
					}
					if(!ObjUtil.isNotNull(form.getItem("TRANS_DRIVER").getDisplayValue())){
						sf.append(Util.TI18N.DRIVER1());
					}
					if(!ObjUtil.isNotNull(form.getItem("TRANS_MOBILE").getDisplayValue())){
					    sf.append(Util.TI18N.MOBILE());	
					}
					
//					if(sf.length() > 0){
//						MSGUtil.sayWarning("车辆登记信息不全，请审核！");
//					}else{
//						//第一件事情是隐藏查询窗口
//						window.hide();
//						form.clearValues();
//						finish_scanner(vehicle_type,plate_no,driver,moblie);
//					}
					
					//第一件事情是隐藏查询窗口
					window.hide();
					form.clearValues();
					finish_scanner(vehicle_type,plate_no,driver,moblie);
					
					
				} else {
					MSGUtil.sayWarning("请扫描作业单!");
				}
			}
		});
		IButton confirmItem = new IButton(Util.BI18N.CONFIRM());
		confirmItem.setIcon(StaticRef.ICON_CONFIRM);
		confirmItem.setWidth(60);
		confirmItem.setAutoFit(true);
		confirmItem.setAlign(Alignment.RIGHT);
		confirmItem.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if(s.length() >= 12){
					String foo = s.substring(0,12);
					scann_confirm(foo);
				}else {
					return;
				}
			}
		});
		
		IButton cancelItem = new IButton(Util.BI18N.CANCEL());
		cancelItem.setIcon(StaticRef.ICON_CANCEL);
		cancelItem.setWidth(60);
		cancelItem.setAutoFit(true);
		cancelItem.setAlign(Alignment.RIGHT);
		cancelItem.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				form.clearValues();
			}
		});
		
		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
        toolStrip.setMembers(scannerConfirmItem,confirmItem,cancelItem);
	}
	
	private void scann_confirm(final String load_no){
		String proName = "SELECT_COUNT_PRO(?,?)";
		ArrayList<String> list = new ArrayList<String>();
		list.add(load_no);
		
		Util.async.execProcedure(list, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {

				if("00".equals(result.substring(0, 2))){
					DataSource shpmDS = LoadJob_search_DS.getInstance("v_loadJob_shpm", "TRANS_SHIPMENT_HEADER");
					loadLeftTable.setDataSource(shpmDS);
					loadLeftTable.invalidateCache();
					Criteria crit = new Criteria();
					crit.addCriteria("OP_FLAG", "M");
					crit.addCriteria("USER_ID", LoginCache.getLoginUser().getUSER_ID());
					crit.addCriteria("STATUS", StaticRef.SHPM_DIPATCH);
					crit.addCriteria("LOAD_NO",load_no);
					crit.addCriteria("SHPM_TIME", "SHPM_TIME");
					loadLeftTable.fetchData(crit,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							form.getItem("LOAD_NO").clearValue();
							ListGridRecord[] record = loadLeftTable.getRecords();
							if(record.length > 0){
//							System.out.println(record[0].getAttribute("TRANS_PLATE_NO")+""+record[0].getAttribute("TRANS_VEHICLE_TYP_ID")+""+record[0].getAttribute("TRANS_DRIVER")+""+record[0].getAttribute("TRANS_MOBILE"));
								form.getItem("TRANS_PLATE_NO").setValue(record[0].getAttribute("TRANS_PLATE_NO"));
								form.getItem("TRANS_VEHICLE_TYP_ID_NAME").setValue(record[0].getAttribute("TRANS_VEHICLE_TYP_ID_NAME"));
								form.getItem("TRANS_DRIVER").setValue(record[0].getAttribute("TRANS_DRIVER"));
								form.getItem("TRANS_MOBILE").setValue(record[0].getAttribute("TRANS_MOBILE"));
							}else{
								MSGUtil.sayWarning("没有查询到符合条件的数据。");
							}
						}
					});
				}else if("01".equals(result.substring(0, 2))){
					MSGUtil.sayWarning(result.substring(2, result.length()));
					form.getItem("LOAD_NO").clearValue();
				}else if("02".equals(result.substring(0, 2))){
					form.getItem("LOAD_NO").clearValue();
					MSGUtil.sayWarning(result.substring(2, result.length()));
				}else if("03".equals(result.substring(0, 2))){
					form.getItem("LOAD_NO").clearValue();
					MSGUtil.sayWarning(result.substring(2, result.length()));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	@SuppressWarnings("unused")
	private void doConfirm(final String load_no){
		String proName = "SELECT_COUNT_PRO(?,?)";
		ArrayList<String> list = new ArrayList<String>();
		list.add(load_no);
		
		Util.async.execProcedure(list, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {

				if("00".equals(result.substring(0, 2))){
					DataSource shpmDS = LoadJob_search_DS.getInstance("v_loadJob_shpm", "TRANS_SHIPMENT_HEADER");
					loadLeftTable.setDataSource(shpmDS);
					loadLeftTable.invalidateCache();
					Criteria crit = new Criteria();
					crit.addCriteria("OP_FLAG", "M");
					crit.addCriteria("USER_ID", LoginCache.getLoginUser().getUSER_ID());
					crit.addCriteria("STATUS", StaticRef.SHPM_DIPATCH);
					crit.addCriteria("LOAD_NO",load_no);
					crit.addCriteria("SHPM_TIME", "SHPM_TIME");
					loadLeftTable.fetchData(crit,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							form.getItem("LOAD_NO").clearValue();
							ListGridRecord[] record = loadLeftTable.getRecords();
							//System.out.println(record[0].getAttribute("TRANS_PLATE_NO")+""+record[0].getAttribute("TRANS_VEHICLE_TYP_ID_NAME")+""+record[0].getAttribute("TRANS_DRIVER")+""+record[0].getAttribute("TRANS_MOBILE"));
							form.getItem("TRANS_PLATE_NO").setValue(record[0].getAttribute("TRANS_PLATE_NO"));
							form.getItem("TRANS_VEHICLE_TYP_ID_NAME").setValue(record[0].getAttribute("TRANS_VEHICLE_TYP_ID_NAME"));
							form.getItem("TRANS_DRIVER").setValue(record[0].getAttribute("TRANS_DRIVER"));
							form.getItem("TRANS_MOBILE").setValue(record[0].getAttribute("TRANS_MOBILE"));
						}
					});
				}else if("01".equals(result.substring(0, 2))){
					MSGUtil.sayWarning(result.substring(2, result.length()));
					form.getItem("LOAD_NO").clearValue();
				}else if("02".equals(result.substring(0, 2))){
					form.getItem("LOAD_NO").clearValue();
					MSGUtil.sayWarning(result.substring(2, result.length()));
				}else if("03".equals(result.substring(0, 2))){
					form.getItem("LOAD_NO").clearValue();
					MSGUtil.sayWarning(result.substring(2, result.length()));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	
	private void finish_scanner(String vehicle_type,String plate_no,String driver,String moblie){
		ListGridRecord[] record = loadLeftTable.getRecords();
		HashMap<String,String> shpm_no = new HashMap<String,String>();
		
		for(int i = 0 ; i <= record.length - 1; i++){
			item = record[i];
			shpm_no.put(String.valueOf(i+1), ObjUtil.ifObjNull(item.getAttribute("SHPM_NO"), "").toString());
		}
		final String load_no = item.getAttribute("LOAD_NO");
//		String plate_no = record[0].getAttribute("TRANS_PLATE_NO");
//		String vehicle_type = record[0].getAttribute("TRANS_VEHICLE_TYP_ID_NAME");
//		String driver = record[0].getAttribute("TRANS_DRIVER");
//		String moblie = record[0].getAttribute("TRANS_MOBILE");
//		String vehicle_type = form.getItem("TRANS_VEHICLE_TYP_ID_NAME").getDisplayValue();
//		String plate_no = form.getItem("TRANS_PLATE_NO").getDisplayValue();
//		String driver = form.getItem("TRANS_DRIVER").getDisplayValue();
//		String moblie = form.getItem("TRANS_MOBILE").getDisplayValue();
		String exec_org_id = LoginCache.getLoginUser().getDEFAULT_ORG_ID();
		
		HashMap<String,Object> listMap = new HashMap<String,Object>();
		listMap.put("1", shpm_no);
		listMap.put("2", load_no);
		listMap.put("3", vehicle_type);
		listMap.put("4", plate_no);
		listMap.put("5", driver);
		listMap.put("6", moblie);
		listMap.put("7", LoginCache.getLoginUser().getUSER_ID());
		listMap.put("8", exec_org_id);
		
		
		
		String json = Util.mapToJson(listMap);
		String proName = "TRANS_CONFIRM_PRO(?,?,?,?,?,?,?,?,?)";
		Util.async.execProcedure(json, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){
					loadRightTable.invalidateCache();
					Criteria crit = new Criteria();
					crit.addCriteria("OP_FLAG","M");
					crit.addCriteria("LOAD_STATUS", StaticRef.TRANS_EXPECT);
					crit.addCriteria("LOAD_NO",load_no);
					crit.addCriteria(form.getValuesAsCriteria());
					crit.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
					loadRightTable.fetchData(crit);
					loadLeftTable.setData(new RecordList());
					
					
				}else if("01".equals(result.substring(0, 2))){
					MSGUtil.sayWarning("车辆已经进入库区，请重新查询！");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}

	public Window getViewPanel() {

		SGText LOAD_NO=new SGText("LOAD_NO", Util.TI18N.LOAD_NO());
		LOAD_NO.setColSpan(2);
		SGCheck SCANN = new SGCheck("","使用扫描器");
		SCANN.setColSpan(2);
		SCANN.setValue(true);
		LOAD_NO.setLength(12);
		
		SGText TRANS_PLATE_NO = new SGText("TRANS_PLATE_NO",Util.TI18N.PLATE_NO(),true);
		SGText TRANS_VEHICLE_TYP_ID = new SGText("TRANS_VEHICLE_TYP_ID",Util.TI18N.VEHL_TYP());
		TRANS_VEHICLE_TYP_ID.setVisible(false);
		SGText TRANS_VEHICLE_TYP_ID_NAME = new SGText("TRANS_VEHICLE_TYP_ID_NAME",Util.TI18N.VECHILE_TYP_ID());
		TRANS_VEHICLE_TYP_ID_NAME.setDisabled(true);
		SGText TRANS_DRIVER = new SGText("TRANS_DRIVER",Util.TI18N.DRIVER1());
		SGText TRANS_MOBILE = new SGText("TRANS_MOBILE",Util.TI18N.MOBILE());
		
		TRANS_PLATE_NO.setTitle(ColorUtil.getRedTitle(Util.TI18N.PLATE_NO()));
		TRANS_VEHICLE_TYP_ID_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.VECHILE_TYP_ID()));
		TRANS_DRIVER.setTitle(ColorUtil.getRedTitle(Util.TI18N.DRIVER1()));
		TRANS_MOBILE.setTitle(ColorUtil.getRedTitle(Util.TI18N.MOBILE()));
	
		
	    LOAD_NO.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if(ObjUtil.isNotNull(form.getItem("LOAD_NO").getValue())){
					s = form.getItem("LOAD_NO").getValue().toString();
					if("true".equals(scann_value)){
						if(s.length() >= 12){
							String foo = s.substring(0,12);
							scann_confirm(foo);
						}else {
							return;
						}
					}
//					else {
//						MSGUtil.sayWarning("调度单不能为空！");
//					}
				}
			}
		});
		
		SCANN.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				scann_value= event.getValue().toString();
				//System.out.println(event.getValue());
				
			}
		});
		
		form = new SGPanel();
		form.setItems(LOAD_NO,SCANN,TRANS_PLATE_NO,TRANS_VEHICLE_TYP_ID,TRANS_VEHICLE_TYP_ID_NAME,TRANS_DRIVER,TRANS_MOBILE);
		form.setHeight(height / 2);
		form.setWidth(width - 30);
		form.setNumCols(8);
		form.setPadding(10);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
		form.setHeight100();
		createForm(form);
		
		TabSet tabSet=new TabSet();
		tabSet.setWidth("100%");
		tabSet.setHeight("100%");
		
		Tab tab=new Tab(Util.TI18N.ARRIVE_REG());//ARRIVE_REG "到库登记"
		tab.setPane(form);
		tabSet.addTab(tab);

		window = new Window();
		window.setTitle(title);  //--yuanlei 2011-2-16
		window.setLeft("30%");
		window.setTop("35%");
		window.setWidth(width);
		window.setHeight(height);
		window.setCanDragReposition(true);
		window.setCanDragResize(true);
		
		window.addItem(tabSet);
		window.addItem(toolStrip);
		window.setShowCloseButton(true);
		
		window.show();
		window.addMinimizeClickHandler(new MinimizeClickHandler() {

			@Override
			public void onMinimizeClick(MinimizeClickEvent event) {
				window.setMinimized(false);
				window.hide();
				event.cancel();
			}

		});

		return window;
	}
}
