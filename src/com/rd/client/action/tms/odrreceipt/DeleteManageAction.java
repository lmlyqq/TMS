package com.rd.client.action.tms.odrreceipt;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsOdrReceiptView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 运输管理-->托运单回单-->货损货差-->删除按钮
 * @author wangjun
 *
 */
public class DeleteManageAction implements ClickHandler {

	private SGTable table;
	private DataSource ds;
	private ListGridRecord[] records = null;
	private TmsOdrReceiptView view;
	
	public DeleteManageAction(SGTable pTable,TmsOdrReceiptView view) {
		this.table = pTable;
		this.view = view;
	}
	public void onClick(ClickEvent event) {

		ds= table.getDataSource();
		records = table.getSelection();
		if(records  != null && records.length > 0) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	doDelete();
                    }
                }
            });
		}
	}
	private void doDelete() {
		ArrayList<String> sqlList = new ArrayList<String>();  
    	ArrayList<String> descrList = new ArrayList<String>();//仅用作写入用户登录日志
    	StringBuffer sf = null;
    	String descr = "";                     //仅用作写入用户登录日志
    	for(int i = 0; i < records.length; i++) {
    		ListGridRecord rec = records[i]; 
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
    		
    		//String[] titles = Util.getPropTitle(ds.getAttribute("tableName"));
    		//String[] fields = Util.getPropField(ds.getAttribute("tableName"));
    		String[] titles = Util.getPropTitle(ds.getTableName());
    		String[] fields = Util.getPropField(ds.getTableName());
    		descr = StaticRef.ACT_DELETE + titles[0] + "【" + rec.getAttribute(fields[0]) + "】";
    		descrList.add(descr);
    	}
    	sf = null;
    	Util.async.doDelete(descrList, sqlList, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					table.OP_FLAG = "M";
					RefreshRecord();
					Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG", table.OP_FLAG);
					criteria.addCriteria("ODR_NO",view.order_no);
					table.fetchData(criteria);
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	private void RefreshRecord() {
		for(int i = 0; i < records.length; i++) {
            table.removeData(records[i]);
            table.deselectAllRecords();
		}
	}
}
