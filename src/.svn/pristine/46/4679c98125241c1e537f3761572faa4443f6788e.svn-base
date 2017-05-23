package com.rd.client.win;

import java.util.HashMap;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.LoadDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class LoadBmsWin extends Window{

	private int width = 500;
	private int height = 450;
	private String top = "38%";
	private String left = "33%";
	private String title = "调度单信息";
	public Window window;
	private DataSource ds;
	private DynamicForm form;
	private HashMap<String,String> mfilter;
	private SGPanel searchForm;

	public LoadBmsWin(DynamicForm form,String top,String left,HashMap<String,String> filter){
		this.form = form;
		this.top = top;
		this.left = left;
		this.mfilter = filter;
	}

	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		//树形结构
		ds = LoadDS.getInstance("V_LOAD_HEADER", "TRANS_LOAD_HEADER");

		
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号",160);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",120);
		ListGridField DRIVER1 = new ListGridField("DRIVER1","司机",120);
		DRIVER1.setHidden(true);
		ListGridField MOBILE1 = new ListGridField("MOBILE1","联系方式",120);
		MOBILE1.setHidden(true);
		ListGridField VEHICLE_TYP_ID = new ListGridField("VEHICLE_TYP_ID","",120);
		VEHICLE_TYP_ID.setHidden(true);
		
		addrList.setFields(LOAD_NO,PLATE_NO,DRIVER1,MOBILE1,VEHICLE_TYP_ID);

        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			  
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				form.setValue("LOAD_NO", record.getAttribute("LOAD_NO"));
				form.setValue("PLATE_NO", record.getAttribute("PLATE_NO"));
				form.setValue("VEHICLE_TYP_ID", record.getAttribute("VEHICLE_TYP_ID"));
				Util.initComboValue(form.getField("SHPM_NO"), "trans_shipment_header", "ODR_NO","ODR_NO"," WHERE load_no ='"+record.getAttribute("LOAD_NO")+"' ","");
				window.hide();
				
			}
		});

        searchForm=new SGPanel();
        
        SGText SUPLR_CODE1=new SGText("LOAD_NO", "调度单号");
        SUPLR_CODE1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList);
				}
			}
		});
        SGText SUPLR_CNAME=new SGText("PLATE_NO", "车牌号");
        SUPLR_CNAME.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList);
				}
			}
		});
        SGText SUPLR_ID=new SGText("SUPLR_ID", "承运商",true);
        SUPLR_ID.setDisabled(true);
        SUPLR_ID.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList);
				}
			}
		});
        if(mfilter != null && mfilter.size() > 0) {
        	SUPLR_ID.setValue(mfilter.get("SUPLR_ID_NAME"));
        }
        
        SGButtonItem searchButton1=new SGButtonItem(StaticRef.FETCH_BTN);
        searchButton1.setStartRow(false);
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
        searchForm.setItems(SUPLR_CODE1,SUPLR_CNAME,SUPLR_ID,searchButton1);
        
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
		criteria.addCriteria("SUPLR_ID",mfilter.get("SUPLR_ID"));
		//criteria.addCriteria("ENABLE_FLAG","Y");
		table.invalidateCache();
		table.fetchData(criteria);  
	}
	
	
}
