package com.rd.client.win;



import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.SkuDS1;
import com.rd.client.view.tms.OrderView;
import com.rd.client.view.tms.SFOrderView;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.KeyPressEvent;
import com.smartgwt.client.widgets.events.KeyPressHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 货品弹出窗口
 * @author fanglm
 * 
 */
public class SkuWin extends Window {
	
	private int width = 350;
	private int height = 300;
	private String top = "38%";
	private String left = "40%";
	private String title = "货品";
	public Window window;
	private DataSource ds;
	private ListGrid table;
	private int row;
	
	private String customer_id;
	private SGForm view;
	private String shpm_no;
	private String full_index;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */

	
	
	public SkuWin(ListGrid table,String customer_id,int row,String top,String left){
		this.table = table;
		this.row = row;
		this.top = top;
		this.left = left;
		this.customer_id = customer_id;
	}
	public SkuWin(ListGrid table,int row,String top,String left,String shpm_no){
		this.table = table;
		this.row = row;
		this.top = top;
		this.left = left;
		this.shpm_no = shpm_no;
	}
	
	public SkuWin(ListGrid table,int row,String top,String left){
		this.table = table;
		this.row = row;
		this.top = top;
		this.left = left;
	}
	
	public SkuWin(ListGrid table,String customer_id,int row,String top,String left,SGForm view,String full_index){
		this.table = table;
		this.row = row;
		this.top = top;
		this.left = left;
		this.customer_id = customer_id;
		this.view = view;
		this.full_index = full_index;
	}
	
	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		ds = SkuDS1.getInstance("SF_SKU","BAS_SKU");
		final TextItem FULL_INDEX = new TextItem("FULL_INDEX",Util.TI18N.FUZZYQRY());
		FULL_INDEX.setShowTitle(false);
		FULL_INDEX.setColSpan(4);
		FULL_INDEX.setWidth(180);
		FULL_INDEX.setValue(full_index);
		SGButtonItem searchBtn = new SGButtonItem(StaticRef.FETCH_BTN);
		final SGPanel searchPanel = new SGPanel();
		searchPanel.setTitleWidth(100);
		searchPanel.setItems(FULL_INDEX,searchBtn);
		
		
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		ListGridField ADDR_CODE = new ListGridField("SKU",80);
		ListGridField ADDR_NAME = new ListGridField("SKU_CNAME",140);
		ListGridField COMMON_FLAG = new ListGridField("COMMON_FLAG",80);
		addrList.setFields(ADDR_CODE,ADDR_NAME,COMMON_FLAG);
		
		final Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		
		if(ObjUtil.isNotNull(customer_id)){
			criteria.addCriteria("CUSTOMER_ID",customer_id);
		}
		if(ObjUtil.isNotNull(full_index)){
			criteria.addCriteria("FULL_INDEX", JSOHelper.getAttribute(table.getJsObj(), "FULL_INDEX"));
		}
		if(ObjUtil.isNotNull(shpm_no)){
			criteria.addCriteria("SHPM_NO",shpm_no);
		}
		criteria.addCriteria("INIT_FLAG","Y");
		addrList.fetchData(criteria,new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				if(addrList.getRecordList().getLength() > 0){
					addrList.selectRecord(0);
					addrList.focus();
				}
				
			}
		});
	
        searchBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
					
					Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG","M");
					criteria.addCriteria("INIT_FLAG","Y");
					if(ObjUtil.isNotNull(customer_id)){
						criteria.addCriteria("CUSTOMER_ID",customer_id);
					}
					if(ObjUtil.isNotNull(shpm_no)){   // wangjun 2010-2-27
						criteria.addCriteria("SHPM_NO",shpm_no);
					}
					
					if(FULL_INDEX.getValue() != null){
						criteria.addCriteria(searchPanel.getValuesAsCriteria());
					}
	        		addrList.fetchData(criteria,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							if(addrList.getRecordList().getLength() > 0){
								addrList.selectRecord(0);
							}
							
						}
					});  
				
			}
		});
        //监听回车键事件
        addrList.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if("Enter".equals(event.getKeyName())){
					window.hide();
					window.destroy();
					Record record = addrList.getSelectedRecord();
					selectRecord(record);
				}
				
			}
		});
        
        
        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				window.hide();
				window.destroy();
				Record record = event.getRecord();
				selectRecord(record);
			}
		});
        
        lay.addMember(searchPanel);
        lay.addMember(addrList);
		
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(lay);
		window.setShowCloseButton(true);
		window.show();
		
		return window;
	}
	public void selectRecord(Record record){
		table.setEditValue(row, "SKU_ID", record.getAttributeAsString("ID"));
		table.setEditValue(row, "SKU_NAME", record.getAttribute("SKU_CNAME"));
		table.setEditValue(row, "SKU", record.getAttribute("SKU"));
		table.setEditValue(row, "TEMPERATURE1", record.getAttributeAsString("TRANS_COND"));
		table.setEditValue(row, "PACK_ID", record.getAttributeAsString("PACK_ID"));
		table.setEditValue(row, "UOM", record.getAttributeAsString("TRANS_UOM"));
		table.setEditValue(row, "QNTY", "1");
		table.setEditValue(row, "VOL", "0.00");
		table.setEditValue(row, "G_WGT","0.00");
		table.setEditValue(row, "N_WGT","0.00");
		table.setEditValue(row, "VOL_GWT_RATIO", record.getAttributeAsString("VOL_GWT_RATIO"));
		if(view != null) {
			if(view.getClass().equals(OrderView.class)) {
				table.startEditing(row, 2, true);
			}
			else if(view.getClass().equals(SFOrderView.class)) {
				table.startEditing(row, 4, true);
			}
			else if(view.getClass().equals(TmsTrackView.class)) {
				table.startEditing(row, 4, true);
				table.setEditValue(row, "SKU_CODE", record.getAttribute("SKU"));
			}
		}
	}
}
