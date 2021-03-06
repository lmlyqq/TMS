package com.rd.client.win;


import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.LoadDS;
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
 * 换车记录车辆信息弹出窗口
 * @author Administrator
 * 
 */
public class LoadNewWin extends Window{
	
	private int width = 630;
	private int height = 450;
	private String top = "38%";
	private String left = "33%";
	private String title = "调度单信息";
	public Window window;
	private DataSource ds;
	private DynamicForm form;
	private String load_no;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */
	public LoadNewWin(DynamicForm form,String top,String left){
		this.form = form;
		this.top = top;
		this.left = left;
	}
	public LoadNewWin(DynamicForm form,String top,String left,String load_no){
		this.form = form;
		this.top = top;
		this.left = left;
		this.load_no=load_no;
	}

	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		//树形结构
		ds = LoadDS.getInstance("V_LOAD_HEADER", "TRANS_LOAD_HEADER");

		
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号",180);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",150);
		ListGridField DRIVER1 = new ListGridField("DRIVER1","驾驶员",120);
		//DRIVER1.setHidden(true);
		ListGridField MOBILE1 = new ListGridField("MOBILE1","联系方式",120);
		//MOBILE1.setHidden(true);
		
		addrList.setFields(LOAD_NO,PLATE_NO,DRIVER1,MOBILE1);

        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			  
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				form.setValue("LOAD_NO", record.getAttribute("LOAD_NO"));

				if(form.getField("PLATE_NO")!=null){
					if(form.getField("PLATE_NO").getValue()==null||form.getField("PLATE_NO").getValue().equals("")){
					
						form.setValue("PLATE_NO", record.getAttribute("PLATE_NO"));
					
					}
				}
				if(form.getField("DRIVER")!=null){
					form.setValue("DRIVER", record.getAttribute("DRIVER1"));
				}
				if(form.getField("MOBILE")!=null){
					form.setValue("MOBILE", record.getAttribute("MOBILE1"));
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
        SGText PLATE_NO1=new SGText("PLATE_NO", "车牌号");
        LOAD_NO1.addKeyPressHandler(new KeyPressHandler() {
			
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
        if(load_no!=null&&!load_no.equals("")){
        	
        	LOAD_NO1.setValue(load_no);
        	
        }
        
        form.setItems(LOAD_NO1,PLATE_NO1,searchButton1);
        
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
					 
						form.setValue("LOAD_NO", record.getAttribute("LOAD_NO"));

						if(form.getField("PLATE_NO")!=null){
							if(form.getField("PLATE_NO").getValue()==null||form.getField("PLATE_NO").getValue().equals("")){
							
								form.setValue("PLATE_NO", record.getAttribute("PLATE_NO"));
							
							}
						}
						if(form.getField("DRIVER")!=null){
							form.setValue("DRIVER", record.getAttribute("DRIVER1"));
						}
						if(form.getField("MOBILE")!=null){
							form.setValue("MOBILE", record.getAttribute("MOBILE1"));
						}
						
						window.hide();

					}
				}
				
				
			}
		});
	}
}
