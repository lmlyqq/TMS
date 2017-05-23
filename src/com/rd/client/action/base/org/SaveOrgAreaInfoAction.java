package com.rd.client.action.base.org;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.TreeTable;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGrid;


/**
 * 组织机构->行政区域页签保存事件
 * @author yuanlei
 *
 */
public class SaveOrgAreaInfoAction implements ClickHandler{

	private TreeGrid tree;
	private TreeTable table;
	private String _selection_ = null;
    
	public SaveOrgAreaInfoAction(TreeTable ptable, TreeGrid treeGrid) {
		tree = treeGrid;
		table = ptable;
	}
	@Override
	public void onClick(ClickEvent event) {
		if(event != null && table.getSelectedRecord() != null) {
			ArrayList<String> sqlList = new ArrayList<String>();
			ListGridRecord[] records = tree.getSelection();
			String org_id = table.getSelectedRecord().getAttribute("ID");
			List<String> areaIds = new ArrayList<String>();
			StringBuffer sf = new StringBuffer();
			sf.append("delete from BAS_ORG_AREA where ORG_ID = '");
			sf.append(org_id);
			sf.append("'");
			sqlList.add(sf.toString());
			for(int i=0 ;i<records.length;i++){
				if(isInvalid(records[i])){
					continue;
				}
				sf = new StringBuffer();
				sf.append("insert into BAS_ORG_AREA(ID,ORG_ID,AREA_ID,AREA_LEVEL,ADDTIME,ADDWHO) select sys_guid(),'");
				sf.append(org_id);
				sf.append("','");
				sf.append(records[i].getAttributeAsString("AREA_CODE"));
				sf.append("','");
				sf.append(records[i].getAttributeAsString("AREA_LEVEL"));
				sf.append("',sysdate,'");
				sf.append(LoginCache.getLoginUser().getUSER_ID());
				sf.append("' from dual");
				sqlList.add(sf.toString());
				if(Integer.parseInt(records[i].getAttribute("AREA_LEVEL")) == 4) {
					areaIds.add(records[i].getAttributeAsString("AREA_CODE"));
				}
			}
			Util.async.excuteSQLListCheckUn(sqlList, getCheckUniqueneSQL(org_id,areaIds), new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(String result) {
					if(result.equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.showOperSuccess();
					}else {
						result = "所选城市已被[" + result.substring(2) + "]分配";
						MSGUtil.sayError(result);
						/*if(result.equals("uniquene")){
							SC.warn("一个行政区域只能分配一个组织机构");
						}*/
					}
					
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
	
	private String getCheckUniqueneSQL(String orgId, List<String> areaIds){
		if(areaIds.size() == 0)return null;
		StringBuffer sf = new StringBuffer("select org_cname from bas_org where id in (select org_id from BAS_ORG_AREA t where t.ORG_ID<>'");
		sf.append(orgId);
		sf.append("'");
		sf.append(" and (");
		int i=0;
		for (String areaId : areaIds) {
			if(i++>0){
				sf.append(" or");
			}
			sf.append(" t.area_id=");
			sf.append(areaId);
		}
		sf.append(" )) and rownum <= 1");
		return sf.toString();
	}
}
