package com.rd.client.action.settlement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.CustomFeeSettView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * 客户费用结算--费用保存按钮
 * @author fangliangmeng
 *
 */
public class SaveReceFeeAction implements ClickHandler {

	private ValuesManager form;
	
	private CustomFeeSettView view;
	
	private Map<String, Object> record;
	
	private String op_flag;
	
	private String proName="SP_SETT_SAV_REC_FEE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public SaveReceFeeAction(CustomFeeSettView view,ValuesManager form){
		this.view = view;
		this.form = form;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {	
		op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
        record = form.getValues(); 
        record.remove("OP_FLAG");
        
        Util.async.execProcedure(getList(), proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
					if(op_flag.equals("A")) {
//						ListGridRecord record = new ListGridRecord();
//						record.setAttribute("ID", result.substring(2));
//						form.setValue("ID", result.substring(2));
//						Util.updateToRecord(form, view.feeTable, record);   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
//						record.setAttribute("FEE_NAME",form.getItem("FEE_ID").getDisplayValue());
//						record.setAttribute("RECE_STAT_NAME","未核销");
//						ArrayList list = new ArrayList(Arrays.asList(view.feeTable.getRecords()));
//						list.add(0,record);
//						view.feeTable.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); //wangjun
						view.feeTable.invalidateCache();
						Criteria criteria = new Criteria();
						criteria.addCriteria("OP_FLAG","M");
						criteria.addCriteria("ODR_NO",view.table.getSelectedRecord().getAttributeAsString("ODR_NO"));
						criteria.addCriteria("DOC_NO",view.table.getSelectedRecord().getAttributeAsString("ODR_NO"));
						view.feeTable.fetchData(criteria);
//						view.feeTable.selectRecord(record);	
						form.setValue("OP_FLAG", StaticRef.MOD_FLAG);//wangjun 2010-12-8
						Util.db_async.getRecord("BILL_PRICE", "BMS_ORDER_HEADER", " where ODR_NO='"+view.table.getSelectedRecord().getAttribute("ODR_NO")+"'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {

							@Override
							public void onFailure(Throwable caught) {
								
							}

							@Override
							public void onSuccess(ArrayList<HashMap<String, String>> result) {
								if(result!=null&&result.size()>0){
									view.table.getSelectedRecord().setAttribute("BILL_PRICE", result.get(0).get("BILL_PRICE"));
									view.table.redraw();
								}
							}
							
						});
						if(view != null){
							view.initSaveBtn();
						}
						view.initLoadFeeBtn(4);
						view.disableFeeName();
					}
					else {
//						Util.updateToRecord(form, view.feeTable, view.feeTable.getSelectedRecord());   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
						//刷新选中的记录  //异常
						//view.feeTable.updateData(view.feeTable.getSelectedRecord());
						view.feeTable.invalidateCache();
						Criteria criteria = new Criteria();
						criteria.addCriteria("OP_FLAG","M");
						criteria.addCriteria("ODR_NO",view.table.getSelectedRecord().getAttributeAsString("ODR_NO"));
						criteria.addCriteria("DOC_NO",view.table.getSelectedRecord().getAttributeAsString("ODR_NO"));
						view.feeTable.fetchData(criteria);
						Util.db_async.getRecord("BILL_PRICE", "BMS_ORDER_HEADER", " where ODR_NO='"+view.table.getSelectedRecord().getAttribute("ODR_NO")+"'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {

							@Override
							public void onFailure(Throwable caught) {
								
							}

							@Override
							public void onSuccess(ArrayList<HashMap<String, String>> result) {
								if(result!=null&&result.size()>0){
									view.table.getSelectedRecord().setAttribute("BILL_PRICE", result.get(0).get("BILL_PRICE"));
									view.table.redraw();
								}
							}
							
						});
						view.initLoadFeeBtn(4);
						view.disableFeeName();
					}
					/*view.feeTable.invalidateCache();
					Criteria cc = new Criteria();
					cc.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
					cc.addCriteria("DOC_NO",record.get("DOC_NO").toString());
					view.feeTable.fetchData(cc);*/
				}else{
					MSGUtil.sayError(result.substring(2));
				}
				
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private ArrayList<String> getList(){
		ArrayList<String> list= new ArrayList<String>();
		if(op_flag.equals("A")) {
			list.add(null);
		}
		else {
			list.add(record.get("ID").toString());
		}
		list.add(record.get("DOC_NO").toString());
		list.add(record.get("FEE_ID").toString());
		list.add(form.getItem("FEE_ID").getDisplayValue());
		list.add(record.get("FEE_BAS").toString());
		list.add(record.get("BAS_VALUE").toString());
		list.add(String.valueOf(record.get("PRICE")));
		list.add(ObjUtil.ifObjNull(record.get("DUE_FEE"),"0").toString());
		list.add(ObjUtil.ifObjNull(record.get("PAY_FEE"),"0").toString());
		if(ObjUtil.isNotNull(record.get("PRE_RECE_TIME"))) {
			list.add(record.get("PRE_RECE_TIME").toString());
		}
		else {
			list.add(null);
		}
		list.add(ObjUtil.ifObjNull(record.get("DISCOUNT_RATE"),"1").toString());
		list.add(ObjUtil.ifObjNull(record.get("NOTES"),"").toString());
		list.add(op_flag);
		list.add(LoginCache.getLoginUser().getUSER_ID());
		
		return list;
	}

}
