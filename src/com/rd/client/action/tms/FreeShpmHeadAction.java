package com.rd.client.action.tms;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsShipmentView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class FreeShpmHeadAction implements ClickHandler {
    private SGTable headTable;
    private ListGridRecord [] record;
    private TmsShipmentView view;
    
//    public FreeShpmHeadAction(SGTable headTable , ListGridRecord[] record){
//    	this.headTable = headTable;
//    	this.record = record;
//    }
    public FreeShpmHeadAction(SGTable headTable,TmsShipmentView view){
    	this.headTable = headTable;
    	this.view = view;
    }
    
	@Override
	public void onClick(ClickEvent event) {
		record = headTable.getSelection();
		if(ObjUtil.isNotNull(record) && record.length > 0) {  //yuanlei 2011-2-14
			//&& record != null && record.toString().trim().length()>0){//这个null值问题，等会要认真考虑一下
			doFree();
		} else {
			SC.say("请勾选作业单后再执行释放！");
		}
	}
	
	private void doFree(){

	  	StringBuffer sf = new StringBuffer();
	  	StringBuffer sf2 = new StringBuffer();
	  	String proName = "SP_SHPM_FREE(?,?)";
	  	ArrayList<String> idList;
		HashMap<String,ArrayList<String>> procesList = new HashMap<String,ArrayList<String>>();
		for (int i = 0; i < record.length; i++) {
			if(!StaticRef.SHPM_FROZEN.equals(record[i].getAttribute("STATUS"))){
				sf.append(record[i].getAttribute("SHPM_NO"));
			} else if (StaticRef.SHPM_CONFIRM.equals(record[i].getAttribute("STATUS"))){
				sf2.append(record[i].getAttribute("SHPM_NO"));
			} else {
				idList = new ArrayList<String>();
				idList.add(record[i].getAttribute("SHPM_NO"));
				procesList.put(record[i].getAttribute("SHPM_NO"),idList);
			}
		}
		if(sf != null && sf.length() > 0){
			MSGUtil.sayError("作业单号"+sf.substring(0, sf.length()-1)+",非【已冻结】状态, 不能执行释放操作!");
			return ;
		}
		if(sf2 != null && sf2.length() > 0){
			MSGUtil.sayError("作业单号"+sf2.substring(0, sf2.length()-1)+",已经处于【已确认】状态, 不用执行释放操作!");
			return ;
		}
		
		Util.async.execPro(procesList, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result)){
					MSGUtil.showOperSuccess();
				}else{
					MSGUtil.sayError(result);
				}
				String[] soo = result.split(",");
				HashMap<String, String> map =new HashMap<String, String>();
				for(int i=0;i<soo.length;i++){
					map.put(soo[i], "1");
				}
				for(int i=0;i<record.length;i++){
					if(map.get(record[i].getAttribute("SHPM_NO")) == null){
						record[i].setAttribute("STATUS",StaticRef.SHPM_CONFIRM);
						record[i].setAttribute("STATUS_NAME", StaticRef.SHPM_CONFIRM_NAME);
//						record[i].setAttribute("EXEC_ORG_ID_NAME", EXEC_ORG_ID.getValue());
						
						headTable.updateData(record[i]);
					}
				}
				
				//view.freeButton.disable();
				//view.freezeButton.enable();
				view.closeButton.enable();
				view.payoutButton.enable();
				headTable.discardAllEdits();
				headTable.redraw();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});

	
	}

}
