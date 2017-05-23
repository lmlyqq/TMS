package com.rd.client.win;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.BasVehicleDS;
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
/**
 * 车辆信息二级窗口
 * @author Administrator
 *
 */
public class BasVehicleWin extends Window{

	private int width = 400;
	private int height = 400;
	private String top = "38%";
	private String left = "33%";
	private String title = "车辆信息";
	public Window window;
	private DataSource ds;
	private DynamicForm form;
	private  SGPanel searchForm=new SGPanel();
	 
	public BasVehicleWin(DynamicForm form,String top,String left){
		this.form = form;
		this.top = top;
		this.left = left;
	}
	
	public Window getViewPanel() {
		
		VLayout lay = new VLayout();
		
		//树形结构
		ds = BasVehicleDS.getInstall("V_BAS_VEHICLEds", "BAS_VEHICLE");

		
		
		
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",120);
		ListGridField ID = new ListGridField("ID","ID",120);
		ID.setHidden(true);
		ListGridField VEHICLE_TYP_ID_NAME = new ListGridField("VEHICLE_TYP_ID_NAME","车辆类型",120);
		ListGridField SUPLR_ID_NAME = new ListGridField("SUPLR_ID_NAME","承运商",120);
		ListGridField SUPLR_ID = new ListGridField("SUPLR_ID","承运商ID",120);
		SUPLR_ID.setHidden(true);
		addrList.setFields(ID,PLATE_NO,VEHICLE_TYP_ID_NAME,SUPLR_ID_NAME,SUPLR_ID);

        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			  
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				form.setValue("PLATE_NO", record.getAttribute("PLATE_NO"));
				form.setValue("SUPLR_ID", record.getAttribute("SUPLR_ID"));
				//form.setValue("CUSTOMER_CNAME", record.getAttribute("CUSTOMER_CNAME"));
				window.hide();
				
			}
		});
        
        SGText PLATE_NO1=new SGText("PLATE_NO", "车牌号");
        PLATE_NO1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					System.out.println("11");
					doFilter(addrList);
				}
			}
		});
       // SGText CUSTOMER_CNAME=new SGText("CUSTOMER_CNAME", "客户名称");
        
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
        searchForm.setItems(PLATE_NO1,searchButton1);
        
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
