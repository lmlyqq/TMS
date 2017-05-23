package com.rd.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.axis.client.Call;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.rd.client.GreetingService;
import com.rd.client.common.obj.FUNCTION;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.obj.system.SYS_USER;
import com.rd.server.timer.SGSendTimerRun;
import com.rd.server.timer.SGTimerRun;
import com.rd.server.timer.U8SGManualAction;
import com.rd.server.timer.U8YHManualAction;
import com.rd.server.timer.YHSendTimerRun;
import com.rd.server.timer.YHTimerRun;
import com.rd.server.util.CreateBean;
import com.rd.server.util.CreateDS;
import com.rd.server.util.CreateHBM;
import com.rd.server.util.CustomExportExcel;
import com.rd.server.util.ExportExcel;
import com.rd.server.util.Log4j;
import com.rd.server.util.LoginContent;
import com.rd.server.util.PrintUtil;
import com.rd.server.util.SQLUtil;
import com.rd.server.util.SUtil;
import com.rd.sql.util.SQLFileUtil;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {
	
	public String greetServer(String input) throws IllegalArgumentException {
		return null;
	}
	
	/**
	 * @author yuanlei
	 * 校验登陆用户信息
	 */
	public SYS_USER getLoginUserInfo(String strUser, String strPwd) {
		
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
        return user;
	}
	
	/**
	 * 获取表或视图的记录数
	 * @param tableName 表名或视图名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getRecordCount(String tableName) {
		ArrayList<String> list = new ArrayList<String>();
		Session session = LoginContent.getInstance().getSession();
        StringBuffer buff = new StringBuffer();
        //buff.append("select rows from sysindexes where id = object_id('" + tableName + "') and indid in (0,1) ");
        buff.append("select count(1) from ");
        buff.append(tableName);
        Query query = session.createSQLQuery(buff.toString());
        List object1 = query.list();
        String sum_count = "0";
        if(object1 != null && object1.size() > 0) {
        	sum_count = object1.get(0).toString();
        }
        String sum_page = Integer.toString(Integer.parseInt(sum_count)/LoginContent.getInstance().pageSize + 1);
        list.add(sum_count);
        list.add(sum_page);
		//LoginContent.getInstance().closeSession();
		//session = null;
        return list;
	}
	
	/**
	 * @author yuanlei
	 * 获取下拉框的值
	 */
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, String> getComboValue(String table, String id, String name, String where, String orderby) {
		LinkedHashMap<String, String> retList = null;
		Session session = LoginContent.getInstance().getSession();
	    StringBuffer sf = new StringBuffer();
	    sf.append("select ");
	    sf.append(id);
	    sf.append(",");
	    sf.append(name);
	    sf.append(" from ");
	    sf.append(table);
	    if(ObjUtil.isNotNull(where)) {
	    	where = illegalFilter(where);
	    	if(where.toLowerCase().indexOf("where") < 0) {
	    		sf.append(" where ");
	    	}
	    	sf.append(where);
	    }
	    if(ObjUtil.isNotNull(orderby)) {
	    	if(orderby.indexOf("order by") < 0) {
	    		sf.append(" order by ");
	    	}
	    	sf.append(orderby);
	    }
	    /*else {
	    	sf.append(" order by addtime desc");
	    }*/
	    //Log4j.info(StaticRef.SQL_LOG, sf.toString());
	    Query query = session.createSQLQuery(sf.toString())
	    	.addScalar(id,Hibernate.STRING).addScalar(name,Hibernate.STRING)
	    	.setCacheable(true).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	    List<HashMap> object = query.list();
        if(object != null && object.size() > 0) {
        	retList = new LinkedHashMap<String, String>();
        	retList.put("", "");
        	for(int i = 0; i < object.size(); i++) {
        		HashMap map = object.get(i);
	        	retList.put(map.get(id).toString(), ObjUtil.ifObjNull(map.get(name),"").toString());
        	}
        }
		LoginContent.getInstance().closeSession();
		session = null;
        object = null;
        return retList;
	}
	
	/**
	 * @author yuanlei
	 * 获取下拉框的值（不使用缓存）
	 */
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, String> getComboValue2(String table, String id, String name, String where, String orderby) {
		LinkedHashMap<String, String> retList = null;
		Session session = LoginContent.getInstance().getSession();
	    StringBuffer sf = new StringBuffer();
	    sf.append("select ");
	    sf.append(id);
	    sf.append(",");
	    sf.append(name);
	    sf.append(" from ");
	    sf.append(table);
	    if(ObjUtil.isNotNull(where)) {
	    	where = illegalFilter(where);
	    	if(where.toLowerCase().indexOf("where") < 0) {
	    		sf.append(" where ");
	    	}
	    	sf.append(where);
	    }
	    if(ObjUtil.isNotNull(orderby)) {
	    	if(orderby.toLowerCase().indexOf("order by") < 0) {
	    		sf.append(" order by ");
	    	}
	    	sf.append(orderby);
	    }
	    /*else {
	    	sf.append(" order by addtime desc");
	    }*/
	    //Log4j.info(StaticRef.SQL_LOG, sf.toString());
	    Query query = session.createSQLQuery(sf.toString())
	    	.addScalar(id,Hibernate.STRING).addScalar(name,Hibernate.STRING)
	    	.setCacheable(false).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	    List<HashMap> object = query.list();
        if(object != null && object.size() > 0) {
        	retList = new LinkedHashMap<String, String>();
        	retList.put("", "");
        	for(int i = 0; i < object.size(); i++) {
        		HashMap map = object.get(i);
	        	retList.put(map.get(id).toString(), ObjUtil.ifObjNull(map.get(name),"").toString());
        	}
        }
		LoginContent.getInstance().closeSession();
		session = null;
        object = null;
        return retList;
	}
	
	
	/**
	 * @author yuanlei
	 * 获取下拉框的值
	 */
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, String> getComboValue(String sql, String id, String name) {
		LinkedHashMap<String, String> retList = null;
		Session session = LoginContent.getInstance().getSession();
	    Query query = session.createSQLQuery(illegalFilter2(sql))
	    	.addScalar(id,Hibernate.STRING).addScalar(name,Hibernate.STRING)
	    	.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	    List<HashMap> object = query.list();
        if(object != null && object.size() > 0) {
        	retList = new LinkedHashMap<String, String>();
        	retList.put("", "");
        	for(int i = 0; i < object.size(); i++) {
        		HashMap map = object.get(i);
	        	retList.put(map.get(id).toString(), ObjUtil.ifObjNull(map.get(name),"").toString());
        	}
        }
		LoginContent.getInstance().closeSession();
		session = null;
        object = null;
        return retList;
	}
	
	public String excuteSQL(String sql) {
		Connection conn = null;
		Statement stmt = null;
		String resultStr = StaticRef.SUCCESS_CODE;
		try {
			conn = LoginContent.getInstance().getConnection();
			if(conn == null)
				return "01";
			stmt = conn.createStatement();
	        if(stmt != null) {
		        stmt.execute(sql);
	        }
		}
		catch(Exception e) {
			e.printStackTrace();
			resultStr = StaticRef.FAILURE_CODE + e.getMessage();
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
	
	public String excuteSQLList(ArrayList<String> sqlList) {
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
	 * @author yuanlei
	 * 获取当前服务器的时间
	 */
	public String getServTime(String format) {
		SimpleDateFormat ft = new SimpleDateFormat(format);
		Date date = new Date();
		return ft.format(date);
	}
	
	/**
	 * @author yuanlei
	 * 获取Sequence
	 */
	@SuppressWarnings("unchecked")
	public String getIDSequence() {
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
	
	@SuppressWarnings("unchecked")
	public String doInsert(ArrayList<String> descrList, ArrayList<String> list, ArrayList<String> extraList, boolean isExtraFinaly) {
		String result = StaticRef.FAILURE_CODE;  
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
		if(session.getAttribute("USER_ID") == null) {
			result += "用户缓存为空,请重新登录!";
		}
		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		try {
			Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
			ArrayList<String> sqlList = new ArrayList<String>();
			HashMap map = null;
			String sql = "";
			if(extraList != null && !isExtraFinaly) {
				for(int m = 0; m < extraList.size(); m++) {
					sql = extraList.get(m).toString();
					//Log4j.info(StaticRef.SQL_LOG, sql);
					sqlList.add(sql);
				}
			}
			String table = "", flds = "";
			for(int i = 0; i < list.size(); i++) {
				String json = list.get(i).toString();
				map=new GsonBuilder().create().fromJson(json,mapType);
				map.put("ID", getIDSequence());
				map.put("ADDWHO", user.getUSER_ID());
				if(!table.equals(map.get("TABLE").toString())) {
					flds = new SQLUtil(true).getColName(map.get("TABLE").toString());  //读取表的所有字段,用来过滤MAP是否存在非表字段的冗余值
					table = map.get("TABLE").toString();
					
				}	
				sql = SUtil.getInsertSQL(map, flds);
				//Log4j.info(StaticRef.SQL_LOG, sql);
				sqlList.add(sql);
			}
			if(extraList != null && isExtraFinaly) {
				for(int m = 0; m < extraList.size(); m++) {
					sql = extraList.get(m).toString();
					//Log4j.info(StaticRef.SQL_LOG, sql);
					sqlList.add(sql);
				}
			}
			result = excuteSQLList(sqlList);
			//SUtil.insertLog(descrList, result.equals(StaticRef.SUCCESS_CODE)?StaticRef.ACT_SUCCESS:StaticRef.ACT_FAILURE, user.getUSER_NAME(), user.getDEFAULT_ORG_ID_NAME());
		}
		catch(Exception e) {
			Log4j.error(StaticRef.ERROR_LOG, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 执行插入语句
	 * @author yuanlei
	 */
	public String doInsert(ArrayList<String> descrList, ArrayList<String> list, ArrayList<String> extraList) {
		return doInsert(descrList, list, extraList, false);
	}
	
	/**
	 * 执行插入语句
	 * @author yuanlei
	 */
	@SuppressWarnings("unchecked")
	public String doInsert(String log, String json) {
		String result = StaticRef.FAILURE_CODE;
		String seq = "";
		try {
			HttpServletRequest request = this.getThreadLocalRequest();
			HttpSession session =request.getSession();
			if(session.getAttribute("USER_ID") == null) {
				result += "用户缓存为空,请重新登录!";
			}
			SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
			seq = getIDSequence();
			Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
			HashMap map=new GsonBuilder().create().fromJson(json,mapType);
			map.put("ID", seq);
			map.put("ADDWHO",user.getUSER_ID());
			map.remove("EDITWHO");
			map.remove("EDITTIME");
			String sql = SUtil.getInsertSQL(map, "");
			//Log4j.info(StaticRef.SQL_LOG, sql);
			result = excuteSQL(sql);
			if(result.subSequence(0, 2).equals(StaticRef.SUCCESS_CODE)) {
				result += seq;
			}
			//SUtil.insertlog(log, result.equals(StaticRef.SUCCESS_CODE)?StaticRef.ACT_SUCCESS:StaticRef.ACT_FAILURE, user.getUSER_NAME(), user.getDEFAULT_ORG_ID_NAME());
		}
		catch(Exception e) {
			Log4j.error(StaticRef.ERROR_LOG, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 执行删除语句
	 * @author yuanlei
	 */
	public String doDelete(ArrayList<String> log, ArrayList<String> list) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
		if(session.getAttribute("USER_ID") == null) {
			return StaticRef.FAILURE_CODE + "用户缓存为空,请重新登录!";
		}
		//SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		String result = excuteSQLList(list);
		//SUtil.insertLog(log, result.equals(StaticRef.SUCCESS_CODE)?StaticRef.ACT_SUCCESS:StaticRef.ACT_FAILURE, user.getUSER_NAME(), user.getDEFAULT_ORG_ID_NAME());
		return result;
	}
	
	/**
	 * 执行更新语句
	 * @author yuanlei
	 */
	@SuppressWarnings("unchecked")
	public String doUpdate(ArrayList<String> logList, ArrayList<String> list) {
		String result = StaticRef.FAILURE_CODE;          
		try {
			HttpServletRequest request = this.getThreadLocalRequest();
			HttpSession session =request.getSession();
			if(session.getAttribute("USER_ID") == null) {
				result += "用户缓存为空,请重新登录!";
			}
			SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
			Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
			ArrayList<String> sqlList = new ArrayList<String>();
			for(int i = 0; i < list.size(); i++) {
				String json = list.get(i).toString();
				HashMap map=new GsonBuilder().create().fromJson(json,mapType);
				map.put("EDITWHO", user.getUSER_ID());
				map.put("EDITTIME", "sysdate");
				String sql = SUtil.getUpdateSQL(map);
				//Log4j.info(StaticRef.SQL_LOG, sql);
				sqlList.add(sql);
			}
			result = excuteSQLList(sqlList);
			//SUtil.insertLog(logList, result.equals(StaticRef.SUCCESS_CODE)?StaticRef.ACT_SUCCESS:StaticRef.ACT_FAILURE, user.getUSER_NAME(), user.getDEFAULT_ORG_ID_NAME());
		}
		catch(Exception e) {
			result += e.getMessage();
			Log4j.error(StaticRef.ERROR_LOG, e.getMessage());
		}
		return result;
	}
	
	

	@SuppressWarnings("unchecked")
	@Override   //修改密码
	public String updatePWD(String id, String oldPwd, String newPWD) {
		String result = StaticRef.SUCCESS_CODE;
		StringBuffer sql = new StringBuffer();
		sql.append("select PASSWORD,SALT from sys_user ");
		sql.append(" where user_id='");
		sql.append(id);
		sql.append("'");
		Session session = LoginContent.getInstance().getSession();
		Query query = session.createSQLQuery(sql.toString())
		.addScalar("PASSWORD",Hibernate.STRING).addScalar("SALT",Hibernate.STRING)
		.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, String>> object = query.list();
		HashMap<String, String> map = (HashMap<String, String>)object.get(0);
		if(map != null){
			String oldStr = map.get("PASSWORD");
			String salt = map.get("SALT");
			if(!SUtil.getMd5(oldPwd, salt).equals(oldStr)){
				result = "输入的原始密码不正确";
			}else{
				String newStr = SUtil.getMd5(newPWD,map.get("SALT"));
				sql = new StringBuffer();
				sql.append("update sys_user set PASSWORD='");
				sql.append(newStr);
				sql.append("' where user_id='"+id+"'");
				result = excuteSQL(sql.toString());
			}
		}
		LoginContent.getInstance().closeSession();
		session = null;
		return result;
	}
/**
 * 修改保存&&新增保存
 * @author fanglm 
 * @created time 2010-8-19 17:59
 */
	@Override
	public String saveOrUpdate(String json,String objName) {
		Object obj = SUtil.putRecordToModel(json, objName);
		Session session = null;
		String messge = StaticRef.SUCCESS_CODE;
		try{
			session = LoginContent.getInstance().getSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(obj);
			tx.commit();
		}catch (Exception e) {
			e.printStackTrace();
			messge = StaticRef.FAILURE_CODE;
		}finally{
			LoginContent.getInstance().closeSession();
			session = null;
		}
		
		return messge;
	}
	/**
	 * 删除操作
	 * @author fanglm 
	 * @created time 2010-8-19 17:59
	 */
	@Override
	public String delete(String json,String objName){
		Object obj = SUtil.putRecordToModel(json, objName);
		Session session = null;
		String messge = StaticRef.SUCCESS_CODE;
		try{
			session = LoginContent.getInstance().getSession();
			Transaction tx = session.beginTransaction();
			session.delete(obj);
			tx.commit();
		}catch (Exception e) {
			e.printStackTrace();
			messge = StaticRef.FAILURE_CODE;
		}finally{
			LoginContent.getInstance().closeSession();
			session = null;
		}
		
		return messge;
	}
	
	/**
	 * 权限配置保存
	 * fanglm 2010-09-02 10:23
	 */
	@Override
	public String savePrivilege(ArrayList<String> list,String role_id,String login_user,String parent_id,String str) {
		String result = StaticRef.SUCCESS_CODE;
		StringBuffer sql = new StringBuffer();
		ArrayList<String> sqlList = new ArrayList<String>();
		sql.append("delete from  sys_role_func where upper(role_id)=upper('"+role_id+"') and SUBSYSTEM_TYPE='"+str+"'");
		sqlList.add(sql.toString());
		
		if(list.size()>0){
			sqlList.add(sql.toString());
			for(int i=0;i<list.size();i++){
				String func_id = list.get(i);
				sql = new StringBuffer();
				sql.append("insert into sys_role_func(FUNCTION_ID,SUBSYSTEM_TYPE,ROLE_ID,ADDWHO,ADDTIME)");
				sql.append(" values('");
				sql.append(func_id);
				sql.append("','");
				sql.append(str);
				sql.append("','");
				sql.append(role_id);
				sql.append("','");
				sql.append(login_user);
				sql.append("',sysdate");
				sql.append(")");
				sqlList.add(sql.toString());
			}
			
		}
		result = excuteSQLList(sqlList);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String exportAction(String headers,String filedNames,String sql) {
		
		  HttpServletResponse response = this.getThreadLocalResponse();
		
	      response.setContentType("octets/stream");
	      response.setCharacterEncoding("UDF-8");

	      response.addHeader("Content-Disposition", "attachment;filename=result.xls");
	      
	      HttpServletRequest request = this.getThreadLocalRequest();
		  HttpSession session =request.getSession();
	      if(session.getAttribute("USER_ID") == null) {
				return "";
	      }
	      String hql =  "select count(1)||'' CC from (" + sql +")";
	      Query query = LoginContent.getInstance().getSession().createSQLQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	      List<HashMap<String, String>> object = query.list();
		  HashMap<String, String> map = (HashMap<String, String>)object.get(0);
	      int max_row = Integer.parseInt(map.get("CC"));
	      
	      
	      
	      if(max_row >= 20000){
	    	  return StaticRef.FAILURE_CODE;
	      }
	      
	      SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
	      
	      ExportExcel ex = new ExportExcel();
	      
	      String path = getServletContext().getRealPath("/");
	      path = path + "user/" + user.getUSER_ID();  //不同用户建立不同文件夹，解决多线程数据对冲
	      
	      File file = new File(path +  "/result.xls");

	      try {
	    	  
	    	  File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			  if(!filePath.exists()){
				filePath.mkdirs();
			  }
	    	  if(!file.exists()){
	 	      	 file.createNewFile();
	 	      }
	    	  
	         OutputStream out = response.getOutputStream();
	         List list = null;
	         HSSFWorkbook book = null;
		      //避免大数据量导出时造成系统瘫痪，每次查询出3000条数据导出EXCEL，循环执行
		      for(int i=0;i<max_row/3000+1;i++){
			      query = LoginContent.getInstance().getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			      
			      query.setFirstResult(i*3000);
			      query.setMaxResults(3000);
			      list = query.list();
			      
			      if(i == 0){
			    	  book = ex.exportExcel("sheet1", headers,filedNames,list, out,"yyyy-MM-dd HH:mm");
			      }else{
			    	  book = ex.appendData(filedNames, list, i*3000, book);
			      }
		      }
		      ex.writeFile(book,file);
	         
			  out.close();
		
			  //System.out.println("excel导出成功！");
			  query = null;
			  book = null;
			  list= null;
			  filePath = null;
	      } catch (IOException e) {

	         e.printStackTrace();

	      }
	      
	      return "./user/" + user.getUSER_ID() + "/result.xls";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String RecAdjBillexportAction(String headers,String filedNames,String sql) {
		HttpServletResponse response = this.getThreadLocalResponse();			
	    response.setContentType("octets/stream");
	    response.setCharacterEncoding("UDF-8");
	    response.addHeader("Content-Disposition", "attachment;filename=result.xls");	      
	    HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
	    if(session.getAttribute("USER_ID") == null) {
			return "";
	    }
	    Query query = null;
	    SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
	    ExportExcel ex = new ExportExcel();
	    String path = getServletContext().getRealPath("/");
	    path = path + "user/" + user.getUSER_ID();  //不同用户建立不同文件夹，解决多线程数据对冲
	    File file = new File(path +  "/result.xls");
	    try {
	    	File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			if(!filePath.exists()){
				filePath.mkdirs();
			}
	    	if(!file.exists()){
	 	      file.createNewFile();
	 	    }
	    	OutputStream out = response.getOutputStream();
	        List list = null;
	        HSSFWorkbook book = null;
	        query = LoginContent.getInstance().getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list = query.list();
			book = ex.RecAdjBillExportExcel("sheet1", headers,filedNames,list, out,"yyyy-MM-dd HH:mm");
			ex.writeFile(book,file);
			out.close();
			query = null;
			book = null;
			list= null;
			filePath = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "./user/" + user.getUSER_ID() + "/result.xls";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String PayAdjBillexportAction(String headers,String filedNames,String sql) {
		HttpServletResponse response = this.getThreadLocalResponse();			
	    response.setContentType("octets/stream");
	    response.setCharacterEncoding("UDF-8");
	    response.addHeader("Content-Disposition", "attachment;filename=result.xls");	      
	    HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
	    if(session.getAttribute("USER_ID") == null) {
			return "";
	    }
	    Query query = null;
	    SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
	    ExportExcel ex = new ExportExcel();
	    String path = getServletContext().getRealPath("/");
	    path = path + "user/" + user.getUSER_ID();  //不同用户建立不同文件夹，解决多线程数据对冲
	    File file = new File(path +  "/result.xls");
	    try {
	    	File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			if(!filePath.exists()){
				filePath.mkdirs();
			}
	    	if(!file.exists()){
	 	      file.createNewFile();
	 	    }
	    	OutputStream out = response.getOutputStream();
	        List list = null;
	        HSSFWorkbook book = null;
	        query = LoginContent.getInstance().getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list = query.list();
			book = ex.PayAdjBillExportExcel("sheet1", headers,filedNames,list, out,"yyyy-MM-dd HH:mm");
			ex.writeFile(book,file);
			out.close();
			query = null;
			book = null;
			list= null;
			filePath = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "./user/" + user.getUSER_ID() + "/result.xls";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String PayReqBillexportAction(String headers,String filedNames,String sql) {
		HttpServletResponse response = this.getThreadLocalResponse();			
	    response.setContentType("octets/stream");
	    response.setCharacterEncoding("UDF-8");
	    response.addHeader("Content-Disposition", "attachment;filename=result.xls");	      
	    HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
	    if(session.getAttribute("USER_ID") == null) {
			return "";
	    }
	    Query query = null;
	    SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
	    ExportExcel ex = new ExportExcel();
	    String path = getServletContext().getRealPath("/");
	    path = path + "user/" + user.getUSER_ID();  //不同用户建立不同文件夹，解决多线程数据对冲
	    File file = new File(path +  "/result.xls");
	    try {
	    	File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			if(!filePath.exists()){
				filePath.mkdirs();
			}
	    	if(!file.exists()){
	 	      file.createNewFile();
	 	    }
	    	OutputStream out = response.getOutputStream();
	        List list = null;
	        HSSFWorkbook book = null;
	        query = LoginContent.getInstance().getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list = query.list();
			book = ex.PayReqBillExportExcel("sheet1", headers,filedNames,list, out,"yyyy-MM-dd HH:mm");
			ex.writeFile(book,file);
			out.close();
			query = null;
			book = null;
			list= null;
			filePath = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "./user/" + user.getUSER_ID() + "/result.xls";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String RecInitBillexportAction() {
		HttpServletResponse response = this.getThreadLocalResponse();			
	    response.setContentType("octets/stream");
	    response.setCharacterEncoding("UDF-8");
	    response.addHeader("Content-Disposition", "attachment;filename=result.xls");	      
	    HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
	    if(session.getAttribute("USER_ID") == null) {
			return "";
	    }
	    Query query = null;
	    SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
	    ExportExcel ex = new ExportExcel();
	    String path = getServletContext().getRealPath("/");
	    path = path + "user/" + user.getUSER_ID();  //不同用户建立不同文件夹，解决多线程数据对冲
	    File file = new File(path +  "/result.xls");
	    try {
	    	File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			if(!filePath.exists()){
				filePath.mkdirs();
			}
	    	if(!file.exists()){
	 	      file.createNewFile();
	 	    }
	    	OutputStream out = response.getOutputStream();
	    	List<HashMap<String, Object>>  list1 = null;
	        HSSFWorkbook book = null;
	        String sql1="select col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12 from LC_REC_HEADER";
	        SQLUtil util = new SQLUtil(true);
	        String keys1 = util.getColName("LC_REC_HEADER");
	        query = util.getQuery(sql1.toString(), keys1, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list1 = query.list();
	        
	        List<HashMap<String, Object>> list2 = null;
	        String keys2 = "COL2,COL3,COL4,COL5,COL6,COL7,COL8,COL9,COL10,COL11,COL12,COL14,COL16,COL17,COL18,COL19,COL20,COL21,COL22,COL23,COL24,COL25,COL26";
	        StringBuffer sql2=new StringBuffer();
	        sql2.append("select distinct ");
	        sql2.append(keys2);
	        sql2.append(" from LC_REC_DETAIL");
	        query = util.getQuery(sql2.toString(), keys2, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list2 = query.list();
	        
	        List<HashMap<String, Object>> list3 = null;
	        //String keys3 = util.getColName("LC_PAY_CUSTOMER_DETAIL");
	        StringBuffer sql3=new StringBuffer();
	        sql3.append("select ");
	        sql3.append("COL26,sum(COL13) as total_amount");
	        sql3.append(" from LC_REC_DETAIL group by COL26");
	        query = util.getQuery(sql3.toString(), "COL26,total_amount", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list3 = query.list();
	        if(list3==null||list3.size()<1){
	        	return "";
	        }
	       
	        HashMap<Object,Object> map=new HashMap<Object, Object>();
	        for(int i=0;i<list3.size();i++){
	        	HashMap<String,Object> map1=list3.get(i);
	        	map.put(map1.get("COL26"), map1.get("total_amount")); 	
	        }
	        
			book = ex.RecCheckBillExportExcel(list1,list2,map);
			ex.writeFile(book,file);
			out.close();
			query = null;
			book = null;
			list1= null;
			list2=null;
			filePath = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "./user/" + user.getUSER_ID() + "/result.xls";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String PayInitBillexportAction1() {
		HttpServletResponse response = this.getThreadLocalResponse();			
	    response.setContentType("octets/stream");
	    response.setCharacterEncoding("UDF-8");
	    response.addHeader("Content-Disposition", "attachment;filename=result.xls");	      
	    HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
	    if(session.getAttribute("USER_ID") == null) {
			return "";
	    }
	    Query query = null;
	    SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
	    ExportExcel ex = new ExportExcel();
	    String path = getServletContext().getRealPath("/");
	    path = path + "user/" + user.getUSER_ID();  //不同用户建立不同文件夹，解决多线程数据对冲
	    File file = new File(path +  "/result.xls");
	    try {
	    	File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			if(!filePath.exists()){
				filePath.mkdirs();
			}
	    	if(!file.exists()){
	 	      file.createNewFile();
	 	    }
	    	OutputStream out = response.getOutputStream();
	    	List<HashMap<String, Object>>  list1 = null;
	        HSSFWorkbook book = null;
	        SQLUtil util = new SQLUtil(true);
	        StringBuffer sql1=new StringBuffer();
	        String keys1 = util.getColName("LC_PAY_CUSTOMER_HEADER");
	        sql1.append("select ");
	        sql1.append(keys1);
	        sql1.append(" from lc_pay_customer_header");
	        query = util.getQuery(sql1.toString(), keys1, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list1 = query.list();
	        if(list1==null||list1.size()<1){
	        	return "";
	        }
	        
	        List<HashMap<String, Object>> list2 = null;
	        String keys2 = "COL2,COL3,COL4,COL5,COL6,COL7,COL8,COL9,COL10,COL11,COL12,COL13,COL14,COL17,COL18,COL19,COL20,COL21,COL22,COL23,COL24,COL25,COL26,COL27";
	        StringBuffer sql2=new StringBuffer();
	        sql2.append("select distinct ");
	        sql2.append(keys2);
	        sql2.append(" from lc_pay_customer_detail");
	        query = util.getQuery(sql2.toString(), keys2, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list2 = query.list();
	        if(list2==null||list2.size()<1){
	        	return "";
	        }
	        
	        List<HashMap<String, Object>> list3 = null;
	        //String keys3 = util.getColName("LC_PAY_CUSTOMER_DETAIL");
	        StringBuffer sql3=new StringBuffer();
	        sql3.append("select ");
	        sql3.append("COL5,sum(COL15) as total_amount");
	        sql3.append(" from lc_pay_customer_detail group by COL5");
	        query = util.getQuery(sql3.toString(), "COL5,total_amount", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list3 = query.list();
	        if(list3==null||list3.size()<1){
	        	return "";
	        }
	        
	        HashMap<Object,Object> map=new HashMap<Object, Object>();
	        for(int i=0;i<list3.size();i++){
	        	HashMap<String,Object> map1=list3.get(i);
	        	map.put(map1.get("COL5"), map1.get("total_amount")); 	
	        }
	        
	        List<HashMap<String, Object>> list4 = null;
	        String sql4="select sum(col15) as all_amount  from lc_pay_customer_detail";
	        query = LoginContent.getInstance().getSession().createSQLQuery(sql4).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list4 = query.list();
	        if(list4==null){
	        	return "";
	        }
	        
	        
	        book = ex.PayInitBillExportExcel1(list1,list2,map,list4);
			ex.writeFile(book,file);
			out.close();
			query = null;
			book = null;
			list1= null;
			list2=null;
			filePath = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "./user/" + user.getUSER_ID() + "/result.xls";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String PayInitBillexportAction2() {
		HttpServletResponse response = this.getThreadLocalResponse();			
	    response.setContentType("octets/stream");
	    response.setCharacterEncoding("UDF-8");
	    response.addHeader("Content-Disposition", "attachment;filename=result.xls");	      
	    HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
	    if(session.getAttribute("USER_ID") == null) {
			return "";
	    }
	    Query query = null;
	    SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
	    ExportExcel ex = new ExportExcel();
	    String path = getServletContext().getRealPath("/");
	    path = path + "user/" + user.getUSER_ID();  //不同用户建立不同文件夹，解决多线程数据对冲
	    File file = new File(path +  "/result.xls");
	    try {
	    	File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			if(!filePath.exists()){
				filePath.mkdirs();
			}
	    	if(!file.exists()){
	 	      file.createNewFile();
	 	    }
	    	OutputStream out = response.getOutputStream();
	    	List<HashMap<String, Object>>  list1 = null;
	        HSSFWorkbook book = null;
	        SQLUtil util = new SQLUtil(true);
	        String keys1 = util.getColName("LC_PAY_SUPLR_HEADER");
	        StringBuffer sql1=new StringBuffer();
	        sql1.append("select ");
	        sql1.append(keys1);
	        sql1.append(" from LC_PAY_SUPLR_HEADER");     
	        query = util.getQuery(sql1.toString(), keys1, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list1 = query.list();
	        
	        List<HashMap<String, Object>> list2 = null;
	        String keys2 = "COL2,COL3,COL4,COL5,COL6,COL7,COL8,COL9,COL10,COL11,COL12,COL13,COL14,COL17,COL18,COL19,COL20,COL21,COL22,COL23,COL24,COL25,COL26,COL27";
	        StringBuffer sql2=new StringBuffer();
	        sql2.append("select distinct ");
	        sql2.append(keys2);
	        sql2.append(" from lc_pay_suplr_detail");
	        query = util.getQuery(sql2.toString(), keys2, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list2 = query.list();
	        if(list2==null||list2.size()<1){
	        	return "";
	        }
	        
	        List<HashMap<String, Object>> list3 = null;
	        //String keys3 = util.getColName("LC_PAY_CUSTOMER_DETAIL");
	        StringBuffer sql3=new StringBuffer();
	        sql3.append("select ");
	        sql3.append("COL8,sum(COL15) as total_amount");
	        sql3.append(" from lc_pay_suplr_detail group by COL8");
	        query = util.getQuery(sql3.toString(), "COL8,total_amount", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list3 = query.list();
	        if(list3==null||list3.size()<1){
	        	return "";
	        }
	       
	        HashMap<Object,Object> map=new HashMap<Object, Object>();
	        for(int i=0;i<list3.size();i++){
	        	HashMap<String,Object> map1=list3.get(i);
	        	map.put(map1.get("COL8"), map1.get("total_amount")); 	
	        }
	        
			book = ex.PayInitBillExportExcel2(list1,list2,map);
			ex.writeFile(book,file);
			out.close();
			query = null;
			book = null;
			list1= null;
			list2=null;
			filePath = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "./user/" + user.getUSER_ID() + "/result.xls";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String InvoiceExportAction(String Id) {
		HttpServletResponse response = this.getThreadLocalResponse();			
	    response.setContentType("octets/stream");
	    response.setCharacterEncoding("UDF-8");
	    response.addHeader("Content-Disposition", "attachment;filename=result.xls");	      
	    HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
	    if(session.getAttribute("USER_ID") == null) {
			return "";
	    }
	    Query query = null;
	    SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
	    ExportExcel ex = new ExportExcel();
	    String path = getServletContext().getRealPath("/");
	    path = path + "user/" + user.getUSER_ID();  //不同用户建立不同文件夹，解决多线程数据对冲
	    File file = new File(path +  "/result.xls");
	    try {
	    	File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			if(!filePath.exists()){
				filePath.mkdirs();
			}
	    	if(!file.exists()){
	 	      file.createNewFile();
	 	    }
	    	OutputStream out = response.getOutputStream();
	        HSSFWorkbook book = null;	     
	        SQLUtil util = new SQLUtil(true);
	        List<HashMap<String, Object>> list = null;
	        String keys = util.getColName("V_PRINT_BILL");
	        StringBuffer sql=new StringBuffer();
	        sql.append("select ");
	        sql.append(keys);
	        sql.append(" from V_PRINT_BILL where ID='"+Id+"'");
	        query = util.getQuery(sql.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        list = query.list(); 
			book = ex.InvoiceExportExcel(list);
			ex.writeFile(book,file);
			out.close();
			query = null;
			book = null;
			list=null;
			filePath = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "./user/" + user.getUSER_ID() + "/result.xls";
	}
	
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public String exportByProAction(String headers,String filedNames,String sql,String pro_name) {
		
		  HttpServletResponse response = this.getThreadLocalResponse();		
	      response.setContentType("octets/stream");
	      response.setCharacterEncoding("UDF-8");
	      response.addHeader("Content-Disposition", "attachment;filename=result.xls");	      
	      HttpServletRequest request = this.getThreadLocalRequest();
		  HttpSession session =request.getSession();
	      if(session.getAttribute("USER_ID") == null) {
				return "";
	      }
	      String code = "0";
	      ResultSet rs = null;
	      String stringQueny = "{call " + pro_name +"(?,?,?,?,?)}";
			Session sessio = LoginContent.getInstance().getSession();
			Connection conn = null;
			@SuppressWarnings("unused")
			CallableStatement cs = null;
			try {
				conn = sessio.connection();
				CallableStatement cstmt = conn.prepareCall(stringQueny);
				cstmt.setInt(1, 1);
				cstmt.setInt(2, 0);
				cstmt.setString(3, sql);
				cstmt.registerOutParameter(4, Types.VARCHAR);
				cstmt.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
				cstmt.execute(); 
				code = cstmt.getString(4);
				rs = (ResultSet) cstmt.getObject(5);
				LoginContent.getInstance().setPageInfo(code, 100);

			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
	      SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
	      
	      if(Integer.parseInt(code) > 20000){
	    	  return StaticRef.FAILURE_CODE;
	      }else if(rs == null){
	    	  return StaticRef.FAILURE_CODE;
	      }
	      
	      ExportExcel ex = new ExportExcel();
	      
	      String path = getServletContext().getRealPath("/");
	      path = path + "user/" + user.getUSER_ID();  //不同用户建立不同文件夹，解决多线程数据对冲
	      
	      File file = new File(path +  "/result.xls");

	      try {
	    	  
	    	  File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			  if(!filePath.exists()){
				filePath.mkdirs();
			  }
	    	  if(!file.exists()){
	 	      	 file.createNewFile();
	 	      }
	    	  
	         OutputStream out = response.getOutputStream();

	         ex.exportExcel("sheet1",headers,filedNames,rs, out,"yyyy-MM-dd HH:mm",file);
	         
	         out.close();

	         //System.out.println("excel导出成功！");
	         ex = null;
	         filePath = null;
	      } catch (IOException e) {

	         e.printStackTrace();

	      }
	      
	      return "./user/" + user.getUSER_ID() + "/result.xls";
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countChild(String tableName, String colum, String value) {
		Integer sum =0;
		StringBuffer sf = new StringBuffer();
    	sf.append("select count(*) as NUM from ");
    	sf.append(tableName);
    	sf.append(" where ");
    	sf.append(colum);
    	sf.append("='");
    	sf.append(value);
    	sf.append("'");
    	Session session = LoginContent.getInstance().getSession();
    	Query query = session.createSQLQuery(sf.toString())
    	.addScalar("NUM",Hibernate.STRING)
    	.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	List<HashMap<String, String>> object = query.list();
		HashMap<String, String> map = (HashMap<String, String>)object.get(0);
		sum = Integer.valueOf(map.get("NUM"));
		return sum;
	}
	
	
	
	/**
	 * 指定条件过滤来获取表或视图的记录数,根据记录数来判断是否唯一性
	 * @param tableName 表名或视图名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getCheckResult(String json) {
		StringBuffer result = new StringBuffer();
		ArrayList<String> titleList = new ArrayList<String>();
		StringBuffer union_sql = new StringBuffer();
		Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
		HashMap map=new GsonBuilder().create().fromJson(json,mapType);
		
		Object[] iter = map.keySet().toArray();
		String key = "", value = "";
		for(int i = 0; i < iter.length; i++) {
			key = (String)iter[i];
			value = map.get(key).toString();
			union_sql.append(" union all ");
			union_sql.append(value);
			titleList.add(key);
		}
		if(union_sql.toString().length() >= 11) {
			Session session = LoginContent.getInstance().getSession();
			Query query = session.createSQLQuery(union_sql.toString().substring(11));
			List object1 = query.list();
	        if(object1 != null && object1.size() > 0) {
	        	int count = 0;
	        	for(int i= 0; i< object1.size(); i++) {
	        		count = Integer.parseInt(object1.get(i).toString());
	        		if(count > 0) {
	        			result.append("[");
	        			result.append(titleList.get(i).toString());
	        			result.append("]已存在!\r\n");
	        		}
	        	}
	        }
			LoginContent.getInstance().closeSession();
			session = null;
		}
        return result.toString();
	}
	
	/**
	 * 获取中文内容的助记码
	 * @author yuanlei
	 * @param 中文字符串
	 */
	@SuppressWarnings("unchecked")
	public String getHintCode(String content) {
		String hint_code = "";
		if(ObjUtil.isNotNull(content)) {
			Session session = LoginContent.getInstance().getSession();
	        StringBuffer buff = new StringBuffer();
	        buff.append("select F_ZJM('");
	        buff.append(content);
	        buff.append("') as HINT from dual");
	        Query query = session.createSQLQuery(buff.toString()).addScalar("HINT", Hibernate.STRING);
	        List<String> object = query.list();
	        if(object != null && object.size() > 0) {
	        	hint_code = object.get(0).toString();
	        }
			LoginContent.getInstance().closeSession();
			session = null;
		}
		return hint_code;
	}
	
	/**
	 * @author yuanlei
	 * 跟踪登录用户的角色获取对应的显示菜单
	 */
	@SuppressWarnings("unchecked")
	public List<FUNCTION> getFuncList() {
		Session session = LoginContent.getInstance().getSession();
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession httpSession =request.getSession();
		if(httpSession.getAttribute("USER_ID") == null) {
			return null;
		}
		SYS_USER user = (SYS_USER)httpSession.getAttribute("USER_ID");
		String role_id = user.getROLE_ID();
		String user_group = user.getUSERGRP_ID();
		StringBuffer sf = new StringBuffer();
        sf.append("select distinct t1.SHOW_SEQUENCE,t1.FUNCTION_ID,t1.FUNCTION_NAME,t1.PARENT_FUNCTION_ID,t1.FUNCTION_FORMNAME from SYS_FUNCTION t1");
        if(ObjUtil.isNotNull(role_id)) {
        	if(role_id.compareTo(StaticRef.SUPER_ROLE) != 0) {
        		sf.append(",SYS_ROLE_FUNC t2 where t1.FUNCTION_ID = t2.FUNCTION_ID and t1.ACTIVE_FLAG = 'Y' and t2.ROLE_ID = '");
        		sf.append(role_id);
        		sf.append("' and t1.subsystem_type = '");
        		sf.append(user.getLOGIN_SYSTEM());
        		sf.append("'");
        		
        	}
        	else {
        		sf.append(" where t1.ACTIVE_FLAG = 'Y' and t1.subsystem_type = '");
        		sf.append(user.getLOGIN_SYSTEM());
        		sf.append("'");
        	}
        }
    	else {
    		sf.append(",SYS_ROLE_FUNC t2 where t1.FUNCTION_ID = t2.FUNCTION_ID and t1.ACTIVE_FLAG = 'Y' and t2.ROLE_ID in (");
    		sf.append(" select role_id from sys_usergrp_role where usergrp_ID = '");
    		sf.append(user_group);
    		sf.append("') and t1.subsystem_type = '");
    		sf.append(user.getLOGIN_SYSTEM());
    		sf.append("'");
    	}
        sf.append(" order by t1.SHOW_SEQUENCE asc");
        Query query = session.createSQLQuery(sf.toString()).addEntity(FUNCTION.class);
        List<FUNCTION> object = query.list();
		LoginContent.getInstance().closeSession();
		session = null;
		return object;
	}
	
	/**
	 * 批量执行存储过程调用
	 * @author fanglm
	 * @param list 存储过程参数集合
	 * @param proName 存储过程名称  execPro(?,?,?,?)
	 * @return 操作成功/失败标识
	 */
		@Override
		public String execPro(HashMap<String, ArrayList<String>> map, String proName) {
			String result = StaticRef.SUCCESS_CODE;
			StringBuffer sf = new StringBuffer();
			Object[] key = map.keySet().toArray();
			StringBuffer successId = new StringBuffer();
			for(int i=0;i<key.length;i++){
				 String output = execProcedure(map.get(key[i]), proName);
				 if(!StaticRef.SUCCESS_CODE.equals(output)){
					 if(StaticRef.WARNING_CODE.equals(output.subSequence(0, 2))) {
						sf.append(output);
						sf.append("\r\n,");
					 }	
					 else {
						 sf.append(output.substring(2));
					 }
				 }else{
					 if(!"00".equals(sf.toString())){
						 sf.append("00");
					 }
				 }
				 
				 if("00".equals(output.substring(0,2))){
					 successId.append(key[i]);
					 successId.append(",");
				 }
			 }
			
			sf.append("@");
			sf.append(successId.toString());
			result = sf.toString();
			
			return result;
		}
		
	/**
	 * 执行存储过程调用
	 * @author fanglm
	 * @param list 存储过程参数集合
	 * @param proName 存储过程名称  execPro(?,?,?,?)
	 * @return 操作成功/失败标识
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String execProcedure(ArrayList<String> list, String proName) {
		String result = StaticRef.SUCCESS_CODE; 
		String sql = "{call " + proName + "}";
		Session session = LoginContent.getInstance().getSession();
		Connection conn = null;
		CallableStatement cs = null;
		try{
			conn = session.connection();
			cs = conn.prepareCall(sql);
			
			int i=0;
			for(i=0;i<list.size();i++){
				cs.setString(i+1, list.get(i));
			}
			cs.registerOutParameter(i+1, Types.VARCHAR);
			cs.execute();
			result = cs.getString(i+1);
//			tx.commit();
		}catch (Exception ee) {
			try{
				result = "01" + ee.getMessage();
				LoginContent.getInstance().closeSession();
				session = null;
				return result;
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
	 * 车辆类型的级联，当选取运输类型，级联从车辆类型中取出相关数据
	 * Author：lijun
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<HashMap<String, String>> getCarInfo(String vehicleType) {
		SQLUtil util = new SQLUtil(true);
		StringBuffer sf = new StringBuffer();
		String keys = "LENGTH_UNIT,LENGTH,WIDTH,HEIGHT,WEIGHT_UNIT,MAX_WEIGHT,VOLUME_UNIT,MAX_VOLUME";
		sf.append("select ");
		sf.append(keys);
		sf.append(" from  BAS_VEHICLE_TYPE where id = '");
		sf.append(vehicleType);
		sf.append("'");
		Query query = util.getQuery2(sf.toString(), keys, null);
		
		List<HashMap<String, String>> object = query.list();
		
		
		return object;
	}

		@SuppressWarnings("unchecked")
		@Override
		public String doSave(ArrayList<String> logList,ArrayList<String> jsonList,String appRow) {
			String result = StaticRef.FAILURE_CODE;          
			try {
				HttpServletRequest request = this.getThreadLocalRequest();
				HttpSession session =request.getSession();
				if(session.getAttribute("USER_ID") == null) {
					result += "用户缓存为空,请重新登录!";
				}
				//SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
				Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
				ArrayList<String> sqlList = new ArrayList<String>();
				String sql;
				String seq;
				int ROW = 1;
				HashMap map;
				String odr_no="";
				boolean billReceFlag = false;
				boolean shpmItemFlag = false;
				for(int i = 0; i < jsonList.size(); i++) {
					String json = jsonList.get(i).toString();
					map=new GsonBuilder().create().fromJson(json,mapType);
					billReceFlag = "TRANS_BILL_RECE".equals(map.get("TABLE").toString().toUpperCase());
					shpmItemFlag = "TRANS_SHIPMENT_ITEM".equals(map.get("TABLE").toString().toUpperCase());
					if(i == 0 && !billReceFlag && !shpmItemFlag){
						ROW = getRowNum(map.get("TABLE").toString(), appRow);
					}
					if(shpmItemFlag){
						ROW = getRowNum("(select shpm_row as odr_row, shpm_no from TRANS_SHIPMENT_ITEM where 1=1 "+appRow+")", appRow);
					}
					if(ObjUtil.isNotNull(map.get("ID"))){
						map.put("EDITWHO", "wpsadmin");
						sql = SUtil.getUpdateSQL(map);
						if(billReceFlag){
							odr_no = map.get("DOC_NO").toString();
						}else if(shpmItemFlag){
							map.remove("LD_QNTY");
							map.remove("UNLD_QNTY");
							map.remove("UNLD_GWGT");
							map.remove("UNLD_VOL");
							odr_no = map.get("ODR_NO").toString();
							ROW = ROW - 1;
						}else{
							odr_no = map.get("ODR_NO").toString();
							ROW = ROW - 1;
						}
					}else{
						if(billReceFlag){
							odr_no = map.get("DOC_NO").toString();
							seq = getIDSequence();
							map.put("ID", seq);
							sql = SUtil.getInsertSQL(map, "");
						}else if(shpmItemFlag){
							odr_no = map.get("ODR_NO").toString();
							
							List<?> list = query("select shpm_no from TRANS_SHIPMENT_HEADER where ODR_NO = '"+odr_no+"'");
							int j = 0;
							for (; j < list.size()-1; j++) {
								sqlList.add(getInsertShpmSQL(map, list.get(j), i));
							}
							sql = getInsertShpmSQL(map, list.get(j), i);
							
							HashMap newMap = new HashMap();
							newMap.putAll(map);
							ROW = getRowNum("TRANS_ORDER_ITEM", " and odr_no = '"+odr_no+"'");
							newMap.put("ODR_ROW", ROW + i);
							newMap.remove("UNLD_QNTY");
							newMap.remove("SHPM_ROW");
							newMap.remove("SHPM_NO");
							String GWGT = ObjUtil.ifObjNull(newMap.remove("UNLD_GWGT"), "").toString();
							String VOL = ObjUtil.ifObjNull(newMap.remove("UNLD_VOL"), "").toString();
							newMap.put("LD_GWGT", GWGT);
							newMap.put("LD_VOL", VOL);
							newMap.put("G_WGT", GWGT);
							newMap.put("VOL", VOL);
							newMap.put("QNTY", newMap.get("LD_QNTY"));
							newMap.put("TABLE", "TRANS_ORDER_ITEM");
							sqlList.add(SUtil.getInsertSQL(newMap, ""));
						}else{
							odr_no = map.get("ODR_NO").toString();
							seq = getIDSequence();
							map.put("ID", seq);
							map.put("ODR_ROW", ROW + i);
							sql = SUtil.getInsertSQL(map, "");
						}
					}
					//Log4j.info(StaticRef.SQL_LOG, sql);
					sqlList.add(sql);
				}
				result = excuteSQLList(sqlList);
				if(!billReceFlag){
					//执行统计汇总存储过程
					String proName = "ORDER_QTY_COUNT(?,?)";
					ArrayList<String> list = new ArrayList<String>();
					list.add(odr_no);
					execProcedure(list, proName);
				}
				//插入业务日志
				if(!(logList == null || logList.isEmpty())){
					mapType = new TypeToken<HashMap<String, String>>() {}.getType();
					sqlList = new ArrayList<String>();
					for (String str : logList) {
						map=new GsonBuilder().create().fromJson(str,mapType);
						seq = getIDSequence();
						map.put("ID", seq);
						sqlList.add(SUtil.getInsertSQL(map, ""));
					}
					excuteSQLList(sqlList);
				}
				
				//SUtil.insertLog(logList, result.equals(StaticRef.SUCCESS_CODE)?StaticRef.ACT_SUCCESS:StaticRef.ACT_FAILURE, user.getUSER_NAME(), user.getDEFAULT_ORG_ID_NAME());
			}
			catch(Exception e) {
				result += e.getMessage();
				Log4j.error(StaticRef.ERROR_LOG, e.getMessage());
			}
			return result;
		}
		
		@SuppressWarnings("unchecked")
		private String getInsertShpmSQL(HashMap map, Object shpmNo, int i){
			String seq = getIDSequence();
			Object table = map.get("TABLE");
			Object addTime = map.get("ADDTIME");
			int ROW = getRowNum("(select shpm_row as odr_row, shpm_no from TRANS_SHIPMENT_ITEM where 1=1 and SHPM_NO='" + shpmNo + "')", " and SHPM_NO='" + shpmNo + "'");
			map.put("ID", seq);
			map.put("SHPM_NO", shpmNo);
			map.put("SHPM_ROW", ROW + i);

			String sql = SUtil.getInsertSQL(map, "");
			map.put("TABLE", table);
			map.put("ADDTIME", addTime);
			return sql;
		}
		
		@SuppressWarnings("unchecked")
		private int getRowNum(String tableName,String appRow){
			int result = 1;
			StringBuffer sql = new StringBuffer();
			sql.append("select max(odr_row) as ROW_NUM from ");
			sql.append(tableName);
			sql.append(" where 1=1 ");
			sql.append(appRow);
			
			Session session = LoginContent.getInstance().getSession();
			Query query = session.createSQLQuery(sql.toString()).addScalar("ROW_NUM",Hibernate.STRING);
			List<String> object = query.list();
		    if(object != null && object.size() > 0) {
		    	if(object.get(0) != null){
		    		String count = object.get(0).toString();
		    		result = Integer.parseInt(count) + 1;
		    	}
		    }
			LoginContent.getInstance().closeSession();
			session = null;
	        return result;
		}

		
		
	
	/**
	 * 执行存储过程调用
	 * @author fanglm
	 * @param list 存储过程参数集合
	 * @param proName 存储过程名称  execPro(?,?,?,?)
	 * @return 操作成功/失败标识
	 */
	public String execProcedure(String json, String proName) {
		String result = StaticRef.SUCCESS_CODE;
		String sql = "{call " + proName + "}";
		Connection conn = null;
		CallableStatement cs = null;
		Session session = null;
		try{
			session = LoginContent.getInstance().getSession();
			conn = LoginContent.getInstance().getConnection();
			if(conn == null)
				return StaticRef.FAILURE_CODE;
			Connection sConn = conn.getMetaData().getConnection();
			cs = sConn.prepareCall(sql);
			
			Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
			HashMap<String, String> map=new GsonBuilder().create().fromJson(json,mapType);
			Object[] iter = map.keySet().toArray();
			String value = null;
			int i = 0;
			for(i = 0; i < iter.length; i++) {
				int seq = Integer.parseInt(iter[i].toString());
				value = map.get((String)iter[i]).toString();
				if(value.indexOf("=") <= 0) {
					cs.setString(seq, value);
				}
				else {
					ArrayDescriptor arrayDesc1 = ArrayDescriptor.createDescriptor("LST", sConn);
					String[] ary = null;
					value = value.replace("{", "").replace("}", "");
					if(value.indexOf(",") <= 0) {
						ary = new String[1];
						ary[0] = value;
					}
					else {
						ary = value.split(",");
					}
					String[] strs = new String[ary.length];
					for(int j = 0; j < ary.length; j++) {
						String[] key_val =  ary[j].split("=");
						strs[j] = key_val[1].trim();
					}
					if(strs != null) {
						ARRAY ora_array = new ARRAY(arrayDesc1, sConn, strs);
						cs.setArray(seq, ora_array);
					}
				}
			}
			cs.registerOutParameter(i+1, Types.VARCHAR);
			cs.execute();
			result = cs.getString(i+1);
		}
		catch (Exception e) {
			e.printStackTrace();
			Log4j.error(StaticRef.ERROR_LOG, e.getMessage());
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
	 * 执行存储过程调用
	 * @author fanglm
	 * @param list 存储过程参数集合
	 * @param proName 存储过程名称  execPro(?,?,?,?)
	 * @return 操作成功/失败标识
	 */
	public String execProcedure(HashMap<String, String> map, String proName) {
		String result = StaticRef.SUCCESS_CODE;
		String sql = "{call " + proName + "}";
		Connection conn = null;
		CallableStatement cs = null;
		Session session = null;
		try{
			session = LoginContent.getInstance().getSession();
			conn = LoginContent.getInstance().getConnection().getMetaData().getConnection();
			if(conn == null)
				return StaticRef.FAILURE_CODE;
			Connection sConn = conn.getMetaData().getConnection();
			cs = sConn.prepareCall(sql);
			
			Object[] iter = map.keySet().toArray();
			String value = null;
			int i = 0;
			for(i = 0; i < iter.length; i++) {
				
				int seq = Integer.parseInt(iter[i].toString());
				value = map.get((String)iter[i]).toString();
				if(value.indexOf("=") <= 0) {
					cs.setString(seq, value);
				}
				else {
					ArrayDescriptor arrayDesc1 = ArrayDescriptor.createDescriptor("LST", sConn);
					String[] ary = null;
					value = value.replace("{", "").replace("}", "");
					if(value.indexOf(",") <= 0) {
						ary = new String[1];
						ary[0] = value;
					}
					else {
						ary = value.split(",");
					}
					String[] strs = new String[ary.length];
					for(int j = 0; j < ary.length; j++) {
						String[] key_val =  ary[j].split("=");
						strs[j] = key_val[1].trim();
					}
					if(strs != null) {
						ARRAY ora_array = new ARRAY(arrayDesc1, sConn, strs);
						cs.setArray(seq, ora_array);
					}
				}
			}
			cs.registerOutParameter(i+1, Types.VARCHAR);
			cs.execute();
			result = cs.getString(i+1);
		}
		catch (Exception e) {
			e.printStackTrace();
			Log4j.error(StaticRef.ERROR_LOG, e.getMessage());
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
	 * @author lijun
	 *  运输跟踪服务的update
	 *  @param logList日志信息
	 *  @param list update循环体内部的信息
	 *  @param extra 循环体外的update信息
	 */
	public String transFollowDoUpdate(ArrayList<String> logList, ArrayList<String> list,ArrayList<String> extraList) {
		String result = StaticRef.FAILURE_CODE;          
		try {
			HttpServletRequest request = this.getThreadLocalRequest();
			HttpSession session =request.getSession();
			if(session.getAttribute("USER_ID") == null) {
				result += "用户缓存为空,请重新登录!";
			}
			SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
			Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
			ArrayList<String> sqlList = new ArrayList<String>();
            String sql = "";
               if(extraList != null) {
				 for(int m = 0; m < extraList.size(); m++) {
					sql = extraList.get(m).toString();
					//Log4j.info(StaticRef.SQL_LOG, sql);
					sqlList.add(sql);
			     }
		       }
			   for(int i = 0; i < list.size(); i++) {
				   String json = list.get(i).toString();
				   HashMap<String,String> map=new GsonBuilder().create().fromJson(json,mapType);
				   map.put("EDITWHO", user.getUSER_ID());
				   sql = SUtil.getUpdateSQL(map);
				   //Log4j.info(StaticRef.SQL_LOG, sql);
				   sqlList.add(sql);
			}
			   result = excuteSQLList(sqlList);
			   //SUtil.insertLog(logList, result.equals(StaticRef.SUCCESS_CODE)?StaticRef.ACT_SUCCESS:StaticRef.ACT_FAILURE, user.getUSER_NAME(), user.getDEFAULT_ORG_ID_NAME());
		}
		catch(Exception e) {
			result += e.getMessage();
			Log4j.error(StaticRef.ERROR_LOG, e.getMessage());
		}
		return result;
	}
	
	//托运单管理--提货单打印
	@SuppressWarnings("unchecked")
	@Override
	public String ordloadPrintView(String id) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
		if(session.getAttribute("USER_ID") == null) {
			return StaticRef.FAILURE_CODE + "用户缓存为空,请重新登录!";
		}
		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		SQLUtil util = new SQLUtil(true);
        String path = getServletContext().getRealPath("/");
        String key = "";
        
        ArrayList printDataList = new ArrayList();
        ArrayList printMapList = new ArrayList(); 
        
        String keys=util.getColName("V_ORDER_HEADER");
        
        StringBuffer buff = new StringBuffer();
        buff.append("select ");
        buff.append(keys);
        buff.append(" from V_ORDER_HEADER");
        buff.append(" where odr_no in (" + id + ") order by odr_no");
        Query query = util.getQuery2(buff.toString(), keys, null);
        List<HashMap<String, String>> objList = query.list();
        int print = 1;
        for(int i=0;i<objList.size();i++){
	        HashMap<String, String> map = objList.get(i);
	        print = Integer.valueOf(ObjUtil.ifNull(map.get("LOAD_PRINT_COUNT"),"0")) + 1;
	        map.put("PRINT_COUNT", "第" + Integer.valueOf(print) +"次打印");
	        map.put("PRINT_WHO", user.getUSER_NAME());
	        map.put("TITLE_NAME", map.get("CUSTOMER_NAME")+"提货单");
	        printMapList.add(map);
	    }
        objList.clear();
        
        
        //取出作业单明细信息
        String itkey = util.getColName("V_PRINT_SHIPMENT_ITEM");
        buff = new StringBuffer();
        buff.append("select ");
        buff.append(itkey);
        buff.append(" from V_PRINT_SHIPMENT_ITEM");//TRANS_SHIPMENT_ITEM
        buff.append(" where ODR_NO in (");
        buff.append(id);
        buff.append(") order by ODR_NO,odr_row");
        query = util.getQuery2(buff.toString(), itkey, null);
        List<HashMap<String, String>> object = query.list();
 
        ArrayList<HashMap<String, String>> item  = new ArrayList<HashMap<String,String>>();
        key = "";
        for(int i =0;i<object.size();i++){
        	if(key.equals(object.get(i).get("ODR_NO"))){
        		item.add(object.get(i));
        	}else{
        		if(i > 0){
        			printDataList.add(item);
        		}
        		key = object.get(i).get("ODR_NO");
        		item = new ArrayList<HashMap<String,String>>();
        		item.add(object.get(i));
        	}
        }
        printDataList.add(item);
        
        String reportName = path + "reportModel/loadment_zt.jasper";  //报表模板相对路径
        
        String pathName = path + "user/" + user.getUSER_ID();
        File file = new File(pathName);
        if(!file.exists()){
        	file.mkdirs();
        }
        String fileName = pathName + "/OdrLoadmentPrint.pdf";
        String result_code = PrintUtil.viewPrintActPS(reportName, printMapList, printDataList,fileName,this.getThreadLocalResponse());
		
		StringBuffer sql = new StringBuffer();
		sql.append("update trans_order_header set load_print_count = ");
		sql.append(print);
		sql.append(" where ODR_NO in (");
		sql.append(id);
		sql.append(")");
		if(result_code.equals(StaticRef.SUCCESS_CODE)){
			excuteSQL(sql.toString());
			return "../user/" + user.getUSER_ID() + "/OdrLoadmentPrint.pdf";
		}else{
			return result_code;
		}
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public String loadPrintView(String id,String json, String load_no, boolean isShow){
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
		if(session.getAttribute("USER_ID") == null) {
			return StaticRef.FAILURE_CODE + "用户缓存为空,请重新登录!";
		}
		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		SQLUtil util = new SQLUtil(true);
        String path = getServletContext().getRealPath("/");
        String key = "";
        String code = "00";
        ArrayList printDataList = new ArrayList();
        ArrayList printMapList = new ArrayList(); 
        
        String keys=util.getColName("V_SHIPMENT_HEADER_PRINT");
        
        StringBuffer buff = new StringBuffer();
        buff.append("select ");
        buff.append(keys);
        buff.append(" from v_shipment_header_print");
        buff.append(" where load_no='");
        buff.append(load_no);
        buff.append("' ");
        buff.append(" and print_flag='Y'");
        //String customer_name ="洋河酒业";
        
        if(id.length() > 1){
        	buff.append(" and SHPM_NO in (" + id + ") ");
        }
        
        buff.append(" order by SHPM_NO,ODR_NO");
       
        
        
        Query query = util.getQuery2(buff.toString(), keys, null);
        List<HashMap<String, String>> objList = query.list();
        if(objList.size()==0 && !isShow){
        	return "01" + "所有作业单都已打印，请重新获取授权！";
        }
        
        int print = 1;
        String doc_no = "ODR_NO";
	    for(int i=0;i<objList.size();i++){
	        HashMap<String, String> map = objList.get(i);
	        //customer_name = map.get("CUSTOMER_NAME");
	        //if (customer_name.equals("双沟酒业")){
	        //	doc_no = "SHPM_NO";
	        //}
	        if(i == 0){
	        	key = ObjUtil.ifObjNull(map.get(doc_no),"").toString();
	        	print = Integer.valueOf(ObjUtil.ifNull(map.get("LOAD_PRINT_COUNT"),"0")) + 1;
		        map.put("PRINT_COUNT", "第" + Integer.valueOf(print) +"次打印");
	        	map.put("PRINT_WHO",user.getUSER_NAME());
	        	map.put("TITLE_NAME", map.get("CUSTOMER_NAME")+"提货单");
		        printMapList.add(map);
	        }
	        else if(!key.equals(ObjUtil.ifObjNull(map.get("ODR_NO"),""))){
	        	key = ObjUtil.ifObjNull(map.get(doc_no),"").toString();
	        	print = Integer.valueOf(ObjUtil.ifNull(map.get("LOAD_PRINT_COUNT"),"0")) + 1;
		        map.put("PRINT_COUNT", "第" + Integer.valueOf(print) +"次打印");
	        	map.put("PRINT_WHO", user.getUSER_NAME());
	        	map.put("TITLE_NAME", map.get("CUSTOMER_NAME")+"提货单");
		        printMapList.add(map);
	        }
	    }
        objList.clear();
        
        
        //取出作业单明细信息
        String itkey = util.getColName("V_PRINT_SHIPMENT_ITEM");
        buff = new StringBuffer();
        buff.append("select ");
        buff.append(itkey);
        buff.append(" from V_PRINT_SHIPMENT_ITEM");//TRANS_SHIPMENT_ITEM
        buff.append(" where load_no='");
        buff.append(load_no);
        buff.append("' ");
        buff.append(" and print_flag='Y'");
        if(id.length() > 1){
        	buff.append(" and SHPM_NO in (" + id + ") ");
        }
        buff.append(" order by shpm_no,shpm_row");
        query = util.getQuery2(buff.toString(), itkey, null);
        List<HashMap<String, String>> object = query.list();
 
        ArrayList<HashMap<String, String>> item  = new ArrayList<HashMap<String,String>>();
        key = "";
       
        for(int i =0;i<object.size();i++){
        	if(key.equals(object.get(i).get(doc_no))){
        		item.add(object.get(i));
        	}else{
        		if(i > 0){
        			printDataList.add(item);
        		}
        		key = object.get(i).get(doc_no);
        		item = new ArrayList<HashMap<String,String>>();
        		item.add(object.get(i));
        	}
        }
        printDataList.add(item);
        
        String reportName = path + "reportModel/loadment.jasper";  //报表模板相对路径
        
        String pathName = path + "user/" + user.getUSER_ID();
        File file = new File(pathName);
        if(!file.exists()){
        	file.mkdirs();
        }
        String fileName = pathName + "/LoadmentPrint.pdf";
        
        File pdf_file = new File(fileName);
		if(pdf_file.exists()){
			pdf_file.delete();
		}
		
		String result_code = PrintUtil.viewPrintActPS(reportName, printMapList, printDataList,fileName,this.getThreadLocalResponse());
		
		
		
		if(result_code.equals(StaticRef.SUCCESS_CODE)){
			
			if(!isShow){
				code = execProcedure(json, "SP_PRINT_LOG(?,?,?,?,?,?)");
			}
			//PDF加密
			try{
				PdfReader reader = new PdfReader(fileName);
				
				//reader.setViewerPreferences(PdfWriter.HideMenubar|PdfWriter.HideToolbar|PdfWriter.HideWindowUI);
				//PdfEncryptor.encrypt(reader, new FileOutputStream( pathName+"/JMLoadmentPrint.pdf"), null, null, 0, false);
			    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream( pathName+"/JMLoadmentPrint.pdf"));
			    PdfWriter writer = stamper.getWriter(); 
			    if(isShow){
			    	stamper.setEncryption(null,null,0,PdfWriter.STRENGTH40BITS);
			    }else{
			    	stamper.setEncryption(null, null, PdfWriter.AllowPrinting
			    			,PdfWriter.STRENGTH40BITS);
			    }
			    writer.setViewerPreferences(PdfWriter.HideToolbar);
			    // | PdfWriter.HideMenubarwriter.setViewerPreferences(PdfWriter.HideWindowUI);
			    stamper.close();
			    //安全起见，删除未加密文件
			    File extfile = new File(fileName);
		        if(extfile.exists()){
		        	extfile.delete();
		        }
		        //String filePath=this.getServletConfig().getServletContext().getRealPath("/");
			    return "../user/" + user.getUSER_ID() + "/JMLoadmentPrint.pdf" + "@" + code;
			}catch (Exception e) {
				return StaticRef.FAILURE_CODE + e.getMessage();
			}
			
		}else{
			return result_code;
		}
		
	}
	
	
	/**
	 * 作业单打印功能实现
	 * @author wangjun 打印预览
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String shpmPrintView(String id,String load_id,String type) {
		
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
		if(session.getAttribute("USER_ID") == null) {
			return StaticRef.FAILURE_CODE + "用户缓存为空,请重新登录!";
		}
		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		SQLUtil util = new SQLUtil(true);
		String key = "";
		
		String path = getServletContext().getRealPath("/");
		
        ArrayList printDataList = new ArrayList();
        ArrayList printMapList = new ArrayList();
		
        String keys=util.getColName("V_SHIPMENT_HEADER_PRINT");
		//取出订单主信息
		StringBuffer buff = new StringBuffer();
        buff.append("select ");
        buff.append(keys);
        buff.append(" from v_shipment_header_print");
        buff.append(" where 1=1 ");
        if(id.length() > 1){
        	buff.append("and CUSTOM_ODR_NO in (");
        	buff.append(id);
 	        buff.append(") ");
        }
        buff.append("and LOAD_NO='");
        buff.append(load_id);
        buff.append("' order by custom_odr_no");
        
        Query query = util.getQuery2(buff.toString(), keys, null);

	    List<HashMap<String, String>> objList = query.list();
	    int print = 1;
	    for(int i=0;i<objList.size();i++){
	        HashMap<String, String> map = objList.get(i);
	        String PICKING_STAT = ObjUtil.ifObjNull(map.get("PICKING_STAT"),"").toString();
	        String shpm_no=map.get("SHPM_NO");
	        if(PICKING_STAT.equals("15")){
	        	return "01"+ "作业单【"+shpm_no+"】仍在分拣库中，不能打印送货单 !";
	        }
	        if(i == 0){
	        	key = ObjUtil.ifObjNull(map.get("CUSTOM_ODR_NO"),"").toString();
	        	print = Integer.valueOf(ObjUtil.ifNull(map.get("SHPM_PRINT_COUNT"),"0")) + 1;
		        map.put("PRINT_COUNT", "第" + Integer.valueOf(print) +"次打印");
	        	map.put("PRINT_WHO", user.getUSER_NAME());
	        	map.put("TITLE_NAME", map.get("CUSTOMER_NAME")+"送货单");
		        printMapList.add(map);
	        }else if(!key.equals(ObjUtil.ifObjNull(map.get("CUSTOM_ODR_NO"),""))){
	        	key = ObjUtil.ifObjNull(map.get("CUSTOM_ODR_NO"),"").toString();
	        	print = Integer.valueOf(ObjUtil.ifNull(map.get("SHPM_PRINT_COUNT"),"0")) + 1;
		        map.put("PRINT_COUNT", "第" + Integer.valueOf(print) +"次打印");
	        	map.put("PRINT_WHO", user.getUSER_NAME());
	        	map.put("TITLE_NAME", map.get("CUSTOMER_NAME")+"送货单");
		        printMapList.add(map);
	        }
	    }
        objList.clear();
        
        
        //取出订单明细信息
        String itkey = util.getColName("V_PRINT_SHIPMENT_ITEM");
        buff = new StringBuffer();
        buff.append("select ");
        buff.append(itkey);
        buff.append(" from V_PRINT_SHIPMENT_ITEM");
        buff.append(" where 1=1 ");
        if(id.length() > 1){
        	buff.append("and CUSTOM_ODR_NO in (");
        	buff.append(id);
 	        buff.append(") ");
        }
        buff.append("and LOAD_NO='");
        buff.append(load_id);
        buff.append("' order by custom_odr_no,shpm_no,shpm_row");
        query = util.getQuery2(buff.toString(), itkey, null);
        List<HashMap<String, String>> object = query.list();
        
        ArrayList<HashMap<String, String>> item  = new ArrayList<HashMap<String,String>>();
        for(int i =0;i<object.size();i++){
        	if(key.equals(ObjUtil.ifObjNull(object.get(i).get("CUSTOM_ODR_NO"),"").toString())){
        		item.add(object.get(i));
        	}else{
        		if(i > 0){
        			printDataList.add(item);
        		}
        		key = ObjUtil.ifObjNull(object.get(i).get("CUSTOM_ODR_NO"),"").toString();
        		item = new ArrayList<HashMap<String,String>>();
        		item.add(object.get(i));
        	}
        }
        printDataList.add(item);
        String pathName = path + "user/" + user.getUSER_ID();
        File file = new File(pathName);
        if(!file.exists()){
        	file.mkdirs();
        }
        
        String reportName = path + "reportModel/shipment.jasper";  //报表模板相对路径
        String pdf_name = "/ShipmentPrint.pdf";
        if(type.equals("SFDAN")){
        	reportName = path + "reportModel/suifudan.jasper";
        	pdf_name = "/SFDPrint.pdf";
        }
        
        
        String fileName = pathName + pdf_name;
        
        File pdf_file = new File(fileName);
		if(pdf_file.exists()){
			pdf_file.delete();
		}
		
		String result_code = PrintUtil.viewPrintActPS(reportName, printMapList, printDataList,fileName,this.getThreadLocalResponse());
		
        
		if(result_code.equals(StaticRef.SUCCESS_CODE)){
			if(!type.equals("SFDAN")){
				StringBuffer sql = new StringBuffer();
				sql.append("update trans_shipment_header set shpm_print_count = ");
				sql.append(print);
				sql.append(" where 1=1 ");
		        if(id.length() > 1){
		        	sql.append("and CUSTOM_ODR_NO in (");
		        	sql.append(id);
		 	        sql.append(") ");
		        }
				sql.append("and LOAD_NO='");
				sql.append(load_id);
		        sql.append("'");
		        excuteSQL(sql.toString());//执行打印次数更新
			}
			return "../user/" + user.getUSER_ID() + pdf_name;
		}else{
			return result_code;
		}
       
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	public String UnShpmPrintView(String id,String json, boolean isShow){
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
		if(session.getAttribute("USER_ID") == null) {
			return StaticRef.FAILURE_CODE + "用户缓存为空,请重新登录!";
		}
		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		SQLUtil util = new SQLUtil(true);
        String path = getServletContext().getRealPath("/");
        String key = "";
        
        ArrayList printDataList = new ArrayList();
        ArrayList printMapList = new ArrayList(); 
        
        String keys=util.getColName("V_SHIPMENT_HEADER");
        
        StringBuffer buff = new StringBuffer();
        buff.append("select ");
        buff.append(keys);
        buff.append(" from v_shipment_header");
        buff.append(" where ");
        if(id.length() > 1){
        	buff.append(" SHPM_NO in (" + id + ") ");
        }
        buff.append(" order by ODR_NO");
        Query query = util.getQuery2(buff.toString(), keys, null);
        List<HashMap<String, String>> objList = query.list();
        
        int print = 1;
	    for(int i=0;i<objList.size();i++){
	        HashMap<String, String> map = objList.get(i);
	        if(i == 0){
	        	key = ObjUtil.ifObjNull(map.get("ODR_NO"),"").toString();
	        	print = Integer.valueOf(ObjUtil.ifNull(map.get("LOAD_PRINT_COUNT"),"0")) + 1;
		        map.put("PRINT_COUNT", "第" + Integer.valueOf(print) +"次打印");
	        	map.put("PRINT_WHO",user.getUSER_NAME());
	        	map.put("TITLE_NAME", map.get("CUSTOMER_NAME")+"提货单");
		        printMapList.add(map);
	        }else if(!key.equals(ObjUtil.ifObjNull(map.get("ODR_NO"),""))){
	        	key = ObjUtil.ifObjNull(map.get("ODR_NO"),"").toString();
	        	print = Integer.valueOf(ObjUtil.ifNull(map.get("LOAD_PRINT_COUNT"),"0")) + 1;
		        map.put("PRINT_COUNT", "第" + Integer.valueOf(print) +"次打印");
	        	map.put("PRINT_WHO", user.getUSER_NAME());
	        	map.put("TITLE_NAME", map.get("CUSTOMER_NAME")+"提货单");
		        printMapList.add(map);
	        }
	    }
        objList.clear();
        
        
        //取出作业单明细信息
        String itkey = util.getColName("V_PRINT_SHIPMENT_ITEM");
        buff = new StringBuffer();
        buff.append("select ");
        buff.append(itkey);
        buff.append(" from V_PRINT_SHIPMENT_ITEM");//TRANS_SHIPMENT_ITEM
        buff.append(" where ");
        if(id.length() > 1){
        	buff.append(" SHPM_NO in (" + id + ") ");
        }
        buff.append(" order by shpm_no,shpm_row");
        query = util.getQuery2(buff.toString(), itkey, null);
        List<HashMap<String, String>> object = query.list();
 
        ArrayList<HashMap<String, String>> item  = new ArrayList<HashMap<String,String>>();
        key = "";
        for(int i =0;i<object.size();i++){ 
        	
        	if (key.equals(object.get(i).get("ODR_NO"))){
        		item.add(object.get(i));
        	}else{
        		if(i > 0){
        			printDataList.add(item);
        		}
        		key = object.get(i).get("ODR_NO");
        		item = new ArrayList<HashMap<String,String>>();
        		item.add(object.get(i));
        	}
        }
        printDataList.add(item);
        
        String reportName = path + "reportModel/loadment.jasper";  //报表模板相对路径
        
        String pathName = path + "user/" + user.getUSER_ID();
        File file = new File(pathName);
        if(!file.exists()){
        	file.mkdirs();
        }
        String fileName = pathName + "/LoadmentPrint.pdf";
		String result_code = PrintUtil.viewPrintActPS(reportName, printMapList, printDataList,fileName,this.getThreadLocalResponse());
		
		
		
		if(result_code.equals(StaticRef.SUCCESS_CODE)){
			
			if(!isShow){
				//String code = execProcedure(json, "SP_PRINT_LOG(?,?,?,?,?,?)");
				//System.out.println(code);
			}
			
			
			//PDF加密
			try{
				PdfReader reader = new PdfReader(fileName);
			    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream( pathName+"/JMLoadmentPrint.pdf"));
			    PdfWriter writer = stamper.getWriter(); 
			    if(isShow){
			    	stamper.setEncryption(null,null,0,PdfWriter.STRENGTH40BITS);
			    }else{
			    	stamper.setEncryption(null, null, PdfWriter.AllowPrinting
			    			,PdfWriter.STRENGTH40BITS);
			    }
			    writer.setViewerPreferences(PdfWriter.HideToolbar);
//			    writer.setViewerPreferences(PdfWriter.HideWindowUI);
			    stamper.close();
			    //安全起见，删除未加密文件
			    File extfile = new File(fileName);
		        if(extfile.exists()){
		        	extfile.delete();
		        }
			    return "./user/" + user.getUSER_ID() + "/JMLoadmentPrint.pdf";
			}catch (Exception e) {
				return e.getMessage();
			}
			
		}else{
			return result_code;
		}
		
	}
	
	/**
	 * 手动触发EDI接口获取订单
	 * @author fanglm
	 */
	@Override
	public String runTimer(String type) {
		StringBuffer msg = new StringBuffer();
		if("YANGHE".equals(type)){
			YHTimerRun yhTimer = new YHTimerRun();
			msg.append(yhTimer.doSth());
			YHSendTimerRun yhSent = new YHSendTimerRun();
			msg.append(yhSent.doSth());
		}else{
			SGTimerRun sgTimer = new SGTimerRun();
			msg.append(sgTimer.doSth());
			SGSendTimerRun sgSent = new SGSendTimerRun();
			msg.append(sgSent.doSth());
		}
		return msg.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getBasVal(String load_no,String shpmNo, String feeBas) {
		String key = "tot_qnty";
		String result = "0";
		Session session = LoginContent.getInstance().getSession();
		
		if(feeBas.equals("体积")){
			key = "tot_vol";
		}else if(feeBas.equals("数量")){
			key = "tot_qnty_each";
		}else if(feeBas.equals("净重")){
			key = "tot_net_w";
		}else if(feeBas.equals("毛重")){
			key = "tot_gross_w";
		}else{
			key = "0";
		}
		StringBuffer sql = new StringBuffer();
		if(ObjUtil.isNotNull(shpmNo)){
			sql.append("select ");
			sql.append(key);
			sql.append(" as BAS_VAL from trans_shipment_header where shpm_no='");
			sql.append(shpmNo);
			sql.append("'");
		}else{
			sql.append("select sum(");
			sql.append(key);
			sql.append(") as BAS_VAL from trans_shipment_header where load_no='");
			sql.append(load_no);
			sql.append("'");
		}
		 Query query = session.createSQLQuery(sql.toString()).addScalar("BAS_VAL", Hibernate.STRING);
	     List<String> object = query.list();
	     if(object != null && object.size() > 0) {
	        result = object.get(0).toString();
	     }
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getBasVal(String shpmNo,String feeBas, boolean isRdc) {
		String key = "tt.tot_qnty";
		String result = "0";
		Session session = LoginContent.getInstance().getSession();
		
		if(feeBas.equals("体积")){
			key = "nvl(tt.tot_vol,0)";
		}else if(feeBas.equals("数量")){
			key = "nvl(tt.tot_qnty_each,0)";
		}else if(feeBas.equals("净重")){
			key = "nvl(tt.tot_net_w,0)";
		}else if(feeBas.equals("毛重")){
			key = "nvl(tt.tot_gross_w,0)";
		}else{
			key = "1";
		}
		
		StringBuffer sql = new StringBuffer();
		if(!isRdc){
			sql.append("select ");
			sql.append(key);
			sql.append(" as BAS_VAL from trans_shipment_header TT where shpm_no='");
			sql.append(shpmNo);
			sql.append("'");
		}else{
			sql.append("select sum(");
			sql.append(key);
			sql.append(") as BAS_VAL from trans_shipment_header t,trans_shipment_header tt ");
			sql.append(" where t.load_id = tt.load_id and t.unload_area_id = tt.unload_area_id ");
			sql.append(" and to_char(t.odr_time,'yyyy-mm-dd') = to_char(tt.odr_time,'yyyy-mm-dd') ");
			sql.append(" and t.shpm_no='");
			sql.append(shpmNo);
			sql.append("'");
		}
		 Query query = session.createSQLQuery(sql.toString()).addScalar("BAS_VAL", Hibernate.STRING);
	     List<String> object = query.list();
	     if(object != null && object.size() > 0) {
	        result = object.get(0).toString();
	     }
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getBasVal(String shpmNo,String feeBas) {
		String key = "tt.tot_qnty";
		String result = "0";
		Session session = LoginContent.getInstance().getSession();
		
		if(feeBas.equals("体积")){
			key = "nvl(tt.tot_vol,0)";
		}else if(feeBas.equals("数量")){
			key = "nvl(tt.tot_qnty_each,0)";
		}else if(feeBas.equals("净重")){
			key = "nvl(tt.tot_net_w,0)";
		}else if(feeBas.equals("毛重")){
			key = "nvl(tt.tot_gross_w,0)";
		}else{
			key = "1";
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(key);
		sql.append(" as BAS_VAL from trans_order_header TT where odr_no='");
		sql.append(shpmNo);
		sql.append("'");
	
		 Query query = session.createSQLQuery(sql.toString()).addScalar("BAS_VAL", Hibernate.STRING);
	     List<String> object = query.list();
	     if(object != null && object.size() > 0) {
	        result = object.get(0).toString();
	     }
		return result;
	}

	@Override
	public String getColName(String vName) {
		SQLUtil util = new SQLUtil(true);
		return util.getColName(vName);
	}

	/**
	 * 手动触发获取订单
	 * @author yuanlei
	 */
	@Override
	public String getU8Order(String type,String custom_odr_no) {
		StringBuffer msg = new StringBuffer();
		if("YH".equals(type)){
			U8YHManualAction yh = new U8YHManualAction();
			msg.append(yh.doSth(custom_odr_no));
		}else{
			U8SGManualAction sg = new U8SGManualAction();
			msg.append(sg.doSth(custom_odr_no));
		}
		return msg.toString();
	}

	@Override
	public String dispatchPrintView(String paramName, String paramValue, String printType) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
		if(session.getAttribute("USER_ID") == null) {
			return StaticRef.FAILURE_CODE + "用户缓存为空,请重新登录!";
		}
		if(paramName == null || "".equals(paramName)){
			paramName = "loadNo";
		}
		if(paramValue == null || "".equals(paramValue)){
			return request.getContextPath()+
			"/report?reportModel="+printType;
		}
		return request.getContextPath()+
				"/report?reportModel="+printType+"&"+paramName+"="+paramValue;
	}

	private String countQuery(String sql) {
		String result = "";
		List<?> list = query(sql);
		if(!(list==null || list.isEmpty())){
			for (Object object : list) {
				if(object!=null){
					result = String.valueOf(object);
				}
				
			}
		}
		return result;
	}

	private List<?> query(String sql) {
		Session curSession = LoginContent.getInstance().getSession();
		SQLQuery query = (SQLQuery)curSession.createSQLQuery(sql);
		return query.list();
	}

	@Override
	public String excuteSQLListCheckUn(ArrayList<String> sqlList,
			String firstExceSql) {
		String result = "";
		if(firstExceSql != null) {
			String ret = countQuery(firstExceSql);
			if(!ObjUtil.isNotNull(ret)){
				result = excuteSQLList(sqlList);
			}
			else {
				result = "01" + ret;
			}
		} 
		else {
			result = excuteSQLList(sqlList);
		}
		return result;
	}

	@Override
	public Map<String, Object> queryData(String sql, boolean isSingleData) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<List<String>> list = new ArrayList<List<String>>();
		List<?> sqlResult = query(sql);
		if(!(sqlResult==null || sqlResult.isEmpty())){
			if(isSingleData && sqlResult.size() == 1){
				resultMap.put("singleData", sqlResult.get(0).toString());
			}else{
				for (Object object : sqlResult) {
					if(object!=null){
						Object[] objs;
						if(object.getClass().isArray()){
							objs = (Object[])object;
						}else{
							objs = new Object[]{object};
						}
						List<String> tempList = new ArrayList<String>();
						for (Object object2 : objs) {
							if(object2 == null){
								tempList.add("");
								continue;
							}
							tempList.add(String.valueOf(object2));
						}
						list.add(tempList);
					}
					
				}
			}
		}
		resultMap.put("data", list);

		return resultMap;
	}

	@Override
	public List<Map<String, Object>> executeScript(String sql) {
		return SQLFileUtil.executeSql(sql);
	}
	
	private String getInsertStrByMap(Map<String, String> map){
		StringBuilder sb = new StringBuilder();
		StringBuilder sbVal = new StringBuilder();
		sb.append("insert into ");
		sb.append(map.get("TableName"));
		sb.append("(");
		sbVal.append(" values(");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if("TableName".equals(entry.getKey())){
				continue;
			}
			sb.append(entry.getKey());
			sb.append(",");
			if("ID".equals(entry.getKey()) || 
					"ADDTIME".equals(entry.getKey()) || 
					"EDITTIME".equals(entry.getKey())){
				sbVal.append(entry.getValue());
			}else{
				sbVal.append("'");
				sbVal.append(entry.getValue());
				sbVal.append("'");
			}
			sbVal.append(",");
		}
		int sbIndex = sb.lastIndexOf(",");
		int sbValIndex = sbVal.lastIndexOf(",");
		sb.replace(sbIndex, sbIndex+1, ")");
		sbVal.replace(sbValIndex, sbValIndex+1, ")");
		sb.append(sbVal);
		return sb.toString();
	}
	
	private String getUpdateStrByMap(Map<String, String> map){
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		sb.append(map.get("TableName"));
		sb.append(" set ");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if("ID".equals(entry.getKey()) || 
					"TableName".equals(entry.getKey())){
				continue;
			}
			sb.append(entry.getKey());
			sb.append("=");
			if("ADDTIME".equals(entry.getKey()) || 
					"EDITTIME".equals(entry.getKey())){
				sb.append(entry.getValue());
			}else{
				sb.append("'");
				sb.append(entry.getValue());
				sb.append("'");
			}
			sb.append(",");
		}
		int sbIndex = sb.lastIndexOf(",");
		sb.replace(sbIndex, sbIndex+1, " ");
		sb.append(" where ID='");
		sb.append(map.get("ID"));
		sb.append("'");
		return sb.toString();
	}
	
	private String getCheckStrByMap(Map<String, String> dataMap, String[] checkFields){
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) from ");
		sb.append(dataMap.get("TableName"));
		sb.append(" where ");
		int i = 0;
		for (String key : checkFields) {
			if("ID".equals(key))
				continue;
			if(i++ > 0)
				sb.append(" and ");
			sb.append(key);
			sb.append("='");
			sb.append(dataMap.get(key));
			sb.append("'");
		}
		String id = dataMap.get("ID");
		if(!(id == null || "sys_guid()".equals(id))){
			sb.append(" and");
			sb.append(" ID <> '");
			sb.append(id);
			sb.append("'");
		}
		return sb.toString();
	}
	
	private ArrayList<String> toListStr(List<Map<String, String>> listData){
		ArrayList<String> list = new ArrayList<String>();
		for (Map<String, String> map : listData) {
			toListStr(list, map);
		}
		return list;
	}
	
	private ArrayList<String> toListStr(ArrayList<String> list, Map<String, String> map){
		if(list == null){
			list = new ArrayList<String>();
		}
		String id = map.get("ID");
		if(id == null || id.trim().length()==0){
			id = "sys_guid()";
			map.put("ID", id);
			list.add(getInsertStrByMap(map));
		}else{
			list.add(getUpdateStrByMap(map));
		}
		return list;
	}

	@Override
	public String excuteSQLListByMap(List<Map<String, String>> listData,
			String[] checkFields) {
		String result = StaticRef.SUCCESS_CODE;
		if(checkFields == null || checkFields.length == 0){
			result = excuteSQLList(toListStr(listData));
		}else{
			for (Map<String, String> map : listData) {
				result = excuteSQLListCheckUn1(toListStr(null, map), getCheckStrByMap(map, checkFields));
				if("uniquene".equals(result)){
					return result;
				}
			}
		}
		return result;
	}
	
	@Override
	public String excuteSQLListCheckUn1(ArrayList<String> sqlList, String firstExceSql) {
		if(firstExceSql == null || countQuery1(firstExceSql) == 0){
			return excuteSQLList(sqlList);
		}
		return "uniquene";
	}
	
	private int countQuery1(String sql) {
		List<?> list = query(sql);
		return list == null||list.isEmpty()? 0 : 
			Integer.parseInt(list.get(0).toString());
	}
	
	@Override
	public LinkedHashMap<String, Object> getSFAddrInfo(String sql) {
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
		List<List<String>> list = new ArrayList<List<String>>();
		List<?> sqlResult = query(sql);
		if (!(sqlResult == null || sqlResult.isEmpty())) {
			for (Object object : sqlResult) {
				if (object != null) {
					Object[] objs=(Object[])object;
					List<String> tempList=new ArrayList<String>();
					for(Object object2:objs){
						if(object2==null){
							tempList.add("");
							continue;
						}
						tempList.add(String.valueOf(object2));
					}
					list.add(tempList);
				}
			}

		}
		resultMap.put("SFData", list);
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getTransNo() {
		String result = "";
		Session session = LoginContent.getInstance().getSession();
        StringBuffer buff = new StringBuffer();
        buff.append("select nvl(t.trans_no, '') as trans_no from SF_TRANS_NO t where t.USE_FLAG = 'N' and rownum=1");
        Query query = session.createSQLQuery(buff.toString()).addScalar("trans_no", Hibernate.STRING);
        List<String> object = query.list();
        if(object != null && object.size() > 0) {
        	result = object.get(0).toString();
        }
        setTransNo(result, "Y");
        return result;
	}

	@Override
	public String setTransNo(String transNo, String useFlag) {
		if(!ObjUtil.isNotNull(transNo)) return "01";
		if(!ObjUtil.isNotNull(useFlag)){
			useFlag = "N";
		}
        StringBuffer buff = new StringBuffer();
        buff.append("update sf_trans_no set use_flag='"+useFlag+"' where trans_no='"+transNo+"'");
        return excuteSQL(buff.toString());
	}
	
	/*
	@Override
	public LinkedHashMap<String, Object> getSFAddrInfo(String sql) {
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		List<?> sqlResult = query(sql);
		if (!(sqlResult == null || sqlResult.isEmpty())) {
			for (Object object : sqlResult) {
				if (object != null) {
					String str=(String)object;
					list.add(str);
				}
			}

		}
		resultMap.put("SFData", list);
		return resultMap;
	}
*/ 
	@SuppressWarnings("unchecked")
	@Override
	public String customExportAction(String headers, String filedNames,
			String sql, HashMap<String, String> paramMap) {
		HttpServletResponse response = this.getThreadLocalResponse();
		response.setContentType("octets/stream");
		response.setCharacterEncoding("UDF-8");
		response.addHeader("Content-Disposition", "attachment;filename=result.xls");
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
		if(session.getAttribute("USER_ID") == null || 
				paramMap == null || paramMap.isEmpty()) {
			return "";
		}
		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		String customId = paramMap.get("CUSTOMER_ID");
		String customSql = "select CUSTOMER_CODE,CUSTOMER_CNAME,CONT_TEL,CONT_FAX,ADDRESS,ORG_ENAME from v_customer where id = '"+customId+"'";
		Query queryCustom = LoginContent.getInstance().getSession().createSQLQuery(customSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, String>> customObject = queryCustom.list();
		if(customObject == null || customObject.size() == 0){
			return "";
		}
		HashMap<String, String> customMap = customObject.get(0);
		customMap.put("USER_ID", user.getUSER_ID());
		customMap.put("ODR_TIME", paramMap.get("ODR_TIME"));
		
		String hql =  "select count(1)||'' CC from (" + sql +")";
		Query query = LoginContent.getInstance().getSession().createSQLQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, String>> object = query.list();
		HashMap<String, String> map = (HashMap<String, String>)object.get(0);
		int max_row = Integer.parseInt(map.get("CC"));
		if(max_row >= 20000){
			return StaticRef.FAILURE_CODE;
		}
		
		CustomExportExcel ex = null;
	      
		String path = getServletContext().getRealPath("/");
		path = path + "user/" + user.getUSER_ID();  //不同用户建立不同文件夹，解决多线程数据对冲
	      
		File file = new File(path +  "/result_custom.xls");

		try {
	    	  
			File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			if(!filePath.exists()){
				filePath.mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			
			OutputStream out = response.getOutputStream();
			List list = null;
			HSSFWorkbook book = null;
			//避免大数据量导出时造成系统瘫痪，每次查询出3000条数据导出EXCEL，循环执行
			for(int i=0;i<max_row/3000+1;i++){
				query = LoginContent.getInstance().getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					      
				query.setFirstResult(i*3000);
				query.setMaxResults(3000);
				list = query.list();
				if(ex == null){
					ex = new CustomExportExcel(headers, filedNames, customMap, list, out);
				}else{
					ex.addData(list);
				}
			}
			book = ex.exportExcel();
			ex.writeFile(book,file);
	         
			out.close();
		
			//System.out.println("excel导出成功！");
			query = null;
			book = null;
			list= null;
			filePath = null;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return "./user/" + user.getUSER_ID() + "/result_custom.xls";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<String>> getTotalResult(String year, String month,
			int totalType) {
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		List<String> dayList = getDays(year, month);
		List<String> totalList = new ArrayList<String>();
		Query query = LoginContent.getInstance().getSession().createSQLQuery(getTotalSql(year, month, totalType)).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, Object>> results = query.list();
		String valueField = getValueField(year, month);
		if(!(results == null || results.isEmpty())){
			for (String day : dayList) {
				String totals = "0";
				for (HashMap<String, Object> hashMap : results) {
					if(day.equals(hashMap.get(valueField))){
						totals = hashMap.get("TOTALS").toString();
						break;
					}
				}
				totalList.add(totals);
			}
			List<String> maxTotalsList = new ArrayList<String>();
			maxTotalsList.add(getMaxTotals(results.get(0).get("MAX_TOTALS")));
			resultMap.put("max_totals", maxTotalsList);
		}
		resultMap.put("days", dayList);
		resultMap.put("totals", totalList);
		return resultMap;
	}
	
	/**
	 * 总载视图，获取统计SQL
	 * @author Lang
	 * @param year
	 * @param month
	 * @param totalType
	 * @return
	 */
	private String getTotalSql(String year, String month, int totalType){
		StringBuilder sb = new StringBuilder();
		if(totalType == 1){
			sb.append("select totals, date_yyyymm, date_yyyy, date_mm, date_dd, ");
			if(ObjUtil.isNotNull(year)){
				if(ObjUtil.isNotNull(month)){
					sb.append("max(totals) over (partition by date_yyyymm) max_totals from v_order_header_total_yyyymm where date_yyyymm = '");
					sb.append(year + month);
					sb.append("'");
				}else{
					sb.append("max(totals) over (partition by date_yyyy) max_totals from v_order_header_total_yyyy where date_yyyy = '");
					sb.append(year);
					sb.append("'");
				}
			}else{
				sb.append("max(totals) over (partition by date_yyyymm) max_totals from v_order_header_total");
			}
		}else if(totalType == 2){
			sb.append("select totals, date_yyyymm, date_yyyy, date_mm, date_dd, ");
			if(ObjUtil.isNotNull(year)){
				if(ObjUtil.isNotNull(month)){
					sb.append("max(totals) over (partition by date_yyyymm) max_totals from v_load_header_total_yyyymm where date_yyyymm = '");
					sb.append(year + month);
					sb.append("'");
				}else{
					sb.append("max(totals) over (partition by date_yyyy) max_totals from v_load_header_total_yyyy where date_yyyy = '");
					sb.append(year);
					sb.append("'");
				}
			}else{
				sb.append("max(totals) over (partition by date_mm) max_totals from v_load_header_total");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 总载视图， 获取横轴数据
	 * @author Lang
	 * @param year
	 * @param month
	 * @return
	 */
	private List<String> getDays(String year, String month){
		List<String> dayList = new ArrayList<String>();
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM");
	    Calendar rightNow = Calendar.getInstance();
		int days = 0;
		int i = 1;
		if(ObjUtil.isNotNull(year)){
			if(ObjUtil.isNotNull(month)){
			    try {
					rightNow.setTime(simpleDate.parse(year+"/"+month));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			    days = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);//根据年月 获取月份天数
			}else{
				days = 12;
			}
		}else{
			simpleDate = new SimpleDateFormat("yyyy");
			days = Integer.valueOf(simpleDate.format(new Date()));
			i = 2013;
		}
	    for (; i <= days; i++) {
	    	if(i > 9){
	    		dayList.add(String.valueOf(i));
	    	}else{
	    		dayList.add("0"+String.valueOf(i));
	    	}
		}
	    return dayList;
	}
	
	/**
	 * 总载视图， 获取统计结果最大值
	 * @author Lang
	 * @param maxTotals
	 * @return
	 */
	private String getMaxTotals(Object maxTotals){
		String maxTotal = maxTotals.toString();
		BigDecimal maxTotalBig = new BigDecimal(maxTotal);
		if(maxTotalBig.compareTo(BigDecimal.ZERO) == 0){
			return "0";
		}
		double b = maxTotal.length();
		BigDecimal divide = BigDecimal.valueOf(Math.pow(10, b));
		BigDecimal divideResult = maxTotalBig.divide(divide);
		BigDecimal result = divideResult.setScale(1, RoundingMode.CEILING);
		return String.valueOf(result.multiply(divide).intValue());
	}
	
	/**
	 * 总载视图， 获取值字段名
	 * @author Lang
	 * @param year
	 * @param month
	 * @return
	 */
	private String getValueField(String year, String month){
		if(ObjUtil.isNotNull(year)){
			if(ObjUtil.isNotNull(month)){
				return "DATE_DD";
			}else{
				return "DATE_MM";
			}
		}else{
			return "DATE_YYYY";
		}
	}
	
	/**
	 * 执行插入语句
	 * @author yuanlei
	 */
	@SuppressWarnings("unchecked")
	public String insertOrg(String json) {
		String result = StaticRef.FAILURE_CODE;
		String seq = "";
		try {
			ArrayList<String> sqlList = new ArrayList<String>();
			
			HttpServletRequest request = this.getThreadLocalRequest();
			HttpSession session =request.getSession();
			if(session.getAttribute("USER_ID") == null) {
				result += "用户缓存为空,请重新登录!";
			}
			SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
			seq = getIDSequence();
			Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
			HashMap map=new GsonBuilder().create().fromJson(json,mapType);
			map.put("ID", seq);
			map.put("ADDWHO",user.getUSER_ID());
			map.remove("EDITWHO");
			map.remove("EDITTIME");
			String sql = SUtil.getInsertSQL(map, "");
			sqlList.add(sql);
			
			StringBuffer sf = new StringBuffer();
			sf.append("insert into sys_user_org(id,user_id,org_id,default_flag,addtime,addwho,parent_org_id)");
			sf.append(" values(sys_guid(),'");
			sf.append(user.getUSER_ID());
			sf.append("','");
			sf.append(seq);
			sf.append("','N',sysdate,'");
			sf.append(user.getUSER_ID());
			sf.append("','");
			sf.append(map.get("PARENT_ORG_ID"));
			sf.append("')");
			sqlList.add(sf.toString());
			//Log4j.info(StaticRef.SQL_LOG, sql);
			result = excuteSQLList(sqlList);
			if(result.subSequence(0, 2).equals(StaticRef.SUCCESS_CODE)) {
				result += seq;
			}
			//SUtil.insertlog(log, result.equals(StaticRef.SUCCESS_CODE)?StaticRef.ACT_SUCCESS:StaticRef.ACT_FAILURE, user.getUSER_NAME(), user.getDEFAULT_ORG_ID_NAME());
		}
		catch(Exception e) {
			Log4j.error(StaticRef.ERROR_LOG, e.getMessage());
		}
		return result;
	}

    protected static String illegalFilter(String value) {
//    	if(value != null) {
//    		if(value.toLowerCase().indexOf("delete") >= 0 || value.toLowerCase().indexOf("drop") >= 0 || 
//    				value.toLowerCase().indexOf("insert") >= 0 || value.toLowerCase().indexOf("update") >= 0) {
//    			value = " and 1=2 ";
//    		}
//    		else {
//    			if(value.toLowerCase().indexOf("select") >= 0 && value.toLowerCase().indexOf("union") < 0 && value.toLowerCase().indexOf(" in ") < 0) {
//    				value = " and 1=2 ";
//    			}
//    		}
//    	}
         return value;
    }
     
    protected static String illegalFilter2(String value) {
    	if(value != null) {
    		if(value.toLowerCase().indexOf("delete") >= 0 || value.toLowerCase().indexOf("drop") >= 0|| 
    				value.toLowerCase().indexOf("insert") >= 0 || value.toLowerCase().indexOf("update") >= 0) {
    			value = " and 1=2 ";
    		}
    		else {
    			if(value.toLowerCase().indexOf("select") >= 0 && value.toUpperCase().indexOf("DUAL") >= 0) {
    				value = " and 1=2 ";
    			}
    		}
    	}
         return value;
    }
    
	@Override
	public ArrayList<String> readExcel(String tableName) {
	
	 try {
		Thread.sleep(500);
	} catch (InterruptedException e1){ 
		e1.printStackTrace();
	}
		
      ArrayList<String> cellList = new ArrayList<String>();
		
      //获得存放文件的物理路径 
      //upload下的某个文件夹 得到当前在线的用户 找到对应的文件夹 
	  ServletContext sctx = getServletContext(); 
		
	  String path = sctx.getRealPath("/upload"); 
		
	  //List<String> list=new ArrayList<String> ();
	  String fileName=tableName;
	  System.out.println(fileName);
	  if(fileName.indexOf("\\")>0){
		  fileName = fileName.substring(fileName.lastIndexOf("\\")+1); 
	  }
	  
	  File excel=new File(path+"\\"+fileName); 
	  
	  try { 

			InputStream is=new FileInputStream(excel);
			
			Workbook workbook = null;
			
			workbook = WorkbookFactory.create(is);
            
			Sheet sheet = workbook.getSheetAt(0);
			 
			Row row = sheet.getRow(0);
			
			for (int i = 0; i <row.getLastCellNum(); i++) {  
	                    
				Cell cell = row.getCell(i);
				
				cellList.add(cell.getRichStringCellValue().toString());
				//System.out.println(cell.getRichStringCellValue());
			}  
			//is.close();
			
	} 
	catch (Exception e) { 
		e.printStackTrace(); 
	}finally{
		excel.delete();
	}
	return cellList;
  }
	
	/**
	 * @author yuanlei
	 * 跟踪登录用户的角色获取对应的显示菜单
	 */
	@SuppressWarnings("unchecked")
	public List<FUNCTION> getFuncList(String role_id, String system) {
		Session session = LoginContent.getInstance().getSession();
		//HttpServletRequest request = this.getThreadLocalRequest();
		//HttpSession httpSession =request.getSession();
		//if(httpSession.getAttribute("USER_ID") == null) {
		//	return null;
		//}
		//SYS_USER user = (SYS_USER)httpSession.getAttribute("USER_ID");
		//String role_id = user.getROLE_ID();
		//String user_group = user.getUSERGRP_ID();
		String user_group = "";
		StringBuffer sf = new StringBuffer();
        sf.append("select t1.FUNCTION_ID,t1.FUNCTION_NAME,t1.PARENT_FUNCTION_ID,t1.FUNCTION_FORMNAME from SYS_FUNCTION t1");
        if(ObjUtil.isNotNull(role_id)) {
        	if(role_id.compareTo(StaticRef.SUPER_ROLE) != 0) {
        		sf.append(",SYS_ROLE_FUNC t2 where t1.FUNCTION_ID = t2.FUNCTION_ID and t1.ACTIVE_FLAG = 'Y' and t2.ROLE_ID = '");
        		sf.append(role_id);
        		sf.append("' and t1.subsystem_type = '");
        		sf.append(system);
        		sf.append("'");
        	}
        	else {
        		sf.append(" where t1.ACTIVE_FLAG = 'Y' and t1.subsystem_type = '");
        		sf.append(system);
        		sf.append("'");
        	}
        }
    	else {
    		sf.append(",SYS_ROLE_FUNC t2 where t1.FUNCTION_ID = t2.FUNCTION_ID and t1.ACTIVE_FLAG = 'Y' and t2.ROLE_ID in (");
    		sf.append(" select role_id from sys_usergrp_role where usergrp_ID = '");
    		sf.append(user_group);
    		sf.append("') and t1.subsystem_type = '");
    		sf.append(system);
    		sf.append("'");
    	}
        sf.append(" order by t1.SHOW_SEQUENCE asc");
        Query query = session.createSQLQuery(sf.toString()).addEntity(FUNCTION.class);
        List<FUNCTION> object = query.list();
		LoginContent.getInstance().closeSession();
		session = null;
		return object;
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
	public String sfOrderExportAction(String headers, String filedNames,
			String sql,String odr_no) {
		HttpServletResponse response = this.getThreadLocalResponse();			
	    response.setContentType("octets/stream");
	    response.setCharacterEncoding("UDF-8");
	    response.addHeader("Content-Disposition", "attachment;filename=result.xls");	      
	    HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session =request.getSession();
	    if(session.getAttribute("USER_ID") == null) {
			return "";
	    }
	    Query query = null;
	    SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
	    ExportExcel ex = new ExportExcel();
	    String path = getServletContext().getRealPath("/");
	    path = path + "user/" + user.getUSER_ID();  //不同用户建立不同文件夹，解决多线程数据对冲
	    File file = new File(path +  "/result.xls");
	    try {
	    	File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			if(!filePath.exists()){
				filePath.mkdirs();
			}
	    	if(!file.exists()){
	 	      file.createNewFile();
	 	    }
	    	OutputStream out = response.getOutputStream();
	        List list = null;
	        HSSFWorkbook book = null;
	         
	        String Sql1="select to_char(f.FROM_LOAD_TIME,'yyyy/mm/dd hh24:mi') as FROM_LOAD_TIME ,f.SKU_NAME,f.CUSTOM_ODR_NO,to_char(f.PRE_LOAD_TIME,'yyyy/mm/dd') as PRE_LOAD_TIME,f.UNLOAD_NAME,s.QNTY,s.QNTY_EACH,to_char(f.PRE_UNLOAD_TIME,'yyyy/mm/dd') as PRE_UNLOAD_TIME,f.NOTES"; 
	        Sql1=Sql1+" from sf_order_item s left join SF_ORDER_HEADER  f on  s.ODR_NO=f.ODR_NO where f.odr_no='"+odr_no+"'";
			     query = LoginContent.getInstance().getSession().createSQLQuery(Sql1)
			     .addScalar("CUSTOM_ODR_NO",Hibernate.STRING)
			     .addScalar("FROM_LOAD_TIME",Hibernate.STRING)
			     .addScalar("PRE_LOAD_TIME",Hibernate.STRING)
			     .addScalar("SKU_NAME",Hibernate.STRING)
			     .addScalar("UNLOAD_NAME",Hibernate.STRING)
			     .addScalar("QNTY",Hibernate.STRING)
			     .addScalar("QNTY_EACH",Hibernate.STRING)
			     .addScalar("PRE_UNLOAD_TIME",Hibernate.STRING)
			     .addScalar("NOTES",Hibernate.STRING)
			     .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			      
			     list = query.list();
			     book = ex.sfOrderExportExcel("sheet1", headers,filedNames,list, out,"yyyy-MM-dd HH:mm");

			     ex.writeFile(book,file);
			     out.close();
			     query = null;
			     book = null;
			     list= null;
			     filePath = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "./user/" + user.getUSER_ID() + "/result.xls";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean createBean(String className, String[] fields,
			String packagePath, String absolutelyPath, String Implements,
			String Extends) {
		
		CreateBean b=new CreateBean();
		
		try{
		
			b.tableToEntity(className, fields, packagePath, absolutelyPath, Implements, Extends);
		
			return true;
		
		}catch (Exception e){
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean createDS(ArrayList<HashMap<String, String>> list,
			HashMap<String, String> map) {
	    
		CreateDS ds=new CreateDS();
		try{
		
			ds.CreateDsXml(list, map);
		
			return true;
	
		}catch(Exception e){
		
			return false;
		
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean createHBM(ArrayList<HashMap<String, String>> list,
			HashMap<String, String> map) {
		CreateHBM hbm=new CreateHBM();
		try{
		
			hbm.CreateHbmXml(list, map);
		
			return true;
	
		}catch(Exception e){
		
			return false;
		
		}
	}
	
	@Override
	public ArrayList<Map<String,String>> getWeather(String cityName) {
		
		ArrayList<Map<String,String>> maps=new ArrayList<Map<String,String>>();
		StringBuffer strBuf;  	      
		String baiduUrl=""; 
		try {                              
	                                
			//通过浏览器直接访问http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ  
	                               
			//5slgyqGDENN7Sy7pw29IUvrZ 是我自己申请的一个AK(许可码)，如果访问不了，可以自己去申请一个新的ak  
	                                
			//百度ak申请地址：http://lbsyun.baidu.com/apiconsole/key  
	                                
			//要访问的地址URL，通过URLEncoder.encode()函数对于中文进行转码                              
	               
			baiduUrl = "http://api.map.baidu.com/telematics/v3/weather?location="+URLEncoder.encode(cityName, "utf-8")+"&output=json&ak=6tYzTvGZSOpYB5Oc2YGGOKt8";                    
	            
		} catch (UnsupportedEncodingException e1) {               
	               
			e1.printStackTrace();                     
			return null;  
		}  
	  	           
		strBuf = new StringBuffer();  
	   
		try{  
	               
			URL url = new URL(baiduUrl);  
	                
			URLConnection conn = url.openConnection();  
	                
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));//转码。  
	                
			String line = null;  

			while ((line = reader.readLine()) != null)  
				strBuf.append(line + " ");  
	                   
			reader.close();  
	
		}catch(MalformedURLException e) {  	                
			e.printStackTrace();   	  
			return null;
		}catch(IOException e){  	                
			e.printStackTrace();
			return null;
		}   
			
		try{
			
			JSONObject json = new JSONObject(strBuf.toString());
			
			System.out.println(json.toString());
			
			if(json.getString("error").equals("0")){
				
				String date=json.get("date").toString();
		
				System.out.println(date);
		
				System.out.println(json.getString("results"));
			        
				JSONArray results=json.getJSONArray("results");  
				  
				JSONObject results0=results.getJSONObject(0);  
				  
				String city =  results0.getString("currentCity");  
				
				String pm25 =  results0.getString("pm25");  
				
				JSONArray weathers=results0.getJSONArray("weather_data");   
	
				HashMap<String,String> map1=new HashMap<String, String>();
				
				map1.put("city", city);
				
				map1.put("pm25", pm25);
				
				maps.add(map1);
				
				for(int i=0;i<weathers.length();i++){
					HashMap<String,String> map=new HashMap<String, String>();
					  
					JSONObject weathers0=weathers.getJSONObject(i); 
					
					String dateTime= weathers0.getString("date");
					  
					String weather= weathers0.getString("weather");
					  
					String wind= weathers0.getString("wind");
					  
					String temperature=weathers0.getString("temperature");
					 
					//String data=date1+" "+weather1+" "+wind+" "+temperature;  
					 
					map.put("data", dateTime);
					map.put("weather", weather);
					map.put("wind", wind);
					map.put("temperature", temperature);				
					map.put("weatherImg",weathers0.getString("dayPictureUrl") );
					
					maps.add(map);
				 
				}
			}else{				
				return null;
			}
			
		}catch (Exception e){
			return null;
		}
		return maps;
	}
	
	/**
	 * 批量WEBSERVICE调用
	 * @author fanglm
	 * @param list 存储过程参数集合
	 * @param proName 存储过程名称  execPro(?,?,?,?)
	 * @return 操作成功/失败标识
	 */
	@Override
	public String execWebService(LinkedHashMap<String, String> map, String serviceName, String methodName) {
		String result = StaticRef.SUCCESS_CODE;
		try {
			String endpoint = "http://127.0.0.1:8083/BMSInterface/services/" + serviceName;
			org.apache.axis.client.Service service = new org.apache.axis.client.Service();   
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(endpoint);
			call.setOperationName(methodName);//WSDL里面描述的接口名称
			
			String[] strs = new String[map.size()];
			
			Object[] iter = map.keySet().toArray();
			String key = "", value = "";
			for(int i = 0; i < iter.length; i++) {
				key = (String)iter[i];
				value = map.get(key);
				call.addParameter(key, org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);//接口的参数
				strs[i] = value;
			}
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);//设置返回类型  
			 
	        result = (String)call.invoke(strs);
		}
		catch(Exception e) {
			result = StaticRef.FAILURE_CODE + e.getMessage();
		}
		return result;
	}

	@Override
	public String downLoadAction(String cusId) {
		//System.out.println("ddddddddddddddddddddd   "+cusId);
		return "";
	}
	
}