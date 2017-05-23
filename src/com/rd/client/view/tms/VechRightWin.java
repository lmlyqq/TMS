package com.rd.client.view.tms;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.LoadJob_ConfirmDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
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
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 运输管理--运输执行 --车辆登记--离库登记按钮
 * @author wangjun
 *
 */
public class VechRightWin extends Window{
	private int width=620;
	private int height=300;
	private String title = "离库登记";  //--yuanlei 2011-2-16 wangjun 2011-2-16
	public ButtonItem confirmItem;
	public Window window = null;
	public DynamicForm form;
	public ListGrid table;
	public DynamicForm mainItem;
	public DataSource ds;
	public SectionStackSection section;
	private VechRegistView view;
	public ToolStrip toolStrip;
	private String s;
	private String scann_value = "true";
	private String foo;
	
	public VechRightWin(DataSource ds, DynamicForm form,
			SectionStackSection p_section,VechRegistView view) {
		this.form = form;
		this.ds = ds;
		this.section = p_section;
		this.view = view;
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
				view.loadLeftTable.setData(new RecordList());
				if(form.getItem("LOAD_NO").getValue() != null){
					doconfirm();
				}else {
					MSGUtil.sayWarning("请扫描离调度单号！");
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
		
		IButton confirm_scanner = new IButton("离库确认");
	    confirm_scanner.setIcon(StaticRef.ICON_CONFIRM);
	    confirm_scanner.setWidth(60);
	    confirm_scanner.setAutoFit(true);
	    confirm_scanner.setAlign(Alignment.RIGHT);
	    confirm_scanner.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				doOperate();
				form.clearValues();
				window.hide();
				
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
		
        toolStrip.setMembers(confirm_scanner,confirmItem,cancelItem);
	}
	
	private void scann_confirm(final String load_no){
		ds = LoadJob_ConfirmDS.getInstance("LOAD_JOB_LEAVE_WHSE","TRANS_LOAD_JOB");
//		ds = LoadJob_search_DS.getInstance("v_loadJob_shpm", "TRANS_SHIPMENT_HEADER");
		
		view.loadLeftTable.setDataSource(ds);
		Criteria crit = new Criteria();
		
		if(!ObjUtil.isNotNull(form.getItem("LOAD_NO").getValue())){
			MSGUtil.sayWarning(Util.TI18N.LOAD_NO()+"不能为空！");
			return;
		}
		crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
		crit.addCriteria("LOAD_NO",load_no);
		crit.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
		
//		crit.addCriteria("STATUS", "STATUS");
		view.loadLeftTable.fetchData(crit,new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				form.getItem("LOAD_NO").clearValue();
				if(view.loadLeftTable.getRecords().length < 1){
					MSGUtil.sayWarning(load_no+" 已经离库或者不合法，不能做离库操作!");
				}
			}
		});
	}
	
	private void doconfirm(){
		ds = LoadJob_ConfirmDS.getInstance("V_TRANS_LOAD_JOB_2","TRANS_LOAD_JOB");
//		DataSource ds2 = LoadJob_search_DS.getInstance("v_loadJob_shpm", "TRANS_SHIPMENT_HEADER");
		
		view.loadLeftTable.setDataSource(ds);
		Criteria crit = new Criteria();
		
		if(!ObjUtil.isNotNull(form.getItem("LOAD_NO").getValue())){
			MSGUtil.sayWarning(Util.TI18N.LOAD_NO()+"不能为空！");
			return;
		}
		crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
		crit.addCriteria(form.getValuesAsCriteria());
		crit.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
//		crit.addCriteria("STATUS", "STATUS");
		view.loadLeftTable.fetchData(crit,new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				if(view.loadLeftTable.getRecords().length < 1){
					MSGUtil.sayWarning(form.getItem("LOAD_NO").getValue().toString()+" 已经离库或者不合法，不能做离库操作!");
				}
			}
		});
	}
	private void doOperate(){
		String proName = "TRANS_LEAVE_PRO(?,?,?)";
		//ArrayList<String> list = new ArrayList<String>();
		ListGridRecord[] record = view.loadLeftTable.getRecords();
		String load_no = record[0].getAttribute("LOAD_NO");
		//list.add(load_no);
        HashMap<String,String> list = new HashMap<String,String>();//wangjun 2011-7-20
        list.put("1",load_no);
		list.put("2",LoginCache.getLoginUser().getUSER_ID());
        
		Util.async.execProcedure(list, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.subSequence(0, 2))){
					MSGUtil.showOperSuccess();
					view.loadRightTable.invalidateCache();
					ListGridRecord[] list = view.loadLeftTable.getRecords();
					for(int i = 0 ; i < list.length ; i++){
						list[i].setAttribute("LOAD_STATUS_NAME", StaticRef.TRANS_LEAVE_NAME);
					}
					view.loadLeftTable.redraw();
				}else if("01".equals(result.substring(0,2))){
					MSGUtil.sayWarning("车辆未装货完成，无法离库！");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

	public Window getViewPanel() {

		SGText LOAD_NO=new SGText("LOAD_NO", Util.TI18N.LOAD_NO());
		
		SGCheck SCANN = new SGCheck("","使用扫描器");
		SCANN.setColSpan(2);
		SCANN.setValue(true);
		
		 LOAD_NO.addChangedHandler(new ChangedHandler() {
				
				@Override
				public void onChanged(ChangedEvent event) {
					if(ObjUtil.isNotNull(form.getItem("LOAD_NO").getValue())){
						s = form.getItem("LOAD_NO").getValue().toString();
						if("true".equals(scann_value)){
							if(s.length() == 12){
								foo = s.substring(0,12);
								scann_confirm(foo);
							}else {
								return;
							}
						}
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
		form.setItems(LOAD_NO,SCANN);
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
		
		Tab tab=new Tab(Util.TI18N.LEAVE_REG());//离库登记 
		tab.setPane(form);
		tabSet.addTab(tab);
	
		VLayout lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.addMember(tabSet);

		window = new Window();
		window.setTitle(title);  //--yuanlei 2011-2-16  wangjun 2011-2-16
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

		return window;
	}
}
