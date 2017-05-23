package com.rd.client.win;


import com.google.gwt.user.client.Cookies;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.OdrComplaintDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
/**
 *
 * @author
 * 
 */
public class NewOdrWin extends Window{
	
	private int width = 900;
	private int height = 450;
	private String top = "19%";
	private String left = "23%";
	private String title = "托运单信息";
	public Window window;
	private DataSource ds;
	private DynamicForm form;
	private SectionStack list_section;
	private DynamicForm pageForm;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */
	public NewOdrWin(DynamicForm form,String top,String left){
		this.form = form;
		this.top = top;
		this.left = left;
	}
	public Window getViewPanel() {
	
		VLayout lay = new VLayout();

		ds = OdrComplaintDS.getInstance("V_ODR_COMPLAINT", "V_ODR_COMPLAINT");

		
		
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		list_section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
	    listItem.setItems(addrList);
	    listItem.setExpanded(true);
	    pageForm=new SGPage(addrList, true).initPageBtn();
	    listItem.setControls(pageForm);
	    list_section.addSection(listItem);
	    list_section.setWidth("100%");
		
		
		ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号",90);
		
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME","承运商",80);
		
		ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",60);

		ListGridField DRIVER = new ListGridField("DRIVER","司机",60);
		
		ListGridField MOBILE = new ListGridField("MOBILE","电话",70);
		
		ListGridField SHPM_NO = new ListGridField("SHPM_NO","作业单号",110);

		ListGridField ODR_NO = new ListGridField("ODR_NO","托运单号",90);
		
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO","客户单号",60);
		
		ListGridField LOAD_DATE = new ListGridField("LOAD_DATE","发货日期",70);
		
		ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE","到货日期",70);
		
		ListGridField VEHICLE_TYP_ID_NAME = new ListGridField("VEHICLE_TYP_ID_NAME","车型",60);
		
		addrList.setFields(LOAD_NO,SUPLR_NAME,VEHICLE_TYP_ID_NAME,PLATE_NO,DRIVER,MOBILE,SHPM_NO,ODR_NO,CUSTOM_ODR_NO,LOAD_DATE,UNLOAD_DATE);

        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			  
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				if(form.getField("ODR_NO")!=null){
				form.setValue("ODR_NO", record.getAttribute("ODR_NO"));
				}
				if(form.getField("CUSTOMER_ID")!=null){
				form.setValue("CUSTOMER_ID", record.getAttribute("CUSTOMER_ID"));
				}
				if(form.getField("CUSTOMER_NAME")!=null){
				form.setValue("CUSTOMER_NAME", record.getAttribute("CUSTOMER_NAME"));
				}
				if(form.getField("LOAD_ADDRESS")!=null){
				form.setValue("LOAD_ADDRESS", record.getAttribute("LOAD_ADDRESS"));
				}
				if(form.getField("UNLOAD_ADDRESS")!=null){
				form.setValue("UNLOAD_ADDRESS", record.getAttribute("UNLOAD_ADDRESS"));
				}
				if(form.getField("RECE_TIME")!=null){
					form.setValue("RECE_TIME", record.getAttribute("UNLOAD_DATE"));	
				}
				if(form.getField("LOAD_TIME")!=null){
					form.setValue("LOAD_TIME", record.getAttribute("LOAD_DATE"));	
				}			
				if(form.getField("LOAD_NO")!=null){
					form.setValue("LOAD_NO", record.getAttribute("LOAD_NO"));
				}
					
				if(form.getField("PLATE_NO")!=null){
					form.setValue("PLATE_NO", record.getAttribute("PLATE_NO"));
				}
					
				if(form.getField("DRIVER")!=null){
					form.setValue("DRIVER", record.getAttribute("DRIVER"));	
				}
					
				if(form.getField("MOBILE")!=null){
					form.setValue("MOBILE", record.getAttribute("MOBILE"));	
				}
				window.hide();
				
			}
		});

        final SGPanel form=new SGPanel();

        SGText LOAD_NO1=new SGText("LOAD_NO", "调度单号");
        LOAD_NO1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
				}
			}
		});
    	SGCombo SUPLR_ID =new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());//承运商
		Util.initSupplier(SUPLR_ID, "");
		SUPLR_ID.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
				}
			}
		});
        SGText PLATE_NO1=new SGText("PLATE_NO", "车牌号");
		PLATE_NO1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
				}
			}
		});
        final SGCombo VEHICLE_TYP_ID = new SGCombo("VEHICLE_TYP_ID",Util.TI18N.VEHICLE_TYPE());
		Util.initComboValue(VEHICLE_TYP_ID, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE  ENABLE_FLAG = 'Y'", "");
		VEHICLE_TYP_ID.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
				}
			}
		});
        SGText ODR_NO1=new SGText("ODR_NO", "托运单号",true);
		ODR_NO1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
				}
			}
		});
        SGText CUSTOM_ODR_NO1=new SGText("CUSTOM_ODR_NO", "客户单号");
		CUSTOM_ODR_NO1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
				}
			}
		});
        SGText DRIVER1=new SGText("DRIVER", "司机");
		DRIVER1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
				}
			}
		});
        SGText MOBILE1=new SGText("MOBILE", "电话");
		MOBILE1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
				}
			}
		});
        SGButtonItem searchButton1=new SGButtonItem(StaticRef.FETCH_BTN);
        searchButton1.setStartRow(false);
        searchButton1.addClickHandler(
  		    new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
  			
  			@Override
  			public void onClick(
  					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
  			
  				Criteria criteria = form.getValuesAsCriteria();
  				if(criteria == null) {
  					criteria = new Criteria();
  				}
  				criteria.addCriteria("OP_FLAG","M");
  				criteria.addCriteria("ENABLE_FLAG","Y");
  				addrList.invalidateCache();
  				addrList.setFilterEditorCriteria(criteria);
  				addrList.fetchData(criteria, new DSCallback() {

  					@Override
  					public void execute(DSResponse response, Object rawData,
  							DSRequest request) {
  						if(rawData.toString().indexOf("HTTP code: 0") > 0 || rawData.toString().indexOf("404") >= 0) {
  							MSGUtil.sayError("服务器连接已中断，请重新登录!");
  						}
  						if(pageForm != null) {
  							pageForm.getField("CUR_PAGE").setValue("1");
  							pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
  							pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
  							String sqlwhere = Cookies.getCookie("SQLWHERE");
  							String key = Cookies.getCookie("SQLFIELD1");
  							String value = Cookies.getCookie("SQLFIELD2");
  							String alias = Cookies.getCookie("SQLALIAS");
  							if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
  								addrList.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
  								addrList.setProperty("SQLFIELD1", key);
  								addrList.setProperty("SQLFIELD2", value);
  								addrList.setProperty("SQLALIAS", alias);
  								//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
  							}
  						}		
  					}
  					
  				});
  			
  			}
  		});
        
  
