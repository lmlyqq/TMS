package com.rd.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.FUNCTION;
import com.rd.client.obj.system.SYS_USER;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getLoginUserInfo(String strUser, String strPwd, AsyncCallback<SYS_USER> callback);
	void getRecordCount(String tableName, AsyncCallback<List<String>> callback);
	void excuteSQL(String sql, AsyncCallback<String> callback);
	void excuteSQLList(ArrayList<String> sqlList, AsyncCallback<String> callback);
	void getComboValue(String table, String id, String name, String where, String orderby,  AsyncCallback<LinkedHashMap<String, String>> callback);
	void getComboValue2(String table, String id, String name, String where, String orderby,  AsyncCallback<LinkedHashMap<String, String>> callback);
	void getComboValue(String sql, String id, String name, AsyncCallback<LinkedHashMap<String, String>> callback);
	void getServTime(String format, AsyncCallback<String> callback);
	void doInsert(String log, String map, AsyncCallback<String> callback);
	void doInsert(ArrayList<String> log, ArrayList<String> map, ArrayList<String> extra, AsyncCallback<String> callback);
	void doInsert(ArrayList<String> log, ArrayList<String> map, ArrayList<String> extra, boolean isExtraFinaly, AsyncCallback<String> callback);
	void doUpdate(ArrayList<String> log, ArrayList<String> map, AsyncCallback<String> callback);
	void doDelete(ArrayList<String> log, ArrayList<String> map, AsyncCallback<String> callback);
	void getIDSequence(AsyncCallback<String> callback);
	void updatePWD(String id, String oldPwd, String newPWD,
			AsyncCallback<String> callback);
	void saveOrUpdate(String json, String objName, AsyncCallback<String> callback);
	void delete(String json, String objName, AsyncCallback<String> callback);
	void savePrivilege(ArrayList<String> list,String role_id,String login_user,String func_id, String str, AsyncCallback<String> callback);
	void customExportAction(String headers, String filedNames, String sql, HashMap<String, String> paramMap, 
			AsyncCallback<String> callback);
	void exportAction(String headers, String filedNames, String sql,
			AsyncCallback<String> callback);	
	void sfOrderExportAction(String headers, String filedNames, String sql,String odr_no,
			AsyncCallback<String> callback);
	void countChild(String tableName, String colum, String value,
			AsyncCallback<Integer> callback);
	void getCheckResult(String sql, AsyncCallback<String> callback);
	void getHintCode(String content, AsyncCallback<String> callback);
	void getFuncList(AsyncCallback<List<FUNCTION>> callback);
	void execProcedure(ArrayList<String> map, String proName,
			AsyncCallback<String> callback);
	void execProcedure(String json, String proName, AsyncCallback<String> callback);
	void getCarInfo(String vehicle_type, AsyncCallback<List<HashMap<String, String>>> callback);
	void doSave(ArrayList<String> logList,ArrayList<String> jsonList,String appRow, AsyncCallback<String> callback);
	void execPro(HashMap<String, ArrayList<String>> map, String proName,
			AsyncCallback<String> callback);
	void transFollowDoUpdate(ArrayList<String> log, ArrayList<String> map, ArrayList<String> extra, AsyncCallback<String> callback);
//	void loadPrintView(String id,String load_no,HashMap<String, String> shpm_no,boolean isShow,AsyncCallback<String> callback);
	void loadPrintView(String id,String json, String load_no,boolean isShow,AsyncCallback<String> callback);
	void shpmPrintView(String id,String load_id,String type,  AsyncCallback<String> callback);
	void runTimer(String type,AsyncCallback<String> callback);
	void ordloadPrintView(String id, AsyncCallback<String> callback);
	void execProcedure(HashMap<String, String> map, String proName,
			AsyncCallback<String> callback);
	void UnShpmPrintView(String id, String json, boolean isShow,
			AsyncCallback<String> callback);
	void exportByProAction(String headers, String filedNames, String sql,
			String pro_name, AsyncCallback<String> callback);
	void getBasVal(String load_no,String shpm_no,String fee_bas,AsyncCallback<String> callback);
	void getBasVal(String shpm_no,String feeBas,boolean is_rdc,AsyncCallback<String> callback);
	void getBasVal(String shpmNo, String feeBas, AsyncCallback<String> callback);
	void getColName(String v_name, AsyncCallback<String> callback);
	void getU8Order(String type, String custom_odr_no, AsyncCallback<String> callback);
	void dispatchPrintView(String paramName, String paramValue, String printType,AsyncCallback<String> callback);
	void excuteSQLListCheckUn(ArrayList<String> sqlList, String firstExceSql,AsyncCallback<String> callback);
	void excuteSQLListCheckUn1(ArrayList<String> sqlList, String firstExceSql,AsyncCallback<String> callback);
	void excuteSQLListByMap(List<Map<String, String>> listData, String[] checkFields, AsyncCallback<String> callback);
	void queryData(String sql, boolean isSingleData, AsyncCallback<Map<String, Object>> callback);
	
	 void executeScript(String sql, AsyncCallback<List<Map<String, Object>>> callback);
	 void getSFAddrInfo(String sql,AsyncCallback<LinkedHashMap<String,Object>> callback);
	 
	 void getTransNo(AsyncCallback<String> callback);
	 void setTransNo(String transNo, String use_flag, AsyncCallback<String> callback);
	 
	 void insertOrg(String json, AsyncCallback<String> callback);
	 
	 void getTotalResult(String year, String month, int totalType, AsyncCallback<Map<String, List<String>>> callback);
	 
	 void readExcel(String tableName, AsyncCallback<ArrayList<String>> callback);
	 
	 void getFuncList(String role_id, String system, AsyncCallback<List<FUNCTION>> callback);
	 void getUserPrivilege(AsyncCallback<LinkedHashMap<String, String>> callback);  //获取用户权限

	 void createBean(String className,String[] fields,String packagePath,String absolutelyPath,String Implements,String Extends,AsyncCallback<Boolean> callback);
	 
	 void createDS(ArrayList<HashMap<String,String>> list,HashMap<String,String> map,AsyncCallback<Boolean> callback);
	
	 void createHBM(ArrayList<HashMap<String,String>> list,HashMap<String,String> map,AsyncCallback<Boolean> callback);
	 
	 void downLoadAction(String cusId, AsyncCallback<String> callback);
	 
	 void getWeather(String cityName,AsyncCallback<ArrayList<Map<String,String>>> callback);
	 
     void execWebService(LinkedHashMap<String,String> map,String serviceName, String methodName, AsyncCallback<String> callback);

     void RecAdjBillexportAction (String headers, String filedNames, String sql,
 			AsyncCallback<String> callback);	
     void PayAdjBillexportAction (String headers, String filedNames, String sql,
  			AsyncCallback<String> callback);	
     void PayReqBillexportAction (String headers, String filedNames, String sql,
   			AsyncCallback<String> callback);	
     void RecInitBillexportAction (AsyncCallback<String> callback);	
     void PayInitBillexportAction1(AsyncCallback<String> callback);
     void PayInitBillexportAction2(AsyncCallback<String> callback);
     void InvoiceExportAction (String id,AsyncCallback<String> callback);	
     
     //void getFieldName2(List<String> list,String key, String value, String alias, AsyncCallback<List<String>> callback);
}
	
