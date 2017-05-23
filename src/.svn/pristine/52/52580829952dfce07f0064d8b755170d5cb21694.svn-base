package com.rd.client.win;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.Cookies;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.BasVehSuplierDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
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
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
/**
 * 换车记录车辆信息弹出窗口
 * @author Administrator
 * 
 */
public class SuplrWin extends Window{
	
	private int width = 610;
	private int height = 480;
	private String top = "38%";
	private String left = "33%";
	private String title = "承运商信息";
	public Window window;
	private DataSource ds;
	private DynamicForm form;
	private String sup_name;
	private SectionStack section;
	private DynamicForm pageForm; 
	
	
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */
	public SuplrWin(DynamicForm form,String top,String left){
		this.form = form;
		this.top = top;
		this.left = left;
	}
	public SuplrWin(DynamicForm form,String top,String left,String sup_name){
		this.form = form;
		this.top = top;
		this.left = left;
		this.sup_name=sup_name;
	}
	

	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		//树形结构
		ds = BasVehSuplierDS.getInstall("BAS_Veh_SUPPLIER", "BAS_SUPPLIER");

		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		
		section = new SectionStack();
		
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_CNAME","承运商名称",120);
		ListGridField ID = new ListGridField("ID","承运商ID",120);
		ID.setHidden(true);
		ListGridField SUPLR_CODE = new ListGridField("SUPLR_CODE","承运商代码",120);
		ListGridField SUPLR_TYP1 = new ListGridField("SUPLR_TYP_NAME","承运商类别",120);
		ListGridField BLACKLIST_FLAG1 = new ListGridField("BLACKLIST_FLAG","黑名单",60);
		BLACKLIST_FLAG1.setType(ListGridFieldType.BOOLEAN);
		addrList.setFields(ID,SUPLR_CODE,SUPLR_NAME,SUPLR_TYP1,BLACKLIST_FLAG1);

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
				form.setValue("SUPLR_ID", record.getAttribute("ID"));
				form.setValue("SUPLR_ID_NAME", record.getAttribute("SUPLR_CNAME"));				
				window.hide();
				
			}
		});
        final SGPanel form=new SGPanel();
        
        SGText FULL_INDEX=new SGText("FULL_INDEX", "模糊查询",true);
        FULL_INDEX.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
				}
			}
		});
        if(sup_name!=null){
        	FULL_INDEX.setValue(sup_name);
        }
        
        
        SGText SUPLR_CODE1=new SGText("SUPLR_CODE", "承运商代码");
        //SUPLR_CODE1.setValue(sup_name);
        SUPLR_CODE1.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
				}
			}
		});
        
        SGText SUPLR_CNAME=new SGText("SUPLR_CNAME", "承运商名称");
        SUPLR_CNAME.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
				}
			}
		});
        
        SGCombo SUPLR_TYP=new SGCombo("SUPLR_TYP",Util.TI18N.SUP_SUPLR_TYP());
    	Util.initCodesComboValue(SUPLR_TYP,"SUPLR_TYP");
    	SUPLR_TYP.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getKeyName()!=null&&("Enter").equals(event.getKeyName())){
					doFilter(addrList,form);
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
  			
  				Criteria criteria = form.getValuesAsCriteria();
  				if(criteria == null) {
  					criteria = new Criteria();
  				}
  				criteria.addCriteria("OP_FLAG","M");
  				criteria.addCriteria("ENABLE_FLAG","Y");
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
        form.setItems(SUPLR_CODE1,SUPLR_CNAME,SUPLR_TYP,FULL_INDEX,searchButton1);
        
        lay.addMember(form);
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
		
		doFilter(addrList,form);
		
		return window;
	}
	
	private void doFilter(final SGTable table,SGPanel form) {
		Criteria criteria =  form.getValuesAsCriteria();
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
	
	
}
