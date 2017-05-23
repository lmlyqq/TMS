package com.rd.client.common.widgets;

import com.rd.client.common.obj.FormUtil;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;

/**
 * 普通单选框
 * @author fanglm
 * @created time 2010-10-14 11:36
 */
public class SGCheck extends CheckboxItem {
	
	private int width= FormUtil.Width;
	/**
	 * 普通单选框
	 * @param colName
	 * @param cName
	 */
	public SGCheck(String colName,String cName){
		super(colName,cName);
		this.setTitleOrientation(TitleOrientation.TOP);
		this.setWidth(width);
		this.setColSpan(2);
	}
	
	/**
	 * 普通单选框
	 * @param colName
	 * @param cName
	 * @param startOrEnd true 换行开始;false 本行结束
	 */
	public SGCheck(String colName,String cName,boolean startOrEnd){
		super(colName,cName);
		this.setTitleOrientation(TitleOrientation.TOP);
		this.setWidth(width);
		this.setColSpan(2);
		this.setStartRow(startOrEnd);
	}
}
