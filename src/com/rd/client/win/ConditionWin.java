package com.rd.client.win;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 费率管理 -- 条件编辑
 * @author fanglm
 * @create time 2011-5-27 17:33
 * 
 */
public class ConditionWin extends Window {
	
	private int width = 480;
	private int height = 300;
	private String top = "38%";
	private String left = "40%";
	private String title = " ";
	public Window window;
	public ToolStrip toolStrip;
	
	private TextAreaItem FULL_INDEX;
	private DynamicForm form;
	
	public ConditionWin(){
	}
	
	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		
		FULL_INDEX = new TextAreaItem("FULL_INDEX","");
		FULL_INDEX.setShowTitle(false);
		FULL_INDEX.setColSpan(4);
		FULL_INDEX.setWidth(450);
		FULL_INDEX.setHeight(50);
		FULL_INDEX.setDisabled(true);
		
		final SGPanel searchPanel = new SGPanel();
		searchPanel.setTitleWidth(100);
		searchPanel.setItems(FULL_INDEX);
		
		
		SGCombo RATE_ID = new SGCombo("RATE_LAB","路线");
		
		TextItem LOAD_AREA_ID = new TextItem("LOAD_AREA_ID");
		LOAD_AREA_ID.setVisible(false);
		
		ComboBoxItem LOAD_AREA_NAME = new ComboBoxItem("LOAD_AREA_NAME", Util.TI18N.LOAD_AREA_NAME());
		LOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		LOAD_AREA_NAME.setWidth(120);
		LOAD_AREA_NAME.setColSpan(4);
		Util.initArea(LOAD_AREA_NAME, LOAD_AREA_ID);
		
		TextItem UNLOAD_AREA_ID = new TextItem("UNLOAD_AREA_ID");
		UNLOAD_AREA_ID.setVisible(false);
		
		ComboBoxItem UNLOAD_AREA_NAME = new ComboBoxItem("UNLOAD_AREA_NAME", Util.TI18N.UNLOAD_AREA_NAME());
		UNLOAD_AREA_NAME.setWidth(120);
		UNLOAD_AREA_NAME.setColSpan(2);
		UNLOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(UNLOAD_AREA_NAME, UNLOAD_AREA_ID);
		
		
		SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID(),true);
		Util.initTrsService(TRANS_SRVC_ID, "");
		
		final SGCombo sku_cls = new SGCombo("SKU_CLS",Util.TI18N.SKU_CLS());//货品类别
		Util.initComboValue(sku_cls, "BAS_SKU_CLS", "SKUCLS", "DESCR_C");
		
		
		
		
		form =new DynamicForm();
		form.setItems(RATE_ID,LOAD_AREA_ID,LOAD_AREA_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,TRANS_SRVC_ID,sku_cls);
//		form.setColWidths(12);
		form.setNumCols(8);
		form.setPadding(8);
		form.setBackgroundColor(StaticRef.ICON_NEW);
		
		createBtn();
		
        lay.addMember(searchPanel);
        lay.addMember(form);
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(lay);
		window.addItem(toolStrip);
		window.setShowCloseButton(true);
		
		window.show();
		
		return window;
	}
	
	private void createBtn(){
		IButton searchBtn = new IButton(Util.BI18N.CONFIRM());
		searchBtn.setIcon(StaticRef.ICON_CONFIRM);
		searchBtn.setWidth(60);
		searchBtn.setAutoFit(true);
		searchBtn.setAlign(Alignment.RIGHT);
		
		searchBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				StringBuffer val = new StringBuffer();
				if(ObjUtil.isNotNull(form.getItem("LOAD_AREA_ID").getValue())){
					val.append("LOAD_AREA_ID = '");
					val.append(form.getItem("LOAD_AREA_ID").getValue());
					val.append("'");
					val.append(" AND ");
				}
				
				if(ObjUtil.isNotNull(form.getItem("UNLOAD_AREA_ID"))){
					val.append("UNLOAD_AREA_ID = '");
					val.append(form.getItem("UNLOAD_AREA_ID").getValue());
					val.append("'");
					val.append(" AND ");
				}
				
				if(ObjUtil.isNotNull(form.getItem("TRANS_SRVC_ID"))){
					val.append("TRANS_SRVC_ID = '");
					val.append(form.getItem("TRANS_SRVC_ID").getValue());
					val.append("'");
					val.append(" AND ");
				}
				
				if(ObjUtil.isNotNull(form.getItem("SKU_CLS"))){
					val.append("SKU_CLS = '");
					val.append(form.getItem("SKU_CLS").getValue());
					val.append("'");
					val.append(" AND ");
				}
				
				FULL_INDEX.setValue(val.substring(0, val.length()-5));
			}
		});
		
		IButton clearBtn = new IButton(Util.BI18N.CLEAR());
		clearBtn.setIcon(StaticRef.ICON_CANCEL);
		clearBtn.setWidth(60);
		clearBtn.setAutoFit(true);
		clearBtn.setAlign(Alignment.RIGHT);
		clearBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				
				form.clearValues();
				FULL_INDEX.clearValue();
			}
		});
		
		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(20);
		toolStrip.setAlign(Alignment.RIGHT);
        toolStrip.setMembers(searchBtn,clearBtn);
	}
}
