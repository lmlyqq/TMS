package com.rd.client.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.DBService;
import com.rd.client.DBServiceAsync;
import com.rd.client.GreetingService;
import com.rd.client.GreetingServiceAsync;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.AreaDS;
import com.rd.client.ds.base.UserCustDS;
import com.rd.client.ds.base.VCAreaDS;
import com.rd.client.obj.system.SYS_USER;
import com.rd.client.prop.I18N_BTN;
import com.rd.client.prop.I18N_MSG;
import com.rd.client.prop.I18N_TIL;
import com.rd.client.win.CustSkuWin;
import com.rd.client.win.OrgWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.EditorEnterEvent;
import com.smartgwt.client.widgets.grid.events.EditorEnterHandler;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;

/**
 * 通用类
 * @author yuanlei
 *
 */
public class Util {
	
    public static final I18N_BTN BI18N = (I18N_BTN) GWT.create(I18N_BTN.class); 
    public static final I18N_MSG MI18N = (I18N_MSG) GWT.create(I18N_MSG.class); 
    public static final I18N_TIL TI18N = (I18N_TIL) GWT.create(I18N_TIL.class); 
    public static final GreetingServiceAsync async=(GreetingServiceAsync)GWT.create(GreetingService.class);
    public static final DBServiceAsync db_async=(DBServiceAsync)GWT.create(DBService.class);
    public static SYS_USER loginCache;
    public static com.smartgwt.client.widgets.Window msgWin;
    public static Canvas msgCas;
    public static Label label;
    private static DateChooserWin dateWin;
	
	/**
	 * BAS_CODES表初始化下拉框的值(不带条件WHERE和顺序ORDER BY)
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 */
	public static void initCodesComboValue(FormItem comboWidget) {
		initCodesComboValue(comboWidget, "BAS_CODES", "ID", "NAME_C", "", "","");
	}
	
//	/**
//	 * BAS_CODES表初始化下拉框的值(不带条件WHERE和顺序ORDER BY)
//	 * @author yuanlei
//	 * @param comboWidget 下拉框控件
//	 * @param table 表名
//	 * @param id 字段ID
//	 * @param name 字段名称
//	 */
//	public static void initCodesComboValue(FormItem comboWidget, String where) {
//		initCodesComboValue(comboWidget, "BAS_CODES", "ID", "NAME_C", where, " order by show_seq asc","");
//	}
	
	
	
	
	/**
	 * BAS_CODES表初始化下拉框的值(使用缓存)
	 * @author fanglm
	 * @param comboWidget 下拉框控件
	 * @param prop_id PROP_ID
	 * @param withNull 是否默认为空
	 */
	public static void initCodesComboValue(FormItem comboWidget, String prop_id) {
		initCodesComboValue(comboWidget, "BAS_CODES", "ID", "NAME_C", prop_id, " order by show_seq asc","");
	}
	/**
	 * BAS_CODES表初始化下拉框的值(使用缓存)
	 * @author fanglm
	 * @param comboWidget 下拉框控件
	 * @param prop_id PROP_ID
	 * @param withNull 是否默认为空
	 */
	public static void initCodesComboValue(FormItem comboWidget, String prop_id,boolean isCode) {
		initCodesComboValue(comboWidget, "BAS_CODES", "CODE", "NAME_C", prop_id, " order by show_seq asc","");
	}
	
	public static void initCodesComboValue(FormItem comboWidget, String prop_id,String default_val) {
		initCodesComboValue(comboWidget, "BAS_CODES", "ID", "NAME_C", prop_id, " order by show_seq asc",default_val);
	}
	
	/**
	 * BAS_CODES表初始化下拉框的值(不带条件WHERE和顺序ORDER BY)
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 */
	public static void initCodesComboValue(ListGridField fieldWidget, String where) {
		initCodesComboValue(fieldWidget, "BAS_CODES", "ID", "NAME_C", where, " order by show_seq asc","");
	}
	
	public static void initCodesComboValue(ListGridField fieldWidget, String where,String default_val) {
		initCodesComboValue(fieldWidget, "BAS_CODES", "ID", "NAME_C", where, " order by show_seq asc", default_val);
	}
	
	/**
	 * 初始化下拉框的值(不带条件WHERE和顺序ORDER BY)
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 */
	public static void initComboValue(FormItem comboWidget, String table, String id, String name) {
		initComboValue(comboWidget, table, id, name, "", "", "");
	}
	
	/**
	 * 初始化下拉框的值
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 * @param where 过滤条件
	 */
	public static void initComboValue(FormItem comboWidget, String table, String id, String name, String where) {
		initComboValue(comboWidget, table, id, name, where, "", "");
	}
	
	/**
	 * 初始化下拉框的值(默认使用缓存)
	 * @author yuanlei
	 * @param comboWidget 列表中的下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 * @param where 过滤条件(可省略WHERE)
	 * @param orderby 排序(可省略 ORDER BY)
	 */
	public static void initCodesComboValue(ListGridField comboWidget, String table, String id, String name, String where, String orderby,final String default_val) {
		final ListGridField combo = comboWidget;
		final String swhere = where;
		LinkedHashMap<String, LinkedHashMap<String, String>> map = LoginCache.getBizCodes();
		if(map != null &&  map.get(swhere) != null) {
//			Object[] values = map.get(swhere).keySet().toArray();
			combo.setValueMap(map.get(swhere));
//			if(withNull){
				combo.setDefaultValue(default_val);
//			}else{
//				combo.setDefaultValue(values[0].toString());
//			}
		}
		else {
			async.getComboValue(table, id, name, " where prop_code='"+where+"'", orderby, new AsyncCallback<LinkedHashMap<String, String>>() {
	
				public void onFailure(Throwable caught) {	
					;
				}
				public void onSuccess(LinkedHashMap<String, String> result) {
					if(result != null && result.size() > 0) {
//						Object[] values = result.keySet().toArray();
						combo.setValueMap(result);
						combo.setDefaultValue(default_val);
						LoginCache.getBizCodes().put(swhere, result);
					}
				}					
			});
		}
	}
	
	/**
	 * 初始化下拉框的值(默认使用缓存)
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 * @param where 过滤条件(可省略WHERE)
	 * @param orderby 排序(可省略 ORDER BY)
	 */
	public static void initCodesComboValue(FormItem comboWidget, String table, String id, String name, String where, String orderby,final String default_val) {
		final FormItem combo = comboWidget;
		final String swhere = where;
		LinkedHashMap<String, LinkedHashMap<String, String>> map = LoginCache.getBizCodes();
		if(map != null &&  map.get(swhere) != null) {
//			Object[] values = map.get(swhere).keySet().toArray();
			LinkedHashMap<String, String> mp =  map.get(swhere);
			combo.setValueMap(mp);
			combo.setDefaultValue(default_val);
		}
		else {
			async.getComboValue(table, id, name, " where prop_code='"+where+"'", orderby, new AsyncCallback<LinkedHashMap<String, String>>() {
	
				public void onFailure(Throwable caught) {	
					;
				}
				public void onSuccess(LinkedHashMap<String, String> result) {
					if(result != null && result.size() > 0) {
						combo.setValueMap(result);
						combo.setDefaultValue(default_val);
						LoginCache.getBizCodes().put(swhere, result);
					}
				}					
			});
		}
	}
	
	/**
	 * 初始化下拉框的值(默认使用缓存)
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 * @param where 过滤条件(可省略WHERE)
	 * @param orderby 排序(可省略 ORDER BY)
	 */
	public static void initStatusComboValue(FormItem comboWidget, String table, String id, String name, String where, String orderby,final String default_val) {
		final FormItem combo = comboWidget;
		final String swhere = where;
		LinkedHashMap<String, LinkedHashMap<String, String>> map = LoginCache.getStatCodes();
		if(map != null &&  map.get(swhere) != null) {
//			Object[] values = map.get(swhere).keySet().toArray();
			LinkedHashMap<String, String> mp =  map.get(swhere);
			combo.setValueMap(mp);
			combo.setDefaultValue(default_val);
		}
		else {
			async.getComboValue(table, id, name, " where prop_code='"+where+"'", orderby, new AsyncCallback<LinkedHashMap<String, String>>() {
	
				public void onFailure(Throwable caught) {	
					;
				}
				public void onSuccess(LinkedHashMap<String, String> result) {
					if(result != null && result.size() > 0) {
						combo.setValueMap(result);
						combo.setDefaultValue(default_val);
						LoginCache.getStatCodes().put(swhere, result);
					}
				}					
			});
		}
	}
	
	/**
	 * 初始化客户下拉框的值()
	 * @author fanglm
	 * @param comboWidget 下拉框控件
	 * @param apend 追加过滤条件( and filed='xxxx');
	 * @param orderby 排序(可省略 ORDER BY)
	 * @param withNull 初始化默认值为空?
	 */
	public static void initCustComboValue(FormItem comboWidget,String append, String orderby,final String default_val) {
		final FormItem combo = comboWidget;
		final String swhere = " WHERE CUSTOMER_FLAG='Y' AND ENABLE_FLAG='Y' " + append;

			async.getComboValue("BAS_CUSTOMER", "ID", "SHORT_NAME", swhere, orderby, new AsyncCallback<LinkedHashMap<String, String>>() {
	
				public void onFailure(Throwable caught) {	
					;
				}
				public void onSuccess(LinkedHashMap<String, String> result) {
					if(result != null && result.size() > 0) {
						combo.setValueMap(result);
						combo.setDefaultValue(default_val);
						LoginCache.getBizCodes().put(swhere, result);
					}
				}					
			});
		
	}
	

	
	/**
	 * 初始化客户下拉框的值()
	 * @author fanglm
	 * @param comboWidget 下拉框控件
	 * @param apend 追加过滤条件( and filed='xxxx');
	 * @param orderby 排序(可省略 ORDER BY)
	 */
	public static void initCustComboValue(FormItem comboWidget,String default_val) {
		initCustComboValue(comboWidget, "", "", default_val);
		
	}
	
	/**
	 * 初始化下拉框的值（不使用缓存）
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 * @param where 过滤条件(可省略WHERE)
	 * @param orderby 排序(可省略 ORDER BY)
	 */
	public static void initComboValue(FormItem[] comboWidget, String table, String id, String name, String where, String orderby) {
		initComboValue(comboWidget, table, id, name, where, orderby, "");
	}
	
	public static void initComboValue(FormItem comboWidget, String table, String id, String name, String where, String orderby) {
		initComboValue(comboWidget, table, id, name, where, orderby, "");
	}
	/**
	 * 添加外部回调函数
	 * @author Lang
	 * 
	 */
	public static void initComboValue(FormItem comboWidget, String table, String id, String name, String where, String orderby,final AsyncCallback<LinkedHashMap<String, String>> callBack, boolean flag) {
		initComboValue(comboWidget, table, id, name, where, orderby, "", callBack);
	}
	
//	/**
//	 * 初始化下拉框的值（不使用缓存）
//	 * @author yuanlei
//	 * @param comboWidget 下拉框控件
//	 * @param table 表名
//	 * @param id 字段ID
//	 * @param name 字段名称
//	 * @param where 过滤条件(可省略WHERE)
//	 * @param orderby 排序(可省略 ORDER BY)
//	 */
//	public static void initComboValue(FormItem comboWidget, String table, String id, String name, String where, String orderby, String defaultValue) {
//		initComboValue(comboWidget, table, id, name, where, orderby, false ,defaultValue);
//	}
	
