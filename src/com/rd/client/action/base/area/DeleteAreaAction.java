package com.rd.client.action.base.area;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.base.BasAreaView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DeleteAreaAction implements ClickHandler {

	private SGTable table;
	private DataSource ds;
	private BasAreaView view;
	private ListGridRecord[] records = null;
	public DeleteAreaAction(SGTable p_table,BasAreaView view) {
		table = p_table;
		ds= table.getDataSource();
		this.view = view;
	}

	@Override
	public void onClick(ClickEvent event) {
		records = table.getSelection();
		if(records  != null && records.length > 0) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	doCheck();
                    }
                }
            });
		}
	}
	
	private void doCheck(){
   	
    	Util.async.countChild(ds.getTableName(),"PARENT_AREA_ID",view.selectRecord.getAttribute("AREA_CODE"), new AsyncCallback<Integer>() {
        //Util.async.countChild(ds.getAttribute("tableName"),"PARENT_AREA_ID",view.selectRecord.getAttribute("AREA_CODE"), new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(Integer result) {
				if(result > 0){
					MSGUtil.sayError("行政区域拥有下级区域，不允许删除!");
				}else{
					StringBuffer firstSql = new StringBuffer();
			    	firstSql.append("select count(1) as NUM from trans_order_header where LOAD_AREA_ID = '");
		    		firstSql.append(view.selectRecord.getAttribute("AREA_CODE"));
		    		firstSql.append("'");
		    		firstSql.append(" or UNLOAD_AREA_ID = '");
		    		firstSql.append(view.selectRecord.getAttribute("AREA_CODE"));
		    		firstSql.append("'");
		    		firstSql.append(" or UNLOAD_AREA_ID2 = '");
		    		firstSql.append(view.selectRecord.getAttribute("AREA_CODE"));
		    		firstSql.append("'");
		    		firstSql.append(" or UNLOAD_AREA_ID3 = '");
		    		firstSql.append(view.selectRecord.getAttribute("AREA_CODE"));
		    		firstSql.append("'");
		    		firstSql.append(" or LOAD_AREA_ID2 = '");
		    		firstSql.append(view.selectRecord.getAttribute("AREA_CODE"));
		    		firstSql.append("'");
		    		firstSql.append(" or LOAD_AREA_ID3 = '");
		    		firstSql.append(view.selectRecord.getAttribute("AREA_CODE"));
		    		firstSql.append("'");
		    		Util.async.queryData(firstSql.toString(), true, new AsyncCallback<Map<String, Object>>() {

						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
						}

						@Override
						public void onSuccess(Map<String, Object> result) {
							if(!(result == null || result.isEmpty() || 
									Integer.valueOf(result.get("singleData").toString()) > 0)) {
								doDelete();
							}else{
								MSGUtil.sayError("数据被托运单管理引用，无法删除！");
							}
						}
					});
					
				}
				
			}
		});
	}
	
	private void doDelete() {
    	ArrayList<String> sqlList = new ArrayList<String>();  
    	StringBuffer sf = null;
//    	for(int i = 0; i < records.length; i++) {
    		ListGridRecord rec = records[0]; 
    		sf = new StringBuffer();
    		sf.append("delete from ");
    		//sf.append(ds.getAttribute("tableName"));
    		sf.append(ds.getTableName());
    		sf.append(" where ");
    		sf.append(ds.getPrimaryKeyFieldName());
    		sf.append(" = '");
    		sf.append(rec.getAttribute(ds.getPrimaryKeyFieldName()));
    		sf.append("'");
    		sqlList.add(sf.toString());
//    	}
    	sf = null;
    	Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					table.OP_FLAG = "M";
					RefreshRecord();
					Criteria criteria = table.getCriteria();
					table.invalidateCache();
					criteria.addCriteria("OP_FLAG", table.OP_FLAG);
					table.filterData(criteria);
					
				}else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	private void RefreshRecord() {
		//for(int i = 0; i < records.length; i++) {
            table.removeData(table.getSelectedRecord());
            table.deselectAllRecords();
		//}
	}
}
