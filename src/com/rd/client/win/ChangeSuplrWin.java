package com.rd.client.win;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.TmsSupplierDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class ChangeSuplrWin extends Window{
	
	private int width = 610;
	private int height = 480;
	private String top = "38%";
	private String left = "33%";
	private String title = "承运商信息";
	public Window window;
	private DataSource ds;
	private DynamicForm form;
	private SGPanel searchForm;
	private String sup_name;
	
	public ChangeSuplrWin(DynamicForm form,String top,String left){
		this.form = form;
		this.top = top;
		this.left = left;
	}
	
	public ChangeSuplrWin(DynamicForm form,String top,String left,String sup_name){
		this.form = form;
		this.top = top;
		this.left = left;
		this.sup_name=sup_name;
	}
	
	public Window getViewPanel() {
		
		VLayout lay = new VLayout();
		
		//树形结构
		ds = TmsSupplierDS.getInstall("BAS_SUPPLIER", "BAS_SUPPLIER");
		
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_CNAME","承运商名称",120);
		ListGridField ID = new ListGridField("ID","承运商ID",120);
		ID.setHidden(true);
		ListGridField SUPLR_CODE = new ListGridField("SUPLR_CODE","承运商代码",120);
		ListGridField SUPLR_TYP1 = new ListGridField("SUPLR_TYP_NAME","承运商类别",120);
		ListGridField BLACKLIST_FLAG1 = new ListGridField("BLACKLIST_FLAG","黑名单",60);
		BLACKLIST_FLAG1.setType(ListGridFieldType.BOOLEAN);
		addrList.setFields(ID,SUPLR_CODE,SUPLR_NAME,SUPLR_TYP1,BLACKLIST_FLAG1);

        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			  
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				form.setValue("SUPLR_ID2", record.getAttribute("ID"));
				form.setValue("SUPLR_ID_NAME", record.getAttribute("SUPLR_CNAME"));
				
				window.hide();
				
			}
		});
        searchForm=new SGPanel();
        
        SGText FULL_INDEX=new SGText("FULL_INDEX", "模糊查询",true);
        FULL_INDEX.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList);
				}
			}
		});
        if(sup_name!=null){
        	FULL_INDEX.setValue(sup_name);
        }
        
        SGText SUPLR_CODE1=new SGText("SUPLR_CODE", "承运商代码");
        SUPLR_CODE1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList);
				}
			}
		});
        SGText SUPLR_CNAME=new SGText("SUPLR_CNAME", "承运商名称");
        SUPLR_CNAME.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList);
				}
			}
		});
        SGCombo SUPLR_TYP=new SGCombo("SUPLR_TYP",Util.TI18N.SUP_SUPLR_TYP());
    	Util.initCodesComboValue(SUPLR_TYP,"SUPLR_TYP");
    	SUPLR_TYP.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList);
				}
			}
		});
        SGButtonItem searchButton1=new SGButtonItem(StaticRef.FETCH_BTN);
        searchButton1.setStartRow(true);
        searchButton1.addClickHandler(
  		    new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
  			
  			@Override
  			public void onClick(
  					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
  			
  				Criteria criteria = searchForm.getValuesAsCriteria();
  				if(criteria == null) {
  					criteria = new Criteria();
  				}
  				criteria.addCriteria("OP_FLAG","M");
  				criteria.addCriteria("ENABLE_FLAG","Y");
  				addrList.invalidateCache();
  				addrList.fetchData(criteria); 
  			
  			}
  		});
        
        searchForm.setNumCols(11);
        searchForm.setTitleWidth(75);
        searchForm.setItems(SUPLR_CODE1,SUPLR_CNAME,SUPLR_TYP,FULL_INDEX,searchButton1);
        
        lay.addMember(searchForm);
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
		
		doFilter(addrList);
		
		return window;
	}
	
	private void doFilter(SGTable table) {
		Criteria criteria = searchForm.getValuesAsCriteria();
		if(criteria == null) {
			criteria = new Criteria();
		}
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("ENABLE_FLAG","Y");
		table.invalidateCache();
		table.fetchData(criteria);  
	}
}
