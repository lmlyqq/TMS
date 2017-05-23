package com.rd.client.win;

import java.util.HashMap;

import com.rd.client.common.widgets.SGTable;
import com.rednels.ofcgwt.client.ChartWidget;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.elements.PieChart;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class ChartWin extends Window {

	private String width = "400";
	private String height = "300";
	private String title = "";
	public Window window = null;
	public DynamicForm form;
	public SGTable table;
	private HashMap<String, Double> map = new HashMap<String, Double>();
	public ChartWin(SGTable p_table,String p_width, String p_height, String p_title) {
		this.width = p_width;
		this.height = p_height;
		this.table = p_table;
		this.title = p_title;
	}

	public void createBtnWidget(ToolStrip strip) {
		;
	}
	
	public Window getViewPanel() {
		
		getPieData();
		
		VLayout stack = new VLayout();
		stack.addMember(addPieChart());
		
		window = new Window();
		window.setTitle(title);
		window.setLeft("20%");
		window.setTop("20%");
		window.setWidth(width);  
		window.setHeight(height); 
		window.setCanDragReposition(true);  
		window.setCanDragResize(true);  
		window.addItem(stack);
		window.show();
		
		return window;
	}
	
	private ChartWidget addPieChart() {
		ChartWidget chart = new ChartWidget();		
		ChartData cd = new ChartData(title,"font-size: 14px; font-family: Verdana; text-align: center;");
		cd.setBackgroundColour("#ffffff");
		PieChart pie = new PieChart();
		pie.setAlpha(0.3f);
		pie.setNoLabels(true);
		pie.setTooltip("#label# $#val#<br>#percent#");
		pie.setAnimate(true);
		pie.setGradientFill(true);
		pie.setColours("#ff0000","#00ff00","#0000ff","#ff9900","#ff00ff");
		
		Object[] iter = map.keySet().toArray();
		for(int i = 0; i < iter.length; i++) {
			String key = (String)iter[i];
			double  value= (double)map.get(key);
			pie.addSlices(new PieChart.Slice(value,key));
		}
		pie.setNoLabels(false);
		cd.addElements(pie);
		chart.setSize(width, height);
		chart.setJsonData(cd.toString());
		return chart;
	}
	
	private void getPieData() {
		ListGridRecord[] records = table.getRecords();
		if(records != null && records.length > 0) {
			map = new HashMap<String, Double>();
			for(int i = 0; i < records.length; i++) {
				double amount =  records[i].getAttributeAsDouble("SALES_AMOUNT");
				String area = records[i].getAttributeAsString("AREA");
				if(map.get(area) != null) {
					double douValue = (double)map.get(area) + amount;
					map.put(area, douValue);
				}
				else {
					map.put(area, amount);
				}
			}
		}
	}
}
