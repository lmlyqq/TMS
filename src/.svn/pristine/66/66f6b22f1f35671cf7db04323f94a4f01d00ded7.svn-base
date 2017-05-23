package com.rd.client.action.base.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.base.BasOrgView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 通用的FORM保存方法
 * @author yuanlei
 *
 */
public class SaveOrgAction implements ClickHandler {

	private ValuesManager form;
	private TreeGrid table;
	private HashMap<String, String> map;
	private Map<String, String> record;
	private Map<String, String> id_name;
	private BasOrgView  view;
	private ArrayList<String> logList;  //日志信息
	
	public SaveOrgAction(TreeGrid p_table, ValuesManager p_form, HashMap<String, String> p_map,BasOrgView view) {
		table = p_table;
		form = p_form;  
		map = p_map;
		this.view = view;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		
		String op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
        record = form.getValues();
        if(op_flag.equals("M")) {
        	id_name = new HashMap<String, String>();
        	id_name.put("PARENT_ORG_ID", table.getSelectedRecord().getAttribute("PARENT_ORG_ID"));
        	id_name.put("PARENT_ORG_ID_NAME", record.get("PARENT_ORG_ID_NAME"));
        }
        record = form.getValues(); 
        record.remove("OP_FLAG");
        if(id_name != null) {
        	convertNameToId(record, id_name);            //将前台FORM的名称转换成ID
        	id_name = null;
        }
		if(record != null) {
			ArrayList<Object> obj = Util.getCheckResult(record, map);
			if(obj != null && obj.size() > 1) {
				String result = obj.get(0).toString();
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					if(obj.get(1) != null) {
						//需要校验唯一性
						chkUnique((HashMap<String, String>)obj.get(1), op_flag);
					}
					else {
						doOperation(op_flag);
					}
				}
				else {
					MSGUtil.sayError(obj.get(1).toString());
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void doInsert(Map map) {
		String json = Util.mapToJson(map);
		
		//设置日志信息
		//String[] titles = Util.getPropTitle(table.getDataSource().getAttribute("tableName"));
		//String[] fields = Util.getPropField(table.getDataSource().getAttribute("tableName"));
		//设置完毕
		
		Util.async.insertOrg(json, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					
					if(view != null) {
						view.reInitOrg();
					}
					
					Tree tree = table.getTree();
					TreeNode[] list = tree.getAllNodes();
					TreeNode node = null, child_node = null;
					TreeNode[] children = null;
					int i = 0;
					for(i = 0; i < list.length; i++) {
						node = list[i];
			            if(node.getAttributeAsString("ID").equals(ObjUtil.ifObjNull(record.get("PARENT_ORG_ID"),""))) {
			            	child_node = new TreeNode();
			            	child_node.setID(result.substring(2));
			            	child_node.setParentID(record.get("PARENT_ORG_ID"));
			            	Util.updateToRecord(form, table, child_node);
			            	child_node.setAttribute("ID",result.substring(2));
			            	child_node.setAttribute("ORG_LEVEL", ObjUtil.ifObjNull(record.get("ORG_LEVEL"),"1"));
			            	tree.add(child_node, node);
			            	if(!tree.isOpen(node)) {
			            		tree.openFolder(node);
			            	}
			            	children = tree.getChildren(node);
			            	break;
			            }
					}
					table.deselectAllRecords();
					if(children != null) {
		            	if(children.length > 1) {
		            		table.selectRecord(children[children.length-1]);
		            	}
		            	else {
		            		table.selectRecord(children[0]);
		            	}
					}
	            	if(view != null){
						view.initSaveBtn();
					}
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	private void doUpdate(ArrayList<String> sqlList) {
        
		Util.async.doUpdate(logList, sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					
					if(view != null) {
						view.reInitOrg();
					}
					
					updateToRecord(form, table, table.getSelectedRecord());   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
					//刷新选中的记录  //异常
					Tree tree = table.getTree();
					TreeNode node = tree.findById(table.getSelectedRecord().getAttribute("ID"));
					table.selectRecord(node);
					tree.closeFolder(node);
					tree.openFolder(node);
					
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
					
					if(view != null){
						view.initSaveBtn();
					}
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	/**
	 * 查询数据的唯一性校验
	 * @author yuanlei
	 * @param map
	 * @param flag
	 */
	private void chkUnique(HashMap<String, String> map, String flag) {
		final String op_flag = flag;
		Util.async.getCheckResult(Util.mapToJson(map), new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				if(!ObjUtil.isNotNull(result)) {
					doOperation(op_flag);
				}
				else {
					MSGUtil.sayError(result);
				}
			}		
		});
	}
	
	/**
	 * 执行插入或更新操作
	 * @author yuanlei
	 * @param op_flag
	 */
	private void doOperation(String op_flag) {
		if(op_flag.equals("M")) {                                  //--修改 
			
			//---设置日志信息
			if(table.getSelectedRecord() != null) {
				Map<String, String> select_map = Util.putRecordToModel(table.getSelectedRecord());   //获取修改前记录
	        	logList = new ArrayList<String>();
	        	//logList.add(Util.getUpdateLog(select_map, record, table.getDataSource().getAttribute("tableName")));  //拼装的描述内容
	        	logList.add(Util.getUpdateLog(select_map, record, table.getDataSource().getTableName()));  //拼装的描述内容
			}
			//---设置完毕
			ArrayList<String> sqlList = new ArrayList<String>();
			if(record != null) {
				//record.put("TABLE", table.getDataSource().getAttribute("tableName"));
				record.put("TABLE", table.getDataSource().getTableName());
				//record.put("EDITWHO", LoginCache.getLoginUser().getUSER_ID());
				String json = Util.mapToJson(record);
				sqlList.add(json);
			}
			doUpdate(sqlList);
		}
		else if(op_flag.equals("A")) {                             //--插入		
			if(record != null) {
				//record.put("TABLE", table.getDataSource().getAttribute("tableName"));
				record.put("TABLE", table.getDataSource().getTableName());
				doInsert(record);
			}
		}
	}
	
	private void convertNameToId(Map<String, String> map, Map<String, String> id_name) {
		Object[] iter = id_name.keySet().toArray();
		String key = "", value = "";
		for(int i = 0; i < iter.length; i++) {
			key = (String)iter[i];
			value = id_name.get(key);
			if((key.indexOf("_NAME") <= 0) || !key.substring(key.length() -5).equals("_NAME")) {
				map.put(key, value);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void updateToRecord(ValuesManager form, ListGrid table, ListGridRecord record) {
		if(record != null) {
			DynamicForm[] forms = form.getMembers();
			Map<String,Object> map = form.getValues();
	        for(int i = 0; i < forms.length; i++) {
	        	Util.updateToRecord(forms[i], table, record,map);
	        }
		}
		return;
	}
}
