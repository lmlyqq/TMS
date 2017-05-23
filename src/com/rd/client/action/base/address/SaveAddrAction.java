package com.rd.client.action.base.address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.view.base.SFAddressView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;
/**
 * 
 * @author xuweibin
 *
 */
public class SaveAddrAction implements ClickHandler {
	private SGTable table1;
	private SGTable table2;
	private String TMS_ADDR_CODE;
	private String TMS_ADDR_NAME;
	private String SF_UNIT_CODE;
	private String UNIT_CODE_M;
	private String SF_ADDR_CODE;
	private String ADDWHO;
	private ListGridRecord record;
	private ComboBoxItem LOAD_NAME;
	private TextItem LOAD_ID;
	private SGText ADDR_ID;
	private SGText UNIT_CODE;
	private SFAddressView view;
	private HashMap<String,String> check_map;
	private String OP_FLAG;
	private HashMap<String,Record> rec;
	private int count_flag;
	private int cou_a_flag;
	private ListGridRecord[] records;
	private int[] edit_rows;
	private ArrayList<String> list;
//	private Set<String> set;
	private ArrayList<String> edit_error;
	private ArrayList<String> unitList;
	private HashMap<String,String> sqlMap;
	private HashMap<String,HashMap<String,String>> mapList;
	private StringBuffer error_num;
	
