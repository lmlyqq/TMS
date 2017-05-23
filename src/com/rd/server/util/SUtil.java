package com.rd.server.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.mortbay.util.ajax.JSON;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.obj.system.SYS_USER;

public class SUtil {

	public static String getMd5(String password, String salt) {
		try {
			MessageDigest md5 = MD5.getInstance();
			md5.update(password.getBytes());
			md5.update(salt.getBytes());
			return MD5.byteArrayToHexString(md5.digest());
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 将JSON字符串实例化对象
	 * @param json
	 * @param objName 类对象路径
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object putRecordToModel(String json,String objName ){
		Object obj = null;
		try{
			Class newClass = Class.forName(objName);
			obj = newClass.newInstance();
			HashMap<String, String> map = (HashMap<String, String>)JSON.parse(json);
			Iterator it = map.entrySet().iterator();
			for(int i=0;it.hasNext();i++){
				Map.Entry entry=(Map.Entry)it.next();
				String method = (String)entry.getKey();
				if(method.indexOf("selection") < 0){
					Method m = newClass.getMethod("set"+method,String.class);
					m.invoke(obj, map.get(method));
				}
			}
			if(map.get("ID") == null || "".equals(map.get("ID"))){
				String id = getIDSequence();
				Method m2 = newClass.getMethod("setID",String.class);
				m2.invoke(obj,id);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}
	

	
	/**
	 * 将MAP对象转换成数据模型
	 * @param map
	 * @param objName 类对象路径
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object convertMapToModel(HashMap<String, Object> map,Class newClass){
		Object obj = null;
		try{
			obj = newClass.newInstance();
			Object[] iter = map.keySet().toArray();
			String method = "";
			Object value = null;
			for(int i = 0; i < iter.length; i++) {
				method = (String)iter[i];
				value = map.get(method);
				if(value != null) {
					Method m = newClass.getMethod("set"+method,String.class);
					m.invoke(obj, map.get(method));
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 获取插入的SQL语句
	 * @author yuanlei
	 * @param map 存储数据
	 * @return
	 */
	public static String getInsertSQL(Map<String, String> map, String flds) {
		StringBuffer sf = new StringBuffer();
		StringBuffer fields = new StringBuffer();
		StringBuffer values = new StringBuffer();
		String key = "";
		Object value = "";
		int pos = 0, date_pos = 0,sysdt = 0;
		
		String tableName = map.get("TABLE").toString();           //表名
		if(!ObjUtil.isNotNull(flds)) {
			flds = new SQLUtil(true).getColName(tableName);  //读取表的所有字段,用来过滤MAP是否存在非表字段的冗余值	
			if(flds != null){
				flds = flds.replaceAll("to_char\\(\\w*,'yyyy/mm/dd hh24:mi:ss'\\) as ", "");
			}
		}
		flds ="," + flds + ",";
		Object[] iter = map.keySet().toArray();
		sf.append("insert into ");
		sf.append(tableName);
		sf.append("(");
		map.remove("TABLE");
		map.remove("ADDTIME");
		for(int i = 0; i < iter.length; i++) {
			key = (String)iter[i];
			try {
				value = illegalFilter(map.get(key));
			}
			catch(Exception e) {
				value = map.get(key);
			}
			String koo = "," + key + ",";
		    if(value != null && flds.indexOf(koo) >= 0) {
				date_pos = value.toString().indexOf(StaticRef.DB_DATE_FUNC);
				sysdt = value.toString().indexOf(StaticRef.SYS_DATE); //如果前台需要将数据库时间插入指定值，不需要单引号
				if(pos > 0) {				
					fields.append(",");
					values.append(",");
					if(date_pos < 0 && sysdt < 0) {
						values.append("'");
					}
				}
				else {
					if(date_pos < 0 && sysdt < 0) {
						values.append("'");
					}
				}
			    fields.append(key);

			    if(value.toString().equals("true")) {
			    	
			    	value = "Y";
			    }
			    else if(value.toString().equals("false")) {
			    	value = "N";
			    }
			    values.append(value);
			    if(date_pos < 0 && sysdt < 0) {
			    	values.append("'");
			    }
		    	pos ++;
		    }
		}
		sf.append(fields);
		sf.append(",ADDTIME) values(");
		sf.append(values);
		sf.append(",sysdate)");
		return sf.toString();
	}
	
	/**
	 * 获取UPDATE的SQL
	 * @author yuanlei
	 * @param map 含有更新值的MAP
	 * @return
	 */
	public static String getUpdateSQL(Map<String, String> map) {
		StringBuffer sf = new StringBuffer();
		
		StringBuffer where = new StringBuffer();
		String key = "", value = "";
		int sysdt = 0,date_pos = 0;
		
		String tableName = map.get("TABLE").toString();           //表名
		map.remove("TABLE");
		map.remove("ADDTIME");
		map.remove("EDITTIME");
		String flds = new SQLUtil(true).getColName(tableName);  //读取表的所有字段,用来过滤MAP是否存在非表字段的冗余值
		if(flds != null){
			flds = flds.replaceAll("to_char\\(\\w*,'yyyy/mm/dd hh24:mi:ss'\\) as ", "");
		}
		flds = ","+flds;
		
		Object[] iter = map.keySet().toArray();
		sf.append("update ");
		sf.append(tableName);
		sf.append(" set EDITTIME = sysdate");
		
		for(int i = 0; i < iter.length; i++) {
			key = (String)iter[i];
			try {
				value = illegalFilter(map.get(key));
			}
			catch(Exception e) {
				value = map.get(key);
			}
//			System.out.println(i);
			date_pos = ObjUtil.ifObjNull(value,"").toString().indexOf(StaticRef.DB_DATE_FUNC);
			sysdt = ObjUtil.ifObjNull(value,"").toString().indexOf(StaticRef.SYS_DATE); //如果前台需要将数据库时间插入指定值，不需要单引号
			if(key.compareTo("TRANS_TRACK_APPEND") == 0){
				where.append(value);
				continue;
			}
			if(value != null && flds.indexOf(","+key) >= 0) {
				if(key.compareTo("ID") != 0) {
				    if(value.toString().equals("true")) {
				    	
				    	value = "Y";
				    }
				    else if(value.toString().equals("false")) {
				    	value = "N";
				    }
			    	sf.append(",");
			    	sf.append(key);
			    	sf.append(" = ");
			    	if(date_pos < 0 && sysdt < 0) {
			    		sf.append("'");
			    	}
			    	sf.append(value);
			    	if(date_pos < 0 && sysdt < 0) {
			    		sf.append("'");
			    	}
			    }
			    else {
		    		where.append(key);
		    		where.append(" = '");
		    		where.append(value);
		    		where.append("'");
			    }
			}
		}
		sf.append(" where ");
		sf.append(where.toString());
		return sf.toString();
	}
	
	public static String iif(String value, String defaultValue) {
		return ObjUtil.isNotNull(value) ? value.trim() : defaultValue;
	}
	
	/**
	 * 
	 * @author yuanlei
	 * @param map 已存储数据的MAP
	 * @param extra 额外附件条件的MAP(key代表表的字段,value代表取表中该字段的值)
	 */
	public static void setAdditionValue(HashMap<String, String> map, HashMap<String, String> extra) {
		
		String key = "", value = "";
		
		Object[] iter = extra.keySet().toArray();
		for(int i = 0; i < iter.length; i++) {
			key = (String)iter[i];
			value = extra.get(key);
			map.put(key, map.get(value).toString());
		}
	}
	
	/**
	 * @author yuanlei
	 * 获取Sequence
	 */
	@SuppressWarnings("unchecked")
	public static String getIDSequence() {
		String result = "";
		Session session = LoginContent.getInstance().getSession();
        StringBuffer buff = new StringBuffer();
        buff.append("select sys_guid() as SEQ from dual");
        Query query = session.createSQLQuery(buff.toString()).addScalar("SEQ", Hibernate.STRING);
        List<String> object = query.list();
        if(object != null && object.size() > 0) {
        	result = object.get(0).toString();
        }
        return result;
	}
	
	public static String boolToStr(String curValue) {
		String reResult = "N";
		if(curValue.compareTo("true") == 0) {
			reResult = "Y";
		}
		return reResult;
	}
	public static String randomNum(int code_len) {  
		    int count = 0;  
		    char str[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };  
		    StringBuffer pwd = new StringBuffer("");  
		    Random r = new Random();  
		    while (count < code_len) {  
		        int i = Math.abs(r.nextInt(10));  
		        if (i >= 0 && i < str.length) {  
		            pwd.append(str[i]);  
		            count++;  
		        }  
		    }  
		    return pwd.toString();  
	}
	
	/**
	 * 插入用户日志信息
	 * @author yuanlei
	 * @param action_descr 操作内容
	 * @param action_result操作结果
	 */
	public static void insertlog(String action_descr, String action_result, String user_name, String org_name) {
		insertLog(action_descr, action_result, "", "", user_name, org_name);
	}
	
	/**
	 * 插入用户日志信息
	 * @author yuanlei
	 * @param action_descr  操作内容
	 * @param action_result 操作结果
	 * @param link          链接地址（暂未使用）
	 */
	public static void insertLog(String action_descr, String action_result, String exce_sql, String link, String user_name, String org_name) {
		String sql = "";
		SYS_USER user = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("ID", getIDSequence());
			map.put("TABLE", "SYS_USER_LOG");
			map.put("USER_NAME", user_name);
			map.put("ORG_NAME", org_name);
			map.put("ACT_DESCR", action_descr);
			map.put("ACT_RESULT", action_result);
			map.put("LINK_CONTENT", link);
			sql = getInsertSQL(map, "");
			
			conn = LoginContent.getInstance().getConnection();
			if(conn == null)
				return;
	        stmt = conn.createStatement();
	        if(stmt != null) {
		        stmt.execute(sql);
	        }
		}
		catch(Exception e) {
			StringBuffer sf = null;
			if(user != null) {
				sf = new StringBuffer();
				sf.append("[");
				sf.append(user.getUSER_NAME());
				sf.append("]");
				sf.append(e.getMessage());
				sf.append(":\r\n");
				sf.append(sql);
				sf.append("\r\n");
		    	Log4j.info(StaticRef.SQL_LOG, sf.toString());
			}
			else {
				sf = new StringBuffer();
				sf.append("[未知用户]");
				sf.append(e.getMessage());
				sf.append(":\r\n");
				sf.append(sql);
				sf.append("\r\n");
		    	Log4j.info(StaticRef.SQL_LOG, sf.toString());
			}
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				if (conn != null) {
		    		conn.close();
		    	}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 批量插入日志记录，用于批量的插入或更新
	 * @author yuanlei
	 * @param descrList
	 * @param result
	 */
	public static void insertLog(ArrayList<String> descrList, String result, String user_name, String org_name) {
		if(descrList != null && descrList.size() > 0) {
			for(int i = 0; i < descrList.size(); i++) {
				if(ObjUtil.isNotNull(descrList.get(i).trim())) {
					insertlog(descrList.get(i),result, user_name, org_name);
				}
			}
		}
	}
	/**
	 * 获取记录数
	 * @author yuanlei
	 * @param sql 包含查询条件的SQL
	 * @return
	 */
	public static ArrayList<String> getRecordCount(String sql) {
		ArrayList<String> retList = new ArrayList<String>();
        StringBuffer buff = new StringBuffer();
        buff.append("select count(1) as CC from (");
        buff.append(sql);
        buff.append(")");       
        String count = getRecordByPro(buff.toString());
    	retList.add(count);
    	int pos = sql.toUpperCase().indexOf(" FROM");
    	if(pos > 0) {
    		retList.add(sql.substring(pos));
    	}
    	else {
    		retList.add("");
    	}

        return retList;
	}
	
	public static LinkedHashMap<String, String> jsonToMap(String json){
		Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
		LinkedHashMap<String, String> map = new GsonBuilder().create().fromJson(json,mapType);
		
		return map;
	}
	
	/**
     * U8接口数据，执行存储过程
     * @author fanglm
     * @createtime 2011-01-06 16:21
     */
    @SuppressWarnings("deprecation")
	public static void execProcedure(String proName){
//    	String result = StaticRef.SUCCESS_CODE;
		String sql = "{call " + proName + "}";
		Session session = LoginContent.getInstance().getSession();
		Connection conn = null;
		CallableStatement cs = null;
		try{
			conn = session.connection();
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.execute();
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
    }
    
    /**
     * 执行存储过程获取分页信息
     * @author fanglm
     * @createtime 2011-07-15 16:21
     */
    @SuppressWarnings("deprecation")
	public static String getRecordByPro(String sql){
    	String result = StaticRef.SUCCESS_CODE;
		String pro_name = "{call SP_GET_RECORD_COUNT(?,?)}";
		Session session = LoginContent.getInstance().getSession();
		Connection conn = null;
		CallableStatement cs = null;
		try{
			conn = session.connection();
			cs = conn.prepareCall(pro_name);
			cs.setString(1,sql);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			result = cs.getString(2);
			LoginContent.getInstance().closeSession();
			session = null;
		}catch (Exception ee) {
			try{
				result = "01" + ee.getMessage();
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
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
    }
    
    /**
     * 存储过程返回数据集合ResultSet解析成ArrayList
     * @author fanglm
     * @param keys 数据列
     * @param reslutSet 数据集
     * @return
     */
    @SuppressWarnings("deprecation")
	public static ArrayList<HashMap<String, String>> ResultToList(String keys,String sf,String CUR_PAGE){
    	String stringQueny = "{call SP_QUERY_PRO(?,?,?,?,?)}";
		Session sessio = LoginContent.getInstance().getSession();
		Connection conn = null;
		@SuppressWarnings("unused")
		CallableStatement cs = null;
    	ArrayList<HashMap<String, String>> reList = new ArrayList<HashMap<String,String>>();
    	HashMap<String, String> map = new HashMap<String, String>();
    	String[] key = keys.split(",");
    	int pageSize = LoginContent.getInstance().pageSize;
    	try{
    		conn = sessio.connection();
			CallableStatement cstmt = conn.prepareCall(stringQueny);
			cstmt.setInt(1, Integer.parseInt(( CUR_PAGE == null || CUR_PAGE.equals("0")) ? "1":CUR_PAGE));
			cstmt.setInt(2, pageSize);
			cstmt.setString(3, sf.toString());
			cstmt.registerOutParameter(4, Types.VARCHAR);
			cstmt.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute(); 
			ResultSet rs = (ResultSet) cstmt.getObject(5);
			String code = cstmt.getString(4);
		    while(rs.next()){
		    	map = new HashMap<String, String>();
		    	for(int i=0; i<key.length; i++){
		    		map.put(key[i], rs.getString(i+1));
		    	}
		    	reList.add(map);
		    } 
		    LoginContent.getInstance().setPageInfo(code, pageSize);
	      }catch (Exception e) {
			e.printStackTrace();
	      }
      return reList;
    }
    
    public static String excuteSQLList(ArrayList<String> sqlList) {
		Connection conn = null;
		Statement stmt = null;
		String resultStr = StaticRef.SUCCESS_CODE;
		try {
			conn = LoginContent.getInstance().getConnection();
			conn.setAutoCommit(false);
	        stmt = conn.createStatement();
	        if(stmt != null && sqlList != null && sqlList.size() > 0) {
	        	for(int i = 0; i < sqlList.size(); i++) {
	        		stmt.addBatch(sqlList.get(i).toString());
	        	}
	        	stmt.executeBatch();
		    	conn.commit();
	        }
		}
		catch(Exception e) {
			e.printStackTrace();
			resultStr = StaticRef.FAILURE_CODE + e.getMessage();
			return resultStr;
		}
		finally {
			try {
		        if(stmt != null) {
	        		stmt.close();
	        	}
			    if (conn != null) {
			    	conn.close();
			    }
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return resultStr;
	}
    
    /**
     * 非法字符串过滤
     * @author Administrator
     * @param str
     * @return
     */
    private static String illegalFilter(String value) {
    	if(value != null) {
    		if(value.indexOf("<") >= 0 && !value.equals("<") && !value.equals("<=")) {
    			value = value.replaceAll("<", "&lt;");
    		}
    		if(value.indexOf(">") >= 0 && !value.equals(">") && !value.equals(">=")){
    			value = value.replaceAll(">", "&gt;");
    		}
    	 	//value = value.replaceAll("\"", "").replaceAll("#", "");
    	 	value = value.replaceAll("javascript", "").replaceAll("expression", "").replaceAll("alert", "");
    	}
         return value;
    }
}