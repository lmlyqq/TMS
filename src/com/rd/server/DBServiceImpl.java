package com.rd.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
//import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;

import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.rd.client.DBService;
import com.rd.client.common.obj.SysParam;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.obj.system.SYS_USER;
import com.rd.server.util.Log4j;
import com.rd.server.util.LoginContent;
import com.rd.server.util.SQLUtil;
import com.rd.server.util.SUtil;

/**
 * The server side implementation of the RPC service.
 */
public class DBServiceImpl extends RemoteServiceServlet implements
		DBService {
	private static final long serialVersionUID = 1L;
	
	private GreetingServiceImpl gs = new GreetingServiceImpl();
	public String dbServer(String input) throws IllegalArgumentException {
		return null;
	}

	/**
	 * 判断记录是否存在
	 * @author yuanlei
	 */
	@SuppressWarnings("unchecked")
	public boolean isExistRecord(String tableName, String condition) {
		boolean bolResult = false;
		Session session = LoginContent.getInstance().getSession();
	    StringBuffer sf = new StringBuffer();
	    sf.append("select count(1) as rec_num from");
	    sf.append(tableName);
	    if(condition.indexOf("where") < 0) {
	    	sf.append(" where ");
	    }
	    sf.append(condition);
	    //Log4j.info(StaticRef.SQL_LOG, sf.toString());
	    Query query = session.createSQLQuery(sf.toString());
	    List<HashMap<String, String>> object = query.list();
        if(object != null && object.size() > 0) {
        	String count = object.get(0).toString();
        	if(Integer.parseInt(count) > 0) {
        		bolResult = true;
        	}
        }
		LoginContent.getInstance().closeSession();
		session = null;
		return bolResult;
	}
	
	/**
	 * 获取指定业务类型的数据字典内容（用作缓存，可在初始化下拉框数据时直接调用）
	 * @author yuanlei
	 */
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, LinkedHashMap<String, String>> getBizCodes() {
		
		LinkedHashMap<String, LinkedHashMap<String, String>> retList = null;
		LinkedHashMap<String, LinkedHashMap<String, String>> ret2List = null;
		//if(retList == null) {
			//String client_id = SUtil.iif(getClientProp().get("client.biz.type"),"ALL");
			
			Session session = LoginContent.getInstance().getSession();
		    StringBuffer sf = new StringBuffer();
		    sf.append("select t_codes.prop_code,t_codes.id,t_codes.code,t_codes.name_c from BAS_CODES t_codes, BAS_CODEPROP t_prop");
		    sf.append(" where t_codes.prop_code = t_prop.prop_code ");
		    //sf.append(" and t_codes.prop_code not in ('TRANS_ODR_STAT', 'SHPM_STAT', 'TRANS_LOAD_STAT', 'ASSIGN_STAT', 'PLAN_STAT', 'LOAD_STAT', 'UNLOAD_STAT', 'LIST_TYP','ACCOUNT_STAT','AUDIT_STAT')");
		    /*if(client_id.compareTo("ALL") != 0) {
		    	sf.append(" and t_prop.biz_type = '");
		    	sf.append(client_id);
		    	sf.append("'");
		    }*/
		    sf.append(" order by t_codes.prop_code,t_codes.default_flag desc,t_codes.show_seq");
		    //Log4j.info(StaticRef.SQL_LOG, sf.toString());
		    Query query = session.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		    List<HashMap> object = query.list();
	        if(object != null && object.size() > 0) {
	        	retList = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	        	ret2List = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	        	String code = ",TRANS_ODR_STAT,SFODR_STAT,SHPM_STAT,TRANS_LOAD_STAT,ASSIGN_STAT,PLAN_STAT,LOAD_STAT,UNLOAD_STAT,LIST_TYP,ACCOUNT_STAT,AUDIT_STAT,";
	        	LinkedHashMap<String, String> code_map;
	        	LinkedHashMap<String, String> code2_map;
	        	String last_prop_id = "", cur_prop_id = "";
	        	for(int i = 0; i < object.size(); i++) {
	        		HashMap map = object.get(i);
	        		cur_prop_id = map.get("PROP_CODE").toString();
	        		if(cur_prop_id.compareTo(last_prop_id) == 0) {   //属于同一PROP_ID，则追加内容
	        			code_map = retList.get(last_prop_id);
	        			code_map.put(map.get("ID").toString(), map.get("NAME_C").toString());
	        			if(code.indexOf("," + cur_prop_id + ",") >= 0) {
	        				code2_map = ret2List.get(last_prop_id);
	        				code2_map.put(map.get("CODE").toString(), map.get("NAME_C").toString());
	        			}
	        		}
	        		else {
	        			code_map = new LinkedHashMap<String, String>();
	        			code_map.put("", "");
	        			code_map.put(map.get("ID").toString(), map.get("NAME_C").toString());
	        			retList.put(cur_prop_id, code_map);
	        			if(code.indexOf("," + cur_prop_id + ",") >= 0) {
	        				code2_map = new LinkedHashMap<String, String>();
	        				code2_map.put("", "");
	        				code2_map.put(map.get("CODE").toString(), map.get("NAME_C").toString());
	        				ret2List.put(cur_prop_id, code2_map);
	        			}
	        		}
	        		last_prop_id = cur_prop_id;
	        	}
	        	LoginContent.getInstance().setBizCodes(retList);
	        	LoginContent.getInstance().setStatCodes(ret2List);
	        }
	        object = null;
			LoginContent.getInstance().closeSession();
			session = null;
		//}
        return retList;
	}
	
	/**
	 * 获取指定业务类型的数据字典内容（用作缓存，可在初始化下拉框数据时直接调用）
	 * @author yuanlei
	 */
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, LinkedHashMap<String, String>> getStatCodes() {
		
		LinkedHashMap<String, LinkedHashMap<String, String>> ret2List = LoginContent.getInstance().getStatCodes();
		LinkedHashMap<String, LinkedHashMap<String, String>> retList = null;
		if(ret2List == null) {
			//String client_id = SUtil.iif(getClientProp().get("client.biz.type"),"ALL");
			
			Session session = LoginContent.getInstance().getSession();
		    StringBuffer sf = new StringBuffer();
		    sf.append("select t_codes.prop_code,t_codes.id,t_codes.code,t_codes.name_c from BAS_CODES t_codes, BAS_CODEPROP t_prop");
		    sf.append(" where t_codes.prop_code = t_prop.prop_code ");
		    //sf.append(" and t_codes.prop_code not in ('TRANS_ODR_STAT', 'SHPM_STAT', 'TRANS_LOAD_STAT', 'ASSIGN_STAT', 'PLAN_STAT', 'LOAD_STAT', 'UNLOAD_STAT', 'LIST_TYP','ACCOUNT_STAT','AUDIT_STAT')");
		    /*if(client_id.compareTo("ALL") != 0) {
		    	sf.append(" and t_prop.biz_type = '");
		    	sf.append(client_id);
		    	sf.append("'");
		    }*/
		    sf.append(" order by t_codes.prop_code,t_codes.default_flag desc,t_codes.show_seq");
		    //Log4j.info(StaticRef.SQL_LOG, sf.toString());
		    Query query = session.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		    List<HashMap> object = query.list();
	        if(object != null && object.size() > 0) {
	        	retList = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	        	ret2List = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	        	String code = ",TRANS_ODR_STAT,SFODR_STAT,SHPM_STAT,TRANS_LOAD_STAT,ASSIGN_STAT,PLAN_STAT,LOAD_STAT,UNLOAD_STAT,LIST_TYP,ACCOUNT_STAT,AUDIT_STAT,";
	        	LinkedHashMap<String, String> code_map;
	        	LinkedHashMap<String, String> code2_map;
	        	String last_prop_id = "", cur_prop_id = "";
	        	for(int i = 0; i < object.size(); i++) {
	        		HashMap map = object.get(i);
	        		cur_prop_id = map.get("PROP_CODE").toString();
	        		if(cur_prop_id.compareTo(last_prop_id) == 0) {   //属于同一PROP_ID，则追加内容
	        			code_map = retList.get(last_prop_id);
	        			code_map.put(map.get("ID").toString(), map.get("NAME_C").toString());
	        			if(code.indexOf("," + cur_prop_id + ",") >= 0) {
	        				code2_map = ret2List.get(last_prop_id);
	        				code2_map.put(map.get("CODE").toString(), map.get("NAME_C").toString());
	        			}
	        		}
	        		else {
	        			code_map = new LinkedHashMap<String, String>();
	        			code_map.put("", "");
	        			code_map.put(map.get("ID").toString(), map.get("NAME_C").toString());
	        			retList.put(cur_prop_id, code_map);
	        			if(code.indexOf("," + cur_prop_id + ",") >= 0) {
	        				code2_map = new LinkedHashMap<String, String>();
	        				code2_map.put("", "");
	        				code2_map.put(map.get("CODE").toString(), map.get("NAME_C").toString());
	        				ret2List.put(cur_prop_id, code2_map);
	        			}
	        		}
	        		last_prop_id = cur_prop_id;
	        	}
	        	LoginContent.getInstance().setBizCodes(retList);
	        	LoginContent.getInstance().setStatCodes(ret2List);
	        }
	        object = null;
			LoginContent.getInstance().closeSession();
			session = null;
		}
        return ret2List;
	}
	
	/**
	 * 获取用户的配置文件信息
	 * @author yuanlei
	 */
	public LinkedHashMap<String, String> getClientProp() {
		try {
			LinkedHashMap<String, String> map;
			if(LoginContent.getInstance().getClientProp() == null) {
				map = new LinkedHashMap<String, String>();
				String prop_file = getServletContext().getRealPath("/") + "WEB-INF"+File.separator+"client_id.properties";
				File file = new File(prop_file);
				FileInputStream ins = new FileInputStream(file);
				Properties p = new Properties();
				InputStreamReader input = new InputStreamReader(ins, "UTF-8");
				p.load(input);

				Object[] iter = p.keySet().toArray();
				String key = "", value = "";
				for(int i = 0; i < iter.length; i++) {
					key = (String)iter[i];
					value = p.get(key).toString();
					map.put(key, value);
				}
				input.close();
				LoginContent.getInstance().setClientProp(map);
			}
			else {
				map = LoginContent.getInstance().getClientProp();
			}
			return map;
		}
		catch(Exception e) {
			Log4j.error(StaticRef.ERROR_LOG, e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> getUserOrg(String user_id) {
		Session session = LoginContent.getInstance().getSession();
	    StringBuffer sf = new StringBuffer();
	    sf.append("select ORG_ID,DEFAULT_FLAG FROM V_USER_ORG");
	    sf.append(" WHERE USER_ID='");
	    sf.append(user_id);
	    sf.append("'");
//	    Log4j.info(StaticRef.SQL_LOG, user + sf.toString());
	    Query query = session.createSQLQuery(sf.toString())
		    .addScalar("ORG_ID",Hibernate.STRING).addScalar("DEFAULT_FLAG",Hibernate.STRING)
		    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	    List<HashMap<String,String>> object = query.list();
	    
	    HashMap<String, String> map = new HashMap<String, String>();
	    for(int i=0;i<object.size();i++){
	    	map.put(object.get(i).get("ORG_ID"), object.get(i).get("DEFAULT_FLAG"));
	    }
		LoginContent.getInstance().closeSession();
		session = null;
		return map;
	}
	
	


	@SuppressWarnings("deprecation")
	@Override
	public String saveUser(ArrayList<String> list, String proName,String passwd,String salt) {
		String result = StaticRef.SUCCESS_CODE;
		String sql = "{call " + proName + "}";
		Session session = LoginContent.getInstance().getSession();
		Connection conn = null;
		CallableStatement cs = null;
		try{
			//Transaction tx = session.beginTransaction();
			conn = session.connection();
			cs = conn.prepareCall(sql);
			
			int i=0;
			for(i=0;i<list.size();i++){
				if(i==3){
					if(passwd.length() == 32){
						cs.setString(i+1,passwd);
					}else{
						salt = SUtil.randomNum(8);
						cs.setString(i+1,SUtil.getMd5(list.get(i),salt));
					}
				}else if(i==4){
					cs.setString(i+1,salt);
				}else{
					cs.setString(i+1, list.get(i));
				}
			}
			cs.registerOutParameter(i+1, Types.VARCHAR);
			cs.executeUpdate();
			result = cs.getString(i+1);
			//tx.commit();
		}catch (Exception ee) {
			try{
				LoginContent.getInstance().closeSession();
				session = null;
			}catch (Exception e) {
				e.printStackTrace();
			}
			ee.printStackTrace();
		}
		finally {
			try {
				if(cs != null) {
					cs.close();
				}
				if(conn != null) {
					conn.close();
				}
				if(session != null) {
					LoginContent.getInstance().closeSession();
					session = null;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> getRolePrivilege(String roleId,String str) {
		Session session = LoginContent.getInstance().getSession();
	    StringBuffer sf = new StringBuffer();
	    sf.append("select FUNCTION_ID FROM sys_role_func");
	    sf.append(" WHERE ROLE_ID='");
	    sf.append(roleId);
	    sf.append("'");
	    sf.append(" and SUBSYSTEM_TYPE='");
	    sf.append(str);
	    sf.append("'");
	    Query query = session.createSQLQuery(sf.toString())
		    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	    List<HashMap<String,String>> object = query.list();
	    
	    HashMap<String, String> map = new HashMap<String, String>();
	    for(int i=0;i<object.size();i++){
	    	map.put(object.get(i).get("FUNCTION_ID"), i+"");
	    }
		LoginContent.getInstance().closeSession();
		session = null;
		return map;
	}

	/**
	 * 插入用户日志信息
	 * @author yuanlei
	 * @param action_descr  操作内容
	 * @param action_result 操作结果
	 * @param link          链接地址（暂未使用）
	 */
	@Override
	public void insertLog(String action_descr, String action_result, String exce_sql, String link, String user_name, String org_name) {
		//SUtil.insertLog(action_descr, action_result, exce_sql, link, user_name, org_name);
	}
	
	/**
	 *  获取请求IP地址
	 *  @author fanglm
	 */
	@Override
	public String getClientIP(){
		HttpServletRequest  request = this.getThreadLocalRequest();
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client_IP");
		}
		return ip;
	}
	
	/**
	 * 
	 * @author yuanlei
	 */
	public ArrayList<String> getPageInfo() {
		return LoginContent.getInstance().getPageInfo();
	}

	/**
	 * 调用通用存储过程执行删除
	 * @author fanglm
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String deletePro(String id, String tableName,String userid) {
		String result = StaticRef.SUCCESS_CODE;
		String proName = "DEL_PRO(?,?,?,?)";
		String sql = "{call " + proName + "}";
		CallableStatement cs = null;
		Connection conn = null;
		Session session = LoginContent.getInstance().getSession();
		try{
			//Transaction tx = session.beginTransaction();
			conn = session.connection();
			cs = conn.prepareCall(sql);
			
			cs.setString(1, id);
			cs.setString(2, tableName);
			cs.setString(3, userid);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.executeUpdate();
			result = cs.getString(4);
			//tx.commit();
		}catch (Exception ee) {
			try{
				if(session != null) {
					LoginContent.getInstance().closeSession();
					session = null;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			ee.printStackTrace();
		}
		finally {
			try {
				if(cs != null) {
					cs.close();
				}
				if(conn != null) {
					conn.close();
				}
				if(session != null) {
					LoginContent.getInstance().closeSession();
					session = null;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ArrayList<String> QTY_CONVER(ArrayList<String> valList) {
		String vol = "0";
		String gwt = "0";
		String qnty = "0";
		String proName = "QTY_CONVER(?,?,?,?,?,?,?)";
		String sql = "{call " + proName + "}";
		Session session = LoginContent.getInstance().getSession();
		Connection conn = null;
		CallableStatement cs = null;
		try{
			//Transaction tx = session.beginTransaction();
			conn = session.connection();
			cs = conn.prepareCall(sql);
			cs.setString(1, valList.get(0));
			cs.setString(2, valList.get(1));
			cs.setString(3, valList.get(2));
			cs.setString(4, valList.get(3));
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.executeUpdate();
			vol = cs.getString(5);
			gwt = cs.getString(6);
			qnty = cs.getString(7);
			//tx.commit();
		}catch (Exception ee) {
			try{
				LoginContent.getInstance().closeSession();
				session = null;
			}catch (Exception e) {
				e.printStackTrace();
			}
			ee.printStackTrace();
		}
		finally {
			try {
				if(cs != null) {
					cs.close();
				}
				if(conn != null) {
					conn.close();
				}
				if(session != null) {
					LoginContent.getInstance().closeSession();
					session = null;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(vol);
		list.add(gwt);
		list.add(qnty);
		return list;
	}

	
	/**
	 * 登录取出系统参数
	 * @author fanglm
	 */
	@SuppressWarnings("unchecked")
	@Override
	public LinkedHashMap<String, SysParam> getSysParam() {
		//String bizType = LoginContent.getInstance().getClientProp().get("client.biz.type");
		String bizType = "ALL";
		Session session = LoginContent.getInstance().getSession();
	    StringBuffer sf = new StringBuffer();
	    sf.append("select config_code,value_int,value_string,sys_type FROM sys_param");
	    sf.append(" WHERE 1=1 ");
	    if(!"ALL".equals(bizType)){
	    	sf.append("sys_type='");
	    	sf.append(bizType);
		    sf.append("'");
	    }
	    Query query = session.createSQLQuery(sf.toString())
		    .addScalar("CONFIG_CODE",Hibernate.STRING).addScalar("VALUE_INT",Hibernate.STRING)
		    .addScalar("VALUE_STRING",Hibernate.STRING).addScalar("SYS_TYPE",Hibernate.STRING)
		    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	    List<HashMap<String,String>> object = query.list();
	    
	    LinkedHashMap<String, SysParam> retList = new LinkedHashMap<String, SysParam>();
	    SysParam sysParam;
	    for(int i=0;i<object.size();i++){
	    	sysParam = new SysParam();
	    	//sysParam.setCONFIG_CODE(object.get(i).get("CONFIG_CODE"));   //yuanlei
	    	sysParam.setVALUE_INT(object.get(i).get("VALUE_INT"));
	    	sysParam.setVALUE_STRING(object.get(i).get("VALUE_STRING"));
	    	//sysParam.setSYS_TYPE(object.get(i).get("SYS_TYPE"));        //yuanlei
	    	retList.put(object.get(i).get("CONFIG_CODE"), sysParam);
	    	if(object.get(i).get("CONFIG_CODE").equals("DEF_RECORD_COUNT")) {
	    		LoginContent.getInstance().setDefPageSize(Integer.parseInt(sysParam.getVALUE_INT()));
	    	}
	    }
		LoginContent.getInstance().closeSession();
		session = null;
		return retList;
	}
	
	/**
	 * clint_id配置文件中配置了默认客户时，取出默认客户数据
	 * @author fanglm
	 */

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> getDftCustomer() {
		SQLUtil util = new SQLUtil(true);
//		String def_cust = LoginContent.getInstance().getClientProp().get("defalut_customer");
		String keys = util.getColName("VC_CUSTOMER");
	    StringBuffer sf = new StringBuffer();
	    sf.append("select ");
	    sf.append(keys);
	    sf.append(" from VC_CUSTOMER WHERE customer_code=(");
	    sf.append("select VALUE_STRING from sys_param where config_code='DEFAULT_CUSTOMER' and enable_flag='Y'");
	    sf.append(")");
	    HashMap<String, Type> map = new HashMap<String, Type>();
//		map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
//		map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
//		map.put("POD_FLAG", Hibernate.YES_NO);
//		map.put("ADDR_EDIT_FLAG", Hibernate.YES_NO);
//		map.put("UNIQ_CONO_FLAG", Hibernate.YES_NO);
//		map.put("SKU_EDIT_FLAG", Hibernate.YES_NO);
		Query query = util.getQuery(sf.toString(), keys, map);
	    
		List<HashMap<String, String>> object = query.list();
		if(object.size() > 0){
			Gson gson = new Gson();
			String json = gson.toJson(object.get(0));
			HashMap<String, String> customer = SUtil.jsonToMap(json);
			return customer;
		}else{
			return new HashMap<String, String>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, String> getSHMPNOSum(LinkedHashMap<String, String> map) {
		SQLUtil sqlUtil = new SQLUtil(true);
		StringBuffer sf = new StringBuffer();
		sf.append("select SUM(TOT_QNTY) AS TOT_QNTY, SUM(TOT_GROSS_W) AS TOT_GROSS_W, SUM(TOT_VOL) AS TOT_VOL");
		sf.append(" from TRANS_SHIPMENT_HEADER ");
		sf.append(" where 1=1 ");
		//sf.append(" where 1=1 and LOAD_NO IS NULL ");
		sf.append(sqlUtil.addLikeSQL("SHPM_NO", map.get("SHPM_NO")));        //作业单号
		sf.append(sqlUtil.addEqualSQL("STATUS", map.get("STATUS")));         //状态
		sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", map.get("CUSTOMER_ID")));  //客户
		sf.append(sqlUtil.addLikeSQL("CUSTOM_ODR_NO", map.get("CUSTOM_ODR_NO")));  //客户单号
		sf.append(sqlUtil.addLikeSQL("LOAD_NAME", map.get("LOAD_NAME")));  //发货方
		sf.append(sqlUtil.addLikeSQL("UNLOAD_NAME", map.get("UNLOAD_NAME")));  //收货方
		sf.append(sqlUtil.addTimeSQL("ODR_TIME", map.get("ODR_TIME"), ">="));  //订单时间从
		sf.append(sqlUtil.addTimeSQL("ODR_TIME", map.get("ODR_TIME_TO"), "<=")); //订单时间到
		sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", map.get("TO_UNLOAD_TIME"), ">="));  //预计到货时间从
		sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", map.get("TO_UNLOAD_TIME_TO"), "<=")); //预计到货时间到
		
		
	    HashMap<String, Type> type = new HashMap<String, Type>();
	    type.put("TOT_QNTY", Hibernate.FLOAT);
	    type.put("TOT_GROSS_W", Hibernate.FLOAT);
	    type.put("TOT_VOL", Hibernate.FLOAT);
	    String keys = "TOT_QNTY,TOT_GROSS_W,TOT_VOL";
		Query query = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);;
		List<HashMap<String, String>> object = query.list();
		Gson gson = new Gson();
		String json = gson.toJson(object.get(0));
		LinkedHashMap<String, String> l_map = SUtil.jsonToMap(json);
		return l_map;
	}
	
	/**
	 * @author yuanlei
	 * 获取指定的记录
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, String>> getRecord(String flds, String table, String where, HashMap<String, String> map) {
		StringBuffer sf = new StringBuffer();
		sf.append("select ");
		sf.append(flds);
		sf.append(" from ");
		sf.append(table);
		sf.append(" ");
		sf.append(where);
		
		Query query = null;
		if(map != null) {
			HashMap<String, Type> list_map = new HashMap<String, Type>();
			Object[] iter = map.keySet().toArray();
			String key = "", value = "";
			for(int i = 0; i < iter.length; i++) {
				key = (String)iter[i];
				value = map.get(key);
				if(value.equals("BOOLEAN")) {
					list_map.put(key, Hibernate.YES_NO);
				}
			}
			query = new SQLUtil(true).getQuery(sf.toString(), flds, list_map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		}
		else {
			HashMap<String, Type> list_map = new HashMap<String, Type>();
			query = new SQLUtil(true).getQuery(sf.toString(), flds, list_map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			//query = session.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		}
		ArrayList<HashMap<String, String>> object = (ArrayList<HashMap<String, String>>) query.list();
		LoginContent.getInstance().closeSession();
		return object;
	}

	/**
	 * @author yuanle
	 * 获取指定的记录
	 */
	public HashMap<String, String> getSingleRecord(String flds, String table, String where, HashMap<String, String> map) {
		List<HashMap<String, String>> records = getRecord(flds, table, where, map);
		if(records.size() > 0) {
			return records.get(0);
		}
		return null;
	}
	
	/**
	 * 获取指定目录下，图片名称集合
	 */
	@Override
	public ArrayList<String> getImageNames(String path) {
		
		//远程服务器路径
		String http = getServletContext().getRealPath("/");
		File file = new File(http + path);
		File[] fileList = file.listFiles();
		if(file.listFiles()== null ){
			return null;
		} else {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < fileList.length; i++) {
			if(".jpg".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))
					||".JPG".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))
					||".JPEG".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))
					||".jpeg".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))
					||".GIF".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))
					||".gif".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))
					||".PNG".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))
					||".png".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))
					||".pdf".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))
					||".PDF".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))
					||".rar".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))
					||".RAR".equals(fileList[i].getName().substring(fileList[i].getName().indexOf(".")))){
				int j =0;
				list.add(j, fileList[i].getName());
				j++;
			}
		}
		return list;
	}
	}

	
    
	/**
	 * 图片 删除
	 * @author lijun
	 */
	
	@Override
	public boolean isDelete(String path) {
		try {
			boolean success = false;
			String http = getServletContext().getRealPath("/");
			File file = new File(http + path);
		    if(file.exists() && file.isFile()){
		    	file.delete();
		        //System.out.println("删除单个文件"+file+"成功！"); 
		    	success = true;
		    } else {
		    	success = false;
		    }
		    return success;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
		
	}
	
	/**
	 * 获取用户的所有菜单权限
	 * @author yuanlei
	 */
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, String> getUserPrivilege() {
		
		LinkedHashMap<String, String> retList = new LinkedHashMap<String, String>();
		Session session = LoginContent.getInstance().getSession();
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession httpSession =request.getSession();
		if(httpSession.getAttribute("USER_ID") == null) {
			return null;
		}
		SYS_USER user = (SYS_USER)httpSession.getAttribute("USER_ID");
		String role_id = user.getROLE_ID();
		//if(role_id.compareTo(StaticRef.SUPER_ROLE) == 0) 
		//	return null;
		String user_group = user.getUSERGRP_ID();
	    StringBuffer sf = new StringBuffer();
	    sf.append("select distinct t1.FUNCTION_ID from SYS_ROLE_FUNC t1,SYS_ROLE_FUNC t2 where t1.FUNCTION_ID = t2.FUNCTION_ID");
	    if(ObjUtil.isNotNull(role_id)) {
        	if(role_id.compareTo(StaticRef.SUPER_ROLE) != 0) {
        		sf.append(" and t2.ROLE_ID = '");
        		sf.append(role_id);
        		sf.append("'");
        	}
        }
    	else {
    		sf.append(" and t2.ROLE_ID in (");
    		sf.append(" select role_id from sys_usergrp_role where usergrp_ID = '");
    		sf.append(user_group);
    		sf.append("')");
    	}
        sf.append(" order by t1.function_id");
	    //Log4j.info(StaticRef.SQL_LOG, sf.toString());
	    Query query = session.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	    List<HashMap> object = query.list();
        if(object != null && object.size() > 0) {
        	String func_list = "";
        	String func_id = "", cur_func_id = "", last_func_id = "";
        	for(int i = 0; i < object.size(); i++) {
        		HashMap map = object.get(i);
        		cur_func_id = map.get("FUNCTION_ID").toString();
        		if(cur_func_id.length() > 6) {
    				if(cur_func_id.length() <= 8) {
        			    func_id = cur_func_id;
        			}
        		    else {
	        			if(cur_func_id.substring(7, 8).equals("_")) {
	        				func_id = cur_func_id.substring(0,7);        				
	        			}
	        			else {
	        				func_id = cur_func_id.substring(0,8);
	        			}
        		    }
        		}
        		else {
        			func_id = cur_func_id;
        		}
        		if(last_func_id.compareTo(func_id)== 0) {   //已存在FUNCTION_ID
        			func_list = retList.get(func_id);
        			func_list += "," + cur_func_id;
        			retList.put(func_id, func_list);
        		}
        		else {
        			func_list = "";
        			func_list = cur_func_id;
        			retList.put(func_id, func_list);
        			last_func_id = func_id;
        		}
        	}
            func_list = null;
        }
        object = null;
		LoginContent.getInstance().closeSession();
		session = null;
        return retList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public LinkedHashMap<String, LinkedHashMap<String, String>> getListCfg() {
		LinkedHashMap<String, LinkedHashMap<String, String>> retList = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		Session session = LoginContent.getInstance().getSession();
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession httpSession =request.getSession();
		if(httpSession.getAttribute("USER_ID") == null) {
			return null;
		}
		SYS_USER user = (SYS_USER)httpSession.getAttribute("USER_ID");
	    StringBuffer sf = new StringBuffer();
	    sf.append("select FIELD_ID,FIELD_WIDTH,FIELD_CNAME,view_name||func_model_code as CFG from v_list_func t where 1=1");
	    if(StaticRef.SUPER_ROLE.equals(user.getROLE_ID())) {
	    	sf.append(" and user_id = 'wpsadmin'");
	    }
	    else {
			sf.append(" and (user_id = '");
			sf.append(user.getUSER_ID());
			sf.append("' or (user_id = 'wpsadmin' and func_model not in (select distinct func_model from v_list_func where user_id = '");
			sf.append(user.getUSER_ID());
			sf.append("')))");
	    }
	    sf.append(" order by func_model_name,cfg,t.SHOW_SEQ");
	    //Log4j.info(StaticRef.SQL_LOG, sf.toString());
	    Query query = session.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, String>> object = query.list();
    	LinkedHashMap<String, String> cfg_map;
    	String last_cfg = "", cur_cfg = "";
    	for(int i = 0; i < object.size(); i++) {
    		HashMap map = object.get(i);
    		cur_cfg = map.get("CFG").toString();
    		if(cur_cfg.compareTo(last_cfg) == 0) {   //属于同一PROP_ID，则追加内容
    			cfg_map = retList.get(last_cfg);
    			cfg_map.put("FIELD", cfg_map.get("FIELD") + "," + map.get("FIELD_ID").toString());
    			cfg_map.put("WIDTH", cfg_map.get("WIDTH") + "," + map.get("FIELD_WIDTH").toString());
    			cfg_map.put("NAME", cfg_map.get("NAME") + "," + map.get("FIELD_CNAME").toString());
    		}
    		else {
    			cfg_map = new LinkedHashMap<String, String>();
    			cfg_map.put("FIELD", map.get("FIELD_ID").toString());
    			cfg_map.put("WIDTH", map.get("FIELD_WIDTH").toString());
    			cfg_map.put("NAME", map.get("FIELD_CNAME").toString());
    			retList.put(cur_cfg, cfg_map);
    		}
    		last_cfg = cur_cfg;
    	}
        object = null;
        LoginContent.getInstance().setListCfg(retList);
		LoginContent.getInstance().closeSession();
		session = null;
        return retList;
	}

	/**
	 * 执行汇总sql语句
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> execCountSql(String sql) {
		Session session = LoginContent.getInstance().getSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, String>> object = query.list();
		Gson gson = new Gson();
		String json = gson.toJson(object.get(0));
		LinkedHashMap<String, String> l_map = SUtil.jsonToMap(json);
		return l_map;
	}

	/** 
	 * 调用存储过程获取订单编号
	 * @author fanglm 2011-2-19 11:48
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String getIdSeq(String tableName) {
		String result = StaticRef.SUCCESS_CODE;
		String proName = "SP_GET_IDSEQ(?,?)";
		String sql = "{call " + proName + "}";
		CallableStatement cs = null;
		Connection conn = null;
		Session session = LoginContent.getInstance().getSession();
		try{
			//Transaction tx = session.beginTransaction();
			conn = session.connection();
			cs = conn.prepareCall(sql);
			
			cs.setString(1, tableName);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.executeUpdate();
			result = cs.getString(2);
			//tx.commit();
		}catch (Exception ee) {
			try{
				if(session != null) {
					LoginContent.getInstance().closeSession();
					session = null;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			ee.printStackTrace();
		}
		finally {
			try {
				if(cs != null) {
					cs.close();
				}
				if(conn != null) {
					conn.close();
				}
				if(session != null) {
					LoginContent.getInstance().closeSession();
					session = null;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 根据传入的SQL语句判断取出的数据条数
	 * @author fanglm
	 * @create tiem 2011-5-4 23:36
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getRecordCount(String sql) {
		String sum = "0";
		Session session = LoginContent.getInstance().getSession();
    	Query query = session.createSQLQuery(sql)
    	.addScalar("NUM",Hibernate.STRING)
    	.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	List<HashMap<String, String>> object = query.list();
		HashMap<String, String> map = (HashMap<String, String>)object.get(0);
		sum = map.get("NUM");
		return sum;
	}

	@Override
	public HashMap<String, String> getPost(String load_no,String param,String login_user) {
		
		InterfaceUtil intUtil = new InterfaceUtil();
		HashMap<String, String> map = null;
		try{
			map = intUtil.mobilePosition(param);
			
			ArrayList<String> list= new ArrayList<String>();
			String url = map.get("img_url");
			String longitude = "0";
			String latitude = "0";
			if(ObjUtil.isNotNull(url)){
				String[] st = url.split("longitude=");
				longitude = st[1].split("&")[0];
				latitude = st[1].split("latitude=")[1].split("&")[0];
			}
			
			//定位成功，写定位日志与跟踪日志
			if(map.get("result_id").equals("3001")){
				list.add(param);
				list.add(map.get("area_name"));
				list.add("手动");
				list.add(longitude);
				list.add(latitude);
				list.add(map.get("result_info"));
				list.add(login_user);
				gs.execProcedure(list, "SP_SHPM_POSITION(?,?,?,?,?,?,?,?)");
				
				String[] urls = url.split("area="); 
				url = urls[0] + "area=" + URLEncoder.encode(urls[1],"UTF-8");
				//String result = "../googleMap.html"+url.split("html")[1];
				String result = "http://api.map.baidu.com/staticimage?width=800&height=600&center=&markers=" + longitude + "," + latitude + "&zoom=12&markerStyles=m,A,0xff0000";
				map.put("img_url", result);
				//经纬度预览1
				//http://api.map.baidu.com/staticimage?width=800&height=600&center=&markers=118.32041829308,33.929161727646&zoom=12&markerStyles=m,A,0xff0000
			    //经纬度预览2
				//http://map.baidu.com/?latlng=39.16575665462,117.37475519458&title=%E6%A0%87%E9%A2%98&content=%E5%A4%A9%E6%B4%A5%E5%B8%82%E4%B8%9C%E4%B8%BD%E5%8C%BA%E7%A9%BA%E6%B8%AF%E7%89%A9%E6%B5%81%E5%8A%A0%E5%B7%A5%E5%8C%BA%E6%B8%AF%E5%9F%8E%E5%A4%A7%E9%81%93%E4%B8%8E%E7%BB%8F%E5%9B%9B%E8%B7%AF%E4%BA%A4%E5%8F%A3%E7%BB%8F%E5%9B%9B%E8%B7%AF217%E5%8F%B7&autoOpen=true&l=
				//获取经纬度1
				//http://api.map.baidu.com/geocoder/v2/?ak=1E061C3158c9bcb8b6f22b53402eb2a8&callback=renderOption&output=xml&address=江苏省宿迁市宿豫区金沙江路
				//&city=宿迁市
			}else{//定位失败，写定位日志，错误日志
				list.add(load_no);
				list.add("手动");
				list.add("N");
				list.add(map.get("result_id"));
				list.add(map.get("result_info"));
				list.add(login_user);
				gs.execProcedure(list, "SP_POSITION_LOG(?,?,?,?,?,?,?)");
			}
		}catch(Exception   e){ 
			e.printStackTrace();
		} 
		return map; 
	}
	
	/**
	 * 基础资料->服务范围->范围信息（明细）
	 * @author Administrator
	 * @param range_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getRangeDetail(String range_id) {
		Session session = LoginContent.getInstance().getSession();
	    StringBuffer sf = new StringBuffer();
	    sf.append("select RANGE_AREA_ID,AREA_LEVEL FROM BAS_RANGE_DETAIL");
	    sf.append(" WHERE RANGE_ID='");
	    sf.append(range_id);
	    sf.append("'");
	    Query query = session.createSQLQuery(sf.toString()).addScalar("RANGE_AREA_ID",Hibernate.STRING).addScalar("AREA_LEVEL",Hibernate.STRING)
		    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	    List<HashMap<String,String>> object = query.list();
	    
	    HashMap<String, String> map = new HashMap<String, String>();
	    for(int i=0;i<object.size();i++){
	    	map.put(object.get(i).get("RANGE_AREA_ID"), object.get(i).get("AREA_LEVEL"));
	    }
		LoginContent.getInstance().closeSession();
		session = null;
		return map;
	}
	
	/**
	 * 基础资料->组织够->行政区域
	 * @author Administrator
	 * @param range_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getOrgAreaInfo(String range_id,String condition) {
		Session session = LoginContent.getInstance().getSession();
	    StringBuffer sf = new StringBuffer();
	    sf.append("select AREA_ID,AREA_LEVEL FROM BAS_ORG_AREA");
	    sf.append(" WHERE ORG_ID='");
	    sf.append(range_id);
	    sf.append("'");
	    sf.append(condition);
	    Query query = session.createSQLQuery(sf.toString()).addScalar("AREA_ID",Hibernate.STRING).addScalar("AREA_LEVEL",Hibernate.STRING)
		    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	    List<HashMap<String,String>> object = query.list();
	    
	    HashMap<String, String> map = new HashMap<String, String>();
	    for(int i=0;i<object.size();i++){
	    	map.put(object.get(i).get("AREA_ID"), object.get(i).get("AREA_LEVEL"));
	    }
		LoginContent.getInstance().closeSession();
		session = null;
		return map;
	}
	
	//图片下载
	@Override
	public String expImageNames(String path,String image) {
//		HttpServletResponse response = this.getThreadLocalResponse();
//		response.setContentType("octets/stream");
//		response.setCharacterEncoding("UDF-8");
//		response.addHeader("Content-Disposition", "attachment;filename=result.xls");
		String http = getServletContext().getRealPath("/");
		File file = new File(http + path);
		File[] fileList = file.listFiles();
		try {
			
			for(int i = 0; i < fileList.length; i++){
				FileInputStream fis = new FileInputStream(fileList[i]);//从a.txt中读出
				String s = image+fileList[i].getName();
				FileOutputStream fos = new FileOutputStream(s);//写到b.txt中去
				byte[] bytes = new byte[1024];    
				int len = -1;    
				while((len=fis.read(bytes))!=-1)  
				{    
					fos.write(bytes, 0, len);    
				}    
				fis.close();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return http;
	}
}