	/**
	 * 初始化下拉框的值（不使用缓存）
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 * @param where 过滤条件(可省略WHERE)
	 * @param orderby 排序(可省略 ORDER BY)
	 */
	public static void initComboValue(FormItem[] comboWidget, String table, String id, String name, String where, String orderby,final String defaultValue) {
		final FormItem[] combo = comboWidget;
//		final boolean hasNull = withNull;
//		final String initValue = defaultValue;
		async.getComboValue(table, id, name, where, orderby, new AsyncCallback<LinkedHashMap<String, String>>() {

			public void onFailure(Throwable caught) {	
				SC.warn(caught.getMessage());
			}
			public void onSuccess(LinkedHashMap<String, String> result) {
				if(result != null && result.size() > 0) {
					Object[] values = result.keySet().toArray();
					for(int m = 0; m < combo.length; m++) {
						combo[m].setValueMap(result);
						
						if(defaultValue != null){
							combo[m].setDefaultValue(defaultValue);
						}else{
							combo[m].setDefaultValue(values[1].toString());
							combo[m].setValue(values[1].toString());
						}
					}
				}
			}					
		});
	}
	//列表的批量初始化下拉框
	public static void initComboValue(ListGridField[] comboWidget, String table, String id, String name, String where, String orderby,final String defaultValue) {
		final ListGridField[] combo = comboWidget;
//		final boolean hasNull = withNull;
//		final String initValue = defaultValue;
		async.getComboValue(table, id, name, where, orderby, new AsyncCallback<LinkedHashMap<String, String>>() {
			
			public void onFailure(Throwable caught) {	
				SC.warn(caught.getMessage());
			}
			public void onSuccess(LinkedHashMap<String, String> result) {
				if(result != null && result.size() > 0) {
					Object[] obj = result.keySet().toArray();
					for(int m = 0; m < combo.length; m++) {
						combo[m].setValueMap(result);
						if(defaultValue == null){
							combo[m].setDefaultValue(result.get(obj[1]));
						}
					}
				}
			}					
		});
	}
	
	
	/**
	 * 保留原方法
	 * @author Lang
	 */
	public static void initComboValue(FormItem comboWidget, String table, String id, String name, String where, String orderby,final String defaultValue) {
		initComboValue(comboWidget, table, id, name, where, orderby, defaultValue, null);
	}
	public static void initComboValue2(FormItem comboWidget, String table, String id, String name, String where, String orderby,final String defaultValue) {
		initComboValue2(comboWidget, table, id, name, where, orderby, defaultValue, null);
	}
	/**
	 * 添加外部回调函数
	 * @author Lang
	 * 
	 */
	public static void initComboValue(FormItem comboWidget, String table, String id, String name, String where, String orderby,final String defaultValue, final AsyncCallback<LinkedHashMap<String, String>> callBack) {
		final FormItem combo = comboWidget;
		async.getComboValue2(table, id, name, where, orderby, new AsyncCallback<LinkedHashMap<String, String>>() {

			public void onFailure(Throwable caught) {	
				SC.warn(caught.getMessage());
				if(callBack != null){
					callBack.onFailure(caught);
				}
			}
			public void onSuccess(LinkedHashMap<String, String> result) {
				if(result != null && result.size() > 0) {
					Object[] values = result.keySet().toArray();
					combo.setValueMap(result);
					
					if(defaultValue != null){
						combo.setDefaultValue(defaultValue);
					}else{
						combo.setDefaultValue(values[1].toString());
						combo.setValue(values[1].toString());
					}
					if(callBack != null){
						callBack.onSuccess(result);
					}
				}
			}					
		});
	}
	
	//不适用缓存获取实时数据	
	public static void initComboValue2(FormItem comboWidget, String table, String id, String name, String where, String orderby,final String defaultValue, final AsyncCallback<LinkedHashMap<String, String>> callBack) {
		final FormItem combo = comboWidget;
		async.getComboValue2(table, id, name, where, orderby, new AsyncCallback<LinkedHashMap<String, String>>() {

			public void onFailure(Throwable caught) {	
				SC.warn(caught.getMessage());
				if(callBack != null){
					callBack.onFailure(caught);
				}
			}
			public void onSuccess(LinkedHashMap<String, String> result) {
				if(result != null && result.size() > 0) {
					Object[] values = result.keySet().toArray();
					combo.setValueMap(result);
					
					if(defaultValue != null){
						combo.setDefaultValue(defaultValue);
					}else{
						combo.setDefaultValue(values[1].toString());
						combo.setValue(values[1].toString());
					}
					if(callBack != null){
						callBack.onSuccess(result);
					}
				}
			}					
		});
	}
	/**
	 * 设置下拉框的值（默认使用缓存）
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 * @param where 过滤条件(可省略WHERE)
	 * @param orderby 排序(可省略 ORDER BY)
	 */
	public static void initComboValue(final ListGridField fieldWidget, String table, String id, String name, String where, String orderby,final String default_value) {
		final ListGridField combo = fieldWidget;
			async.getComboValue(table, id, name, where, orderby, new AsyncCallback<LinkedHashMap<String, String>>() {
	
				public void onFailure(Throwable caught) {	
					;
				}
				public void onSuccess(LinkedHashMap<String, String> result) {
					if(result != null && result.size() > 0) {
						Object[] obj = result.keySet().toArray();
						combo.setValueMap(result);
						if(default_value == null){
							combo.setDefaultValue(result.get(obj[1]));
						}
					}
				}					
			});
	}
	
	
	
	public static void initComboValue(final ListGridField fieldWidget, String table, String id, String name, String where, String orderby){
		initComboValue(fieldWidget, table, id, name, where, orderby, "");
	}
	public static String boolToStr(String curValue) {
		String reResult = "N";
		if(curValue.compareTo("true") == 0) {
			reResult = "Y";
		}
		return reResult;
	}
	
