package com.rd.client.action.tms.order;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 运输管理--托运单管理--右键--冻结，取消冻结操作
 * @author fanglm
 *
 */
public class OrdFrozenAction implements ClickHandler{
	
	private ListGrid table;
	
	public StringBuffer msg;
	
	private ListGridRecord[] list;
	
	private boolean frozen;
	
	/**
	 * 冻结\取消冻结
	 * @param table 列表
	 * @param frozen IS TRUE 冻结;IS FALSE 取消冻结
	 */
	public OrdFrozenAction(ListGrid table,boolean frozen){
		this.table = table;
		this.frozen = frozen;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		list = table.getSelection();
		if(list  != null && list.length > 0) {
			if(frozen){
				SC.confirm("请确认执行订单冻结！", new BooleanCallback() {
					public void execute(Boolean value) {
	                    if (value != null && value) {
	                    	doFrozen();
	                    }
	                }
	            });
			}else{
				doFrozen();
			}
		}
		

	}
	
	private void doFrozen(){
		
		StringBuffer sf = new StringBuffer();
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		//存储过程名称
		String proName="SP_ORDER_FROZEN(?,?,?)"; //odr_no,user_id,output_result
		
		//取消冻结
		if(!frozen){
			proName = "SP_ORDER_RELEASE(?,?,?)";
		}
		
		
		ArrayList<String> idList;
		HashMap<String,ArrayList<String>> procesList = new HashMap<String,ArrayList<String>>();
		for(int i=0;i<list.length;i++){
			if(frozen){
				if(!StaticRef.ORD_STATUS_CREATE.equals(list[i].getAttribute("STATUS"))){
					sf.append(list[i].getAttribute("ODR_NO"));
					sf.append(",");
				}else{
					idList = new ArrayList<String>();
					idList.add(list[i].getAttribute("ODR_NO"));
					idList.add(loginUser);
					procesList.put(list[i].getAttribute("ODR_NO"),idList);
				}
			}else{
				if(!"90".equals(list[i].getAttribute("STATUS"))){
					sf.append(list[i].getAttribute("ODR_NO"));
					sf.append(",");
				}else{
					idList = new ArrayList<String>();
					idList.add(list[i].getAttribute("ODR_NO"));
					idList.add(loginUser);
					procesList.put(list[i].getAttribute("ODR_NO"),idList);
				}
			}
		}
		if(sf.toString().length() > 0){
			if(frozen){
				MSGUtil.sayError("订单 "+sf.substring(0,sf.length()-1) +",非【" + StaticRef.SO_CREATE_NAME + "】状态, 不能执行冻结操作!");
			}else{
				MSGUtil.sayError("订单 "+sf.substring(0,sf.length()-1) +",非【已冻结】状态, 不能执行取消冻结操作!");
			}
			return;
		}
		
		Util.async.execPro(procesList, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){
					MSGUtil.showOperSuccess();
				}else{
					MSGUtil.sayError(result);
				}
				String[] soo = result.split(",");
				HashMap<String, String> map =new HashMap<String, String>();
				for(int i=0;i<soo.length;i++){
					map.put(soo[i], "1");
				}
				//刷新状态
				for(int i=0;i<list.length;i++){
					if(map.get(list[i].getAttribute("ODR_NO")) == null){
						if(frozen){
							list[i].setAttribute("STATUS", "90");
							list[i].setAttribute("STATUS_NAME", "已冻结");
						}else{
							list[i].setAttribute("STATUS", "10");
							list[i].setAttribute("STATUS_NAME", StaticRef.SO_CREATE_NAME);
						}
						table.updateData(list[i]);
						table.redraw();
					}
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}


}
