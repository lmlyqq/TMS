package com.rd.client.view.system;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.FileManageWin;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TextAreaWrap;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
@ClassForNameAble
public class SQLExecuteView extends SGForm implements PanelFactory {
	
	private SGPanel inputForm;
	private SGPanel resultForm;
	public IButton submitButton;
	public static final String RESULT_MSG_KEY = "#resultMsg#".intern();
	public static final String ERROR_LINE_KEY = "#errorLine#".intern();
	public static final String E_KEY = "#e#".intern();
	public static final String COLUMNS_KEY = "#columns#".intern();
	public static final String VALUES_KEY = "#values#".intern();
	private IButton fileButton;

	/*public SQLExecuteView(String id) {
		super(id);
	}*/

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		
	}

	@Override
	public void createForm(DynamicForm form) {

	}

	@Override
	public Canvas getViewPanel() {
		Double width = (((Page.getWidth()-60) * 0.8));
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth100();
		
		VLayout orderItem = new VLayout(); 
		orderItem.setWidth100();
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth(width.intValue());
		toolStrip.setHeight(30);
		
		fileButton = new IButton("文件管理");
		fileButton.setVisible(false);
		fileButton.setWidth(120);
		fileButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new FileManageWin();
			}
		});
		
		IButton dinfoButton = new IButton("列出数据库连接信息");
		dinfoButton.setWidth(120);
		dinfoButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				execute("select database_infos");
			}
		});
		
		IButton viewButton = new IButton("列出所有视图");
		viewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				execute(getQuerySQL("VIEW"));
			}
		});
		IButton proButton = new IButton("列出所有存储");
		proButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				execute(getQuerySQL("PROCEDURE"));
			}
		});
		IButton funButton = new IButton("列出所有函数");
		funButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				execute(getQuerySQL("FUNCTION"));
			}
		});
		IButton tabButton = new IButton("列出所有表");
		tabButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				execute(getQuerySQL("TABLE"));
			}
		});
		
		submitButton = new IButton("提交");
		submitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final Object value = inputForm.getField("inputText").getValue();
				if(!ObjUtil.isNotNull(value)){
					return;
				}
				execute(value.toString());
			}
		});
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(fileButton, dinfoButton, viewButton, proButton, funButton, tabButton, submitButton);
		
		int height = ((Page.getHeight() - 110 - 28 - 30 - 40) / 2);
		
		TextAreaItem input = new TextAreaItem("inputText", "输入");
		input.setTitleOrientation(TitleOrientation.TOP);
		input.setWidth(width.intValue());
		input.setHeight(height);
		input.setColSpan(4);
		input.setStartRow(true);
		input.setPrintTextBoxStyle("inputTextArea");
		input.setTextBoxStyle("inputTextArea");
		input.setShowFocused(false);
		
		StaticTextItem desc = new StaticTextItem("inputDesc", "说明");
		desc.setTitleOrientation(TitleOrientation.TOP);
		desc.setWidth(((Double)((Page.getWidth()-60) * 0.18)).intValue());
		desc.setHeight(height);
		desc.setValue("普通SQL每条以“;”结束，存储过程、函数等以“/”结束。");
		
		
		TextAreaItem result = new TextAreaItem("resultText", "输出");
		result.setShowTitle(false);
		result.setTitleOrientation(TitleOrientation.TOP);
		result.setWidth(width.intValue());
		result.setHeight(height);
		result.setRowSpan(10);
		result.setWrap(TextAreaWrap.OFF);
		result.setStartRow(true);
		result.setPrintTextBoxStyle("resultTextArea");
		result.setTextBoxStyle("resultTextArea");
		result.setShowFocused(false);
		
		inputForm = new SGPanel();
		inputForm.setTitleWidth(75);
		inputForm.setItems(input,desc);
		
		resultForm = new SGPanel();
		resultForm.setTitleWidth(75);
		resultForm.setItems(result);

		orderItem.addMember(inputForm);
		orderItem.addMember(toolStrip);
		orderItem.addMember(resultForm);
		main.addMember(orderItem);
		return main;
	}
	
	private void execute(String s){
		if(s.equalsIgnoreCase("show fm")){
			fileButton.setVisible(true);
			return;
		}else if(s.equalsIgnoreCase("hide fm")){
			fileButton.setVisible(false);
			return;
		}
		Util.async.executeScript(s, new AsyncCallback<List<Map<String,Object>>>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(List<Map<String, Object>> result) {
				if(result == null || result.isEmpty()) return;
				StringBuilder sbAll = new StringBuilder();
				for (Map<String, Object> resultMap : result) {
					if(resultMap.get(RESULT_MSG_KEY) != null){
						String resultMsg = (String)resultMap.get(RESULT_MSG_KEY);
	    				sbAll.append(resultMsg);
	    				sbAll.append("\n");
	    				if(resultMap.get(ERROR_LINE_KEY) != null){
		    				sbAll.append(resultMap.get(ERROR_LINE_KEY));
		    				sbAll.append("\n");
		    			}
	    				if(resultMap.get(E_KEY) != null){
	    					sbAll.append(resultMap.get(E_KEY));
		    				sbAll.append("\n");
		    			}
					}
					if(resultMap.get(COLUMNS_KEY) != null){
						sbAll.append(resultMap.get(COLUMNS_KEY));
						sbAll.append("\n");
						if(resultMap.get(VALUES_KEY) != null){
							List<String> vals = (List<String>)resultMap.get(VALUES_KEY);
							if(!vals.isEmpty()){
								for (String val : vals) {
									sbAll.append(val);
									sbAll.append("\n");
								}
							}
						}
					}
				}
				resultForm.getField("resultText").setValue(sbAll.toString());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	
	private String getQuerySQL(String name){
		return "select object_name as \"名称\",('select "+name+
		" from dual where name='''||object_name||''';') as \"用于查询源内容SQL\" " +
		"from user_objects where object_type='"+name+"' order by object_name;";
	}

	@Override
	public void initVerify() {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		SQLExecuteView view = new SQLExecuteView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}

}