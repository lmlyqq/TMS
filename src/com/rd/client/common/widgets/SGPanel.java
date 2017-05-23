package com.rd.client.common.widgets;

import com.rd.client.common.util.ColorUtil;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;

/**
 * 面板控件
 * @author fanglm
 * @created time 2010-10-14 11:54
 */
public class SGPanel extends DynamicForm {
	
	/**
	 * 面板控件
	 */
	public SGPanel(){
		super();
		this.setBackgroundColor(ColorUtil.BG_COLOR);
		this.setAlign(Alignment.LEFT);
		this.setTitleSuffix("");
		this.setWidth100();
		this.setTitleWidth(100);
		this.setCellPadding(3);
		this.setNumCols(8);
		this.setCanHover(true);
		this.setShowHover(true);
	}
}
