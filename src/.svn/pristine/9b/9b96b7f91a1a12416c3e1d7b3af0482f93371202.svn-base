package com.rd.client.common.widgets;

import com.rd.client.common.obj.FormUtil;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;

/**
 * 日期型时间控件
 * @author fanglm
 * @created time 2010-10-14 11:40
 */
public class SGDate extends DateTimeItem {
	private int width = FormUtil.Width;
	/**
	 * 日期型时间控件 yyyy-MM-dd
	 * @param colName 对应数据源字段名
	 * @param cName 国际化名称
	 */
	public SGDate(String colName,String cName){
		super(colName,cName);
		this.setTitleOrientation(TitleOrientation.TOP);
		this.setWidth(width);
        this.setColSpan(2);
        this.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
        this.setInputFormat("DMY");
	}
	/**
	 * 日期型时间控件 yyyy-MM-dd
	 * @param colName 对应数据源字段名
	 * @param cName 国际化名称
	 * @param startOrEnd true 换行开始;false 本行结束
	 */
	public SGDate(String colName,String cName,boolean startOrEnd){
		super(colName,cName);
		this.setTitleOrientation(TitleOrientation.TOP);
		this.setWidth(width);
        this.setColSpan(2);
        this.setStartRow(startOrEnd);
        this.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
        this.setInputFormat("DMY");
	}
}
