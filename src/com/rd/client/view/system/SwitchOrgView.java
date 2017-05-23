package com.rd.client.view.system;

import com.rd.client.MainForm;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.system.ChangeWMSDS;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
/**
 * 切换仓库
 * @author lijun
 *
 */
public class SwitchOrgView extends Window {
	 private int width = 510;
     private int height = 340;
     private String top = "25%";
     private String left = "25%";
     private String title = Util.TI18N.CHANGEWMS();
     private Window window;
     private DataSource ds;
     private SGTable table;
     private MainForm form;
     
     public SwitchOrgView(MainForm form){
    	 this.form = form;
     }
     
     public Window getViewPanel(){
    	 ds = ChangeWMSDS.getInstall("V_WAREHOUSE");
    	 table = new SGTable(ds,"100%","70%");
    	 table.setCanEdit(false);
    	 createListField(table);
    	 table.fetchData();
    	 table.setShowFilterEditor(false);
    	 
    	 table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				LoginCache.whseInfo.put(record.getAttribute("ID"), record.getAttribute("WHSE_NAME"));
				
				String whse_id = "当前仓库："+LoginCache.whseInfo.get(record.getAttribute("ID")).toString();
				form.wareLab2.setContents("<font style=\"font-size:12px;font-family:微软雅黑;\">" + whse_id + "</font>");
	
				window.hide();
				
				/*//关闭所有打开的页签
				TabSet tabSet = form.getTabSet();
				Tab[] tabs = tabSet.getTabs();
				if(tabs != null && tabs.length > 1) {
					for(int i = 0; i < tabs.length; i++) {
						if(i > 0) {
							tabSet.removeTab(tabs[i]);
						}
					}
					tabSet.redraw();
				}*/
			}
		});
    	 window = new Window();
    	 window.setTitle(title);
    	 window.setTop(top);
    	 window.setLeft(left);
    	 window.setHeight(height);
    	 window.setWidth(width);
    	 window.setShowCloseButton(true);
    	 window.addItem(table);
    	 window.show();
    	 
    	 
    	 return window;
    	 
     }
     
     public void createListField(SGTable table){
    	 ListGridField ID = new ListGridField("ID",Util.TI18N.ID());
    	 ID.setHidden(true);
    	 ListGridField WHSE_CODE = new ListGridField("WHSE_CODE",Util.TI18N.WHSE_CODE(),60);
    	 ListGridField WHSE_NAME = new ListGridField("WHSE_NAME",Util.TI18N.WHSE_NAME(),100);
    	 ListGridField WHSE_ATTR = new ListGridField("WHSE_ATTR_NAME",Util.TI18N.WHSE_ATTR(),60);
    	 ListGridField WHSE_CLS = new ListGridField("WHSE_CLS_NAME",Util.TI18N.WHSE_CLS(),75);
    	 ListGridField WHSE_TYP = new ListGridField("WHSE_TYP_NAME",Util.TI18N.WHSE_TYP(),75);
    	 ListGridField AREA_NAME = new ListGridField("AREA_NAME",Util.TI18N.AREA_NAME(),80);
    	 
    	 table.setFields(ID,WHSE_CODE,WHSE_NAME,WHSE_ATTR,WHSE_CLS,WHSE_TYP,AREA_NAME);
    	 
    	 
     }
     
     
}