package com.rd.client.view.tms;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运输管理--运输执行--提货装车--完成装货按钮
 * @author wangjun
 *
 */
public class LoadRightWin extends Window{
	private int width=620;
	private int height=300;  
	public ButtonItem confirmItem;
	public Window window = null;
	public DynamicForm form;
	public ListGrid table;
	public DynamicForm mainItem;
	public DataSource ds;
	public SectionStackSection section;
	public LoadJobView view;
	public ToolStrip toolStrip;
	private SGTable LoadLeftTable;
	@SuppressWarnings("unused")
	private SGTable LoadRightTable;
	private Record item;
	
	public LoadRightWin(DataSource ds, DynamicForm form,
			SectionStackSection p_section, LoadJobView view,
		    SGTable LoadLeftTable, SGTable LoadRightTable) {
		
		this.form = form;
		this.ds = ds;
		this.section = p_section;
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
				
				 doOperate();
				 form.clearValues();
				 window.hide();
				
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
		
		ListGridRecord[] records = LoadLeftTable.getSelection();
		
		String proName = "TRANS_FINISH_LOAD(?,?,?,?,?,?,?,?,?,?,?)";
		
		String END_LOAD_TIME = ObjUtil.ifObjNull(form.getItem("END_LOAD_TIME").getValue()," ").toString();
		//HashMap<String,String> LOAD_WHSE = new HashMap<String,String>();
		//HashMap<String,String> PLATE_NO = new HashMap<String,String>();
		HashMap<String,String> LOAD_NO = new HashMap<String,String>();
		HashMap<String,String> WHSE_ID = new HashMap<String,String>();
		HashMap<String,String> LOAD_STATUS = new HashMap<String,String>();
		HashMap<String,String> UNLOAD_AREA_NAME = new HashMap<String,String>();
		HashMap<String,String> SHPM_NO = new HashMap<String,String>();
		HashMap<String,String> CUSTOM_ODR_NO = new HashMap<String,String>();

		for(int i=0;i<records.length;i++){
			LOAD_STATUS.put(String.valueOf(i+1), records[i].getAttribute("LOAD_STATUS"));
			//LOAD_WHSE.put(String.valueOf(i+1), records[i].getAttribute("LOAD_WHSE"));
			//PLATE_NO.put(String.valueOf(i+1), records[i].getAttribute("PLATE_NO"));
			LOAD_NO.put(String.valueOf(i+1), records[i].getAttribute("LOAD_NO"));
			WHSE_ID.put(String.valueOf(i+1), records[i].getAttribute("LOAD_WHSE"));
			
			LOAD_STATUS.put(String.valueOf(i+1), records[i].getAttribute("LOAD_STATUS"));
			UNLOAD_AREA_NAME.put(String.valueOf(i+1), records[i].getAttribute("UNLOAD_NAME"));
			SHPM_NO.put(String.valueOf(i+1), records[i].getAttribute("SHPM_NO"));
			CUSTOM_ODR_NO.put(String.valueOf(i+1), records[i].getAttribute("CUSTOM_ODR_NO"));

		}
		
		ListGridRecord[] record = LoadLeftTable.getSelection();
		item= record[0];
		String PLATE_NO  = item.getAttribute("PLATE_NO");
		String LOAD_WHSE = item.getAttribute("LOAD_WHSE");
		
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		listmap.put("1", END_LOAD_TIME);
		listmap.put("2", LOAD_WHSE);
		listmap.put("3", PLATE_NO);
		listmap.put("4", LOAD_NO);
		listmap.put("5", WHSE_ID);
		listmap.put("6", LOAD_STATUS);
		listmap.put("7", UNLOAD_AREA_NAME);
		listmap.put("8", SHPM_NO);
		listmap.put("9", CUSTOM_ODR_NO);
		listmap.put("10",LoginCache.getLoginUser().getUSER_ID());
		
	    String json = Util.mapToJson(listmap);
		
	    
	    Util.async.execProcedure(json, proName, new AsyncCallback<String>() {
			
	    	@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.subSequence(0, result.length()))){
				MSGUtil.showOperSuccess();
				ListGridRecord[] record = view.loadLeftTable.getSelection();
				for (int i = 0; i < record.length; i++) {
					record[i].setAttribute("LOAD_STATUS",StaticRef.TRANS_FINISH);
					record[i].setAttribute("LOAD_STATUS_NAME",StaticRef.TRANS_FINISH_NAME);
					view.loadLeftTable.updateData(record[i]);
					
				}
			
			    view.loadLeftTable.redraw();
			    view.staLoadButton.disable();
				view.finLoadButton.disable();
				view.cancelButton.disable();
		    	   
		       }else {
		    	   MSGUtil.sayWarning(result.substring(2,result.length()));
		       }
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	    
	}

	public Window getViewPanel() {
		SGDate END_LOAD_TIME = new SGDate("END_LOAD_TIME", Util.TI18N.END_LOAD_TIME_FINISH(),true);
		END_LOAD_TIME.setWidth(120);
		END_LOAD_TIME.setDefaultValue(Util.getCurTime());
		form = new SGPanel();
		form.setItems(END_LOAD_TIME);
		
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
		
		Tab tab=new Tab(Util.TI18N.FINISH_LOAD());//完成装货
		tab.setPane(form);
		tabSet.addTab(tab);
	
		VLayout lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.addMember(tabSet);

		window = new Window();
		window.setTitle("完成装货");
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