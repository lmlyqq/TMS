package com.rd.client.common.action;

import com.rd.client.common.util.Util;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

/**
 * 文本框失去焦点时的验证事件
 * @author fanglm
 *
 */
public class ValidateAction implements BlurHandler{
	//错误提示
	private String erro_msg = "";
	//正则表达式
	private String mater = "";
	//文本框
	private TextItem textItem;
	//邮箱
	public static String EMAIL = "EMAIL";
	//邮政编码
	public static String ZIP = "ZIP";
	//网址
	public static String HTTP_URL = "HTTP_URL";
	
	public ValidateAction(TextItem textItem,String type){
		this.textItem = textItem;
		if(EMAIL.equals(type)){
			erro_msg = Util.MI18N.EMAIL_ERROR();
			mater = "^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$";
		}
		else if(ZIP.equals(type)){
			erro_msg = Util.MI18N.ZIP_ERROR();
			mater = "\\d{6}";
		}
		else if(HTTP_URL.equals(type)){
			erro_msg = Util.MI18N.URL_ERROR();
			mater = "^(http(s)?:\\/\\/)?(www\\.)?[\\w-]+\\.\\w{2,4}(\\/)?";
		}
		RegExpValidator validate = new RegExpValidator();
		validate.setErrorMessage(erro_msg);
		validate.setExpression(mater);
		textItem.setValidators(validate);
	}

	@Override
	public void onBlur(BlurEvent event) {
		textItem.validate();
		
	}
	
}
