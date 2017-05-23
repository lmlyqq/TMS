package com.rd.client.action.base.range;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGrid;


/**
 * 服务范围保存事件
 * @author yuanlei
 *
 */
public class SaveRangeAction implements ClickHandler{

	private TreeGrid tree;
	private SGTable table;
	private String _selection_ = null;
    
	public SaveRangeAction(SGTable ptable, TreeGrid treeGrid) {
		tree = treeGrid;
		table = ptable;
	}
	@Override
	public void onClick(ClickEvent event) {
		if(event != null && table.getSelectedRecord() != null) {
			ArrayList<String> sqlList = new ArrayList<String>();
			ListGridRecord[] records = tree.getSelection();
			
			
			String range_id = table.getSelectedRecord().getAttribute("ID");
			StringBuffer sf = new StringBuffer();
			sf.append("delete from BAS_RANGE_DETAIL where RANGE_ID = '");
			sf.append(range_id);
			sf.append("'");
			sqlList.add(sf.toString());
			for(int i=0 ;i<records.length;i++){
				if(isInvalid(records[i])){
					continue;
				}
				sf = new StringBuffer();
				sf.append("insert into BAS_RANGE_DETAIL(ID,RANGE_ID,RANGE_AREA_ID,AREA_LEVEL,ADDTIME,ADDWHO) select sys_guid(),'");
				sf.append(range_id);
				sf.append("','");
				sf.append(records[i].getAttributeAsString("AREA_CODE"));
				sf.append("','");
				sf.append(records[i].getAttributeAsString("AREA_LEVEL"));
				sf.append("',sysdate,'");
				sf.append(LoginCache.getLoginUser().getUSER_ID());
				sf.append("' from dual");
				sqlList.add(sf.toString());
			}
			Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(String result) {
					if(result.equals(StaticRef.SUCCESS_CODE))
						MSGUtil.showOperSuccess();
					else 
						MSGUtil.showOperError();
				}
				
			});
		}
	}
	/**
	 * 判断Record下所有子节点是否全部选中
	 * @author Lang.Xiao
	 * @param record
	 * @return 全部选中返回false,否则返回true
	 */
	private boolean isInvalid(ListGridRecord record){
		if(!record.getAttributeAsBoolean("isFolder")){
			return false;
		}
		if(_selection_ == null){
			String[] attrs = record.getAttributes();
			for (int i = attrs.length-1; i > 0; i--) {
				if(attrs[i].indexOf("_selection_") > 0){
					_selection_ = attrs[i];
					break;
				}
			}
			if(_selection_ == null) return false;
		}
		return record.getAttributeAsBoolean(_selection_);
	}
}
