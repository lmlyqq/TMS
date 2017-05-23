package com.rd.client.win;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.Cookies;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.BasStaff1DS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 驾驶员信息弹出窗口
 * @author Administrator
 * 
 */
public class VehicleStaffWin extends Window {
	
	private int width = 800;
	private int height = 480;
	private String top = "38%";
	private String left = "33%";
	private String title = "驾驶员信息";
	public Window window;
	private DataSource ds;
	private ListGrid table;
	private int row;
	private SGPanel searchPanel;
	private String driver;
	private SectionStack section;
	private DynamicForm pageForm; 
	
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */
	public VehicleStaffWin(ListGrid table,int row,String top,String left){
		this.table = table;
		this.row = row;
		this.top = top;
		this.left = left;
	}
	public VehicleStaffWin(ListGrid table,int row,String top,String left,String driver){
		this.table = table;
		this.row = row;
		this.top = top;
		this.left = left;
		this.driver=driver;
	}
	
	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		//树形结构
		ds = BasStaff1DS.getInstall("V_BAS_STAFF1", "BAS_STAFF");
		
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		section = new SectionStack();
		
		ListGridField ID = new ListGridField("ID","ID",90);
		ID.setHidden(true);
		ListGridField STAFF_CODE = new ListGridField("STAFF_CODE","人员编号",90);
		ListGridField STAFF_NAME = new ListGridField("STAFF_NAME","姓名",60);
		ListGridField MOBILE1 = new ListGridField("MOBILE","手机号码",80);
		ListGridField DRVR_LIC_NUM = new ListGridField("DRVR_LIC_NUM","驾驶证号",120);
		ListGridField ID_NO1 = new ListGridField("ID_NO","身份证号",120);
		ListGridField BLACKLIST_FLAG1 = new ListGridField("BLACKLIST_FLAG","黑名单",60);
		BLACKLIST_FLAG1.setType(ListGridFieldType.BOOLEAN);
		
		addrList.setFields(ID,STAFF_CODE,STAFF_NAME,MOBILE1,DRVR_LIC_NUM,ID_NO1,BLACKLIST_FLAG1);
		
//		final SGCombo VEHICLE_STAT = new SGCombo("VEHICLE_STAT",Util.TI18N.VEHICLE_STAT());
//		Util.initCodesComboValue(VEHICLE_STAT, "VECHICLE_STAT");
//		
//		final SGCombo VEHICLE_TYP_ID = new SGCombo("VEHICLE_TYP_ID",Util.TI18N.VEHICLE_TYPE());
//		Util.initComboValue(VEHICLE_TYP_ID, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE  ENABLE_FLAG = 'Y'", "");
//		
//		SGCombo SUPLR_ID = new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());  //供应商
//		Util.initSupplier(SUPLR_ID, "");
		
		SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(addrList);
    	listItem.setExpanded(true);
    	pageForm =new SGPage(addrList, true).initPageBtn();
    	listItem.setControls(pageForm);
    	section.addSection(listItem);
		
