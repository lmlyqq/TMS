package com.rd.client.action.tms.order;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGText;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 托运单管理--列表右键 -- 汇总功能
 * @author fanglm
 *
 */
public class SetGroupWin extends Window{

	private int width = 285;
	private int height = 100;
	private String top = "38%";
	private String left = "40%";
	private String title = "多家配";
	private ListGrid table;
	public ToolStrip toolStrip;
	private IButton confirmBtn;
	private SGPanel panel;
	
	public Window window;
	
	public SetGroupWin(ListGrid table){
		this.table = table;
	}
	
	public Window getViewPanel() {
		
		VLayout lay = new VLayout();
		
		panel = new SGPanel();
		panel.setNumCols(4);

		
		
		
		final SGText notes = new SGText("NOTES", Util.TI18N.NOTES());
		notes.setWidth(270);
		panel.setItems(notes);
		
		lay.addMember(panel);
		
		confirmBtn = new IButton(Util.BI18N.CONFIRM());
		confirmBtn.setIcon(StaticRef.ICON_CONFIRM);
		confirmBtn.setWidth(60);
		confirmBtn.setAutoFit(true);
		confirmBtn.setAlign(Alignment.RIGHT);
		
		IButton cancelBtn = new IButton(Util.BI18N.CANCEL());
		cancelBtn.setIcon(StaticRef.ICON_CANCEL);
		cancelBtn.setWidth(60);
		cancelBtn.setAutoFit(true);
		cancelBtn.setAlign(Alignment.RIGHT);
		cancelBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(window != null){
					window.minimize();
					window.destroy();
				}
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
        toolStrip.setMembers(confirmBtn,cancelBtn);
        
        init();
        
		
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(lay);
		window.addItem(toolStrip);
		window.setShowCloseButton(true);
		window.show();
		
		window.addMinimizeClickHandler(new MinimizeClickHandler() {
			
			@Override
			public void onMinimizeClick(MinimizeClickEvent event) {
				if(window != null){
					window.minimize();
					window.destroy();
				}
				
			}
		});
		window.addCloseClickHandler(new CloseClickHandler() {

			//for smartg3.0
			@Override
			public void onCloseClick(CloseClientEvent event) {
				if(window != null){
					window.minimize();
					window.destroy();
				}
			}

		});
		
		return window;
		
	}
	
	private void init(){
		confirmBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				window.hide();
				
				final ListGridRecord[]  records = table.getSelection();
				if(records.length == 0){
					return;
				}
				final String note = ObjUtil.ifObjNull(panel.getValue("NOTES"),0).toString();
				final String btch_num = records[0].getAttributeAsString("ODR_NO");
				StringBuffer id = new StringBuffer();
				
				for(int i=0;i<records.length;i++){
					id.append("'");
					id.append(records[i].getAttribute("ODR_NO"));
					id.append("',");
				}
				
				StringBuffer sql = new StringBuffer();
				sql.append("update trans_order_header set notes='");
				sql.append(note);
				sql.append("',BTCH_NUM='");
				sql.append(btch_num);
				sql.append("' where odr_no in(");
				sql.append(id.substring(0, id.length()-1));
				sql.append(")");
				
				Util.async.excuteSQL(sql.toString(), new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							
							for(int i=0;i<records.length;i++){
								records[i].setAttribute("NOTES", note);
								records[i].setAttribute("BTCH_NUM", btch_num);
								table.updateData(records[i]);
							}
							table.redraw();
							
						}else{
							SC.warn(result);
						}
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
				
				
			}
		});
	}
}
