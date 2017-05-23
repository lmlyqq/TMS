package com.rd.client.view.system;




import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.AddrDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 行政区域公用弹出窗口
 * @author Administrator
 * 
 */
public class UserAddrWin extends Window {
	
	private int width = 600;
	private int height = 400;
	private String top = "38%";
	private String left = "40%";
	private String title = "经销商";
	public Window window;
	private DataSource addrDS;
	private ListGrid table;
	private StringBuffer ids;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */

	
	
	public UserAddrWin(ListGrid table,String top,String left){
		this.table = table;
		this.top = top;
		this.left = left;
	}
	
	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		RecordList rList = table.getDataAsRecordList(); //已存在数据
		ids = new StringBuffer();
		for(int i=0;i<rList.getLength();i++){
			ids.append("'");
			ids.append(rList.get(i).getAttribute("ADDR_ID"));
			ids.append("',");
		}
		
		//树形结构
		addrDS = AddrDS.getInstance("BAS_ADDRESS");
		final TextItem FULL_INDEX = new TextItem("FULL_INDEX","");
		FULL_INDEX.setShowTitle(false);
		FULL_INDEX.setColSpan(6);
		FULL_INDEX.setWidth(260);
		SGButtonItem searchBtn = new SGButtonItem(StaticRef.FETCH_BTN);
		final SGPanel searchPanel = new SGPanel();
		searchPanel.setItems(FULL_INDEX,searchBtn);
		
		
//		final TreeTable treeGrid = new TreeTable(addrDS, "100%", "90%"); 
		final SGTable listTable = new SGTable(addrDS);
		listTable.setShowFilterEditor(false);
//		listTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		listTable.setShowSelectedStyle(true);
		listTable.setShowResizeBar(false);
		
		ListGridField addr_name = new ListGridField("ADDR_NAME",Util.TI18N.ADDR_NAME(),180);
		ListGridField address = new ListGridField("ADDRESS",Util.TI18N.ADDRESS(),260);
		ListGridField area_id_name  = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME(),80);
        listTable.setFields(addr_name,area_id_name,address);
        
        //确认和取消按钮
        SGPanel btnForm = new SGPanel();
        SGButtonItem confirmBtn = new SGButtonItem(StaticRef.CONFIRM_BTN);
        SGButtonItem cancelBtn = new SGButtonItem(StaticRef.CANCEL_BTN);
        btnForm.setItems(confirmBtn,cancelBtn);
        
        //取消按钮点击事件
        cancelBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				listTable.deselectAllRecords();
				
			}
		});
        searchBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
//				if(FULL_INDEX.getValue() != null){
					Criteria criteria = new Criteria();
	        		criteria.addCriteria("OP_FLAG","M");
//	        		criteria.addCriteria("FULL_INDEX",FULL_INDEX.getValue().toString());
	        		criteria.addCriteria(searchPanel.getValuesAsCriteria());
	        		if(ids.length() > 0){
	        			criteria.addCriteria("AREA_IDS",ids.substring(0,ids.length()-1));
	        		}
	        		listTable.fetchData(criteria);
//				}
				
			}
		});
        //确认按钮点击事件
        confirmBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] records = listTable.getSelection();
				RecordList recordList = new RecordList();
				ListGridRecord rec ;
				RecordList rList = table.getDataAsRecordList(); //已存在数据
				
				for(int j=0;j<rList.getLength();j++){
					recordList.add(rList.get(j));
				}
				
				for(int i=0;i<records.length;i++){
					rec = new ListGridRecord();
					rec.setAttribute("ADDR_NAME", records[i].getAttributeAsString("ADDR_NAME"));
					rec.setAttribute("ADDRESS", records[i].getAttributeAsString("ADDRESS"));
					rec.setAttribute("ADDR_ID", records[i].getAttributeAsString("ID"));
					
					recordList.add(rec);
				}
				table.setData(recordList);
				window.hide();
				window.destroy();
				
			}
		});
        
        lay.addMember(searchPanel);
        lay.addMember(listTable);
        lay.addMember(btnForm);
		
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(lay);
		window.setShowCloseButton(true);
		window.show();
		
		return window;
	}
}
