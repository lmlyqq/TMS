package com.rd.client.win;



import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.AddrDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 地址点公用弹出窗口
 * @author Administrator
 * 
 */
public class AddrWin extends Window {
	
	private int width = 480;
	private int height = 400;
	private String top = "38%";
	private String left = "40%";
	private String title = "地址点";
	public Window window;
	private DataSource ds;
	private ListGrid table;
	private int row;
	private String customer_id;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */

	
	
	public AddrWin(ListGrid table,String customer_id,int row,String top,String left){
		this.table = table;
		this.row = row;
		this.top = top;
		this.left = left;
		this.customer_id = customer_id;
	}
	
	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		//树形结构
		ds = AddrDS.getInstance("BAS_ADDRESS");
		final TextItem FULL_INDEX = new TextItem("FULL_INDEX",Util.TI18N.FUZZYQRY());
		FULL_INDEX.setShowTitle(false);
		FULL_INDEX.setColSpan(5);
		FULL_INDEX.setWidth(220);
		SGButtonItem searchBtn = new SGButtonItem(StaticRef.FETCH_BTN);
		final SGPanel searchPanel = new SGPanel();
		searchPanel.setTitleWidth(100);
		searchPanel.setItems(FULL_INDEX,searchBtn);
		
		
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE",80);
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME",100);
		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME(),80);
		ListGridField ADDRESS = new ListGridField("ADDRESS",200);
		addrList.setFields(ADDR_CODE,ADDR_NAME,AREA_NAME,ADDRESS);
	
        searchBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

					Criteria criteria = new Criteria();
	        		criteria.addCriteria("OP_FLAG","M");
	        		criteria.addCriteria("ENABLE_FLAG","Y");
	        		criteria.addCriteria("AND_LOAD_FLAG","Y");
	        		if(ObjUtil.isNotNull(customer_id)){
	        			criteria.addCriteria("CUSTOMER_ID",customer_id);
	        		}
	        		if(FULL_INDEX.getValue() != null){
						criteria.addCriteria(searchPanel.getValuesAsCriteria());
					}
	        		addrList.fetchData(criteria);  

				
			}
		});
        
        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				table.setEditValue(row, "LOAD_NAME", record.getAttribute("ADDR_NAME"));
				table.setEditValue(row, "LOAD_ID", record.getAttribute("ID"));
				table.setEditValue(row, "LOAD_AREA_ID", record.getAttribute("AREA_ID"));
				table.setEditValue(row, "LOAD_AREA_NAME", record.getAttribute("AREA_NAME"));
				table.setEditValue(row, "LOAD_AREA_ID2", record.getAttribute("AREA_ID2"));
				table.setEditValue(row, "LOAD_AREA_NAME2", record.getAttribute("AREA_NAME2"));
				table.setEditValue(row, "LOAD_AREA_ID3", record.getAttribute("AREA_ID3"));
				table.setEditValue(row, "LOAD_AREA_NAME3", record.getAttribute("AREA_NAME3"));
				table.setEditValue(row, "LOAD_ADDRESS", record.getAttribute("ADDRESS"));
				table.setEditValue(row, "LOAD_CONTACT", record.getAttribute("CONT_NAME"));
				table.setEditValue(row, "LOAD_TEL", record.getAttribute("CONT_TEL"));
				table.setEditValue(row, "LOAD_CODE", record.getAttribute("ADDR_CODE"));
				window.hide();
				window.destroy();
				
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
}
