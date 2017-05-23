package com.rd.client.common.widgets;

import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;

/**
 * 时间型时间控件
 * @author fanglm
 * @created time 2010-10-14 11:40
 */
public class SGDateTime extends DateTimeItem {
	
	/**
	 * 普通时间控件 yyyy-MM-dd HH:mm
	 * @param colName 对应数据源字段名
	 * @param cName 国际化名称
	 */
	public SGDateTime(String colName,String cName){
		super(colName,cName);
		this.setTitleOrientation(TitleOrientation.TOP);
		this.setWidth(130);
        this.setColSpan(2);
        this.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATETIME);
        this.setInputFormat("DMY");
        this.setTextBoxStyle("<font-size:8px;>");
        //this.setUseMask(true);
	}
	/**
	 * 普通时间控件 yyyy-MM-dd HH:mm
	 * @param colName 对应数据源字段名
	 * @param cName 国际化名称
	 * @param startOrEnd true 换行开始;false 本行结束
	 */
	public SGDateTime(String colName,String cName,boolean startOrEnd){
		super(colName,cName);
		this.setTitleOrientation(TitleOrientation.TOP);
		this.setWidth(130);
        this.setColSpan(2);
        this.setStartRow(startOrEnd);
        this.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATETIME);
        this.setInputFormat("DMY");
        //this.setUseMask(true);
	}
}
