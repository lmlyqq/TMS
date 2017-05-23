package com.rd.client.common.widgets;

import com.rd.client.common.util.Util;
import com.rd.client.ds.base.AddrDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.grid.ListGridField;

public class SGSelect extends ComboBoxItem {
	
	public SGSelect(String colName,String cname){
		super(colName,cname);
		createSelect();
	}
	
	private void createSelect(){
		DataSource ds = AddrDS.getInstance("BAS_ADDRESS");
		
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_NAME = new ListGridField("AREA_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField ADDRESS = new ListGridField("ADDRESS");
		this.setWidth(120);  
		this.setColSpan(2);
		this.setOptionDataSource(ds);  
		this.setDisabled(false);
		this.setShowDisabled(false);
		this.setDisplayField("ADDR_NAME");  
		this.setPickListWidth(450);
	
//		this.set
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("LOAD_FLAG","Y");
		criteria.addCriteria("ENABLE_FLAG","Y");
		this.setPickListCriteria(criteria);
		
		this.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,ADDRESS);
	}
}
