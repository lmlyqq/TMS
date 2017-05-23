package com.rd.client.view.system;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 接口管理--手动处理
 * @author fanglm
 *
 */
public class InterUserCtrolView extends Window {
	 private int width = 280;
     private int height = 400;
     private String top = "25%";
     private String left = "25%";
     private String title = "手动EDI";
     private Window window;
     private SGPanel form;
     private ToolStrip toolStrip;
     
     public Window getViewPanel(){
    	 form = new SGPanel();
    	final IButton runU8Inter = createUDFBtn("洋河接口", StaticRef.ICON_DOWN);
    	final IButton runSGU8Inter = createUDFBtn("双沟接口", StaticRef.ICON_DOWN);
    	runSGU8Inter.setAutoFit(true);
// 		final SGButtonItem runU8Inter = new SGButtonItem("触发U8接口",StaticRef.ICON_CONFIRM);
// 		SGButtonItem clearBtn = new SGButtonItem("清除",StaticRef.ICON_CLEAR);
    	IButton clearBtn = createUDFBtn("清除", StaticRef.ICON_CLEAR);
    	clearBtn.setAutoFit(true);
    	
 		final TextAreaItem log = new TextAreaItem("","");
 		
 		log.setStartRow(true);
 		log.setColSpan(6);
 		log.setHeight(340);
 		log.setWidth(240);
 		log.setDisabled(true);
 		form.setItems(log);

 		runU8Inter.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				runU8Inter.disable();
				runSGU8Inter.enable();
 				log.setValue("正在连接接口服务器，请稍等...\n");
 				Util.async.runTimer("YANGHE",new AsyncCallback<String>() {
 					
 					@Override
 					public void onSuccess(String result) {
 						log.setValue(log.getValue()+result);
 						runU8Inter.enable();
 						runSGU8Inter.enable();
 						
 					}
 					
 					@Override
 					public void onFailure(Throwable caught) {
 						// TODO Auto-generated method stub
 						
 					}
 				});
			}
		});
 		runSGU8Inter.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				runU8Inter.disable();
				runSGU8Inter.enable();
 				log.setValue("正在连接接口服务器，请稍等...\n");
 				Util.async.runTimer("SGJY",new AsyncCallback<String>() {
 					
 					@Override
 					public void onSuccess(String result) {
 						log.setValue(log.getValue()+result);
 						runU8Inter.enable();
 						runSGU8Inter.enable();
 						
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
				log.setValue("");
			}
		});
 		
 		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(3);
		toolStrip.setSeparatorSize(5);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
		toolStrip.setAlign(Alignment.RIGHT);
        toolStrip.setMembers(runU8Inter,runSGU8Inter,clearBtn);
 		
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
     public IButton createUDFBtn(String btn_name, String icon_dir) {
     	IButton button = new IButton(btn_name);
     	button.setShowRollOver(true);
     	button.setShowDisabled(true);
     	button.setShowDownIcon(true);
 		button.setAutoFit(true);
 		button.setIcon(icon_dir);
// 		button.setAccessKey("G");
 		return button;
     }
     
}