	/**
	 * 把MAP转成JSON传输·
	 * @author yuanlei
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String mapToJson(Map map) {
		String json = "";
		if(map != null) {
			Object[] iter = map.keySet().toArray();
			JSONObject obj = new JSONObject();
			String key = "";
			Object value = null;
			for(int i = 0; i < iter.length; i++) {
				key = (String)iter[i];
				value = map.get(key);
				if(value != null) {
					obj.put(key, new JSONString(value.toString()));
				}
				else {
					obj.put(key, new JSONString(""));
				}
			}
			json = obj.toString();
		}
		return json;
	}
	
	public static void customExportUtil(String header,String filedName,String sql, HashMap<String, String> paramMap){
		
		async.customExportAction(header,filedName,sql, paramMap, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.FAILURE_CODE.equals(result)){
					MSGUtil.sayError("导出记录超出最大限制20000行，请重新筛选条件！");
				}else{
					Window.open(result, "", "");
				}
				
			}
		});
	}
	
	public static void sfOrderExportUtil(String header,String filedName,String sql,String odr_no){
		
		async.sfOrderExportAction(header,filedName,sql,odr_no,new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.FAILURE_CODE.equals(result)){
					MSGUtil.sayError("导出记录超出最大限制20000行，请重新筛选条件！");
				}else{
					Window.open(result, "", "");
				}
				
			}
		});
	}
	

	public static void exportUtil(String header,String filedName,String sql){
		
		async.exportAction(header,filedName,sql, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.FAILURE_CODE.equals(result)){
					MSGUtil.sayError("导出记录超出最大限制20000行，请重新筛选条件！");
				}else{
					Window.open(result, "", "");
				}
				
			}
		});
	}
	//应收对账
	public static void RecInitExportUtil(){
		
		async.RecInitBillexportAction(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.FAILURE_CODE.equals(result)){
					MSGUtil.sayError("导出记录超出最大限制20000行，请重新筛选条件！");
				}else{
					Window.open(result, "", "");
				}				
			}
		});
	}
	//应付对账(项目)
	public static void PayInitExportUtil1(){
		
		async.PayInitBillexportAction1(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.FAILURE_CODE.equals(result)){
					MSGUtil.sayError("导出记录超出最大限制20000行，请重新筛选条件！");
				}else{
					Window.open(result, "", "");
				}				
			}
		});
	}
	
	//应付对账(承运商)
	public static void PayInitExportUtil2(){
		
		async.PayInitBillexportAction2(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.FAILURE_CODE.equals(result)){
					MSGUtil.sayError("导出记录超出最大限制20000行，请重新筛选条件！");
				}else{
					Window.open(result, "", "");
				}				
			}
		});
	}
	
	//应收调度单导出
	public static void RecAdjExportUtil(String header,String filedName,String sql){
		
		async.RecAdjBillexportAction(header,filedName,sql, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.FAILURE_CODE.equals(result)){
					MSGUtil.sayError("导出记录超出最大限制20000行，请重新筛选条件！");
				}else{
					Window.open(result, "", "");
				}
				
			}
		});
	}
	
	//应付调度单导出
	public static void PayAdjExportUtil(String header,String filedName,String sql){
		
		async.PayAdjBillexportAction(header,filedName,sql, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.FAILURE_CODE.equals(result)){
					MSGUtil.sayError("导出记录超出最大限制20000行，请重新筛选条件！");
				}else{
					Window.open(result, "", "");
				}
				
			}
		});
	}
	
	//应付调度单导出
	public static void PayReqExportUtil(String header,String filedName,String sql){
		
		async.PayReqBillexportAction(header,filedName,sql, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.FAILURE_CODE.equals(result)){
					MSGUtil.sayError("导出记录超出最大限制20000行，请重新筛选条件！");
				}else{
					Window.open(result, "", "");
				}
				
			}
		});
	}
	
	
	//发票导出
	public static void InvoiceExportUtil(String id){
		
		async.InvoiceExportAction(id, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.FAILURE_CODE.equals(result)){
					MSGUtil.sayError("导出记录超出最大限制20000行，请重新筛选条件！");
				}else{
					Window.open(result, "", "");
				}
				
			}
		});
	}
	
	
	//下载
	public static void downLoadUtil(String cusId){
		
		async.downLoadAction(cusId, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				//Window.open(result, "", "");
			}
		});
	}
	
//	public static void getFieldName2(List<String> list,String key, String value, String alias){
//		async.getFieldName2(list, key, value, alias, new AsyncCallback<List<String>>() {
//			
//			@Override
//			public void onSuccess(List<String> result) {
//				
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				
//			}
//		});
//		
//	}
	/**
	 * 调用存储过程实现导出功能
	 * @author fanglm
	 * @param header
	 * @param filedName
	 * @param sql
	 * @param pro_name
	 */
	public static void exportUtil(String header,String filedName,String sql,String pro_name){
		
		async.exportByProAction(header,filedName,sql,pro_name, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.FAILURE_CODE.equals(result)){
					MSGUtil.sayError("导出记录超出最大限制20000行，请重新筛选条件！");
				}else{
					Window.open(result, "", "");
				}
				
			}
		});
	}
	
	public static void initMsgWin() {
		msgCas = new Canvas();
		msgCas.setPadding(5);
		msgCas.setContents("");
		//新增 XWB 2016-03-25
		msgCas.setWidth100();
		msgCas.setHeight100();
		//新增结束
		  
		msgWin = new com.smartgwt.client.widgets.Window();   
		msgWin.setTitle("系统提示");   
		msgWin.setHeaderIcon("rd/msg.png", 16, 16);
		msgWin.setShowEdges(true);
		msgWin.setKeepInParentRect(true);
		msgWin.setWidth(260);   
		msgWin.setHeight(140);
		msgWin.setTop(Page.getHeight() - 145);
		msgWin.setLeft(Page.getWidth() - 276);
		msgWin.setCanDragResize(true);   
		msgWin.setShowCloseButton(false);
		msgWin.hide();
		
		msgWin.addItem(msgCas);
		if(msgWin.isDrawn()) {
			msgWin.redraw();
		}
		else {
			msgWin.draw();
		}
		showMsg(0, "欢迎使用普菲斯物流信息化管理平台!");
	}
	
	/**
	 * 提示消息框
	 * @param type 0-成功/1-失败
	 * @param msg  消息内容
	 */
	public static void showMsg(int type, String msg) {
		int schedule = 1000;
		if(type == 1) {
			msgCas.setContents("<font style=\"color:#ff0000;font-size:13px;\">" + msg + "</font>"); 
			// 修改 XWB 2016-03-25
//			schedule = 3000;
			schedule = 2000;
			// 修改结束
		}
		else {
			msgCas.setContents("<font style=\"color:#000000;font-size:13px;\">" + msg + "</font>");  
			// 修改 XWB 2016-03-25
//			schedule = 5000;
			schedule = 1000;
			// 修改结束
		}
		msgWin.show();
		final Timer timer = new Timer() {
			public void run(){
				msgWin.hide();
				//timer.cancel();
			}
		};
		timer.schedule(schedule);
	}
	
	/**
	 * 获取空值校验的结果,校验通过则返回控制，否则返回错误信息
	 * @author yuanlei
	 * @param valueMap FORM或表格中获取的字段值
	 * @param field 字段名
	 * @param title 字段对应的中文标题
	 * @return
	 */
	public static String chkNullResult(Map<String, String> valueMap, String field, String title) {
		String msg = "";
		String[] foo = field.split(",");
		for(int i=0;i<foo.length;i++){
			Object value = valueMap.get(foo[i]);
			if(foo[i].indexOf("_FLAG") >= 0 && value == null){
				value = "N";
			}
			String str = getFlagAsString(ObjUtil.ifObjNull(value, "").toString());
			if(!ObjUtil.isNotNull(str)){
				//fanglm 2011-2-19
				//	&& !ObjUtil.isNotNull(valueMap.get("ID"))) {
				msg = "[" + title + "]" + Util.MI18N.CHK_NOTNULL() + "\r\n";
				return msg;
			}
		}
		
		return msg;
	}
	
	/**
	 * 获取时间格式的校验结果,校验通过则返回控制，否则返回错误信息
	 * @author yuanlei
	 * @param valueMap FORM或表格中获取的字段值
	 * @param field 字段名
	 * @param title 字段对应的中文标题
	 * @return
	 */
	public static String chkDateResult(Map<String, String> valueMap, String field, String title) {
		String msg = "[" + title + "]" + Util.MI18N.CHK_DATE() + "\r\n";
		if(ObjUtil.isNotNull(valueMap.get(field))) {
			String datetime = (String)valueMap.get(field);
			if(StaticRef.FORMAT_DATETIME.compareTo(DateDisplayFormat.TOJAPANSHORTDATETIME) == 0) {
				if(isValidDate(datetime, "YYYY/MM/DD HH:MM")) {
					msg = "";
					if(datetime.length() > 19) {
						valueMap.put(field, "to_date('" + datetime.substring(0,19) + "','YYYY-MM-DD HH24:MI:SS')");
					}
					else {
						valueMap.put(field, "to_date('" + datetime + "','YYYY-MM-DD HH24:MI:SS')");
					}
					return msg;
				}
			}
			if(StaticRef.FORMAT_DATE.compareTo(DateDisplayFormat.TOJAPANSHORTDATE) == 0) {
				if(isValidDate(datetime, "YYYY/MM/DD")) {
					msg = "";
					valueMap.put(field, "to_date('" + datetime + "','YYYY-MM-DD HH24:MI:SS')");
					return msg;
				}
			}
		}
		else {
			msg = "";
		}
		return msg;
	}
	
	/**
	 * 判断日期是否合法
	 * @author yuanlei
	 * @param datetime 当前的日期值
	 * @param format 指定的格式
	 * @return
	 */
	public static boolean isValidDate(String datetime, String format) {
		if(datetime == null || datetime.length() < format.length()-2)
			return false;
		datetime = datetime.substring(0, format.length()-2);
		//fanglm 2010-12-1 时间格式校验，月份和日期为个数情况
		StringBuffer sf = new StringBuffer();
		String[] val = datetime.replaceAll("-", "/").split("/");
		for(int i=0;i<val.length;i++){
			if(i == 0){
				sf.append(val[i]);
			}else if( i== 1){
				sf.append("/");
				if(val[i].length() == 1){
					sf.append("0"+val[i]);
				}else{
					sf.append(val[i]);
				}
			}else if( i== 2){
				sf.append("/");
				if(val[i].split(" ")[0].length() == 1){
					sf.append("0"+val[i].split(" ")[0]);
				}else{
					sf.append(val[i].split(" ")[0]);
				}
				if(val[i].split(" ").length >1){
					sf.append(" ");
					sf.append(val[i].split(" ")[1]);
				}
			}
		}
		datetime = sf.toString();
		for(int i = 0; i < datetime.length(); i++) {
			char chr = format.charAt(i);
			if(chr == 'Y' || chr == 'M' || chr == 'D' || chr == 'H' || chr == 'S') {
				if(!Character.isDigit(datetime.charAt(i))) {
					return false;
				}
			}
			else if(chr == '-' || chr == '/' || chr == '.') {
				if(datetime.charAt(i) != '-' && datetime.charAt(i) != '/' && datetime.charAt(i) != '.') {
					return false;
				}
			}
			else if(chr == ' ' || chr == ':') {
				if(datetime.charAt(i) != chr) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 获取校验唯一行的SQL
	 * @author yuanlei
	 * @param field 字段
	 * @param value 界面所有字段的值
	 * @param tableName 表明
	 * @return
	 */
	public static String getUniqueSQL(String field, Map<String, String> valueMap, String tableName) {
		StringBuffer sf = new StringBuffer();
		sf.append("select count(1) from ");
		sf.append(tableName);
		sf.append(" where 1=1");
		sf.append(getSplitField(field,valueMap));
		if(ObjUtil.isNotNull(valueMap.get("ID"))) {  //修改保存时，从数据路里校验唯一性时需要把自己排除在外
			sf.append(" and ID != '");
			sf.append(valueMap.get("ID"));
			sf.append("'");
		}
		return sf.toString();
	}
	
	private static String getSplitField(String sql, Map<String, String> valueMap) {
		String[] array = sql.split(",");
		StringBuffer sf = new StringBuffer();
		Object value;
		for(int i = 0; i < array.length; i++) {
			value = valueMap.get(array[i]);
			sf.append(" and ");
			sf.append(array[i]);
			sf.append("='");
			sf.append(getFlagAsString(value.toString()));
			sf.append("'");
		}
		return sf.toString();
	}
	private static String getFlagAsString(String curValue){
		String reResult = curValue;
		if(curValue.compareTo("true") == 0) {
			reResult = "Y";
		}else if(curValue.compareTo("false") == 0){
			reResult = "N";
		}
		return reResult;
	}
	
	/**
	 * 获取校验的结果（如果校验不通过，则返回错误信息，如果校验通过，则返回下一步校验的SQL或空值
	 * @author yuanlei
	 * @param valueMap FORM或表格中获取的字段值
	 * @param verifyMap 需校验的字段信息，包换字段名称和校验类型
	 * @return List(0) = 00(成功) or 01(失败); List(1) = 下一步校验的SQL或空值 OR 失败信息
	 */
	public static ArrayList<Object> getCheckResult(Map<String, String> valueMap, HashMap<String, String> verifyMap) {
		ArrayList<Object> retList = new ArrayList<Object>();
		String result = StaticRef.FAILURE_CODE;
		String msg = "";
		HashMap<String, String> sqlMap = new HashMap<String, String>();
		if(valueMap == null || verifyMap == null) {
			retList.add(StaticRef.FAILURE_CODE);
			retList.add(Util.MI18N.PARAM_ERROR());
			return retList;
		}
		String tableName = verifyMap.get("TABLE");
		Object[] iter = verifyMap.keySet().toArray();
		String field = "", value = "", title = "";
		for(int i = 0; i < iter.length; i++) {
			field = (String)iter[i];
//			if(ObjUtil.isNotNull(valueMap.get(field)) && !field.equals("TABLE")) {   
			if(!field.equals("TABLE")){
				//‘TABLE’为特殊处理，因为数据库并不存在TABLE的字段，只是在VIEW中人为往字段值的MAP中加入了一个TABLE,所以解析的字段的时候要去掉
				value = verifyMap.get(field).substring(0,2);
				title = verifyMap.get(field).substring(2);
				if(value.equals(StaticRef.CHK_NOTNULL)) {
					msg += chkNullResult(valueMap, field, title);
				}
				else if(value.equals(StaticRef.CHK_UNIQUE)) {
					msg += chkNullResult(valueMap, field, title);
					if(ObjUtil.isNotNull(msg)) {
						retList.add(result);
						retList.add(msg);
						return retList;
					}
					boolean bool = true;
					if(field.indexOf("_FLAG") >=0 ){
						String[] soo = field.split(",");
						for(int j=0;j<soo.length;j++){
							if(soo[j].indexOf("_FLAG") >= 0){
								Object val = valueMap.get(soo[j]);
								if(val == null || val.toString().equals("false")){
									bool = false;
								}
								
							}
						}
					}
					if(bool){
						sqlMap.put(title, getUniqueSQL(field, valueMap, tableName));
					}
				}
				else if(value.equals(StaticRef.CHK_DATE)) {
					msg += chkDateResult(valueMap, field, title);
				}
			}
		}
		if(ObjUtil.isNotNull(msg)) {
			retList.add(result);
			retList.add(msg);
		}
		else {
			retList.add(StaticRef.SUCCESS_CODE);
			if(sqlMap.size() <= 0) {    //没有需要校验的SQL语句，所以直接返回NULL值
				sqlMap = null;
			}
			retList.add(sqlMap);
		}
		return retList;
	}
	
	
	/**
	 * 从数据库中动态获取下拉框的值
	 * @author yuanlei
	 * @param combo
	 * @param table
	 * @param id
	 * @param name
	 * @param orderby
	 * @param withNull
	 * @param defaultValue
	 */
	public static void initDynamicCombo(final FormItem combo, final String table, final String id, final String name, final String orderby) {
		initDynamicCombo(combo, table, id, name, orderby, "");
	}
	
	/**
	 * 从数据库中动态获取下拉框的值
	 * @author yuanlei
	 * @param combo
	 * @param table
	 * @param id
	 * @param name
	 * @param orderby
	 * @param withNull
	 */
	public static void initDynamicCombo(final FormItem combo, final String table, final String id, final String name, final String orderby, String defaultValue) {
//		final boolean hasNull = withNull;
		final String initValue = defaultValue;
		combo.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getCharacterValue() != null && event.getKeyName().equals("Enter") && combo != null && combo.getValue() != null) {
					combo.setValueMap("");
					initComboValue(combo, table, id, name, " where FULL_INDEX like '%" + combo.getValue().toString().toUpperCase() + "%'", orderby, initValue);
				}
			}
		
		});
	}

	
	public static void initDynamicCombo(final ListGrid listG,final ListGridField combo, final String table, final String id, final String name, final String orderby) {
		final Canvas canvas = new Canvas();
		final int row  = listG.getEditRow();
		combo.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if(event.getValue() != null && event.getValue().toString().trim().length() >1) {
					Criteria crit = new Criteria();
					crit.setAttribute("OP_FLAG", "M");
					crit.setAttribute("FULL_INDEX", event.getValue().toString());
					
					SGTable table = new SGTable(AreaDS.getInstance("BAS_AREA"), "100%", "100%", true, true ,false);
					ListGridField AREA_CODE = new ListGridField("AREA_CODE", Util.TI18N.AREA_CODE(), 80);
					ListGridField SHORT_NAME = new ListGridField("SHORT_NAME", Util.TI18N.SHORT_NAME(), 80);
					ListGridField HINT_CODE = new ListGridField("HINT_CODE", Util.TI18N.HINT_CODE(), 70);
					table.setFields(AREA_CODE, SHORT_NAME, HINT_CODE);
					
					table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
						
						@Override
						public void onRecordDoubleClick(RecordDoubleClickEvent event) {
							if(ObjUtil.isNotNull(event.getRecord().getAttribute("SHORT_NAME"))){
								listG.setEditValue(row, combo.getName(),event.getRecord().getAttributeAsString("SHORT_NAME") );
							}
							canvas.hide();
							canvas.destroy();
						}
					});

					table.setCriteria(crit);
					table.fetchData(crit);
					table.setCanEdit(false);
					canvas.addChild(table);
					canvas.setWidth(280);
					canvas.setHeight(230);
					canvas.setLeft(780);
					canvas.setTop(234);
					canvas.setVisible(true);
					if(canvas.isDrawn()) {
						canvas.redraw();
					}
					else {
						canvas.draw();
					}
				}
			}
		
		});
	}
	
	/**
	 * 批量初始化下拉框
	 * @author yuanlei
	 * @param comboWidget
	 * @param where
	 */
	public static void initCodesComboValue(ComboBoxItem[] comboWidget, String where) {
		initCodesComboValue(comboWidget, "BAS_CODES", "ID", "NAME_C", where, " order by show_seq asc", true);
	}
	
	/**
	 * 批量初始化下拉框的值(默认使用缓存)
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 * @param where 过滤条件(可省略WHERE)
	 * @param orderby 排序(可省略 ORDER BY)
	 */
	public static void initCodesComboValue(ComboBoxItem[] comboWidget, String table, String id, String name, String where, String orderby, boolean withNull) {
		final ComboBoxItem[] combo = comboWidget;
		final String swhere = where;
		final boolean hasNull = withNull;
		LinkedHashMap<String, LinkedHashMap<String, String>> map = LoginCache.getBizCodes();
		if(map != null &&  map.get(swhere) != null) {
			Object[] values = map.get(swhere).values().toArray();
			if(combo != null && combo.length > 0) {
				for(int i = 0; i < combo.length; i++) {
					combo[i].setValueMap(map.get(swhere));
					if(hasNull) {
						combo[i].setDefaultValue(" ");
					}
					else {
						combo[i].setDefaultValue(values[0].toString());
					}
				}
			}
		}
		else {
			async.getComboValue(table, id, name, where, orderby, new AsyncCallback<LinkedHashMap<String, String>>() {
	
				public void onFailure(Throwable caught) {	
					;
				}
				public void onSuccess(LinkedHashMap<String, String> result) {
					if(result != null && result.size() > 0) {
						Object[] values = result.keySet().toArray();
						if(combo != null && combo.length > 0) {
							for(int i = 0; i < combo.length; i++) {
								combo[i].setValueMap(result);
								if(hasNull) {
									combo[i].setDefaultValue(" ");
								}
								else {
									combo[i].setDefaultValue(values[0].toString());
								}
							}
						}
						LoginCache.getBizCodes().put(swhere, result);
					}
				}					
			});
		}
	}
	
	/**
	 * 执行机构初始化
	 * @author yuanlei
	 * @param EXEC_ORG_ID
	 */
	public static void initOrg(SGCombo EXEC_ORG_ID, String defaultValue, String where) {
		initComboValue(EXEC_ORG_ID, "BAS_ORG", "ID", "SHORT_NAME", where, " SHOW_SEQ ASC", defaultValue);
	}
	public static void initOrg(ListGridField EXEC_ORG_ID,String defaultValue, String where, boolean withNull) {
		String table = "V_USER_ORG";
		if(LoginCache.getLoginUser().getROLE_ID().equals(StaticRef.SUPER_ROLE)) {
			where = "";
			table = "BAS_ORG";
		}
		initComboValue(EXEC_ORG_ID, table, "ID", "SHORT_NAME", where, "");
	}
	
	/**
	 * 初始化运输服务
	 * @author yuanlei
	 * @param TRANS_SRVC_ID
	 * @param defaultValue
	 */
	public static void initTrsService(SGCombo TRANS_SRVC_ID, String defaultValue) {
		initComboValue(TRANS_SRVC_ID, "BAS_TRANS_SERVICE", "ID", "SHORT_NAME", "", " SHOW_SEQ ASC", defaultValue);
	}
	public static void initTrsService(ListGridField TRANS_SRVC_ID, String defaultValue) {
		initComboValue(TRANS_SRVC_ID, "BAS_TRANS_SERVICE", "ID", "SHORT_NAME", "", " SHOW_SEQ ASC");
	}
	
	/**
	 * 初始化承运商
	 * @author fanglm
	 * @param SUPLR_ID
	 * @param defaultValue
	 * @param withNull
	 */
	public static void initSupplier(SGCombo SUPLR_ID,String defaultValue){
		initComboValue(SUPLR_ID, "BAS_SUPPLIER", "ID", "SHORT_NAME", "", " SHOW_SEQ ASC");
	}
	
	public static void initOrgSupplier(ListGridField SUPLR_ID, String where) {
		String swhere = " where 1 = 1";
		if(!LoginCache.getLoginUser().getROLE_ID().equals(StaticRef.SUPER_ROLE)) {
			swhere += " and org_id in (select org_id from sys_user_org where user_id ='" + LoginCache.getLoginUser().getUSER_ID() + "')";
		}
		if(ObjUtil.isNotNull(where)) {
			swhere += where;
		}
		initComboValue(SUPLR_ID, "V_ORG_SUPPLIER", "SUPLR_ID", "SUPLR_CNAME", swhere, "");
	}
	
	public static void initOrgSupplier(ArrayList<ListGridField> SUPLR_ID, String where) {
		String swhere = " where 1 = 1";
		if(!LoginCache.getLoginUser().getROLE_ID().equals(StaticRef.SUPER_ROLE)) {
			swhere += " and org_id in (select org_id from sys_user_org where user_id ='" + LoginCache.getLoginUser().getUSER_ID() + "')";
		}
		if(ObjUtil.isNotNull(where)) {
			swhere += where;
		}
		initComboBatch(SUPLR_ID, "V_ORG_SUPPLIER", "SUPLR_ID", "SUPLR_CNAME", swhere, "", null);
	}
	
