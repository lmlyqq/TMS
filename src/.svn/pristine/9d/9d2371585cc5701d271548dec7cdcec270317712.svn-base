package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class DispatchPrintAction implements com.smartgwt.client.widgets.menu.events.ClickHandler{
	
	private SGTable table;
	private String printType;
	public static final Map<String, String> bizTyp2PrintTypMap = new HashMap<String, String>();
	static{
		bizTyp2PrintTypMap.put(StaticRef.B2B+"1", "b2bDispatch");//B2B 同城提货(B2B调度单)
		bizTyp2PrintTypMap.put(StaticRef.B2B+"2", "b2bDispatch");//B2B 干线运输(B2B调度单)
		bizTyp2PrintTypMap.put(StaticRef.B2B+"3", "b2bDispatch");//B2B 同城配送(B2B调度单)
		bizTyp2PrintTypMap.put(StaticRef.B2C+"1", "deliveryDispatch");//B2C 同城提货(提货调度入库单)
		bizTyp2PrintTypMap.put(StaticRef.B2C+"2", "arteryDispatch");//B2C 干线运输(干线调度单)
		bizTyp2PrintTypMap.put(StaticRef.B2C+"3", "b2cArtDispatch");//B2C 同城配送(B2C调度单)
		bizTyp2PrintTypMap.put(StaticRef.LD+"1", "deliveryDispatch");//LD 同城提货(提货调度入库单)
		bizTyp2PrintTypMap.put(StaticRef.LD+"2", "arteryDispatch");//LD 干线运输(干线调度单)
		bizTyp2PrintTypMap.put(StaticRef.LD+"3", "sendDispatch");//LD 同城配送(零担派送出库单)
		bizTyp2PrintTypMap.put(StaticRef.SHOP+"1", "deliveryDispatch");//店配 同城提货(提货调度入库单)
		bizTyp2PrintTypMap.put(StaticRef.SHOP+"2", "arteryDispatch");//店配 干线运输(干线调度单)
		bizTyp2PrintTypMap.put(StaticRef.SHOP+"3", "shopDispatch");//店配 同城配送(店配派送出库单)
	}
	
	public DispatchPrintAction(SGTable p_table){
		this.table = p_table;
	}
	
	public DispatchPrintAction(SGTable p_table, String printType){
		this.table = p_table;
		this.printType = printType;
	}
	@Override
	public void onClick(MenuItemClickEvent event) {
		if(table.getSelectedRecord() == null){
			SC.warn("请选择一个调度单！");
			return;
		}
		final ListGridRecord[] records = table.getSelection();
		if(records.length > 0){
			List<String> loadNos = new ArrayList<String>();
			for (ListGridRecord listGridRecord : records) {
				loadNos.add(listGridRecord.getAttribute("LOAD_NO"));
			}
			doPrint(loadNos);
		}else{
			SC.warn("请选择一个调度单！");
			return;
		}
	}
	
	private void doPrint(List<String> loadNoList){
		if(loadNoList.isEmpty()) return ;
		//保留原来的功能
		if(ObjUtil.isNotNull(this.printType)){
			open(this.printType, "loadNo", loadNoList.get(0));
			this.printType = null;
			return;
		}
		//智能选择打印模板
		Util.async.queryData(getQuerySql(loadNoList), false, new AsyncCallback<Map<String,Object>>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Map<String, Object> result) {
				Object data = result.get("data");
				if(data instanceof List) {
					Map<String, String> loadNo2BizTypMap = new HashMap<String, String>();
					List<List<String>> dataList = (List<List<String>>)data;
					for (List<String> list : dataList) {
						if(list.size() == 3){
							String loadNo = list.get(0);
							String bizTyp = list.get(1);
							String transSrvcId = list.get(2);
							if(loadNo2BizTypMap.containsKey(bizTyp+transSrvcId) && !loadNo2BizTypMap.get(bizTyp+transSrvcId).equals(loadNo)){
								loadNo = loadNo2BizTypMap.get(bizTyp+transSrvcId)+","+loadNo;
							}
							loadNo2BizTypMap.put(bizTyp+transSrvcId, loadNo);
						}
					}
					for (Map.Entry<String, String> entry : loadNo2BizTypMap.entrySet()) {
						open(bizTyp2PrintTypMap.get(entry.getKey()), "loadNo", entry.getValue());
					}
				}
			}
		});
	}
	/**
	 * 获取查询SQL
	 * @author Lang
	 * @param loadNoList
	 * @return
	 */
	private String getQuerySql(List<String> loadNoList){
		StringBuilder sb = new StringBuilder("select tsh.load_no,tsh.biz_typ,tsh.trans_srvc_id from trans_shipment_header tsh where load_no in('");
		for (String str : loadNoList) {
			sb.append(str);
			sb.append("','");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append(")");
		return sb.toString();
	}
	/**
	 * 弹出打印窗口
	 * @author Lang
	 * @param printType
	 * @param paramName
	 * @param paramValue
	 */
	public native static void open(String printType, String paramName, String paramValue)/*-{
		var url = $wnd.location.href;
		url = url.substring(0, url.lastIndexOf('/'));
		url = url+'/report?reportModel='+printType+"&"+paramName+"="+paramValue;
		$wnd.open(url);
	}-*/;

}
