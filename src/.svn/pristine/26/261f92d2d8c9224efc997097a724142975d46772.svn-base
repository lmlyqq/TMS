package com.rd.client.view.tms;

import com.rd.client.PanelFactory;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.reflection.ClassForNameAble;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

@ClassForNameAble
public class GPSManagerView extends SGForm implements PanelFactory {

	/*public GPSManagerView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		
		  VLayout layout = new VLayout();  
	        layout.setWidth(1300);  
	        layout.setHeight100();  
	  
	        final HTMLPane htmlPane = new HTMLPane();  
	        htmlPane.setShowEdges(true);  
	        htmlPane.setContentsURL("http://www.513gs.com/ologin.html?key=jyVRTDQzaNtKch4PCvgf5jxfafl2Nd2xOpuyh");  
	        htmlPane.setContentsType(ContentsType.PAGE);  
	  
	        HStack hStack = new HStack();  
	        hStack.setHeight(5);  
	        hStack.setLayoutMargin(10);  
	        hStack.setMembersMargin(10);  
	  
//	        IButton yahooButton = new IButton("上海紫华");  
//	        yahooButton.addClickHandler(new ClickHandler() {  
//	            public void onClick(ClickEvent event) {  
//	                htmlPane.setContentsURL("http://www.513gs.com/ologin.html?key=jyVRTDQzaNtKch4PCvgf5jxfafl2Nd2xOpuyh");  
//	            }  
//	        });  
//	        hStack.addMember(yahooButton);  
	  
//	        IButton googleButton = new IButton("中国电信");  
//	        googleButton.addClickHandler(new ClickHandler() {  
//	            public void onClick(ClickEvent event) {  
//	                htmlPane.setContentsURL("http://202.102.112.25/login.aspx?company=sqyhwl&user=sqyhwl&psw=sqyhwl");  
//	            }  
//	        });  
//	        hStack.addMember(googleButton);  
	  
	        IButton wikipediaButton = new IButton("Wikipedia");  
	        wikipediaButton.addClickHandler(new ClickHandler() {  
	            public void onClick(ClickEvent event) {  
	                htmlPane.setContentsURL("http://www.wikipedia.org/");  
	            }  
	        });  
//	        hStack.addMember(wikipediaButton);  
	  
	        IButton baiduButton = new IButton("Baidu");  
	        baiduButton.addClickHandler(new ClickHandler() {  
	            public void onClick(ClickEvent event) {  
	                htmlPane.setContentsURL("http://www.baidu.com/");  
	            }  
	        });  
//	        hStack.addMember(baiduButton);  
	        
	        htmlPane.setContentsURL("http://202.102.112.25/login.aspx?company=sqyhwl&user=sqyhwl&psw=sqyhwl");  
	  
//	        layout.addMember(hStack);  
	        layout.addMember(htmlPane);  
	        layout.draw();  
		
		
		
		return layout;
	}
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}
	
	@Override
	public void initVerify() {
		
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		GPSManagerView view = new GPSManagerView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}

}
