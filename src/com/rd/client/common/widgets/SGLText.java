package com.rd.client.common.widgets;

import com.rd.client.common.obj.FormUtil;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * 长型文本控件
 * @author fanglm
 * @create Time 2010-10-14 11:31
 */
public class SGLText extends TextItem {
	
	private int width = FormUtil.longWidth;
	private int colSpan = 4;
	//private int sWidth = 210;
	//private int sColSpan = 4;
	
	/**
	 * 长型文本控件
	 * @param colName 对应数据源字段名
	 * @param cName 国际化名称
	 */
	public SGLText(String colName,String cName){
		super(colName,cName);
		this.setTitleOrientation(TitleOrientation.TOP);
		this.setWidth(width);
		//System.out.println(this.getWidth());
        this.setColSpan(colSpan);
	}
	/**
	 * 长型文本控件
	 * @param colName 对应数据源字段名
	 * @param cName 国际化名称
	 * @param startOrEnd true 换行开始;false 本行结束
	 */
	public SGLText(String colName,String cName,boolean startOrEnd){
		super(colName,cName);
		this.setTitleOrientation(TitleOrientation.TOP);
		this.setWidth(width);
		//System.out.println(this.getWidth());
        this.setColSpan(colSpan);
        this.setStartRow(startOrEnd);
	}
	
	/**
	 * 长型文本控件
	 * @param colName 对应数据源字段名
	 * @param cName 国际化名称
	 */
	/*public SGLText(String colName,String cName,String str){
		super(colName,cName);
		this.setWidth(sWidth);
        this.setColSpan(sColSpan);
	}*/
	/**
	 * 长型文本控件
	 * @param colName 对应数据源字段名
	 * @param cName 国际化名称
	 * @param startOrEnd true 换行开始;false 本行结束
	 */
	/*public SGLText(String colName,String cName,boolean startOrEnd,String str){
		super(colName,cName);
		this.setWidth(sWidth);
        this.setColSpan(sColSpan);
        this.setStartRow(startOrEnd);
	}*/
}
