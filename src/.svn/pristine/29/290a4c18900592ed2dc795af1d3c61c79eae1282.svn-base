/**
 * 
 */
package com.rd.client.common.widgets;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.widgets.IButton;

/**
 * 按钮的封装类(暂时未使用)
 * @author yuanlei
 *
 */
public class SGIButton extends IButton{
	
	/**
	 * 创建通用按钮
	 * @param btn_type 按钮类型（见StaticRef里配置）
	 */
	public SGIButton(String btn_type) {
		createBtn(btn_type);
	}
	
	public SGIButton(String btn_name, String icon_dir) {
		create(btn_name, icon_dir);
	}
	
    /**
     * 创建界面的按钮
     * @author yuanlei
     * @param btn_type  按钮类型
     * @return
     */
    public void createBtn(String btn_type) {
    	String btn_name = "",icon_dir = "";
    	if(btn_type.equals(StaticRef.CREATE_BTN)) {
    		btn_name = Util.BI18N.NEW();
    		icon_dir = StaticRef.ICON_NEW;
    	}
    	else if(btn_type.equals(StaticRef.SAVE_BTN)) {
    		btn_name = Util.BI18N.SAVE();
    		icon_dir = StaticRef.ICON_SAVE;
    	}
    	else if(btn_type.equals(StaticRef.DELETE_BTN)) {
    		btn_name = Util.BI18N.DELETE();
    		icon_dir = StaticRef.ICON_DEL;
    	}
    	else if(btn_type.equals(StaticRef.CANCEL_BTN)) {
    		btn_name = Util.BI18N.CANCEL();
    		icon_dir = StaticRef.ICON_CANCEL;
    	}
    	else if(btn_type.equals(StaticRef.FETCH_BTN)) {
    		btn_name = Util.BI18N.SEARCH();
    		icon_dir = StaticRef.ICON_SEARCH;
    	}
    	else if(btn_type.equals(StaticRef.CLEAR_BTN)) {
    		btn_name = Util.BI18N.CLEAR();
    		icon_dir = StaticRef.ICON_CLEAR;
    	}
    	else if(btn_type.equals(StaticRef.IMPORT_BTN)) {
    		btn_name = Util.BI18N.IMPORT();
    		icon_dir = StaticRef.ICON_IMPORT;
    	}
    	else if(btn_type.equals(StaticRef.EXPORT_BTN)) {
    		btn_name = Util.BI18N.EXPORT();
    		icon_dir = StaticRef.ICON_EXPORT;
    	}
    	else if(btn_type.equals(StaticRef.PRINT_BTN)) {
    		btn_name = Util.BI18N.PRINT();
    		icon_dir = StaticRef.ICON_PRINT;
    	}
    	else if(btn_type.equals(StaticRef.PRVIEW_BTN)) {
    		btn_name = Util.BI18N.PRINTVIEW();
    		icon_dir = StaticRef.ICON_PVIEW;
    	}
    	else if(btn_type.equals(StaticRef.FREEZE_BTN)){
    		btn_name = Util.BI18N.FREEZE();
    		icon_dir = StaticRef.ICON_SAVE;
    	}
    	else if(btn_type.equals(StaticRef.FREE_BTN)){
    		btn_name = Util.BI18N.FREE();
    		icon_dir= StaticRef.ICON_END;
    		
    	}else if(btn_type.equals(StaticRef.CONFIRM_ORDER_BTN)){
    		btn_name = Util.BI18N.CONFIRMORDER();
    		icon_dir= StaticRef.ICON_SAVE;
    	}else if(btn_type.equals(StaticRef.MANY_ORDER_BTN)){
    		btn_name = Util.BI18N.MANYORDER();
    		icon_dir= StaticRef.ICON_NEW;
    	}else if(btn_type.equals(StaticRef.CANCEL_ORDER_BTN)){
    		btn_name = Util.BI18N.CANCELORDER();
    		icon_dir= StaticRef.ICON_CANCEL;
    	}else if(btn_type.equals(StaticRef.PUT_IMAGE_BTN)){
    		btn_name = Util.BI18N.PUTIMAGE();
    		icon_dir= StaticRef.ICON_END;//
    		
    	}else if(btn_type.equals(StaticRef.PAYOUT_BTN)){
    		btn_name = Util.BI18N.PAYOUT();
    		icon_dir = StaticRef.ICON_CANCEL;
    	}else if(btn_type.equals(StaticRef.CLOSE_BTN)){
    		btn_name = Util.BI18N.CLOSE();
    		icon_dir = StaticRef.ICON_CLEAR;
    	}
    	create(btn_name, icon_dir);
    }
    
    private void create(String btn_name, String icon_dir) {
		this.setTitle(btn_name);
		this.setShowRollOver(true);
		this.setShowDisabled(true);
		this.setShowDownIcon(true);
		this.setAutoFit(true);
		this.setIcon(icon_dir);
    }
}
