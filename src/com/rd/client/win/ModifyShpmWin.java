package com.rd.client.win;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.action.base.address.AreaChangeAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.AddrDS;
import com.rd.client.ds.tms.ShpmDS2;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 托运单管理--列表右键 -- 汇总功能
 * @author fanglm
 *
 */
public class ModifyShpmWin extends Window{

	private ValuesManager vm;
	private DataSource ds;
	private SGTable table;
	private SGPanel panel;
	
	private Map<String, Object> record;
	
	public static interface CloseHandler{
		void onClosed(String message);
	}
	private List<CloseHandler> closeHandlers=new ArrayList<CloseHandler>();
	public void addCloseHandler(CloseHandler handler){
		closeHandlers.add(handler);
	}
	public void removeHandler(CloseHandler handler){
		closeHandlers.remove(handler);
	}
	public void fireCloseEvent(String message){
		for(CloseHandler handler:closeHandlers)
			handler.onClosed(message);
	} 
	
	public ModifyShpmWin(SGTable headTable,ListGridRecord[] p_cache_map,boolean bo){
		table = headTable;
		final ListGridRecord selectRecord = p_cache_map[0];
		vm = new ValuesManager();
		ds = ShpmDS2.getInstance("V_SHIPMENT_HEADER", "TRANS_SHIPMENT_HEADER");
		vm.setDataSource(ds);
		setTitle("作业单信息");
		setTop("25%");
		setLeft("25%");
        setWidth("980");
        setHeight("510");
		setCanDragResize(true);
		
		SGText SHPM_NO = new SGText("SHPM_NO", Util.TI18N.SHPM_NO());
		SHPM_NO.setDisabled(true);
		
		SGText CUSTOMER_NAME = new SGText("CUSTOMER_NAME", "客户");
		CUSTOMER_NAME.setDisabled(true);
		
		SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", ColorUtil.getRedTitle("订单类型"));
		Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP");
		
		SGCombo TRANS_TYPE = new SGCombo("ODR_TYP", ColorUtil.getRedTitle("运输类型"));
		Util.initCodesComboValue(TRANS_TYPE, "TRS_TYP");
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME", ColorUtil.getRedTitle(Util.TI18N.EXEC_ORG_ID()));
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "30%", "40%");
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO","客户订单号",true);//运单号
		SGText REFENENCE1 = new SGText("REFENENCE1","运单号"); //内单号
		SGCombo VEHICLE_TYP = new SGCombo("VEHICLE_TYP_ID","要求车型");
		Util.initComboValue(VEHICLE_TYP, "BAS_VEHICLE_TYPE","ID","VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'"," order by SHOW_SEQ ASC");
		final SGText TEMPERATURE1 = new SGText("TEMPERATURE1","温度下限");
		TEMPERATURE1.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(ObjUtil.isNotNull(TEMPERATURE1.getValue())){
					String regex = "^(-|\\+)?\\d+$";
		        	if(!TEMPERATURE1.getValue().toString().matches(regex)){
		        		SC.say("温度从只能输入整数");
		        		return;
		        	}
				}
			}
		});
		
		final SGText TEMPERATURE2 = new SGText("TEMPERATURE2","温度上限");
		TEMPERATURE2.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(ObjUtil.isNotNull(TEMPERATURE2.getValue())){
					String regex = "^(-|\\+)?\\d+$";
		        	if(!TEMPERATURE2.getValue().toString().matches(regex)){
		        		SC.say("温度到只能输入整数");
		        		return;
		        	}
				}
			}
		});
		
		
		SGDateTime ODR_TIME = new SGDateTime("ODR_TIME", "下单时间",true);
		ODR_TIME.setWidth(FormUtil.Width);
		//Util.initDateTime(panel, ODR_TIME);
		SGDateTime POD_TIME3 = new SGDateTime("FROM_LOAD_TIME1","要求发货时间");
		POD_TIME3.setWidth(FormUtil.Width);
		//Util.initDateTime(panel, POD_TIME3);

		SGDateTime POD_TIME4 = new SGDateTime("PRE_LOAD_TIME1", "到");
		POD_TIME4.setWidth(FormUtil.Width);
		//Util.initDateTime(panel, POD_TIME4);

		SGDateTime POD_TIME5 = new SGDateTime("FROM_UNLOAD_TIME1", "要求收货时间");
		POD_TIME5.setWidth(FormUtil.Width);
		//Util.initDateTime(panel, POD_TIME5);
		SGDateTime POD_TIME6 = new SGDateTime("PRE_UNLOAD_TIME1", "到");
		POD_TIME6.setWidth(FormUtil.Width);
		//Util.initDateTime(panel, POD_TIME6);
		
		
		SGDateTime REQ_LOAD_TIME = new SGDateTime("PRE_LOAD_TIME","要求发货时间");
		REQ_LOAD_TIME.setWidth(FormUtil.Width);
		//Util.initDateTime(panel, REQ_LOAD_TIME);
		
		SGDateTime REQ_UNLOAD_TIME = new SGDateTime("PRE_UNLOAD_TIME","要求到货时间");
		REQ_UNLOAD_TIME.setWidth(FormUtil.Width);
		//Util.initDateTime(panel, REQ_UNLOAD_TIME);
		
		SGText DISCOUNT = new SGText("DISCOUNT", "重泡比");
		SGText TOT_WORTH = new SGText("GOODS_WORTH", "货值");
		SGCombo ORD_PRO_LEVER=new SGCombo("UGRT_GRD","优先级");//优先级
		Util.initCodesComboValue(ORD_PRO_LEVER, "UGRT_GRD");
		
		SGLText notes = new SGLText("NOTES", Util.TI18N.NOTES());
		SGCheck UNLOAD_FLAG = new SGCheck("UNLOAD_FLAG", "需卸货",true);
		UNLOAD_FLAG.setTitleOrientation(TitleOrientation.LEFT);
		UNLOAD_FLAG.setColSpan(1);
		SGCheck BUK_FLAG = new SGCheck("BUK_FLAG", "粗订单");
		BUK_FLAG.setTitleOrientation(TitleOrientation.LEFT);
		BUK_FLAG.setColSpan(1);
		SGCheck SLF_PICKUP_FLAG = new SGCheck("SLF_PICKUP_FLAG", Util.TI18N.SLF_PICKUP_FLAG());
		SLF_PICKUP_FLAG.setTitleOrientation(TitleOrientation.LEFT);
		SLF_PICKUP_FLAG.setColSpan(1);
		SGCheck SLF_DELIVER_FLAG = new SGCheck("SLF_DELIVER_FLAG", Util.TI18N.SLF_DELIVER_FLAG());
		SLF_DELIVER_FLAG.setTitleOrientation(TitleOrientation.LEFT);
		SLF_DELIVER_FLAG.setColSpan(1);

		final SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID", ColorUtil.getRedTitle(Util.TI18N.TRANS_SRVC_ID()));
		TRANS_SRVC_ID.setVisible(false);
		
		SGLText TRANS_DEMAND = new SGLText("TRANS_DEMAND", "客户运输要求");
		
		SGCombo AREA_ID = new SGCombo("LOAD_AREA_ID",Util.TI18N.PROVINCE(),true);
		SGCombo AREA_ID2 = new SGCombo("LOAD_AREA_ID2",Util.TI18N.CITY());
		SGCombo AREA_ID3 = new SGCombo("LOAD_AREA_ID3",Util.TI18N.AREA());
		SGText LOAD_AREA_NAME = new SGText("LOAD_AREA_NAME",Util.TI18N.PROVINCE());
		LOAD_AREA_NAME.setVisible(false);
		SGText LOAD_AREA_NAME2 = new SGText("LOAD_AREA_NAME2",Util.TI18N.CITY());
		LOAD_AREA_NAME2.setVisible(false);
		final SGText LOAD_AREA_NAME3 = new SGText("LOAD_AREA_NAME3",Util.TI18N.AREA());
		LOAD_AREA_NAME3.setVisible(false);
		
		TextItem LOAD_ID = new TextItem("LOAD_ID");
		LOAD_ID.setVisible(false);
		
		final ComboBoxItem LOAD_NAME = new ComboBoxItem("LOAD_NAME", Util.TI18N.LOAD_NAME());
		LOAD_NAME.setWidth(FormUtil.longWidth);
		LOAD_NAME.setColSpan(4);
		LOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		
		SGLText LOAD_ADDRESS = new SGLText("LOAD_ADDRESS", Util.TI18N.LOAD_ADDRESS(),true);

		
		// 2 发货联系人，联系电话 ， 计划发货时间，客户自送
		SGText LOAD_CONTACT = new SGText("LOAD_CONTACT",Util.TI18N.CONT_NAME());
		SGText LOAD_TEL = new SGText("LOAD_TEL", Util.TI18N.CONT_TEL());
