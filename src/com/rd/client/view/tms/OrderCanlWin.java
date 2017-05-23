package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 托运单管理--订单取消二级窗口
 * @author Administrator
 *
 */
public class OrderCanlWin extends Window{
	private int width = 340;
	private int height = 200;
	DynamicForm form = new DynamicForm();
	
	public static interface CloseHandler{
		void onClosed(String message);
	}
	
	private List<CloseHandler> closeHandlers=new ArrayList<CloseHandler>();
	
	//private ListGrid table;
	
	public void addCloseHandler(CloseHandler handler){
		closeHandlers.add(handler);
	}
	public void removeHandler(CloseHandler handler){
		closeHandlers.remove(handler);
	}
	public void fireCloseEvent(String message){
		for(CloseHandler handler:closeHandlers)
			handler.onClosed(message);
	} 
	
	public OrderCanlWin(final ListGrid table){
		//this.table = table;
		setTitle("订单信息");
		setLeft("40%");
		setTop("25%");
		setWidth(width);  
		setHeight(height); 
        setAlign(Alignment.CENTER);
		setCanDragReposition(true);  
		setCanDragResize(true);
        
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth100();
		toolStrip.setHeight(12);
		toolStrip.setPadding(3);
		toolStrip.setSeparatorSize(12);
		toolStrip.setMembersMargin(3);
		
		IButton confirmButton = new IButton(Util.BI18N.CONFIRM());
		//confirmButton.setIcon(StaticRef.ICON_SAVE);
		confirmButton.setAutoFit(false);
		confirmButton.setWidth(80);
		toolStrip.addMember(confirmButton);
		
		IButton cancelButton = new IButton(Util.BI18N.CANCEL());
		//cancelButton.setIcon(StaticRef.ICON_CANCEL);
		cancelButton.setAutoFit(false);
		cancelButton.setWidth(80);
		toolStrip.addMember(cancelButton);
		
		VStack halfway = new VStack();		
	    createTab1(halfway);

		
	    confirmButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if(!ObjUtil.isNotNull(form.getField("CANCEL_REASON").getValue())) {
					MSGUtil.sayError("请选择取消原因!");
					return;
				}
				ArrayList<String> sqlList = new ArrayList<String>();
				StringBuffer sf = new StringBuffer();
				sf.append("update TRANS_ORDER_HEADER set CANCEL_REASON = ");
				sf.append("'"+form.getField("CANCEL_REASON").getValue()+"',");
				sf.append("CANCEL_NOTES = ");
				sf.append("'"+form.getField("CANCEL_NOTES").getValue()+"',");
				sf.append("STATUS = '90',");
				sf.append("STATUS_NAME = '已关闭'");
				sf.append(" where ODR_NO = ");
				sf.append("'"+table.getSelectedRecord().getAttribute("ODR_NO")+"'");
				sqlList.add(sf.toString());
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						MSGUtil.sayError("订单取消失败");
					}

					@Override
					public void onSuccess(String result) {
						MSGUtil.showOperSuccess();
						table.getSelectedRecord().setAttribute("STATUS_NAME","已关闭");
						table.getSelectedRecord().setAttribute("STATUS","90");
						table.redraw();
						hide();
					}
					
				});
			}
	    	
	    });
	    cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(form != null){
					form.clearValues();
				}
			}
			
		});
	    
	    addItem(halfway);
	    addItem(toolStrip);
	  
		draw();
		
	}
	
	private void createTab1(VStack canvas) {
		
		createComboForm(form);
		canvas.addMember(form);
	}
	
	private void createComboForm(DynamicForm form) {
		form.setHeight(height/2);
		form.setWidth(width-20);
		form.setNumCols(6);
		form.setPadding(5);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
		form.setHeight100();
		
		SGCombo CANCEL_REASON = new SGCombo("CANCEL_REASON", ColorUtil.getRedTitle("取消原因"));
		CANCEL_REASON.setTitleOrientation(TitleOrientation.TOP);
		CANCEL_REASON.setTitleVAlign(VerticalAlignment.TOP);
		Util.initCodesComboValue(CANCEL_REASON, "CLOSE_REASON");
		

//		CANCEL_NOTES.setStartRow(true);
//		CANCEL_NOTES.setColSpan(4);
//		CANCEL_NOTES.setHeight(50);
//		CANCEL_NOTES.setWidth(FormUtil.longWidth);
//		CANCEL_NOTES.setTitleOrientation(TitleOrientation.TOP);
//		CANCEL_REASON.setTitleVAlign(VerticalAlignment.TOP);		
//		CANCEL_NOTES = new TextAreaItem("SPLIT_REASON", "拆分备注");
	
		TextAreaItem CANCEL_NOTES = new TextAreaItem("CANCEL_NOTES","原因描述");
		CANCEL_NOTES.setStartRow(true);
		CANCEL_NOTES.setColSpan(6);
		CANCEL_NOTES.setHeight(50);
		CANCEL_NOTES.setWidth(306);
		CANCEL_NOTES.setTitleOrientation(TitleOrientation.TOP);
		CANCEL_NOTES.setTitleVAlign(VerticalAlignment.TOP);
		
		form.setItems(CANCEL_REASON,CANCEL_NOTES);
	}
	
}