//        if(this.form.getValue("ODR_NO")!=null&&!(this.form.getValue("ODR_NO")).equals("")){   	
//        	ODR_NO1.setValue(this.form.getValue("ODR_NO").toString());       	
//        }      
        
        form.setItems(LOAD_NO1,SUPLR_ID,PLATE_NO1,VEHICLE_TYP_ID,ODR_NO1,CUSTOM_ODR_NO1,DRIVER1,MOBILE1,searchButton1);        
        form.setWidth("60%");
        
        lay.addMember(form);      
        lay.addMember(list_section);
		
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(lay);
		window.setShowCloseButton(true);
		window.show();
		
		//doFilter(addrList,form);
		
		return window;
	}
	
	private void doFilter(final SGTable table,DynamicForm form1) {
		table.invalidateCache();
		Criteria criteria = form1.getValuesAsCriteria();
		if(criteria == null) {
			criteria = new Criteria();
		}
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("ENABLE_FLAG","Y");
		
		table.fetchData(criteria,new DSCallback(){

			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				
				if(rawData.toString().indexOf("HTTP code: 0") > 0 || rawData.toString().indexOf("404") >= 0) {
						MSGUtil.sayError("服务器连接已中断，请重新登录!");
					
				}
					
				if(pageForm != null) {		
					pageForm.getField("CUR_PAGE").setValue("1");
					pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
					pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
				}	
			}
		});	
	}
}
