package com.rd.client.common.widgets;

import com.rd.client.common.util.StaticRef;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyUpEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyUpHandler;
import com.smartgwt.client.widgets.grid.ListGrid;

/**
 * 分页的组件
 * @author yuanlei
 *
 */
public class SGPage {

	private DynamicForm form;
	private ListGrid grid;
	
    private TextItem cur_page;
    private TextItem sum_page;
    
    private boolean showForm = true;
	
	public SGPage(SGTable table, boolean p_showForm) {
		showForm = p_showForm;
		grid = table;
	}
	
	public SGPage(ListGrid table, boolean p_showForm) {
		showForm = p_showForm;
		grid = table;
	}
	
	/**
	 * 执行分页查询
	 * yuanlei(2010-6-19)
	 */
	public void doPageQuery(int page_type) {
		final int type = page_type;
		setPageValue(type);
		grid.invalidateCache();
		Criteria criteria = grid.getCriteria();
//		if(criteria == null){
//			criteria = new Criteria();
//		}
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria(form.getValuesAsCriteria());
		grid.fetchData(criteria);
	}

	/**
	 * 设置当前页索引�?
	 * yuanlei(2010-6-19)
	 */
	private void setPageValue(int page_type) {
		switch (page_type) {
			case StaticRef.FIRST_PAGE: {
				cur_page.setValue("1");
				break;
			}
			case StaticRef.PRE_PAGE: {
				int index = Integer.parseInt(cur_page.getDisplayValue());
				if(index > 1) {
					cur_page.setValue(Integer.toString(index - 1));
				}
				break;
			}
			case StaticRef.NEXT_PAGE: {
				int index = Integer.parseInt(cur_page.getDisplayValue());
				int size =  Integer.parseInt(sum_page.getDisplayValue());
				if(index < size) {
					cur_page.setValue(Integer.toString(index + 1));
				}
				break;
			}
			case StaticRef.LAST_PAGE: {
				int size =  Integer.parseInt(sum_page.getDisplayValue());
				if(size > 0) {
					cur_page.setValue(Integer.toString(size));
				}
				break;
			}
			case StaticRef.INIT_PAGE: {
				cur_page.setValue("0");
				break;
			}
		}
	}
	
	/**
	 * 布局中加入分页按钮
	 * yuanlei(2010-6-19)
	 */
	public DynamicForm initPageBtn() {
		form = new DynamicForm();
		form.setPadding(0);
		form.setMargin(1);
		form.setCellPadding(1);
		form.setNumCols(9);
		form.setAlign(Alignment.LEFT);
		form.setWidth(270);
        
        
        cur_page = new TextItem("CUR_PAGE"); 
        cur_page.setDefaultValue("1");
        cur_page.setShowTitle(false);
        cur_page.setWidth("28");
        cur_page.setTop(4);
        cur_page.setHeight(18);
        
        StaticTextItem lab = new StaticTextItem();  
        lab.setWrap(false);  
        lab.setShowTitle(false);
        lab.setDefaultValue("<font style=\"color: #ffffff;\">/</font>");
        lab.setAlign(Alignment.CENTER);   

        sum_page = new TextItem("SUM_PAGE"); 
        sum_page.setDefaultValue("0");
        sum_page.setShowTitle(false);
        sum_page.setWidth("30");
        sum_page.setTop(4);
        sum_page.setHeight(18);
        
        ButtonItem firstButton = new ButtonItem();
        firstButton.setIcon(StaticRef.ICON_FIRST);
        firstButton.setTitle("");
        firstButton.setPrompt("首页");
        firstButton.setWidth(24);
        firstButton.setStartRow(true);
        firstButton.setEndRow(false);
        firstButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doPageQuery(StaticRef.FIRST_PAGE);
			}      	
        });        
        ButtonItem frontButton = new ButtonItem();
        frontButton.setIcon(StaticRef.ICON_PRE);
        frontButton.setPrompt("上页");	
        frontButton.setTitle("");
        frontButton.setWidth(24);
        frontButton.setStartRow(false);
        frontButton.setEndRow(false);
        frontButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doPageQuery(StaticRef.PRE_PAGE);
			}        	
        });        
        ButtonItem nextButton = new ButtonItem();
        nextButton.setIcon(StaticRef.ICON_NEXT);
        nextButton.setPrompt("下页"); 
        nextButton.setTitle("");
        nextButton.setWidth(24);
        nextButton.setStartRow(false);
        nextButton.setEndRow(false);
        nextButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doPageQuery(StaticRef.NEXT_PAGE);
			}        	
        });       
        ButtonItem lastButton = new ButtonItem();
        lastButton.setIcon(StaticRef.ICON_LAST);
        lastButton.setPrompt("末页");
        lastButton.setTitle("");
        lastButton.setWidth(24);
        lastButton.setStartRow(false);
        lastButton.setEndRow(false);
        lastButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doPageQuery(StaticRef.LAST_PAGE);
			}        	
        }); 
        
        StaticTextItem rec_lab = new StaticTextItem();  
        rec_lab.setWrap(false);  
        rec_lab.setShowTitle(false);
        rec_lab.setWidth(50);
        rec_lab.setDefaultValue("<div align=\"right\">记录数:</div>");
        rec_lab.setAlign(Alignment.CENTER);
        
        TextItem record = new TextItem("TOTAL_COUNT");
        record.setShowTitle(false);
        record.setDefaultValue("0");
        record.setWidth("40");
        record.setTop(4);
        record.setHeight(18);
        record.setStartRow(false);
        record.setEndRow(true);
        
        cur_page.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {

				if(event.getKeyName().equals("Enter")) {
					doPageQuery(StaticRef.GO_PAGE);
				}
			}
        	
        });
        form.setItems(firstButton, frontButton, cur_page, lab, sum_page, nextButton, lastButton, rec_lab, record);
        if(!showForm) {
        	form.setVisible(showForm);
        }
        return form;
	}
}
