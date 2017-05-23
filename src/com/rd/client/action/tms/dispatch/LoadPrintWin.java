package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 提货单打印窗口
 * @author yuanlei
 *
 */
public class LoadPrintWin extends Window{
	private int width = 1024;
	private int height = 768;
	public LoadPrintWin window = null;
	public StringBuffer msg;
	
	public static interface CloseHandler{
		void onClosed(String message);
	}
	private List<CloseHandler> closeHandlers=new ArrayList<CloseHandler>();
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

	public LoadPrintWin(String p_url, final String json, final SGTable load_table,final SGTable shpm_table, boolean initBtn) {
		//this.url = p_url;      
        final HTMLPane htmlPane = new HTMLPane(); 
        htmlPane.setBackgroundColor ("transparent");
        //htmlPane.setID("print001");
        //htmlPane.setShowEdges(true);  
        //htmlPane.setContentsURL(url);
        htmlPane.setContents("<iframe height='100%' width='100%' scrolling='auto' id='print001' frameborder='0' src=\"./user/wpsadmin/JMLoadmentPrint.pdf\"></iframe>");
        //htmlPane.setContents("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"/><title>无标题文档</title></head><body id =\"print001\"><embed src=\"./user/wpsadmin/JMLoadmentPrint.pdf\" width=\"100%\" height=\"300%\"></embed></body></html>");
        htmlPane.setContentsType(ContentsType.PAGE);
        //htmlPane.setContentsURL("./user/wpsadmin/JMLoadmentPrint.pdf");
        Canvas canvas = new Canvas();
        canvas.setWidth(200);
        canvas.setHeight(30);
        canvas.setTop(0);
        canvas.setLeft(800);
        canvas.setBackgroundColor("#484848");
        if(isFirefox()) {
        	htmlPane.addChild(canvas);
        }           	 
	    
		setTitle("提货单打印");
		setLeft("11%");
		setTop("0");
		setWidth(width);  
		setHeight(height); 
		setCanDragReposition(true);  
		setCanDragResize(true);
		addItem(htmlPane);
		
        HeaderControl print = new HeaderControl(HeaderControl.PRINT, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//System.out.println(htmlPane.getInnerHTML());
				//doPrint(htmlPane.getID());
				Util.async.execProcedure(json, "SP_PRINT_LOG(?,?,?,?,?,?)", new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError(caught.getMessage());
				}

				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
						//printButton.setDisabled(true);
						
		                load_table.getSelectedRecord().setAttribute("PRINT_FLAG", result.substring(2));
						
						if(shpm_table != null && shpm_table.getSelection().length > 0){
							
							ListGridRecord[] records = shpm_table.getSelection();
							
							for(int i = 0 ; i <records.length ; i++){
								Record record = records[i];
								record.setAttribute("PRINT_FLAG", "N");
							}
							
							shpm_table.updateData(shpm_table.getSelectedRecord()); 
						}
						load_table.updateData(load_table.getSelectedRecord());
					}
				}					
			});
			}
        	
        });
        HeaderControl close = new HeaderControl(HeaderControl.CLOSE, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				LoadPrintWin win = getThis();
				win.destroy();
				win.fireCloseEvent("Popup window has been destroyed");
			}
        	
        }); 
		setHeaderControls(HeaderControls.HEADER_LABEL, print, close); 
		
		draw();
	}
	
	public static native boolean doPrint(String id) /*-{
		//alert($wnd.document.getElementById('print001').window);
		//$wnd.document.getElementById('print001').focus();
        //$wnd.document.getElementById('print001').print();
		//alert($wnd.frames["print001"].src);
		alert(id);
		var myfrm = $wnd.frames(id);
		myfrm.focus();
	    //alert($wnd.frames["isc_3M"]);
	    myfrm.print();
        return true; 
	}-*/;
	public static native boolean isFirefox() /*-{
	    if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){  
	        return true;  
    	}
    	else {
    	    return false;
    	} 
    }-*/;
	
	protected LoadPrintWin getThis() {
		return this;
	}
}