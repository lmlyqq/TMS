package com.rd.client.win;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.Cookies;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.CustomerListDS;
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
 * 客户信息二级窗口
 * @author Administrator
 *
 */
public class CustomerWin extends Window{

	private int width = 480;
	private int height = 400;
	private String top = "38%";
	private String left = "33%";
	private String title = "客户信息";
	public Window window;
	private DataSource ds;
	private DynamicForm form;
    private SGPanel searchForm;
    private String name;
    private SectionStack section;
	private DynamicForm pageForm; 
    
    
	public CustomerWin(DynamicForm form,String top,String left){
		this.form = form;
		this.top = top;
		this.left = left;
	}
	
	public CustomerWin(DynamicForm form,String top,String left,String name){
		this.form = form;
		this.top = top;
		this.left = left;
		this.name = name;
	}
	
	public Window getViewPanel() {
		
		VLayout lay = new VLayout();
		
		//树形结构
		ds = CustomerListDS.getInstall("BAS_CUSTOMERList", "BAS_CUSTOMER");

		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		section = new SectionStack();
		
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_CNAME","客户名称",180);
		ListGridField ID = new ListGridField("ID","客户代码",120);
		ID.setHidden(true);
		ListGridField CUSTOMER_CODE = new ListGridField("CUSTOMER_CODE","客户代码",120);
		addrList.setFields(ID,CUSTOMER_CODE,CUSTOMER_NAME);

		SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(addrList);
    	listItem.setExpanded(true);
    	pageForm =new SGPage(addrList, true).initPageBtn();
    	listItem.setControls(pageForm);
    	section.addSection(listItem);
		
        addrList.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			  
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				form.setValue("CUSTOMER_ID", record.getAttribute("ID"));
				form.setValue("CUSTOMER_ID_NAME", record.getAttribute("CUSTOMER_CNAME"));
				form.setValue("CUSTOMER_CNAME", record.getAttribute("CUSTOMER_CNAME"));
				window.hide();
				
			}
		});
        searchForm=new SGPanel();
        
        SGText CUSTOMER_CODE1=new SGText("CUSTOMER_CODE", "客户代码");
        CUSTOMER_CODE1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList);
				}
			}
		});
        
        SGText CUSTOMER_CNAME=new SGText("CUSTOMER_CNAME", "客户名称");
        CUSTOMER_CNAME.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList);
				}
			}
		});
        
        SGText FULL_INDEX=new SGText("FULL_INDEX", "模糊查询",true);
        FULL_INDEX.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList);
				}
			}
		});
        if(name!=null){
        	FULL_INDEX.setValue(name);
        }
        
        SGButtonItem searchButton1=new SGButtonItem(StaticRef.FETCH_BTN);
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
  				criteria.addCriteria("CUSTOMER_FLAG", "Y");
  				final Criteria cri = criteria;
  				addrList.invalidateCache();
  				addrList.fetchData(criteria,new DSCallback() {

  					@SuppressWarnings("unchecked")
  					@Override
  					public void execute(DSResponse response, Object rawData,DSRequest request) {
  						if(pageForm != null) {
  							pageForm.getField("CUR_PAGE").setValue("1");
  							pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
  							pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
  							String sqlwhere = Cookies.getCookie("SQLWHERE");
  							if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
  								addrList.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
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
  		});
        searchForm.setItems(CUSTOMER_CODE1,CUSTOMER_CNAME,FULL_INDEX,searchButton1);
        
        lay.addMember(searchForm);
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
		Criteria criteria =searchForm.getValuesAsCriteria();
		if(criteria == null) {
			criteria = new Criteria();
		}
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("ENABLE_FLAG","Y");
		criteria.addCriteria("CUSTOMER_FLAG","Y");
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
	
}
