package com.rd.client.common.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.widgets.DateChooser;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;


/**
 * 日期、时间控件弹出窗口
 * @author lijun
 * 
 */
public class DateChooserWin extends VLayout {
    
	private SGTable groupTable;
	private int itemRow;
	private ValuesManager vm;
	private DateDisplayFormat format;
	private String fieldName;
	private SGPanel panel;
	
	/**
	 * Form布局中日期控件
	 * @param vm valuesManager
	 * @param fieldName 控件名称
	 * @param format 时间格式
	 * @param top 顶部位置
	 * @param left 左边位置
	 */
	public DateChooserWin(ValuesManager vm,String fieldName,DateDisplayFormat format,String top,String left){
		this.vm = vm;
		this.fieldName = fieldName;
		this.format = format;
		create(top, left);
	}
	/**
	 * spanel布局中日期控件
	 * @param panel SGPanel
	 * @param fieldName 控件名称
	 * @param format 时间格式
	 * @param top 顶部位置
	 * @param left 左边位置
	 */
	public DateChooserWin(SGPanel panel,String fieldName,DateDisplayFormat format,String top,String left){
		this.panel = panel;
		this.fieldName = fieldName;
		this.format = format;
		create(top,left);
	}
	
	/**
	 * 列表布局中日期控件
	 * @param vm valuesManager
	 * @param fieldName 控件名称
	 * @param format 时间格式
	 * @param top 顶部位置
	 * @param left 左边位置
	 */
	public DateChooserWin(SGTable groupTable,int itemRow,String fieldName,DateDisplayFormat format,String top,String left){
		this.groupTable = groupTable;
		this.itemRow = itemRow;
		this.format = format;
		this.fieldName = fieldName;
		create(top, left);
	}
	
	
	public VLayout create(String top, String left) {
		setShowEdges(true);
		setTop(top);
		setLeft(left); 
		setBackgroundColor("#c3dcfd");
		bringToFront();
		
		Date date=new Date();
		final DateTimeFormat dateFormatter = DateTimeFormat.getFormat("HH:mm");
        String time = dateFormatter.format(date);

        final DateChooser dateChooser = new DateChooser();  
        dateChooser.setWidth(180); 
        dateChooser.setShowCancelButton(false);
        dateChooser.setShowTodayButton(false);
        dateChooser.setCancelButtonTitle("关闭");
        dateChooser.setShowEdges(true);
        dateChooser.setNoDoubleClicks(false);
        //dateChooser.setEdgeSize(1);
        
        final TextItem hour = new TextItem("HOURS");
        hour.setShowTitle(false);
        hour.setWidth(28);
        hour.setHeight(16);
        hour.setAlign(Alignment.CENTER);
        hour.setValue(time.substring(0,2));
        
        StaticTextItem lab = new StaticTextItem();  
        lab.setWrap(false);  
        lab.setShowTitle(false);
        lab.setWidth(4);
        lab.setDefaultValue("<font style=\"font-weight:bold;\">:</font>");
        lab.setAlign(Alignment.CENTER);   
        
        final TextItem minutes = new TextItem("MINUTES");
        minutes.setShowTitle(false);
        minutes.setWidth(28);
        minutes.setHeight(16);
        minutes.setAlign(Alignment.CENTER);
        minutes.setValue(time.substring(3,5));
        
        ButtonItem today = new ButtonItem("BTN_CONFIRM");
        today.setTitle("确定");
        today.setWidth(50);
        today.setHeight(20);
        today.setStartRow(false);
        today.setEndRow(false);
        today.setAlign(Alignment.CENTER);
        
        StaticTextItem lab2 = new StaticTextItem();  
        lab2.setWrap(false);  
        lab2.setShowTitle(false);
        lab2.setWidth(4);
        lab2.setDefaultValue("");
        lab2.setAlign(Alignment.CENTER);
        
        ButtonItem close = new ButtonItem("BTN_CLOSE");
        close.setName("关闭");
        close.setWidth(50); 
        close.setHeight(20);
        close.setAlign(Alignment.CENTER);
        close.setStartRow(false);
        close.setEndRow(false);
        
        final DynamicForm form = new DynamicForm();
        form.setWidth(180);
		form.setPadding(0);
		form.setMargin(0);
		form.setCellPadding(0);
		form.setAlign(Alignment.CENTER);
        form.setItems(hour,lab,minutes,today,lab2,close);
        form.setNumCols(6);
        //form.setBackgroundColor("#c3dcfd");
        form.setShowEdges(false);
        //form.setEdgeSize(3);
        
        addMember(dateChooser); 
        addMember(form);
        
        close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dateChooser.destroy();
				form.destroy();
				getThis().destroy();
			}
        	
        });
        today.addClickHandler(new ClickHandler() {
        	@Override
			public void onClick(ClickEvent event) {
        		DateTimeFormat formate = DateTimeFormat.getFormat("yyyy-MM-dd");
	       		String sysDate = hour.getValue() + ":" + minutes.getValue();
        		String dates = formate.format(dateChooser.getData());
        		
        		if(DateDisplayFormat.TOJAPANSHORTDATE.equals(format)){
	       			 if(vm != null){
	       				 vm.setValue(fieldName, dates);
	       			 }else if(groupTable != null){
	       				 groupTable.setEditValue(itemRow,fieldName, dates);
	       			 } else if(panel != null){
	       				 panel.setValue(fieldName, dates);
	       			 }
	       		} else if(DateDisplayFormat.TOJAPANSHORTDATETIME.equals(format)){
	       			 if(vm != null){
	       				 vm.setValue(fieldName, dates+" "+sysDate);
	       			 }else if(groupTable != null){
	       				 groupTable.setEditValue(itemRow,fieldName, dates+" "+sysDate);
	       			 } else if(panel != null){
	       				 panel.setValue(fieldName, dates+" "+sysDate);
	       			 }      			 
	       		}
        		
				dateChooser.destroy();
				form.destroy();
				getThis().destroy();
			}
        });
        
        /*dateChooser.addDataChangedHandler(new DataChangedHandler() {  
	       	 @Override  
	       	 public void onDataChanged(DataChangedEvent event) { 
	       		 DateTimeFormat formate = DateTimeFormat.getFormat("yyyy-MM-dd");
	       		 String sysDate = getHourMinutes();
	       		 String dates = formate.format(dateChooser.getData());
	       		 if(DateDisplayFormat.TOJAPANSHORTDATE.equals(format)){
	       			 if(vm != null){
	       				 vm.setValue(fieldName, dates);
	       			 }else if(groupTable != null){
	       				 groupTable.setEditValue(itemRow,fieldName, dates);
	       			 } else if(panel != null){
	       				 panel.setValue(fieldName, dates);
	       			 }
	       		 } else if(DateDisplayFormat.TOJAPANSHORTDATETIME.equals(format)){
	       			 if(vm != null){
	       				 vm.setValue(fieldName, dates+" "+sysDate);
	       			 }else if(groupTable != null){
	       				 groupTable.setEditValue(itemRow,fieldName, dates+" "+sysDate);
	       			 } else if(panel != null){
	       				 panel.setValue(fieldName, dates+" "+sysDate);
	       			 }      			 
	       		 }
	       		 hide();
	       	}  
        }); */
        draw();
        return this;
	}
	
	private VLayout getThis() {
		return this;
	}
	
	public static native String getHour() /*-{
	    var now = new Date();
	    var hour=now.getHours();
	    alert(hour);			
		return hour;

	}-*/; 
	
	public static native String getMinutes() /*-{
    	var now = new Date();
	    var minute=now.getMinutes();
		if (minute < 10) {
		    minute = "0" + minute;
		}		
		alert(minute);			
		return minute;
	
	}-*/; 
	
	/*public static native String getHourMinutes() /*-{
		var now = new Date();
		var hour=now.getHours();
		var minute=now.getMinutes();
		if (minute < 10) {
		    minute = "0" + minute;
		}				
		return hour + ':' + minute;

	}-*/; 
}
