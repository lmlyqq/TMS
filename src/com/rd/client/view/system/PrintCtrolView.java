package com.rd.client.view.system;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGText;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 *  系统管理--打印授权
 * @author fanglm
 *
 */
public class PrintCtrolView extends Window {
	 private int width = 250;
     private int height = 150;
     private String top = "25%";
     private String left = "25%";
     private String title = "打印授权";
     private Window window;
     private SGPanel form;
     private ToolStrip toolStrip;
     
     public Window getViewPanel(){
    	 form = new SGPanel();
    	final SGText load_no = new SGText("LOAD_NO","调度号");
    	final IButton runU8Inter = new IButton("授权打印");
    	IButton clearBtn = new IButton("清除");
 
 		form.setItems(load_no);

 		runU8Inter.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				StringBuffer sql = new StringBuffer();
				sql.append("update trans_load_header set print_flag='Y' where load_no='");
				sql.append(load_no.getValue());
				sql.append("'");
				
				Util.async.excuteSQL(sql.toString(), new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						MSGUtil.showOperSuccess();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
 		
 		clearBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				load_no.setValue("");
			}
		});
 		
 		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(20);
		toolStrip.setAlign(Alignment.RIGHT);
        toolStrip.setMembers(runU8Inter,clearBtn);
 		
		 window = new Window();
		 window.setTitle(title);
		 window.setTop(top);
		 window.setLeft(left);
		 window.setHeight(height);
		 window.setWidth(width);
		 window.setShowCloseButton(true);
		 window.addItem(toolStrip);
		 window.addItem(form);
		 window.show();
		 return window;
    	 
     }
     
     
}

