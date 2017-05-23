package com.rd.client.win;



import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.SkuDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 货品弹出窗口
 * @author Administrator
 * 
 */
public class CustSkuWin extends Window {
	
	private int width = 280;
	private int height = 300;
	private String top = "38%";
	private String left = "40%";
	private String title = "货品";
	public Window window;
	private DataSource ds;
	private ValuesManager vm;
	private TextItem sku_name;
	private TextItem sku_id;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */

	
	
	public CustSkuWin(TextItem sku_name,TextItem sku_id,ValuesManager vm){
		this.sku_name = sku_name;
		this.sku_id = sku_id;
		this.vm = vm;
	}
	
	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		//树形结构
		ds = SkuDS.getInstance("BAS_SKU");
		final TextItem FULL_INDEX = new TextItem("FULL_INDEX",Util.TI18N.FUZZYQRY());
		FULL_INDEX.setShowTitle(false);
		FULL_INDEX.setColSpan(4);
		FULL_INDEX.setWidth(180);
		SGButtonItem searchBtn = new SGButtonItem(StaticRef.FETCH_BTN);
		final SGPanel searchPanel = new SGPanel();
		searchPanel.setTitleWidth(100);
		searchPanel.setItems(FULL_INDEX,searchBtn);
		
		
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		ListGridField ADDR_CODE = new ListGridField("SKU",80);
		ListGridField ADDR_NAME = new ListGridField("SKU_CNAME",140);
		addrList.setFields(ADDR_CODE,ADDR_NAME);
	
        searchBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
					Criteria criteria = new Criteria();
	        		criteria.addCriteria("OP_FLAG","M");
	        		if(ObjUtil.isNotNull(vm.getValueAsString("CUSTOMER_ID"))){
	        			criteria.addCriteria("CUSTOMER_ID",vm.getValueAsString("CUSTOMER_ID"));
	        		}
					if(FULL_INDEX.getValue() != null){
//	        			criteria.addCriteria("FULL_INDEX",FULL_INDEX.getValue().toString());
						criteria.addCriteria(searchPanel.getValuesAsCriteria());
					}
	        		addrList.fetchData(criteria);  
				
			}
		});
        
        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				sku_name.setValue(record.getAttribute("SKU_CNAME"));
				sku_id.setValue(record.getAttribute("ID"));
				window.hide();
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
