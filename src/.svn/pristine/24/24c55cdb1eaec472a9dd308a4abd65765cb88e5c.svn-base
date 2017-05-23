package com.rd.client.view.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.ImportInfoDS;
import com.rd.client.reflection.ClassForNameAble;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 系统管理-导入文件
 * @author fanglm
 *
 */
@ClassForNameAble
public class ReportConfigView extends SGForm implements PanelFactory {
	
	public SGTable table;
	private DataSource ds;
	 public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ds=ImportInfoDS.getInstance("SYS_IMPORT_CONFIG", "SYS_IMPORT_CONFIG");
		table=new SGTable(ds, "100%", "90%", false, true, false);
		table.setCanEdit(true);
		ListGridField field1=new ListGridField("EXCEL_NAME","EXCEL列名");
		field1.setCanEdit(false);
		ListGridField field2=new ListGridField("FIELD_NAME","表字段名");
		field2.setCanEdit(true);
		ListGridField field3=new ListGridField("SHOW_SEQ","SHOW_SEQ");
		field3.setHidden(true);
		table.setFields(field1,field2,field3);
		
		final DynamicForm uploadForm = new DynamicForm();
		uploadForm.setWidth("100%");
		uploadForm.setNumCols(16);
        uploadForm.setAction("excelUploadServlet");
        uploadForm.setEncoding(Encoding.MULTIPART);
        uploadForm.setMethod(FormMethod.POST);
        uploadForm.setTarget("foo");
        //uploadForm.setPadding(5);
        HTMLPane htmlPane = new HTMLPane();
		htmlPane.setContentsType(ContentsType.PAGE);
		htmlPane.setContents("<iframe name='foo' id='foo' style='position:absolute;width:0;height:0;border:0' onload='javascript:displayUploadResult(this)'></iframe>");
		htmlPane.setWidth("1");
		htmlPane.setHeight("1");
		
		final UploadItem pathItem = new UploadItem("filePath","路径");
		pathItem.setWidth(250);
		pathItem.setStartRow(true);
		pathItem.setColSpan(2);
		
