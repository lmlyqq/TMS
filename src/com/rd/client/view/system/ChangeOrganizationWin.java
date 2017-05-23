package com.rd.client.view.system;

import com.rd.client.MainForm;
import com.rd.client.common.ds.TreeListDS;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.TreeTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.tree.TreeGridField;

/**
 * 组织切换
 * @author lijun
 *
 */
public class ChangeOrganizationWin extends Window {
	 private int width = 400;
     private int height = 340;
     private String top = "25%";
     private String left = "40%";
     private String title = Util.TI18N.CHANGEORG();
     private Window window;
     private DataSource ds;
     private MainForm form;
     
     public ChangeOrganizationWin(MainForm form){
    	 this.form = form;
     }
     
     
     public Window getViewPanel(){
    	 ds = TreeListDS.getInstance("V_USER_ORG");
    	 final TreeTable tree = new TreeTable(ds, "100%", "100%");
    	 tree.addDrawHandler(new DrawHandler() {
			
			@Override
			public void onDraw(DrawEvent event) {
				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
				
				tree.fetchData(criteria);
				
				
			}
		});
    	 
    	 tree.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record selectRecord = tree.getSelectedRecord();
				
			    LoginCache.getLoginUser().setDEFAULT_ORG_ID(selectRecord.getAttribute("ORG_ID"));

				LoginCache.getLoginUser().setDEFAULT_ORG_ID_NAME(selectRecord.getAttribute("SHORT_NAME"));

				String org = "当前机构："+LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME().toString();
				form.orglab.setContents("<font style=\"font-size:12px;font-family:微软雅黑;\">" + org + "</font>");
				window.hide();
			}
		});
    	 
    	 TreeGridField field = new TreeGridField("SHORT_NAME");
    	 tree.setFields(field);

    	 window = new Window();
    	 window.setLeft(left);
    	 window.setHeight(height);
    	 window.setTop(top);
    	 window.setWidth(width);
    	 window.setTitle(title);
    	 window.addItem(tree);
    	 window.setShowCloseButton(true);
    	 window.setVisible(true);
    	 window.show();
    	 
    	 return window;
    	 
     }
}
