package com.rd.client.action.tms.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 运输管理--托运单管理--计费按钮
 * @author fanglm
 *
 */
public class FeeCalculateAction implements ClickHandler,com.smartgwt.client.widgets.events.ClickHandler{
	
	private ListGrid table;
	
	public StringBuffer msg;
	
	public OrderView view;
	
	private ListGridRecord[] list;
	public FeeCalculateAction(ListGrid table,OrderView view){
		this.table = table;
		this.view = view;
	}

	@Override
	public void onClick(MenuItemClickEvent event) {
		doSome();
	}
	
	@Override
	public void onClick(ClickEvent event) {
		if("BC".equals(JSOHelper.getAttribute(((IButton)event.getSource()).getJsObj(), "eventSource"))){
			JSOHelper.setAttribute(((IButton)event.getSource()).getJsObj(), "eventSource", "");
			doSome();
		}else{
			Collection<IButton> saveButton = view.save_map.values();
			for (IButton iButton : saveButton) {
				JSOHelper.setAttribute(iButton.getJsObj(), "eventSource", "JF");
				iButton.fireEvent(event);
				event.cancel();
				return;
			}
		}
	}
	
	private void doSome() {
		list = table.getSelection();
		
		if (list.length == 0)
			return;
		
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		//存储过程名称
		String proName="SP_REC_CALCUALTE(?,?,?)"; //odr_no,user_id,output_result
		
		ArrayList<String> idList;
		HashMap<String,ArrayList<String>> procesList = new HashMap<String,ArrayList<String>>();
		for(int i=0;i<list.length;i++){
			idList = new ArrayList<String>();
			idList.add(list[i].getAttribute("ODR_NO"));
			idList.add(loginUser);
			procesList.put(list[i].getAttribute("ODR_NO"), idList);
		}
		
		Util.async.execPro(procesList, proName, new AsyncCallback<String>() {			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					
					MSGUtil.showOperSuccess();	
					
					Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG","M");
					criteria.addCriteria("DOC_NO",list[0].getAttributeAsString("ODR_NO"));
					view.groupTable2.invalidateCache();
					view.groupTable2.fetchData(criteria);
				}else{
					String msg = result.split("@")[0];
					MSGUtil.sayWarning(msg.substring(2));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});

	}

	

}