	public SaveAddrAction(SGTable table1,SGTable table2,HashMap<String,String> p_map,SFAddressView view){
		this.table1=table1;
		this.table2=table2;
		this.check_map=p_map;
		this.view=view;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event){
		LOAD_NAME=(ComboBoxItem)view.tmsForm.getItem("LOAD_NAME");
		LOAD_ID=(TextItem)view.tmsForm.getItem("LOAD_ID");
		ADDR_ID=(SGText)view.tmsForm.getItem("ADDR_ID");
		UNIT_CODE=(SGText)view.tmsForm.getItem("UNIT_CODE");
		ADDWHO=LoginCache.getLoginUser().getUSER_ID();
		OP_FLAG=view.OP_FLAG;
		count_flag = 0;
		cou_a_flag = 0;
		records=table1.getSelection();
		HashMap<String,String> map;
		StringBuffer sf;
		
		//判断冷运网点和顺丰网点的区域是否一致
		String area_id2=ObjUtil.ifObjNull(view.tmsForm.getItem("LOAD_AREA_ID2").getValue(), "").toString();
		if(records != null && records.length>0){
			for(Record rec : records){
				if(!area_id2.equals(rec.getAttribute("AREA_ID2"))){
					MSGUtil.sayError("冷运网点和顺丰网点的所在城市要一致");
					return;
				}
			}
		}else{
			MSGUtil.sayError("请选择一个顺丰网点");
			return;
		}
		edit_error=new ArrayList<String>();
		list = new ArrayList<String>();
		rec=new HashMap<String,Record>();
		map=new HashMap<String,String>();
		//修改
		if(OP_FLAG.equals("M")){
//			set=Collections.synchronizedSet(new HashSet<String>());
			sqlMap=new HashMap<String,String>();
			SF_ADDR_CODE=records[0].getAttribute("ADDR_CODE");
			edit_rows=table2.getAllEditRows();
			mapList=new HashMap<String,HashMap<String,String>>();
			unitList=new ArrayList<String>();
			if(edit_rows!=null && edit_rows.length>0){
				for(int i=0;i<edit_rows.length;i++){
					record=table2.getRecord(edit_rows[i]);
					TMS_ADDR_CODE=record.getAttribute("TMS_ADDR_CODE");
					map=getEditValue(edit_rows[i]);
					UNIT_CODE_M=map.get("UNIT_CODE");
					SF_UNIT_CODE=record.getAttribute("UNIT_CODE");
					if(UNIT_CODE_M==null || UNIT_CODE_M.length()==0){
						map.remove("UNIT_CODE");
						map.put("UNIT_CODE", SF_ADDR_CODE);
						UNIT_CODE_M=SF_ADDR_CODE;
					}
					map.put("TMS_ADDR_CODE",TMS_ADDR_CODE);
					if(map!=null){
						if(check_map!=null){
							ArrayList<Object> obj=Util.getCheckResult(map, check_map);
							if(obj!=null && obj.size()>1){
								String result =obj.get(0).toString();
								if(result.equals(StaticRef.SUCCESS_CODE)){
									if(obj.get(1)!=null){
										chkUnique((HashMap<String,String>)obj.get(1),0);
									}
								}else{
									MSGUtil.sayError(obj.get(1).toString());
									return;
								}
							}
						}
					}
					mapList.put(i+"", map);
					unitList.add(UNIT_CODE_M);
					sf=new StringBuffer();
					sf.append("update TRANS_SSS_ADDR set UNIT_CODE='"+UNIT_CODE_M+"'");
					sf.append(" where SF_ADDR_CODE='"+SF_ADDR_CODE+"'");
					sf.append(" and TMS_ADDR_CODE='"+TMS_ADDR_CODE+"'");
					sf.append(" and UNIT_CODE='"+SF_UNIT_CODE+"'");
					sqlMap.put(i+"", sf.toString());
				}
			}
		}else if (OP_FLAG.equals("A")){
//			set=Collections.synchronizedSet(new HashSet<String>());
			String sf_addr_code;
			String sf_area_name;
			String sf_addr_name;
			String sf_unit=UNIT_CODE.getDisplayValue();
			TMS_ADDR_NAME=LOAD_NAME.getDisplayValue();
			TMS_ADDR_CODE=LOAD_ID.getDisplayValue();
			String tms_addr_id=ADDR_ID.getDisplayValue();
			for(int i=0;i<records.length;i++){
				ListGridRecord record=records[i];
				sf_addr_code=record.getAttribute("ADDR_CODE");
				sf_addr_name=record.getAttribute("ADDR_NAME");
				sf_area_name=record.getAttribute("AREA_NAME2");
				if(sf_unit==null || sf_unit.length()==0){
					SF_UNIT_CODE=sf_addr_code;
				}else{
					SF_UNIT_CODE=sf_unit;
				}
				map.put("UNIT_CODE", SF_UNIT_CODE);
				map.put("TMS_ADDR_CODE", TMS_ADDR_CODE);
				if(map!=null){
					if(check_map!=null){
						ArrayList<Object> obj=Util.getCheckResult(map, check_map);
						if(obj!=null && obj.size()>1){
							String result =obj.get(0).toString();
							if(result.equals(StaticRef.SUCCESS_CODE)){
								if(obj.get(1)!=null){
									chkUnique((HashMap<String,String>)obj.get(1),records.length);
								}
							}else{
								MSGUtil.sayError(obj.get(1).toString());
								return;
							}
						}
					}
				}
				sf=new StringBuffer();
				sf.append("insert into TRANS_SSS_ADDR(SF_ADDR_CODE,SF_ADDR_NAME,SF_AREA_NAME,TMS_ADDR_CODE," +
						"TMS_ADDR_NAME,ADDTIME,ADDWHO,TMS_ADDR_ID,UNIT_CODE) ");
				sf.append(" values(" + "'" + sf_addr_code + "'" + ","
						+ "'" + sf_addr_name + "'" + "," + "'"
						+ sf_area_name + "'" + "," + "'"
						+ TMS_ADDR_CODE + "'" + "," + "'"
						+ TMS_ADDR_NAME + "'" + ",sysdate," + "'"
						+ ADDWHO + "'"+",'"+tms_addr_id+"','"+SF_UNIT_CODE+"')");
				list.add(sf.toString());
				
			}
		}
	}
	
