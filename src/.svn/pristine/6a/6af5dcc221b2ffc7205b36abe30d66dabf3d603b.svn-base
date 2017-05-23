package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
/**
 * 出分拣库
 * @author wangjun
 *
 */
public class OutSortingWhAction implements ClickHandler {

	private SGTable unshpmTable;
	private String SHPM_NO;
	
	public OutSortingWhAction(SGTable p_unshpmTable) {
		this.unshpmTable = p_unshpmTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) { 
		
		
		Object PICKING_STAT = ObjUtil.ifObjNull(unshpmTable.getSelectedRecord().getAttribute("PICKING_STAT"),"");
		SHPM_NO = unshpmTable.getSelectedRecord().getAttribute("SHPM_NO");
		if(!ObjUtil.isNotNull(PICKING_STAT)){
			SC.warn("作业单【"+SHPM_NO +"】不在分拣库中，不允许出分拣库!");
			return;
		}else if(PICKING_STAT.equals("15")){
        	doOperate();
        }else{
        	SC.warn("作业单【"+SHPM_NO +"】不在分拣库中，不允许出分拣库!");
        	return;
        }
	}
	
	private void doOperate(){
		
		StringBuffer sf = new StringBuffer();
		sf.append("UPDATE  TRANS_SHIPMENT_HEADER SET PICKING_STAT = 20 WHERE ");
		sf.append("SHPM_NO = '");
		sf.append(unshpmTable.getSelection()[0].getAttribute("SHPM_NO"));
		sf.append("' ");
		
		StringBuffer sf2 = new StringBuffer();
		sf2.append("insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO) values(");
		sf2.append("sys_guid()");
		sf2.append(",");
		sf2.append("'OUT_SORTING_WH'");
		sf2.append(",");
		sf2.append("'SHPM_NO'");
		sf2.append(",'");
		sf2.append(unshpmTable.getSelection()[0].getAttribute("SHPM_NO"));
		sf2.append("',");
		sf2.append("'出分拣库'");
		sf2.append(",");
		sf2.append("SYSDATE");
		sf2.append(",");
		sf2.append("'");
		sf2.append(LoginCache.getLoginUser().getUSER_ID());
		sf2.append("'");
		sf2.append(")");
		
		ArrayList<String> idList = new ArrayList<String>();
		idList.add(sf.toString());
		idList.add(sf2.toString());
		
		
		Util.async.excuteSQLList(idList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
			   if(StaticRef.SUCCESS_CODE.equals(result.subSequence(0, result.length()))){
				MSGUtil.showOperSuccess();
				unshpmTable.getSelectedRecord().setAttribute("PICKING_STAT", 20);
//				unshpmTable.updateData(unshpmTable.getSelectedRecord()); 
				unshpmTable.redraw();
//				unshpmTable.refreshFields();
				
				
//				ListGridRecord[] records = unshpmTable.getSelection();
//				for(int i = 0 ; i <records.length ; i++){
//					Record record = records[i];
//					record.setAttribute("PICKING_STAT", "20");
//				}
//				unshpmTable.updateData(unshpmTable.getSelectedRecord()); 
				
			   }else {
		    	   
		    	   MSGUtil.sayWarning(result.substring(2,result.length()));
		       }
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

}
