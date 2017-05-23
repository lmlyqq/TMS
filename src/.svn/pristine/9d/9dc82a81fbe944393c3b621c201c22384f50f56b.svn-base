package com.rd.client.common.widgets;

import com.rd.client.common.obj.FormUtil;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * 普通文本控件
 * 
 * @author fanglm
 * @created time 2010-10-14 11:21
 */
public class SGText extends TextItem {

	private int width = FormUtil.Width;
	/**
	 * 普通文本控件
	 * 
	 * @param colName
	 *            对应数据源字段名
	 * @param cName
	 *            国际化名称
	 */
	public SGText(String colName, String cName) {
		super(colName, cName);
		this.setTitleOrientation(TitleOrientation.TOP);
		this.setWidth(width);
		this.setColSpan(2);
	}

	/**
	 * 普通文本控件
	 * 
	 * @param colName
	 *            对应数据源字段名
	 * @param cName
	 *            国际化名称
	 * @param startOrEnd
	 *            true 换行开始;false 本行结束
	 */
	public SGText(String colName, String cName, boolean startOrEnd) {
		super(colName, cName);
		this.setTitleOrientation(TitleOrientation.TOP);
		this.setWidth(width);
		this.setColSpan(2);
		this.setStartRow(startOrEnd);
	}
}