//	/**
//	 * 初始化行政区域
//	 * @author yuanlei
//	 * @param AREA
//	 * @param defaultValue
//	 */
//	public static void initArea(ComboBoxItem AREA, String defaultValue) {
// 		initDynamicCombo(AREA, "VC_AREA", "ID", "AREA_CNAME", " AREA_CODE ASC", false, defaultValue); //yuanlei 动态获取
//	}
	/*public static void initArea(ListGrid listG,ListGridField AREA, String defaultValue) {
		initDynamicCombo(listG,AREA, "VC_AREA", "AREA_CODE", "AREA_CNAME", " AREA_CODE ASC");
	}*/
	public static void initArea(final SGTable table, final ListGridField AREA_NAME, String area_code, String area_cname, String defaultValue) {
		final String code = area_code;
		final String name = area_cname;
		DataSource ds = VCAreaDS.getInstance("VC_BAS_AREA");
		
		ListGridField AREA_CODE = new ListGridField("AREA_CODE", Util.TI18N.AREA_CODE(), 70);
		ListGridField SHOW_NAME = new ListGridField("SHORT_NAME", Util.TI18N.SHORT_NAME(), 90);
		ListGridField HINT_CODE = new ListGridField("HINT_CODE", Util.TI18N.HINT_CODE(), 60);
		final ComboBoxItem area_name = new ComboBoxItem();
		area_name.setColSpan(2);
		area_name.setWidth(120);
		area_name.setOptionDataSource(ds);  
		area_name.setDisabled(false);
		area_name.setShowDisabled(false);
		area_name.setDisplayField("CONTENT");
		area_name.setValueField("SHORT_NAME");
		area_name.setPickListWidth(240);
		area_name.setPickListBaseStyle("myBoxedGridCell");
		area_name.setPickListFields(AREA_CODE, SHOW_NAME, HINT_CODE);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		area_name.setPickListCriteria(criteria);
		
		AREA_NAME.setEditorType(area_name);
		AREA_NAME.addEditorExitHandler(new EditorExitHandler() {

			@Override
			public void onEditorExit(EditorExitEvent event) {
				Object obj = event.getNewValue();
				if(obj != null) {
					if(obj.toString().equals("")) {
						SC.say("未找到匹配的行政区域!");
						Record rec = event.getRecord();
						if(rec != null) {
							table.setEditValue(event.getRowNum(), code, rec.getAttribute(code));
							table.setEditValue(event.getRowNum(), name, rec.getAttribute(name));
							table.setEditValue(event.getRowNum(), code+name, rec.getAttribute(name));
						}
						else {
							table.setEditValue(event.getRowNum(), code, "");
							table.setEditValue(event.getRowNum(), name, "");
							table.setEditValue(event.getRowNum(), code+name, "");
						}
					}
					else {
						String curValue = ObjUtil.ifObjNull(event.getNewValue(),"").toString();
						//SC.warn(table.getEditValue(event.getRowNum(), code+name).toString());
						if(!ObjUtil.ifObjNull(table.getEditValue(event.getRowNum(), code+name),"").toString().equals(curValue)) {
							int pos = curValue.indexOf("[");
							if(pos > 0) {
								table.setEditValue(event.getRowNum(), code, curValue.substring(0,pos));
								table.setEditValue(event.getRowNum(), name, curValue.replace("]", "").substring(pos+1, curValue.length() - 1));
								table.setEditValue(event.getRowNum(), code+name, curValue.replace("]", "").substring(pos+1, curValue.length() - 1));
							}
							else {
								SC.say("请从区域列表中选择数据!");
								Record rec = event.getRecord();
								if(rec != null) {
									table.setEditValue(event.getRowNum(), code, rec.getAttribute(code));
									table.setEditValue(event.getRowNum(), name, rec.getAttribute(name));
									table.setEditValue(event.getRowNum(), code+name, rec.getAttribute(name));
								}
							}
						}
					}
				}
			}
			
		});
	}
	
	/**
	 * 初始化行政区域
	 * @author fanglm
	 * @param area_name  地址点 展示控件
 	 * @param area_id  地址点iD 隐藏控件
	 */
	public static void initArea(final ComboBoxItem area_name,final TextItem area_id){
		DataSource ds = VCAreaDS.getInstance("VC_BAS_AREA");
		
		ListGridField AREA_CODE = new ListGridField("AREA_CODE", Util.TI18N.AREA_CODE(), 70);
		ListGridField SHORT_NAME = new ListGridField("SHORT_NAME", Util.TI18N.SHORT_NAME(), 90);
		ListGridField HINT_CODE = new ListGridField("HINT_CODE", Util.TI18N.HINT_CODE(), 60);
		ListGridField AREA_LEVEL = new ListGridField("AREA_LEVEL", Util.TI18N.AREA_LEVEL(), 60);
		area_name.setColSpan(2);
		area_name.setWidth(FormUtil.Width);
		area_name.setOptionDataSource(ds);  
		area_name.setDisabled(false);
		area_name.setShowDisabled(false);
		area_name.setDisplayField("CONTENT");
		area_name.setValueField("SHORT_NAME");
		area_name.setPickListWidth(240);
		area_name.setPickListBaseStyle("myBoxedGridCell");
		area_name.setPickListFields(AREA_CODE, SHORT_NAME,AREA_LEVEL,HINT_CODE);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		area_name.setPickListCriteria(criteria);
		
		area_name.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
				Record selectedRecord  = area_name.getSelectedRecord();
				if(selectedRecord != null && area_id != null){
					area_id.setValue(selectedRecord.getAttribute("AREA_CODE"));
				}
				
			}
		});
		area_name.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if(ObjUtil.ifObjNull(area_name.getValue(),"").toString().length() < 1) {
					if(area_id != null){
						area_id.setValue("");
					}
				}
			}
			
		});
		
	}
	
	/**
	 * 初始化行政区域(连动地址)
	 * @author yuanlei
	 * @param area_name  地址点 展示控件
 	 * @param area_id  地址点iD 隐藏控件
	 */
	public static void initAreaAddr(final ComboBoxItem area_name,final TextItem addr_id){
		
		DataSource ds = VCAreaDS.getInstance("VC_BAS_AREA");
		
		ListGridField AREA_CODE = new ListGridField("AREA_CODE", Util.TI18N.AREA_CODE(), 70);
		ListGridField SHORT_NAME = new ListGridField("SHORT_NAME", Util.TI18N.SHORT_NAME(), 90);
		ListGridField HINT_CODE = new ListGridField("HINT_CODE", Util.TI18N.HINT_CODE(), 60);
		area_name.setColSpan(2);
		area_name.setWidth(120);
		area_name.setOptionDataSource(ds);  
		area_name.setDisabled(false);
		area_name.setShowDisabled(false);
		area_name.setDisplayField("CONTENT");
		area_name.setValueField("SHORT_NAME");
		area_name.setPickListWidth(240);
		area_name.setPickListBaseStyle("myBoxedGridCell");
		area_name.setPickListFields(AREA_CODE, SHORT_NAME,HINT_CODE);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		area_name.setPickListCriteria(criteria);
		
		area_name.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
				Record selectedRecord  = area_name.getSelectedRecord();
				if(selectedRecord != null && addr_id != null){
					addr_id.setValue(selectedRecord.getAttribute("AREA_CODE"));
				}
				
			}
		});
		area_name.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if(ObjUtil.ifObjNull(area_name.getValue(),"").toString().length() < 1) {
					if(addr_id != null){
						addr_id.setValue("");
					}
				}
			}
			
		});
		
	}
	
	
	/**
	 * 初始化组织机构二级窗口
	 * @param ORG_NAME 机构显示 控件
	 * @param ORG_ID 机构隐藏空间
	 * @param isAll 是否显示全部组织就够
	 * @param top 二级窗口距离顶部距离
	 * @param left 二级窗口距离左边距离
	 */
	public static void initOrg(final TextItem ORG_NAME,final TextItem ORG_ID,final boolean isAll,final String top,final String left){
		PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
        searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new OrgWin(ORG_NAME,ORG_ID,isAll,top,left).getViewPanel();
			}
		});
        ORG_NAME.setIcons(searchPicker);
	}
	
	/**
	 * 初始化组织机构二级窗口
	 * @param ORG_NAME 机构显示 控件
	 * @param ORG_ID 机构隐藏空间
	 * @param isAll 是否显示全部组织就够
	 * @param top 二级窗口距离顶部距离
	 * @param left 二级窗口距离左边距离
	 */
	public static void initOrg(final TextItem ORG_NAME,final TextItem ORG_ID,final boolean isAll,final String top,final String left,final TextItem FATHER_ORG_ID){
		PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
        searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new OrgWin(ORG_NAME,ORG_ID,isAll,top,left,FATHER_ORG_ID).getViewPanel();
			}
		});
        ORG_NAME.setIcons(searchPicker);
	}
	
	/**
	 * 货品控件弹出二级窗口初始化
	 * @author fanglm
	 * @param SKU_NAME  显示货品控件
	 * @param SKU_ID 隐藏货品id控件
	 * @param vm 
	 */
	public static void initSku(final TextItem SKU_NAME,final TextItem SKU_ID,final ValuesManager vm){
//		SKU_NAME.setColSpan(1);
		PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH,new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				new CustSkuWin(SKU_NAME, SKU_ID, vm).getViewPanel();
			}
		});
        SKU_NAME.setIcons(searchPicker);
        SKU_NAME.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(!ObjUtil.isNotNull(SKU_NAME.getValue())){
					SKU_NAME.setValue("");
	        	    SKU_ID.setValue("");
				}
				
			}
		});
	}
	
	/**
	 * 插入用户日志信息
	 * @author yuanlei
	 * @param action_descr  操作内容
	 * @param action_result 操作结果
	 */
	/*public static void insertLog(String action_descr, String action_result, String sql) {
		insertLog(action_descr, action_result, sql, "");
	}*/
	
	/**
	 * 插入用户日志信息
	 * @author yuanlei
	 * @param action_descr  操作内容
	 * @param action_result 操作结果
	 * @param link          链接地址（暂未使用）
	 */
	/*public static void insertLog(String action_descr, String action_result, String sql, String link) {
		SYS_USER user = LoginCache.getLoginUser(); 
		Util.db_async.insertLog(action_descr, action_result, sql, link, user.getUSER_NAME(), user.getDEFAULT_ORG_ID_NAME(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				;
			}

			@Override
			public void onSuccess(Void result) {
				;
			}
			
		});
	}*/
	/**
	 * 将Record转换成map
	 * @param record
	 * @return
	 * @author fanglm
	 */
	public static HashMap<String, String> putRecordToModel(Record record){
		HashMap<String, String> map = new HashMap<String, String>();
		String[] keys = record.getAttributes();
		for(int i=0;i<keys.length;i++){
			if(keys[i].indexOf("selection") < 0){
				map.put(keys[i], record.getAttribute(keys[i]));
			}
		}
		return map;
	}
	
	/**
	 * 将修改的值写入Record，用于修改记录后刷新和定位列表数据
	 * @author yuanlei
	 * @param form  存放FORM的容器
	 * @param table 表格
	 * @param record 表格中的Record记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void updateToRecord(ValuesManager form, ListGrid table, ListGridRecord record) {
		if(record != null) { 
			DynamicForm[] forms = form.getMembers();
			Map<String,Object> map = form.getValues();
	        for(int i = 0; i < forms.length; i++) {
	        	updateToRecord(forms[i], table, record,map);
	        }
	        //2010-12-01 fanglm
	        
	        Object[] obj = map.keySet().toArray();
	        String key ="";
	        for(int i=0;i<obj.length;i++){
	        	 key = (String)obj[i];
	        	 if(key.indexOf("_selection") < 0){
	        		 record.setAttribute(key, map.get(key));
	        	 }
	        }
		}
	}
	
	/**
	 * 将修改的值写入Record，用于修改记录后刷新和定位列表数据
	 * @author fanglm
	 * @param form  存放FORM的容器
	 * @param table 表格
	 * @param record 表格中的Record记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void updateToRecord(DynamicForm form, ListGrid table, ListGridRecord record) {
		if(record != null) { 
//			DynamicForm[] forms = form.getMembers();
			Map<String,Object> map = form.getValues();
//	        for(int i = 0; i < forms.length; i++) {
	        	updateToRecord(form, table, record,map);
//	        }
	        //2010-12-01 fanglm
	        
	        Object[] obj = map.keySet().toArray();
	        String key ="";
	        for(int i=0;i<obj.length;i++){
	        	 key = (String)obj[i];
	        	 if(key.indexOf("_selection") < 0){
	        		 record.setAttribute(key, map.get(key));
	        	 }
	        }
		}
	}
	
	/**
	 * 将修改的值写入Record，用于修改记录后刷新和定位列表数据
	 * @author yuanlei
	 * @param form  界面布局的FORM
	 * @param table 表格
	 * @param record 表格中的Record记录
	 * @return
	 */
	public static void updateToRecord(DynamicForm form, ListGrid table, ListGridRecord record,Map<String,Object> map) {
		String field = "";
    	FormItem[] items = form.getFields();
    	for(int j = 0; j < items.length; j++) {
    		field = items[j].getFieldName();
    		//2010-12-01 fanglm
    		if(map != null){
    			map.remove(field);
    		}
			if(items[j].getClass().equals(SelectItem.class) || items[j].getClass().getSuperclass().equals(SelectItem.class)
					|| items[j].getClass().equals(ComboBoxItem.class) || items[j].getClass().getSuperclass().equals(ComboBoxItem.class)) {
				//下拉框
				if(!ObjUtil.ifObjNull(items[j].getValue(),"").equals(ObjUtil.ifNull(record.getAttribute(field + "_NAME"),""))) {
					record.setAttribute(field + "_NAME", items[j].getDisplayValue());
					//2010-12-01 fanglm
					record.setAttribute(field, items[j].getValue().toString());
				}
//				if(!ObjUtil.ifObjNull(items[j].getValue(),"").equals(ObjUtil.ifNull(record.getAttribute(field.substring(0,field.length()-3) + "_NAME"),""))){
//					record.setAttribute(field.substring(0,field.length()-3) + "_NAME", items[j].getDisplayValue());
//				}
				
				if(table.getField(field) != null && !ObjUtil.ifObjNull(items[j].getValue(),"").equals(ObjUtil.ifNull(record.getAttribute(field),""))) {
					record.setAttribute(field, ObjUtil.ifObjNull(items[j].getValue(),"").toString());
				}
			}
			else if(items[j].getClass().equals(CheckboxItem.class) || items[j].getClass().getSuperclass().equals(CheckboxItem.class)) {
				//复选框
				if(!ObjUtil.ifObjNull(items[j].getValue(),"").equals(ObjUtil.ifNull(record.getAttribute(field),""))) {
					if(ObjUtil.isNotNull(items[j].getValue())) {
						String flag = "Y".equals(items[j].getValue().toString()) || "true".equals(items[j].getValue().toString()) ? "true" : "false";
						record.setAttribute(field, Boolean.valueOf(flag));
					}
					else {
						record.setAttribute(field, false);
					}
				}
			}
			else {
				//其他
				if(!ObjUtil.ifObjNull(items[j].getValue(),"").equals(ObjUtil.ifNull(record.getAttribute(field),""))) {
				    record.setAttribute(field, items[j].getValue());
				}
			}
    	}
		return;
	}
	
	
	
	public static String iff(Object obj,String default_value){
		if(obj == null){
			return default_value;
			
		}else{
			return obj.toString();
		}
	}
	
	/**
	 * 获取client_id.properties配置文件里的注释（用于写用户日志）
	 * @author yuanlei
	 * @param tableName 配置文件中对应的表名
	 * @return
	 */
	public static String[] getPropTitle(String tableName) {
		/*String prop_attr = LoginCache.getClientProp().get(tableName + "_NAME");
		if(ObjUtil.isNotNull(prop_attr)) {
			return prop_attr.split(",");
		}*/
		return new String[1];
	} 
	/**
	 * 获取client_id.properties配置文件里的字段(用于写用户日志)
	 * @author yuanlei
	 * @param tableName 配置文件中对应的表名
	 * @return
	 */
	public static String[] getPropField(String tableName) {
		/*String prop_attr = LoginCache.getClientProp().get(tableName + "_FIELD");
		if(ObjUtil.isNotNull(prop_attr)) {
			return prop_attr.split(",");
		}*/
		return new String[1];
	}
	
	/**
	 * 更新操作时，生成用户日志的描述（日志要求写入由什么变为什么）
	 * @author yuanlei
	 * @param origin_map
	 * @param modify_map
	 * @param table
	 * @return
	 */
	public static String getUpdateLog(Map<String, String> origin_map, Map<String, String> modify_map, String table) {
		String result = " ";
		int len = 0;
		String[] titles = getPropTitle(table);
		String[] fields = getPropField(table);
		if(titles.length < fields.length) {
			len = titles.length;
		}
		else {
			len = fields.length;
		}
		StringBuffer sf = new StringBuffer();
		for(int i = 0; i < len; i++) {
			if(ObjUtil.isNotNull(fields[i]) && ObjUtil.isNotNull(origin_map.get(fields[i]))
					&& ObjUtil.isNotNull(modify_map.get(fields[i])) && !origin_map.get(fields[i]).equals(modify_map.get(fields[i]))) {
				sf.append(",");
				sf.append(titles[i]);
				sf.append(StaticRef.ACT_FROM);
				sf.append("【");
				sf.append(origin_map.get(fields[i]));
				sf.append("】");
				sf.append(StaticRef.ACT_TO);
				sf.append("【");
				sf.append(modify_map.get(fields[i]));
				sf.append("】");
				//result += "," + titles[i] + StaticRef.ACT_FROM + "【" + origin_map.get(fields[i]) + "】" 
				//		+ StaticRef.ACT_TO + "【" +modify_map.get(fields[i]) + "】";
			}
		}
		result = sf.toString();
		if(result.trim().length() > 0) { 
			result = result.substring(1);
		}
		return result;
	}
	public static String getFlag(Object obj){
		if(obj == null || obj.toString().equals("false")){
			return "N";
		}else{
			return "Y";
		}
	}
	
	public static native String getODR_NO() /*-{
		var now = new Date();
		var year=now.getYear()-100;
		var month=now.getMonth()+1;
		var day=now.getDate();
		var hour=now.getHours();
		var minute=now.getMinutes();
		var second=now.getSeconds();				
		return 'YH' + year + month + day + hour + minute + second;
		
	}-*/;
	
	
	public static void initGridEditVehicle(ListGridField field, String where) {
		initComboValue(field, "BAS_VEHICLE", "PLATE_NO", "PLATE_NO", where, " order by SHOW_SEQ ASC");
	}
	
	/**
	 * 单据状态
	 * @author yuanlei
	 * @param comboWidget
	 */
	public static void initStatus(FormItem comboWidget, String status,String default_val) {
		initStatusComboValue(comboWidget, "BAS_CODES", "CODE", "NAME_C", status, " order by show_seq asc",default_val);
	}
	/**
	 * 初始化列表日期控件 格式YYYY-MM-DD
	 * @param field ListGridField
	 * @author fanglm
	 */
	public static void initListGridDate(ListGridField field){
		SGDate date = new SGDate("", "");
		field.setEditorType(date);
	}
	
	/**
	 * 初始化列表日期控件 格式YYYY-MM-DD HH:MM
	 * @param field ListGridField
	 * @author fanglm
	 */
	public static void initListGridDateTime(ListGridField field){
		SGDateTime date = new SGDateTime("", "");
		field.setEditorType(date);
	}
	
	/**
	 * 日期控件，xxForm-xxTo日期大小判断，xxForm带入xxTO
	 * @param form
	 * @param to
	 * @author fanglm
	 */
	public static void initDateFromTO(final DateItem form,final DateItem to){
		//从日期写入到日期
		form.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
				Object dateFrom = event.getValue();
				if(ObjUtil.isNotNull(dateFrom)){
					to.setValue(dateFrom.toString());
				}
				
			}
		});
		
		//从日期小于到日期
		to.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
				Object dateTo = event.getValue();
				Object dateFrom = form.getValue();
				if(ObjUtil.isNotNull(dateTo) && ObjUtil.isNotNull(dateFrom)){
					if(!DateUtil.isAfter(dateFrom.toString(), dateTo.toString())){
						to.setValue(dateFrom.toString());
					}
				}
				
			}
		});
	}
	
	/**
	 *  界面按钮状态初始与联动
	 * @param add_map 新增、删除按钮组
	 * @param save_map 保存、取消按钮组
	 * @param boo true初始化新增按钮组可用
	 * @author fanglm
	 */
	public static void initBtnChange(HashMap<String, IButton> add_map,HashMap<String, IButton> save_map,boolean boo){
		if(boo){
			enableOrDisables(add_map,true);
			enableOrDisables(save_map, false);
		}else{
			enableOrDisables(add_map, false);
			enableOrDisables(save_map, true);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void enableOrDisables(final HashMap tmps, boolean b) {
		if (tmps != null) {
			Iterator it = tmps.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry m=(Map.Entry)it.next();
				String key = (String)m.getKey();
				if (tmps.get(key) instanceof IButton) {
					IButton action = (IButton) tmps.get(key);
					setButtonEnabled(key, action, b);
				} else if (tmps.get(key) instanceof ButtonItem) {
					ButtonItem button = (ButtonItem) tmps.get(key);
					setButtonItemEnabled(key, button, b);
					
				}

			}
		}
	}
	

	public static void setButtonEnabled(String id, IButton action, boolean b) {
//		if (hasPrivilege(id)) {// 有权限
		if(b){
			action.enable();
			action.setShowDisabled(true);
		}else{
			action.disable();
			action.setShowDisabled(true);
		}
	}
	
	public static void setButtonItemEnabled(String id, ButtonItem action, boolean b) {
//		if (hasPrivilege(id)) {// 有权限
		if(b){
			action.enable();
		}else{
			action.disable();
		}
	}
	
	/**
	 * 列表操作，显示小数位控制。
	 * @param field 列表字段
	 * @param type	小数类型
	 * @author fanglm
	 */
	public static void initFloatListField(final ListGridField field,final String type){
		//设为float格式
		field.setType(ListGridFieldType.FLOAT);
		field.setCellFormatter(new CellFormatter() {  
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
            	String val;
            	NumberFormat nf = NumberFormat.getFormat(type);  
                if(record.getAttribute(field.getName()) == null){
                	val = nf.format(0.00);
                }else{
                	 try {  
                     	val = nf.format(Double.valueOf(ObjUtil.ifObjNull(value,0).toString()));
                         return val;  
                     } catch (Exception e) {
                     	e.printStackTrace();
                         return value.toString();  
                     }  
                }
                return val;
            }  
        });  
	}
	/**
	 * Form操作，显示小数位控制。
	 * @param item 文本字段
	 * @param type	小数类型
	 * @author fanglm
	 */
	public static void initFloatTextItem(final TextItem item,final String type){
		//正则校验，只准输入数字和小数点
		item.setKeyPressFilter("[0-9.]"); 
		item.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				 String val;
				 String mask = type.replace(",", "");
				 NumberFormat nf = NumberFormat.getFormat(mask);  
				if(!ObjUtil.isNotNull(item.getValue())){ 
					val = "0";
				}else{
					val = item.getValue().toString();
				}
                try {  
                	val = nf.format(Double.valueOf(val));
                } catch (Exception e) {
                	e.printStackTrace();
                }
                item.setValue(val);
			}
		});
	}
	
	public static void initDateTime(final SGTable table, final ListGridField date_item) {
		initDateTime(table, date_item, DateDisplayFormat.TOJAPANSHORTDATETIME);
	}
	
	public static void initDate(final SGTable table, final ListGridField date_item) {
		initDateTime(table, date_item, DateDisplayFormat.TOJAPANSHORTDATE);
	}
	
	
	/**
	 * 初始化列表操作时间控件
	 * @param table
	 * @param itemRow
	 * @param date_item
	 * @param format
	 * @param top
	 * @param left
	 */
	public static void initDateTime(final SGTable table, final ListGridField date_item,final DateDisplayFormat format){
		FormItemIcon icon = new FormItemIcon();
		date_item.setIcons(icon);
		date_item.setShowSelectedIcon(true);
		table.addEditorEnterHandler(new EditorEnterHandler() {

			@Override
			public void onEditorEnter(EditorEnterEvent event) {
				date_item.setAttribute("ROW_NUM", event.getRowNum());
			}
			
		});
		icon.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				int row = date_item.getAttributeAsInt("ROW_NUM");
				int top = 0;
				int left = 0;
				int x = EventHandler.getX() - 100;
				int y = EventHandler.getY() + 10;
				int screen_y = Page.getScrollHeight() + Page.getScrollTop();
				int screen_x = Page.getWidth() + Page.getScrollLeft();
				if(screen_y - y > 150) {
					top = y;
				}
				else {
					top = y - 200;
				}
				if(screen_x - x > 180) {
					left = x;
				}
				else {
					left = x - 180;
				}
				if(dateWin != null) {
					dateWin.destroy();
					dateWin = null;
				}
				dateWin = new DateChooserWin(table,row, date_item.getName(),format,Integer.toString(top),Integer.toString(left));
			}
		});
	}
	
	public static void initDateTime(final ValuesManager vm,final TextItem date_item) {
		initDateTime(vm, date_item, DateDisplayFormat.TOJAPANSHORTDATETIME);
	}
	
	public static void initDate(final ValuesManager vm,final TextItem date_item) {
		initDateTime(vm, date_item, DateDisplayFormat.TOJAPANSHORTDATE);
	}
	/**
	 * 初始化Form操作时间控件
	 * @param table
	 * @param itemRow
	 * @param date_item
	 * @param format
	 * @param top
	 * @param left
	 */
	public static void initDateTime(final ValuesManager vm,final TextItem date_item, final DateDisplayFormat format){
		FormItemIcon icon = new FormItemIcon();
		date_item.setIcons(icon);
		date_item.setShowIcons(true);
		icon.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				int top = 0;
				int left = 0;
				int x = EventHandler.getX() - 100;
				int y = EventHandler.getY() + 10;
				int screen_y = Page.getScrollHeight() + Page.getScrollTop();
				int screen_x = Page.getWidth() + Page.getScrollLeft();
				if(screen_y - y > 150) {
					top = y;
				}
				else {
					top = y - 195;
				}
				if(screen_x - x > 180) {
					left = x;
				}
				else {
					left = x - 175;
				}
				if(dateWin != null) {
					dateWin.destroy();
					dateWin = null;
				}
				dateWin = new DateChooserWin(vm,date_item.getName(),format,Integer.toString(top),Integer.toString(left));
			}
		});
	}
	
	
	public static void initDateTime(final SGPanel panel,final TextItem date_item) {
		initDateTime(panel, date_item, DateDisplayFormat.TOJAPANSHORTDATETIME);
	}
	
	public static void initDate(final SGPanel panel,final TextItem date_item) {
		initDateTime(panel, date_item, DateDisplayFormat.TOJAPANSHORTDATE);
	}
	/**
	 * 初始化panel操作时间控件
	 * @param panel
	 * @param itemRow
	 * @param date_item
	 * @param format
	 * @param top
	 * @param left
	 */
	public static void initDateTime(final SGPanel panel,final TextItem date_item,final DateDisplayFormat format ){

		FormItemIcon icon = new FormItemIcon();
		date_item.setIcons(icon);
		date_item.setShowIcons(true);
		icon.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				int top = 0;
				int left = 0;
				int x = EventHandler.getX() - 100;
				int y = EventHandler.getY() + 10;
				int screen_y = Page.getScrollHeight() + Page.getScrollTop();
				int screen_x = Page.getWidth() + Page.getScrollLeft();
				if(screen_y - y > 150) {
					top = y;
				}
				else {
					top = y - 195;
				}
				if(screen_x - x > 180) {
					left = x;
				}
				else {
					left = x - 175;
				}
				if(dateWin != null) {
					dateWin.destroy();
					dateWin = null;
				}
				dateWin = new DateChooserWin(panel,date_item.getName(),format,Integer.toString(top),Integer.toString(left));
			}
		});
	
	}
	
	public static native String getCurTime() /*-{
       //系统当前时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate();
		var hour=now.getHours();
		var minute=now.getMinutes();
		if (minute < 10) {
		    minute = "0" + minute;
		}
		var res = year+"/"+month+"/"+ day + " " + hour + ":" + minute;
		return res;
	}-*/; 
	
	public static native String getPreDay() /*-{
	   //系统当前最早时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate();	
		var res = year+"/"+month+"/"+ day + " 00:00";
		return res;
	
	}-*/; 
	
	public static native String getLatestDay() /*-{
	   //系统当前最晚时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate();	
		var res = year+"/"+month+"/"+ day + " 23:59";
		return res;
	
	}-*/; 
	
	public static native String getYesPreDay() /*-{
	
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate();
		if(day==1){
			month=now.getMonth();
			if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
		      day = 31;
			}else if (month == 2 ){
				if(year % 4 == 0)
				   day = 29;
				else
				   day = 28;
			}else{
				day = 30;
			}
		  month=now.getMonth();
		}else{
			day=now.getDate()-1;
		}
		var res = year+"/"+month+"/"+ day + " 00:00";
		return res;
	
	}-*/; 
	public static native String getYesLatestDay() /*-{
	   //昨天最晚时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate();
		if(day==1){
			month=now.getMonth();
			if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
		      day = 31;
			}else if (month == 2 ){
				if(year % 4 == 0)
				   day = 29;
				else
				   day = 28;
			}else{
				day = 30;
			}
		  month=now.getMonth();
		}else{
			day=now.getDate()-1;
		}	
		var res = year+"/"+month+"/"+ day + " 23:59";
		return res;
	
	}-*/; 
	public static native String getMonthPreDay() /*-{
	  //本月最早时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate()-1;	
		var res = year+"/"+month+"/"+ 1 + " 00:00";
		return res;
	
	}-*/; 
	public static native String getMonthLatestDay() /*-{
	   //本月最晚时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day = 30;
		if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
			day = 31;
		}else if (month == 2 ){
			if(year % 4 == 0)
				day = 29;
			else
				day = 28
		}else{
			day = 30;
		}
		
		var res = year+"/"+month+"/"+ day + " 23:59";
		return res;

    }-*/; 
	
	public static native String getWeekPreDay() /*-{
	   //本星期最早时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate();
		
		if(new Date().getDay()==0){
		    week="周日";
		    day=now.getDate();
		}else if(new Date().getDay()==1){
		    week="周一";
		    if(day<=1){
		    	if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
		    		day=now.getDate()+31-1
		        	month=now.getMonth();
				}else if(month == 2){
					if(year % 4 == 0){
						day = 29;
						day=now.getDate()+29-1
						month=now.getMonth();
					}else{
						day = 28
						day=now.getDate()+28-1
		        	    month=now.getMonth();
					}
				}else{
					    day=now.getDate()+30-1
		        	    month=now.getMonth();
				}		
		    }else {
		    	day=now.getDate()-1;
		    	month=now.getMonth()+1;
		    }
		   
		}else if(new Date().getDay()==2){
			week="周二"; 
			if(day<=2){
		    	if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
		    		day=now.getDate()+31-2
		        	month=now.getMonth();
				}else if(month == 2){
					if(year % 4 == 0){
						day = 29;
						day=now.getDate()+29-2
						month=now.getMonth();
					}else{
						day = 28
						day=now.getDate()+28-2
		        	    month=now.getMonth();
					}
				}else{
					    day=now.getDate()+30-2
		        	    month=now.getMonth();
				}		
		    }else {
		    	day=now.getDate()-2;
		    	month=now.getMonth()+1;
		    }
		}else if(new Date().getDay()==3){
			week="周三";
			if(day<=3){
		    	if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
		    		day=now.getDate()+31-3
		        	month=now.getMonth();
				}else if(month == 2){
					if(year % 4 == 0){
						day = 29;
						day=now.getDate()+29-3
						month=now.getMonth();
					}else{
						day = 28
						day=now.getDate()+28-3
		        	    month=now.getMonth();
					}
				}else{
					    day=now.getDate()+30-3
		        	    month=now.getMonth();
				}	
		    }else {
		    	day=now.getDate()-3;
		    	month=now.getMonth()+1;
		    }
			
		}else if(new Date().getDay()==4){
			week="周四";
			if(day<=4){
		    	if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
		    		day=now.getDate()+31-4
		        	month=now.getMonth();
				}else if(month == 2){
					if(year % 4 == 0){
						day = 29;
						day=now.getDate()+29-4
						month=now.getMonth();
					}else{
						day = 28
						day=now.getDate()+28-4
		        	    month=now.getMonth();
					}
				}else{
					    day=now.getDate()+30-4
		        	    month=now.getMonth();
				}		
		    }else {
		    	day=now.getDate()-4;
		    	month=now.getMonth()+1;
		    }
			
		}else if(new Date().getDay()==5){
			week="周五";
			if(day<=5){
				day=now.getDate();
		    	if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
		    		day=now.getDate()+31-5;
		        	month=now.getMonth();
				}else if(month == 2){
					if(year % 4 == 0){
						day = 29;
						day=now.getDate()+29-5;
						month=now.getMonth();
					}else{
						day = 28
						day=now.getDate()+28-5;
		        	    month=now.getMonth();
					}
				}else{
					    day=now.getDate()+30-5;
		        	    month=now.getMonth();
				}	
		    }else {
		    	day=now.getDate()-5;
		    	month=now.getMonth()+1;
		    }
			
		}else if(new Date().getDay()==6){
			week="周六";
			if(day<=6){
				if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
		    		day=now.getDate()+31-6
		        	month=now.getMonth();
				}else if(month == 2){
					if(year % 4 == 0){
						day = 29;
						day=now.getDate()+29-6
						month=now.getMonth();
					}else{
						day = 28
						day=now.getDate()+28-6
		        	    month=now.getMonth();
					}
				}else{
					    day=now.getDate()+30-6
		        	    month=now.getMonth();
				}
		    }else {
		    	day=now.getDate()-6;
		    	month=now.getMonth()+1;
		    }
			
		}	
		
		
		
		var res = year+"/"+month+"/"+ day + " 00:00";
		return res;
	
	}-*/; 
	public static native String getWeekLatestDay() /*-{
	   //本星期最晚时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate();
		if(new Date().getDay()==0){
		    week="周日";
		    
		    	if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
			    	if(day>26){
			    		if(now.getDate()-27==0){
			    	  		day=now.getDate()-27+31;
			    	   		month=now.getMonth()+1;
			    		}else
			    		    day=now.getDate()-27;
			    	   		month=now.getMonth()+2;
			    	   		
			    	}else if(day<=25){
		      	      	day=now.getDate()+6;
		      	      	month=now.getMonth()+1;
		    		}
		    		
		    	}else if(month == 2){
		    		if(year % 4 == 0){
			    		if(day>24){
			    			if(now.getDate()-25==0){
				    			day=now.getDate()-25+29;
				    	   		month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-25;
				    	   		month=now.getMonth()+2;
			    		}else if(day<=24){
		      	      		day=now.getDate()+6;
		      	      		month=now.getMonth()+1;
		    			}
		    		
		    		}else{
			    		if(day>23){
			    			if(now.getDate()-24==0){
			    				day=now.getDate()-24+28;
			    	   			month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-24;
			    	   	   		month=now.getMonth()+2;
			    	   	   		
			    		}else if(day<=23){
		      	      		day=now.getDate()+6;
		      	      		month=now.getMonth()+1;
		    			}
		    		}
		    		
		    	}else{
			    	
			    	if(day>25){
			    		if(now.getDate()-26==0){
			    			day=now.getDate()-26+30;
			    	   		month=now.getMonth()+1;
			    		}else
			    		   day=now.getDate()-26;
			    	   	   month=now.getMonth()+2;
			    	   		
			    	}else if(day<=24){
		      	      day=now.getDate()+6;
		      	      month=now.getMonth()+1;
		    		}
		    			
		    	}
		    
		}else if(new Date().getDay()==1){
		      week="周一";
		      if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
			    	if(day>25){
			    		if(now.getDate()-26==0){
			    	  		day=now.getDate()-26+31;
			    	   		month=now.getMonth()+1;
			    		}else
			    		    day=now.getDate()-26;
			    	   		month=now.getMonth()+2;
			    	   		
			    	}else if(day<=25){
		      	      	day=now.getDate()+5;
		      	      	month=now.getMonth()+1;
		    		}
		    		
		    	}else if(month == 2){
		    		if(year % 4 == 0){
			    		if(day>23){
			    			if(now.getDate()-24==0){
				    			day=now.getDate()-24+29;
				    	   		month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-24;
				    	   		month=now.getMonth()+2;
			    		}else if(day<=23){
		      	      		day=now.getDate()+5;
		      	      		month=now.getMonth()+1;
		    			}
		    		
		    		}else{
			    		if(day>22){
			    			if(now.getDate()-23==0){
			    				day=now.getDate()-23+28;
			    	   			month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-23;
			    	   	   		month=now.getMonth()+2;
			    	   	   		
			    		}else if(day<=22){
		      	      		day=now.getDate()+5;
		      	      		month=now.getMonth()+1;
		    			}
		    		}
		    		
		    	}else{
			    	
			    	if(day>24){
			    		if(now.getDate()-25==0){
			    			day=now.getDate()-25+30;
			    	   		month=now.getMonth()+1;
			    		}else
			    		   day=now.getDate()-25;
			    	   	   month=now.getMonth()+2;
			    	   		
			    	}else if(day<=24){
		      	      day=now.getDate()+5;
		      	      month=now.getMonth()+1;
		    		}
		    			
		    	}
		}else if(new Date().getDay()==2){
		
				week="周二"; 
			if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
			    	if(day>26){
			    		if(now.getDate()-27==0){
			    	  		day=now.getDate()-27+31;
			    	   		month=now.getMonth()+1;
			    		}else
			    		    day=now.getDate()-27;
			    	   		month=now.getMonth()+2;
			    	   		
			    	}else if(day<=26){
		      	      	day=now.getDate()+4;
		      	      	month=now.getMonth()+1;
		    		}
		    		
		    	}else if(month == 2){
		    		if(year % 4 == 0){
			    		if(day>24){
			    			if(now.getDate()-25==0){
				    			day=now.getDate()-25+29;
				    	   		month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-25;
				    	   		month=now.getMonth()+2;
			    		}else if(day<=24){
		      	      		day=now.getDate()+4;
		      	      		month=now.getMonth()+1;
		    			}
		    		
		    		}else{
			    		if(day>23){
			    			if(now.getDate()-24==0){
			    				day=now.getDate()-24+28;
			    	   			month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-24;
			    	   	   		month=now.getMonth()+2;
			    	   	   		
			    		}else if(day<=23){
		      	      		day=now.getDate()+4;
		      	      		month=now.getMonth()+1;
		    			}
		    		}
		    		
		    	}else{
			    	
			    	if(day>25){
			    		if(now.getDate()-26==0){
			    			day=now.getDate()-26+30;
			    	   		month=now.getMonth()+1;
			    		}else
			    		   day=now.getDate()-26;
			    	   	   month=now.getMonth()+2;
			    	   		
			    	}else if(day<=25){
		      	      day=now.getDate()+4;
		      	      month=now.getMonth()+1;
		    		}
		    			
		    	}
			
		}else if(new Date().getDay()==3){
			week="周三";
			if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
			    	if(day>27){
			    		if(now.getDate()-28==0){
			    	  		day=now.getDate()-28+31;
			    	   		month=now.getMonth()+1;
			    		}else
			    		    day=now.getDate()-28;
			    	   		month=now.getMonth()+2;
			    	   		
			    	}else if(day<=27){
		      	      	day=now.getDate()+3;
		      	      	month=now.getMonth()+1;
		    		}
		    		
		    	}else if(month == 2){
		    		if(year % 4 == 0){
			    		if(day>25){
			    			if(now.getDate()-26==0){
				    			day=now.getDate()-26+29;
				    	   		month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-26;
				    	   		month=now.getMonth()+2;
			    		}else if(day<=25){
		      	      		day=now.getDate()+3;
		      	      		month=now.getMonth()+1;
		    			}
		    		
		    		}else{
			    		if(day>24){
			    			if(now.getDate()-25==0){
			    				day=now.getDate()-25+28;
			    	   			month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-25;
			    	   	   		month=now.getMonth()+2;
			    	   	   		
			    		}else if(day<=24){
		      	      		day=now.getDate()+3;
		      	      		month=now.getMonth()+1;
		    			}
		    		}
		    		
		    	}else{
			    	
			    	if(day>26){
			    		if(now.getDate()-27==0){
			    			day=now.getDate()-27+30;
			    	   		month=now.getMonth()+1;
			    		}else
			    		   day=now.getDate()-27;
			    	   	   month=now.getMonth()+2;
			    	   		
			    	}else if(day<=26){
		      	      day=now.getDate()+3;
		      	      month=now.getMonth()+1;
		    		}
		    			
		    	}
		}else if(new Date().getDay()==4){
			week="周四";
			if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
			    	if(day>28){
			    		if(now.getDate()-29==0){
			    	  		day=now.getDate()-29+31;
			    	   		month=now.getMonth()+1;
			    		}else
			    		    day=now.getDate()-29;
			    	   		month=now.getMonth()+2;
			    	   		
			    	}else if(day<=28){
		      	      	day=now.getDate()+2;
		      	      	month=now.getMonth()+1;
		    		}
		    		
		    	}else if(month == 2){
		    		if(year % 4 == 0){
			    		if(day>26){
			    			if(now.getDate()-27==0){
				    			day=now.getDate()-27+29;
				    	   		month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-27;
				    	   		month=now.getMonth()+2;
			    		}else if(day<=26){
		      	      		day=now.getDate()+2;
		      	      		month=now.getMonth()+1;
		    			}
		    		
		    		}else{
			    		if(day>25){
			    			if(now.getDate()-26==0){
			    				day=now.getDate()-26+28;
			    	   			month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-26;
			    	   	   		month=now.getMonth()+2;
			    	   	   		
			    		}else if(day<=25){
		      	      		day=now.getDate()+2;
		      	      		month=now.getMonth()+1;
		    			}
		    		}
		    		
		    	}else{
			    	
			    	if(day>27){
			    		if(now.getDate()-28==0){
			    			day=now.getDate()-28+30;
			    	   		month=now.getMonth()+1;
			    		}else
			    		   day=now.getDate()-28;
			    	   	   month=now.getMonth()+2;
			    	   		
			    	}else if(day<=27){
		      	      day=now.getDate()+2;
		      	      month=now.getMonth()+1;
		    		}
		    			
		    	}
		}else if(new Date().getDay()==5){
			week="周五";
			if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
			    	if(day>29){
			    		if(now.getDate()-30==0){
			    	  		day=now.getDate()-30+31;
			    	   		month=now.getMonth()+1;
			    		}else
			    		    day=now.getDate()-30;
			    	   		month=now.getMonth()+2;
			    	   		
			    	}else if(day<=29){
		      	      	day=now.getDate()+1;
		      	      	month=now.getMonth()+1;
		    		}
		    		
		    	}else if(month == 2){
		    		if(year % 4 == 0){
			    		if(day>27){
			    			if(now.getDate()-28==0){
				    			day=now.getDate()-28+29;
				    	   		month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-28;
				    	   		month=now.getMonth()+2;
			    		}else if(day<=27){
		      	      		day=now.getDate()+1;
		      	      		month=now.getMonth()+1;
		    			}
		    		
		    		}else{
			    		if(day>26){
			    			if(now.getDate()-27==0){
			    				day=now.getDate()-27+28;
			    	   			month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-27;
			    	   	   		month=now.getMonth()+2;
			    	   	   		
			    		}else if(day<=26){
		      	      		day=now.getDate()+1;
		      	      		month=now.getMonth()+1;
		    			}
		    		}
		    		
		    	}else{
			    	
			    	if(day>28){
			    		if(now.getDate()-29==0){
			    			day=now.getDate()-29+30;
			    	   		month=now.getMonth()+1;
			    		}else
			    		   day=now.getDate()-29;
			    	   	   month=now.getMonth()+2;
			    	   		
			    	}else if(day<=28){
		      	      day=now.getDate()+1;
		      	      month=now.getMonth()+1;
		    		}
		    			
		    	}
		}else if(new Date().getDay()==6){
			week="周六";
			if(month==1 || month==3 ||month==5 || month ==7 ||month ==8 ||month ==10 || month==12){
			    	if(day>30){
			    		if(now.getDate()-31==0){
			    	  		day=now.getDate()-31+31;
			    	   		month=now.getMonth()+1;
			    		}else
			    		    day=now.getDate()-31;
			    	   		month=now.getMonth()+2;
			    	   		
			    	}else if(day<=30){
		      	      	day=now.getDate();
		      	      	month=now.getMonth()+1;
		    		}
		    		
		    	}else if(month == 2){
		    		if(year % 4 == 0){
			    		if(day>28){
			    			if(now.getDate()-29==0){
				    			day=now.getDate()-29+29;
				    	   		month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-29;
				    	   		month=now.getMonth()+2;
			    		}else if(day<=28){
		      	      		day=now.getDate();
		      	      		month=now.getMonth()+1;
		    			}
		    		
		    		}else{
			    		if(day>27){
			    			if(now.getDate()-28==0){
			    				day=now.getDate()-28+28;
			    	   			month=now.getMonth()+1;
			    			}else
			    				day=now.getDate()-28;
			    	   	   		month=now.getMonth()+2;
			    	   	   		
			    		}else if(day<=27){
		      	      		day=now.getDate();
		      	      		month=now.getMonth()+1;
		    			}
		    		}
		    		
		    	}else{
			    	
			    	if(day>29){
			    		if(now.getDate()-30==0){
			    			day=now.getDate()-30+30;
			    	   		month=now.getMonth()+1;
			    		}else
			    		   day=now.getDate()-30;
			    	   	   month=now.getMonth()+2;
			    	   		
			    	}else if(day<=29){
		      	      day=now.getDate();
		      	      month=now.getMonth()+1;
		    		}
		    			
		    	}
		}
		var res = year+"/"+month+"/"+ day + " 23:59";
		return res;
	
	}-*/; 
	
	public static native String getAllYearPreDay() /*-{
	   //一年最早时间
		var now = new Date();
		var year=now.getFullYear();
		var month=1;
		var day=now.getDate();	
		var res = year+"/"+month+"/"+ 1 + " 00:00";
		return res;

	}-*/; 
	
	public static native String getAllYearLatestDay() /*-{
	   //一年最晚时间
		var now = new Date();
		var year=now.getFullYear();
		var month=12;
		var day=now.getDate();	
		var res = year+"/"+month+"/"+ 31 + " 23:59";
		return res;
	
	}-*/; 
	
	public static native String getHalfYearPreDay() /*-{
	   //半年度最早时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate();
		if(0<month && month<=6){
			month=1;
		}else{
			month=7;
		}
		var res = year+"/"+month+"/"+ 1 + " 00:00";
		return res;
	
	}-*/; 
	
	public static native String getHalfYearLatestDay() /*-{
	   //半年度最晚时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate();
		if(0<month && month<=6){
			month=6;
			day=31;
		}else{
			month=12;
			day=31;
		}
			
		var res = year+"/"+month+"/"+ day + " 23:59";
		return res;
	
	}-*/; 
	public static native String getQuarterPreDay() /*-{
		//本季度最早时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate();
		if(1<=month && month <=3 ){
			month=1;
		}else if(4<=month && month <=6){
			month=4;
		}else if(7<=month && month <=9){
			month=7;
		}else if(10<=month && month <=12){
			month=10;
		}
		var res = year+"/"+month+"/"+ 1 + " 00:00";
		return res;
	
	}-*/; 
	
	public static native String getQuarterLatestDay() /*-{
	   //本季度最晚时间
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var day=now.getDate();
		if(1<=month && month <=3 ){
			month=3;
			day=31;
		}else if(4<=month && month <=6){
			month=6;
			day=30;
		}else if(7<=month && month <=9){
			month=9;
			day=30;
		}else if(10<=month && month <=12){
			month=12;
			day=31;
		}
		var res = year+"/"+month+"/"+ day + " 23:59";
		return res;
	
	}-*/; 
	
	/**
	 * 设置下拉框的值（不使用缓存）
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 * @param where 过滤条件(可省略WHERE)
	 * @param orderby 排序(可省略 ORDER BY)
	 */
	public static void initDefinalCombo(final ListGridField fieldWidget, String sql, String id, String name, final String default_value) {
		final ListGridField combo = fieldWidget;
			async.getComboValue(sql, id, name, new AsyncCallback<LinkedHashMap<String, String>>() {
	
				public void onFailure(Throwable caught) {	
					;
				}
				public void onSuccess(LinkedHashMap<String, String> result) {
					if(result != null && result.size() > 0) {
						Object[] obj = result.keySet().toArray();
						combo.setValueMap(result);
						if(default_value == null){
							combo.setDefaultValue(result.get(obj[1]));
						}
					}
				}					
			});
	}
	
	/**
	 * 根据当前登录用户初始化客户,用于各个查询窗口
	 * @author Lang
	 * @param customer_name
	 * @param customer_id
	 */
	public static void initCustomerByQuery(final ComboBoxItem customer_name,final TextItem customer_id){
		initCustomerByQuery(customer_name, customer_id, true);
	}
	
	/**
	 * 根据当前登录用户初始化客户,用于各个查询窗口
	 * @author Lang
	 * @param customer_name
	 * @param customer_id
	 * @param flag
	 */
	public static void initCustomerByQuery(final ComboBoxItem customer_name,final TextItem customer_id, boolean flag){
		DataSource custDS = UserCustDS.getInstance("VC_CUSTOMER");
		ListGridField CUSTOMER_CNAME = new ListGridField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME());
		ListGridField CUSTOMER_CODE = new ListGridField("CUSTOMER_CODE",Util.TI18N.CUSTOMER_CODE(),80);
		ListGridField CUSTOM_ATTR = new ListGridField("CUSTOM_ATTR",Util.TI18N.CUSTOM_ATTR());
		CUSTOM_ATTR.setHidden(true);
		customer_name.setOptionDataSource(custDS);  
		customer_name.setDisabled(false);
		customer_name.setShowDisabled(false);
		customer_name.setDisplayField("FULL_INDEX"); 
		customer_name.setPickListBaseStyle("myBoxedGridCell");
		customer_name.setPickListWidth(230);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		if(flag){
			criteria.addCriteria("LOGIN_USER",LoginCache.getLoginUser().getUSER_ID());
		}
		customer_name.setPickListCriteria(criteria);
		
		customer_name.setPickListFields(CUSTOMER_CODE, CUSTOMER_CNAME,CUSTOM_ATTR);
		customer_name.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
				final Record selectedRecord  = customer_name.getSelectedRecord();
				if(selectedRecord != null){
					customer_name.setValue(selectedRecord.getAttribute("CUSTOMER_NAME"));
					customer_id.setValue(selectedRecord.getAttribute("CUSTOMER_ID"));
				}
				
			}
		});
		
		customer_name.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(!ObjUtil.isNotNull(customer_name.getValue()) && customer_id != null){
					customer_id.setValue("");
				}
			}
		});
	}
	
	/**
	 * 批量设置下拉框的值
	 * @author yuanlei
	 * @param comboWidget 下拉框控件
	 * @param table 表名
	 * @param id 字段ID
	 * @param name 字段名称
	 * @param where 过滤条件(可省略WHERE)
	 * @param orderby 排序(可省略 ORDER BY)
	 */
	public static void initComboBatch(final ArrayList<ListGridField> fields, String table, String id, String name, String where, String orderby,final String defaultValue) {
		if(fields != null && fields.size() > 0) {
			async.getComboValue(table, id, name, where, orderby, new AsyncCallback<LinkedHashMap<String, String>>() {

				public void onFailure(Throwable caught) {	
					SC.warn(caught.getMessage());
				}
				public void onSuccess(LinkedHashMap<String, String> result) {
					if(result != null && result.size() > 0) {
						Object[] values = result.keySet().toArray();
						for(int m = 0; m < fields.size(); m++) {
							fields.get(m).setValueMap(result);
							
							if(defaultValue != null){
								fields.get(m).setDefaultValue(defaultValue);
							}else{
								fields.get(m).setDefaultValue(values[1].toString());
							}
						}
					}
				}					
			});
		}
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap illegalMapFilter(HashMap map) {
		if(map != null) {
			Object[] iter = map.keySet().toArray();
			String key = "";
			Object value = null;
			for(int i = 0; i < iter.length; i++) {
				key = (String)iter[i];
				value = map.get(key);
				try {
					value = illegalFilter(value);
					map.put(key, value);
				}
				catch(Exception e) {
					key = "";
					value = "";
				}
			}
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static Map illegalMapFilter(Map map) {
		if(map != null) {
			Object[] iter = map.keySet().toArray();
			String key = "";
			Object value = null;
			for(int i = 0; i < iter.length; i++) {
				key = (String)iter[i];
				value = map.get(key);
				try {
					value = illegalFilter(value);
					map.put(key, value);
				}
				catch(Exception e) {
					key = "";
					value = "";
				}
			}
		}
		return map;
	}
	
    /**
     * 非法字符串过滤
     * @author Administrator
     * @param str
     * @return
     */
    private static Object illegalFilter(Object chk_value) {
    	if(chk_value != null && chk_value.getClass().equals(String.class)) {
    		String value = chk_value.toString();
    		if(value.indexOf("<") >= 0 && !value.equals("<") && !value.equals("<=")) {
    			value = value.replaceAll("<", "&lt;");
    		}
    		if(value.indexOf(">") >= 0 && !value.equals(">") && !value.equals(">=")){
    			value = value.replaceAll(">", "&gt;");
    		}
    	 	value = value.replaceAll("\"", "").replaceAll("#", "");
         	value = value.replaceAll("javascript", "").replaceAll("expression", "").replaceAll("alert", "");
         	return value;
    	}
         return chk_value;
    }

	
}