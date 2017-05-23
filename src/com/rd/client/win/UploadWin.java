package com.rd.client.win;




import java.util.ArrayList;

import com.rd.client.common.action.UploadAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.view.tms.TmsBarcodeView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 地址点公用弹出窗口
 * @author Administrator
 * 
 */
public class UploadWin extends Window {
	
	private int width = 340;
	private int height = 80;
	private String top = "38%";
	private String left = "40%";
	private String title = "请选择上传数据";
	public Window window;
	private TmsBarcodeView view;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */

	public UploadWin(){
		
	};
	
	public UploadWin(TmsBarcodeView view){
		this.view = view;
	}
	
	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		final DynamicForm uploadForm = new DynamicForm();
		uploadForm.setNumCols(4);
        uploadForm.setAction("uploadServlet");
        uploadForm.setEncoding(Encoding.MULTIPART);
        uploadForm.setMethod(FormMethod.POST);
        uploadForm.setTarget("foo");
	        
		final UploadItem imageItem = new UploadItem("image","路径");
        SGButtonItem saveItem = new SGButtonItem("确定","确定");
        saveItem.setIcon(StaticRef.ICON_CONFIRM);
        saveItem.setWidth(60);
        saveItem.setAutoFit(true);
        saveItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(ObjUtil.isNotNull(imageItem.getValue())){
					String image = imageItem.getValue().toString();
					ArrayList<String> list = new ArrayList<String>();
					Record[] rcords = view.table.getRecords();
					list.add(rcords[0].getAttribute("SHPM_NO"));
					list.add(rcords[0].getAttribute("SKU"));
					list.add(rcords[0].getAttribute("ADDR_CODE"));
					list.add(rcords[0].getAttribute("SHPM_ROW"));
					int sum = 0;
					for(int i=0;i<rcords.length;i++){
						sum = sum + (int)Math.floor(Double.parseDouble(rcords[i].getAttribute("QNTY")));
					}
					list.add(sum+"");
					list.add(LoginCache.getLoginUser().getUSER_ID());
					
					
					new UploadAction(image, uploadForm,list,window).submit();
					
				}else{
					MSGUtil.sayWarning("请选择所要上传的文件");
				}
				
			}
		});
	      
//        uploadForm.setProperty("TEST", "FANGLM");
	        
	    uploadForm.setItems(imageItem,saveItem);
        
        lay.addMember(uploadForm);
		
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(lay);
		window.setShowCloseButton(true);
		window.show();
		
		return window;
	}
}
