package com.rd.client.action.tms.order;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 生成订单组
 * @author Administrator
 *
 */
public class GenerateOdrGroupAction implements ClickHandler{

	private SGTable table;
	private ListGridRecord[] records;
	public int hRow=0;
	
	public GenerateOdrGroupAction(SGTable p_table, OrderView p_view) {
		this.table = p_table;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		records = table.getSelection();
		
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> order_map = new HashMap<String, String>(); 
		
		if(records != null && records.length > 0) {
			ListGridRecord rec = null;
			for(int i = 0; i < records.length; i++) {
				rec = records[i];
				order_map.put(Integer.toString(i+1), rec.getAttribute("ODR_NO"));
			}
			listmap.put("1", order_map);
			listmap.put("2", LoginCache.getLoginUser().getUSER_ID());
			listmap.put("3", "N");
			String json = Util.mapToJson(listmap);
			Util.async.execProcedure(json, "SP_ODRGROUP_CREATE(?,?,?,?)", new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
						SC.confirm(result.substring(2), new BooleanCallback() {
							public void execute(Boolean value) {
			                    if (value != null && value) {
			                    	
			                    }
			                }
			            });
//						MSGUtil.sayInfo(result.substring(2));
						
						String group_no = result.substring(2,14);
						//刷新待调订单列表	
						ListGridRecord rec = null;
						for(int i = 0; i < records.length; i++) {
							rec = records[i];
							rec.setAttribute("BTCH_NUM", group_no);
							table.updateData(rec);
						}
						table.redraw();
					}
					else{
						MSGUtil.sayError(result);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError("服务器连接已中断，请重新登录!");
				}
			});
		}else {
			MSGUtil.sayWarning("未选择托运单!");
			return;
		}
	}

}