		final SelectItem tableCom=new SelectItem("tableCom", "数据库表");
		tableCom.setColSpan(2);
		//tableCom.setWidth(120);
		Util.initComboValue(tableCom, "user_tables t", "table_name", "table_name",""," order by t.table_name asc");
		tableCom.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if(tableCom.getValue()!=null){
					final ListGridRecord[] records=table.getRecords();
					if(records!=null&&records.length>0){
						for(int i=0;i<records.length;i++){							
							records[i].setAttribute("FIELD_NAME", "");							
						}
						table.redraw();
						Util.db_async.getSingleRecord("column_name", "user_tab_cols",  " where table_name='"+tableCom.getValue()+"'", null, new AsyncCallback<HashMap<String,String>>() {
							
							@Override
							public void onSuccess(HashMap<String, String> result) {
								Util.initComboValue(table.getField("FIELD_NAME"), "user_tab_cols u", "column_name", "column_name", " where table_name='"+tableCom.getValue()+"'","order by u.column_name");
								table.redraw();
							}
							
							@Override
							public void onFailure(Throwable caught) {
								
								SC.say("下拉框初始化失败！");
							}
						});
					}					
				}				
			}
		});
		
		
        final SGButtonItem saveItem = new SGButtonItem("确定","确定");
        saveItem.setIcon(StaticRef.ICON_CONFIRM);
        saveItem.setWidth(60);
        saveItem.setColSpan(1);
        saveItem.setAutoFit(false);
        saveItem.setStartRow(false);
        saveItem.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {
				if(ObjUtil.isNotNull(pathItem.getValue())){
					String fileName=pathItem.getValue().toString();
					if(pathItem.getValue().toString().endsWith(".xls") || 
							pathItem.getValue().toString().endsWith(".xlsx")){						
						uploadForm.submitForm();
						System.out.println("client:" + fileName);
						Util.async.readExcel(fileName, new AsyncCallback<ArrayList<String>>() {

							@Override
							public void onFailure(Throwable caught) {
								
							}

							@Override
							public void onSuccess(ArrayList<String> result) {
								System.out.println("start");
								
								ListGridRecord []records=new ListGridRecord[result.size()];
								
								if(result != null) {
									for(int i=0; i < result.size(); i++) {
										System.out.println(result.get(i));
										
										ListGridRecord listRecord=new ListGridRecord();
										
										listRecord.setAttribute("EXCEL_NAME", result.get(i));	
										
										listRecord.setAttribute("FIELD_NAME"," ");
										
										records[i]=listRecord;
									}
									table.invalidateCache();							
									table.setRecords(records);
									table.redraw();
								}
								System.out.println("end");
							}
							
						});
						
					}else{
						MSGUtil.sayWarning("请选择Excel文件");
					}
				}else{
					MSGUtil.sayWarning("请选择所要上传的文件");
				}
				//text.setValue("123,456,789");
			}
		});

        final SGButtonItem saveButton = new SGButtonItem("保存","保存");
        saveButton.setIcon(StaticRef.ICON_SAVE);
        saveButton.setWidth(60);
        saveButton.setColSpan(1);
        saveButton.setAutoFit(false);
        saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if(tableCom.getValue()!=null&&!tableCom.getValue().equals("")){
				
				ListGridRecord [] records = table.getRecords();
				
				int [] rows = table.getAllEditRows();
				
				String str="";
				
				for(int i=0;i<rows.length;i++){
					
					str=str+rows[i];
				}
				
				if(records!=null&&records.length>0){
					
					List<String> titleList=new ArrayList<String>();
					
					List<String> colList=new ArrayList<String>();
					
					List<String> titleList1=new ArrayList<String>();
					
					//List<String> colList1=new ArrayList<String>();
					
					for(int i=0;i<records.length;i++){
						
						ListGridRecord record=records[i];
						
					    if(record.getAttribute("EXCEL_NAME")!=null&&!record.getAttribute("EXCEL_NAME").equals("")){
			
					    	titleList.add(record.getAttribute("EXCEL_NAME"));
					    
					    }
					    
					    if(str.indexOf(Integer.toString(i))>=0){
					    	
					    	Record rec=table.getEditedRecord(i);
					    	
					    	if(rec.getAttribute("FIELD_NAME")!=null&&!rec.getAttribute("FIELD_NAME").equals(" ")&&!rec.getAttribute("FIELD_NAME").equals("")){
					    	
					    		if(!colList.contains(rec.getAttribute("FIELD_NAME"))){
					    		
					    			colList.add(rec.getAttribute("FIELD_NAME"));
					    			
					    			
					    		}else{
					    			
					    			MSGUtil.sayError("第"+Integer.toString(i+1)+"行重复");
					    			return;
					    		}
					    	
					    	}else{
					    		
					    		colList.add("");
					    	
					    	}
					    }else{
					    	
					    	if(record.getAttribute("FIELD_NAME")!=null&&!record.getAttribute("FIELD_NAME").equals(" ")&&!record.getAttribute("FIELD_NAME").equals("")){
					    		
					    		if(!colList.contains(record.getAttribute("FIELD_NAME"))){
					    		
					    			colList.add(record.getAttribute("FIELD_NAME"));
					    			
					    		}else{
					    			
					    			//SC.say();
					    			MSGUtil.sayError("第"+Integer.toString(i+1)+"行重复");
					    			return;
					    		}
					    	}else{
					    		
					    		colList.add("");
					    	
					    	}
					    	
					    	
					    }
					   
					}
					
					
					
					HashSet<String> tSet = new HashSet<String>(titleList);   
					 
					titleList1.addAll(tSet);   
					
					if((colList.size()==titleList.size())){
   												
							ArrayList<String> sqlList = new ArrayList<String>();
							
							String sql1="delete from SYS_IMPORT_CONFIG where TABLE_NAME='"+tableCom.getValue()+"'";
							
							sqlList.add(sql1);
							
							for(int i=0;i<titleList.size();i++){
								
								String sql="insert into SYS_IMPORT_CONFIG (EXCEL_NAME,FIELD_NAME,TABLE_NAME,SHOW_SEQ)values('"+titleList.get(i)+"','"+colList.get(i)+"','"+tableCom.getValue()+"',"+(i+1)+")";
								
								sqlList.add(sql);
							}
							
							
							
							
							
							for(int i=0;i<rows.length;i++){
								
								Record record=table.getEditedRecord(i);
								
								//colList1.add(record.getAttribute("FIELD_NAME"));
								
								if(record.getAttribute("SHOW_SEQ")!=null){
								
									String sql="update SYS_IMPORT_CONFIG set FIELD_NAME='"+record.getAttribute("FIELD_NAME")+"' where TABLE_NAME='"+tableCom.getValue()+"' and SHOW_SEQ="+record.getAttribute("SHOW_SEQ")+" ";
								
									sqlList.add(sql);
								
								}else{
									String sql="update SYS_IMPORT_CONFIG set SHOW_SEQ="+(i+1)+",FIELD_NAME='"+record.getAttribute("FIELD_NAME")+"' where TABLE_NAME='"+tableCom.getValue()+"' and EXCEL_NAME='"+record.getAttribute("EXCEL_NAME")+"' ";
									sqlList.add(sql);
								}
								 
							
						}
							
							Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									
									
								}

								@Override
								public void onSuccess(String result) {
									if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
										MSGUtil.showOperSuccess();
									}else{
										MSGUtil.sayError(result.substring(2));
									}
								}	
							});
							
					}else{
						
						SC.say("请确认数据是否填写正确！");
						
					}
					
					
				}
		
			}else{
				SC.say("表名不能为空！");
			}
				}

        });
        final SGButtonItem inputButton = new SGButtonItem("载入","载入");
        inputButton.setIcon(StaticRef.ICON_DOWN);
        inputButton.setWidth(60);
        inputButton.setColSpan(1);
        inputButton.setAutoFit(false);
        inputButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if(tableCom.getValue()!=null){
					Util.initComboValue(table.getField("FIELD_NAME"), "user_tab_cols", "column_name", "column_name", " where table_name='"+tableCom.getValue()+"'","");
					
					table.invalidateCache();
					
					int []rows=table.getAllEditRows();
					for(int i=0;i<rows.length;i++){
						
						table.clearEditValue(rows[i], "EXCEL_NAME");
						table.clearEditValue(rows[i], "FIELD_NAME");
						
					}
					Criteria criteria=new Criteria();
					criteria.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
				    criteria.addCriteria("TABLE_NAME",tableCom.getValue().toString());
				    table.fetchData(criteria);
				    
				  
				    
				  //  table.redraw();
				    
				}
			}
        	
        	
        	
        });
        final SGButtonItem deleteButton = new SGButtonItem("删除","删除");
        deleteButton.setIcon(StaticRef.ICON_DEL);
        deleteButton.setWidth(60);
        deleteButton.setColSpan(1);
        deleteButton.setAutoFit(false);
        deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(tableCom.getValue()!=null){
				
				SC.confirm("是否删除指定记录", new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value != null && value) {
							ArrayList<String> sqlList = new ArrayList<String>();
							String sql="delete from SYS_IMPORT_CONFIG where TABLE_NAME='"+tableCom.getValue()+"'";
							sqlList.add(sql);
							Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
								
									MSGUtil.sayError("处理出错");
								}

								@Override
								public void onSuccess(String result) {
									if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
										MSGUtil.showOperSuccess();
										//table.clearCriteria();
										table.invalidateCache();
										Criteria criteria=new Criteria();
										criteria.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
									    criteria.addCriteria("TABLE_NAME",tableCom.getValue().toString());
									    table.fetchData(criteria);
//									    table.redraw();									
									}else{
										MSGUtil.sayError(result.substring(2));
									}									
								}														
							});						
						}					
					}	
				});
				}else{
					
					SC.say("请选择要删除的表");
					
				}
			
			}
        });
        
	    uploadForm.setItems(pathItem,saveItem,tableCom,inputButton,saveButton,deleteButton);
	    
		VLayout main = new VLayout();
		//main.addMember(toolStrip);
		main.addMember(uploadForm);
		main.addMember(htmlPane);
		main.addMember(table);
		main.setWidth100();
		main.setHeight100();
	    
		return main;
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
		ReportConfigView view = new ReportConfigView();
		view.setFUNCID(id);
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
