package com.rd.client.win;

import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.settlement.BillDetailDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGridField;

public class SupOrderDetailWin extends Window{
	
/**
 * 相关订单信息
 * @author wangjun
 */
	
	private int width = 1000;
	private int height = 200;
	private String top = "35%";
	private String left = "12%";
	private String title = "相关订单信息";
	public  Window window;
	private DataSource ds;
	private SGTable table;
	public String odr_no;
	public String shpm_no;



	public SupOrderDetailWin() {
	
	}
	public SupOrderDetailWin(String odr_no,String shpm_no,SGTable table){
		this.odr_no=odr_no;
		this.shpm_no=shpm_no;
		this.table = table;
	}

	public SupOrderDetailWin(int width, int height, String top, String left,
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
		ds = BillDetailDS.getInstance("V_BILL_DETAIL","TRANS_BILL_DETAIL");
		table = new SGTable(ds, "100%", "70%", false, true, false);
		
		Criteria crit = new Criteria();
		crit.addCriteria("OP_FLAG", "M");
		crit.addCriteria("ODR_NO", odr_no);
		crit.addCriteria("shpm_no", shpm_no);
		
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
		/*
		   	序列号、调度单号、客户单号、客户、货品、数量、发货数量，
			收货数量，车牌号、司机、配车备注，对账状态，审核状态
			
	     */
		ListGridField SERIAL_NUM = new ListGridField("SERIAL_NUM", "回单序列号", 70);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO", Util.TI18N.LOAD_NO(), 90);
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(), 90);
		ListGridField DRIVER = new ListGridField("DRIVER", Util.TI18N.DRIVER1(), 60);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO", Util.TI18N.PLATE_NO(), 60);
		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),130);
		ListGridField ORD_QNTY_EACH = new ListGridField("QNTY_EACH", Util.TI18N.ORD_TOT_QNTY(), 70);
		ListGridField LD_QNTY = new ListGridField("LD_QNTY", Util.TI18N.LD_QNTY(), 70);
		ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY", Util.TI18N.UNLD_QNTY(), 70);
		ListGridField ACCOUNT_STAT = new ListGridField("ACCOUNT_STAT_NAME", "对账状态", 70);
		ListGridField AUDIT_STAT = new ListGridField("AUDIT_STAT_NAME", "审核状态", 70);
		ListGridField TRANS_NOTES = new ListGridField("TRANS_NOTES", "配车备注", 100);
		
		
		table.setFields(SERIAL_NUM,LOAD_NO,CUSTOM_ODR_NO,DRIVER,PLATE_NO,SKU_NAME,ORD_QNTY_EACH,LD_QNTY,
				UNLD_QNTY,ACCOUNT_STAT,AUDIT_STAT,TRANS_NOTES);	
	}
}
