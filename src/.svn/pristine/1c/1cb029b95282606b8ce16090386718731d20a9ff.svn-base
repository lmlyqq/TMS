package com.rd.client.common.widgets;

import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.widgets.menu.MenuItem;


/**
 * 封装右键控件
 * @author fanglm
 *
 */
public class SGMItem extends MenuItem {
	
	public SGMItem(String title,String keyTitle,String key,String keyName){
		this.setTitle(title);
		this.setKeyTitle(keyTitle);
		KeyIdentifier keyIdentifier = new KeyIdentifier();
		if("ALT".equals(key)){
			keyIdentifier.setAltKey(true);
		}else if("CTRL".equals(key)){
			keyIdentifier.setCtrlKey(true);
		}
		keyIdentifier.setKeyName(keyName);
        this.setKeys(keyIdentifier);
	}
	
	public SGMItem(String title){
		this.setTitle(title);
	}
	/**
	 * Alt+key 的右键快捷键方式
	 * @param title 显示title
	 * @param keyName 键值
	 * @return
	 * @author fanglm
	 */
	public static SGMItem altMenu(String title,String keyName){
		return new SGMItem(title,"Alt+"+keyName,"ALT",keyName);
	}
	
	/**
	 * Ctrl+key 的右键快捷键方式
	 * @param title 显示title
	 * @param keyName 键值
	 * @return
	 * @author fanglm
	 */
	public static SGMItem ctrlMenu(String title,String keyName){
		return new SGMItem(title,"Ctrl+"+keyName,"CTRL",keyName);
	}

}