	private void chkUnique(HashMap<String, String> map,final int index) {
		Util.async.getCheckResult(Util.mapToJson(map), new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				if(!ObjUtil.isNotNull(result)){
					if(OP_FLAG.equals("M")){
						if((count_flag+1)==edit_rows.length){
							doUpdate();
						}
						count_flag++;
					}else if (OP_FLAG.equals("A")){
						cou_a_flag ++;
						if(cou_a_flag ==records.length){
//							doOper(list,index,null);
							doInsert();
						}
					}
				}else{
					MSGUtil.sayError(result);
					if(OP_FLAG.equals("M")){
						edit_error.add(edit_rows[count_flag]+"");
						if((count_flag+1)==edit_rows.length){
							doUpdate();
						}
						count_flag++;
					}else if(OP_FLAG.equals("A")){
						cou_a_flag ++;
						edit_error.add(cou_a_flag+"");
						if (cou_a_flag == records.length) {
//							doInsert(list,index,null);
							doInsert();
						}
					}
				}
			}		
		});
	}
	
	private void doInsert(){
		if (edit_error != null && edit_error.size() > 0) {
			for (int i = edit_error.size()-1; i >= 0; i--) {
				list.remove(Integer.valueOf(edit_error.get(i))-1);
			}
		}
		
		if (list != null && list.size()>0) {
			doOper(list,records.length,null);
		}
	}
	
	private void doUpdate(){
//		if(!checkSelf()){
//			return;
//		}
		if(edit_error.size()==0){
			for(int i=0;i<edit_rows.length;i++){
				list.add(sqlMap.get(i+""));
				rec.put(unitList.get(i), table2.getEditedRecord(edit_rows[i]));
			}
		}else{
			boolean flag;
			error_num=new StringBuffer();
			for(int i=0;i<edit_rows.length;i++){
				flag=true;
				for(int j=0;j<edit_error.size();j++){
					if((edit_rows[i]+"").equals(edit_error.get(j))){
						flag=false;
						error_num.append(" "+(Integer.parseInt(edit_error.get(j))+1)+" ");
						break;
					}
				}
				if(flag){
					list.add(sqlMap.get(i+""));
					rec.put(unitList.get(i), table2.getEditedRecord(edit_rows[i]));
				}
			}
		}
		if(list!=null && list.size()>0){
//			list=new ArrayList<String>(set);
			doOper(list,0,rec);
		}
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<String,String> getEditValue(int row){
		HashMap<String,String> vMap=(HashMap<String,String>)table2.getEditValues(row);
		Object[] key=vMap.keySet().toArray();
		for(int i=0;i<key.length;i++){
			if(vMap.get(key[i])==null){
				vMap.put(key[i].toString(),"");
			}
		}
		return vMap;
	}
//	private boolean checkSelf(){
//		Object[] strs=mapList.keySet().toArray();
//		HashSet<String> hashSet=new HashSet<String>();
//		if(strs.length==1){
//			return true;
//		}else{
//			for(int i=0;i<strs.length;i++){
//				hashSet.add(strs[i].toString()+mapList.get(strs[i]).get(i+""));
//			}
//		}
//		if(hashSet.size()==strs.length){
//			return true;
//		}
//		MSGUtil.sayError("修改后的数据中存在重复项,请修改");
//		return false;
//	}
	
	private void doOper(ArrayList<String> list,final int length,final HashMap<String,Record> rec){
		Util.async.excuteSQLList(list, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String result) {
				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
					if(edit_error==null || edit_error.size()==0){
						MSGUtil.showOperSuccess();
						if(view.OP_FLAG.equals("M")){
							view.freshTmsTable();
						}else if(view.OP_FLAG.equals("A")){
							if(length==1){
								ListGridRecord record =new ListGridRecord();
								record.setAttribute("UNIT_CODE", SF_UNIT_CODE);
								record.setAttribute("TMS_ADDR_NAME", TMS_ADDR_NAME);
								record.setAttribute("TMS_ADDR_CODE", TMS_ADDR_CODE);
								ArrayList<ListGridRecord> list=new ArrayList<ListGridRecord>(Arrays.asList(table2.getRecords()));
								list.add(0,record);
								table2.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()]));
								table2.redraw();
							}
						}
						clearValue();
					}else{
						if (OP_FLAG.equals("A")) {
							MSGUtil.showOperSuccess();
						}else {
							MSGUtil.sayWarning("行"+error_num.toString()+"更新失败");
						}
						clearValue();
						view.freshTmsTable();
					}
				}else{
					MSGUtil.sayError(result);
				}
			}
		});
	}
	private void clearValue(){
		UNIT_CODE.setValue("");
		LOAD_ID.setValue("");
		LOAD_NAME.setValue("");
		ADDR_ID.setValue("");
		view.initBtn3();
	}
}