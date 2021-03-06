package com.rd.client.win;

import java.util.HashMap;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.HaulingCapacityManagerDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
/**
 * 换车记录车辆信息弹出窗口
 * @author Administrator
 * 
 */
public class ChangeVehicleWin extends Window{
	
	private int width = 800;
	private int height = 480;
	private String top = "38%";
	private String left = "33%";
	private String title = "车辆信息";
	public Window window;
	private DataSource ds;
	private HashMap<String,String> mfilter;
	private SGPanel searchPanel;
	private ValuesManager form;
	private String plate_no;
	private String suplr_id;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */
	public ChangeVehicleWin(ValuesManager form,String top,String left){
		this.form = form;
		this.top = top;
		this.left = left;
	}
	
	public ChangeVehicleWin(ValuesManager form,String top,String left,String plate_no,String suplr_id){
		this.form = form;
		this.top = top;
		this.left = left;
		this.plate_no=plate_no;
		this.suplr_id=suplr_id;
	}
	
	public ChangeVehicleWin(ValuesManager form,String top,String left,HashMap<String,String> filter){
		this.form = form;
		this.top = top;
		this.left = left;
		this.mfilter = filter;
	}
	
	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		//树形结构
		ds = HaulingCapacityManagerDS.getInstall("V_BAS_VEHICLE", "BAS_VEHICLE");
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		ListGridField PLATE_NO1 = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),70);
		ListGridField VEHICLE_STAT1 = new ListGridField("VEHICLE_STAT_NAME",Util.TI18N.VEHICLE_STAT(),60);
		ListGridField CURRENT_AREA = new ListGridField("CURRENT_AREA","当前区域",80);
		ListGridField TMP_ATTR1 = new ListGridField("TMP_ATTR_NAME",Util.TI18N.TMP_ATTR(),80);
		ListGridField AVAIL_FLAG = new ListGridField("AVAIL_FLAG_NAME",Util.TI18N.AVAIL_ATTR(),70);
		ListGridField VEHICLE_TYP_ID_NAME = new ListGridField("VEHICLE_TYP_ID_NAME",Util.TI18N.VEHICLE_TYP(),90);
		ListGridField MAX_WEIGHT1 = new ListGridField("MAX_WEIGHT1",Util.TI18N.MAX_WEIGHT(),70);
		ListGridField REMAIN_GROSS_W1 = new ListGridField("REMAIN_GROSS_W","余量",70);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_ID_NAME",Util.TI18N.SUPLR_NAME(),80);
		ListGridField SUPLR_ID1 = new ListGridField("SUPLR_ID",Util.TI18N.SUPLR_NAME(),80);
		SUPLR_ID1.setHidden(true);
		addrList.setFields(PLATE_NO1,VEHICLE_STAT1,REMAIN_GROSS_W1,TMP_ATTR1, CURRENT_AREA, AVAIL_FLAG, VEHICLE_TYP_ID_NAME,MAX_WEIGHT1, SUPLR_NAME,SUPLR_ID1);
	
		final SGCombo VEHICLE_STAT = new SGCombo("VEHICLE_STAT",Util.TI18N.VEHICLE_STAT());
		Util.initCodesComboValue(VEHICLE_STAT, "VECHICLE_STAT");
		VEHICLE_STAT.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		
		
		final SGCombo VEHICLE_TYP_ID = new SGCombo("VEHICLE_TYP_ID",Util.TI18N.VEHICLE_TYPE());
		Util.initComboValue(VEHICLE_TYP_ID, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE  ENABLE_FLAG = 'Y'", "");
		VEHICLE_TYP_ID.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		SGCombo SUPLR_ID = new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());  //供应商
		Util.initSupplier(SUPLR_ID, "");
		SUPLR_ID.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		final SGText CUR_LOCATION = new SGText("CURRENT_AREA","当前区域");
		CUR_LOCATION.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		SGCombo TMP_ATTR = new SGCombo("TMP_ATTR", Util.TI18N.TMP_ATTR(), true);
		Util.initCodesComboValue(TMP_ATTR, "TMP_ATTR");
		TMP_ATTR.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		SGCombo AVAIL_ATTR = new SGCombo("AVAIL_ATTR", Util.TI18N.AVAIL_ATTR());
		AVAIL_ATTR.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		SGText MAX_WEIGHT = new SGText("MAX_WEIGHT","载重量大于");
		MAX_WEIGHT.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		SGText PLATE_NO= new SGText("PLATE_NO",Util.TI18N.PLATE_NO());
		PLATE_NO.setValue(plate_no);
		PLATE_NO.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		if(mfilter != null && mfilter.size() > 0) {
			Util.initCodesComboValue(TMP_ATTR, "TMP_ATTR", mfilter.get("TMP_ATTR"));
			Util.initCodesComboValue(AVAIL_ATTR, "AVAIL_ATTR",mfilter.get("AVAIL_FLAG"));
			MAX_WEIGHT.setValue(mfilter.get("REMAIN_GROSS_W"));
			Util.initComboValue(SUPLR_ID,"BAS_SUPPLIER","ID","SHORT_NAME",""," SHOW_SEQ ASC",mfilter.get("SUPLR_ID"));
		}else{
			Util.initComboValue(SUPLR_ID,"BAS_SUPPLIER","ID","SHORT_NAME",""," SHOW_SEQ ASC",suplr_id);
		}
		
		SGButtonItem searchBtn = new SGButtonItem(StaticRef.FETCH_BTN);
		searchPanel = new SGPanel();
		searchPanel.setNumCols(11);
		searchPanel.setTitleWidth(75);
		searchPanel.setItems(VEHICLE_STAT, VEHICLE_TYP_ID, SUPLR_ID, CUR_LOCATION, TMP_ATTR, AVAIL_ATTR, MAX_WEIGHT, PLATE_NO, searchBtn);
		
		
		
        searchBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				doFilter1(addrList);
			}
		});
        
        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				form.setValue("PLATE_NO2", record.getAttribute("PLATE_NO"));
				form.setValue("VEHICLE_TYP_ID2", record.getAttribute("VEHICLE_TYP_ID"));
				form.setValue("DRIVER2", record.getAttribute("DRIVER1_NAME"));
				form.setValue("MOBILE2", record.getAttribute("MOBILE1"));
				form.setValue("SUPLR_ID2", record.getAttribute("SUPLR_ID"));
				form.setValue("SUPLR_ID_NAME", record.getAttribute("SUPLR_ID_NAME"));
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
		
		doFilter(addrList);
		
		return window;
	}
	
	private void doFilter(SGTable table) {
		Criteria criteria = searchPanel.getValuesAsCriteria();
		if(criteria == null) {
			criteria = new Criteria();
		}
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("ENABLE_FLAG","Y");
		if(mfilter!=null){
			criteria.addCriteria("TMP_ATTR",mfilter.get("TMP_ATTR"));
			criteria.addCriteria("AVAIL_ATTR",mfilter.get("AVAIL_FLAG"));
			criteria.addCriteria("MAX_WEIGHT",mfilter.get("REMAIN_GROSS_W"));
			criteria.addCriteria("SUPLR_ID",mfilter.get("SUPLR_ID"));
		}
		
		table.invalidateCache();
		table.fetchData(criteria);  
	}
	
	private void doFilter1(SGTable table) {
		Criteria criteria = searchPanel.getValuesAsCriteria();
		if(criteria == null) {
			criteria = new Criteria();
		}
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("ENABLE_FLAG","Y");
		table.invalidateCache();
		table.fetchData(criteria);  
	}
	
}
