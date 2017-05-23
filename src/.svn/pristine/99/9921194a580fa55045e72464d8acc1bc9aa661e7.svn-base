package com.rd.client.view.tms;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运输管理--运输执行 --提货装车--开始装货按钮
 * @author wangjun
 *
 */
public class LoadLeftWin extends Window{
	private int width=620;
	private int height=300;
	public ButtonItem confirmItem;
	public Window window = null;
	public DynamicForm form;
	public ListGrid table;
	public DynamicForm mainItem;
	public DataSource ds;
	private LoadJobView view;
	private SGTable LoadLeftTable;
	private SGTable LoadRightTable;
	public ToolStrip toolStrip;
	private Record item;
	public SGDate START_LOAD_TIME;
	
	public LoadLeftWin(DataSource ds, DynamicForm form,
			LoadJobView view,SGTable LoadLeftTable,SGTable LoadRightTable) {
		this.form = form;
		this.ds = ds;
		this.view = view;
		this.LoadLeftTable = LoadLeftTable;
		this.LoadRightTable = LoadRightTable;
	}

	public void createBtnWidget(ToolStrip strip) {

	}

	public void createForm(DynamicForm searchForm) {
		
		IButton confirmItem = new IButton(Util.BI18N.CONFIRM());
		confirmItem.setIcon(StaticRef.ICON_CONFIRM);
		confirmItem.setWidth(60);
		confirmItem.setAutoFit(true);
		confirmItem.setAlign(Alignment.RIGHT);
		confirmItem.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					if(ObjUtil.isNotNull(form.getItem("DOCK").getValue().toString())){
						doOperate();
						window.hide();
						
					}else {
						MSGUtil.sayWarning("请选择装货 DOCK!");
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
        toolStrip.setMembers(confirmItem,cancelItem);
	}
	
	private void doOperate(){
		ListGridRecord[] record = LoadRightTable.getSelection();
		
		String proName = "TRANS_LOAD_PRO_1(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		HashMap<String,String> plate_no = new HashMap<String,String>();
		HashMap<String,String> queue_seq = new HashMap<String,String>();
		HashMap<String,String> unload_name = new HashMap<String,String>();
		HashMap<String,String> CUSTOM_ODR_NO = new HashMap<String,String>();
		HashMap<String,String> LOAD_NO = new HashMap<String,String>();
		
		item= record[0];
		StringBuffer sf = new StringBuffer();
		
		for (int i = 1; i < record.length; i++) {
			if(!item.getAttribute("PLATE_NO").equals(record[i].getAttribute("PLATE_NO"))){
				sf.append(record[i].getAttribute("PLATE_NO"));
				
			}
		}
		if(sf.length() > 0 ){
			MSGUtil.sayWarning("请选择同一车货进入DOCK装货！");
			return;
		}
		for(int i = 0 ; i <record.length ; i++){
			item = record[i];
			queue_seq.put(String.valueOf(i+1), item.getAttribute("QUEUE_SEQ"));
			unload_name.put(String.valueOf(i+1), item.getAttribute("UNLOAD_NAME"));
			CUSTOM_ODR_NO.put(String.valueOf(i+1), item.getAttribute("CUSTOM_ODR_NO"));
			LOAD_NO.put(String.valueOf(i+1), item.getAttribute("LOAD_NO"));
		}
		
		String plate_no  = item.getAttribute("PLATE_NO");
		String CONSIGNER = ObjUtil.ifObjNull(form.getItem("CONSIGNER").getValue(), "").toString();
		String DOCK = form.getItem("DOCK").getValue().toString();
		String STEVEDORE = ObjUtil.ifObjNull(form.getItem("STEVEDORE").getValue()," ").toString();
		String STEVE_COUNT = ObjUtil.ifObjNull(form.getItem("STEVE_COUNT").getValue(),"0").toString();
		String START_LOAD_TIME = ObjUtil.ifObjNull(form.getItem("START_LOAD_TIME").getValue()," ").toString();
		String LOAD_WHSE = item.getAttribute("LOAD_WHSE");
		String SCANNER = ObjUtil.ifObjNull(form.getItem("SCANNER").getValue()," ").toString();
		String LOAD_NO2  = item.getAttribute("LOAD_NO");
		HashMap<String,Object> listMap = new HashMap<String,Object>();
		listMap.put("1", plate_no);
		listMap.put("2", queue_seq);
		listMap.put("3", unload_name);
		listMap.put("4", CONSIGNER);
		listMap.put("5", DOCK);
		listMap.put("6", STEVEDORE);
		listMap.put("7", STEVE_COUNT);
		listMap.put("8", START_LOAD_TIME);
		listMap.put("9", LOAD_WHSE);
		listMap.put("10", SCANNER);
		listMap.put("11", CUSTOM_ODR_NO);
		listMap.put("12", LoginCache.getLoginUser().getUSER_ID());
		listMap.put("13", LOAD_NO);
		listMap.put("14", LOAD_NO2);
		
//		ArrayList<String> idList = new ArrayList<String>();
//		idList.add(form.getItem("PLATE_NO").getValue().toString());
//		idList.add(record.getAttribute("QUEUE_SEQ"));
//		idList.add(ObjUtil.ifObjNull(form.getItem("CONSIGNER").getValue(), "").toString());
//		idList.add(form.getItem("DOCK").getValue().toString());
//		idList.add(ObjUtil.ifObjNull(form.getItem("STEVEDORE").getValue()," ").toString());
//		idList.add(ObjUtil.ifObjNull(form.getItem("STEVE_COUNT").getValue(),"0").toString());
//		idList.add(ObjUtil.ifObjNull(form.getItem("START_LOAD_TIME").getValue()," ").toString());
//		idList.add(record.getAttribute("LOAD_WHSE"));
//		idList.add(ObjUtil.ifObjNull(form.getItem("SCANNER").getValue()," ").toString());
		String json = Util.mapToJson(listMap);
		Util.async.execProcedure(json, proName,new AsyncCallback<String>() { 
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					LoadLeftTable.invalidateCache();
					LoadLeftTable.getField("PLATE_NO").getValueField();
					
				    Criteria crit2 = new Criteria();
				    crit2.addCriteria("OP_FLAG","M");
				    crit2.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
				    crit2.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
				    crit2.addCriteria("ORDER_BY", "DOCK_NAME");//正在装货按照DOCK排序
					crit2.addCriteria("A",!view.crit_flag);
					view.crit_flag = !view.crit_flag;
					LoadLeftTable.fetchData(crit2,new DSCallback() {
							
					  @Override
					  public void execute(DSResponse response, Object rawData, DSRequest request) {
						  LoadLeftTable.selectRecord(LoadLeftTable.getRecord(0));
						  LoadRightTable.invalidateCache();
						  
						  Criteria crit1 = new Criteria();
						  crit1.addCriteria("OP_FLAG","M");
						  crit1.addCriteria("LOAD_STATUS",StaticRef.TRANS_EXPECT);
						  crit1.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
						  LoadRightTable.fetchData(crit1);
						  
					}
					});
					    
					    LoadLeftTable.redraw();
					    LoadRightTable.redraw();
					    
					    view.staLoadButton.disable();
						view.finLoadButton.disable();
						view.cancelButton.enable();
				} else {
					MSGUtil.sayWarning(result.substring(0, result.length()));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	public Window getViewPanel() {

		SGText PLATE_NO=new SGText("PLATE_NO", Util.TI18N.PLATE_NO(),true);
		PLATE_NO.setTitle(ColorUtil.getRedTitle(Util.TI18N.PLATE_NO()));
		PLATE_NO.setDisabled(true);
		SGCombo CONSIGNER=new SGCombo("CONSIGNER", Util.TI18N.CONSIGNER());//发货员
		Util.initComboValue(CONSIGNER, "V_BAS_STAFF","ID","STAFF_NAME"," WHERE STAFF_TYP = 'D59C3EE87335441A80582B49F21E6C0E'");
		
		SGCombo DOCK=new SGCombo("DOCK","DOCK",true);
		DOCK.setTitle(ColorUtil.getRedTitle("DOCK"));
		Util.initComboValue(DOCK, "BAS_WAREHOUSE_DOCK", "ID","DOCK", " WHERE WHSE_ID = '"+view.record.getAttribute("LOAD_WHSE")+"' ORDER BY DOCK ASC");
		
		SGCombo STEVEDORE=new SGCombo("STEVEDORE", Util.TI18N.STEVEDORE());//装卸工
		Util.initComboValue(STEVEDORE, "V_BAS_STAFF","ID","STAFF_NAME"," WHERE STAFF_TYP = '4575FF4BB225474CA4D644B3519DCD0A'");
		
		SGText STEVE_COUNT=new SGText("STEVE_COUNT", Util.TI18N.STEVE_COUNT());//装卸人数
		STEVE_COUNT.setWidth(60);
		
		START_LOAD_TIME = new SGDate("START_LOAD_TIME", Util.TI18N.START_LOAD_TIME(),true);
		SGCombo SCANNER=new SGCombo("SCANNER", Util.TI18N.SCANNER());//扫描员
		Util.initComboValue(SCANNER, "V_BAS_STAFF","ID","STAFF_NAME"," WHERE STAFF_TYP = 'D59C3EE87335441A80582B49F21E6C0E'");
		

		Util.async.getServTime("yyyy/MM/dd HH:mm",new AsyncCallback<String>() {//wangjun 2011-4-20

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				START_LOAD_TIME.setDefaultValue(result);
			}
			
	    });
		
		form = new SGPanel();
		form.setItems(PLATE_NO,CONSIGNER,DOCK,STEVEDORE,STEVE_COUNT,START_LOAD_TIME,SCANNER);
		form.getItem("PLATE_NO").setValue(view.record.getAttribute("PLATE_NO"));
		
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
		
		Tab tab=new Tab(Util.TI18N.START_LOAD());//开始装货
		tab.setPane(form);
		tabSet.addTab(tab);
	
		VLayout lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.addMember(tabSet);

		window = new Window();
		window.setTitle("开始装货");
		window.setLeft("30%");
		window.setTop("35%");
		window.setWidth(width);
		window.setHeight(height);
		window.setCanDragReposition(true);
		window.setCanDragResize(true);
		
		window.addItem(lay1);
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
		window.addCloseClickHandler(new CloseClickHandler() {
			
			//for smartgwt3.0
			/*@Override
			public void onCloseClick(CloseClickEvent event) {
				window.clear();
			}*/

			@Override
			public void onCloseClick(CloseClientEvent event) {
				window.clear();
			}
		});

		return window;
	}
	
//	public static native String getCurTime() /*-{
//
//	var now = new Date();
//	var year=now.getFullYear();
//	var month=now.getMonth()+1;
//	var day=now.getDate();
//	var hour=now.getHours();
//	var minute=now.getMinutes();
//	var second = now.getSecond();
//	if (minute < 10) {
//	    minute = "0" + minute;
//	}
//	var res = year+"/"+month+"/"+ day + " " + hour + ":" + minute +":"+seconds;
//	return res;
//}-*/; 
//}
	public void getCurTime(){
		
	}
}