/**
 * 
 */
package com.rd.client.common.widgets;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.widgets.form.fields.ButtonItem;

/**
 * 按钮的封装类
 * @author yuanlei
 *
 */
public class SGButtonItem extends ButtonItem{
	
	private String btn_type; //按钮类型
	private boolean start = false;//换行开始
	private String btn_name = "";
	
	public SGButtonItem(String btn_type) {
		super();
		this.btn_type = btn_type;
		createBtn();
	}
	public SGButtonItem(String btn_type,boolean start) {
		super();
		this.btn_type = btn_type;
		this.start = start;
		createBtn();
	}
	
	public SGButtonItem(String btn_name,String icon_dir){
		super();
		this.btn_type = "USER_DEFINE";
		this.btn_name = btn_name;
		//this.icon_dir = icon_dir;
		createBtn();
	}
	
	
	public void createBtn(){
		if(btn_type.equals(StaticRef.CREATE_BTN)) {
    		btn_name = Util.BI18N.NEW();
    		//icon_dir = StaticRef.ICON_NEW;
    	}
    	else if(btn_type.equals(StaticRef.SAVE_BTN)) {
    		btn_name = Util.BI18N.SAVE();
    		//icon_dir = StaticRef.ICON_SAVE;
    	}
    	else if(btn_type.equals(StaticRef.DELETE_BTN)) {
    		btn_name = Util.BI18N.DELETE();
    		//icon_dir = StaticRef.ICON_DEL;
    	}
    	else if(btn_type.equals(StaticRef.CANCEL_BTN)) {
    		btn_name = Util.BI18N.CANCEL();
    		//icon_dir = StaticRef.ICON_CANCEL;
    	}
    	else if(btn_type.equals(StaticRef.FETCH_BTN)) {
    		btn_name = Util.BI18N.SEARCH();
    		//icon_dir = StaticRef.ICON_SEARCH;
    	}
    	else if(btn_type.equals(StaticRef.CLEAR_BTN)) {
    		btn_name = Util.BI18N.CLEAR();
    		//icon_dir = StaticRef.ICON_CLEAR;
    	}
    	else if(btn_type.equals(StaticRef.IMPORT_BTN)) {
    		btn_name = Util.BI18N.IMPORT();
    		//icon_dir = StaticRef.ICON_IMPORT;
    	}
    	else if(btn_type.equals(StaticRef.EXPORT_BTN)) {
    		btn_name = Util.BI18N.EXPORT();
    		//icon_dir = StaticRef.ICON_EXPORT;
    	}
    	else if(btn_type.equals(StaticRef.PRINT_BTN)) {
    		btn_name = Util.BI18N.PRINT();
    		//icon_dir = StaticRef.ICON_PRINT;
    	}
    	else if(btn_type.equals(StaticRef.PRVIEW_BTN)) {
    		btn_name = Util.BI18N.PRINTVIEW();
    		//icon_dir = StaticRef.ICON_PVIEW;
    	}
    	else if(btn_type.equals(StaticRef.CONFIRM_BTN)){
    		btn_name = Util.BI18N.CONFIRM();
    		//icon_dir = StaticRef.ICON_SAVE;
    	}
		this.setTitle(btn_name);
		//this.setIcon(icon_dir);
		this.setWidth(80);
		this.setAutoFit(true);
		this.setEndRow(false);
		this.setStartRow(start);
	}
}
