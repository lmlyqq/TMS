package com.rd.client.win;

import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.settlement.SupFeeCoutWinlDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGridField;

public class SupFeeCoutWin extends Window{
	
/**
 * 费用结算
 * @author wangjun
 */
	
	private int width = 665;
	private int height = 300;
	private String top = "25%";
	private String left = "22%";
	private String title = "汇总信息";
	public  Window window;
	private DataSource ds;
	private SGTable table;
	public String load_no;



	public SupFeeCoutWin() {
	
	}
	public SupFeeCoutWin(String load_no,SGTable table){
		this.load_no=load_no;
		this.table = table;
	}

	public SupFeeCoutWin(int width, int height, String top, String left,
			String title, Window window, DataSource ds) {
		this.width = width;
		this.height = height;
		this.top = top;
		this.left = left;
		this.title = title;
		this.window = window;
		this.ds = ds;
	}

	public Window getViewPanel(){	
		ds=SupFeeCoutWinlDS.getInstance("V_BILL_GP_SKU","V_BILL_GP_SKU");
		table = new SGTable(ds, "100%", "70%", false, true, false);
		
		Criteria crit = new Criteria();
		crit.addCriteria("OP_FLAG", "M");
		crit.addCriteria("LOAD_NO", load_no);
		
		table.fetchData(crit);
		
		table.setCanEdit(false);
	    createListFields(table);	
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.setShowCloseButton(true);
		window.addItem(table);
		window.show();
		return window;
	}


	private void createListFields(SGTable table) {
		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),130);
		ListGridField SKU = new ListGridField("SKU",Util.TI18N.SKU(),80);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE","应付金额",60);
		ListGridField PRE_FEE = new ListGridField("PRE_FEE","计划金额",60);
		ListGridField ORD_QNTY_EACH = new ListGridField("QNTY_EACH", Util.TI18N.ORD_TOT_QNTY(), 90);
		ListGridField LD_QNTY_EACH = new ListGridField("LD_QNTY_EACH", Util.TI18N.FOLLOW_LD_QNTY(), 90);
		ListGridField UNLD_QNTY_EACH = new ListGridField("UNLD_QNTY_EACH", Util.TI18N.UNLD_QNTY(), 90);
		table.setFields(SKU_NAME,SKU ,ORD_QNTY_EACH,LD_QNTY_EACH,UNLD_QNTY_EACH, DUE_FEE, PRE_FEE);	
	}
}
