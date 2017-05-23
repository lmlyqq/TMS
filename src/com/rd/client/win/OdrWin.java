package com.rd.client.win;


import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.TranOrderDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
/**
 *
 * @author
 * 
 */
public class OdrWin extends Window{
	
	private int width = 460;
	private int height = 450;
	private String top = "38%";
	private String left = "33%";
	private String title = "托运单信息";
	public Window window;
	private DataSource ds;
	private DynamicForm form;
	private String odr_no;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */
	public OdrWin(DynamicForm form,String top,String left){
		this.form = form;
		this.top = top;
		this.left = left;
	}
	public OdrWin(DynamicForm form,String top,String left,String odr_no){
		this.form = form;
		this.top = top;
		this.left = left;
		this.odr_no=odr_no;
	}

	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		//树形结构
		ds = TranOrderDS.getInstance("V_ORDER_HEADER", "TRANS_ORDER_HEADER");

		
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		ListGridField ODR_NO = new ListGridField("ODR_NO","托运单号",120);
		
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO","客户单号",120);
		
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME","客户名称",120);

		ListGridField CUSTOMER_ID = new ListGridField("CUSTOMER_ID","客户代码",120);
		CUSTOMER_ID.setHidden(true);
		
		ListGridField LOAD_ADDRESS = new ListGridField("LOAD_ADDRESS","发货地址",120);
		LOAD_ADDRESS.setHidden(true);
		
		ListGridField UNLOAD_ADDRESS = new ListGridField("UNLOAD_ADDRESS","收货地址",120);
		UNLOAD_ADDRESS.setHidden(true);
		
		ListGridField PRE_LOAD_TIME = new ListGridField("PRE_LOAD_TIME","发货时间",120);
		PRE_LOAD_TIME.setHidden(true);
		
		ListGridField PRE_UNLOAD_TIME = new ListGridField("PRE_UNLOAD_TIME","到货时间",120);
		PRE_UNLOAD_TIME.setHidden(true);
		
		
		addrList.setFields(ODR_NO,CUSTOM_ODR_NO,CUSTOMER_NAME,CUSTOMER_ID,LOAD_ADDRESS,UNLOAD_ADDRESS);

        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			  
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				form.setValue("ODR_NO", record.getAttribute("ODR_NO"));
				
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
					form.setValue("RECE_TIME", record.getAttribute("PRE_UNLOAD_TIME"));	
				}
				if(form.getField("LOAD_TIME")!=null){
					form.setValue("LOAD_TIME", record.getAttribute("PRE_LOAD_TIME"));	
				}
				window.hide();
				
			}
		});

        final SGPanel form=new SGPanel();
        
        SGText ODR_NO1=new SGText("ODR_NO", "托运单号");
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
        SGText CUSTOMER_NAME1=new SGText("CUSTOMER_NAME", "客户名称",true);
        CUSTOMER_NAME1.addKeyPressHandler(new KeyPressHandler() {
			
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
  				addrList.fetchData(criteria); 
  			
  			}
  		});
        
  
        if(odr_no!=null&&!odr_no.equals("")){
        	
        	ODR_NO1.setValue(odr_no);
        	
        }
        
        
        form.setItems(ODR_NO1,CUSTOM_ODR_NO1,CUSTOMER_NAME1,searchButton1);
        
        lay.addMember(form);
       
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
		
		doFilter(addrList,form);
		
		return window;
	}
	
	private void doFilter(SGTable table,DynamicForm form1) {
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
				
				int i=response.getTotalRows();
				
				RecordList list=response.getDataAsRecordList();
				
				if(list!=null&&!list.isEmpty()){
					
					Record record=list.get(0);
					
					if(i==1){
					 
						form.setValue("ODR_NO", record.getAttribute("ODR_NO"));
						
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
							form.setValue("RECE_TIME", record.getAttribute("PRE_LOAD_TIME"));	
						}
						if(form.getField("LOAD_TIME")!=null){
							form.setValue("LOAD_TIME", record.getAttribute("PRE_UNLOAD_TIME"));	
						}
						
						window.hide();

					}
				}
				
				
			}
		});
	
	}
}