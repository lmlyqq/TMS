
package com.rd.client.view.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.obj.FormUtil;
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
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 系统管理-HBM生成
 * @author fanglm
 *
 */
@ClassForNameAble
public class HbmCreateView extends SGForm implements PanelFactory {
	
	public SGTable table;
	public SGPanel mainForm;
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		table=new SGTable();
		table.setCanEdit(true);
		table.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		ListGridField column_name=new ListGridField("column_name","字段名",120);
		column_name.setCanEdit(false);
		ListGridField comments=new ListGridField("comments","字段标题",120);
		comments.setCanEdit(false);
		ListGridField type=new ListGridField("type","字段类型",120);
		type.setCanEdit(true);
		
		ListGridField INSERT=new ListGridField("INSERT","INSERT",60);
		INSERT.setType(ListGridFieldType.BOOLEAN);
		INSERT.setCanEdit(true);
		
		ListGridField UPDATE=new ListGridField("UPDATE","UPDATE",60);
		UPDATE.setType(ListGridFieldType.BOOLEAN);
		UPDATE.setCanEdit(true);
		
		ListGridField primaryKey=new ListGridField("primaryKey","主键",60);
		primaryKey.setType(ListGridFieldType.BOOLEAN);
		comments.setCanEdit(true);
		
		table.setFields(column_name,comments,type,INSERT,UPDATE,primaryKey);
		final DynamicForm uploadForm = new DynamicForm();
		uploadForm.setWidth("90%");
		uploadForm.setNumCols(10);
		
		final SelectItem tableCom=new SelectItem("tableCom", "数据库表");
		Util.initComboValue(tableCom, "user_tables", "table_name", "table_name");


        final SGButtonItem inputButton = new SGButtonItem("载入","载入");
        inputButton.setIcon(StaticRef.ICON_DOWN);
        inputButton.setWidth(60);
        inputButton.setAutoFit(false);
        inputButton.setStartRow(false);
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
								for(int i=0;i<table.getRecords().length;i++){
									
									table.getRecords()[i].setAttribute("INSERT", true);
									table.getRecords()[i].setAttribute("UPDATE", true);                          
								}						
								
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
	    mainForm.setHeight("90%");
	    mainForm.setWidth("40%");
	    createMainForm();
	    table.setWidth("50%");
	    table.setHeight("90%");
	    
	    HLayout hlay=new HLayout();
	    hlay.addMember(table);
	    hlay.addMember(mainForm);
		VLayout main = new VLayout();
		main.addMember(uploadForm);
		main.addMember(hlay);
		main.setWidth100();
		main.setHeight100();
	    
		return main;
	}
	
	public void createMainForm(){
		
		final SGText ID=new SGText("ID", "ID");
		
		final SGText TABLENAME=new SGText("TABLENAME", "TABLENAME");
		
		final SGLText CLASSNAME=new SGLText("CLASSNAME", "CLASSNAME",true);
		CLASSNAME.setWidth(FormUtil.longWidth);
		CLASSNAME.setColSpan(8);
		
		final SGText PackagePath=new SGText("PackagePath", "包路径",true);
		PackagePath.setColSpan(2);
		
		final SGText FilePath=new SGText("FilePath", "文件路径");
		FilePath.setColSpan(2);

		SGButtonItem createHBMButton=new SGButtonItem("创建HBM文件","创建HBM文件");
		createHBMButton.setStartRow(true);
		createHBMButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				ListGridRecord []records=table.getSelection();
				
				if(records==null||records.length==0){
					SC.say("请选择列表字段");
					return;
				}
//				for(int j=0;j<records.length;j++){
//					if(records[j].getAttribute("type")==null||records[j].getAttribute("type").equals("")){
//						SC.say("列表type为空");
//						return;
//					}
//				}
				if(ID.getValue()==null){
					SC.say("ID字段为空");
					return;
				}
				if(TABLENAME.getValue()==null){
					SC.say("TABLENAME字段为空");
					return;
				}
				if(CLASSNAME.getValue()==null){
					SC.say("CLASSNAME字段为空");
					return;
				}	

				ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
				
				for(int i=0;i<records.length;i++){
					HashMap<String,String> map=new HashMap<String, String>();
					map.put("name", ObjUtil.ifNull(records[i].getAttribute("column_name"),""));
					map.put("title",ObjUtil.ifNull( records[i].getAttribute("comments"),""));
					map.put("type", ObjUtil.ifNull(records[i].getAttribute("type"),""));
					map.put("insert", ObjUtil.ifNull(records[i].getAttribute("INSERT"),""));
					map.put("update",ObjUtil.ifNull( records[i].getAttribute("UPDATE"),""));
					map.put("prk", ObjUtil.ifNull(records[i].getAttribute("primaryKey"),""));
					list.add(map);
					//System.out.println(records[i].getAttribute("primaryKey"));
				}
				
				
				HashMap<String,String> map=new HashMap<String, String>();
				
				map.put("ID", ObjUtil.ifNull(ID.getValue(),"").toString());
				map.put("TABLENAME", ObjUtil.ifNull(TABLENAME.getValue(),"").toString());
				map.put("CLASSNAME", ObjUtil.ifNull(CLASSNAME.getValue(),"").toString());
				map.put("PackagePath", ObjUtil.ifNull(PackagePath.getValue(),"").toString());
				map.put("FilePath", ObjUtil.ifNull(FilePath.getValue(),"").toString());
		
				Util.async.createHBM(list, map,new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						SC.say("出错");
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result){				
							SC.say("创建成功");
						}else{
							SC.say("创建失败");
						}
						
						System.out.println(result);
					}

				
				});
			}
		});
		
		createHBMButton.setColSpan(4);
		
		mainForm.setIsGroup(true);
		mainForm.setGroupTitle("属性设置");
		
		mainForm.setFields(ID,TABLENAME,CLASSNAME,PackagePath,FilePath,createHBMButton);
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
		HbmCreateView view = new HbmCreateView();
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
