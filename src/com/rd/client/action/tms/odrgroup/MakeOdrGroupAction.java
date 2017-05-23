package com.rd.client.action.tms.odrgroup;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.OrderGroupView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 生成订单组
 * @author yuanlei
 *
 */
public class MakeOdrGroupAction implements ClickHandler {

	private SGTable table;
	private ListGridRecord[] records;
	public int hRow=0;
	
	public MakeOdrGroupAction(SGTable p_table, OrderGroupView p_view) {
		this.table = p_table;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		records = table.getSelection();
		
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> order_map = new HashMap<String, String>(); //托运单 
		
		if(records != null && records.length > 0) {
			ListGridRecord rec = null;
			String load_area_name2 = "",unload_area_name2 = "";
			for(int i = 0; i < records.length; i++) {
				rec = records[i];
				if(i > 0) {
					if(!load_area_name2.equals(rec.getAttribute("LOAD_AREA_NAME2")) || 
							!unload_area_name2.equals(rec.getAttribute("UNLOAD_AREA_NAME2"))) {
						SC.warn("所选订单发货地和收货地必须相同!");
						return;
					}	
				}
				load_area_name2 = rec.getAttribute("LOAD_AREA_NAME2");
				unload_area_name2 = rec.getAttribute("UNLOAD_AREA_NAME2");
				order_map.put(Integer.toString(i+1), rec.getAttribute("ODR_NO"));
				
			}
			listmap.put("1", order_map);
			listmap.put("2", LoginCache.getLoginUser().getUSER_ID());
			listmap.put("3", "Y");
			String json = Util.mapToJson(listmap);
			Util.async.execProcedure(json, "SP_ODRGROUP_CREATE(?,?,?,?)", new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.sayInfo(result.substring(2));
						
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
		}
		else {
			MSGUtil.sayWarning("未选择托运单!");
			return;
		}
	}

}
