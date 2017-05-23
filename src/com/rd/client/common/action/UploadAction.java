package com.rd.client.common.action;

import java.util.ArrayList;

import com.rd.client.common.obj.LoginCache;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;


public class UploadAction {
	private DynamicForm uploadForm;
	private String path = "user/";
	private String fileName;
	//private ArrayList<String> list; //传值参数/
	//private Window window;
	private String fName;
	public UploadAction(){
		
	}
	public UploadAction(String fileName ,DynamicForm uploadForm,ArrayList<String> list,Window window){
		this.fileName = fileName;
	    this.uploadForm = uploadForm;
	    //this.list = list;
	    //this.window = window;
	}
	
	public void submit() {
		String[] name = fileName.split("/");
		fName = name[name.length-1];
		String str = path + LoginCache.getLoginUser().getUSER_ID();
		
		if(str.indexOf("#") > 0){
			str = str.replace("#", "%23");
		}
		uploadForm.setAction("uploadServlet?unique_ID="+str + "&fileName="+fName +"&type=BARCODE");
		
		uploadForm.submitForm();
		
		/*Util.async.checkQnty(fName, list, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(!result){
					SC.confirm("上传条码数量与实际作业单数量不符，是否确认通过？", new BooleanCallback() {
						public void execute(Boolean value) {
			                if (value != null && value) {
			                	Util.async.upCheck(fName, list, new AsyncCallback<Boolean>() {
									
									@Override
									public void onSuccess(Boolean result) {
										MSGUtil.sayInfo("上传成功.");
										window.setMinimized(false);
										window.hide();
					            		
									}
									
									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										
									}
								});
			                }
			            }
			        });
				}else{
					MSGUtil.sayInfo("上传成功.");
					window.setMinimized(false);
					window.hide();
					uploadForm.clearValues();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});*/
		

		
	}
	
}
