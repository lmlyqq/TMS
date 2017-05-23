package com.rd.client.win;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGPanel;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 时间管理
 * @author wangjun
 * 
 */
public class FeeRuleWin extends Window {
	
	private int width = 310;
	private int height = 210;
	private String top = "38%";
	private String left = "40%";
	private String title = " ";
	public Window window;
	private TextItem bas_rate;
	private String cur_value;
	
	
	public FeeRuleWin(TextItem bas_rate,String top,String left){
		this.bas_rate = bas_rate;
		this.top = top;
		this.left = left;
	}
	
	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		final TextItem FULL_INDEX = new TextItem("FULL_INDEX",Util.TI18N.FUZZYQRY());
		FULL_INDEX.setShowTitle(false);
		FULL_INDEX.setLeft(13);
		FULL_INDEX.setColSpan(4);
		FULL_INDEX.setWidth(270);
		FULL_INDEX.setHeight(30);
		FULL_INDEX.setDefaultValue(bas_rate.getValue().toString());
		
		final SGPanel searchPanel = new SGPanel();
		searchPanel.setTitleWidth(100);
		searchPanel.setItems(FULL_INDEX);
		
		final SGButtonItem sevenBtn = new SGButtonItem("7","");
		sevenBtn.setWidth(40);
		sevenBtn.setHeight(25);
		sevenBtn.setRowSpan(1);
		sevenBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
		SGButtonItem eightBtn = new SGButtonItem("8","");
		eightBtn.setWidth(40);
		eightBtn.setHeight(25);
		eightBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
		SGButtonItem nineBtn = new SGButtonItem("9","");
		nineBtn.setWidth(40);
		nineBtn.setHeight(25);
		nineBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + event.getItem().getTitle());
			}
		});
		SGButtonItem xBtn = new SGButtonItem("/","");
		xBtn.setWidth(40);
		xBtn.setHeight(25);
		xBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);			
			}
		});
        final SGCombo combo=new SGCombo("C_FIELD", "");
        combo.setWidth(87);
        combo.setHeight(25);
        combo.setTitleOrientation(TitleOrientation.LEFT);
        combo.setShowTitle(false);
        Util.initComboValue(combo, "USER_TAB_COLUMNS", "COLUMN_NAME", "COLUMN_NAME", " where TABLE_NAME = 'V_FEE_RULE'", "");
        combo.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				if(event.getValue() != null) {
					cur_value = event.getItem().getDisplayValue();
					searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
					bas_rate.setValue(FULL_INDEX.getDisplayValue());
					combo.setValue("");
				}
			}
        	
        });
		
		SGButtonItem fourBtn = new SGButtonItem("4","");
		fourBtn.setWidth(40);
		fourBtn.setHeight(25);
		fourBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});   
		fourBtn.setStartRow(true);
		SGButtonItem fiveBtn = new SGButtonItem("5","");
		fiveBtn.setWidth(40);
		fiveBtn.setHeight(25);
		fiveBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		}); 
		SGButtonItem sixBtn = new SGButtonItem("6","");
		sixBtn.setWidth(40);
		sixBtn.setHeight(25);
		sixBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
		SGButtonItem xinBtn = new SGButtonItem("*","");
		xinBtn.setWidth(40);
		xinBtn.setHeight(25);
		xinBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
		SGButtonItem leftBtn = new SGButtonItem("(","");
		leftBtn.setWidth(40);
		leftBtn.setHeight(25);
		leftBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
		SGButtonItem rightBtn = new SGButtonItem(")","");
		rightBtn.setWidth(40);
		rightBtn.setHeight(25);
		rightBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
        
        SGButtonItem oneBtn = new SGButtonItem("1","");
        oneBtn.setWidth(40);
        oneBtn.setHeight(25);
        oneBtn.setStartRow(true);
        oneBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
        SGButtonItem twoBtn = new SGButtonItem("2","");
		twoBtn.setWidth(40);
		twoBtn.setHeight(25);
		twoBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
		SGButtonItem threeBtn = new SGButtonItem("3","");
		threeBtn.setWidth(40);
		threeBtn.setHeight(25);
		threeBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
		SGButtonItem jBtn = new SGButtonItem("-","");
		jBtn.setWidth(40);
		jBtn.setHeight(25);
		jBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
		SGButtonItem baBtn = new SGButtonItem("Backspace","");
		baBtn.setWidth(84);
		baBtn.setHeight(25);
		baBtn.setColSpan(2);
		baBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue().replace(cur_value, ""));
			}
			
		});
		SGButtonItem zeroBtn = new SGButtonItem("0","");
		zeroBtn.setWidth(40);
		zeroBtn.setHeight(25);
		zeroBtn.setStartRow(true);
		zeroBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
        SGButtonItem pointBtn = new SGButtonItem(".","");
        pointBtn.setWidth(40);
        pointBtn.setHeight(25);
        pointBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
		});
        SGButtonItem plusBtn = new SGButtonItem("+","");
        plusBtn.setWidth(40);
        plusBtn.setHeight(25);
        plusBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
        	
        });
        
		SGButtonItem equalBtn = new SGButtonItem("=","");
		equalBtn.setWidth(40);
		equalBtn.setHeight(25);
		equalBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cur_value = event.getItem().getTitle();
				searchPanel.setValue("FULL_INDEX", FULL_INDEX.getDisplayValue() + cur_value);
			}
			
		});
		SGButtonItem cBtn = new SGButtonItem("C","");
		cBtn.setWidth(40);
		cBtn.setHeight(25);
		cBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cur_value = "";
				searchPanel.setValue("FULL_INDEX", "");
			}
			
		});
		SGButtonItem enterBtn = new SGButtonItem("Enter","");
		enterBtn.setWidth(40);
		enterBtn.setHeight(25);
		enterBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				bas_rate.setValue(FULL_INDEX.getDisplayValue());
				window.destroy();
			}
			
		});
		
		DynamicForm form=new DynamicForm();
		form.setItems(sevenBtn,eightBtn,nineBtn,xBtn,combo,
				      fourBtn,fiveBtn,sixBtn,xinBtn,leftBtn,rightBtn,
				      oneBtn,twoBtn,threeBtn,jBtn,baBtn,
				      zeroBtn,pointBtn,plusBtn,equalBtn,cBtn,enterBtn);
//		form.setColWidths(12);
		form.setNumCols(8);
		form.setPadding(8);
		form.setBackgroundColor(StaticRef.ICON_NEW);
		
        lay.addMember(searchPanel);
        lay.addMember(form);
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
