package com.rd.client.common.widgets;

import com.rd.client.common.obj.FormUtil;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * 普通文本控件
 * @author fanglm
 * @created time 2010-10-14 11:21
 */
public class SGSText extends TextItem {
	
	private int width = FormUtil.shortWidth;
	/**
	 * 普通文本控件
	 * @param colName 对应数据源字段名
	 * @param cName 国际化名称
	 */
	public SGSText(String colName,String cName){
		super(colName,cName);
		this.setWidth(width);
        this.setColSpan(1);
        this.setTitleOrientation(TitleOrientation.TOP);
	}
	/**
	 * 普通文本控件
	 * @param colName 对应数据源字段名
	 * @param cName 国际化名称
	 * @param startOrEnd true 换行开始;false 本行结束
	 */
	public SGSText(String colName,String cName,boolean startOrEnd){
		super(colName,cName);
		this.setWidth(width);
        this.setColSpan(1);
        this.setStartRow(startOrEnd);
        this.setTitleOrientation(TitleOrientation.TOP);
	}
}
