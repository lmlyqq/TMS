package com.rd.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.rd.client.common.obj.SysParam;
/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("dbgreet")
public interface DBService extends RemoteService {
	boolean isExistRecord(String tableName, String condition);
	LinkedHashMap<String, LinkedHashMap<String, String>> getBizCodes();
	LinkedHashMap<String, LinkedHashMap<String, String>> getStatCodes();
	LinkedHashMap<String, String> getClientProp();
	HashMap<String, String> getUserOrg(String user_id);
	String saveUser(ArrayList<String> map,String proName,String passwd,String salt);
	//获取角色权限 fanglm
	HashMap<String, String> getRolePrivilege(String role_id,String str);
	void insertLog(String action_descr, String action_result, String sql, String link, String user_name, String org_name);
	String getClientIP();
    ArrayList<String> getPageInfo();
    String deletePro(String id,String tableName,String userid);
//    String deletePro(String id,String tableName);
    ArrayList<String> QTY_CONVER(ArrayList<String> valList);
    LinkedHashMap<String, SysParam> getSysParam();
    HashMap<String, String> getDftCustomer();
    LinkedHashMap<String, String> getSHMPNOSum(LinkedHashMap<String, String> crit);
//    HashMap<String, String> getSingleRecord(String flds, String table, String where);
    ArrayList<String> getImageNames(String path);
    boolean isDelete(String path) throws RuntimeException;
    HashMap<String, String> getSingleRecord(String flds, String table, String where, HashMap<String, String> map);
    ArrayList<HashMap<String, String>> getRecord(String flds, String table, String where, HashMap<String, String> map);
    LinkedHashMap<String, String> getUserPrivilege();  //获取用户权限
    LinkedHashMap<String, LinkedHashMap<String, String>> getListCfg();
    HashMap<String, String> execCountSql(String sql);
    //fanglm 获取订单编号
    String getIdSeq(String tableName);
    String getRecordCount(String sql);
    HashMap<String,String> getPost(String load_no,String param,String login_user);
	HashMap<String, String> getRangeDetail(String range_id);
	HashMap<String, String> getOrgAreaInfo(String org_id, String condition);
	String expImageNames(String path,String image);

}
