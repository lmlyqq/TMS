package com.rd.client.action.tms.shpmreceipt;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class UploadImageAction extends Window{
	private int width = 400;
	private int height = 310;
	public SectionStackSection section;
	public DynamicForm mainSearch;
	private SGPanel form;
	public Window window ;
	public SGTable orderTable;
	private DynamicForm  uploadForm;
//	private TmsOrdManageView view = new TmsOrdManageView();
	private UploadItem imageItem;
	private UploadItem imageItem1;
	private UploadItem imageItem2;
	private UploadItem imageItem3;
	private UploadItem imageItem4;
	private UploadItem imageItem5;
	private UploadItem imageItem6;
	private UploadItem imageItem7;
	private UploadItem imageItem8;
	private String unique_ID;
	private String type;

	public String odrNo;
	
	public UploadImageAction(SGTable orderTable){
		this.orderTable = orderTable;
	}
	public UploadImageAction(SGTable orderTable,String unique_ID){
		this.orderTable = orderTable;
	    this.unique_ID = unique_ID;
	}
	
	public UploadImageAction(SGTable orderTable,String unique_ID,String type){
		this.orderTable = orderTable;
	    this.unique_ID = unique_ID;
	    this.type = type;
	    
	}
	
	
	public Window getViewPanel() {
		    form = new SGPanel();
			form.setHeight(height/2);
			form.setWidth(width-20);
			form.setNumCols(8);
			form.setPadding(5);
			form.setTitleWidth(75);
			form.setAlign(Alignment.CENTER);
			form.setHeight100();
			createForm(form);
			
			VLayout vLay = new VLayout();
			vLay.setWidth100();
			vLay.setBackgroundColor(ColorUtil.BG_COLOR);
			
			uploadForm = new DynamicForm();
			uploadForm.setNumCols(2);
			uploadForm.setAction("uploadServlet");
			uploadForm.setEncoding(Encoding.MULTIPART);
			uploadForm.setMethod(FormMethod.POST);
			uploadForm.setTarget("foo");
			
			HTMLPane htmlPane = new HTMLPane();
			htmlPane.setContentsType(ContentsType.PAGE);
			htmlPane
					.setContents("<iframe name='foo' style='position:absolute;width:0;height:0;border:0'></iframe>");
			htmlPane.setWidth("1");
			htmlPane.setHeight("1");
			
			imageItem = new UploadItem("image", "图片1");
//			TextItem notes = new TextItem("NOTES", "描述");
			imageItem.setWidth(150);
			imageItem.setColSpan(4);
//			imageItem.getTitle();
//			imageItem.setShowPickerIcon(true);
//			imageItem.setShowTitle(false);
//			imageItem.setShowHintInField(true);
//			imageItem.setTextBoxStyle(textBoxStyle)
			imageItem.setTitleOrientation(TitleOrientation.LEFT);
			imageItem.setAlign(Alignment.LEFT);
			imageItem1 = new UploadItem("image", "图片2");
			imageItem1.setWidth(150);
			imageItem1.setColSpan(4);
			imageItem1.setTitleOrientation(TitleOrientation.LEFT);
			imageItem1.setAlign(Alignment.LEFT);
			imageItem2 = new UploadItem("image", "图片3");
			imageItem2.setWidth(150);
			imageItem2.setColSpan(4);
			imageItem2.setTitleOrientation(TitleOrientation.LEFT);
			imageItem2.setAlign(Alignment.LEFT);
			imageItem3 = new UploadItem("image", "图片4");
			imageItem3.setWidth(150);
			imageItem3.setColSpan(4);
			imageItem3.setTitleOrientation(TitleOrientation.LEFT);
			imageItem3.setAlign(Alignment.LEFT);
			imageItem4 = new UploadItem("image", "图片5");
			imageItem4.setWidth(150);
			imageItem4.setColSpan(4);
			imageItem4.setTitleOrientation(TitleOrientation.LEFT);
			imageItem4.setAlign(Alignment.LEFT);
			imageItem5 = new UploadItem("image", "图片6");
			imageItem5.setWidth(150);
			imageItem5.setColSpan(4);
			imageItem5.setTitleOrientation(TitleOrientation.LEFT);
			imageItem5.setAlign(Alignment.LEFT);
			imageItem6 = new UploadItem("image", "图片7");
			imageItem6.setWidth(150);
			imageItem6.setColSpan(4);
			imageItem6.setTitleOrientation(TitleOrientation.LEFT);
			imageItem6.setAlign(Alignment.LEFT);
			imageItem7 = new UploadItem("image", "图片8");
			imageItem7.setWidth(150);
			imageItem7.setColSpan(4);
			imageItem7.setTitleOrientation(TitleOrientation.LEFT);
			imageItem7.setAlign(Alignment.LEFT);
			imageItem8 = new UploadItem("image", "图片9");
			imageItem8.setWidth(150);
			imageItem8.setColSpan(4);
			imageItem8.setTitleOrientation(TitleOrientation.LEFT);
			imageItem8.setAlign(Alignment.LEFT);
			uploadForm.setItems(imageItem,imageItem1,imageItem2,imageItem3,imageItem4,imageItem5,imageItem6,imageItem7,imageItem8);
			uploadForm.setAlign(Alignment.LEFT);
			
			
			VLayout lay1 = new VLayout();
			lay1.setWidth100();
			lay1.setHeight100();
			lay1.setMembers(form);
			lay1.setMembers(uploadForm);

			 window = new Window();
			 window.setTitle("上传");
			 window.setLeft("45%");
			 window.setTop("30%");
			 window.setWidth(width);  
			 window.setHeight(height); 
			 window.setAlign(Alignment.CENTER);
			 window.setCanDragReposition(true);  
			 window.setCanDragResize(true);
			 window.addItem(lay1);
			 window.addItem(mainSearch);
			 return window;
	}
	public void createForm(DynamicForm dynamicForm){
		
		
		
		ButtonItem saveItem = new ButtonItem("save", "上传");
		saveItem.setAutoFit(true);
		saveItem.setIcon(StaticRef.ICON_UPLOAD);
		saveItem.setColSpan(4);
		saveItem.setStartRow(false);
		saveItem.setEndRow(false);
		saveItem.setAutoFit(true);
		saveItem.addClickHandler(new ClickHandler() {
		  @Override
		  public void onClick(ClickEvent event) {
			if(unique_ID != null){
				String str = type+unique_ID;
				if(str.indexOf("#") > 0){
					str = str.replace("#", "%23");
				}
				uploadForm.setAction("uploadServlet?unique_ID="+str);
				if(imageItem.getValue() != null){
					String iffn = imageItem.getValue().toString().toLowerCase();
					if(iffn.endsWith(".jpg") || 
							iffn.endsWith(".jpeg") || 
							iffn.endsWith(".gif") ||
							iffn.endsWith(".png")){
						ArrayList<String> sqlList = new ArrayList<String>();
						if(type.equals(StaticRef.SHPM_RECLIM_URL)){
							StringBuffer sf = new StringBuffer();
							sf.append("update TRANS_SHIPMENT_HEADER set UPLOAD_FLAG = 'Y' where shpm_no = '"+unique_ID+"'");
							sqlList.add(sf.toString());
							StringBuffer sf1 = new StringBuffer();
							sf1.append("update TRANS_ORDER_HEADER set UPLOAD_FLAG = 'Y' where odr_no = (select odr_no from TRANS_SHIPMENT_HEADER where shpm_no = '"+unique_ID+"')");
							sqlList.add(sf1.toString());
						}else if(type.equals(StaticRef.ORDER_RECLIM_URL)){
							StringBuffer sf = new StringBuffer();
							sf.append("update TRANS_ORDER_HEADER set UPLOAD_FLAG = 'Y' where odr_no = '"+unique_ID+"'");
							sqlList.add(sf.toString());
						}
						Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								if(result.equals(StaticRef.SUCCESS_CODE)){
									uploadForm.submitForm();
									MSGUtil.sayInfo("上传成功.");
									uploadForm.destroy();//必需要destory该form，否则会导致重新开启一个新的页面
									window.destroy();
								}
							}
							
							@Override
							public void onFailure(Throwable caught) {
								
							}
						});
							
					}else {
						MSGUtil.sayWarning("只能上传图片.");
					}
				} else {
					
					MSGUtil.sayWarning("请选择图片.");
				}
			} else {
				MSGUtil.sayWarning("请选择影像对应的回单号!");
			}
		  }
	  });
		ButtonItem clearItem = new ButtonItem(Util.BI18N.CLEAR());
		clearItem.setIcon(StaticRef.ICON_CANCEL);
		clearItem.setColSpan(1);
		clearItem.setAutoFit(true);
		clearItem.setStartRow(false);
		clearItem.setEndRow(false);
		clearItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uploadForm.clearValues();
			}
		});
		
		mainSearch = new DynamicForm();
		mainSearch.setCellPadding(5);
		mainSearch.setNumCols(5);
		mainSearch.setItems(saveItem,clearItem);
		mainSearch.setBackgroundColor(ColorUtil.BG_COLOR);
	}
//	public static native boolean compareFile() /*-{
//		var FileMaxSize = 50;//限制上传的文件大小，单位(k)
//		var  Sys = {};
//           
//        if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){   
//            Sys.firefox=true;
//        } 
//		unction checkFileChange(obj) {
//			obj
//            var filesize = 0;
//            if(Sys.firefox){
//                filesize = obj.files[0].fileSize;
//            }
//            alert(filesize);
//        }
//	if(img.fileSize>FileMaxSize){
////	      alert("The file size exceeds "+FileMaxSize+"K，please choose a smaller one!");
////	      document.personRight.imgfile1.focus();
//	      return false;
//	}else{
//		return true;
//	}
//	}-*/;


	
}