		SGText STAFF_CODE1 = new SGText("STAFF_CODE","人员编号");
		STAFF_CODE1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		
		SGText STAFF_NAME1 = new SGText("STAFF_NAME","姓名");
		STAFF_NAME1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		
		SGText MOBILE = new SGText("MOBILE","手机号码");
		MOBILE.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		
		SGText ID_NO = new SGText("ID_NO","身份证号码",true);
		ID_NO.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		
		SGText DRVR_LIC_NUM1 = new SGText("DRVR_LIC_NUM","驾驶证号");
		DRVR_LIC_NUM1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		
	    SGCheck BLACKLIST_FLAG=new SGCheck("BLACKLIST_FLAG", "黑名单");
		BLACKLIST_FLAG.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
		SGText FULL_INDEX = new SGText("FULL_INDEX","模糊查询");
		FULL_INDEX.setValue(driver);
		FULL_INDEX.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter1(addrList);
				}
			}
		});
	
		SGButtonItem searchBtn = new SGButtonItem(StaticRef.FETCH_BTN);
		searchPanel = new SGPanel();
		searchPanel.setNumCols(11);
		searchPanel.setTitleWidth(75);
		searchPanel.setItems(STAFF_CODE1, STAFF_NAME1, MOBILE,FULL_INDEX, ID_NO, DRVR_LIC_NUM1, BLACKLIST_FLAG, searchBtn);
		
		
	
	
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
				ListGridRecord tRecord = table.getRecord(row);
				//table.setEditValue(row, "PLATE_NO", record.getAttribute("PLATE_NO"));
				//table.setEditValue(row, "VEHICLE_TYP_ID", record.getAttribute("VEHICLE_TYP_ID"));
				table.setEditValue(row, "DRIVER1", record.getAttribute("STAFF_NAME"));
				table.setEditValue(row, "MOBILE1", record.getAttribute("MOBILE"));
				table.setEditValue(row, "DRIVER_ID", record.getAttribute("ID"));
				if(tRecord != null){
					//tRecord.setAttribute("PLATE_NO", record.getAttribute("PLATE_NO"));
					//tRecord.setAttribute("VEHICLE_TYP_ID", record.getAttribute("VEHICLE_TYP_ID"));
					tRecord.setAttribute("DRIVER1", record.getAttribute("STAFF_NAME"));
					tRecord.setAttribute("MOBILE1", record.getAttribute("MOBILE"));
					tRecord.setAttribute("DRIVER_ID", record.getAttribute("ID"));
				}
				window.hide();
				
			}
		});
        
        lay.addMember(searchPanel);
        lay.addMember(section);
		
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
	
	private void doFilter(final SGTable table) {
		Criteria criteria = searchPanel.getValuesAsCriteria();
		if(criteria == null) {
			criteria = new Criteria();
		}
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("ENABLE_FLAG","Y");
		final Criteria cri = criteria;
		table.invalidateCache();
		table.fetchData(criteria,new DSCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void execute(DSResponse response, Object rawData,DSRequest request) {
				if(pageForm != null) {
					pageForm.getField("CUR_PAGE").setValue("1");
					pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
					pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
					String sqlwhere = Cookies.getCookie("SQLWHERE");
					if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
						table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
						//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
					}
				}
				LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) cri.getValues();
				if(map.get("criteria") != null) {
					map.remove("criteria");
				}
				if(map.get("_constructor") != null) {
					map.remove("_constructor");
				}
				if(map.get("C_ORG_FLAG") != null) {
					Object obj = map.get("C_ORG_FLAG");
					Boolean c_org_flag = (Boolean)obj;
					map.put("C_ORG_FLAG",c_org_flag.toString());
				}			
			}
		});    
	}
	
	private void doFilter1(final SGTable table) {
		Criteria criteria = searchPanel.getValuesAsCriteria();
		if(criteria == null) {
			criteria = new Criteria();
		}
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("ENABLE_FLAG","Y");
		final Criteria cri = criteria;
		table.invalidateCache();
		table.fetchData(criteria,new DSCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void execute(DSResponse response, Object rawData,DSRequest request) {
				if(pageForm != null) {
					pageForm.getField("CUR_PAGE").setValue("1");
					pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
					pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
					String sqlwhere = Cookies.getCookie("SQLWHERE");
					if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
						table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
					}
				}
				LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) cri.getValues();
				if(map.get("criteria") != null) {
					map.remove("criteria");
				}
				if(map.get("_constructor") != null) {
					map.remove("_constructor");
				}
				if(map.get("C_ORG_FLAG") != null) {
					Object obj = map.get("C_ORG_FLAG");
					Boolean c_org_flag = (Boolean)obj;
					map.put("C_ORG_FLAG",c_org_flag.toString());
				}			
			}
		});    
	}
}