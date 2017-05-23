package com.rd.client.win;

import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGText;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 上传文件公用弹出窗口
 * @author Lang
 * 
 */
public class UploadExcelWin extends Window {
	
	private int width = 540;
	private int height = 140;
	private String top = "38%";
	private String left = "30%";
	private String title = "请选择上传数据";
	public Window window;
	//private IButton refresh;
	private UploadItem pathItem;
	//private String tplName;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */
	
	public UploadExcelWin(){
		this((IButton)null);
	}
	
	public UploadExcelWin(String tplName){
		this((IButton)null, tplName);
	}

	public UploadExcelWin(IButton refresh){
		this(refresh, "tpl.xls");
	};
	
	public UploadExcelWin(IButton refresh, String tplName){
		//this.refresh = refresh;
		//this.tplName = tplName;
	};
	
	public Window getViewPanel() {
		return getViewPanel("ORDER");
	}
	
	/**
	 * 
	 * @author Lang
	 * @param typeName 导入类型
	 * @return
	 */
	public Window getViewPanel(String typeName) {
	
		VLayout lay = new VLayout();
		
		final DynamicForm uploadForm = new DynamicForm();
		uploadForm.setID("DDtable");
		uploadForm.setNumCols(4);
        uploadForm.setAction("excelUploadServlet");
        uploadForm.setEncoding(Encoding.MULTIPART);
        uploadForm.setMethod(FormMethod.POST);
        uploadForm.setTarget("foo");
        uploadForm.setPadding(5);
        HTMLPane htmlPane = new HTMLPane();
		htmlPane.setContentsType(ContentsType.PAGE);
		htmlPane.setContents("<iframe name='foo' id='foo' style='position:absolute;width:0;height:0;border:0' onload='javascript:displayUploadResult(this)'></iframe>");
		htmlPane.setWidth("1");
		htmlPane.setHeight("1");
	
		final TextItem text = new TextItem("uploadTextItem");	//用于接收结果的字段
		text.setWidth(10);
		text.setHeight(10);
		text.setCellStyle("display_none");
		text.setShowTitle(false);

				
		final SGText t=new SGText("text", "文本");	
		t.setDisabled(true);
		
		pathItem = new UploadItem("filePath","路径");
		pathItem.setWidth(320);
		pathItem.setStartRow(true);
		pathItem.setColSpan(2);
		
        SGButtonItem saveItem = new SGButtonItem("确定","确定");
        saveItem.setIcon(StaticRef.ICON_CONFIRM);
        saveItem.setWidth(60);
        saveItem.setAutoFit(false);
        saveItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(ObjUtil.isNotNull(pathItem.getValue())){
					String fileName=pathItem.getValue().toString();
					if(pathItem.getValue().toString().endsWith(".xls") || 
							pathItem.getValue().toString().endsWith(".xlsx")){
						uploadForm.submitForm();
						//CloseClientEvent.fire(window, window.getConfig());
						getExcel(fileName);
						if(text.getValue()!=null){
						String title=text.getValue().toString();
						
						String[] ts=title.split(",");
						
						for(int i=1;i<ts.length+1;i++){
							
							if(!ts[i].endsWith("")){
							 
								ListGridRecord []records=new ListGridRecord[ts.length];
								
								records[i].setAttribute(ts[i], ts[i]);
								
								
							}
						}
						}
					}else{
						MSGUtil.sayWarning("请选择Excel文件");
					}
				}else{
					MSGUtil.sayWarning("请选择所要上传的文件");
				}
				text.setValue("123");
			}
		});

	    uploadForm.setItems(pathItem,text,t);
	    
	    StaticTextItem notes = new StaticTextItem("notes", "说明");
	    notes.setTitleOrientation(TitleOrientation.LEFT);
	    notes.setValue(ColorUtil.getBlueTitle("修改模板后请将所有单元格格式都设为“文本格式”"));
	    notes.setWrap(false);
	    notes.setWrapTitle(false);
	    
	    lay.setWidth100();
	    lay.setHeight100();
        lay.addMember(uploadForm);
        lay.addMember(htmlPane);
        
        TabSet leftTabSet = new TabSet();   
        leftTabSet.setTabBarPosition(Side.TOP);
        leftTabSet.setTabBarAlign(Side.LEFT);
        leftTabSet.setWidth100();
		Tab Tab1 = new Tab("导入");
	    Tab1.setPane(lay);	 

	    leftTabSet.addTab(Tab1); 
        
        ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(4);
		//toolStrip.addSeparator();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.addFormItem(notes);
        toolStrip.addFormItem(saveItem);
       
		
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(leftTabSet);
		window.addItem(toolStrip);
		window.setShowCloseButton(true);
		window.show();
		window.addCloseClickHandler(new CloseClickHandler() {
			
			@Override
			public void onCloseClick(CloseClientEvent event) {
				pathItem.setValue("");
				window.hide();
			}
		});
		
		return window;
	}
	
//	private void showResult(Object result){
//		CloseClientEvent.fire(window, window.getConfig());
//		if(result != null){
//			System.out.println("222"+result);
//		}
//	}
	
	public native static void doDownTpl(String tplName)/*-{
		var url = $wnd.location.href;
		url = url.substring(0, url.lastIndexOf('/'));
		url = url+'/excel/'+tplName;
		$wnd.open(url);
	}-*/;
	
	
	public native static String getExcel(String fileName)/*-{
		//alert(fileName);
		var xmlHttp = new XMLHttpRequest();	
		var title=""; 
		xmlHttp.open("GET","/excelTitleServlet?fileName=" + fileName,true); 
		xmlHttp.onreadystatechange = function(){
			if(xmlHttp.readyState==4){  
			if(xmlHttp.status==200){  
				title=xmlHttp.responseText; 
				var text=$wnd.DDtable.getField("text");
				alert(text);
				if(text._value==title){			
				}else{
					text.setValue(""+title);
				}
				
				return title; 
             }     
          }  
		} 
        xmlHttp.send(null); 
        return title;
}-*/;
	
	public native static void callback()/*-{
		

}-*/;
}