//		if("1".equals(selectRecord.getAttribute("TRANS_SRVC_ID"))){
//			SLF_DELIVER_FLAG.setDisabled(false);
//		}else{
//			SLF_DELIVER_FLAG.setDisabled(true);
//		}

		// 4 收货区域 ，收货方 ，收货地址
		SGCombo AREA_ID4 = new SGCombo("UNLOAD_AREA_ID",Util.TI18N.PROVINCE(),true);
		SGCombo AREA_ID5 = new SGCombo("UNLOAD_AREA_ID2",Util.TI18N.CITY());
		SGCombo AREA_ID6 = new SGCombo("UNLOAD_AREA_ID3",Util.TI18N.AREA());
		SGText AREA_NAME4 = new SGText("UNLOAD_AREA_NAME",Util.TI18N.PROVINCE());
        AREA_NAME4.setVisible(false);
        SGText AREA_NAME5 = new SGText("UNLOAD_AREA_NAME2",Util.TI18N.CITY());
        AREA_NAME5.setVisible(false);
        final SGText AREA_NAME6 = new SGText("UNLOAD_AREA_NAME3",Util.TI18N.AREA());
        AREA_NAME6.setVisible(false);
		
		TextItem UNLOAD_ID = new TextItem("UNLOAD_ID");
		UNLOAD_ID.setVisible(false);
		
		final ComboBoxItem UNLOAD_NAME = new ComboBoxItem("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());
		UNLOAD_NAME.setWidth(FormUtil.longWidth);
		UNLOAD_NAME.setColSpan(4);
		UNLOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
		SGLText UNLOAD_ADDRESS = new SGLText("UNLOAD_ADDRESS",Util.TI18N.UNLOAD_ADDRESS(),true);
		// 5 收货联系人，联系电话， 计划收货时间，客户自提
		SGText UNLOAD_CONTACT = new SGText("UNLOAD_CONTACT",Util.TI18N.CONT_NAME());
		SGText UNLOAD_TEL = new SGText("UNLOAD_TEL", Util.TI18N.CONT_TEL());
		
		SGCheck DIRECT_FLAG = new SGCheck("UDF5", Util.TI18N.SHPM_UDF5(),true);
		DIRECT_FLAG.setVisible(bo);
//		if ("2".equals(selectRecord.getAttribute("TRANS_SRVC_ID"))) {
//			DIRECT_FLAG.setDisabled(false);
//		}else {
//			DIRECT_FLAG.setDisabled(true);
//		}
		
//		if("3".equals(selectRecord.getAttribute("TRANS_SRVC_ID"))){
//			SLF_PICKUP_FLAG.setDisabled(false);
//		}else{
//			SLF_PICKUP_FLAG.setDisabled(true);
//		}
		
		Util.initComboValue(AREA_ID, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " AREA_LEVEL = '3'", "");
		Util.initComboValue(AREA_ID2, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("LOAD_AREA_ID") + "'", "");
		Util.initComboValue(AREA_ID3, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("LOAD_AREA_ID2") + "'", "");
		AREA_ID.addChangedHandler(new AreaChangeAction(LOAD_AREA_NAME,AREA_ID2,LOAD_AREA_NAME2));
        AREA_ID2.addChangedHandler(new AreaChangeAction(LOAD_AREA_NAME2,AREA_ID3,LOAD_AREA_NAME3));
        AREA_ID3.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem = (SGCombo)event.getSource();
				String value = fatherItem.getDisplayValue();
				if(ObjUtil.isNotNull(value) && LOAD_AREA_NAME3 != null){
					LOAD_AREA_NAME3.setValue(value);
				}
			}
        	
        });
		
		initUnLoadId(LOAD_NAME, LOAD_ADDRESS, LOAD_ID, AREA_ID, LOAD_AREA_NAME, AREA_ID2, LOAD_AREA_NAME2, AREA_ID3, LOAD_AREA_NAME3, LOAD_CONTACT, LOAD_TEL);
		
		
		
		Util.initComboValue(AREA_ID4, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " AREA_LEVEL = '3'", "");
		Util.initComboValue(AREA_ID5, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("UNLOAD_AREA_ID") + "'", "");
		Util.initComboValue(AREA_ID6, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("UNLOAD_AREA_ID2") + "'", "");
		AREA_ID4.addChangedHandler(new AreaChangeAction(AREA_NAME4,AREA_ID5,AREA_NAME5));
        AREA_ID5.addChangedHandler(new AreaChangeAction(AREA_NAME5,AREA_ID6,AREA_NAME6));
        AREA_ID6.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem = (SGCombo)event.getSource();
				String value = fatherItem.getDisplayValue();
				if(ObjUtil.isNotNull(value) && AREA_NAME6 != null){
					AREA_NAME6.setValue(value);
				}
			}
        	
        });
		
		//初始化收货方下拉框
		initUnLoadId(UNLOAD_NAME, UNLOAD_ADDRESS, UNLOAD_ID, AREA_ID4, AREA_NAME4, AREA_ID5, AREA_NAME5, AREA_ID6, AREA_NAME6, UNLOAD_CONTACT, UNLOAD_TEL);
		
		panel = new SGPanel();
		panel.setNumCols(10);
		panel.setWidth("40%");
		panel.setHeight100();
		panel.setTitleWidth(75);
		panel.setItems(SHPM_NO,CUSTOMER_NAME,BIZ_TYP,TRANS_TYPE,EXEC_ORG_ID_NAME,CUSTOM_ODR_NO,REFENENCE1,VEHICLE_TYP,
				TEMPERATURE1,TEMPERATURE2,ODR_TIME,POD_TIME3,POD_TIME4,POD_TIME5,POD_TIME6,REQ_LOAD_TIME,REQ_UNLOAD_TIME,TOT_WORTH,DISCOUNT,
				ORD_PRO_LEVER,TRANS_DEMAND,notes,UNLOAD_FLAG,BUK_FLAG,SLF_PICKUP_FLAG,SLF_DELIVER_FLAG,
				EXEC_ORG_ID,TRANS_SRVC_ID,AREA_ID,AREA_ID2,AREA_ID3,LOAD_AREA_NAME,LOAD_AREA_NAME2,LOAD_AREA_NAME3,LOAD_ID, LOAD_NAME, LOAD_ADDRESS,LOAD_CONTACT, LOAD_TEL,
				AREA_ID4,AREA_NAME4,AREA_ID5,AREA_NAME5,AREA_ID6,AREA_NAME6, UNLOAD_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT, UNLOAD_TEL
                ,UNLOAD_ID);
		
		vm.addMember(panel);
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setHeight(28);
		toolStrip.setPadding(2);
		toolStrip.setAlign(Alignment.LEFT);
        toolStrip.setSeparatorSize(12);
		toolStrip.setMembersMargin(4);
		IButton confirmButton = new IButton(Util.BI18N.CONFIRM());
		confirmButton.setIcon(StaticRef.ICON_SAVE);
		confirmButton.setAutoFit(true);
		toolStrip.addMember(confirmButton);
		IButton cancelButton = new IButton(Util.BI18N.CANCEL());
		cancelButton.setIcon(StaticRef.ICON_CANCEL);
		cancelButton.setAutoFit(true);
		toolStrip.addMember(cancelButton);
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		confirmButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(ClickEvent event) {
				record = vm.getValues(); 
				final ArrayList<String> paramList = new ArrayList<String>();
				paramList.add((String)record.get("SHPM_NO"));
				Map<String, Object> update = new HashMap<String, Object>(); 
				update.put("TABLE", "TRANS_SHIPMENT_HEADER");
				update.put("ID", record.get("ID"));
				update.put("BIZ_TYP", record.get("BIZ_TYP"));
				update.put("ODR_TYP", record.get("ODR_TYP"));
				update.put("EXEC_ORG_ID", record.get("EXEC_ORG_ID"));
				update.put("EXEC_ORG_ID_NAME", record.get("EXEC_ORG_ID_NAME"));
				update.put("CUSTOM_ODR_NO", record.get("CUSTOM_ODR_NO"));
				update.put("REFENENCE1", record.get("REFENENCE1"));
				update.put("VEHICLE_TYP_ID", record.get("VEHICLE_TYP_ID"));
				update.put("TEMPERATURE1", record.get("TEMPERATURE1"));
				update.put("TEMPERATURE2", record.get("TEMPERATURE2"));
				update.put("DISCOUNT", record.get("DISCOUNT"));
				update.put("GOODS_WORTH", record.get("GOODS_WORTH"));
				update.put("UGRT_GRD", record.get("UGRT_GRD"));
				update.put("NOTES", record.get("NOTES"));
				update.put("TRANS_DEMAND", record.get("TRANS_DEMAND"));
				update.put("LOAD_AREA_ID", record.get("LOAD_AREA_ID"));
				update.put("LOAD_AREA_NAME", record.get("LOAD_AREA_NAME"));
				update.put("LOAD_AREA_ID2", record.get("LOAD_AREA_ID2"));
				update.put("LOAD_AREA_NAME2", record.get("LOAD_AREA_NAME2"));
				update.put("LOAD_AREA_ID3", record.get("LOAD_AREA_ID3"));
				update.put("LOAD_AREA_NAME3", record.get("LOAD_AREA_NAME3"));
				update.put("LOAD_ID", record.get("LOAD_ID"));
				update.put("LOAD_NAME", record.get("LOAD_NAME"));
				update.put("LOAD_ADDRESS", record.get("LOAD_ADDRESS"));
				update.put("LOAD_CONTACT", record.get("LOAD_CONTACT"));
				update.put("LOAD_TEL", record.get("LOAD_TEL"));
				update.put("UNLOAD_AREA_ID", record.get("UNLOAD_AREA_ID"));
				update.put("UNLOAD_AREA_NAME", record.get("UNLOAD_AREA_NAME"));
				update.put("UNLOAD_AREA_ID2", record.get("UNLOAD_AREA_ID2"));
				update.put("UNLOAD_AREA_NAME2", record.get("UNLOAD_AREA_NAME2"));
				update.put("UNLOAD_AREA_ID3", record.get("UNLOAD_AREA_ID3"));
				update.put("UNLOAD_AREA_NAME3", record.get("UNLOAD_AREA_NAME3"));
				update.put("UNLOAD_ID", record.get("UNLOAD_ID"));
				update.put("UNLOAD_NAME", record.get("UNLOAD_NAME"));
				update.put("UNLOAD_ADDRESS", record.get("UNLOAD_ADDRESS"));
				update.put("UNLOAD_CONTACT", record.get("UNLOAD_CONTACT"));
				update.put("UNLOAD_TEL", record.get("UNLOAD_TEL"));
				update.put("BUK_FLAG", record.get("BUK_FLAG"));
				update.put("UNLOAD_FLAG", record.get("UNLOAD_FLAG"));
				update.put("SLF_PICKUP_FLAG", record.get("SLF_PICKUP_FLAG"));
				update.put("SLF_DELIVER_FLAG", record.get("SLF_DELIVER_FLAG"));
				Boolean pickupFlag = Boolean.valueOf(selectRecord.getAttribute("SLF_PICKUP_FLAG"));
				Boolean pickupNewFlag = Boolean.valueOf(record.get("SLF_PICKUP_FLAG").toString());
				if(pickupNewFlag != null){//原勾着的
					pickupFlag = pickupNewFlag;
				}
				//String flag = pickupFlag?"Y":"N";
				//paramList.add(flag);
				Boolean deliverFlag = Boolean.valueOf(selectRecord.getAttribute("SLF_DELIVER_FLAG"));
				Boolean deliverNewFlag = Boolean.valueOf(record.get("SLF_DELIVER_FLAG").toString());
				if(deliverNewFlag != null){//原勾着的
					deliverFlag = deliverNewFlag;
				}
				//paramList.add(deliverFlag?"Y":"N");
				Boolean bukflag = Boolean.valueOf(selectRecord.getAttribute("BUK_FLAG"));
				Boolean bukNewFlag = Boolean.valueOf(record.get("BUK_FLAG").toString());
				if(bukNewFlag != null){//原勾着的
					bukflag = bukNewFlag;
				}
				Boolean unloadflag = Boolean.valueOf(selectRecord.getAttribute("UNLOAD_FLAG"));
				Boolean unloadNewFlag = Boolean.valueOf(record.get("UNLOAD_FLAG").toString());
				if(unloadNewFlag != null){//原勾着的
					unloadflag = unloadNewFlag;
				}
				String fromUnloadTime = ObjUtil.ifObjNull(record.get("PRE_LOAD_TIME"), "").toString();
				String preUnloadTime = ObjUtil.ifObjNull(record.get("PRE_UNLOAD_TIME"), "").toString();
				String odrTime = ObjUtil.ifObjNull(record.get("ODR_TIME"), "").toString();
				if(ObjUtil.isNotNull(fromUnloadTime)){
					update.put("PRE_LOAD_TIME", "to_date('"+fromUnloadTime+"', 'yyyy/mm/dd hh24:mi:ss')");
				}
				if(ObjUtil.isNotNull(preUnloadTime)){
					update.put("PRE_UNLOAD_TIME", "to_date('"+preUnloadTime+"', 'yyyy/mm/dd hh24:mi:ss')");
				}
				if(ObjUtil.isNotNull(odrTime)){
					update.put("ODR_TIME", "to_date('"+odrTime+"', 'yyyy/mm/dd hh24:mi:ss')");
				}
				String json = Util.mapToJson(update);
				ArrayList<String> sqlList = new ArrayList<String>();
				sqlList.add(json);
				//paramList.add((String)record.get("UNLOAD_ID"));
				//paramList.add(record.get("UDF5").toString());
				paramList.add(LoginCache.getLoginUser(). getUSER_ID());
				final boolean SLF_PICKUP_FLAG = pickupFlag.booleanValue();
				final boolean SLF_DELIVER_FLAG = deliverFlag.booleanValue();
				final boolean BUK_FLAG = bukflag.booleanValue();
				final boolean UNLOAD_FLAG = unloadflag.booleanValue();
				Util.async.doUpdate(null, sqlList, new AsyncCallback<String>() {
					
					@Override
					public void onFailure(Throwable caught) {
						MSGUtil.sayError(caught.getMessage());
					}
			
					@Override
					public void onSuccess(String result) {
						if(result.equals(StaticRef.SUCCESS_CODE)) {
//							MSGUtil.showOperSuccess();
//							ListGridRecord rec = table.getSelection()[0];
//							rec.setAttribute("LOAD_ADDRESS", record.get("LOAD_ADDRESS"));
//							rec.setAttribute("LOAD_CONTACT", record.get("LOAD_CONTACT"));
//							rec.setAttribute("LOAD_TEL", record.get("LOAD_TEL"));
//							rec.setAttribute("UNLOAD_AREA_ID", record.get("UNLOAD_AREA_ID"));
//							rec.setAttribute("UNLOAD_AREA_NAME", record.get("UNLOAD_AREA_NAME"));
//							rec.setAttribute("UNLOAD_AREA_ID2", record.get("UNLOAD_AREA_ID2"));
//							rec.setAttribute("UNLOAD_AREA_NAME2", record.get("UNLOAD_AREA_NAME2"));
//							rec.setAttribute("UNLOAD_AREA_ID3", record.get("UNLOAD_AREA_ID3"));
//							rec.setAttribute("UNLOAD_AREA_NAME3", record.get("UNLOAD_AREA_NAME3"));
//							rec.setAttribute("UNLOAD_ID", record.get("UNLOAD_ID"));
//							rec.setAttribute("UNLOAD_NAME", record.get("UNLOAD_NAME"));
//							rec.setAttribute("UNLOAD_ADDRESS", record.get("UNLOAD_ADDRESS"));
//							rec.setAttribute("UNLOAD_CONTACT", record.get("UNLOAD_CONTACT"));
//							rec.setAttribute("UNLOAD_TEL", record.get("UNLOAD_TEL"));
//							rec.setAttribute("UDF5", record.get("UDF5"));
//							rec.setAttribute("SLF_PICKUP_FLAG", record.get("SLF_PICKUP_FLAG"));
//							rec.setAttribute("SLF_DELIVER_FLAG", record.get("SLF_DELIVER_FLAG"));
//							rec.setAttribute("FROM_UNLOAD_TIME", record.get("FROM_UNLOAD_TIME"));
//							rec.setAttribute("PRE_UNLOAD_TIME", record.get("PRE_UNLOAD_TIME"));
//							table.updateData(rec);
//							table.redraw();
							
							Util.async.execProcedure(paramList, "SP_SHPM_SLF_MODIFY(?,?)", new AsyncCallback<String>() {
								
								@Override
								public void onFailure(Throwable caught) {
									
								}

								@Override
								public void onSuccess(String result) {
									if(result.equals(StaticRef.SUCCESS_CODE)) {
										getThis().hide();
										final ListGridRecord rec = table.getSelection()[0];
										rec.setAttribute("BIZ_TYP", record.get("BIZ_TYP"));
										rec.setAttribute("ODR_TYP", record.get("ODR_TYP"));
										rec.setAttribute("EXEC_ORG_ID", record.get("EXEC_ORG_ID"));
										rec.setAttribute("EXEC_ORG_ID_NAME", record.get("EXEC_ORG_ID_NAME"));
										rec.setAttribute("CUSTOM_ODR_NO", record.get("CUSTOM_ODR_NO"));
										rec.setAttribute("REFENENCE1", record.get("REFENENCE1"));
										rec.setAttribute("VEHICLE_TYP_ID", record.get("VEHICLE_TYP_ID"));
										rec.setAttribute("TEMPERATURE1", record.get("TEMPERATURE1"));
										rec.setAttribute("TEMPERATURE2", record.get("TEMPERATURE2"));
										rec.setAttribute("DISCOUNT", record.get("DISCOUNT"));
										rec.setAttribute("GOODS_WORTH", record.get("GOODS_WORTH"));
										rec.setAttribute("UGRT_GRD", record.get("UGRT_GRD"));
										rec.setAttribute("NOTES", record.get("NOTES"));
										rec.setAttribute("TRANS_DEMAND", record.get("TRANS_DEMAND"));
										rec.setAttribute("LOAD_AREA_ID", record.get("LOAD_AREA_ID"));
										rec.setAttribute("LOAD_AREA_NAME", record.get("LOAD_AREA_NAME"));
										rec.setAttribute("LOAD_AREA_ID2", record.get("LOAD_AREA_ID2"));
										rec.setAttribute("LOAD_AREA_NAME2", record.get("LOAD_AREA_NAME2"));
										rec.setAttribute("LOAD_AREA_ID3", record.get("LOAD_AREA_ID3"));
										rec.setAttribute("LOAD_AREA_NAME3", record.get("LOAD_AREA_NAME3"));
										rec.setAttribute("LOAD_ID", record.get("LOAD_ID"));
										rec.setAttribute("LOAD_NAME", record.get("LOAD_NAME"));
										rec.setAttribute("LOAD_ADDRESS", record.get("LOAD_ADDRESS"));
										rec.setAttribute("LOAD_CONTACT", record.get("LOAD_CONTACT"));
										rec.setAttribute("LOAD_TEL", record.get("LOAD_TEL"));
										rec.setAttribute("UNLOAD_AREA_ID", record.get("UNLOAD_AREA_ID"));
										rec.setAttribute("UNLOAD_AREA_NAME", record.get("UNLOAD_AREA_NAME"));
										rec.setAttribute("UNLOAD_AREA_ID2", record.get("UNLOAD_AREA_ID2"));
										rec.setAttribute("UNLOAD_AREA_NAME2", record.get("UNLOAD_AREA_NAME2"));
										rec.setAttribute("UNLOAD_AREA_ID3", record.get("UNLOAD_AREA_ID3"));
										rec.setAttribute("UNLOAD_AREA_NAME3", record.get("UNLOAD_AREA_NAME3"));
										rec.setAttribute("UNLOAD_ID", record.get("UNLOAD_ID"));
										rec.setAttribute("UNLOAD_NAME", record.get("UNLOAD_NAME"));
										rec.setAttribute("UNLOAD_ADDRESS", record.get("UNLOAD_ADDRESS"));
										rec.setAttribute("UNLOAD_CONTACT", record.get("UNLOAD_CONTACT"));
										rec.setAttribute("UNLOAD_TEL", record.get("UNLOAD_TEL"));
										rec.setAttribute("SLF_PICKUP_FLAG", SLF_PICKUP_FLAG);
										rec.setAttribute("SLF_DELIVER_FLAG", SLF_DELIVER_FLAG);
										rec.setAttribute("BUK_FLAG", BUK_FLAG);
										rec.setAttribute("UNLOAD_FLAG", UNLOAD_FLAG);
										rec.setAttribute("PRE_LOAD_TIME", record.get("PRE_LOAD_TIME"));
										rec.setAttribute("PRE_UNLOAD_TIME", record.get("PRE_UNLOAD_TIME"));
										rec.setAttribute("ODR_TIME", record.get("ODR_TIME"));
										
										Util.updateToRecord(vm, table, rec);
										Util.async.queryData("select STATUS_NAME, LOAD_NO from " +
												"trans_shipment_header where shpm_no = '"+
												selectRecord.getAttribute("SHPM_NO")+"'", false, new AsyncCallback<Map<String,Object>>() {
											@Override
											public void onFailure(Throwable caught) {
												
											}
											@Override
											public void onSuccess(Map<String, Object> result) {
												List<List<String>> list = (List<List<String>>)result.get("data");
												if(list == null || list.isEmpty()){
													return;
												}
												rec.setAttribute("STATUS_NAME", list.get(0).get(0));
												rec.setAttribute("LOAD_NO", list.get(0).get(1));
												table.redraw();
											}
										});
//										table.updateData(rec);
										MSGUtil.sayInfo("更新成功!");
									}else{
										MSGUtil.sayError(result);
									}
								}
								
							});
							/**
							HashMap<String, Object> shpm_map = new HashMap<String, Object>(); //托运单 
							HashMap<String, Object> unload_map = new HashMap<String, Object>(); //作业单   
							HashMap<String, Object> udf5_map = new HashMap<String, Object>(); //车辆   
							HashMap<String, Object> listmap = new HashMap<String, Object>();
							shpm_map.put("1", record.get("SHPM_NO"));
							unload_map.put("1", record.get("UNLOAD_ID"));
							udf5_map.put("1", record.get("UDF5"));
							listmap.put("1", shpm_map);
							listmap.put("2", unload_map);
							listmap.put("3", udf5_map);
							listmap.put("4", LoginCache.getLoginUser(). getUSER_ID());
							String json = Util.mapToJson(listmap);
							Util.async.execProcedure(json, "SP_SHPM_MODIFY(?,?,?,?,?)", new AsyncCallback<String>() {
	
								@Override
								public void onFailure(Throwable caught) {
									
								}

								@Override
								public void onSuccess(String result) {
									if(result.equals(StaticRef.SUCCESS_CODE)) {
										MSGUtil.sayInfo("更新签收机构成功!");
									}
								}
								
							});
							**/
						}
						else {
							MSGUtil.sayError(result);
						}
					}
				});
				sqlList.add(json);
			}
		});
		
		vm.addMember(panel);
		vm.editRecord(selectRecord);
		//SHPM_NO.setValue(Util.iff(main_map[0].getAttribute("SHPM_NO"),"").toString());
		//CUSTOM_ODR_NO.setValue(Util.iff(main_map[0].getAttribute("CUSTOM_ODR_NO"),"").toString());
		SLF_DELIVER_FLAG.setValue(Boolean.valueOf(selectRecord.getAttribute("SLF_DELIVER_FLAG")));
		DIRECT_FLAG.setValue(Boolean.valueOf(selectRecord.getAttribute("UDF5")));
		SLF_PICKUP_FLAG.setValue(Boolean.valueOf(selectRecord.getAttribute("SLF_PICKUP_FLAG")));
		
		addItem(panel);
		addItem(toolStrip);
		draw();
	}
	
	public ModifyShpmWin getThis(){
		return this;
	}
	
	//初始化收货方下拉框
	//初始化收货方下拉框
	private void initUnLoadId(final ComboBoxItem load_name,final SGLText address,final TextItem load_id
			,final SGCombo load_area_id,final TextItem load_area_name
			,final SGCombo load_area_id2,final TextItem load_area_name2
			,final SGCombo load_area_id3,final TextItem load_area_name3
			,final SGText cont_name,final SGText cont_tel){
		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
		
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField ADDRESS = new ListGridField("ADDRESS");
		load_name.setOptionDataSource(ds2);  
		load_name.setDisabled(false);
		load_name.setShowDisabled(false);
		load_name.setDisplayField("FULL_INDEX");
		load_name.setPickListBaseStyle("myBoxedGridCell");
		load_name.setPickListWidth(450);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("AND_RECV_FLAG","Y");
		criteria.addCriteria("ENABLE_FLAG","Y");
		load_name.setPickListCriteria(criteria);
		
		load_name.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,ADDRESS);
		load_name.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Record selectedRecord  = load_name.getSelectedRecord();
				if(selectedRecord != null){
					Util.initComboValue(load_area_id2, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID") + "'", "");
					Util.initComboValue(load_area_id3, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID2") + "'", "");
					load_name.setValue(selectedRecord.getAttribute("ADDR_NAME"));
					address.setValue(selectedRecord.getAttribute("ADDRESS"));
					load_area_name.setValue(selectedRecord.getAttribute("AREA_ID_NAME"));
					load_area_id.setValue(selectedRecord.getAttribute("AREA_ID"));
					load_area_name2.setValue(selectedRecord.getAttribute("AREA_NAME2"));
					load_area_id2.setValue(selectedRecord.getAttribute("AREA_ID2"));
					load_area_name3.setValue(selectedRecord.getAttribute("AREA_NAME3"));
					load_area_id3.setValue(selectedRecord.getAttribute("AREA_ID3"));
					load_id.setValue(selectedRecord.getAttribute("ID"));
					cont_name.setValue(selectedRecord.getAttribute("CONT_NAME"));
					cont_tel.setValue(selectedRecord.getAttribute("CONT_TEL"));
				}
			}
		});
	}
}