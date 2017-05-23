package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.ShpmDetailDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 调度配载->已调作业单->移除明细
 * @author yuanlei
 *
 */
public class SplitShpmItemWin extends Window{
	private int width = 1024;
	private int height = 298;
	public Window window = null;
	private String shpm_no;
	private String custom_odr_no;
	private SGTable table;
	private SGTable load_table;  //调度单
	private SGTable unshpm_table;  //待调
	private SGTable shpm_table;    //已调
	//private DynamicForm sumForm;
	private DynamicForm pageForm;

	public SplitShpmItemWin(SGTable p_load_table, SGTable p_unshpm_table, SGTable p_shpm_table, DynamicForm p_sumForm, DynamicForm p_pageForm){
		load_table = p_load_table;
		unshpm_table = p_unshpm_table;
		shpm_table = p_shpm_table;
		//sumForm = p_sumForm;
		pageForm = p_pageForm;
	}

	public Window getViewPanel() {
		
		ListGridRecord rec = shpm_table.getSelectedRecord();
		shpm_no = rec.getAttribute("SHPM_NO");
		custom_odr_no = rec.getAttribute("CUSTOM_ODR_NO");
		
		VLayout layout = new VLayout();  
        layout.setWidth100();  
        layout.setHeight100();  
  
        DataSource detailDS = ShpmDetailDS.getInstance("V_SHIPMENT_ITEM","TRANS_SHIPMENT_ITEM");
        table = new SGTable(detailDS, "100%",
				"200", false, true, false);
		table.setCanEdit(true);
		
		table.setAlign(Alignment.RIGHT);
		table.setShowRowNumbers(false);
		table.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		table.setAutoFitData(Autofit.VERTICAL);

		ListGridField SHPM_ROW = new ListGridField("SHPM_ROW",Util.TI18N.SHPM_ROW(),50);
		SHPM_ROW.setCanEdit(false);
		ListGridField SKU_ID = new ListGridField("SKU_CODE",Util.TI18N.SKU_ID(),60);
		SKU_ID.setCanEdit(false);
		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),120);
		SKU_NAME.setCanEdit(false);
		ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),60);
		SKU_SPEC.setCanEdit(false);
		ListGridField UOM = new ListGridField("UOM",Util.TI18N.UOM(),40);
		UOM.setCanEdit(false);
		ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.SHPM_QNTY(),60);
		Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
		QNTY.addEditorExitHandler(new EditorExitHandler() {

			@Override
			public void onEditorExit(EditorExitEvent event) {
				if(event != null) {
					int row = event.getRowNum();
					ListGridRecord rec = table.getRecord(row);
					if(ObjUtil.isNotNull(event.getNewValue())) {
						double cur_qnty = Double.parseDouble(ObjUtil.ifObjNull(event.getNewValue(),0).toString());
						double init_qnty = Double.parseDouble(rec.getAttribute("QNTY").toString());
						double rate = getRate(cur_qnty, init_qnty);
						if(cur_qnty <= 0) {
							SC.warn("无效拆分,数量不能小于或等于0!");
							table.setEditValue(row, "QNTY", rec.getAttribute("QNTY"));
							table.setEditValue(row, "VOL", rec.getAttribute("VOL"));
							table.setEditValue(row, "G_WGT", rec.getAttribute("G_WGT"));
							table.setEditValue(row, "N_WGT", rec.getAttribute("N_WGT"));
							return;
						}
						else if(rate > 1) {
							SC.warn("无效拆分,数量不能大于原单量!");
							table.setEditValue(row, "QNTY", rec.getAttribute("QNTY"));
							table.setEditValue(row, "VOL", rec.getAttribute("VOL"));
							table.setEditValue(row, "G_WGT", rec.getAttribute("G_WGT"));
							table.setEditValue(row, "N_WGT", rec.getAttribute("N_WGT"));
							return;
						}
						if(cur_qnty > 0 && rate > 0) {
						    double vol = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("VOL"),"0").toString());
							double g_wgt = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("G_WGT"),"0").toString());
							double n_wgt = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("N_WGT"),"0").toString());
			
							table.setEditValue(row, "VOL", rate*vol);
							table.setEditValue(row, "G_WGT", rate*g_wgt);
							table.setEditValue(row, "N_WGT", rate*n_wgt);
						}
					}
				}
			}
			
		});
		
		ListGridField ODR_QNTY = new ListGridField("ODR_QNTY",Util.TI18N.ODR_QNTY(),60);
		ODR_QNTY.setCanEdit(false);
		Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
		
		ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.LD_QNTY(),60);
		LD_QNTY.setCanEdit(false);
		Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
		
		ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY",Util.TI18N.UNLD_QNTY(),60);
		Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
		UNLD_QNTY.setCanEdit(false);
		
		ListGridField G_WGT = new ListGridField("G_WGT",Util.TI18N.G_WGT(),50);
		Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
		G_WGT.setCanEdit(false);
		
		ListGridField VOL = new ListGridField("VOL",Util.TI18N.VOL(),70);
		Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
		VOL.setCanEdit(false);
		
		ListGridField TOT_QNTY_EACH = new ListGridField("QNTY_EACH",Util.TI18N.R_EA(),60);
		TOT_QNTY_EACH.setAlign(Alignment.RIGHT); 
		TOT_QNTY_EACH.setCanEdit(false);
		Util.initFloatListField(TOT_QNTY_EACH, StaticRef.QNTY_FLOAT);
		
		ListGridField LOTATT02 =new ListGridField("LOTATT02",Util.TI18N.LOTATT02(),90);
		LOTATT02.setCanEdit(false);
		
		ListGridField LOT_ID =new ListGridField("LOT_ID",Util.TI18N.LOT_ID(),90);
		LOT_ID.setCanEdit(false);
		
		table.setFields(SHPM_ROW,SKU_ID,SKU_NAME,SKU_SPEC,UOM,ODR_QNTY,QNTY,TOT_QNTY_EACH,LD_QNTY,UNLD_QNTY,G_WGT,VOL,LOTATT02,LOT_ID);
        layout.setLayoutLeftMargin(38); 
  
        SGText SHPM_NO = new SGText("SHPM_NO",Util.TI18N.SHPM_NO());
        SHPM_NO.setTitleOrientation(TitleOrientation.LEFT);
        SHPM_NO.setValue(shpm_no);
        SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());
        CUSTOM_ODR_NO.setTitleOrientation(TitleOrientation.LEFT);
        CUSTOM_ODR_NO.setValue(custom_odr_no);
        DynamicForm form = new DynamicForm();
        form.setWidth100();
        form.setHeight(24);
        form.setNumCols(12);
        form.setItems(SHPM_NO,CUSTOM_ODR_NO);
        
        HStack hStack1 = new HStack();  
        hStack1.setHeight(24);  
        hStack1.setLayoutMargin(10);  
        hStack1.setMembersMargin(5);  
  
        IButton yahooButton = new IButton("拆分");  
        yahooButton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
    			doRefresh();
                doSplit();
                window.destroy();
            }  
        });  
        hStack1.addMember(yahooButton);  
        layout.addMember(form); 
        layout.addMember(table);  
        layout.addMember(hStack1); 
	    
	    window = new Window();
		window.setTitle("作业单明细信息");
		window.setLeft("12%");
		window.setTop("35%");
		window.setWidth(width);  
		window.setHeight(height); 
		window.setCanDragReposition(true);  
		window.setCanDragResize(true);
		window.addItem(layout);
		
		Criteria findValues = new Criteria();
		findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
		findValues.addCriteria("SHPM_NO", shpm_no);
		table.fetchData(findValues);
		
		return window;
	}
	
    private double getRate(double douPart, double douTotal) {
  	  
	    double rate = 0.0000;
	    if(douTotal > 0) {
	    	rate = douPart/douTotal;
	    }
	    return rate;
    }
    
	private void doSplit(){
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> row_map = new HashMap<String, String>();   //行号
		HashMap<String, String> qnty_map = new HashMap<String, String>();  //数量
		HashMap<String, String> vol_map = new HashMap<String, String>();   //体积
		HashMap<String, String> gw_map = new HashMap<String, String>();    //毛重
		HashMap<String, String> nw_map = new HashMap<String, String>();    //净重 
		HashMap<String, String> worth_map = new HashMap<String, String>(); //货值   
		
		if(table != null) {
			ListGridRecord[] records = table.getSelection();
			int[] edit_rows = new int[records.length];
			for(int i = 0; i < records.length; i++) {
				edit_rows[i] = table.getRecordIndex(records[i]);
			}
			Record rec = null;
			
			for(int i = 0; i < edit_rows.length; i++) {
				
				rec = table.getEditedRecord(edit_rows[i]);
				row_map.put(Integer.toString(i+1), rec.getAttribute("SHPM_ROW"));
				qnty_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("QNTY"),"0").toString());
				vol_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("VOL"),"0").toString());
				gw_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("G_WGT"),"0").toString());
				nw_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("N_WGT"),"0").toString());
				worth_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("WORTH"),"0").toString());
			}
			listmap.put("1", shpm_no);
			listmap.put("2", row_map);
			listmap.put("3", qnty_map);
			listmap.put("4", vol_map);
			listmap.put("5", gw_map);
			listmap.put("6", nw_map);
			listmap.put("7", worth_map);
			listmap.put("8", "");
			listmap.put("9", "");
			listmap.put("10", LoginCache.getLoginUser().getUSER_ID());
			String json = Util.mapToJson(listmap);
			Util.async.execProcedure(json, "SP_DISPATCH_SHPM_SPLIT_QNTY(?,?,?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.sayInfo(result.substring(2));
						
						doRefresh();
					}
					else{
						MSGUtil.sayError(result.substring(2));
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError(caught.getMessage());
				}
			});
		}
	}
	
	/**
	 * 刷新事件
	 * @author yuanlei
	 */
	public void doRefresh() {
		unshpm_table.discardAllEdits();
		unshpm_table.invalidateCache();
		Criteria crit = unshpm_table.getCriteria();
		if(crit == null) {
			crit = new Criteria();
		}
		crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
		crit.addCriteria("STATUS", StaticRef.SHPM_CONFIRM);
		crit.addCriteria("EMPTY_FLAG","Y");
		//fanglm 2011-3-18 按区域排序
//		crit.addCriteria("ORDER_BY_AREA","Y");
		if(!ObjUtil.isNotNull(crit.getAttribute("EXEC_ORG_ID"))) {
			crit.addCriteria("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		}
		crit.addCriteria("C_ORG_FLAG","true");
		//final LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) crit.getValues();
		unshpm_table.fetchData(crit, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				if(pageForm != null) {
					pageForm.getField("CUR_PAGE").setValue("1");
					LoginCache.setPageResult(unshpm_table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
				}
				unshpm_table.setSelectOnEdit(true);
				if(unshpm_table.getRecords().length > 0) {
					unshpm_table.selectRecord(unshpm_table.getRecord(0));
				}
				
				/*Util.db_async.getSHMPNOSum(map, new AsyncCallback<LinkedHashMap<String, String>>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(LinkedHashMap<String, String> result) {
						sumForm.setValue("TOT_VOL", ObjUtil.ifObjNull(result.get("TOT_VOL"), "").toString());
						sumForm.setValue("TOT_GROSS_W", ObjUtil.ifObjNull(result.get("TOT_GROSS_W"), "").toString());
						sumForm.setValue("TOT_QNTY", ObjUtil.ifObjNull(result.get("TOT_QNTY"), "").toString());
					}
				});*/
				ListGridRecord rec = load_table.getSelectedRecord();
				load_table.collapseRecord(rec);
				load_table.redraw();
			}
		});
	}
}
