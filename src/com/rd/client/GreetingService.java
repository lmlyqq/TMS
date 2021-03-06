package com.rd.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.rd.client.common.obj.FUNCTION;
import com.rd.client.obj.system.SYS_USER;
 
/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	SYS_USER getLoginUserInfo(String strUser, String strPwd);
	List<String> getRecordCount(String tableName);
	String excuteSQL(String sql);
	String excuteSQLList(ArrayList<String> sqlList);
	LinkedHashMap<String, String> getComboValue(String table, String id, String name, String where, String orderby);
	LinkedHashMap<String, String> getComboValue2(String table, String id, String name, String where, String orderby);
	LinkedHashMap<String, String> getComboValue(String table, String id, String name);
	String getServTime(String format);
	String getIDSequence();
//	String getInsertSQL(Map map, String keyName);
	String updatePWD(String id,String oldPwd,String newPWD);
	String saveOrUpdate(String json,String objName);
	String delete(String json,String objName);
	String doInsert(String log, String map);
	String doInsert(ArrayList<String> log, ArrayList<String> map, ArrayList<String> extra);
	String doInsert(ArrayList<String> log, ArrayList<String> map, ArrayList<String> extra, boolean isExtraFinaly);
	String doUpdate(ArrayList<String> log, ArrayList<String> map);
	String doDelete(ArrayList<String> log, ArrayList<String> map);
	String savePrivilege(ArrayList<String> list,String role_id,String login_user,String func_id,String str);
	String customExportAction(String headers,String filedNames,String sql, HashMap<String, String> paramMap);
	String exportAction(String headers,String filedNames,String sql);
	String sfOrderExportAction(String headers,String filedNames,String sql,String odr_no);
	String exportByProAction(String headers,String filedNames,String sql,String pro_name);
	int countChild(String tableName,String colum,String value);
	String getCheckResult(String sql);
	String getHintCode(String content);
	List<FUNCTION> getFuncList();
	String execProcedure(ArrayList<String> map,String proName);
	String execProcedure(String json, String proName);
	List<HashMap<String, String>> getCarInfo(String vehicle_type);
	String doSave(ArrayList<String> logList,ArrayList<String> jsonList,String appRow);
	String execPro(HashMap<String,ArrayList<String>> map,String proName);
	String transFollowDoUpdate(ArrayList<String> log, ArrayList<String> map, ArrayList<String> extra);
//	String loadPrintView(String id,String load_no,HashMap<String, String> shpm_no,boolean isShow);
	String loadPrintView(String id,String json, String load_no,boolean isShow); 
	String shpmPrintView(String id,String load_no,String type);
	String UnShpmPrintView(String id,String json,boolean isShow); 
	String ordloadPrintView(String id);
	String runTimer(String type);
	String execProcedure(HashMap<String, String> map, String proName);
	String getBasVal(String load_no,String shpm_no,String fee_bas);
	String getBasVal(String shpm_no,String feeBas, boolean is_rdc);
	String getBasVal(String shpmNo, String feeBas);
	String dispatchPrintView(String paramName, String paramValue, String printType);
	String excuteSQLListCheckUn(ArrayList<String> sqlList, String firstExceSql);
	String excuteSQLListCheckUn1(ArrayList<String> sqlList, String firstExceSql);
	String excuteSQLListByMap(List<Map<String, String>> listData, String[] checkFields);
	Map<String, Object> queryData(String sql, boolean isSingleData);
	
	String getColName(String v_name);
	String getU8Order(String type, String custom_odr_no);
	
	List<Map<String, Object>> executeScript(String sql);
	LinkedHashMap<String,Object> getSFAddrInfo(String sql);
	
	String getTransNo();
	String setTransNo(String transNo, String useFlag);
	
	Map<String, List<String>> getTotalResult(String year, String month, int totalType);
	
	String insertOrg(String json);
	ArrayList<String> readExcel(String tableName);
	List<FUNCTION> getFuncList(String role_id, String system);
    LinkedHashMap<String, String> getUserPrivilege();  //获取用户权限
    
    boolean createBean(String className,String[] fields,String packagePath,String absolutelyPath,String Implements,String Extends);
    
    boolean createDS(ArrayList<HashMap<String,String>> list,HashMap<String,String> map);
    
    boolean createHBM(ArrayList<HashMap<String,String>> list,HashMap<String,String> map);
    
    String downLoadAction(String cusId);
    
    ArrayList<Map<String,String>> getWeather(String cityName);
  
	String execWebService(LinkedHashMap<String,String> map,String serviceName, String methodName);
//收款调整单导出
	String RecAdjBillexportAction(String headers,String filedNames,String sql);
//付款调整单导出	
	String PayAdjBillexportAction(String headers,String filedNames,String sql);
//应付请款单导出
	String PayReqBillexportAction(String headers,String filedNames,String sql);
//应收对账单
	String RecInitBillexportAction();
//应付对账单（项目）
	String PayInitBillexportAction1();
//应付对账单（承运商）
	String PayInitBillexportAction2();
//发票管理
	String InvoiceExportAction(String id);
	
	//List<String> getFieldName2(List<String> list,String key, String value, String alias);
}
