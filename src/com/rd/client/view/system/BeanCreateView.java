
package com.rd.client.view.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.util.ObjUtil;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 系统管理-BEAN生成
 * @author fanglm
 *
 */
@ClassForNameAble
public class BeanCreateView extends SGForm implements PanelFactory {
	
	public SGTable table;
	public SGPanel mainForm;
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		//ds=ColCommentDS.getInstance("user_col_comments", "user_col_comments");
		table=new SGTable();
		table.setCanEdit(true);
		table.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		ListGridField field1=new ListGridField("column_name","列名");
		field1.setCanEdit(false);
		ListGridField field2=new ListGridField("comments","注释");
		field2.setCanEdit(false);

		table.setFields(field1,field2);
		final DynamicForm uploadForm = new DynamicForm();
	
		
		final SelectItem tableCom=new SelectItem("tableCom", "数据库表");
		Util.initComboValue(tableCom, "user_tables", "table_name", "table_name");


        final SGButtonItem inputButton = new SGButtonItem("载入","载入");
        inputButton.setIcon(StaticRef.ICON_DOWN);
        inputButton.setWidth(60);
        inputButton.setAutoFit(false);
        inputButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if(tableCom.getValue()!=null){
					Util.db_async.getRecord("column_name,comments", "user_col_comments", "where table_name ='"+tableCom.getValue()+"'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							if(result!=null&&result.size()>0){
							
								ListGridRecord[] records=new ListGridRecord[result.size()];
								
								for(int i=0;i<result.size();i++){
								
									HashMap<String, String> map=result.get(i);
									
									//ListGridRecord record=records[i];
									records[i] = new ListGridRecord();
									
									records[i].setAttribute("column_name",ObjUtil.ifNull(map.get("column_name"), ""));
									
									records[i].setAttribute("comments",ObjUtil.ifNull(map.get("comments"),""));
								}
								table.setData(records);
								table.redraw();
							}else{
								return;
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
				}
			}
        	
        	
        	
        });
     
        
	    uploadForm.setItems(tableCom,inputButton);
	    
	    mainForm=new SGPanel();
	    mainForm.setHeight("40%");
	    mainForm.setWidth("50%");
	    createMainForm();
	    
		VLayout main = new VLayout();
		main.addMember(uploadForm);
		main.addMember(table);
		main.addMember(mainForm);
		main.setWidth100();
		main.setHeight100();
	    
		return main;
	}
	
	public void createMainForm(){
		
		final SGText Extends=new SGText("Extends", "Extends");
		
		final SGText Implements=new SGText("Implements", "Implements");
		
		final SGText ClassName=new SGText("ClassName", " 类名");
		
		final SGLText PackagePath=new SGLText("packagePath", "包路径",true);
		
		final SGLText AbsolutelyPath=new SGLText("absolutelyPath", "绝对路径",true);
		
		
		ButtonItem createButton=new ButtonItem("确认生成");
		createButton.setStartRow(true);
		
		createButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				ListGridRecord []records=table.getSelection();
				
				if(records==null||records.length==0){
					return;
				}
				if(ClassName.getValue()==null){
					return;
				}				
				String className=ClassName.getValue().toString();
				
				if(PackagePath.getValue()==null&&AbsolutelyPath.getValue()==null){
					return;
				}	
				String packagePath=ObjUtil.ifNull(PackagePath.getValue(), "").toString();
				String absolutelyPath=ObjUtil.ifNull(AbsolutelyPath.getValue(), "").toString();
				
				String []fields=new String[records.length];
				for(int i=0;i<records.length;i++){
					fields[i]=records[i].getAttribute("column_name");		
				}
				
				String implementsM=ObjUtil.ifNull(Implements.getValue(), "").toString();
				String extendsM=ObjUtil.ifNull(Extends.getValue(), "").toString();
				Util.async.createBean(className, fields, packagePath, absolutelyPath, implementsM, extendsM,new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result){
						
							SC.say("生成成功");
						}else{
							SC.say("生成失败");
						}
						
						System.out.println(result);
					}

				
				});
			}
		});
		
		
		
		mainForm.setFields(Extends,Implements,ClassName,PackagePath,AbsolutelyPath,createButton);
	}
	

	@Override
	public void createForm(DynamicForm form) {
		
	}
  

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		  
	
	}
	
	
	@Override
	public void onDestroy() {
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		setFUNCID(id);
		BeanCreateView view = new BeanCreateView();
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}


	@Override
	public void initVerify() {
		
	}
	
}
