package com.rd.client.common.action;

import com.rd.client.common.util.MSGUtil;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;


public class ImageUploadAction implements ClickHandler {
	private String unique_id;
	private String type;
	private String  getValue;
	private DynamicForm uploadForm;
	
	public ImageUploadAction(){
		
	}
	public ImageUploadAction(String unique_id , String type , String getValue ,DynamicForm uploadForm){
	     this.unique_id = unique_id;
	     this.type = type;
	     this.getValue = getValue;
	     this.uploadForm = uploadForm;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		if(unique_id != null){
			String str = type+unique_id;
			
			if(str.indexOf("#") > 0){
				str = str.replace("#", "%23");
			}
			uploadForm.setAction("uploadServlet?unique_ID="+str);
			if(getValue != null){
				//int  dot = getValue.toString().indexOf(".");
				//if(".jpg".equals(getValue.substring(dot,getValue.length()))||".jpeg".equals(getValue.substring(dot,getValue.length()))
				//		||".JPG".equals(getValue.substring(dot,getValue.length()))||".JPEG".equals(getValue.substring(dot,getValue.length()))){
						uploadForm.submitForm();
						MSGUtil.sayInfo("上传成功.");
//						uploadForm.destroy();//必需要destory该form，否则会导致重新开启一个新的页面
						uploadForm.clearValues();
				/*}else {
					MSGUtil.sayWarning("只能上传图片.");
				}*/
			} else {
				
				MSGUtil.sayWarning("请选择图片.");
			}
			
		} else {
			MSGUtil.sayWarning("请选择上传图片对应的货品编号.");
		}

	}
	

}
