package com.rd.client.action.tms.order;


import java.util.HashMap;

import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 运输管理--托运单管理--托运单明细--右键--新增
 * @author fanglm
 *
 */
public class NewOrderItemAction implements ClickHandler,com.smartgwt.client.widgets.events.ClickHandler{

	private SGTable table = null;
	//private SGTable main_table;  --yuanlei 2011-2-14
	private OrderView view;
	private int row;
	private HashMap<String, String> map;
	
	
	public NewOrderItemAction(SGTable p_table,OrderView view) {
		table = p_table;
		this.view = view;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		table.OP_FLAG = "A";
//		if(view.vm.getValueAsString("ODR_NO") == null){
//			MSGUtil.sayError("订单主信息不存在，不能新增明细！");
//			return;
//		}
		ListGridRecord rec = table.getSelectedRecord();
		if(!StaticRef.ORD_STATUS_CREATE.equals(rec.getAttribute("STATUS"))){
			MSGUtil.sayError("订单非【" + StaticRef.SO_CREATE_NAME + "】状态，不能新增明细！");
			return;
		}
		
		/*if(main_table != null) {
			main_table.invalidateCache();
		}*/
		
		create(false);
	}
	public void create(boolean bool){
		
		table.OP_FLAG = "A";
		/*if(main_table != null) {
			main_table.invalidateCache();
		}*/
		table.startEditingNew();
		row = table.getAllEditRows()[table.getAllEditRows().length-1];
		view.itemRow = row;
		map = LoginCache.getDefCustomer();
//		table.setEditValue(row, "PACK_ID", "CDA046CDE9824B648E27273CF9656CA4");
//		table.setEditValue(row, "UOM", "个");
		
		table.setEditValue(view.itemRow, "OP_FLAG", "A");
		table.setEditValue(view.itemRow, "ODR_NO", view.vm.getValueAsString("ODR_NO"));
		if (bool){
			table.setEditValue(view.itemRow, "SKU_NAME", view.customerRecord.getAttribute("SKU_NAME"));
			table.setEditValue(view.itemRow,"SKU_ID",view.customerRecord.getAttribute("SKU_ID"));
			table.setEditValue(view.itemRow, "SKU", view.customerRecord.getAttribute("SKU"));
			table.setEditValue(view.itemRow, "SKU_SPEC", view.customerRecord.getAttribute("SKU_SPEC"));
			table.setEditValue(view.itemRow, "PACK_ID", view.customerRecord.getAttribute("PACK_ID"));
			table.setEditValue(view.itemRow, "UOM", view.customerRecord.getAttribute("TRANS_UOM"));
		}
		table.setEditValue(view.itemRow, "QNTY", "1");
		table.setEditValue(view.itemRow, "QNTY_EACH", "1");
		
		if(view.customerRecord != null){
			if(view.customerRecord.getAttributeAsBoolean("SKU_EDIT_FLAG")){
				table.getField("VOL").setCanEdit(true);
				table.getField("G_WGT").setCanEdit(true);
			}else{
				table.getField("VOL").setCanEdit(false);
				table.getField("G_WGT").setCanEdit(false);
			}
			/**
			table.setEditValue(view.itemRow, "OP_FLAG", "A");
			table.setEditValue(view.itemRow, "ODR_NO", view.vm.getValueAsString("ODR_NO"));*/
			table.setEditValue(view.itemRow, "LOAD_ID", view.vm.getValueAsString("LOAD_ID"));
			if(ObjUtil.isNotNull(view.vm.getValueAsString("LOAD_ID"))) {
				table.setEditValue(view.itemRow, "LOAD_NAME", view.vm.getValueAsString("LOAD_NAME"));
				table.setEditValue(view.itemRow, "LOAD_AREA_ID", view.vm.getValueAsString("LOAD_AREA_ID"));
				table.setEditValue(view.itemRow, "LOAD_AREA_NAME", view.vm.getValueAsString("LOAD_AREA_NAME"));
				table.setEditValue(view.itemRow, "LOAD_AREA_ID2", view.vm.getValueAsString("LOAD_AREA_ID2"));
				table.setEditValue(view.itemRow, "LOAD_AREA_NAME2", view.vm.getValueAsString("LOAD_AREA_NAME2"));
				table.setEditValue(view.itemRow, "LOAD_AREA_ID3", view.vm.getValueAsString("LOAD_AREA_ID3"));
				table.setEditValue(view.itemRow, "LOAD_AREA_NAME3", view.vm.getValueAsString("LOAD_AREA_NAME3"));
				table.setEditValue(view.itemRow, "LOAD_ADDRESS", view.vm.getValueAsString("LOAD_ADDRESS"));
				table.setEditValue(view.itemRow, "LOAD_CONTACT", view.vm.getValueAsString("LOAD_CONTACT"));
				table.setEditValue(view.itemRow, "LOAD_TEL", view.vm.getValueAsString("LOAD_TEL"));
				table.setEditValue(view.itemRow, "LOAD_CODE", view.vm.getValueAsString("LOAD_CODE"));
			}
			
			table.setEditValue(view.itemRow, "UNLOAD_ID", view.vm.getValueAsString("UNLOAD_ID"));
			if(ObjUtil.isNotNull(view.vm.getValueAsString("UNLOAD_ID"))) {
				table.setEditValue(view.itemRow, "UNLOAD_NAME", view.vm.getValueAsString("UNLOAD_NAME"));
				table.setEditValue(view.itemRow, "UNLOAD_AREA_ID", view.vm.getValueAsString("UNLOAD_AREA_ID"));
				table.setEditValue(view.itemRow, "UNLOAD_AREA_NAME", view.vm.getValueAsString("UNLOAD_AREA_NAME"));
				table.setEditValue(view.itemRow, "UNLOAD_AREA_ID2", view.vm.getValueAsString("UNLOAD_AREA_ID2"));
				table.setEditValue(view.itemRow, "UNLOAD_AREA_NAME2", view.vm.getValueAsString("UNLOAD_AREA_NAME2"));
				table.setEditValue(view.itemRow, "UNLOAD_AREA_ID3", view.vm.getValueAsString("UNLOAD_AREA_ID3"));
				table.setEditValue(view.itemRow, "UNLOAD_AREA_NAME3", view.vm.getValueAsString("UNLOAD_AREA_NAME3"));
				table.setEditValue(view.itemRow, "UNLOAD_ADDRESS", view.vm.getValueAsString("UNLOAD_ADDRESS"));
				table.setEditValue(view.itemRow, "UNLOAD_CONTACT", view.vm.getValueAsString("UNLOAD_CONTACT"));
				table.setEditValue(view.itemRow, "UNLOAD_TEL", view.vm.getValueAsString("UNLOAD_TEL"));
				table.setEditValue(view.itemRow, "UNLOAD_CODE", view.vm.getValueAsString("UNLOAD_CODE"));
			}
			/*if (bool){
				table.setEditValue(view.itemRow, "SKU_NAME", view.customerRecord.getAttribute("SKU_NAME"));
				table.setEditValue(view.itemRow,"SKU_ID",view.customerRecord.getAttribute("SKU_ID"));
				table.setEditValue(view.itemRow, "SKU", view.customerRecord.getAttribute("SKU"));
				table.setEditValue(view.itemRow, "SKU_SPEC", view.customerRecord.getAttribute("SKU_SPEC"));
				table.setEditValue(view.itemRow, "PACK_ID", view.customerRecord.getAttribute("PACK_ID"));
				table.setEditValue(view.itemRow, "UOM", view.customerRecord.getAttribute("TRANS_UOM"));
			}
			table.setEditValue(view.itemRow, "QNTY", "0");
			table.setEditValue(view.itemRow, "QNTY_EACH", "0");**/
		}else if(map != null){
			if("Y".equals(ObjUtil.ifNull(map.get("SKU_EDIT_FLAG"),"Y"))){
				table.getField("VOL").setCanEdit(true);
				table.getField("G_WGT").setCanEdit(true);
			}else{
				table.getField("VOL").setCanEdit(false);
				table.getField("G_WGT").setCanEdit(false);
			}
		}
		
//		view.enableOrDisables(view.save_map, true);
//		view.canButton.enable();
		view.enableOrDisables(view.add_map, true);
		view.enableOrDisables(view.del_map, false);
		view.enableOrDisables(view.sav_detail_map, true);
		view.enableOrDisables(view.del_detail_map, false);
	}

	@Override
	public void onClick(ClickEvent event) {
		table.OP_FLAG = "A";
//		if(view.vm.getValueAsString("ODR_NO") == null){
//			MSGUtil.sayError("订单主信息不存在，不能新增明细！");
//			return;
//		}
		
		if(!StaticRef.ORD_STATUS_CREATE.equals(view.vm.getValueAsString(("STATUS")))){
			MSGUtil.sayError("订单非【" + StaticRef.SO_CREATE_NAME + "】状态，不能新增明细！");
			return;
		}
		
//		if(!view.saveButton.isDisabled() && StaticRef.MOD_FLAG.equals(ObjUtil.ifNull(view.vm.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG))){
//			MSGUtil.sayError("订单主信息未保存，不能新增明细!");
//			return;
//		}
		
		/*if(main_table != null) {
			main_table.invalidateCache();
		}*/
		
		create(false);
	}
}
